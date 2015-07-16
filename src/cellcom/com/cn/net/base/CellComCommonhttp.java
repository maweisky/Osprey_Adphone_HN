package cellcom.com.cn.net.base;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;

import org.afinal.simplecache.ConfigCacheUtil;
import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.widget.Toast;
import cellcom.com.cn.bean.Info;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.CellcomUtil;
import cellcom.com.cn.util.Consts;
import cellcom.com.cn.util.LogMgr;
import cellcom.com.cn.util.SharepreferenceUtil;
import cellcom.com.cn.util.Tracking;

public class CellComCommonhttp implements CellComHttpInterface {
	private Context context;
	private FinalHttp finalHttp;
	private ConfigCacheUtil configCacheUtil;

	public CellComCommonhttp(Context context, ConfigCacheUtil configCacheUtil) {
		// TODO Auto-generated constructor stub
		this.finalHttp = new FinalHttp();
		this.context = context;
		this.configCacheUtil = configCacheUtil;
	}

	@Override
	public void configTimeout(int timeout) {
		// TODO Auto-generated method stub
		finalHttp.configTimeout(timeout);
	}

	@Override
	public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		// TODO Auto-generated method stub
		finalHttp.configSSLSocketFactory(sslSocketFactory);
	}

	@Override
	public void configRequestExecutionRetryCount(int count) {
		// TODO Auto-generated method stub
		finalHttp.configRequestExecutionRetryCount(count);
	}

	@Override
	public void addHeader(String header, String value) {
		// TODO Auto-generated method stub
		finalHttp.addHeader(header, value);
	}

	private void dealResult(String url, final String urlSb, AjaxParams params,
			final NetCallBack<CellComAjaxResult> callBack) {
		finalHttp.get(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onLoading(long count, long current) { // 每1秒钟自动被回调一次
				callBack.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String result = (t == null ? "" : t).toString().trim();
				LogMgr.showLog("result==>" + result);
				configCacheUtil.setUrlCache(result, urlSb);
				CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(
						result);
				if (cellComAjaxResult != null) {
					callBack.onSuccess(cellComAjaxResult);
				}
			}

			@Override
			public void onStart() {
				// 开始http请求的时候回调
				callBack.onStart();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				callBack.onFailure(t, strMsg);
				Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void dealResultWithHead(String url, final String urlSb,
			Header[] headers, AjaxParams params,
			final NetCallBack<CellComAjaxResult> callBack) {
		finalHttp.get(url, headers, params, new AjaxCallBack<Object>() {
			@Override
			public void onLoading(long count, long current) { // 每1秒钟自动被回调一次
				callBack.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String result = (t == null ? "" : t).toString().trim();
				LogMgr.showLog("result==>" + result);
				configCacheUtil.setUrlCache(result, urlSb);
				CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(
						result);
				if (cellComAjaxResult != null) {
					callBack.onSuccess(cellComAjaxResult);
				}
			}

			@Override
			public void onStart() {
				// 开始http请求的时候回调
				callBack.onStart();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				callBack.onFailure(t, strMsg);
				Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void get(String url, final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		AjaxParams params = getPublicParams(context, url, null);
		String result = configCacheUtil.getUrlCache(urlSb.toString());
		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealResult(url, urlSb.toString(), params, callBack);
		}
	}

	@Override
	public void get(String url, AjaxParams params,
			final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		params = getPublicParams(context, url, params);
		String result = configCacheUtil.getUrlCache(urlSb.toString());

		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealResult(url, urlSb.toString(), params, callBack);
		}
	}

	@Override
	public void get(String url, Header[] headers, AjaxParams params,
			final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		urlSb.append(params.toString());
		params = getPublicParams(context, url, params);
		String result = configCacheUtil.getUrlCache(urlSb.toString());
		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealResultWithHead(url, urlSb.toString(), headers, params, callBack);
		}
	}

	@Override
	public Object getSync(String url) {
		// TODO Auto-generated method stub
		return finalHttp.getSync(url);
	}

	@Override
	public Object getSync(String url, AjaxParams params) {
		// TODO Auto-generated method stub
		return finalHttp.getSync(url, params);
	}

	@Override
	public Object getSync(String url, Header[] headers, AjaxParams params) {
		// TODO Auto-generated method stub
		return finalHttp.getSync(url, headers, params);
	}

	private void dealPostResult(String url, final String cacheKey,
			final NetCallBack<CellComAjaxResult> callBack, AjaxParams params) {
		finalHttp.post(url, params, new AjaxCallBack<Object>() {
			@Override
			public void onLoading(long count, long current) { // 每1秒钟自动被回调一次
				callBack.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String result = (t == null ? "" : t).toString().trim();
				LogMgr.showLog("result==>" + result);
				configCacheUtil.setUrlCache(result, cacheKey);
				CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(
						result);
				if (cellComAjaxResult != null) {
					callBack.onSuccess(cellComAjaxResult);
				}
			}

			@Override
			public void onStart() {
				// 开始http请求的时候回调
				callBack.onStart();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				callBack.onFailure(t, strMsg);
				Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void post(String url, final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		AjaxParams params = getPublicParams(context, url, null);
		String result = configCacheUtil.getUrlCache(urlSb.toString());
		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealPostResult(url, urlSb.toString(), callBack, params);
		}
	}

	@Override
	public void post(String url, AjaxParams params,
			final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		urlSb.append(params.toString());
		params = getPublicParams(context, url, params);
		String result = configCacheUtil.getUrlCache(urlSb.toString());
		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealPostResult(url, urlSb.toString(), callBack, params);
		}
	}

	@Override
	public void post(String url, Header[] headers, AjaxParams params,
			String contentType, final NetCallBack<CellComAjaxResult> callBack) {
		// TODO Auto-generated method stub
		StringBuffer urlSb = new StringBuffer(url);
		urlSb.append(params.toString());
		params = getPublicParams(context, url, params);
		String result = configCacheUtil.getUrlCache(urlSb.toString());
		if (result != null) {
			CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(result);
			if (cellComAjaxResult != null) {
				callBack.onSuccess(cellComAjaxResult);
			}
		} else {
			dealPostResultWithHead(url, urlSb.toString(), headers, params,
					contentType, callBack);
		}
	}

	private void dealPostResultWithHead(String url, final String urlSb,
			Header[] headers, AjaxParams params, String contentType,
			final NetCallBack<CellComAjaxResult> callBack) {
		finalHttp.post(url, headers, params, contentType,
				new AjaxCallBack<Object>() {
					@Override
					public void onLoading(long count, long current) { // 每1秒钟自动被回调一次
						callBack.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String result = (t == null ? "" : t).toString().trim();
						LogMgr.showLog("result==>" + result);
						configCacheUtil.setUrlCache(result, urlSb);
						CellComAjaxResult cellComAjaxResult = new CellComAjaxResult(
								result);
						if (cellComAjaxResult != null) {
							callBack.onSuccess(cellComAjaxResult);
						}
					}

					@Override
					public void onStart() {
						// 开始http请求的时候回调
						callBack.onStart();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						callBack.onFailure(t, strMsg);
						Toast.makeText(context, strMsg, Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	@Override
	public Object postSync(String url) {
		// TODO Auto-generated method stub
		AjaxParams params = getPublicParams(context, url, null);
		return finalHttp.postSync(url, params);
	}

	@Override
	public Object postSync(String url, AjaxParams params) {
		// TODO Auto-generated method stub
		params = getPublicParams(context, url, params);
		return finalHttp.postSync(url, params);
	}

	@Override
	public Object postSync(String url, Header[] headers, AjaxParams params,
			String contentType) {
		// TODO Auto-generated method stub
		params = getPublicParams(context, url, params);
		return finalHttp.postSync(url, headers, params, contentType);
	}

	@Override
	public HttpHandler<File> download(String url, String target,
			final NetCallBack<File> callback) {
		// TODO Auto-generated method stub
		AjaxParams params = getDownloadPublicParams(context, url, null);
		return finalHttp.download(url, params, target,
				new AjaxCallBack<File>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						callback.onFailure(t, strMsg);
						LogMgr.showLog("onFailure==>" + strMsg);
						Toast.makeText(context, strMsg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						callback.onStart();
					}

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Toast.makeText(
								context,
								t == null ? "null" : t.getAbsoluteFile()
										.toString(), Toast.LENGTH_SHORT).show();
						callback.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						callback.onLoading(count, current);
						super.onLoading(count, current);
					}
				});
	}

	@Override
	public HttpHandler<File> download(String url, String target,
			boolean isResume, final NetCallBack<File> callback) {
		// TODO Auto-generated method stub
		AjaxParams params = getDownloadPublicParams(context, url, null);
		return finalHttp.download(url, params, target, isResume,
				new AjaxCallBack<File>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						callback.onFailure(t, strMsg);
						LogMgr.showLog("onFailure==>" + strMsg);
						Toast.makeText(context, strMsg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						callback.onStart();
					}

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Toast.makeText(
								context,
								t == null ? "null" : t.getAbsoluteFile()
										.toString(), Toast.LENGTH_SHORT).show();
						callback.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						callback.onLoading(count, current);
						super.onLoading(count, current);
					}
				});
	}

	@Override
	public HttpHandler<File> download(String url, AjaxParams params,
			String target, final NetCallBack<File> callback) {
		// TODO Auto-generated method stub
		params = getDownloadPublicParams(context, url, params);
		return finalHttp.download(url, params, target,
				new AjaxCallBack<File>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						callback.onFailure(t, strMsg);
						LogMgr.showLog("onFailure==>" + strMsg);
						Toast.makeText(context, strMsg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						callback.onStart();
					}

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Toast.makeText(
								context,
								t == null ? "null" : t.getAbsoluteFile()
										.toString(), Toast.LENGTH_SHORT).show();
						callback.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						callback.onLoading(count, current);
						super.onLoading(count, current);
					}
				});
	}

	@Override
	public HttpHandler<File> download(String url, AjaxParams params,
			String target, boolean isResume, final NetCallBack<File> callback) {
		// TODO Auto-generated method stub
		params = getDownloadPublicParams(context, url, params);
		return finalHttp.download(url, params, target, isResume,
				new AjaxCallBack<File>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						callback.onFailure(t, strMsg);
						LogMgr.showLog("onFailure==>" + strMsg);
						Toast.makeText(context, strMsg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						callback.onStart();
					}

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Toast.makeText(
								context,
								t == null ? "null" : t.getAbsoluteFile()
										.toString(), Toast.LENGTH_SHORT).show();
						callback.onSuccess(t);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						callback.onLoading(count, current);
						super.onLoading(count, current);
					}
				});
	}

	private AjaxParams getDownloadPublicParams(Context context, String url,
			AjaxParams params) {
		/*
		 * Info info = Tracking.getEventInfo(context); // String signmsg = //
		 * CellcomUtil.encodeMD5("SDFSWLPUG57JE4Z1G3WGNWU"+info.getTimestamp());
		 * params = (params == null ? new AjaxParams() : params);
		 * params.put("timestamp", info.getTimestamp()); params.put("os",
		 * info.getOs());// 手机操作系统 params.put("service", info.getService());//
		 * 服务 // params.put("icpname", "");// 接口名字 params.put("version",
		 * info.getApp_version());// 客户端版本号 params.put("imsi",
		 * info.getImsi());// imsi params.put("deviceid", info.deviceid);// imei
		 * LogMgr.showLog("http.url = " + (url + "?" + params.toString()));
		 * return params;
		 */
		Info info = Tracking.getEventInfo(context);

		params = params == null ? new AjaxParams() : params;
		String token = cellcom.com.cn.util.SharepreferenceUtil.getDate(context,
				"token");
		params.put("token", token);// token
		params.put("timestamp", info.getTimestamp() + "");
		params.put("os", info.getOs());
		params.put("service", info.getService());

		params.put("version", info.getApp_version());
		params.put("imsi", info.getImsi());
		params.put("deviceid", info.deviceid);
		LogMgr.showLog("http.url = " + url + "?" + params.toString());
		return params;
	}

	private AjaxParams getPublicParams(Context context, String url,
			AjaxParams params) {
		Info info = Tracking.getEventInfo(context);
		String interfacename = url.substring(url.lastIndexOf("/") + 1,
				url.lastIndexOf(".flow"));// 接口名称
		String sysParamFlow = Tracking.getSysParamFlow(context);
		String authstring = "";
		if (sysParamFlow.equalsIgnoreCase(interfacename)) {
			authstring = CellcomUtil.encodeMD5(info.getService()
					+ info.getTimestamp() + "cellcom");
		} else {
			String key = SharepreferenceUtil.getDate(context, Consts.KEY);
			authstring = CellcomUtil.encodeMD5(info.getService()
					+ info.getTimestamp() + key);
		}
		params = (params == null ? new AjaxParams() : params);
		String token = cellcom.com.cn.util.SharepreferenceUtil.getDate(context,
				"token");
		params.put("token", token);// token
		params.put("os", info.getOs());// 手机操作系统
//		params.put("devicename", info.getDevicename());// 手机操作系统
		params.put("service", info.getService());// 服务
		params.put("icpname", interfacename);// 接口名字
		params.put("version", info.getApp_version());// 客户端版本号
		params.put("timestamp", info.getTimestamp() + "");// 时间戳
		params.put("imsi", info.getImsi());// imsi
		params.put("authstring", authstring);// authstring
		params.put("deviceid", info.deviceid);// imei
		LogMgr.showLog("http.url = " + (url + "?" + params.toString()));
		return params;
	}

}
