package cellcom.com.cn.util;

import android.util.Log;

/**
 * 日志工具类
 * @author WangZhongjie
 *
 */
public class LogMgr {

	private static final String TAG="cellcomnet";
	
	// 是否打开Log日志输出 ，true打开，，false关闭
	public static boolean logOn = true;
  
	
	/**** 5中Log日志类型 *******/
	/** 调试日志类型 */
	public static final int DEBUG = 111;
	/** 错误日志类型 */
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告调试日志类型 */
	public static final int WARN = 115;
 
	/** 显示，打印日�?*/
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
	
	public void closeLog() {
		logOn=false;
	}
	
	public void openLog() {
		logOn=true;
	}
	//默认info级别
	public static void showLog(String Message) {
		showLog(Message,INFO);
	}
}
