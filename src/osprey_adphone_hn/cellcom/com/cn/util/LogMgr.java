package osprey_adphone_hn.cellcom.com.cn.util;

import android.util.Log;

/**
 * 
 * @author WangZhongjie
 * 
 */
public class LogMgr {

	private static final String TAG = "osprey_adphone_hn";

	public static boolean logOn = false;

	public static final int DEBUG = 111;
	public static final int ERROR = 112;
	public static final int INFO = 113;
	public static final int VERBOSE = 114;
	public static final int WARN = 115;

	public static void showLog(String Message, int Style) {
		if (logOn) {
			switch (Style) {

			case DEBUG:
				Log.d(TAG, Message);
				break;
			case ERROR:
				Log.e(TAG, Message);
				break;
			case INFO:
				Log.i(TAG, Message);
				break;
			case VERBOSE:
				Log.v(TAG, Message);
				break;
			case WARN:
				Log.w(TAG, Message);
				break;
			}
		}
	}

	//
	public static void showLog(String Message) {
		showLog(Message, INFO);
	}
}
