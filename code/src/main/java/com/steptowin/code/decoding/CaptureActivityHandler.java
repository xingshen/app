/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.steptowin.code.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.steptowin.code.CaptureViewDelegate;
import com.steptowin.code.R;
import com.steptowin.code.camera.CameraManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 */
public final class CaptureActivityHandler extends Handler {

    private static final String TAG = CaptureActivityHandler.class
            .getSimpleName();

    private final CaptureViewDelegate captureDelegate;
    private DecodeThread decodeThread;
    private State state;

    private static final ExecutorService mDecodeBitmapThread = Executors
            .newSingleThreadExecutor();

    /**
     * 扫描过程中的三种状态:预览,成功,空闲(pause)
     * 扫描失败则继续预览
     */
    private enum State {
        PREVIEW, SUCCESS, IDLE
    }

    public CaptureActivityHandler(CaptureViewDelegate captureViewDelegate) {
        this.captureDelegate = captureViewDelegate;
        state = State.IDLE;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
            Bundle bundle = message.getData();

            Bitmap barcode = bundle == null ? null : (Bitmap) bundle
                    .getParcelable(DecodeThread.BARCODE_BITMAP);

            captureDelegate.handleDecode((Result) message.obj, barcode);

        } else if (message.what == R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
            captureDelegate.decodeFail((Exception)message.obj);
        } else if (message.what == R.id.return_scan_result) {
//            captureDelegate.setResultDecode(Activity.RESULT_OK, (Intent) message.obj);
        } else if (message.what == R.id.launch_product_query) {
            String url = (String) message.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            captureDelegate.launchProductQuery(intent);
        }
    }

    public void quitSynchronously() {
        state = State.IDLE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);

    }

    public void restartPreviewAndDecode() {
        if (state == State.IDLE) {
            state = State.PREVIEW;
            if(null != decodeThread)
                decodeThread.interrupt();
            decodeThread = new DecodeThread(this, null, null,
                    captureDelegate.getResultPointCallback());
            decodeThread.start();
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
//            captureDelegate.drawViewfinder();
        }
    }

    public void decodeBitmapAsync(final String bitmapPath) {
        mDecodeBitmapThread.execute(new Runnable() {

            @Override
            public void run() {
                decodeThread.getHandler().decode(bitmapPath);
            }
        });
    }
}
