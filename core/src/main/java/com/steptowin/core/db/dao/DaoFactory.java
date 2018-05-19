package com.steptowin.core.db.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.steptowin.core.common.AppVariables;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zg
 * @Description 类描述：DAO工厂类，负责创建各个数据库表DAO类。数据库表的DAO对象采用单例模式，初始化化DAO类会比较耗用资源，所以，要尽量重用DAO对象
 * 。新增表，则要创建对应的dao对象
 * @date 创建日期 2014-7-11 上午11:29:51
 * @record 修改记录：
 */
public class DaoFactory {

    private static DaoFactory factory = null;
    private Context globalContext = null;

    private Map<Class, Dao<?, Integer>> daoMap;// 各个表操作的dao类映射

    public Map<Class, DaoBase> daoImpMap;// 各个表操作的实现类映射

    private OrmLiteSqliteOpenHelper sqliteOpenHelper;

    public static DaoFactory getFactory() {
        if (null == factory) {
            factory = new DaoFactory(AppVariables.getApplicationContext());
        }
        return factory;
    }

    private DaoFactory(Context context) {
        globalContext = context;
        daoMap = new HashMap<Class, Dao<?, Integer>>();
        daoImpMap = new HashMap<Class, DaoBase>();
    }

    public void init(OrmLiteSqliteOpenHelper helper){
        if(null == helper)
            throw new IllegalArgumentException("helper should not be null");
        this.sqliteOpenHelper = helper;
    }

    public synchronized <T> Dao<T, Integer> getDao(Class<T> clazz) {
        if (null == daoMap.get(clazz)) {
            try {
                if(null == sqliteOpenHelper)
                    throw new NullPointerException("调用init方法，初始化数据库了吗？");
                Dao<T, Integer> dao = sqliteOpenHelper.getDao(clazz);
                daoMap.put(clazz, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (Dao<T, Integer>)daoMap.get(clazz);
    }

    public void reset() {
        daoMap.clear();
        daoMap = null;
        daoImpMap.clear();
        daoImpMap = null;
        factory = null;
    }

}
