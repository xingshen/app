package com.steptowin.albums;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.steptowin.common.adapter.SimpleViewHolderAdapter;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.cache.ImageLoader;
import com.steptowin.core.event.Event;
import com.steptowin.core.event.EventWrapper;

import butterknife.ButterKnife;

/**
 * desc:本地相册
 * author：zg
 * date:16/6/13
 * time:下午7:40
 */
public class AlbumsFragment extends BaseFragment {
    public static final int CHOOSE_MODE_DEFAULT = 0;
    public static final int CHOOSE_MODE_SINGLE = 1;
    public static final int CHOOSE_MODE_MUTL = 2;


    GridView gvAlbums;
    SimpleViewHolderAdapter<Picture> albumsAdapter;

    private AlbumsPresenter albumsPresenter;
    private int chooseMode = CHOOSE_MODE_DEFAULT;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_albums;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        albumsPresenter = (AlbumsPresenter) getActivityPresenter();
        chooseMode = getHoldingActivity().getIntent().getIntExtra("1", CHOOSE_MODE_DEFAULT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        gvAlbums = (GridView) rootView.findViewById(R.id.gv_albums);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        albumsAdapter = new SimpleViewHolderAdapter<Picture>(getAttachedContext()) {
            @Override
            public int getItemLayoutRes(ViewGroup parent, int position) {
                return R.layout.item_album_picture;
            }

            @Override
            public void onBindViewHolder(SimpleViewHolder holder, int position) {
                Picture picture = getItem(position);
                ImageLoader.displayImage(holder.getView(ImageView.class, R.id.iv_picture), picture.getThumbPath(), R.drawable.ic_launcher);
                holder.getView(ImageView.class, R.id.iv_checked).setVisibility(picture.isChecked() ? View.VISIBLE : View.GONE);

            }
        };
        gvAlbums.setAdapter(albumsAdapter.replaceAll(albumsPresenter.getPictures(null)));


        gvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Picture picture = albumsAdapter.getItem(i);
                picture.setChecked(!picture.isChecked());
                if (chooseMode == CHOOSE_MODE_SINGLE) {
                    Event event = Event.create(R.id.event_album_choose_one);
                    event.putParam(Picture.class, picture);
                    EventWrapper.post(event);
                    getActivityDelegate().finish();
                } else {
                    albumsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean eventEnable() {
        return true;
    }

    @Override
    public void onEventMainThread(Event event) {
        switch (event._id) {
            case 0:
                String folder = event.getParam(String.class);
                albumsAdapter.replaceAll(albumsPresenter.getPictures(folder));
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
