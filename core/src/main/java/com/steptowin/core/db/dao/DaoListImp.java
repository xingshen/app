package com.steptowin.core.db.dao;

import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */
public class DaoListImp<E> extends DaoAbs<Collection<E>> {
    private Class<E> mClazz;
    private DaoImp<E> mDao;

    public DaoListImp(Class<E> clazz) {
        mClazz = clazz;
        mDao = new DaoImp<>(clazz);
    }


    @Override
    public Long insert(Collection<E> es) {
        Long index = -1l;
        if (!checkCollection(es))
            return index;
        for (E e : es) {
            index = mDao.insert(e);
        }
        return index;
    }

    @Override
    public Collection<E> insertIfNotExists(Collection<E> es) {
        if (!checkCollection(es))
            return Collections.EMPTY_LIST;
        for (E e : es) {
            mDao.insert(e);
        }
        return es;
    }

    @Override
    public Long insertOrUpdate(Collection<E> es) {
        Long index = -1l;
        if (!checkCollection(es))
            return index;
        for (E e : es) {
            index = mDao.insertOrUpdate(e);
        }
        return index;
    }

    @Override
    public Long delete(Collection<E> es) {
        Long index = -1l;
        if (!checkCollection(es))
            return index;
        try {
            return Long.valueOf(DaoFactory.getFactory().getDao(mClazz).delete(es));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Long update(Collection<E> es) {
        Long index = -1l;
        if (!checkCollection(es))
            return index;
        for (E e : es) {
            mDao.update(e);
        }
        return index;
    }

    @Override
    public List<Collection<E>> query(PreparedQuery<Collection<E>> preparedQuery) {
        return null;
    }


    @Override
    public Long refresh(Collection<E> es) {
        Long index = -1l;
        if (!checkCollection(es))
            return index;
        for (E e : es) {
            index = mDao.refresh(e);
        }
        return index;
    }

    @Override
    public Long count() {
        return -1l;
    }

    private boolean checkCollection(Collection<E> es) {
        if (null == es || es.size() == 0)
            return false;
        return true;
    }
}
