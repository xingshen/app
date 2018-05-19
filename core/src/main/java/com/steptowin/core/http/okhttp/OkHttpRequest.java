package com.steptowin.core.http.okhttp;

import android.content.Context;

import com.steptowin.core.http.okhttp.download.FileDownloadCallback;
import com.steptowin.core.http.okhttp.download.FileDownloadTask;
import com.steptowin.core.http.okhttp.upload.FileUploadInfo;
import com.steptowin.core.http.okhttp.upload.ProgressRequestBody;
import com.steptowin.core.http.okhttp.upload.ProgressSubscriber;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *@desc: okhttp请求,包含文件上传和下载
 * 注意:需要执行初始化{@link OkHttpFinal#defaultInit(Context)}
 * 请求常用接口使用{@link com.steptowin.core.http.retrofit.RetrofitClient}
 *@author zg
 *@time 16/6/21
 */
public class OkHttpRequest {

    /**
     * 下载文件
     *
     * @param url
     * @param target
     * @param callback
     */
    public static void download(String url, File target, FileDownloadCallback callback) {
        if (!StringUtils.isEmpty(url) && target != null) {
            FileDownloadTask task = new FileDownloadTask(url, target, callback);
            task.execute();
        }
    }

    /**
     * 文件上传,可以使用{@link Subscription#unsubscribe()}来停止上传，但是目前没有继续上传的方法
     *
     * @param uploadInfo
     */
    public static Subscription upload(final FileUploadInfo uploadInfo) {
        return Observable.just(uploadInfo).subscribeOn(Schedulers.io()).map(new Func1<FileUploadInfo, Request>() {
            @Override
            public Request call(FileUploadInfo fileUploadInfo) {
                return getRequestFromFileUploadInfo(fileUploadInfo);
            }
        }).map(new Func1<Request, Response>() {
            @Override
            public Response call(Request request) {
                try {
                    return executeUpload(request);
                } catch (IOException e) {
                    e.printStackTrace();
                    uploadInfo.getSubscriber().onError(e);
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                try {
                    if (null != response)
                        uploadInfo.getSubscriber().onCompleted(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    uploadInfo.getSubscriber().onError(e);
                }
            }
        });
    }

    public static Subscription upload(String url, String filePath, ProgressSubscriber subscriber) {
        FileUploadInfo.Builder builder = FileUploadInfo.Builder.defaultBuilder();
        return upload(builder.url(url).filePath(filePath).subscriber(subscriber).build());
    }

    /**
     * 把要上传的文件，封装成okhttp请求
     *
     * @param uploadInfo
     * @return
     */
    private static Request getRequestFromFileUploadInfo(FileUploadInfo uploadInfo) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart(uploadInfo.getName(), uploadInfo.getFileName(), RequestBody.create(null, new File(uploadInfo.getFilePath())));
        MultipartBody multipartBody = builder.build();

        ProgressRequestBody requestBody = new ProgressRequestBody(multipartBody, uploadInfo.getSubscriber());
        Request request = new Request.Builder().url(uploadInfo.getUrl()).post(requestBody).build();
        return request;
    }

    private static Response executeUpload(Request request) throws IOException {
        return OkHttpFinal.getInstance().getOkHttpClient().newCall(request).execute();
    }

}
