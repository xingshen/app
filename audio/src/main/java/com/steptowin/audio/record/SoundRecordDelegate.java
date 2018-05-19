package com.steptowin.audio.record;

/**
 * @Description 类描述：录音功能接口
 * @author cying
 * @date 创建日期 2015-8-10 下午2:05:35
 * @record 修改记录：
 */
public interface SoundRecordDelegate {

	/**
	 * 获得录音文件路径
	 * 
	 * @return
	 */
	String getSoundFilePath();

	/**
	 * 设置录音文件保存的路径
	 * 
	 * @param path
	 */
	void setSoundFilePath(String path);

	/**
	 * 开始录音
	 */
	void startRecord();

	/**
	 * 停止录音
	 */
	void stopRecord();

	/**
	 * 取消录音
	 */
	void cancelRecord();

	/**
	 * 当录音开始时的回调，在{@link #startRecord()}调用后会被调用
	 */
	void onRecordStart();

	/**
	 * 录音时间变化回调
	 * 
	 * @param timeInSecond
	 */
	void onRecordTimeUpdate(int timeInSecond);

	/**
	 * 音量显示回调
	 */
	void onRecordVolumeUpdate();

	/**
	 * 当录音停止时，当调用{@link #startRecord()}或{@link #stopRecord()}时会调用该回调
	 * 
	 * @param timeInSecond
	 * @param recordSuccess
	 */
	void onRecordStop(int timeInSecond, boolean recordSuccess);

	/**
	 * 设置最大录音时间，当录音超过这个时间值时会自动调用{@link #stopRecord()}停止录音
	 * 
	 * @param maxTimeInSecond
	 */
	void setMaxRecordTime(int maxTimeInSecond);

	/**
	 * 设置最小录音时间，当录音时间在最小时间之内时，会自动调用{@link #cancelRecord()}取消录音
	 * 
	 * @param minTimeInSecond
	 */
	void setMinRecordTime(int minTimeInSecond);

	/**
	 * 得到声音最大的振幅
	 * 
	 * @return
	 */
	double getAmplitude();

	/**
	 * 是否存在录音
	 * 
	 * @return
	 */
	boolean isRecording();

	/**
	 * 设置音量等级数
	 * @param count
     */
	void setVolumeLevelCount(int count);

	/**
	 * 获取当前音量大小等级
	 * @return
     */
	int getVolumeLevel();

}
