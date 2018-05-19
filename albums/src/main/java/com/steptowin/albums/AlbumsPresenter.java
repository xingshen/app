package com.steptowin.albums;

import android.text.TextUtils;

import com.steptowin.common.base.BasePresenter;
import com.steptowin.core.common.callback.SimpleCallback;

import java.util.List;

/**
 * desc: 相册业务处理
 * author：zg
 * date:16/6/13
 * time:下午1:41
 */
public class AlbumsPresenter extends BasePresenter<AlbumsView> {
    LocalImageHelper localImageHelper;

    public AlbumsPresenter(AlbumsView albumsView){
        attachView(albumsView);
    }

    public AlbumsPresenter() {
        localImageHelper = LocalImageHelper.getHelper(null);
    }

    @Override
    public void attachView(AlbumsView view) {
        super.attachView(view);
    }

    public void loadAlbums() {
        if (!localImageHelper.isInited()) {
            localImageHelper.setInitCallback(new SimpleCallback() {
                @Override
                public void successOnUI(Object o) {
                    if (null != getView()){
                        getView().setData(localImageHelper.getPictures(LocalImageHelper.ALL_PICTURE));
                        getView().setFolders(localImageHelper.getFolders());
                    }
                }
            });
        } else {
            getView().setData(localImageHelper.getPictures(LocalImageHelper.ALL_PICTURE));
            getView().setFolders(localImageHelper.getFolders());
        }
    }

    public List<Picture> getPictures(String folder) {
        if (TextUtils.isEmpty(folder))
            folder = LocalImageHelper.ALL_PICTURE;
        return localImageHelper.getPictures(folder);
    }

    public List<Picture> getCheckedPictures(){
        return localImageHelper.getCheckedPictures();
    }

    public void clearCheckedInfo(){
        localImageHelper.clearChecked();
    }
}
