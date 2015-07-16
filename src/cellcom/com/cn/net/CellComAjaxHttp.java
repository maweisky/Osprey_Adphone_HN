package cellcom.com.cn.net;

import java.io.File;

import net.tsz.afinal.http.HttpHandler;

import org.afinal.simplecache.ConfigCacheUtil;
import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import cellcom.com.cn.net.base.CellComCommonhttp;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.net.base.CellComRsahttp;


public class CellComAjaxHttp {
	private HttpTypeMode mode;
	public enum HttpTypeMode {  
	    COMMONHTTP, RSAHTTP  
	} 
	public enum HttpWayMode {  
	    GET, POST  
	}
	private CellComHttpInterface cellComHttpInterface;
	private ConfigCacheUtil configCacheUtil;
	public CellComAjaxHttp() {
		// TODO Auto-generated constructor stub
		configCacheUtil=ConfigCacheUtil.getInstance();
	}
	/**
	 * 
	 * @param mode 网络访问的类型 CellComAjaxHttp.HttpTypeMode.COMMONHTTP ，CellComAjaxHttp.HttpTypeMode.RSAHTTP
	 * @param context 上下文对象
	 */
	public void init(HttpTypeMode mode,Context context) {
		// TODO Auto-generated method stub
		this.mode=mode;
		configCacheUtil.init(context);
		if(mode==HttpTypeMode.COMMONHTTP){
			cellComHttpInterface=new CellComCommonhttp(context,configCacheUtil);
		}else if(mode==HttpTypeMode.RSAHTTP){
			cellComHttpInterface=new CellComRsahttp(context,configCacheUtil);
		}
	}
	/**
	 * 采用rsa访问网络时使用
	 * @param url 服务地址
	 * @param callBack 回调函数
	 * @throws Exception 
	 */
	public void get3Des(String url,final NetCallBack<String> callBack) throws Exception{
		((CellComRsahttp)cellComHttpInterface).get3Des(url, callBack);
	}
	/**
     * 设置网络连接超时时间，默认为10秒钟
     * @param timeout
     */
	public void configTimeout(int timeout){
		cellComHttpInterface.configTimeout(timeout);
	}
	
	/**
     * 设置https请求时  的 SSLSocketFactory
     * @param sslSocketFactory
     */
	public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory){
		cellComHttpInterface.configSSLSocketFactory(sslSocketFactory);
	}
	
	/**
     * 配置错误重试次数
     * @param retry
     */
	public void configRequestExecutionRetryCount(int count){
		cellComHttpInterface.configRequestExecutionRetryCount(count);
	}
	
	/**
    * 添加http请求头
    * @param header
    * @param value
	*/
	public void addHeader(String header, String value){
		cellComHttpInterface.addHeader(header, value);
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param httpWayMode 访问方式 CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 * @param netCallBack 回调函数
	 */
	public void send(String url,HttpWayMode httpWayMode,CellComHttpInterface.NetCallBack<CellComAjaxResult> netCallBack){
		if(httpWayMode==HttpWayMode.GET){			
			if(mode==HttpTypeMode.RSAHTTP){
				cellComHttpInterface.post(url, netCallBack);
			}else{
				cellComHttpInterface.get(url, netCallBack);
			}
		}else if(httpWayMode==HttpWayMode.POST){
			cellComHttpInterface.post(url, netCallBack);
		}
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param cellComAjaxParams 提交参数
	 * @param httpWayMode 访问方式  CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 * @param netCallBack 回调函数
	 */
	public void send(String url,CellComAjaxParams cellComAjaxParams,HttpWayMode httpWayMode,CellComHttpInterface.NetCallBack<CellComAjaxResult> netCallBack){
		if(httpWayMode==HttpWayMode.GET){	
			if(mode==HttpTypeMode.RSAHTTP){
				cellComHttpInterface.post(url, cellComAjaxParams.getAjaxParams(), netCallBack);
			}else{
				cellComHttpInterface.get(url, cellComAjaxParams.getAjaxParams(), netCallBack);
			}
		}else if(httpWayMode==HttpWayMode.POST){
			cellComHttpInterface.post(url, cellComAjaxParams.getAjaxParams(), netCallBack);
		}
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param headers 头参数
	 * @param cellComAjaxParams 提交参数
	 * @param contentType 内容类型
	 * @param httpWayMode 访问方式 CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 * @param netCallBack 回调函数
	 */
	public void send(String url,Header[] headers,CellComAjaxParams cellComAjaxParams,String contentType,HttpWayMode httpWayMode,CellComHttpInterface.NetCallBack<CellComAjaxResult> netCallBack){
		if(httpWayMode==HttpWayMode.GET){		
			if(mode==HttpTypeMode.RSAHTTP){
				cellComHttpInterface.post(url, headers, cellComAjaxParams.getAjaxParams(), contentType, netCallBack);
			}else{
				cellComHttpInterface.get(url, headers, cellComAjaxParams.getAjaxParams(), netCallBack);
			}
		}else if(httpWayMode==HttpWayMode.POST){
			cellComHttpInterface.post(url, headers, cellComAjaxParams.getAjaxParams(), contentType, netCallBack);
		}
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param httpWayMode 访问方式 CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 */
	public Object sendSync(String url,HttpWayMode httpWayMode){
		if(httpWayMode==HttpWayMode.GET){		
			if(mode==HttpTypeMode.RSAHTTP){
				return cellComHttpInterface.postSync(url);
			}else{
				return cellComHttpInterface.getSync(url);
			}
		}else if(httpWayMode==HttpWayMode.POST){
			return cellComHttpInterface.postSync(url);
		}
		return null;
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param cellComAjaxParams 提交参数
	 * @param httpWayMode 访问方式 CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 */
	public Object sendSync(String url,CellComAjaxParams cellComAjaxParams,HttpWayMode httpWayMode){
		if(httpWayMode==HttpWayMode.GET){
			if(mode==HttpTypeMode.RSAHTTP){
				return cellComHttpInterface.postSync(url,cellComAjaxParams.getAjaxParams());
			}else{
				return cellComHttpInterface.getSync(url, cellComAjaxParams.getAjaxParams());
			}
		}else if(httpWayMode==HttpWayMode.POST){
			return cellComHttpInterface.postSync(url,cellComAjaxParams.getAjaxParams());
		}
		return null;
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param headers 头参数
	 * @param cellComAjaxParams 提交参数
	 * @param contentType 内容类型
	 * @param httpWayMode 访问方式 CellComAjaxHttp.HttpWayMode.POST,CellComAjaxHttp.HttpWayMode.GET
	 */
	public Object sendSync(String url,Header[] headers,CellComAjaxParams cellComAjaxParams,String contentType,HttpWayMode httpWayMode){
		if(httpWayMode==HttpWayMode.GET){	
			if(mode==HttpTypeMode.RSAHTTP){
				return cellComHttpInterface.postSync(url, headers, cellComAjaxParams.getAjaxParams(), contentType);
			}else{
				return cellComHttpInterface.getSync(url, headers, cellComAjaxParams.getAjaxParams());
			}
		}else if(httpWayMode==HttpWayMode.POST){
			return cellComHttpInterface.postSync(url, headers, cellComAjaxParams.getAjaxParams(), contentType);
		}
		return  null;
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param target 目录地址
	 * @param netCallBack 回调函数
	 * @return HttpHandler<File>.stop() 停止下载
	 */
	public HttpHandler<File> downLoad(String url,String target,CellComHttpInterface.NetCallBack<File> netCallBack){
		return cellComHttpInterface.download(url, target, netCallBack);
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param cellComAjaxParams 提交参数
	 * @param target 目录地址
	 * @param netCallBack 回调函数
	 * @return HttpHandler<File>.stop() 停止下载
	 */
	public HttpHandler<File> downLoad(String url,CellComAjaxParams cellComAjaxParams,String target,CellComHttpInterface.NetCallBack<File> netCallBack){
		return cellComHttpInterface.download(url, cellComAjaxParams.getAjaxParams(), target, netCallBack);
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param target 目录地址
	 * @param isResume true:断点续传 false:不断点续传（全新下载）
	 * @param netCallBack 回调函数
	 * @return HttpHandler<File>.stop() 停止下载
	 */
	public HttpHandler<File> downLoad(String url,String target,Boolean isResume,CellComHttpInterface.NetCallBack<File> netCallBack){
		return cellComHttpInterface.download(url, target, isResume, netCallBack);
	}
	
	/**
	 * 
	 * @param url 服务地址
	 * @param cellComAjaxParams 提交参数
	 * @param target  目录地址
	 * @param isResume true:断点续传 false:不断点续传（全新下载）
	 * @param netCallBack 回调函数
	 * @return HttpHandler<File>.stop() 停止下载
	 */
	public HttpHandler<File> downLoad(String url,CellComAjaxParams cellComAjaxParams,String target,Boolean isResume,CellComHttpInterface.NetCallBack<File> netCallBack){
		return cellComHttpInterface.download(url, cellComAjaxParams.getAjaxParams(), target, isResume, netCallBack);
	}
}
