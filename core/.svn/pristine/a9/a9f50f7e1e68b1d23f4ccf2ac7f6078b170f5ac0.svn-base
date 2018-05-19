package com.steptowin.core.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StreamTool {
    // 把输入流的东西读到指定的文件中
    public static void save(InputStream in, String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.close();
        in.close();
    }

    public static void save(InputStream is, String path, OnLoadListener onLoadListener)
            throws IOException {
        if (is != null && path != null) {
            File file = new File(path).getParentFile();
            File download_file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            download_file.createNewFile();
            FileOutputStream out = new FileOutputStream(path);
            int len = -1;
            byte[] bytes = new byte[1024];
            int loadedLength = 0;
            int total = is.available();
            while ((len = is.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                loadedLength += len;
                onLoadListener.onLoading(loadedLength * 100 % total);
            }
            out.close();
            is.close();
        }
    }

    public static InputStream StringTOInputStream(String in) {
        if (in == null)
            in = "";
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        return is;
    }

    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 从输入流读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    public interface OnLoadListener {
        void onLoading(int percent);
    }

    public static InputStream getFileInputStream(String path) {
        try {
            return FileTool.isFileExists(path) ? new FileInputStream(path) : null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getConnInputStream(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
            return urlConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
