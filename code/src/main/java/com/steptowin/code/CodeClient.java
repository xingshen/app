package com.steptowin.code;

import android.content.Context;
import android.hardware.Camera;
import android.os.PowerManager;
import android.view.SurfaceHolder;

import com.steptowin.code.camera.CameraManager;
import com.steptowin.code.decoding.CaptureActivityHandler;

import java.io.IOException;

/**
 * desc:码端,
 * author：zg
 * date:16/6/23
 * time:下午3:00
 */
public class CodeClient {
    private static CodeClient instance = new CodeClient();
    private Context context;
    PowerManager.WakeLock mWakeLock;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private CaptureViewDelegate captureViewDelegate;

    public static CodeClient getInstance() {
        if (null == instance)
            instance = new CodeClient();
        return instance;
    }

    /**
     * 页面启动时初始化
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        CameraManager.init(context);
        cameraManager = CameraManager.get();
        if (context instanceof CaptureViewDelegate) {
            captureViewDelegate = (CaptureViewDelegate) context;
        }
    }

    public void setCaptureViewDelegate(CaptureViewDelegate delegate) {
        this.captureViewDelegate = delegate;
    }

    /**
     * 初始化UIhandler
     * @param delegate
     */
    public void initHandler(CaptureViewDelegate delegate) {
        handler = new CaptureActivityHandler(delegate);
    }

    public void resume() {
        mWakeLock.acquire();
    }

    public void pause() {
        mWakeLock.release();
    }

    /**
     * 开启预览
     * @param surfaceHolder
     */
    public void startPreview(SurfaceHolder surfaceHolder) {
        cameraManager.stopPreview();
        try {
            Camera camera = cameraManager.openDriver();
            camera.setPreviewDisplay(surfaceHolder);
            cameraManager.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止预览
     * @param surfaceHolder
     */
    public void stopPreview(SurfaceHolder surfaceHolder) {
        cameraManager.stopPreview();
        cameraManager.closeDriver();
    }

    /**
     * 识别照片中的码
     *
     * @param path
     */
    public void decodeByPath(String path) {
        stopDecode();
        handler.decodeBitmapAsync(path);
    }

    /**
     * 开启识别线程
     */
    public void startDecode() {
        if (null == handler)
            return;
        handler.restartPreviewAndDecode();
    }

    /**
     * 停止识别线程
     */
    public void stopDecode() {
        if (null == handler)
            return;
        handler.quitSynchronously();
    }


}
