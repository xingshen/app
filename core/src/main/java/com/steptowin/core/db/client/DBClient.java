package com.steptowin.core.db.client;

import android.util.Log;

import com.steptowin.core.common.callback.CallMainRunnable;
import com.steptowin.core.common.callback.ICallback;
import com.steptowin.core.common.thread.CachedThreadExecutor;
import com.steptowin.core.db.RxSupportDB;
import com.steptowin.core.db.dao.DaoImp;
import com.steptowin.core.db.dao.DaoListImp;
import com.steptowin.core.db.operation.IQuery;
import com.steptowin.core.db.operation.Operation;
import com.steptowin.core.tools.RxSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Desc: 数据请求客户端, 使用前请先DBHelper初始化
 * @Author: zg
 * @Time: 2016/3/7 17:41
 */
public class DBClient {
    private Executor dbExecutor;
    private RxSupport rxSupport;

    private DBClient() {
        dbExecutor = CachedThreadExecutor.create();
        rxSupport = new RxSupportDB(dbExecutor);
    }

    public static DBClient create() {
        return new DBClient();
    }

    public <T, R> R execute(final DaoOperationInfo<T> daoOperationInfo) {
        if (daoOperationInfo.isSynchronous())
            return executeCommand(daoOperationInfo);

        if (daoOperationInfo.isObservable() && RxSupport.HAS_RX_JAVA) {
            return (R) rxSupport.createObservable(new Func1() {
                @Override
                public Object call(Object o) {
                    return executeCommand(daoOperationInfo);
                }
            });
        }

        final ICallback<T> callback = daoOperationInfo.getCallback();
        dbExecutor.execute(new CallMainRunnable<T>() {
            @Override
            public T obtainResponse() {
                return (T) executeCommand(daoOperationInfo);
            }

            @Override
            public void start() {
                if (null != callback)
                    callback.start();
            }

            @Override
            public void success(T o) {
                if (null != callback)
                    callback.success(o);
            }

            @Override
            public void failure(Throwable throwable) {
                if (null != callback)
                    callback.failure(throwable);
            }
        });

        return null;//异步返回为空
    }

    private <T, R> R executeCommand(DaoOperationInfo<T> daoOperationInfo) {
        R r = null;
        DaoListImp<T> daoListImp = new DaoListImp<T>(daoOperationInfo.getOperationClass());
        DaoImp<T> daoImp = new DaoImp<T>(daoOperationInfo.getOperationClass());
        switch (daoOperationInfo.getOperation()) {
            case INSERT:
                r = (R) daoListImp.insert(daoOperationInfo.getData());
                break;
            case INSERT_IF_NOT_EXIST:
                r = (R) daoListImp.insertIfNotExists(daoOperationInfo.getData());
                break;
            case INSERT_OR_UPDATE:
                r = (R) daoListImp.insertOrUpdate(daoOperationInfo.getData());
                break;
            case DELETE:
                r = (R) daoListImp.delete(daoOperationInfo.getData());
                break;
            case DELETE_ALL:
                r = (R) daoImp.deleteAll(daoOperationInfo.getOperationClass());
                break;
            case UPDATE:
                r = (R) daoListImp.update(daoOperationInfo.getData());
                break;
            case QUERY:
                if (null != daoOperationInfo.getQuery()) {
                    r = (R) daoImp.query(daoOperationInfo.getQuery().prepare());
                } else {
                    Log.w("DBClint", "query 操作需要给出 IQuery实例。。");
                    r = (R) new ArrayList<>();
                }
                break;
        }
        return r;
    }


    public <T> DaoOperationInfo.Builder createOperationBuilder(Class<T> clazz) {
        DaoOperationInfo.Builder builder = new DaoOperationInfo.Builder();
        return builder.operationClass(clazz);
    }

    public <T> DaoOperationInfo.Builder createAsynOperationBuilder(Class<T> clazz) {
        DaoOperationInfo.Builder builder = new DaoOperationInfo.Builder();
        return builder.operationClass(clazz).async().obserable(true);
    }

    public <T> long insert(T t) {
        DaoOperationInfo<T> daoOperationInfo = createOperationBuilder(t.getClass()).operation(Operation.INSERT).data(t).build();
        return (long) execute(daoOperationInfo);
    }

    public <T> Observable insertAsyn(T... t) {
        if (!checkMutableParams(t))
            return null;
        DaoOperationInfo.Builder builder = createAsynOperationBuilder(t[0].getClass());
        DaoOperationInfo<T> daoOperationInfo = builder.operation(Operation.INSERT).data(t).build();
        return (Observable) execute(daoOperationInfo);
    }


    public <T> long delete(T t) {
        DaoOperationInfo<T> daoOperationInfo = createOperationBuilder(t.getClass()).operation(Operation.DELETE).data(t).build();
        return (long) execute(daoOperationInfo);
    }

    public <T> Observable deleteAsyn(T... t) {
        if (!checkMutableParams(t))
            return null;
        DaoOperationInfo<T> daoOperationInfo = createAsynOperationBuilder(t[0].getClass()).operation(Operation.DELETE).data(t).build();
        return (Observable) execute(daoOperationInfo);
    }

    public <T> Observable deleteAll(Class<T> clazz) {
        DaoOperationInfo<T> daoOperationInfo = createAsynOperationBuilder(clazz).operation(Operation.DELETE_ALL).build();
        return (Observable) execute(daoOperationInfo);
    }

    public <T> long update(T t) {
        DaoOperationInfo<T> daoOperationInfo = createOperationBuilder(t.getClass()).operation(Operation.UPDATE).data(t).build();
        return (long) execute(daoOperationInfo);
    }

    public <T> Observable updateAsyn(T... t) {
        if (!checkMutableParams(t))
            return null;
        DaoOperationInfo<T> daoOperationInfo = createAsynOperationBuilder(t[0].getClass()).operation(Operation.UPDATE).data(t).build();
        return (Observable) execute(daoOperationInfo);
    }

    public <T> List<T> query(Class<T> clazz, IQuery query) {
        DaoOperationInfo<T> daoOperationInfo = createOperationBuilder(clazz).operation(Operation.QUERY).query(query).build();
        return (List<T>) execute(daoOperationInfo);
    }

    public <T> Observable<List<T>> queryAsyn(Class<T> clazz, IQuery<T> query) {
        DaoOperationInfo.Builder builder = createAsynOperationBuilder(clazz);
        DaoOperationInfo<T> daoOperationInfo = builder.operation(Operation.QUERY).query(query).build();
        return (Observable) execute(daoOperationInfo);
    }

    /**
     * @desc 检查可变参数是否有效。 不为空，返回true，否则返回false
     * <p/>
     * edit by zg
     */
    private <T> boolean checkMutableParams(T... t) {
        if (null == t || t.length == 0)
            return false;
        return true;
    }
}
