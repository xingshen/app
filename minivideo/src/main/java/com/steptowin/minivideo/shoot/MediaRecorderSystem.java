package com.steptowin.minivideo.shoot;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;

import com.steptowin.minivideo.shoot.model.MediaObject;

import java.io.IOException;

/**
 * 使用系统MediaRecorder录制，适合低端机
 * 
 * @author yixia.com
 *
 */
public class MediaRecorderSystem extends MediaRecorderBase implements MediaRecorder.OnErrorListener {

	/** 系统MediaRecorder对象 */
	private MediaRecorder mMediaRecorder;

	public MediaRecorderSystem() {

	}

	/** 开始录制 */
	@Override
	public MediaObject.MediaPart startRecord() {
		if (mMediaObject != null && mSurfaceHolder != null && !mRecording) {
			MediaObject.MediaPart result = mMediaObject.buildMediaPart(mCameraId, ".mp4");

			try {
				if (mMediaRecorder == null) {
					mMediaRecorder = new MediaRecorder();
					mMediaRecorder.setOnErrorListener(this);
				} else {
					mMediaRecorder.reset();
				}

				// Step 1: Unlock and set camera to MediaRecorder
				camera.unlock();
				mMediaRecorder.setCamera(camera);
				mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

				// Step 2: Set sources
				mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//before setOutputFormat()
				mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//before setOutputFormat()

				mMediaRecorder.setOrientationHint(90);

				mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

				//设置视频输出的格式和编码
				CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
				//                mMediaRecorder.setProfile(mProfile);
				mMediaRecorder.setVideoSize(640, 480);//after setVideoSource(),after setOutFormat()
//				mMediaRecorder.setVideoSize(480, 480);
				mMediaRecorder.setAudioEncodingBitRate(44100);
				if (mProfile.videoBitRate > 2 * 1024 * 1024)
					mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
				else
					mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
				mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);//after setVideoSource(),after setOutFormat()

				mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//after setOutputFormat()
				mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//after setOutputFormat()

				//mMediaRecorder.setVideoEncodingBitRate(800);

				// Step 4: Set output file
				mMediaRecorder.setOutputFile(result.mediaPath);
				Log.e("zhou", "result.mediaPath=" + result.mediaPath);
				// Step 5: Set the preview output
				//				mMediaRecorder.setOrientationHint(90);//加了HTC的手机会有问题

				Log.e("Yixia", "OutputFile:" + result.mediaPath);

				mMediaRecorder.prepare();
				mMediaRecorder.start();
				mRecording = true;
				return result;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				Log.e("Yixia", "startRecord", e);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("Yixia", "startRecord", e);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Yixia", "startRecord", e);
			}
		}
		return null;
	}

	/** 停止录制 */
	@Override
	public void stopRecord() {
		long endTime = System.currentTimeMillis();
		if (mMediaRecorder != null) {
			//设置后不会崩
			mMediaRecorder.setOnErrorListener(null);
			mMediaRecorder.setPreviewDisplay(null);
			try {
				mMediaRecorder.stop();
			} catch (IllegalStateException e) {
				Log.w("Yixia", "stopRecord", e);
			} catch (RuntimeException e) {
				Log.w("Yixia", "stopRecord", e);
			} catch (Exception e) {
				Log.w("Yixia", "stopRecord", e);
			}
		}

		if (camera != null) {
			try {
				camera.lock();
			} catch (RuntimeException e) {
				Log.e("Yixia", "stopRecord", e);
			}
		}

		// 判断数据是否处理完，处理完了关闭输出流
		if (mMediaObject != null) {
			MediaObject.MediaPart part = mMediaObject.getCurrentPart();
			if (part != null && part.recording) {
				part.recording = false;
				part.endTime = endTime;
				part.duration = (int) (part.endTime - part.startTime);
				part.cutStartTime = 0;
				part.cutEndTime = part.duration;
			}
		}
		mRecording = false;
	}

	/** 释放资源 */
	@Override
	public void release() {
		super.release();
		if (mMediaRecorder != null) {
			mMediaRecorder.setOnErrorListener(null);
			try {
				mMediaRecorder.release();
			} catch (IllegalStateException e) {
				Log.w("Yixia", "stopRecord", e);
			} catch (Exception e) {
				Log.w("Yixia", "stopRecord", e);
			}
		}
		mMediaRecorder = null;
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		try {
			if (mr != null)
				mr.reset();
		} catch (IllegalStateException e) {
			Log.w("Yixia", "stopRecord", e);
		} catch (Exception e) {
			Log.w("Yixia", "stopRecord", e);
		}
		if (mOnErrorListener != null)
			mOnErrorListener.onVideoError(what, extra);
	}

	/** 不需要视频数据回调 */
	@Override
	protected void setPreviewCallback() {
		//super.setPreviewCallback();
	}
}
