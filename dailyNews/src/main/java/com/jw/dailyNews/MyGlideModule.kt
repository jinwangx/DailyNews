package com.jw.dailyNews

import android.content.Context
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.InputStream

@GlideModule
class MyGlideModule : AppGlideModule() {


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
        //设置磁盘缓存目录（和创建的缓存目录相同）
        val storageDirectory = Environment.getExternalStorageDirectory()
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            cacheDirectory = storageDirectory.toString() + "/GlideCache/" + context.packageName
        else
            cacheDirectory = context.cacheDir.toString() + "/imageCache"
        val file = File(cacheDirectory!!)
        val parentFile = file.parentFile
        if (!parentFile.exists())
            parentFile.mkdirs()
        //设置缓存的大小为100M
        val cacheSize = 200 * 1000 * 1000L
        builder.setDiskCache(DiskLruCacheFactory(cacheDirectory, "dailyNews",cacheSize))

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(BaseApplication.okHttpClient))
    }

    companion object {

        var cacheDirectory: String? = null
            private set
    }

}