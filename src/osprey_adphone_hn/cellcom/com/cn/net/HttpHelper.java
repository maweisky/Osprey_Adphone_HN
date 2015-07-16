package osprey_adphone_hn.cellcom.com.cn.net;

import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.telephony.TelephonyManager;
import cellcom.com.cn.net.CellComAjaxHttp;

public class HttpHelper {
	private static HttpHelper httpHelper = null;
	private static CellComAjaxHttp cellComAjaxHttp = null;

	private HttpHelper(Context context) {
		cellComAjaxHttp = new CellComAjaxHttp();
		cellComAjaxHttp.init(CellComAjaxHttp.HttpTypeMode.RSAHTTP, context);
		cellComAjaxHttp.configTimeout(30000);
		cellComAjaxHttp.configRequestExecutionRetryCount(10);
		configSSLSocketFactory(context);
	}

	public static CellComAjaxHttp getInstances(Context con) {
		if (httpHelper == null) {
			httpHelper = new HttpHelper(con);
		}
		return cellComAjaxHttp;
	}

	private static void configSSLSocketFactory(Context context) {
		SSLSocketFactory socketFactory = null;
		try {
			KeyStore trusKeyStore = KeyStore.getInstance("BKS");
			trusKeyStore.load(
					context.getResources().openRawResource(R.raw.server_trust),
					"cellcom@_@!".toCharArray());
			socketFactory = new SSLSocketFactory(trusKeyStore);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (socketFactory != null) {
			cellComAjaxHttp.configSSLSocketFactory(socketFactory);
		}
	}

	public static String getAndroidId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId() == null ? "" : tm.getDeviceId();
	}
}
