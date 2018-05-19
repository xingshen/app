package com.steptowin.audio.record;

/**
 * desc:音量测量
 * author：zg
 * date:2016/4/30 0030
 * time:下午 4:08
 */
public class VolumeMeasure {
    int volumeLevelCount = 10;
    final double maxVloumn = 32767;//最大音量

    public VolumeMeasure(){
    }

    public void setVolumeLevelCount(int volumeLevelCount){
        this.volumeLevelCount = volumeLevelCount;
    }

    public int getVolumeLevel(double amplitude) {
        return (int)(amplitude*volumeLevelCount/maxVloumn);
    }
}
