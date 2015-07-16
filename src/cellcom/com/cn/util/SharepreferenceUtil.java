package cellcom.com.cn.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * 
 * @author Administrator mw
 *
 */
public class SharepreferenceUtil {

	/**
	 * 从本地文件中读取数据
	 * 
	 * @param cxt 上下文对象
	 * @param name 获取数据的key
	 * @return value
	 */
	public static String getDate(Context cxt, String name) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(cxt);
		sp = cxt.getSharedPreferences(Consts.cellcomnetXmlName, cxt.MODE_PRIVATE);
		String value = sp.getString(name, "");
		return value;
	}
	
	/**
	 *  保存数据到本地文件
	 *  
	 * @param cxt 上下文对象
	 * @param str_key_values 保存数据的键值对
	 */
	public static void saveData(Context cxt, String[][] str_key_values) {
		SharedPreferences sp = cxt.getSharedPreferences(Consts.cellcomnetXmlName, cxt.MODE_PRIVATE);
		if (str_key_values != null && str_key_values.length > 0) {
			if (str_key_values[0].length != 2)
				throw new IllegalArgumentException("参数格式错误,key-value");
			Editor editor = sp.edit();
			for (String[] keyValue : str_key_values) {
				LogMgr.showLog("savedate param=" + keyValue[0] + " value:"
						+ keyValue[1]);
				editor.putString(keyValue[0], keyValue[1]);
			}
			editor.commit();
		}
	}
}
