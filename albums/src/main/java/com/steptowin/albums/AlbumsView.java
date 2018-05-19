package com.steptowin.albums;

import com.steptowin.common.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * desc: 相册视图接口,用户操作交互
 * author：zg
 * date:16/6/13
 * time:下午1:42
 */
public interface AlbumsView extends BaseView<List<Picture>> {

    void setFolders(Map<String,List<Picture>> folders);

}
