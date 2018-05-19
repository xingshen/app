package com.steptowin.core.db.client;

import com.steptowin.core.common.callback.ICallback;
import com.steptowin.core.db.operation.IQuery;
import com.steptowin.core.db.operation.Operation;

import java.util.Arrays;
import java.util.List;

/**
 * @Desc: 数据库请求的包装信息类
 * @Author: zg
 * @Time: 2016/3/9 17:50
 */
public class DaoOperationInfo<T> {

    private Class<T> mClass;
    private Operation mOperation;
    private List<T> mData;
    private IQuery mQuery;//查询操作使用的接口
    private ICallback<T> mCallback;//异步操作时的回调

    private boolean isObservable;
    private boolean isSynchronous;
    private boolean isShowProgressDialog;


    private DaoOperationInfo(Builder builder) {
        this.mClass = builder.mClass;
        this.mOperation = builder.mOperation;
        this.mData = builder.mData;
        this.mQuery = builder.mQuery;
        this.mCallback = builder.mCallback;
        this.isShowProgressDialog = builder.isShowProgressDialog;
        this.isObservable = builder.isObservable;
        this.isSynchronous = builder.isSynchronous;
    }

    public Class<T> getOperationClass() {
        return this.mClass;
    }

    public Operation getOperation() {
        return mOperation;
    }

    public List<T> getData() {
        return mData;
    }

    public IQuery getQuery() {
        return mQuery;
    }


    public ICallback<T> getCallback() {
        return mCallback;
    }

    public boolean isObservable() {
        return isObservable;
    }

    public boolean isSynchronous() {
        return isSynchronous;
    }

    public static class Builder<T> {
        private Class<T> mClass;
        private Operation mOperation;
        private List<T> mData;
        private IQuery mQuery;
        private ICallback<T> mCallback;
        private boolean isObservable;
        private boolean isSynchronous;
        private boolean isShowProgressDialog;

        public Builder() {
            ensureDefaultBuild();
        }

        public Builder operationClass(Class<T> clazz) {
            this.mClass = clazz;
            return this;
        }

        public Builder operation(Operation operation) {
            this.mOperation = operation;
            return this;
        }

        public Builder data(T... ts) {
            if (null != ts)
                mData = Arrays.asList(ts);
            return this;
        }

        public Builder data(List<T> ds) {
            if (null != ds)
                mData = ds;
            return this;
        }


        public Builder query(IQuery query) {
            this.mQuery = query;
            return this;
        }

        public Builder callback(ICallback<T> callback) {
            this.mCallback = callback;
            return this;
        }

        public Builder obserable(boolean isObservable) {
            this.isObservable = isObservable;
            return this;
        }

        public Builder async() {
            this.isSynchronous = false;
            return this;
        }

        private void ensureDefaultBuild() {
            isSynchronous = true;
            isShowProgressDialog = true;
        }

        public DaoOperationInfo build() {
            return new DaoOperationInfo(this);
        }
    }
}
