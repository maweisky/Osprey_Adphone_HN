package org.afinal.simplecache;

import android.content.Context;

/**
 * 缓存工具类
 * @author naibo-liao
 * @时间： 2013-1-4下午02:30:52
 */
public class ConfigCacheUtil{
	
	private static ConfigCacheUtil instance;
	private Context context;
    public static ConfigCacheUtil getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = new ConfigCacheUtil();
                }
            }
        }
        return instance;
    }
	public void init(Context context) {
		// TODO Auto-generated method stub
		mCache = ACache.get(context);
		this.context=context;
	}
	private static ACache mCache;
    
    /**
     * CONFIG_CACHE_MODEL_LONG : 长时间(7天)缓存模式 <br>
     * CONFIG_CACHE_MODEL_ML : 中长时间(12小时)缓存模式<br>
     * CONFIG_CACHE_MODEL_MEDIUM: 中等时间(2小时)缓存模式 <br>
     * CONFIG_CACHE_MODEL_SHORT : 短时间(5分钟)缓存模式
     */
    public enum ConfigCacheModel {
        CONFIG_CACHE_MODEL_SHORT, CONFIG_CACHE_MODEL_MEDIUM, CONFIG_CACHE_MODEL_ML, CONFIG_CACHE_MODEL_LONG;
    }

    /**
     * 获取缓存
     * @param url 访问网络的URL
     * @return 缓存数据
     */
    public String getUrlCache(String url) {
    	String testString=null;
        if(url == null) {
            return null;
        }
        if(!NetUtils.isConnected(context)){
        	testString = mCache.getAsString(url);
        }
        return testString;
    }

    /**
     * 设置缓存
     * @param data
     * @param url
     */
    public void setUrlCache(String data, String url) {
    	if(url == null) {
            return;
        }
    	mCache.put(url, data);
    }

    /**
     * 删除历史缓存文件
     * @param cacheFile
     */
    public static void clearCache(String url) {
    	mCache.remove(url);
    }
}