package com.steptowin.core.db.operation;

import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.steptowin.core.db.dao.DaoFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Desc: 生成常用的几种查询操作
 * 还可以通过实现{@link IQuery}接口，调用{@link #createBuilder(Class)}自定义查询操作
 * @Author: zg
 * @Time: 2016/3/9 17:25
 */
public class Querys {

    /**
     * 可以自定义查询
     *
     * @return
     */
    public static <T> QueryBuilder<T, Integer> createBuilder(Class<T> clazz) {
        QueryBuilder mQueryBuilder = DaoFactory.getFactory().getDao(clazz).queryBuilder();
        return mQueryBuilder;
    }

    /**
     * 单个条件查询
     *
     * @param clazz
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> IQuery<T> queryForCondition(Class<T> clazz, String key, Object value) {
        Condition condition = new Condition(key, value);
        return new ConditionQuery(clazz).setCondition(condition);
    }

    /**
     * 多条件查询
     *
     * @param clazz
     * @param conditions
     * @param <T>
     * @return
     */
    public static <T> IQuery<T> queryForConditions(Class<T> clazz, List<Condition> conditions) {
        return new ConditionQuery(clazz).setCondition(conditions);
    }

    /**
     * 查询所有
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> IQuery<T> queryForAll(Class<T> clazz) {
        return new AllQuery(clazz);
    }

    /**
     * 分页查询
     *
     * @param clazz
     * @param page
     * @param <T>
     * @return
     */
    public static <T> IQuery<T> queryForPage(Class<T> clazz, IPage page) {
        return new PageQuery(clazz).setPage(page);
    }


    public static abstract class BaseQuery<T> implements IQuery {
        protected Class<T> mClazz;
        protected QueryBuilder<T, Integer> queryBuilder;

        public BaseQuery(Class<T> clazz) {
            this.mClazz = clazz;
            queryBuilder = createBuilder(clazz);
        }

        @Override
        public IQuery orderBy(String column, Order order) {
            queryBuilder.orderBy(column, order == Order.ASC);
            return this;
        }
    }


    static class ConditionQuery<T> extends BaseQuery {
        private List<Condition> mConditions = new ArrayList<>();

        public ConditionQuery(Class<T> clazz) {
            super(clazz);
        }

        public ConditionQuery<T> setCondition(Condition... condition) {
            return setCondition(Arrays.asList(condition));
        }

        public ConditionQuery<T> setCondition(List<Condition> condition) {
            if (null != condition)
                mConditions = condition;
            return this;
        }


        @Override
        public PreparedQuery prepare() {
            try {
                Where<T, Integer> where = queryBuilder.where();
                for (Condition condition : mConditions) {
                    where = where.eq(condition.key, condition.value);
                    switch (condition.logic) {
                        case Condition.LOGIC_AND:
                            where.and();
                            break;
                        case Condition.LOGIC_OR:
                            where.or();
                            break;
                        case Condition.LOGIC_NOT:
                            where.not();
                            break;
                    }
                }
                return queryBuilder.prepare();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 条件查询的单个条件类
     */
    public static final class Condition {
        final static int LOGIC_UNDIFNE = 0;
        final static int LOGIC_OR = 1;
        final static int LOGIC_AND = 2;
        final static int LOGIC_NOT = 3;

        int logic = LOGIC_UNDIFNE;
        String key;
        Object value;

        public Condition(String k, Object v) {
            key = k;
            value = v;
        }

        public Condition setLogin(int l) {
            logic = l;
            return this;
        }
    }


    static final class AllQuery extends BaseQuery {

        public AllQuery(Class clazz) {
            super(clazz);
        }

        @Override
        public PreparedQuery prepare() {
            try {
                return queryBuilder.prepare();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static final class PageQuery extends BaseQuery {
        private IPage mPage;

        public PageQuery(Class clazz) {
            super(clazz);
        }

        public PageQuery setPage(IPage page) {
            mPage = page;
            return this;
        }

        @Override
        public PreparedQuery prepare() {
            try {
                if (null != mPage)
                    return queryBuilder.limit(mPage.getCount()).offset(mPage.getOffset()).prepare();
                else
                    Log.w("" + getClass().getSimpleName(), "分页查询需要给出页码指示。。");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 分页查询接口
     */
    public interface IPage {
        /**
         * 每页查询的数量
         *
         * @return
         */
        long getCount();

        /**
         * 从哪个位置查询，需要根据当前页码计算
         *
         * @return
         */
        long getOffset();

        IPage next();

        void reset();
    }


    public static class Page implements IPage {
        public static final int DEFAULT_COUNT = 10;
        private long count = DEFAULT_COUNT;//每页条数

        public long index;//当页第一条所在行数

        private Page nextPage;

        private Page() {
        }

        public Page(long count) {
            this.count = count;
            initNextPage();
        }

        private void initNextPage() {
            nextPage = new Page();
            nextPage.count = count;
            nextPage.index = 0 - count;//第0页，（不存在）
        }

        @Override
        public IPage next() {
            nextPage.index = nextPage.index + count;
            return nextPage;
        }

        @Override
        public void reset() {
            initNextPage();
        }

        @Override
        public long getCount() {
            return count;
        }

        @Override
        public long getOffset() {
            return index;
        }
    }
}
