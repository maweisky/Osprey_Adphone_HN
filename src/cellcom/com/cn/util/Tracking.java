package cellcom.com.cn.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import cellcom.com.cn.bean.Info;

public class Tracking {
	private static final String LogTag = Tracking.class.getCanonicalName();

	public static Info getEventInfo(Context context) {
		Info eventInfo = new Info();
		eventInfo.deviceid = getAndroidId(context);
		eventInfo.imsi = getImsi(context);
		eventInfo.timestamp = getTimeStamp();
		eventInfo.version = getAppVersioin(context)[0];
		eventInfo.os = "android";
		eventInfo.service=getMetaDataBundle(context).getString("service");
		eventInfo.os_version = Build.VERSION.RELEASE;
		eventInfo.devicename = Build.MODEL;
		String[] screenDesc = getScreenDesc(context);
		eventInfo.screen_width = screenDesc[0];
		eventInfo.screen_height = screenDesc[1];
		eventInfo.carrier = getCarrier(context);
		eventInfo.network_connection = getNetworkConnection(context);
		return eventInfo;
	}
	
	public static List<CharSequence> getFlowSet(Context context){
		CharSequence[] items=context.getResources().getStringArray(getMetaDataBundle(context).getInt("flow"));
		Arrays.asList(items);
		return Arrays.asList(items);
	}
	
	public static String getSysParamFlow(Context context){
		return getMetaDataBundle(context).getString("sysparamflow");
	}
	
	public static String getNetworkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if ((networkInfo != null) && (networkInfo.isConnectedOrConnecting())) {
			if (networkInfo.getType() == 0) {
				return networkInfo.getSubtypeName();
			}
			return networkInfo.getTypeName();
		}
		return "Disconnected";
	}

	public static String getCarrier(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService("phone");
		String carrier = "";
		String networkOperator = telephonyManager.getNetworkOperator();
		if ((networkOperator.equals("46000"))
				|| (networkOperator.equals("46002")))
			carrier = "中国移动";
		else if (networkOperator.equals("46001"))
			carrier = "中国联通";
		else if (networkOperator.equals("46003"))
			carrier = "中国电信";
		else {
			carrier = telephonyManager.getNetworkOperatorName();
		}
		return carrier;
	}

	public static String[] getScreenDesc(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		String[] desc = new String[2];
		desc[0] = Integer.toString(displayMetrics.widthPixels);
		desc[1] = Integer.toString(displayMetrics.heightPixels);
		return desc;
	}

//	public static String getAppVersioin(Context context) {
//		try {
//			PackageManager packageManager = context.getPackageManager();
//			PackageInfo packageInfo = packageManager.getPackageInfo(
//					context.getPackageName(), 0);
//			return packageInfo.versionName;
//		} catch (PackageManager.NameNotFoundException e) {
//			Log.e(LogTag, "getAppVersioin.NameNotFoundException", e);
//		}
//		return "unknown_app_version";
//	}
	public static String[] getAppVersioin(Context context) {
		String []codename = null;
		String versionCode="";	//�汾��
		String versionName="";	//�汾���
		String prodate="20110101";		//�汾����ʱ��
		try{
			//---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode=pi.versionCode+"";
			versionName=pi.versionName;
			
//			if(versionName!=null && versionName.indexOf("-")>=0){
//				//versionCode=versionCode+"."+versionName.substring(0, versionName.indexOf("-"));
//				versionCode=versionName.substring(0, versionName.indexOf("-"));
//				prodate=versionName.substring( versionName.indexOf("-")+1,versionName.length());
//			}
			codename=new String[]{versionName,versionCode};
            //codename=new String[]{versionCode,versionName};
		}catch(Exception e){
			codename=new String[]{"0",""};
			Log.d("getAppVersionName","getAppVersionName->"+e.toString());
		}
		return codename;
	}

	public static String getAndroidId(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId()==null?"":tm.getDeviceId();
	}

	public static String getApplicationId(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(), 128);
			return applicationInfo.metaData
					.getString("UBTracker-Application-Id");
		} catch (NullPointerException e) {
			Log.w(LogTag, e.toString());
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(LogTag, "getApplicationId.NameNotFoundException", e);
		}
		return "unknown_app_id";
	}

	public static String getClientKey(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(), 128);
			return applicationInfo.metaData.getString("UBTracker-Client-Key");
		} catch (NullPointerException e) {
			Log.w(LogTag, e.toString());
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(LogTag, "getClientKey.NameNotFoundException", e);
		}
		return "unknown_client_key";
	}

	public static String getTimeNow() {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return format.format(new Date());
	}

	public static boolean isNetworkAvailable(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			NetworkInfo info = cManager.getActiveNetworkInfo();

			return (info != null) && (info.isAvailable());
		}

		return false;
	}

	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission,
				context.getPackageName()) == 0;
	}
	
	public static String getImsi(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId()==null?"":tm.getSubscriberId();
	}
	
	public static synchronized long getTimeStamp(){
		long seconds = System.currentTimeMillis() ;//时间戳
		return seconds;
	}
	public static Bundle getMetaDataBundle(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return applicationInfo.metaData;
		} catch (NullPointerException e) {
			LogMgr.showLog("getMetaData NullPointerException = "+e.toString());
		} catch (PackageManager.NameNotFoundException e) {
			LogMgr.showLog("getMetaData NameNotFoundException = "+e.toString());
		}
		return null;
	}
}
