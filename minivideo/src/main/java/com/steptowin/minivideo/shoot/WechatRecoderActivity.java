package com.steptowin.minivideo.shoot;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.steptowin.core.common.callback.SimpleSubscriber;
import com.steptowin.core.tools.AppStorage;
import com.steptowin.minivideo.R;
import com.steptowin.minivideo.ffmpeg.FFmpegController;
import com.steptowin.minivideo.shoot.common.CommonCons;
import com.steptowin.minivideo.shoot.common.OnDialogListener;
import com.steptowin.minivideo.shoot.common.RecoderAttrs;
import com.steptowin.minivideo.shoot.model.MediaObject;
import com.steptowin.minivideo.shoot.util.ConvertToUtils;
import com.steptowin.minivideo.shoot.util.DeviceUtils;
import com.steptowin.minivideo.shoot.util.FileUtils;
import com.steptowin.minivideo.shoot.widget.AspectRatioSurfaceView;
import com.steptowin.minivideo.shoot.widget.CircleBackgroundTextView;
import com.steptowin.minivideo.shoot.widget.RecoderProgress;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author maimingliang@gmail.com
 * <p>
 * Created by maimingliang on 2016/9/25.
 */
public class WechatRecoderActivity extends BaseActivity implements MediaRecorderBase.OnErrorListener, MediaRecorderBase.OnEncodeListener {


    public static final String VIDEO_PATH = "video_path";

    /**
     * 录制最长时间
     */
    public static int RECORD_TIME_MAX = 6 * 10 * 1000;
    /**
     * 录制最小时间
     */
    public static int RECORD_TIME_MIN = 2 * 1000;

    /**
     * 按住拍偏移距离
     */
    private static float OFFSET_DRUTION = 25.0f;

    /**
     * titel_bar 取消颜色
     */
    private static int TITEL_BAR_CANCEL_TEXT_COLOR = 0xFF00FF00;


    /**
     * 按住拍 字体颜色
     */
    private static int PRESS_BTN_COLOR = 0xFF00FF00;

    /**
     * progress 小于录制最少时间的颜色
     */
    private static int LOW_MIN_TIME_PROGRESS_COLOR = 0xFFFC2828;
    /**
     * progress 颜色
     */
    private static int PROGRESS_COLOR = 0xFF00FF00;

    private static int PRESS_BTN_BG = 0xFF00FF00;

    /**
     * 对焦图片宽度
     */
    private int mFocusWidth;
    /**
     * 底部背景色
     */
    private int mBackgroundColorNormal, mBackgroundColorPress;
    /**
     * 屏幕宽度
     */
    private int mWindowWidth;

    /**
     * SDK视频录制对象
     */
    private MediaRecorderBase mMediaRecorder;
    /**
     * 视频信息
     */
    private MediaObject mMediaObject;

    private MediaObject.MediaPart part;

    /**
     * 对焦动画
     */
    private Animation mFocusAnimation;
    private boolean mCreated;


    private boolean isCancelRecoder;
    private boolean isRecoder;

    private Handler mHandler = new Handler();


    private static OnDialogListener mOnDialogListener;

    TextView mTvRecorderCancel;
    TextView mTvSelectVideo;
    RelativeLayout mLayoutHeader;
    AspectRatioSurfaceView mSurfaceView;
    ImageView mImgRecordFocusing;
    RelativeLayout mRlRecoderSurfaceview;
    TextView mTvRecoderTips;
    RecoderProgress mRecorderProgress;
    CircleBackgroundTextView mBtnPress;
    RelativeLayout mRlRecorderBottom;
    LinearLayout mRlBottomRecoder;
    TextView tvOrientationTipLeft;
    TextView tvOrientationTipRight;
    TextView tvOrientationTipBottom;

    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCreated = false;
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        setContentView(R.layout.activity_wechat_recoder);
        initCustomerAttrs();
        initView();
        initData();
        mCreated = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(orientationListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
//        UtilityAdapter.freeFilterParser();
//        UtilityAdapter.initFilterParser();

        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mMediaRecorder.prepare();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(orientationListener);
        stopRecord();
//        UtilityAdapter.freeFilterParser();
        if (mMediaRecorder != null)
            mMediaRecorder.release();

    }

    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
//        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder = new MediaRecorderSystem();

        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        VCamera.setVideoCachePath(AppStorage.getTmpPath());
        File f = new File(VCamera.getVideoCachePath());
        if (!FileUtils.checkFile(f)) {
            f.mkdirs();
        }
        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key,
                VCamera.getVideoCachePath() + key);
        mMediaRecorder.setScreenSize(new Point(DeviceUtils.getScreenWidth(this), DeviceUtils.getScreenHeight(this)));
        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.prepare();
    }

    /**
     * 手动对焦
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean checkCameraFocus(MotionEvent event) {
        mImgRecordFocusing.setVisibility(View.GONE);
        float x = event.getX();
        float y = event.getY();
        float touchMajor = event.getTouchMajor();
        float touchMinor = event.getTouchMinor();

        Rect touchRect = new Rect((int) (x - touchMajor / 2),
                (int) (y - touchMinor / 2), (int) (x + touchMajor / 2),
                (int) (y + touchMinor / 2));
        // The direction is relative to the sensor orientation, that is, what
        // the sensor sees. The direction is not affected by the rotation or
        // mirroring of setDisplayOrientation(int). Coordinates of the rectangle
        // range from -1000 to 1000. (-1000, -1000) is the upper left point.
        // (1000, 1000) is the lower right point. The width and height of focus
        // areas cannot be 0 or negative.
        // No matter what the zoom level is, (-1000,-1000) represents the top of
        // the currently visible camera frame
        if (touchRect.right > 1000)
            touchRect.right = 1000;
        if (touchRect.bottom > 1000)
            touchRect.bottom = 1000;
        if (touchRect.left < 0)
            touchRect.left = 0;
        if (touchRect.right < 0)
            touchRect.right = 0;

        if (touchRect.left >= touchRect.right
                || touchRect.top >= touchRect.bottom)
            return false;

        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(new Camera.Area(touchRect, 1000));
        if (!mMediaRecorder.manualFocus(new Camera.AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                // if (success) {
                mImgRecordFocusing.setVisibility(View.GONE);
                // }
            }
        }, focusAreas)) {
            mImgRecordFocusing.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mImgRecordFocusing
                .getLayoutParams();
        int left = touchRect.left - (mFocusWidth / 2);// (int) x -
        // (focusingImage.getWidth()
        // / 2);
        int top = touchRect.top - (mFocusWidth / 2);// (int) y -
        // (focusingImage.getHeight()
        // / 2);
        if (left < 0)
            left = 0;
        else if (left + mFocusWidth > mWindowWidth)
            left = mWindowWidth - mFocusWidth;
        if (top + mFocusWidth > mWindowWidth)
            top = mWindowWidth - mFocusWidth;

        lp.leftMargin = left;
        lp.topMargin = top;
        mImgRecordFocusing.setLayoutParams(lp);
        mImgRecordFocusing.setVisibility(View.VISIBLE);

        if (mFocusAnimation == null)
            mFocusAnimation = AnimationUtils.loadAnimation(this,
                    R.anim.record_focus);

        mImgRecordFocusing.startAnimation(mFocusAnimation);

//		mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);// 最多3.5秒也要消失
        return true;
    }


    private void initCustomerAttrs() {


        int maxTime = getIntent().getIntExtra(CommonCons.RECORD_TIME_MAX, 0);

        if (maxTime != 0) {
            RECORD_TIME_MAX = maxTime;
        }

        int minTime = getIntent().getIntExtra(CommonCons.RECORD_TIME_MIN, 0);

        if (minTime != 0) {
            RECORD_TIME_MIN = minTime;
        }

        int offset = getIntent().getIntExtra(CommonCons.OFFSET_DRUTION, 0);

        if (offset != 0) {
            OFFSET_DRUTION = offset;
        }

        int cancelColor = getIntent().getIntExtra(CommonCons.TITEL_BAR_CANCEL_TEXT_COLOR, 0);

        if (cancelColor != 0) {
            TITEL_BAR_CANCEL_TEXT_COLOR = cancelColor;
        }

        int btnColor = getIntent().getIntExtra(CommonCons.PRESS_BTN_COLOR, 0);

        if (btnColor != 0) {
            PRESS_BTN_COLOR = btnColor;
        }

        int minTimeProgressColor = getIntent().getIntExtra(CommonCons.LOW_MIN_TIME_PROGRESS_COLOR, 0);

        if (minTimeProgressColor != 0) {
            LOW_MIN_TIME_PROGRESS_COLOR = minTimeProgressColor;
        }
        int color = getIntent().getIntExtra(CommonCons.PROGRESS_COLOR, 0);


        if (color != 0) {
            PROGRESS_COLOR = color;
        }

        int pressbg = getIntent().getIntExtra(CommonCons.PRESS_BTN_BG, 0);

        if (pressbg != 0) {
            PRESS_BTN_BG = pressbg;
        }

    }


    private void initData() {
        mWindowWidth = DeviceUtils.getScreenWidth(this);

        mFocusWidth = ConvertToUtils.dipToPX(this, 64);
//        mBackgroundColorNormal = getResources().getColor(R.color.black);// camera_bottom_bg
//        mBackgroundColorPress = getResources().getColor(
//                R.color.camera_bottom_press_bg);
        try {
            mImgRecordFocusing.setImageResource(R.drawable.ms_video_focus_icon);
        } catch (OutOfMemoryError e) {
            Log.e("maiml", e.getMessage());
        }

        mTvRecorderCancel.setTextColor(TITEL_BAR_CANCEL_TEXT_COLOR);
        mBtnPress.setTextColor(PRESS_BTN_COLOR);
        mBtnPress.setCircleColor(PRESS_BTN_BG);


        mRecorderProgress.setMaxTime(RECORD_TIME_MAX);
        mRecorderProgress.setMinRecordertime(RECORD_TIME_MIN);
        mRecorderProgress.setLowMinTimeProgressColor(LOW_MIN_TIME_PROGRESS_COLOR);
        mRecorderProgress.setProgressColor(PROGRESS_COLOR);

        mRecorderProgress.setProgressListener(new RecoderProgress.ProgressListener() {
            @Override
            public void onProgress(float time) {
                Log.e("zhou", "time=" + time);
            }

            @Override
            public void onComplete() {
                mBtnPress.performClick();
            }
        });
//        initSurfaceView();
        setListener();
    }


    private void startRecoder() {


        isCancelRecoder = false;
        mTvRecoderTips.setText("上滑取消录制");
        mTvRecoderTips.setTextColor(getResources().getColor(R.color.white));
        mTvRecoderTips.setBackgroundColor(getResources().getColor(R.color.transparent));
        mTvRecoderTips.setVisibility(View.VISIBLE);
        if (mMediaRecorder == null) {
            return;
        }
        part = mMediaRecorder.startRecord();
        if (part == null) {
            return;
        }
        this.mRecorderProgress.startAnimation();
        isRecoder = true;

    }


    private void startEncoding() {
        mMediaRecorder.startEncoding();
    }


    private void stopAll() {

        mTvRecoderTips.setVisibility(View.INVISIBLE);
        mRecorderProgress.stopAnimation();
        stopRecord();
        isRecoder = false;


    }

    private void releaseCancelRecoder() {


        isCancelRecoder = true;
        mTvRecoderTips.setTextColor(getResources().getColor(R.color.white));
        mTvRecoderTips.setBackgroundColor(getResources().getColor(R.color.red));
        mTvRecoderTips.setVisibility(View.VISIBLE);
        mTvRecoderTips.setText("释放取消录制");


    }

    private void slideCancelRecoder() {

        isCancelRecoder = false;
        mTvRecoderTips.setText("上滑取消录制");
        mTvRecoderTips.setTextColor(getResources().getColor(R.color.white));
        mTvRecoderTips.setBackgroundColor(getResources().getColor(R.color.transparent));
        mTvRecoderTips.setVisibility(View.VISIBLE);


    }

    private void recoderShortTime() {
        mTvRecoderTips.setText("录制时间太短");
        mTvRecoderTips.setVisibility(View.VISIBLE);
        mTvRecoderTips.setTextColor(getResources().getColor(R.color.white));
        mTvRecoderTips.setBackgroundColor(getResources().getColor(R.color.red));
        removeRecoderPart();
        mHandler.postDelayed(mRunable, 1000l);
    }

    private void hideRecoderTxt() {
        mTvRecoderTips.setVisibility(View.INVISIBLE);
        if (isRecoder) {
            mTvRecoderTips.setVisibility(View.VISIBLE);
        }
    }

    private void removeRecoderPart() {
        // 回删
        if (mMediaObject != null) {
            mMediaObject.removeAllPart();

        }
    }


    /**
     * 停止录制
     */
    private void stopRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
        }
    }

    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            hideRecoderTxt();
        }
    };

    private void startRecoderAnim(View paramView) {
        AnimatorSet locald = new AnimatorSet();
        locald.playTogether(
                ObjectAnimator.ofFloat(paramView, "scaleX", new float[]{1.0F, 1.2F, 1.5F}),
                ObjectAnimator.ofFloat(paramView, "scaleY", new float[]{1.0F, 1.2F, 1.5F}),
                ObjectAnimator.ofFloat(paramView, "alpha", new float[]{1.0F, 0.25F, 0.0F})
        );

        locald.setDuration(300L).start();
    }

    private void stopRecoderAnim(View paramView) {
        AnimatorSet locald = new AnimatorSet();
        locald.playTogether(
                ObjectAnimator.ofFloat(paramView, "scaleX", new float[]{1.5F, 1.2F, 1.0F}),
                ObjectAnimator.ofFloat(paramView, "scaleY", new float[]{1.5F, 1.2F, 1.0F}),
                ObjectAnimator.ofFloat(paramView, "alpha", new float[]{0.0F, 0.25F, 1.0F})
        );

        locald.setDuration(300L).start();
    }


    private void setListener() {
        if (DeviceUtils.hasICS()) {
            mSurfaceView.setOnTouchListener(onSurfaveViewTouchListener);
        }

//        mBtnPress.setOnTouchListener(onVideoRecoderTouchListener);

        mBtnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecoder) {
                    startRecoderAnim(mBtnPress);
                    startRecoder();
                } else {
                    stopAll();
                    stopRecoderAnim(mBtnPress);
                    if (isCancelRecoder) {
                        hideRecoderTxt();
                        removeRecoderPart();
                        return;
                    }
                    int duration = mMediaObject.getDuration();
                    if (duration < RECORD_TIME_MIN) {
                        recoderShortTime();

                        return;
                    }


                    if (null != part && null != mMediaObject) {
                        FFmpegController.getInstance(WechatRecoderActivity.this).compressVideo(WechatRecoderActivity.this, part.mediaPath, mMediaObject.getOutputVideoPath(), new SimpleSubscriber<Object>() {
                            @Override
                            public void onCompleted() {
                                Intent data = new Intent();
                                data.putExtra(WechatRecoderActivity.VIDEO_PATH, mMediaObject.getOutputVideoPath());
                                setResult(RESULT_OK, data);
                                finish();
                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        });

                    }

                    startEncoding();
                }
            }
        });

        mTvRecorderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void initView() {
        tvOrientationTipBottom = (TextView) findViewById(R.id.tv_orientation_tip_bottom);
        tvOrientationTipRight = (TextView) findViewById(R.id.tv_orientation_tip);
        tvOrientationTipLeft = (TextView) findViewById(R.id.tv_orientation_tip_left);
        mTvRecorderCancel = (TextView) findViewById(R.id.tv_recorder_cancel);
        mTvSelectVideo = (TextView) findViewById(R.id.tv_select_video);
        mLayoutHeader = (RelativeLayout) findViewById(R.id.layout_header);
        mSurfaceView = (AspectRatioSurfaceView) findViewById(R.id.surfaceView);
        mImgRecordFocusing = (ImageView) findViewById(R.id.img_record_focusing);
        mRlRecoderSurfaceview = (RelativeLayout) findViewById(R.id.rl_recoder_surfaceview);
        mTvRecoderTips = (TextView) findViewById(R.id.tv_recoder_tips);
        mRecorderProgress = (RecoderProgress) findViewById(R.id.recorder_progress);
        mBtnPress = (CircleBackgroundTextView) findViewById(R.id.btn_press);
        mRlRecorderBottom = (RelativeLayout) findViewById(R.id.rl_recorder_bottom);
        mRlBottomRecoder = (LinearLayout) findViewById(R.id.ll_bottom_recoder);
    }


    /**
     * 点击屏幕录制
     */
    private View.OnTouchListener onVideoRecoderTouchListener = new View.OnTouchListener() {
        private float startY;
        private float moveY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null) {
                return false;
            }

            switch (event.getAction()) {

                default:
                    return true;
                case MotionEvent.ACTION_DOWN:

                    startY = event.getY();
                    startRecoderAnim(mBtnPress);
                    startRecoder();
                    break;
                case MotionEvent.ACTION_MOVE:

                    moveY = event.getY();
                    float drution = moveY - startY;

                    if ((drution > 0.0f) && Math.abs(drution) > OFFSET_DRUTION) {
                        slideCancelRecoder();

                    }
                    if ((drution < 0.0f) && (Math.abs(drution) > OFFSET_DRUTION)) {
                        releaseCancelRecoder();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    stopAll();
                    stopRecoderAnim(mBtnPress);
                    if (isCancelRecoder) {
                        hideRecoderTxt();
                        removeRecoderPart();
                        return true;
                    }
                    int duration = mMediaObject.getDuration();
                    if (duration < RECORD_TIME_MIN) {
                        recoderShortTime();
                        return true;
                    }
                    startEncoding();
                    break;

            }

            return true;


        }

    };


    /**
     * 点击屏幕录制
     */
    private View.OnTouchListener onSurfaveViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null || !mCreated) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 检测是否手动对焦
                    if (checkCameraFocus(event))
                        return true;
                    break;
            }
            return true;
        }

    };


    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }

    @Override
    public void onEncodeStart() {

        if (mOnDialogListener != null) {
            mOnDialogListener.onShowDialog(this);
        } else {
            showProgress("", "正在处理中...");

        }
    }

    @Override
    public void onEncodeProgress(int progress) {

    }

    @Override
    public void onEncodeComplete() {

        if (mOnDialogListener != null) {
            mOnDialogListener.onHideDialog(this);
        } else {
            hideProgress();
        }


        String outputVideoPath = mMediaObject.getOutputVideoPath();
        Intent data = new Intent();
        data.putExtra(VIDEO_PATH, outputVideoPath);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onEncodeError() {
        if (mOnDialogListener != null) {
            mOnDialogListener.onHideDialog(this);
        } else {
            hideProgress();
        }
        Toast.makeText(this, "视频转码失败",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (mMediaObject != null)
            mMediaObject.delete();
        finish();
        overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
    }


    public static Intent launchActivity(Context context, RecoderAttrs attrs, int requestCode) {

        if (context instanceof OnDialogListener) {
            mOnDialogListener = (OnDialogListener) context;
        }

        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, WechatRecoderActivity.class);


        if (attrs != null) {
            bundle.putInt(CommonCons.RECORD_TIME_MAX, attrs.getRecoderTimeMax());
            bundle.putInt(CommonCons.RECORD_TIME_MIN, attrs.getRecoderTimeMin());
            bundle.putInt(CommonCons.TITEL_BAR_CANCEL_TEXT_COLOR, attrs.getTitelBarCancelTextColor());
            bundle.putInt(CommonCons.PRESS_BTN_COLOR, attrs.getPressBtnColor());
            bundle.putInt(CommonCons.OFFSET_DRUTION, attrs.getOffsetDrution());
            bundle.putInt(CommonCons.LOW_MIN_TIME_PROGRESS_COLOR, attrs.getLowMinTimeProgressColor());
            bundle.putInt(CommonCons.PROGRESS_COLOR, attrs.getProgressColor());
            bundle.putInt(CommonCons.PRESS_BTN_BG, attrs.getPressBtnBg());
            intent.putExtras(bundle);
        }
        return intent;

//        ((Activity) context).startActivityForResult(intent,requestCode);
    }

    public static Intent launchActivity(Context context, int requestCode) {
        return launchActivity(context, null, requestCode);
    }


    private SensorEventListener orientationListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float azimuth = sensorEvent.values[0];
                float pitch = sensorEvent.values[1];
                float roll = sensorEvent.values[2];
                if (pitch < -45 && pitch > -135) {
//                    orient.setText("Top side of the phone is Up!");
                    tvOrientationTipLeft.setVisibility(View.GONE);
                    tvOrientationTipRight.setVisibility(View.GONE);
                    tvOrientationTipBottom.setVisibility(View.GONE);
                } else if (pitch > 45 && pitch < 135) {

//                    orient.setText("Bottom side of the phone is Up!");
                    tvOrientationTipLeft.setVisibility(View.GONE);
                    tvOrientationTipRight.setVisibility(View.GONE);
                    tvOrientationTipBottom.setVisibility(View.VISIBLE);
                } else if (roll > 45) {

//                    orient.setText("Right side of the phone is Up!");
                    tvOrientationTipLeft.setVisibility(View.GONE);
                    tvOrientationTipRight.setVisibility(View.VISIBLE);
                    tvOrientationTipBottom.setVisibility(View.GONE);
                } else if (roll < -45) {

//                    orient.setText("Left side of the phone is Up!");
                    tvOrientationTipLeft.setVisibility(View.VISIBLE);
                    tvOrientationTipRight.setVisibility(View.GONE);
                    tvOrientationTipBottom.setVisibility(View.GONE);
                }

            }
        }

    };
}
