package com.steptowin.core.tools;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapTool {

    /**
     * 从URL中得到Drawable
     *
     * @param url
     * @return
     */
    public static Drawable loadImageFromUrl(String url) {
        // TODO Auto-generated method stub  

        URL u;
        InputStream i = null;

        try {
            u = new URL(url);
            i = (InputStream) u.getContent();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    /**
     * 从URL中得到Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromUrl(String url) {
        URL u;
        InputStream i = null;

        try {
            u = new URL(url);
            i = (InputStream) u.getContent();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getBitmapFromInputStream(i);
    }


    /**
     * 从url中得到输入流
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static InputStream getRequest(String path) throws Exception {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if (conn.getResponseCode() == 200) {
            return conn.getInputStream();
        }

        return null;
    }

    /**
     * 从输入流中得到字节数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();

        return outStream.toByteArray();
    }

    /**
     * 将字节数组转为Bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray != null && byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * 将字节数组转化成Drawable
     *
     * @param byteArray
     * @return
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
        return Drawable.createFromStream(ins, null);
    }

    /**
     * 得到圆角的Bitmap
     *
     * @param url    网络地址
     * @param pixels 多少像素
     * @return
     * @throws Exception
     */
    public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
            throws Exception {
        byte[] bytes = getBytesFromUrl(url);
        Bitmap bitmap = byteToBitmap(bytes);
        return toRoundCorner(bitmap, pixels);
    }

    /**
     * 得到圆角的Drawable
     *
     * @param url    网络地址
     * @param pixels 多少像素
     * @return
     * @throws Exception
     */
    public static Drawable geRoundDrawableFromUrl(String url, int pixels)
            throws Exception {
        byte[] bytes = getBytesFromUrl(url);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
        return toRoundCorner(bitmapDrawable, pixels);
    }

    /**
     * 从网络地址URI中得到字节数组
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static byte[] getBytesFromUrl(String url) throws Exception {
        return readInputStream(getRequest(url));
    }

    /**
     * 将Bitmap转为字节数组
     *
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 从数据流中加载bitmap对象
     *
     * @return
     */
    public static Bitmap getBitmapFromInputStream(InputStream is) {
        Bitmap bm = null;
        if (is != null)
            bm = BitmapFactory.decodeStream(is);
        return bm;
    }

    /**
     * 从byte数组中加载bitmap对象
     *
     * @return
     */
    public static Bitmap createFromByteArray(String path) {
        Bitmap bm = null;
        byte[] bytes = getBytes(path);
        bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bm;
    }


    /**
     * 根据输入流， 缩小比获取位图对象
     *
     * @param in
     * @param scale
     * @return
     */
    public static Bitmap getBitmap(InputStream in, int scale) {
        Bitmap _bitmap = null;
        Options _ops = new Options();
        _ops.inSampleSize = scale;
        _bitmap = BitmapFactory.decodeStream(in, null, _ops);
        return _bitmap;
    }


    /**
     * 根据指定输入的宽高，保持纵横比，缩小获取位图对象
     *
     * @param
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmap(byte[] bytes, int width, int height) {
        Bitmap bm = null;
        if (bytes != null) {
            BitmapFactory.Options opts = new Options();
            opts.inJustDecodeBounds = true;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            int realW = opts.outWidth;
            int realH = opts.outHeight;
            int xScale = realW / width;
            int yScale = realH / height;
            int scale = xScale > yScale ? xScale : yScale;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = scale;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);


        }
        return bm;
    }


    public Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBmp(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    public Bitmap drawableToBmp(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 从文件中加载bitmap对象
     *
     * @return
     */
    public static Bitmap createBitmapFromFile(String pathName, int w, int h) {

        File f = new File(pathName);

        Bitmap b = null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            Bitmap bm = BitmapFactory.decodeFile(pathName, o);
            int realW = o.outWidth;
            int realH = o.outHeight;
            int xScale = realW / w;
            int yScale = realH / h;
            int scale = xScale > yScale ? xScale : yScale;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = (int) scale;
            FileInputStream fis = new FileInputStream(f);

            b = BitmapFactory.decodeStream(fis, null, o2);

            fis.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;

    }


    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(filename, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(filename, options);
        } catch (OutOfMemoryError e) {
            Log.e("BitmapDecoder", "decodeSampledBitmapFromFile内存溢出，如果频繁出现这个情况 可以尝试配置增加内存缓存大小");
            e.printStackTrace();
            return null;
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     * 从res资源中加载bitmap位图
     *
     * @return
     */
    public static Bitmap createBitmapFromResources(Resources res, int id) {
        Bitmap bm = BitmapFactory.decodeResource(res, id);
        return bm;
    }

    /**
     * 从文件中加载bitmap对象
     *
     * @return
     */
    public static Bitmap createBitmapFromFile(String pathName) {
        Bitmap bm = BitmapFactory.decodeFile(pathName);
        return bm;
    }

    /**
     * 获取指定文件的内容到一个字节数组
     *
     * @return
     */
    public static byte[] getBytes(String path) {
        byte[] content = null;
        try {
            InputStream in = new FileInputStream(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            in.close();
            out.close();
            content = out.toByteArray();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 将位图保存到指定的路径
     *
     * @param path
     * @param bitmap
     * @throws IOException
     */
    public static boolean saveBitmap(String path, Bitmap bitmap) throws IOException {
        OutputStream write = null;
        if (path != null && bitmap != null) {
            File _file = new File(path);
            //如果文件夹不存在则创建一个新的文件
            if (!_file.exists()) {
                _file.getParentFile().mkdirs();
                _file.createNewFile();
            }
            //创建输出流
            write = new FileOutputStream(_file);
            //获取文件名
            String fileName = _file.getName();
            //取出文件的格式名
            String endName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("png".equalsIgnoreCase(endName)) {
                //bitmap的压缩格式
                bitmap.compress(CompressFormat.PNG, 100, write);
                write.close();
                return true;
            } else {
                bitmap.compress(CompressFormat.JPEG, 100, write);
                long file_size = _file.length();
                write.close();
                return true;
            }
        }
        if (write != null) {
            write.close();
        }
        return false;
    }


    /**
     * 根据传入的位图对象，复制一个新的位图对象返回
     *
     * @param bitmap 源位图对象
     * @return 新的位图对象
     */
    public static Bitmap copyBitmap(Bitmap bitmap) {
        Bitmap bm = Bitmap.createBitmap(bitmap);
        return bm;
    }


    /**
     * 创建一个空的位图对象(有宽 高 和 颜色属性)
     *
     * @param width  指定bitmap的宽度
     * @param height 指定bitmap的高度
     * @param config
     * @return
     */
    public static Bitmap createEmptyBitmap(int width, int height, Config config) {
        Bitmap bm = Bitmap.createBitmap(width, height, config);
        return bm;
    }

    /**
     * 裁剪原图片的一部分并返回
     *
     * @param bitmap 原位图
     * @param x      The x coordinate of the first pixel in source
     * @param y      The y coordinate of the first pixel in source
     * @param width  The number of pixels in each row
     * @param height The number of rows
     * @return
     */
    public static Bitmap getPartOfSourceBitmap(Bitmap bitmap, int x, int y, int width, int height) {
        Bitmap bm = Bitmap.createBitmap(bitmap, x, y, width, height);
        return bm;
    }

    /**
     * 使用矩阵，将源位图旋转一定角度并返回新位图对象
     * Matrix matrix = new Matrix();
     * matrix.setRotate(90);
     * 使用矩阵，将源位图对象 伸缩一定比例 并返回新的位图对象
     * matrix.setScale(1.2f, 0.5f);
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createRotateBitmap(Bitmap bitmap, int x, int y, int width, int height, Matrix matrix, boolean filter) {
        Bitmap bm = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, filter);
        return bm;
    }


    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
                                               int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }


}
