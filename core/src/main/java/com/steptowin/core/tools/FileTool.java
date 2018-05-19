package com.steptowin.core.tools;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class FileTool {



    public static boolean isFileExists(String path){
        if(!TextUtils.isEmpty(path) && new File(path).exists())
            return true;
        return false;
    }

    /**
     * 获取文件大小，以MB或者KB为单位
     */
    public static String getFileLength(File file) {
        long size = file.length();
        DecimalFormat df = new DecimalFormat("#.0");
        if (size > 1024 * 1024) { // 是否大于1M
            return df.format((double) size / (double) (1024 * 1024)) + "M";
        } else {
            return df.format((double) size / (double) 1024) + "K";
        }
    }


    //-----------------------------------------------------------------------

    /**
     * Returns the size of the specified file or directory. If the provided
     * {@link File} is a regular file, then the file's length is returned.
     * If the argument is a directory, then the size of the directory is
     * calculated recursively. If a directory or subdirectory is security
     * restricted, its size will not be included.
     *
     * @param file the regular file or directory to return the size
     *             of (must not be {@code null}).
     * @return the length of the file, or recursive size of the directory,
     * provided (in bytes).
     * @throws NullPointerException     if the file is {@code null}
     * @throws IllegalArgumentException if the file does not exist.
     * @since 2.0
     */
    public static long sizeOf(File file) {

        if (!file.exists()) {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }

    }

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     *
     * @param directory directory to inspect, must not be {@code null}
     * @return size of directory in bytes, 0 if directory is security restricted, a negative number when the real total
     * is greater than {@link Long#MAX_VALUE}.
     * @throws NullPointerException if the directory is {@code null}
     */
    public static long sizeOfDirectory(File directory) {
        checkDirectory(directory);

        final File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            return 0L;
        }
        long size = 0;

        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    size += sizeOf(file);
                    if (size < 0) {
                        break;
                    }
                }
            } catch (IOException ioe) {
                // Ignore exceptions caught when asking if a File is a symlink.
            }
        }

        return size;
    }

    /**
     * Determines whether the specified file is a Symbolic Link rather than an actual file.
     * <p/>
     * Will not return true if there is a Symbolic Link anywhere in the path,
     * only if the specific file is.
     * <p/>
     * <b>Note:</b> the current implementation always returns {@code false} if the system
     * is detected as Windows using {@link FilenameTool#isSystemWindows()}
     *
     * @param file the file to check
     * @return true if the file is a Symbolic Link
     * @throws IOException if an IO error occurs while checking the file
     * @since 2.0
     */
    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (FilenameTool.isSystemWindows()) {
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks that the given {@code File} exists and is a directory.
     *
     * @param directory The {@code File} to check.
     * @throws IllegalArgumentException if the given {@code File} does not exist or is not a directory.
     */
    private static void checkDirectory(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    /**
     * 根据路径删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {

        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        return file.delete();

    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                if (null != files)
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    File files[] = file.listFiles();
                    if (null != files && files.length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */
    static public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部空间大小
     *
     * @return
     */
    static public long getTotalInternalMemorySize() {
        File file = Environment.getDataDirectory();// Gets the Android data
        // directory
        StatFs stat = new StatFs(file.getPath());
        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getBlockCount(); // block总数
        return totalBlocks * blockSize;
    }



    /**
     * 复制文件
     *
     * @param oldPath 原始文件路径
     * @param newPath 文件存放的新路径
     */
    public static String copyFile(String oldPath, String newPath) {
        String message = "";
        try {
            String name = oldPath.substring(oldPath.lastIndexOf("/") + 1,
                    oldPath.length());
            newPath = newPath + name;
            File newFile = new File(newPath);
            if (newFile.exists()) {
                newFile.delete();
            }
            InputStream is = new FileInputStream(oldPath);
            OutputStream os = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            message = "保存成功";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            message = "文件路径不存在";
        } catch (IOException e) {
            e.printStackTrace();
            message = "写入出错";
        }
        return message;
    }

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is
     * not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist)
     * then an IOException is thrown.
     *
     * @param directory directory to create, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     * @throws IOException  if the directory cannot be created or the file already exists but is not a directory
     */
    public static void forceMkdir(File directory) throws IOException {
        if(null == directory)
            return;
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message =
                        "File "
                                + directory
                                + " exists and is "
                                + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    String message =
                            "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }

    public static boolean mkdirs(File directory) {
        try {
            forceMkdir(directory);
            return true;
        } catch (IOException e){
        }
        return false;
    }



}
