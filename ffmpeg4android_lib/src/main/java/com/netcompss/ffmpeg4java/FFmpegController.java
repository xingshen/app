package com.netcompss.ffmpeg4java;


import android.content.Context;

import com.netcompss.ffmpeg4android.FFmpegTool;
import com.netcompss.ffmpeg4java.ShellUtils.ShellCallback;
import com.steptowin.core.common.callback.SimpleSubscriber;
import com.steptowin.core.common.thread.CachedThreadExecutor;
import com.steptowin.core.common.thread.MainThreadExecutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FFmpegController {
    private static FFmpegController instance;

    private final static String TAG = "FFMPEG";

    private File mFileTemp;

    private String mCmdCat = "sh cat";

    Executor executor;

    Executor uiExecutor;

    Context context;


    private FFmpegController() {
    }

    private FFmpegController(Context context) {
        executor = CachedThreadExecutor.create();
        uiExecutor = new MainThreadExecutor();
        this.context = context;
        FFmpegTool.installBinaries(context, false);
    }

    public static FFmpegController getInstance(Context context) {
        if (null == instance)
            instance = new FFmpegController(context);
        return instance;
    }

    public void killProcessor(SimpleSubscriber<Object> subscriber){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                try {
                    FFmpegTool.killVideoProcessor(false,false);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

//
//        try {
//            FFmpegTool.killVideoProcessor(false,false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 拼接几个视频
     * @param videos
     * @param out
     * @param subscriber
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Subscription concatVideo(final ArrayList<Clip> videos, final Clip out, SimpleSubscriber<Object> subscriber) throws IOException, InterruptedException {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                try {
                    FFmpegTool.concatVideo(videos, out, new ShellCallback() {
                        @Override
                        public void shellOut(String shellLine) {

                        }

                        @Override
                        public void processComplete(int exitValue) {
                            subscriber.onCompleted();
                        }
                    });
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 通过减小码率来压缩
     */
    public Subscription compressVideo(final Context context, final String orginPath, final String outPath, SimpleSubscriber<Object> subscriber) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                try {
                    Clip in = new Clip(orginPath);
                    Clip out = new Clip(outPath);
                    out.videoBitrate = FFmpegTool.VR_0_75M;
                    FFmpegTool.compressVideo(context, in, out, new ShellCallback() {
                        @Override
                        public void shellOut(String shellLine) {

                        }

                        @Override
                        public void processComplete(int exitValue) {
                            subscriber.onCompleted();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public class Argument {
        String key;
        String value;

        public static final String VIDEOCODEC = "-vcodec";
        public static final String AUDIOCODEC = "-acodec";

        public static final String VIDEOBITSTREAMFILTER = "-vbsf";
        public static final String AUDIOBITSTREAMFILTER = "-absf";

        public static final String VERBOSITY = "-v";
        public static final String FILE_INPUT = "-i";
        public static final String SIZE = "-s";
        public static final String FRAMERATE = "-r";
        public static final String FORMAT = "-f";
        public static final String BITRATE_VIDEO = "-b:v";

        public static final String BITRATE_AUDIO = "-b:a";
        public static final String CHANNELS_AUDIO = "-ac";
        public static final String FREQ_AUDIO = "-ar";

        public static final String STARTTIME = "-ss";
        public static final String DURATION = "-t";

    }


}