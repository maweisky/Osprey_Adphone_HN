package cellcom.com.cn.util;


public class CellcomUtil {

	 /**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeMD5(String str) {
		if (str == null)
			return str;
		return MD5.compile(str);
	}
}
