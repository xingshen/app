package com.steptowin.app.view.fragment;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.steptowin.app.DBHelper;
import com.steptowin.app.model.bean.ContactBean;
import com.steptowin.app.view.activity.R;
import com.steptowin.app.view.activity.TestFragmentActivity;
import com.steptowin.app.view.activity.db.DbActivity;
import com.steptowin.app.view.activity.http.HttpActivity;
import com.steptowin.app.view.audio.AudioFragment;
import com.steptowin.app.view.code.CodeFragment;
import com.steptowin.app.view.event.EventbusActivity;
import com.steptowin.app.view.fragment.coor_layout.CoorMainFragment;
import com.steptowin.app.view.thread.ThreadFragment;
import com.steptowin.common.base.BaseFragment;
import com.steptowin.core.event.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc:
 * author：zg
 * date:16/6/9
 * time:下午11:39
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.btn_http)
    Button btnHttp;
    @Bind(R.id.btn_db)
    Button btnDb;
    @Bind(R.id.btn_test_fragment)
    Button btnTestFragment;
    @Bind(R.id.btn_kill_other)
    Button btnKillOther;
    @Bind(R.id.btn_albums)
    Button btnAlbums;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.container)
    FrameLayout container;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //    @DebugTrace
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

//    @DebugTrace
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 批量添加数据
     *
     * @param cursor
     * @return
     */
    public boolean add(Cursor cursor) {
        SQLiteDatabase db = DBHelper.getHelper(getHoldingActivity()).getWritableDatabase();
        long result = 0;
        db.beginTransaction();
        while (cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            String contactname = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactnumber = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            values.put("name", contactname);
            values.put("number", contactnumber);
            result = db.insert("tb_contacts", null, values);
//            GlobalConstants.PrintLog_D("[ContactsDAO->add] cursor result = "
//                    + result + " number = " + contactnumber);
        }
        db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        db.endTransaction();
        cursor.close();
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public void BatchAddContact(List<ContactBean> list)
            throws RemoteException, OperationApplicationException {
//        GlobalConstants.PrintLog_D("[GlobalVariables->]BatchAddContact begin");
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (ContactBean contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            // 真正添加
            ContentProviderResult[] results = getHoldingActivity().getContentResolver()
                    .applyBatch(ContactsContract.AUTHORITY, ops);
            // for (ContentProviderResult result : results) {
            // GlobalConstants
            // .PrintLog_D("[GlobalVariables->]BatchAddContact "
            // + result.uri.toString());
            // }
        }
    }

    @OnClick({R.id.btn_http, R.id.btn_db, R.id.btn_test_fragment,
            R.id.btn_kill_other, R.id.btn_albums, R.id.btn_audio,
            R.id.btn_thread, R.id.btn_code, R.id.btn_event, R.id.btn_mutil_add_contacts,
            R.id.btn_learn_coor,R.id.btn_compress_video,R.id.btn_service,
            R.id.btn_modules,R.id.btn_aroute_modules})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_service:
                addFragment(new ServiceFragment());
                break;
            case R.id.btn_learn_coor:
                addFragment(new CoorMainFragment());
                break;
            case R.id.btn_mutil_add_contacts:
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//
//                List<ContactBean> contacts = new ArrayList<>();
//                for (int i = 2500; i < 3000; i++) {
//                    ContactBean contactBean = new ContactBean();
//                    contactBean.setName("xing" + i);
//                    contactBean.setNumber("1815817" + i);
//                    contacts.add(contactBean);
//                }
//                try {
//                    BatchAddContact(contacts);
//                    Log.i("info", "添加成功");
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                } catch (OperationApplicationException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.btn_http:
                getActivityDelegate().startActivity(HttpActivity.class);
                break;
            case R.id.btn_db:
                getActivityDelegate().startActivity(DbActivity.class);
                break;
            case R.id.btn_test_fragment:
                getActivityDelegate().startActivity(TestFragmentActivity.class);
                break;
            case R.id.btn_kill_other:
                replaceFragment(new KillOther());
                break;
            case R.id.btn_albums:
                ARouter.getInstance().build("/albums/AlbumsActivity").navigation();
//                getActivityDelegate().startActivity(AlbumsActivity.class);
                break;
            case R.id.btn_audio:
                replaceFragment(new AudioFragment());
                break;
            case R.id.btn_thread:
                addFragment(new ThreadFragment());
                break;
            case R.id.btn_code:
                replaceFragment(new CodeFragment());
                break;
            case R.id.btn_event:
                getActivityDelegate().startActivity(EventbusActivity.class);
                break;
            case R.id.btn_compress_video:

                addFragment((Fragment)ARouter.getInstance().build("/video/compress").navigation());

                break;
            case R.id.btn_modules:
                switchToActivity("home","com.steptowin.firstbundle.Main2Activity");
                break;
            case R.id.btn_aroute_modules:
                break;
                default:
                    break;
        }
    }


    public void switchToActivity(String key,String activityName){
//        Intent intent = new Intent();
//        intent.setClassName(getHoldingActivity(),activityName);
//        mActivityDelegate.startChildActivity(container,key,intent);

        Intent intent = new Intent();
        intent.setPackage(getHoldingActivity().getPackageName());
        intent.setClassName(getHoldingActivity(), activityName);
//        intent.setClassName(getHoldingActivity(), "com.steptowin.secondbundle.MainActivity");
        startActivity(intent);
    }
    @Override
    public boolean eventEnable() {
        return true;
    }

    @Override
    public void onEventMainThread(Event event) {
        switch (event._id) {
            case R.id.event_album_sure:
//                List<Picture> checkedPics = (List<Picture>)event.list;
//                List<Picture> checkedPics = (List<Picture>) event.getParam(List.class);
//                ToastTool.showDebugToast(getAttachedContext(), "选中图片张数为:" + checkedPics.size());
        }
    }


}