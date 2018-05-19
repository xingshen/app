package com.steptowin.core.db.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */
public class DaoImp<T> extends DaoAbs<T> {
    private Class<T> mClazz;

    public DaoImp(Class<T> clazz) {
        mClazz = clazz;
    }

    @Override
    public synchronized Long insert(T t) {
        if (null != t) {
            try {
                return Long.valueOf(DaoFactory.getFactory().getDao(mClazz).create(t));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1l;
    }

    @Override
    public T insertIfNotExists(T t) {
        if (null != t) {
            try {
                return DaoFactory.getFactory().getDao(mClazz).createIfNotExists(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Long insertOrUpdate(T t) {
        if (null != t) {
            try {
                DaoFactory.getFactory().getDao(mClazz).createOrUpdate(t);
                return 0l;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1l;
    }

    @Override
    public synchronized Long delete(T t) {
        if (null != t) {
            try {
                return Long.valueOf(DaoFactory.getFactory().getDao(mClazz).delete(t));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1l;
    }

    @Override
    public Boolean deleteAll(Class<T> clazz) {
        try {
            Dao<T, Integer> dao = DaoFactory.getFactory().getDao(clazz);
            TableUtils.clearTable(dao.getConnectionSource(),clazz);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized Long update(T t) {
        if (null != t) {
            try {
                return Long.valueOf(DaoFactory.getFactory().getDao(mClazz).update(t));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1l;
    }

    @Override
    public synchronized List<T> query(PreparedQuery<T> preparedQuery) {
        if (null != preparedQuery) {
            try {
                return (List<T>) DaoFactory.getFactory().getDao(mClazz)
                        .query((preparedQuery));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public synchronized Long refresh(T t) {
        if (null != t) {
            try {
                return Long.valueOf(DaoFactory.getFactory().getDao(mClazz).refresh(t));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1l;
    }

    @Override
    public Long count() {
        try {
            return DaoFactory.getFactory().getDao(mClazz).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1l;
    }
}
