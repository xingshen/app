package com.steptowin.code;

import android.graphics.Bitmap;
import android.view.SurfaceView;

import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;

/**
 * desc:扫码视图代理
 * author：zg
 * date:16/6/22
 * time:下午2:41
 */
public interface CaptureViewDelegate {

    /**
     * 预览时,识别到点回调
     * @return
     */
    ResultPointCallback getResultPointCallback();

    /**
     * 摄像头预览
     * @return
     */
    SurfaceView getSurFaceView();

    /**
     * 识别成功后的结果回调
     * @param result
     * @param barcode
     */
    void handleDecode(Result result, Bitmap barcode);

    /**
     *
     * @param e
     */
    void decodeFail(Exception e);

}
