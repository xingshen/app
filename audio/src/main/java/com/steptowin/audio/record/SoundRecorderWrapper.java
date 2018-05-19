package com.steptowin.audio.record;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import com.steptowin.audio.FileUtil;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cying
 * @Description 类描述：录音功能类,
 * 录音需要添加系统权限:android.permission.RECORD_AUDIO
 * @date 创建日期 2015-8-11 上午10:03:52
 * @record 修改记录：
 */
public abstract class SoundRecorderWrapper implements SoundRecordDelegate {

    private static int SAMPLE_RATE_IN_HZ = 8000;
    final int MSG_UPDATE_VOLUME_UI= 1;
    final int MSG_UPDATE_TIME_UI= 2;

    /**
     * 音量显示更新间隔时间，毫秒单位
     */
    final int UPDATE_VOLUME_INTERVAL_TIME = 500;

    private String mSoundFilePath;
    private MediaRecorder mRecorder;
    private AtomicInteger mCurrentRecordTime;
    private AtomicBoolean mIsRecording;

    private final Handler mHandler;
    private final Runnable mRecordTimeRunnable;

    private final Runnable mRecordStartRunnable;

    private int mMaxRecordTime;
    private int mMinRecordTime;

    private VolumeMeasure mVolumeMeasure;
    ExecutorService cachedExecutor ;

    public SoundRecorderWrapper() {

        cachedExecutor = Executors.newCachedThreadPool();
        mVolumeMeasure = new VolumeMeasure();
        this.mCurrentRecordTime = new AtomicInteger();
        this.mIsRecording = new AtomicBoolean(false);
        this.mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_UPDATE_VOLUME_UI:
                        onRecordVolumeUpdate();
                        break;
                    case MSG_UPDATE_TIME_UI:
                        onRecordTimeUpdate(mCurrentRecordTime.getAndIncrement());

                        // 时间超过最大时间自动完成
                        if (mMaxRecordTime > 0
                                && mCurrentRecordTime.get() >= mMaxRecordTime) {
                            stopRecord();
                        }
                        break;
                }
            }
        };

        mRecordTimeRunnable = new Runnable() {

            @Override
            public void run() {

                onRecordTimeUpdate(mCurrentRecordTime.getAndIncrement());
                mHandler.postDelayed(this, 1000);

                // 时间超过最大时间自动完成
                if (mMaxRecordTime > 0
                        && mCurrentRecordTime.get() >= mMaxRecordTime) {
                    stopRecord();
                }
            }
        };

        mRecordStartRunnable = new Runnable() {

            @Override
            public void run() {
                onRecordStart();
            }
        };

    }

    private MediaRecorder createRecorder(String path) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        recorder.setOutputFile(path);
        return recorder;

    }

    @Override
    public String getSoundFilePath() {
        return mSoundFilePath;
    }

    @Override
    public synchronized void setSoundFilePath(String path) {
        this.mSoundFilePath = path;
    }

    @Override
    public synchronized void startRecord() {
        try {

            if (mIsRecording.get()) {
                stopRecord();
            }

            if (!TextUtils.isEmpty(mSoundFilePath)) {
                mCurrentRecordTime.set(0);
                if (mRecorder == null) {
                    mRecorder = createRecorder(mSoundFilePath);

                    try {
                        mRecorder.prepare();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mRecorder.start();
                mIsRecording.set(true);
                mHandler.post(mRecordStartRunnable);

                // 开始计时
//                mHandler.post(mRecordTimeRunnable);

                cachedExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (mIsRecording.get()){
                            android.os.Message msg = new android.os.Message();
                            msg.what = MSG_UPDATE_TIME_UI;
                            mHandler.sendMessage(msg);
                            SystemClock.sleep(1000);
                        }
                    }
                });

                cachedExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (mIsRecording.get()){
                            android.os.Message msg = new android.os.Message();
                            msg.what = MSG_UPDATE_VOLUME_UI;
                            mHandler.sendMessage(msg);
                            SystemClock.sleep(100);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void stopRecord() {
        final int recordTime = mCurrentRecordTime.get();

        // boolean b=mMinRecordTime>0&&recordTime<=mMinRecordTime;
        // 时间过短删除文件
        stop(mMinRecordTime <= 0 || recordTime > mMinRecordTime);

    }

    private void stop(boolean isSuccess) {
        try {
            if (mRecorder != null && mIsRecording.get()) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                mIsRecording.set(false);
            }

            final int time = mCurrentRecordTime.get();
            mCurrentRecordTime.set(0);
            mHandler.removeCallbacks(mRecordTimeRunnable);
            if (isSuccess) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        onRecordStop(time, true);
                    }
                });
            } else {
                FileUtil.deleteFile(mSoundFilePath);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        onRecordStop(time, false);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMaxRecordTime(int maxTimeInSecond) {
        this.mMaxRecordTime = maxTimeInSecond;
    }

    @Override
    public void setMinRecordTime(int minTimeInSecond) {
        this.mMinRecordTime = minTimeInSecond;
    }

    @Override
    public double getAmplitude() {
        if (mRecorder != null) {
            return (mRecorder.getMaxAmplitude()); // 得到最大的振幅
        } else
            return 0;
    }

    @Override
    public synchronized void cancelRecord() {
        stop(false);
    }

    public int getRecordTimeInSecond() {
        return mCurrentRecordTime.get();
    }

    @Override
    public boolean isRecording() {
        return mIsRecording.get();
    }


    @Override
    public void setVolumeLevelCount(int volumeLevelCount) {
        mVolumeMeasure.setVolumeLevelCount(volumeLevelCount);
    }


    @Override
    public int getVolumeLevel() {
        return mVolumeMeasure.getVolumeLevel(getAmplitude());
    }
}
