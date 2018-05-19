package com.steptowin.core.db.operation;

import com.j256.ormlite.stmt.PreparedQuery;

/**
 * @Desc: 查询操作接口，此接口实例会传递给{@link com.steptowin.core.db.client.DaoOperationInfo}使用。默认查询操作，可调用{@link Querys}
 * @Author: zg
 * @Time: 2016/3/9 14:46
 */
public interface IQuery<T> {
    enum Order{
        ASC/*升*/,DESC/*降*/
    }

    IQuery<T> orderBy(String column, Order order);

    /**
     * {@link PreparedQuery}是ormlite查询操作最后环节
     * @return
     */
    PreparedQuery<T> prepare();
}
