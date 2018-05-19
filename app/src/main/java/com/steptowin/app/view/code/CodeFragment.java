package com.steptowin.app.view.code;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.tool.Title;
import com.steptowin.code.CaptureViewDelegate;
import com.steptowin.code.CodeClient;
import com.steptowin.code.view.ViewfinderResultPointCallback;
import com.steptowin.code.view.ViewfinderView;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.event.Event;
import com.steptowin.core.tools.ToastTool;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:扫码测试
 * author：zg
 * date:16/6/22
 * time:上午11:44
 */
public class CodeFragment extends BaseFragment implements CaptureViewDelegate {

    CodeClient codeClient;
    @Bind(R.id.preview_view)
    SurfaceView previewView;
    @Bind(R.id.viewfinder_view)
    ViewfinderView viewfinderView;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_code;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeClient = CodeClient.getInstance();
        codeClient.init(getAttachedContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        codeClient.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        codeClient.pause();
    }

    @Override
    public ResultPointCallback getResultPointCallback() {
        return new ViewfinderResultPointCallback(viewfinderView);
    }

    @Override
    public SurfaceView getSurFaceView() {
        return previewView;
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        ToastTool.showDebugToast(getAttachedContext(), "扫描结果:" + result.getText());
    }

    @Override
    public void decodeFail(Exception e) {
        ToastTool.showDebugToast(getAttachedContext(), "扫描失败:" + e);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        codeClient.setCaptureViewDelegate(this);
        codeClient.initHandler(this);

        SurfaceHolder surfaceHolder = previewView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                codeClient.startPreview(surfaceHolder);
                if (!decodeByPath)
                    codeClient.startDecode();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                codeClient.stopDecode();
                codeClient.stopPreview(surfaceHolder);
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Title title = Title.init(this);
        title.setTitle("扫码").addMenu(R.menu.menu_choose_pic)
                .setMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_choose_pic:
//                                Intent intent = new Intent();
//                                intent.setClass(getAttachedContext(), AlbumsActivity.class);
//                                intent.putExtra(ResTool.getString(R.string.key_album_choose_mode), AlbumsFragment.CHOOSE_MODE_SINGLE);
//                                getActivityDelegate().startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });

    }

    boolean decodeByPath;

    @Override
    public void onEventMainThread(Event event) {
        switch (event._id) {
            case R.id.event_album_choose_one:
                decodeByPath = true;
//                Picture picture = event.getParam(Picture.class);
//                String realPath = UriTool.getRealPathFromContentPath(getAttachedContext(), picture.getPath());
//                codeClient.decodeByPath(realPath);
                break;
        }
    }

    @Override
    public boolean eventEnable() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
