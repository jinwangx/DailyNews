package com.jw.dailyNews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jw.dailyNews.Constants;

import Lib.MyNews;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 下午 4:39
 * 作者：Mr.jin
 * 描述：缓存工具类
 */
public class CacheUtils {
	public static String PREF_NAME= Constants.PREF_NAME;
	private static final Context mContext=MyNews.getInstance().getContext();
	private static SharedPreferences sp =
			mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

	/**
	 * 设置缓存
	 * @param key url
	 * @param value json
	 */
	public static void setCache(String key, String value) {
		sp.edit().putString(key, value).commit();
	}
	public static void setCache(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}
	public static void setCache(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}
	public static void setCache(String key, float value) {
		sp.edit().putFloat(key, value).commit();
	}
	public static void setCache(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}


	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public static String getCacheString(String key, String defaultString) {
		return sp.getString(key, defaultString);
	}
	public static int getCacheInt(String key,int defaultInt) {
		return sp.getInt(key, defaultInt);
	}
	public static boolean getCacheBoolean(String key,boolean defaultBoolean) {
		return sp.getBoolean(key, defaultBoolean);
	}
	public static float getCacheFloat(String key) {
		return sp.getFloat(key, 0);
	}
	public static long getCacheLong(String key) {
		return sp.getLong(key, 0);
	}

	/**
	 * 移除对应key的缓存
	 * @param key
	 */
	public static void removeKey(String key){
		sp.edit().remove(key).commit();
	}

	/**
	 * 清空缓存
	 * @return
	 */
	public static void clear(){
		sp.edit().clear().commit();
	}

	/**
	 * 判断缓存中是否存在某key
	 * @param key
	 * @return
	 */
	public static boolean CacheContains(String key){
		return sp.contains(key);
	}
}
