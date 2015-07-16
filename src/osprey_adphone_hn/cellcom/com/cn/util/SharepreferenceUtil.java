package osprey_adphone_hn.cellcom.com.cn.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author Administrator mw
 * 
 */
public class SharepreferenceUtil {
	public static String fileName = "contextfile";
	public static final String SP_FILE_GWELL = "gwell";
	public static final String KEY_RECENTNAME_EMAIL = "recentName_email";
	public static final String KEY_RECENTPASS_EMAIL = "recentPass_email";
	public static final String RECENT_LOGIN_TYPE = "recent_login_type";

	public static void write(Context context, String fileName, String k, int v) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(k, v);
		editor.commit();
	}

	public static void write(Context context, String fileName, String k,
			boolean v) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putBoolean(k, v);
		editor.commit();
	}

	public static void write(Context context, String fileName, String k,
			String v) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(k, v);
		editor.commit();
	}

	public static void write(Context context, String fileName, String k, long v) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putLong(k, v);
		editor.commit();
	}

	public static int readInt(Context context, String fileName, String k) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getInt(k, 0);
	}

	public static int readInt(Context context, String fileName, String k,
			int defv) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getInt(k, defv);
	}

	public static long readLong(Context context, String fileName, String k,
			int defv) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getLong(k, defv);
	}

	public static boolean readBoolean(Context context, String fileName, String k) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, false);
	}

	public static boolean readBoolean(Context context, String fileName,
			String k, boolean defBool) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getBoolean(k, defBool);
	}

	public static String readString(Context context, String fileName, String k) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getString(k, null);
	}

	public static String readString(Context context, String fileName, String k,
			String defV) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return preference.getString(k, defV);
	}

	public static void remove(Context context, String fileName, String k) {
		SharedPreferences preference = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.remove(k);
		editor.commit();
	}

	public static void clean(Context cxt, String fileName) {
		SharedPreferences preference = cxt.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor.clear();
		editor.commit();
	}

}
