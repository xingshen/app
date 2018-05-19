/*
 * Copyright (C) 2010 ZXing authors
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.steptowin.code.R;
import com.steptowin.code.camera.CameraManager;
import com.steptowin.code.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;

public final class DecodeHandler extends Handler {
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final CaptureActivityHandler captureActivityHandler;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(CaptureActivityHandler captureActivityHandler,
                  Hashtable<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.captureActivityHandler = captureActivityHandler;
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        Result rawResult = null;

        // modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = CameraManager.get()
                .buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            decodeFailed(re);
        } finally {
            multiFormatReader.reset();
        }

        if (rawResult != null) {
            decodeSuccessed(rawResult,source.renderCroppedGreyscaleBitmap());
        }
    }

    private void decodeSuccessed(Result rawResult,Bitmap barcodeBmp){
        Message message = Message.obtain(captureActivityHandler,
                R.id.decode_succeeded, rawResult);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
                barcodeBmp);
        message.setData(bundle);
        message.sendToTarget();
    }

    private void decodeFailed(Exception e){
        e.printStackTrace();
        Message message = Message.obtain(captureActivityHandler,
                R.id.decode_failed);
        message.obj = e;
        message.sendToTarget();
    }

    public void decode(String bitmapPath) {

        Result rawResult = null;
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String
            // path,
            // Options opt)
            // 并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
            options.inJustDecodeBounds = true;
            // 此时的bitmap是null，这段代码之后，options.outWidth 和
            // options.outHeight就是我们想要的宽和高了
            bitmap = BitmapFactory.decodeFile(bitmapPath, options);

            // 以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
            options.inSampleSize = (int) Math
                    .ceil((double) options.outHeight / 400d);
            if (options.inSampleSize <= 0) {
                options.inSampleSize = 1; // 防止其值小于或等于0
            }

            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(bitmapPath, options);

            // 新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            int[] data = new int[width * height];
            bitmap.getPixels(data, 0, width, 0, 0, width, height);
            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(
                    width, height, data);
            // 将图片转换成二进制图片
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    rgbLuminanceSource));
            // 初始化解析对象
            // 开始解析

            rawResult = multiFormatReader.decodeWithState(binaryBitmap);
            decodeSuccessed(rawResult,bitmap);

        } catch (Exception e) {
            decodeFailed(e);
        } finally {
            multiFormatReader.reset();
        }

    }

}
