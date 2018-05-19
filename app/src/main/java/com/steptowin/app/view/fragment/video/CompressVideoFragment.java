package com.steptowin.app.view.fragment.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.netcompss.ffmpeg4android.FFmpegTool;
import com.steptowin.app.view.activity.PlayActivity;
import com.steptowin.app.view.activity.R;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.common.tool.UriTool;
import com.steptowin.common.tool.context.AppStorage;
import com.steptowin.core.common.callback.SimpleSubscriber;
import com.steptowin.core.common.thread.CachedThreadExecutor;
import com.steptowin.core.tools.FileTool;
import com.steptowin.core.tools.ToastTool;
import com.steptowin.core.tools.WLog;
import com.steptowin.minivideo.ffmpeg.Clip;
import com.steptowin.minivideo.ffmpeg.FFmpegController;
import com.steptowin.minivideo.shoot.WechatRecoderActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * desc:视频压缩
 * author：zg
 * date:16/11/13
 * time:下午4:14
 */
@Route(path = "/video/compress")
public class CompressVideoFragment extends BaseFragment {
    static final int RESULT_CODE_COMPRESS_VIDEO = 1000;
    static final int RESULT_CODE_SHOOT_VIDEO = 1001;

    @Bind(R.id.editText)
    EditText editText;
    Executor executor;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_compress_video;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        workFolder = AppStorage.getTmpPath();

        executor = CachedThreadExecutor.create();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        MediaScannerConnection.scanFile(activity, new String[]{path}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {
                getHoldingActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastTool.showLongToast(getHoldingActivity(), "扫描完啦");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    Subscription compressSubscription;
    /**
     * http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4
     * http://1255622107.vod2.myqcloud.com/cc7fcadevodgzp1255622107/a40a39ef7447398156074340927/m185kEmA898A.mp4
     */
    String videoUrl = "http://1255622107.vod2.myqcloud.com/cc7fcadevodgzp1255622107/a40a39ef7447398156074340927/m185kEmA898A.mp4";

    @OnClick({R.id.btnSelectVideo, R.id.btnCompressVideo, R.id.btnCompressVideoWithLogo
            , R.id.btn2play, R.id.btn2concatla, R.id.btnKillProcessor,
            R.id.btn2shoot, R.id.btn2transcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn2shoot:
                startActivityForResult(WechatRecoderActivity.launchActivity(getHoldingActivity(), 0), RESULT_CODE_SHOOT_VIDEO);
                break;
            case R.id.btnKillProcessor:
                if (null != compressSubscription)
                    compressSubscription.unsubscribe();
                FFmpegController.getInstance(getHoldingActivity()).killProcessor(new SimpleSubscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        ToastTool.showLongToast(getHoldingActivity(), "取消成功啦");
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastTool.showLongToast(getHoldingActivity(), "取消失败啦");
                    }
                });
                break;
            case R.id.btn2concatla:
                addFragment(new ConcatVideoFragment());
                break;
            case R.id.btn2play:
                try {
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(Uri.parse(videoUrl), "video/*");
                    startActivity(videoIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                startActivity(VideoPlayerActivity.getIntent(getHoldingActivity(),displayName));
                break;
            case R.id.btnSelectVideo:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, RESULT_CODE_COMPRESS_VIDEO);
                break;
            case R.id.btnCompressVideoWithLogo:
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        FFmpegTool.compressVideoWithLogo(getHoldingActivity(), displayName, compressedName, workFolder, logoPath, new FFmpegTool.CompressListener() {
                            @Override
                            public void onSuccess(String orginPath, String savePath) {
                                WLog.error("zhou","压缩成功");
                                ToastTool.showLongToast(getHoldingActivity(), "压缩成功");
                            }

                            @Override
                            public void onFail(final Throwable e) {
                                WLog.error("zhou","压缩失败");
                                ToastTool.showLongToast(getHoldingActivity(), "压缩失败：" + e);

                            }
                        });
                    }
                });
                break;
            case R.id.btnCompressVideo:

                compressSubscription = FFmpegController.getInstance(getHoldingActivity()).compressVideo(getHoldingActivity(), displayName, compressedName, new SimpleSubscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        MediaScannerConnection.scanFile(getHoldingActivity(), new String[]{compressedName}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                                WLog.error("zhou", "压缩成功");
                                ToastTool.showLongToast(getHoldingActivity(), "压缩成功");
                            }
                        });
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        WLog.error("zhou", "压缩失败");
                        ToastTool.showLongToast(getHoldingActivity(), "压缩失败：" + e);
                    }
                });

//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        FFmpegTool.compressVideo(getHoldingActivity(), displayName, compressedName, workFolder, new FFmpegTool.CompressListener() {
//                            @Override
//                            public void onSuccess(String orginPath, String savePath) {
//                                new MainThreadExecutor().execute(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ToastTool.showLongToast(getHoldingActivity(), "压缩成功");
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onFail(final Throwable e) {
//                                new MainThreadExecutor().execute(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ToastTool.showLongToast(getHoldingActivity(), "压缩失败：" + e);
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
                break;
            case R.id.btn2transcode:
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        FFmpegTool.transCodeVideo(getHoldingActivity(), displayName, workFolder, transCodeName, new FFmpegTool.CompressListener() {
                            @Override
                            public void onSuccess(String orginPath, String savePath) {
                                WLog.error("zhou","转码成功");
                                ToastTool.showLongToast(getHoldingActivity(), "转码成功");
                            }

                            @Override
                            public void onFail(final Throwable e) {
                                WLog.error("zhou","转码失败：" + e);
                                ToastTool.showLongToast(getHoldingActivity(), "转码失败：" + e);

                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    String workFolder = null;

    String displayName;

    String compressedName;
    String transCodeName;

    String logoPath = "/storage/emulated/0/";

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (resCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();

            if (reqCode == RESULT_CODE_COMPRESS_VIDEO) {
                if (uri != null) {

                    displayName = UriTool.getRealPathFromURI(getHoldingActivity(), uri);

//                    playVideo(displayName);

//                    addFragment(VideoPlayFragment.instance(displayName));


                    int potIndex = displayName.lastIndexOf(".");

                    compressedName = displayName.substring(0, potIndex) + "compressed" + displayName.substring(potIndex);
                    transCodeName = displayName.substring(0, potIndex) + "transcode.mp4";
//                    compressedName = displayName.replace(".mp4", "compressed.mp4");
//                    compressedName = displayName.replace(".ts", "compressed.ts");

                    editText.setText(displayName);

                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(displayName);
                    String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                    String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                    String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
                    logoPath = logoPath + "logo_" + rotation + ".png";
                    editText.setText(displayName + "   " + width + "*" + height + " " + rotation);

                }
            } else if (reqCode == RESULT_CODE_SHOOT_VIDEO) {
                String videoPath = data.getStringExtra(WechatRecoderActivity.VIDEO_PATH);

                play(videoPath);
            }
        }
    }

    private void play(String videoPath) {

        startActivity(new Intent(getHoldingActivity(), PlayActivity.class).putExtra("path", videoPath));
    }

    public static class ConcatVideoFragment extends BaseFragment {
        static final int RESULT_CODE_SELECT_VIDEO_FIRST = 1;
        static final int RESULT_CODE_SELECT_VIDEO_SECOND = 2;
        @Bind(R.id.editText_first)
        EditText firstET;
        @Bind(R.id.editText_second)
        EditText secondET;

        String firstVideo;
        String secondVideo;
        String concatedVideo;
        Executor executor;

        @Override
        public int getLayoutResId() {
            return R.layout.fragment_concat_video;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // TODO: inflate a fragment view
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            executor = CachedThreadExecutor.create();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }


        @OnClick({R.id.btnSelectFirstVideo, R.id.editText_first, R.id.btnSelectSecondVideo, R.id.editText_second, R.id.btn2concat})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnSelectFirstVideo:
                    Intent firstIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    firstIntent.setType("video/*");
                    startActivityForResult(firstIntent, RESULT_CODE_SELECT_VIDEO_FIRST);
                    break;

                case R.id.btnSelectSecondVideo:
                    Intent secondIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    secondIntent.setType("video/*");
                    startActivityForResult(secondIntent, RESULT_CODE_SELECT_VIDEO_SECOND);
                    break;

                case R.id.btn2concat:
                    final String tmpVideoFile = AppStorage.getTmpPath() + "videos.txt";
                    File tmpVideo = new File(tmpVideoFile);
                    if (tmpVideo.exists()) {
                        FileTool.deleteFile(tmpVideoFile);
                        try {
                            tmpVideo.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    concatVideo();

//                    if (!StringTool.isEmpty(firstVideo) && !StringTool.isEmpty(secondVideo)) {
//                        try {
//
//                            /**
//                             * 第一种合并方法
//                             */
//                            FileWriter fileWriter = new FileWriter(tmpVideo, true);
//                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//                            bufferedWriter.write("file " + firstVideo);
//                            bufferedWriter.write("\n");
//                            bufferedWriter.write("file " + secondVideo);
//                            bufferedWriter.close();
//
//                            /**
//                             * 第二种合并方法
//                             */
////                            tmpVideoFile = firstVideo+"|"+secondVideo;
//
//
//                            /**
//                             * 第三种合并方法
//                             * -i /storage/emulated/0/videokit/sample.mp4 -i /storage/emulated/0/videokit/in.mp4
//                             */
////                            tmpVideoFile = "-i "+firstVideo+" -i "+secondVideo;
//                            FFmpegTool.concatVideos(getHoldingActivity(), tmpVideoFile, AppStorage.getTmpPath() + "合并后.mp4", AppStorage.getTmpPath(), new FFmpegTool.ConcatListener() {
//                                @Override
//                                public void onSuccess(String savePath) {
////                                    FileTool.deleteFile(tmpVideoFile);
//                                    ToastTool.showLongToast(getHoldingActivity(), "合并成功");
//                                }
//
//                                @Override
//                                public void onFail(Throwable e) {
////                                    FileTool.deleteFile(tmpVideoFile);
//                                    ToastTool.showLongToast(getHoldingActivity(), "合并失败");
//                                }
//                            });
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

//                    }

                    break;
            }
        }

        private void concatVideo() {
            try {

                FFmpegController fc = FFmpegController.getInstance(getHoldingActivity());
                Clip in = new Clip();
                in.path = firstVideo;
                Clip in1 = new Clip();
                in1.path = secondVideo;
                ArrayList<Clip> videos = new ArrayList<>();
                videos.add(in);
                videos.add(in1);

                final Clip out = new Clip();
                out.path = AppStorage.getTmpPath() + "压缩后.mp4";
                fc.concatVideo(videos, out, new SimpleSubscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && data != null) {

                Uri uri = data.getData();

                switch (requestCode) {
                    case RESULT_CODE_SELECT_VIDEO_FIRST:
                        firstVideo = UriTool.getRealPathFromURI(getHoldingActivity(), uri);

                        firstET.setText(firstVideo);

                        MediaMetadataRetriever retr = new MediaMetadataRetriever();
                        retr.setDataSource(firstVideo);
                        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
                        firstET.setText(firstVideo + "   " + width + "*" + height + " " + rotation);
                        break;
                    case RESULT_CODE_SELECT_VIDEO_SECOND:
                        secondVideo = UriTool.getRealPathFromURI(getHoldingActivity(), uri);

                        secondET.setText(secondVideo);

                        MediaMetadataRetriever retr1 = new MediaMetadataRetriever();
                        retr1.setDataSource(secondVideo);
                        String height1 = retr1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                        String width1 = retr1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                        String rotation1 = retr1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
                        secondET.setText(secondVideo + "   " + width1 + "*" + height1 + " " + rotation1);
                        break;

                }
            }
        }
    }
}