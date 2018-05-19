package com.steptowin.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * desc:视频播放页面
 * author：zg
 * date:16/12/2
 * time:下午3:53
 */
public class VideoPlayerActivity extends FragmentActivity {

    public static final String KEY_VIDEO_PATH = "video_path";


    public static final int VIDEO_PLAY_PROGRESS = 1;
    public static final int VIDEO_PLAY_OVER = 2;


    VideoView videoView;

    TextView tvProgress;

    SeekBar sbProgress;

    Button btnPlay;

    /**
     * 视频本地路径或者url
     * http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4
     */
    String videoPath;
    int currentPos;

    String videoUrl = "http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4";


    Handler playHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_PLAY_PROGRESS:
                    currentPos = videoView.getCurrentPosition();
                    int pos = Math.round(videoView.getCurrentPosition() * 1.0f / 1000);
                    tvProgress.setText(pos + "");
//                        Log.e("zhou","pos="+pos);
//                        Log.e("zhou","videoView.getDuration()="+videoView.getDuration());
                    sbProgress.setProgress(videoView.getCurrentPosition());
                    sendEmptyMessageDelayed(VIDEO_PLAY_PROGRESS, 1 * 1000);
                    break;
                case VIDEO_PLAY_OVER:
                    btnPlay.setText("开始");
//                        removeMessages(VIDEO_PLAY_PROGRESS);
                    break;
            }
        }
    };

    public static Intent getIntent(Context context, String path) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(KEY_VIDEO_PATH, path);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_playing);

        initView();
        initData();
        initVideo();
        if (null != getLastCustomNonConfigurationInstance())
            currentPos = (int) getLastCustomNonConfigurationInstance();
        if (currentPos > 0 && currentPos < videoView.getDuration()) {
            videoView.seekTo(currentPos);
            videoView.start();
            btnPlay.setText("暂停");
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return currentPos;
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.vv_test1);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        sbProgress = (SeekBar) findViewById(R.id.sb_progress);
        btnPlay = (Button) findViewById(R.id.btn_play);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                    btnPlay.setText("暂停");
                } else {
                    videoView.pause();
                    btnPlay.setText("开始");
                }
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b && videoView.isPlaying()){
                    currentPos = i;
                    int pos = Math.round(i * 1.0f / 1000);
                    tvProgress.setText(pos + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(currentPos);
            }
        });
    }

    private void initData() {
        videoPath = getIntent().getStringExtra(KEY_VIDEO_PATH);
    }

    private void initVideo() {
        if (null == videoPath || "".equals(videoPath)) {
            return;
        }
        prepareVideo(videoPath);
    }


    @Override
    protected void onDestroy() {
        stopVideo();
        super.onDestroy();
    }

    private void prepareVideo(String path) {
        if (videoView.isPlaying())
            videoView.stopPlayback();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                sbProgress.setMax(mediaPlayer.getDuration());
                playHandler.sendEmptyMessage(VIDEO_PLAY_PROGRESS);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playHandler.sendEmptyMessage(VIDEO_PLAY_OVER);
            }
        });
        videoView.setVideoPath(path);
    }

    private void stopVideo() {
        playHandler.removeMessages(VIDEO_PLAY_PROGRESS);
        playHandler.removeMessages(VIDEO_PLAY_OVER);
        if (videoView.isPlaying())
            videoView.stopPlayback();
    }
}
