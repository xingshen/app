package com.steptowin.albums;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.steptowin.core.common.callback.ICallback;
import com.steptowin.core.common.thread.CachedThreadExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zg
 * @desc: 本地相册处理类.
 * 注意:需要在程序初始化时,调用{}{@link #getHelper(Context)}进入后台初始化操作
 * 否则,会返回错误.
 * @time 16/6/13
 */
public class LocalImageHelper {
    private static LocalImageHelper instance;
    public static final String ALL_PICTURE = "所有图片";
    private final Context context;

    /**
     * 是否进行过本地相册初始化
     */
    private AtomicBoolean isInited = new AtomicBoolean(false);

    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    final List<Picture> pictures = Collections.synchronizedList(new ArrayList<Picture>());

    final Map<String, List<Picture>> folders = new HashMap<String, List<Picture>>();

    private Executor executor = CachedThreadExecutor.create();

    private ICallback initCallback;

    public static LocalImageHelper getHelper(Context context) {
        if (null == instance) {
            if (!(context instanceof Application)) {
                throw new IllegalStateException("应该在程序启动时初始化,这个时候不合适哦!你是不是想重新扫描本地相册,调用rescan试试!!!");
            }
            instance = new LocalImageHelper(context);
        }
        return instance;
    }

    private LocalImageHelper(Context context) {
        this.context = context;
        init(context);
    }

    /**
     * 子线程初始化一次
     *
     * @param context
     */
    private void init(Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!isInited.get()) {
                    if (null != initCallback)
                        initCallback.start();
                    initImage();
                    if (null != initCallback)
                        initCallback.success(null);

                    isInited.set(true);
                }
            }
        });
    }

    public void setInitCallback(ICallback callback) {
        this.initCallback = callback;
    }

    public boolean isInited() {
        return isInited.get();
    }


    private synchronized void initImage() {
        clearAll();
        //获取大图的游标
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                STORE_IMAGES,   // 字段
                null,         // No where clause
                null,         // No where clause
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);//大图ID
            String path = cursor.getString(1);//大图路径
            int degree = cursor.getInt(2);
            Picture picture = createSinglePicture(id, path, degree);
            appendPicture(picture, false);
        }
        folders.put(ALL_PICTURE, pictures);
        cursor.close();
    }

    //添加最新的图片到所有图片
    public Picture addNewOneToPath(Uri fileUri, boolean isAddToCheck) {
        Cursor cursor = context.getContentResolver().query(
                fileUri,  // 大图URI
                STORE_IMAGES,   // 字段
                null,         // No where clause
                null,         // No where clause
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor == null)
            return null;
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);//大图ID
            String path = cursor.getString(1);//大图路径
            int degree = cursor.getInt(2);
            Picture picture = createSinglePicture(id, path, degree);

            appendPicture(picture, true);
            return picture;
        }
        return null;
    }

    /**
     * 读取到一张照片后,添加到集合中
     *
     * @param picture
     * @param reverse 刚刚拍照后获取到的照片,放在第一个位置
     */
    private void appendPicture(Picture picture, boolean reverse) {
        if (null != picture) {
            pictures.add(picture);
            //判断文件夹是否已经存在
            if (folders.containsKey(picture.getFolder())) {
                List<Picture> pictures = folders.get(picture.getFolder());
                if (reverse) {
                    pictures.add(0, picture);
                } else {
                    pictures.add(picture);
                }
            } else {
                List<Picture> files = new ArrayList<Picture>();
                files.add(picture);
                folders.put(picture.getFolder(), files);
            }
        }
    }

    /**
     * 由id,path,degree生成一个照片实体
     *
     * @param id
     * @param path
     * @param degree
     * @return
     */
    private Picture createSinglePicture(int id, String path, int degree) {
        File file = new File(path);
        //判断大图是否存在
        if (file.exists()) {
            //小图URI
            String thumbUri = getThumbnail(id, path);
            //获取大图URI
            String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(id)).build().toString();
            if (TextUtils.isEmpty(uri))
                return null;
            if (TextUtils.isEmpty(thumbUri))
                thumbUri = uri;
            //获取目录名
            String folder = file.getParentFile().getName();

            Picture picture = new Picture();
            picture.setPath(uri);
            picture.setThumbPath(thumbUri);
            if (degree != 0) {
                degree = degree + 180;
            }
            picture.setDegree(360 - degree);
            picture.setFolder(folder);
            return picture;
        }
        return null;
    }

    private String getThumbnail(int id, String path) {
        //获取大图的缩略图
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(thumId)).build().toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    public Map<String, List<Picture>> getFolders() {
        return folders;
    }

    public List<Picture> getCheckedItems() {
        List<Picture> checkedItems = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture.isChecked()) {
                checkedItems.add(picture);
            }
        }
        return checkedItems;
    }

    public List<Picture> getPictures(String folder) {
        return folders.get(folder);
    }

    public List<Picture> getCheckedPictures() {
        ArrayList<Picture> checkedPics = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture.isChecked()) {
                checkedPics.add(picture);
            }
        }
        return checkedPics;
    }


    public void clearChecked() {
        for (Picture picture : pictures) {
            picture.setChecked(false);
        }
    }

    public void clearAll() {
        clearChecked();
        pictures.clear();
        folders.clear();
    }

    /**
     * 重新扫描
     */
    public void rescan() {
        isInited.set(false);
        init(context);
    }
}
