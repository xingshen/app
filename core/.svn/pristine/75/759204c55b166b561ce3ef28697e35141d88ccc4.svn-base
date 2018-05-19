package com.steptowin.core.http.okhttp.upload;

import android.support.annotation.NonNull;

/**
 * @Desc: 文件上传包
 * @Author: zg
 * @Time:
 */
public class FileUploadInfo {
    private String url;
    private String name;
    private String fileName;
    private String filePath;
    private ProgressSubscriber subscriber;

    public FileUploadInfo(Builder builder) {
        this.url = builder.url;
        this.filePath = builder.filePath;
        this.name = builder.name;
        this.fileName = builder.fileName;
        this.subscriber = builder.subscriber;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public ProgressSubscriber getSubscriber() {
        return subscriber;
    }

    public static final class Builder {
        @NonNull
        private String url;
        @NonNull
        private String filePath;
        private String name;
        private String fileName;
        private ProgressSubscriber subscriber;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder subscriber(ProgressSubscriber subscriber) {
            this.subscriber = subscriber;
            return this;
        }

        public static Builder defaultBuilder(){
            Builder builder = new Builder();
            return builder.name("Filedata").fileName("temp");
        }

        public FileUploadInfo build() {
            if (url == null) throw new IllegalStateException("url == null");
            if (filePath == null) throw new IllegalStateException("filePath == null");
            return new FileUploadInfo(this);
        }


    }


}
