package com.steptowin.app.view.audio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.steptowin.app.view.activity.R;
import com.steptowin.audio.play.AudioPlayer;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.tools.ToastTool;
import com.steptowin.core.tools.WLog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

/**
 * desc:
 * author：zg
 * date:16/6/17
 * time:下午6:30
 */
public class AudioFragment extends BaseFragment {
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.btn_start)
    Button btnStart;
    @Bind(R.id.btn_stop)
    Button btnStop;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.et_sound_path)
    EditText et_sound_path;

    /**
     * /storage/ext_sd/My Documents/My Recordings/语音0005.aac
     * /storage/emulated/0/Sounds/语音 022.m4a
     * http://sc1.111ttt.com/2016/3/06/19/199192330123.mp3
     * http://sc1.111ttt.com/2016/1/06/20/199201045559.mp3
     * http://weixue.steptowin.com:8000/data/doc/20160620/ca2m_vel1e2ct6mui.mp3
     */
    String soundPath = "http://sc1.111ttt.com/2016/1/06/20/199201045559.mp3";

    AudioPlayer audioPlayer;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_audio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
//        try {
//            soundPath = "http://"+ URLEncoder.encode("sc1.111ttt.com/2016/3/06/19/199192330123.mp3","utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        audioPlayer = new AudioPlayer() {
            @Override
            public void onStart() {
                tvHint.setText("开始播放");
            }

            @Override
            public void onPrepare() {
                tvHint.setText("正在缓冲...");
            }

            @Override
            public void onPause() {
                tvHint.setText("已暂停");
            }

            @Override
            public void onStop() {
                super.onStop();
                tvHint.setText("已停止");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                tvHint.setText("已结束");
            }

            @Override
            public void onFail(Exception e) {
                WLog.debug("zhou", "onFail----e:" + e);
                tvHint.setText("播放失败");
            }

            @Override
            public void onPlayingTimeUpdate(int timeInSecond) {
                super.onPlayingTimeUpdate(timeInSecond);
                tvHint.setText("正在播放:" + timeInSecond);
            }
        };

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        audioPlayer.destroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_pause, R.id.btn_convert})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                audioPlayer.start(soundPath);
                if (audioPlayer.getState() == AudioPlayer.State.PAUSING)
                    audioPlayer.play();
                else
                    audioPlayer.start(soundPath);
                break;
            case R.id.btn_stop:
                audioPlayer.stop();
                break;
            case R.id.btn_pause:
                audioPlayer.pause();
                break;
            case R.id.btn_convert:
                audioConvert();
                break;
            default:
                break;
        }
    }


    private void audioConvert() {
        /**
         * * /storage/ext_sd/My Documents/My Recordings/语音0005.aac
         * /storage/emulated/0/Recordings/20180508_201912.m4a
         */
        String soundPath = "/storage/emulated/0/Recordings/20180508_201912.m4a";
        File flacFile = new File(soundPath);
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                // So fast? Love it!
                WLog.error("zhou", "转码后的地址：" + convertedFile.getAbsolutePath());
                ToastTool.showLongToast(getHoldingActivity(), "转码完成");
            }

            @Override
            public void onFailure(Exception error) {
                // Oops! Something went wrong
                WLog.error("zhou", "转码失败：" + error.getMessage());
            }
        };
        AndroidAudioConverter.with(getHoldingActivity())
                // Your current audio file
                .setFile(flacFile)

                // Your desired audio format
                .setFormat(AudioFormat.MP3)

                // An callback to know when conversion is finished
                .setCallback(callback)

                // Start conversion
                .convert();
    }
}
