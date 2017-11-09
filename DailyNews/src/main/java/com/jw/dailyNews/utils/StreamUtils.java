package com.jw.dailyNews.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import Lib.MyNews;

/**
 * Author: Administrator
 * Created on:  2017/8/20.
 * Description:
 */

public class StreamUtils {
    public static InputStream getAssetsInputStream(String name){
        InputStream open=null;
        try {
            open = MyNews.getInstance().getContext().getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return open;
    }

    public static String readfromStream(InputStream in)
    {
        String result=null;
        //字节输出流
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        try {
            while((len=in.read(buffer))!=-1)
            {
                out.write(buffer,0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            result=out.toString();
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void releaseAssets(Context context, String name) {
        BufferedInputStream inputStream = null;
        BufferedOutputStream out = null;
        try {
            //得到绝对路径
            String path = context.getFilesDir().getAbsolutePath()+"/"+name;
            File file=new File(path);
            if(file.exists())
                return;
            else
                file.createNewFile();
            inputStream = new BufferedInputStream(context.getAssets().open(name));
            out = new BufferedOutputStream(new FileOutputStream(file));
            int b = 0;
            while ((b = inputStream.read()) != -1) {
                out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
