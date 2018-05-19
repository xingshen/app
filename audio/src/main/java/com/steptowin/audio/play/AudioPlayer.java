package com.steptowin.audio.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * desc: 音频播放简单实现,支持本地文件和在线播放(无缓存)
 * 只允许同时播放一个音频
 * author：zg
 * date:16/6/17
 * time:下午5:19
 */
public abstract class AudioPlayer implements AudioPlayerDelegate {
    static final int MSG_UPDATE_PLAYING_TIME = 1;
    static final int MSG_START = 2;
    static final int MSG_PREPARE = 3;
    static final int MSG_PLAY = 4;
    static final int MSG_PAUSE = 5;
    static final int MSG_STOP = 6;
    static final int MSG_DESTROY = 7;

    public enum State{
        IDLE,PREPARING,PLAYING,PAUSING
    }

    private MediaPlayer mediaPlayer;
    private State state = State.IDLE;
    private AtomicInteger mCurrentRecordTime;

    private final Handler handler;
    ExecutorService cachedExecutor;
    Future playTimeFuture;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public AudioPlayer() {
        cachedExecutor = Executors.newCachedThreadPool();
        mediaPlayer = new MediaPlayer();
        mCurrentRecordTime = new AtomicInteger();
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_PLAYING_TIME:
                        onPlayingTimeUpdate(mCurrentRecordTime.incrementAndGet());
                        break;
                    case MSG_START:
                        onStart();
                        break;
                    case MSG_PREPARE:
                        onPrepare();
                        break;
                    case MSG_PLAY:
                        onPlay();
                        break;
                    case MSG_PAUSE:
                        onPause();
                        break;
                    case MSG_STOP:
                        onStop();
                        break;
                    case MSG_DESTROY:
                        onDestroy();
                        break;
                }
            }
        };

    }

    @Override
    public void start(String path) {
        if (getState() == State.IDLE) {
            handler.sendEmptyMessage(MSG_START);
            onBeforePrepare();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
                setState(State.PREPARING);
                handler.sendEmptyMessage(MSG_PREPARE);
            } catch (Exception e) {
                onFail(e);
                setState(State.IDLE);
            }
        }
    }

    @Override
    public void onPrepare() {

    }

    private void onBeforePrepare() {
        if (null == mediaPlayer)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                setState(State.IDLE);
                onComplete();
                if (null != mediaPlayer) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Log.i("info", "zhou----onBufferingUpdate----i=" + i);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                onAfterPrepared(mediaPlayer);
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void onAfterPrepared(MediaPlayer mediaPlayer) {
        setState(State.PLAYING);
        mediaPlayer.start();
        onAfterStart();
    }

    private void onAfterStart() {
        playTimeFuture = cachedExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                while (true) {
                    switch (getState()) {
                        case PLAYING:
                            android.os.Message msg = new android.os.Message();
                            msg.what = MSG_UPDATE_PLAYING_TIME;
                            handler.sendMessage(msg);
                            SystemClock.sleep(1000);
                            break;
                        case PAUSING:
                            lock.lock();
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                lock.unlock();
                            }
                            break;
                        case IDLE:
                            return;
                        default:
                            return;
                    }
                }
            }
        });
    }

    @Override
    public void play() {
        setState(State.PLAYING);
        handler.sendEmptyMessage(MSG_PLAY);
        if (null != mediaPlayer) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void pause() {
        setState(State.PAUSING);
        handler.sendEmptyMessage(MSG_PAUSE);
        if (null != mediaPlayer) {
            mediaPlayer.pause();
        }
    }

    private void setState(State state) {
        lock.lock();
        try {
            this.state = state;
            switch (state) {
                case PLAYING:
                    condition.signal();
                    break;
                case IDLE:
                    mCurrentRecordTime.set(0);
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop() {
        setState(State.IDLE);
        handler.sendEmptyMessage(MSG_STOP);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    @Override
    public void destroy() {
        setState(State.IDLE);
        handler.sendEmptyMessage(MSG_DESTROY);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void onFail(Exception e) {

    }

    @Override
    public void onPlayingTimeUpdate(int timeInSecond) {

    }
}
