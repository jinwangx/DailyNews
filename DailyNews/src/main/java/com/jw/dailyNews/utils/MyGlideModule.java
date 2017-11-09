package com.jw.dailyNews.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.GlideModule;
import com.jw.dailyNews.BaseApplication;

import java.io.File;
import java.io.InputStream;

public class MyGlideModule implements GlideModule {

    private static String downloadDirectoryPath;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        File storageDirectory = Environment.getExternalStorageDirectory();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            downloadDirectoryPath = storageDirectory+"/GlideCache/"+context.getPackageName();
        else
            downloadDirectoryPath=context.getCacheDir()+"/imageCache";
        File file = new File(downloadDirectoryPath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists())
            parentFile.mkdirs();
        //设置缓存的大小为100M
        int cacheSize = 100*1000*1000;
        builder.setDiskCache( new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(BaseApplication.getOkHttpClient()));
    }

    public static String  getCacheDirectory(){
        return downloadDirectoryPath;
    }

}