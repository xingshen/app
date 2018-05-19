package com.netcompss.ffmpeg4android;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.netcompss.ffmpeg4java.Clip;
import com.netcompss.ffmpeg4java.FFmpegController;
import com.netcompss.ffmpeg4java.ShellUtils;
import com.netcompss.loader.LoadJNI;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * desc:ffmpeg工具类，以命令的方式运行
 * author：zg
 * date:16/11/14
 * time:下午8:04
 */
public class FFmpegTool {
    private final static String TAG = "FFMPEG";
    private static String mCmdCat = "sh cat";

    /**
     * 码率
     */
    public static final String VR_0_5M = "524288";
    public static final String VR_0_75M = "786432";
    public static final String VR_1M = "1048576";
    public static final String VR_1_5M = "1572864";
    public static final String VR_2M = "2097152";

    private static String vrate = VR_0_75M;

    private static String mFfmpegBin;

    public interface CompressListener {
        void onSuccess(String orginPath, String savePath);

        void onFail(Throwable e);
    }


    /**
     * 通过减小码率来压缩
     * 1分钟视频，512kbps，4M左右
     * 1024kbps，8M左右
     * 压缩命令
     * String commandStr2 = "ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -s 160x120 -r 30 -aspect 3:4 -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -b 2097152 /sdcard/videokit/out2.mp4";
     */
    static final String CMD_COMPRESS_VIDEO = "ffmpeg -y -i %s -strict experimental -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -vb " + vrate + " %s";

    public static void compressVideo(final Context context, final String orginPath, final String savePath, String workFolder, final CompressListener listener) {
        LoadJNI vk = new LoadJNI();
        try {
            vk.run(GeneralUtils.utilConvertToComplex(String.format(CMD_COMPRESS_VIDEO, orginPath, savePath)), workFolder, context);

            MediaScannerConnection.scanFile(context, new String[]{savePath}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                    if (null != listener) {
                        if (context instanceof Activity) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(orginPath, savePath);
                                    return;
                                }
                            });
                        } else {
                            listener.onSuccess(orginPath, savePath);
                        }
                    }

                }
            });
        } catch (final Throwable e) {
            Log.e(Prefs.TAG, "vk run exeption.", e);
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFail(e);
                        return;
                    }
                });
            } else {
                listener.onFail(e);
            }

        }
    }


    /**
     * 压缩的同时添加水印
     * String commandStr2 = "ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -s 160x120 -r 30 -aspect 3:4 -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -b 2097152 -vf \"movie=logo.png [logo]; [in][logo] overlay=5:5 [out]\" -preset ultrafast /sdcard/videokit/out2.mp4";
     */
    static final String CMD_COMPRESS_VIDEO_WITH_LOGO_PRE = "ffmpeg -y -i %s -strict experimental -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -vb 524288 -vf";
    static final String CMD_COMPRESS_VIDE_WITH_LOGO = "movie=%s [watermark]; [in][watermark] overlay=10:10 [out]";

    public static void compressVideoWithLogo(Context context, final String orginPath, final String savePath, String workFolder, String logoPath, final CompressListener listener) {
        LoadJNI vk = new LoadJNI();
        try {
            String formatCmdPre = String.format(CMD_COMPRESS_VIDEO_WITH_LOGO_PRE, orginPath);
            String formatCmd = String.format(CMD_COMPRESS_VIDE_WITH_LOGO, logoPath);
            String[] args = GeneralUtils.utilConvertToComplex(formatCmdPre);
            args = ArrayUtils.add(args, formatCmd);
            args = ArrayUtils.add(args, savePath);
            vk.run(args, workFolder, context);

            MediaScannerConnection.scanFile(context, new String[]{savePath}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                    if (null != listener)
                        listener.onSuccess(orginPath, savePath);
                }
            });
        } catch (Throwable e) {
            Log.e(Prefs.TAG, "vk run exeption.", e);
            if (null != listener)
                listener.onFail(e);
        }
    }

    /**
     * ffmpeg -y -i /sdcard/Video/1.MTS -strict experimental -vcodec libx264 -preset ultrafast -crf 24 /sdcard/videokit/out.mp4
     * ffmpeg -y -i %s -strict experimental -vcodec libx264 -preset ultrafast -crf 24 %s
     */
    static final String CMD_TRANSCODE_VIDEO = "ffmpeg -y -i %s -strict experimental -vcodec libx264 -preset ultrafast -crf 24 %s";

    public static void transCodeVideo(Context context, final String orginPath, String workFolder, final String savePath, final CompressListener listener) {
        LoadJNI vk = new LoadJNI();
        try {
            String formatCmd = String.format(CMD_TRANSCODE_VIDEO, orginPath, savePath);
            String[] args = GeneralUtils.utilConvertToComplex(formatCmd);

            vk.run(args, workFolder, context);

            MediaScannerConnection.scanFile(context, new String[]{savePath}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                    if (null != listener) {
                        listener.onSuccess(orginPath, savePath);
                    }
                }
            });
        } catch (Throwable e) {
            Log.e(Prefs.TAG, "vk run exeption.", e);
            if (null != listener) {
                listener.onFail(e);
            }
        }
    }

    public interface ConcatListener {
        void onSuccess(String savePath);

        void onFail(Throwable e);
    }


    /**
     * 拼接 :我们需要将需要拼接的视频文件按以下格式保存在一个列表 list.txt 中：
     *file '/path/to/file1'
     *file '/path/to/file2'
     *file '/path/to/file3'
     *相应的命令为：
     *ffmpeg -f concat -i **list.txt** -c copy output.mp4
     * ffmpeg -i concat:"intermediate1.mpg|intermediate2.mpg" -c copy intermediate_all.mpg
     */
    /**
     * 第一种合并方法
     */
    static final String CMD_CONCAT_VIDEOS_BY_FILE = "ffmpeg -y -f concat -i %s -c copy %s";

    /**
     * 第二种合并方法
     */
    static final String CMD_CONCAT_VIDEOS = "ffmpeg -i \"concat:%s\" -c copy %s";

    /**
     * "ffmpeg -y -i /storage/emulated/0/videokit/sample.mp4 -i /storage/emulated/0/videokit/in.mp4 -strict experimental -filter_complex [0:v]scale=640x480,setsar=1:1[v0];[1:v]scale=640x480,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1 -ab 48000 -ac 2 -ar 22050 -s 640x480 -r 30 -vcodec mpeg4 -b 786432 %s"
     * "ffmpeg -y %s -strict experimental -filter_complex [0:v]scale=640x480,setsar=1:1[v0];[1:v]scale=640x480,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1 -ab 48000 -ac 2 -ar 22050 -s 640x480 -r 30 -vcodec mpeg4 -b 786432 %s"
     *
     * @param context
     * @param filePath
     * @param outputPath
     * @param workFolder
     * @param listener
     */
    static final String CMD_CONCAT_VIDEOS_2 = "ffmpeg -y %s -strict experimental -filter_complex [0:v]scale=640x480,setsar=1:1[v0];[1:v]scale=640x480,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1 -ab 48000 -ac 2 -ar 22050 -s 640x480 -r 30 -vcodec mpeg4 -b 786432 %s";

    public static void concatVideos(final Context context, String filePath, final String outputPath, String workFolder, final ConcatListener listener) {
        LoadJNI vk = new LoadJNI();
        try {
            vk.run(GeneralUtils.utilConvertToComplex(String.format(CMD_CONCAT_VIDEOS_BY_FILE, filePath, outputPath)), workFolder, context);

            MediaScannerConnection.scanFile(context, new String[]{outputPath}, new String[]{"video/*"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                    if (null != listener) {
                        if (context instanceof Activity) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(outputPath);
                                    return;
                                }
                            });
                        } else {
                            listener.onSuccess(outputPath);
                        }
                    }

                }
            });
        } catch (final Throwable e) {
            Log.e(Prefs.TAG, "vk run exeption.", e);
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFail(e);
                        return;
                    }
                });
            } else {
                listener.onFail(e);
            }

        }
    }

    public static void installBinaries(Context context, boolean overwrite) {
        mFfmpegBin = installBinary(context, R.raw.ffmpeg, "ffmpeg", overwrite);
    }

    public String getBinaryPath() {
        return mFfmpegBin;
    }

    private static String installBinary(Context ctx, int resId, String filename, boolean upgrade) {
        try {
            File f = new File(ctx.getDir("bin", 0), filename);
            if (f.exists()) {
                f.delete();
            }
            copyRawFile(ctx, resId, f, "0755");
            return f.getCanonicalPath();
        } catch (Exception e) {
            Log.e(TAG, "installBinary failed: " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Copies a raw resource file, given its ID to the given location
     *
     * @param ctx   context
     * @param resid resource id
     * @param file  destination file
     * @param mode  file permissions (E.g.: "755")
     * @throws IOException          on error
     * @throws InterruptedException when interrupted
     */
    private static void copyRawFile(Context ctx, int resid, File file, String mode) throws IOException, InterruptedException {
        final String abspath = file.getAbsolutePath();
        // Write the iptables binary
        final FileOutputStream out = new FileOutputStream(file);
        final InputStream is = ctx.getResources().openRawResource(resid);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        is.close();
        // Change the permissions
        Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
    }


    /**
     * * 通过减小码率来压缩
     * 1分钟视频，512kbps，4M左右
     * 1024kbps，8M左右
     * 压缩命令
     * String commandStr2 = "ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -s 160x120 -r 30 -aspect 3:4 -ab 48000 -ac 2 -ar 22050 -vcodec mpeg4 -b 2097152 /sdcard/videokit/out2.mp4";
     *
     * @param context
     * @param orginPath 源文件路径
     * @param out       输入路径
     * @param sc
     * @throws Exception
     */
    public static void compressVideo(final Context context, Clip orginPath, Clip out, ShellUtils.ShellCallback sc) throws Exception {
        ArrayList<String> cmd = new ArrayList<String>();


        String mediaPath = new File(orginPath.path).getCanonicalPath();

        cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(mediaPath);
        cmd.add("-strict");
        cmd.add("experimental");
        cmd.add("-ab");
        cmd.add("48000");
        cmd.add("-ac");
        cmd.add("2");
        cmd.add("-ar");
        cmd.add("22050");
        cmd.add("-vcodec");
        cmd.add("mpeg4");
        cmd.add("-vb");
        cmd.add(out.videoBitrate);

        File fileOut = new File(out.path);

        cmd.add(fileOut.getCanonicalPath());

        execFFMPEG(cmd, sc);
    }

    public static void concatVideo(final ArrayList<Clip> videos, final Clip out, final ShellUtils.ShellCallback sc) throws Exception {
        StringBuffer sbCat = new StringBuffer();
        for (Clip c : videos) {
            sbCat.append("file ");
            sbCat.append(c.path);
            sbCat.append("\r\n");
        }
        sbCat.deleteCharAt(sbCat.length() - 1);
        File fileExportOut = new File(out.path);
        if (fileExportOut.exists())
            fileExportOut.delete();
        File parentFile = fileExportOut.getParentFile();
        parentFile.mkdirs();
        File tempFile = new File(parentFile, "list.txt");
        if (tempFile.exists()) {
            tempFile.delete();
        } else {
            tempFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(sbCat.toString().getBytes());
        fos.close();

        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");
        cmd.add("-f");
        cmd.add("concat");
        cmd.add("-i");
        cmd.add(tempFile.getCanonicalPath());
        cmd.add("-c");
        cmd.add("copy");

        cmd.add(fileExportOut.getCanonicalPath());

        execFFMPEG(cmd, sc);
    }

    public static Clip createSlideshowFromImagesAndAudio(ArrayList<Clip> images, Clip audio, Clip out, String mFileTemp, int durationPerSlide, ShellUtils.ShellCallback sc) throws Exception {

        final String imageBasePath = new File(mFileTemp, "image-").getCanonicalPath();
        final String imageBaseVariablePath = imageBasePath + "%03d.jpg";


        ArrayList<String> cmd = new ArrayList<String>();


        String newImagePath = null;
        int imageCounter = 0;

        Clip imageCover = images.get(0); //add the first image twice

        cmd = new ArrayList<String>();
        cmd.add(mFfmpegBin);
        cmd.add("-y");

        cmd.add("-i");
        cmd.add(new File(imageCover.path).getCanonicalPath());

        if (out.width != -1 && out.height != -1) {
            cmd.add("-s");
            cmd.add(out.width + "x" + out.height);
        }

        newImagePath = imageBasePath + String.format(Locale.US, "%03d", imageCounter++) + ".jpg";
        cmd.add(newImagePath);

        execFFMPEG(cmd, sc);

        for (Clip image : images) {
            cmd = new ArrayList<String>();
            cmd.add(mFfmpegBin);
            cmd.add("-y");

            cmd.add("-i");
            cmd.add(new File(image.path).getCanonicalPath());

            if (out.width != -1 && out.height != -1) {
                cmd.add("-s");
                cmd.add(out.width + "x" + out.height);
            }

            newImagePath = imageBasePath + String.format(Locale.US, "%03d", imageCounter++) + ".jpg";
            cmd.add(newImagePath);

            execFFMPEG(cmd, sc);


        }

        //then combine them
        cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");

        cmd.add("-loop");
        cmd.add("0");

        cmd.add("-f");
        cmd.add("image2");

        cmd.add("-r");
        cmd.add("1/" + durationPerSlide);

        cmd.add("-i");
        cmd.add(imageBaseVariablePath);

        cmd.add("-strict");
        cmd.add("-2");//experimental

        String fileTempMpg = new File(mFileTemp, "tmp.mpg").getCanonicalPath();

        cmd.add(fileTempMpg);

        execFFMPEG(cmd, sc);

        //now combine and encode
        cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");

        cmd.add("-i");
        cmd.add(fileTempMpg);

        if (audio != null && audio.path != null) {
            cmd.add("-i");
            cmd.add(new File(audio.path).getCanonicalPath());

            cmd.add("-map");
            cmd.add("0:0");

            cmd.add("-map");
            cmd.add("1:0");

            cmd.add(FFmpegController.Argument.AUDIOCODEC);
            cmd.add("aac");

            cmd.add(FFmpegController.Argument.BITRATE_AUDIO);
            cmd.add("128k");

        }

        cmd.add("-strict");
        cmd.add("-2");//experimental

        cmd.add(FFmpegController.Argument.VIDEOCODEC);


        if (out.videoCodec != null)
            cmd.add(out.videoCodec);
        else
            cmd.add("mpeg4");

        if (out.videoBitrateK != -1) {
            cmd.add(FFmpegController.Argument.BITRATE_VIDEO);
            cmd.add(out.videoBitrateK + "k");
        }

        cmd.add(new File(out.path).getCanonicalPath());


        execFFMPEG(cmd, sc);

        return out;
    }


    public static void concatAndTrimFilesMPEG(ArrayList<Clip> videos, Clip out, boolean preConvert, ShellUtils.ShellCallback sc) throws Exception {

        int idx = 0;

        if (preConvert) {
            for (Clip mdesc : videos) {
                if (mdesc.path == null)
                    continue;

                //extract MPG video
                ArrayList<String> cmd = new ArrayList<String>();

                cmd.add(mFfmpegBin);
                cmd.add("-y");
                cmd.add("-i");
                cmd.add(mdesc.path);

                if (mdesc.startTime != null) {
                    cmd.add("-ss");
                    cmd.add(mdesc.startTime);
                }

                if (mdesc.duration != -1) {
                    cmd.add("-t");
                    cmd.add(String.format(Locale.US, "%f", mdesc.duration));

                }

				/*
                cmd.add ("-acodec");
				cmd.add("pcm_s16le");

				cmd.add ("-vcodec");
				cmd.add("mpeg2video");
				*/

                if (out.audioCodec == null)
                    cmd.add("-an"); //no audio

                //cmd.add("-strict");
                //cmd.add("experimental");

                //everything to mpeg
                cmd.add("-f");
                cmd.add("mpeg");
                cmd.add(out.path + '.' + idx + ".mpg");

                execFFMPEG(cmd, sc);

                idx++;
            }
        }

        StringBuffer cmdRun = new StringBuffer();

        cmdRun.append(mCmdCat);

        idx = 0;

        for (Clip vdesc : videos) {
            if (vdesc.path == null)
                continue;

            if (preConvert)
                cmdRun.append(out.path).append('.').append(idx++).append(".mpg").append(' '); //leave a space at the end!
            else
                cmdRun.append(vdesc.path).append(' ');
        }

        String mCatPath = out.path + ".full.mpg";

        cmdRun.append("> ");
        cmdRun.append(mCatPath);

        String[] cmds = {"sh", "-c", cmdRun.toString()};
        Runtime.getRuntime().exec(cmds).waitFor();


        Clip mInCat = new Clip();
        mInCat.path = mCatPath;

        processVideo(mInCat, out, false, sc);

        out.path = mCatPath;
    }

    public static void extractAudio(Clip mdesc, String audioFormat, File audioOutPath, ShellUtils.ShellCallback sc) throws IOException, InterruptedException {

        //no just extract the audio
        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(new File(mdesc.path).getCanonicalPath());

        cmd.add("-vn");

        if (mdesc.startTime != null) {
            cmd.add("-ss");
            cmd.add(mdesc.startTime);
        }

        if (mdesc.duration != -1) {
            cmd.add("-t");
            cmd.add(String.format(Locale.US, "%f", mdesc.duration));

        }

        cmd.add("-f");
        cmd.add(audioFormat); //wav

        //everything to WAV!
        cmd.add(audioOutPath.getCanonicalPath());

        execFFMPEG(cmd, sc);

    }

    public static void processVideo(Clip in, Clip out, boolean enableExperimental, ShellUtils.ShellCallback sc) throws Exception {

        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");

        if (in.format != null) {
            cmd.add(FFmpegController.Argument.FORMAT);
            cmd.add(in.format);
        }

        if (in.videoCodec != null) {
            cmd.add(FFmpegController.Argument.VIDEOCODEC);
            cmd.add(in.videoCodec);
        }

        if (in.audioCodec != null) {
            cmd.add(FFmpegController.Argument.AUDIOCODEC);
            cmd.add(in.audioCodec);
        }

        cmd.add("-i");
        cmd.add(new File(in.path).getCanonicalPath());

        if (out.videoBitrateK > 0) {
            cmd.add(FFmpegController.Argument.BITRATE_VIDEO);
            cmd.add(out.videoBitrateK + "k");
        }

        if (out.width > 0) {
            cmd.add(FFmpegController.Argument.SIZE);
            cmd.add(out.width + "x" + out.height);

        }
        if (out.videoFps != null) {
            cmd.add(FFmpegController.Argument.FRAMERATE);
            cmd.add(out.videoFps);
        }

        if (out.videoCodec != null) {
            cmd.add(FFmpegController.Argument.VIDEOCODEC);
            cmd.add(out.videoCodec);
        }

        if (out.videoBitStreamFilter != null) {
            cmd.add(FFmpegController.Argument.VIDEOBITSTREAMFILTER);
            cmd.add(out.videoBitStreamFilter);
        }


        if (out.videoFilter != null) {
            cmd.add("-vf");
            cmd.add(out.videoFilter);
        }

        if (out.audioCodec != null) {
            cmd.add(FFmpegController.Argument.AUDIOCODEC);
            cmd.add(out.audioCodec);
        }

        if (out.audioBitStreamFilter != null) {
            cmd.add(FFmpegController.Argument.AUDIOBITSTREAMFILTER);
            cmd.add(out.audioBitStreamFilter);
        }
        if (out.audioChannels > 0) {
            cmd.add(FFmpegController.Argument.CHANNELS_AUDIO);
            cmd.add(out.audioChannels + "");
        }

        if (out.audioBitrate > 0) {
            cmd.add(FFmpegController.Argument.BITRATE_AUDIO);
            cmd.add(out.audioBitrate + "k");
        }

        if (out.format != null) {
            cmd.add("-f");
            cmd.add(out.format);
        }

        if (out.duration > 0) {
            cmd.add(FFmpegController.Argument.DURATION);
            cmd.add(out.duration + "");
        }

        if (enableExperimental) {
            cmd.add("-strict");
            cmd.add("-2");//experimental
        }

        cmd.add(new File(out.path).getCanonicalPath());

        execFFMPEG(cmd, sc);

    }

    private class FileMover {

        InputStream inputStream;
        File destination;

        public FileMover(InputStream _inputStream, File _destination) {
            inputStream = _inputStream;
            destination = _destination;
        }

        public void moveIt() throws IOException {

            OutputStream destinationOut = new BufferedOutputStream(new FileOutputStream(destination));

            int numRead;
            byte[] buf = new byte[1024];
            while ((numRead = inputStream.read(buf)) >= 0) {
                destinationOut.write(buf, 0, numRead);
            }

            destinationOut.flush();
            destinationOut.close();
        }
    }

    public static int killVideoProcessor(boolean asRoot, boolean waitFor) throws IOException {
        int killDelayMs = 300;

        int result = -1;

        int procId = -1;

        while ((procId = ShellUtils.findProcessId(mFfmpegBin)) != -1) {

            //	Log.d(TAG, "Found PID=" + procId + " - killing now...");

            String[] cmd = {ShellUtils.SHELL_CMD_KILL + ' ' + procId + ""};

            try {
                result = ShellUtils.doShellCommand(cmd, new ShellUtils.ShellCallback() {

                    @Override
                    public void shellOut(String msg) {

                    }

                    @Override
                    public void processComplete(int exitValue) {


                    }

                }, asRoot, waitFor);
                Thread.sleep(killDelayMs);
            } catch (Exception e) {
            }
        }

        return result;
    }


    public Clip trim(Clip mediaIn, boolean withSound, String outPath, ShellUtils.ShellCallback sc) throws Exception {
        ArrayList<String> cmd = new ArrayList<String>();

        Clip mediaOut = new Clip();

        String mediaPath = mediaIn.path;

        cmd = new ArrayList<String>();

        cmd.add(mFfmpegBin);
        cmd.add("-y");

        if (mediaIn.startTime != null) {
            cmd.add(FFmpegController.Argument.STARTTIME);
            cmd.add(mediaIn.startTime);
        }

        if (mediaIn.duration != -1) {
            cmd.add("-t");
            cmd.add(String.format(Locale.US, "%f", mediaIn.duration));

        }

        cmd.add("-i");
        cmd.add(mediaPath);

        if (!withSound)
            cmd.add("-an");

        cmd.add("-strict");
        cmd.add("-2");//experimental

        mediaOut.path = outPath;

        cmd.add(mediaOut.path);

        execFFMPEG(cmd, sc);

        return mediaOut;
    }
//
//    public void concatAndTrimFilesMP4Stream(ArrayList<Clip> videos, Clip out, boolean preconvertClipsToMP4, boolean useCatCmd, ShellUtils.ShellCallback sc) throws Exception {
//
//
//        File fileExportOut = new File(out.path);
//
//        StringBuffer sbCat = new StringBuffer();
//
//        int tmpIdx = 0;
//
//
//        for (Clip vdesc : videos) {
//
//            Clip mdOut = null;
//
//            if (preconvertClipsToMP4) {
//                File fileOut = new File(mFileTemp, tmpIdx + "-trim.mp4");
//                if (fileOut.exists())
//                    fileOut.delete();
//
//                boolean withSound = false;
//
//                mdOut = trim(vdesc, withSound, fileOut.getCanonicalPath(), sc);
//
//                fileOut = new File(mFileTemp, tmpIdx + ".ts");
//                if (fileOut.exists())
//                    fileOut.delete();
//
//                mdOut = convertToMP4Stream(mdOut, null, -1, fileOut.getCanonicalPath(), sc);
//            } else {
//                File fileOut = new File(mFileTemp, tmpIdx + ".ts");
//                if (fileOut.exists())
//                    fileOut.delete();
//                mdOut = convertToMP4Stream(vdesc, vdesc.startTime, vdesc.duration, fileOut.getCanonicalPath(), sc);
//            }
//
//            if (mdOut != null) {
//                if (sbCat.length() > 0)
//                    sbCat.append("|");
//
//                sbCat.append(new File(mdOut.path).getCanonicalPath());
//                tmpIdx++;
//            }
//        }
//
//        File fileExportOutTs = new File(fileExportOut.getCanonicalPath() + ".ts");
//
//        if (useCatCmd) {
//
//            //cat 0.ts 1.ts > foo.ts
//            StringBuffer cmdBuff = new StringBuffer();
//
//            cmdBuff.append(mCmdCat);
//            cmdBuff.append(" ");
//
//            StringTokenizer st = new StringTokenizer(sbCat.toString(), "|");
//
//            while (st.hasMoreTokens())
//                cmdBuff.append(st.nextToken()).append(" ");
//
//            cmdBuff.append("> ");
//
//            cmdBuff.append(fileExportOut.getCanonicalPath() + ".ts");
//
//            Runtime.getRuntime().exec(cmdBuff.toString());
//
//            ArrayList<String> cmd = new ArrayList<String>();
//
//            cmd = new ArrayList<String>();
//
//            cmd.add(mFfmpegBin);
//            cmd.add("-y");
//            cmd.add("-i");
//
//            cmd.add(fileExportOut.getCanonicalPath() + ".ts");
//
//            cmd.add("-c");
//            cmd.add("copy");
//
//            cmd.add("-an");
//
//            cmd.add(fileExportOut.getCanonicalPath());
//
//            execFFMPEG(cmd, sc, null);
//
//
//        } else {
//
//            //ffmpeg -i "concat:intermediate1.ts|intermediate2.ts" -c copy -bsf:a aac_adtstoasc output.mp4
//            ArrayList<String> cmd = new ArrayList<String>();
//
//            cmd.add(mFfmpegBin);
//            cmd.add("-y");
//            cmd.add("-i");
//            cmd.add("concat:" + sbCat.toString());
//
//            cmd.add("-c");
//            cmd.add("copy");
//
//            cmd.add("-an");
//
//            cmd.add(fileExportOut.getCanonicalPath());
//
//            execFFMPEG(cmd, sc);
//
//        }
//
//        if ((!fileExportOut.exists()) || fileExportOut.length() == 0) {
//            throw new Exception("There was a problem rendering the video: " + fileExportOut.getCanonicalPath());
//        }
//
//
//    }


    private static void execFFMPEG(List<String> cmd, ShellUtils.ShellCallback sc, File fileExec) throws IOException, InterruptedException {

        enablePermissions();

        execProcess(cmd, sc, fileExec);
    }

    private static void enablePermissions() throws IOException {
        Runtime.getRuntime().exec("chmod 700 " + mFfmpegBin);

    }

    private static void execFFMPEG(List<String> cmd, ShellUtils.ShellCallback sc) throws IOException, InterruptedException {
        execFFMPEG(cmd, sc, new File(mFfmpegBin).getParentFile());
    }

    private static int execProcess(List<String> cmds, final ShellUtils.ShellCallback sc, File fileExec) throws IOException, InterruptedException {

        //ensure that the arguments are in the correct Locale format
        for (String cmd : cmds) {
            cmd = String.format(Locale.US, "%s", cmd);
        }

        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.directory(fileExec);

        StringBuffer cmdlog = new StringBuffer();

        for (String cmd : cmds) {
            cmdlog.append(cmd);
            cmdlog.append(' ');
        }

        sc.shellOut(cmdlog.toString());

        //pb.redirectErrorStream(true);

        Process process = pb.start();


        // any error message?
        StreamGobbler errorGobbler = new StreamGobbler(
                process.getErrorStream(), "ERROR", sc);

        // any output?
        StreamGobbler outputGobbler = new
                StreamGobbler(process.getInputStream(), "OUTPUT", sc);

        errorGobbler.start();
        outputGobbler.start();

        final int exitVal = process.waitFor();

        sc.processComplete(exitVal);

        return exitVal;

    }


    private int execProcess(String cmd, ShellUtils.ShellCallback sc, File fileExec) throws IOException, InterruptedException {

        //ensure that the argument is in the correct Locale format
        cmd = String.format(Locale.US, "%s", cmd);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(fileExec);

        //	pb.redirectErrorStream(true);
        Process process = pb.start();


        // any error message?
        StreamGobbler errorGobbler = new
                StreamGobbler(process.getErrorStream(), "ERROR", sc);

        // any output?
        StreamGobbler outputGobbler = new
                StreamGobbler(process.getInputStream(), "OUTPUT", sc);

        // kick them off
        errorGobbler.start();
        outputGobbler.start();


        int exitVal = process.waitFor();

        sc.processComplete(exitVal);

        return exitVal;


    }

    private static class InfoParser implements ShellUtils.ShellCallback {

        private Clip mMedia;
        private int retValue;

        public InfoParser(Clip media) {
            mMedia = media;
        }

        @Override
        public void shellOut(String shellLine) {
            if (shellLine.contains("Duration:")) {

//		  Duration: 00:01:01.75, start: 0.000000, bitrate: 8184 kb/s

                String[] timecode = shellLine.split(",")[0].split(":");


                double duration = 0;

                duration = Double.parseDouble(timecode[1].trim()) * 60 * 60; //hours
                duration += Double.parseDouble(timecode[2].trim()) * 60; //minutes
                duration += Double.parseDouble(timecode[3].trim()); //seconds

                mMedia.duration = duration;


            }

            //   Stream #0:0(eng): Video: h264 (High) (avc1 / 0x31637661), yuv420p, 1920x1080, 16939 kb/s, 30.02 fps, 30 tbr, 90k tbn, 180k tbc
            else if (shellLine.contains(": Video:")) {
                String[] line = shellLine.split(":");
                String[] videoInfo = line[3].split(",");

                mMedia.videoCodec = videoInfo[0];
            }

            //Stream #0:1(eng): Audio: aac (mp4a / 0x6134706D), 48000 Hz, stereo, s16, 121 kb/s
            else if (shellLine.contains(": Audio:")) {
                String[] line = shellLine.split(":");
                String[] audioInfo = line[3].split(",");

                mMedia.audioCodec = audioInfo[0];

            }


            //
            //Stream #0.0(und): Video: h264 (Baseline), yuv420p, 1280x720, 8052 kb/s, 29.97 fps, 90k tbr, 90k tbn, 180k tbc
            //Stream #0.1(und): Audio: mp2, 22050 Hz, 2 channels, s16, 127 kb/s

        }

        @Override
        public void processComplete(int exitValue) {
            retValue = exitValue;

        }
    }

    private static class StreamGobbler extends Thread {
        InputStream is;
        String type;
        ShellUtils.ShellCallback sc;

        StreamGobbler(InputStream is, String type, ShellUtils.ShellCallback sc) {
            this.is = is;
            this.type = type;
            this.sc = sc;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    if (sc != null)
                        sc.shellOut(line);

            } catch (IOException ioe) {
                //   Log.e(TAG,"error reading shell slog",ioe);
                ioe.printStackTrace();
            }
        }
    }

}
