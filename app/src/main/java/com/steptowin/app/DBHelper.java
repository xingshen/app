package com.steptowin.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.steptowin.app.model.bean.sql.User;
import com.steptowin.core.db.common.DatabaseUtil;
import com.steptowin.core.db.dao.DaoFactory;

import java.sql.SQLException;

/**
 * @author zg
 * @Description 类描述：数据库初始化类，创建表、移除表可以在这里实现
 * @date 创建日期 2014-7-11 上午11:26:21
 * @record 修改记录：
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "weixue.db";// 根据不同用户登录进来动态创建不同数据库
    public static final int DATABASE_VERSION = 4;// 每次修改数据库结构或者类型都要版本号+1
    private static DBHelper helper;

    private DBHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
        DaoFactory.getFactory().init(this);
    }

    public static synchronized DBHelper getHelper(Context context) {
        return getHelper(context, DATABASE_NAME);
    }

    public static synchronized DBHelper getHelper(Context context, String dbName) {
        if (helper == null) {
            helper = new DBHelper(context, dbName);
        }
        return helper;
    }

    public static void reset() {
        helper = null;
        DaoFactory.getFactory().reset();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        // 在这里创建表
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        /**
         * 数据库升级时，考虑旧的版本兼容问题。需要分多次升级
         */
        if (oldVersion < 10) {
            DatabaseUtil.upgradeTable(db, connectionSource, User.class, DatabaseUtil.OPERATION_TYPE.UPDATE);
        }
    }

}
