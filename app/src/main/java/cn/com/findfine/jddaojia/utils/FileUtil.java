package cn.com.findfine.jddaojia.utils;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {

    public static String getCacheFilePath() {
        return Environment.getExternalStorageDirectory() + "/jddaojia/";
    }

    public static void copyAssertFile(AssetManager assetManager, String srcFile, String outFilePath) {

        try {
            copyAssertFile(assetManager.open(srcFile), outFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyAssertFile(InputStream in, String outFilePath) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(outFilePath);
            copyFile(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
