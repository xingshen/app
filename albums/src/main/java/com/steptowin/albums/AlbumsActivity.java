package com.steptowin.albums;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.common.base.BaseListFragment;
import com.steptowin.common.base.SimpleFragmentActivityWithTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * desc:相册
 * author：zg
 * date:16/6/14
 * time:下午3:10
 */
@Route(path="/albums/AlbumsActivity")
public class AlbumsActivity extends SimpleFragmentActivityWithTitle<List<Picture>, AlbumsView, AlbumsPresenter> implements AlbumsView {

    private AlbumsFragment albumsFragment;
    private List<String> folders;

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @NonNull
    @Override
    public AlbumsPresenter createPresenter() {
        return new AlbumsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumsFragment = new AlbumsFragment();
        getPresenter().loadAlbums();
//        Title title = Title.init(this).addMenu(R.menu.menu_album);
//        title.setMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case 0:
//                        if (null != folders) {
//                            FoldersFragment foldersFragment = new FoldersFragment();
//                            foldersFragment.setFolders(folders);
//                            addFragment(foldersFragment);
//                        }
//                        ToastTool.showDebugToast(getAttachedContext(), "图集");
//                        break;
//                    case 1:
//                        Event event = Event.create(R.id.event_album_sure).putParam(List.class, getPresenter().getCheckedPictures());
////                        event.list = getPresenter().getCheckedPictures();
//                        EventWrapper.post(event);
//                        getActivityDelegate().finish();
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void setData(List<Picture> data) {
        getFragmentManagerDelegate().replaceFragment(getFragmentContainerId(), albumsFragment, false);
    }

    @Override
    public void setFolders(Map<String, List<Picture>> folders) {
        this.folders = new ArrayList<>(folders.keySet());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().clearCheckedInfo();
    }

    public static class FoldersFragment extends BaseListFragment {

        private List<String> folders;

        public void setFolders(List<String> folders) {
            this.folders = folders;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
//            ListView listView = getListView();
//            listView.setBackgroundColor(ResTool.getColor(R.color.white));
//            setListAdapter(new SimpleViewHolderAdapter<String>(view.getContext()) {
//                @Override
//                public void onBindViewHolder(SimpleViewHolder holder, int position) {
//                    holder.getView(TextView.class, R.id.tv_folder_name).setText(getItem(position));
////                    holder.getConvertView(TextView.class).setText(getItem(position));
//                }
//
//
//                @Override
//                public int getItemLayoutRes(ViewGroup parent, int position) {
//                    return R.layout.item_album_folder;
//                }
//
//
////                @Override
////                public View getItemLayout(ViewGroup parent, int position) {
////                    return new TextView(parent.getContext());
////                }
//            }.replaceAll(folders));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
//            Event event = Event.create(R.id.event_album_switch);
//            event.putParam(String.class, (String) getListAdapter().getItem(position));
//            EventWrapper.post(event);
//            getFragmentManagerDelegate().removeFragment();
        }
    }
}
