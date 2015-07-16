package cellcom.com.cn.net.base;

import java.io.File;

import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;

import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;

import cellcom.com.cn.net.CellComAjaxResult;

public interface CellComHttpInterface {
	public abstract class NetCallBack<T>{
		public abstract void onSuccess(T t);
		public void onLoading(long count, long current){};
		public void onStart(){};
		public void onFailure(Throwable t, String strMsg){};
	}
	/**
     * 设置网络连接超时时间，默认为10秒钟
     * @param timeout
     */
	public void configTimeout(int timeout);
	
	/**
     * 设置https请求时  的 SSLSocketFactory
     * @param sslSocketFactory
     */
	public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory);
	
	/**
     * 配置错误重试次数
     * @param retry
     */
	public void configRequestExecutionRetryCount(int count);
	
	/**
    * 添加http请求头
    * @param header
    * @param value
	*/
	public void addHeader(String header, String value);
	
	
	
	//------------------get 请求-----------------------
	public void get( String url,NetCallBack<CellComAjaxResult> callBack);
	
	public void get( String url, AjaxParams params, NetCallBack<CellComAjaxResult> callBack);
	
	public void get( String url, Header[] headers, AjaxParams params, NetCallBack<CellComAjaxResult> callBack);
	
	public Object getSync( String url);
	
	public Object getSync( String url, AjaxParams params);
	
	public Object getSync( String url, Header[] headers, AjaxParams params);
	
	//------------------post 请求-----------------------
	public void post(String url, NetCallBack<CellComAjaxResult> callBack);
	
	public void post(String url, AjaxParams params, NetCallBack<CellComAjaxResult> callBack);
	
	public void post( String url, Header[] headers, AjaxParams params, String contentType,NetCallBack<CellComAjaxResult> callBack);
	
	public Object postSync(String url);
	
	public Object postSync(String url, AjaxParams params);
	
	public Object postSync( String url, Header[] headers, AjaxParams params, String contentType);
	
	//---------------------下载---------------------------------------
    public HttpHandler<File> download(String url,String target,NetCallBack<File> callback);

    public HttpHandler<File> download(String url,String target,boolean isResume,NetCallBack<File> callback);
    
    public HttpHandler<File> download( String url,AjaxParams params, String target, NetCallBack<File> callback) ;
    
    public HttpHandler<File> download( String url,AjaxParams params, String target,boolean isResume,NetCallBack<File> callback) ;

}
