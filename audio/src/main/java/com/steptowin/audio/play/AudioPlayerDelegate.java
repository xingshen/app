package com.steptowin.audio.play;

/**
 * desc:音频播放代理接口
 * author：zg
 * date:16/6/17
 * time:下午2:49
 */
public interface AudioPlayerDelegate {

    /**
     * 开始播放
     * @param path
     */
    void start(String path);
    void onStart();
    void onPrepare();

    /**
     * 暂停后播放
     */
    void play();
    void onPlay();

    /**
     * 暂停
     */
    void pause();
    void onPause();

    /**
     * 停止
     */
    void stop();
    void onStop();

    /**
     * 销毁(页面关闭时用)
     */
    void destroy();
    void onDestroy();

    /**
     * 完成
     */
    void onComplete();

    void onFail(Exception e);

    /**
     * 获取当前播放状态
     * @return
     */
    AudioPlayer.State getState();

    /**
     * 播放时间变化回调
     *
     * @param timeInSecond
     */
    void onPlayingTimeUpdate(int timeInSecond);


}
