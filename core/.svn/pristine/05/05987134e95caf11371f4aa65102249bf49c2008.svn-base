package com.steptowin.core.db.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.steptowin.core.db.dao.DaoFactory;

/**
 * @author zg
 * @Description 类描述：数据库初始化类，创建表、移除表可以在这里实现
 * app参考此类配置表结构
 * @date 创建日期 2014-7-11 上午11:26:21
 * @record 修改记录：
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static String DATABASE_NAME = "steptowin.db";// 根据不同用户登录进来动态创建不同数据库
    public static final int DATABASE_VERSION = 5;// 每次修改数据库结构或者类型都要版本号+1
    private static DBHelper helper;
    private Context cx;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DaoFactory.getFactory().init(this);
    }

    public static synchronized DBHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DBHelper(context);
        }
        return helper;
    }

    public static synchronized DBHelper getHelper(Context context, String dbName) {
        if (helper == null) {
            DATABASE_NAME = dbName;
            helper = new DBHelper(context);
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
//		try {
//			TableUtils.createTable(connectionSource, User.class);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        /**
         * 使用如下升级方法
         * {@link DatabaseUtil#upgradeTable(SQLiteDatabase, ConnectionSource, Class, DatabaseUtil.OPERATION_TYPE)}
         */
    }

}
