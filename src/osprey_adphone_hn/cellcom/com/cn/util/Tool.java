package osprey_adphone_hn.cellcom.com.cn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 
 * @author kinglong
 * 
 */
public class Tool {

	/**
	 * 
	 * @param num
	 *            输入相应的手机号码
	 * @return 返回-1为输入号码有误， 1为移动2为联通3为电信
	 */
	public static int netWorkSupply(String num) {
		String yidong = "134135136137138139147150151152157158159187188|130131132145155156186|133153180189"; // 移动的号码段|联通号码段|电信
		num = num.substring(0, 2);
		int shifou = yidong.indexOf(num);
		if (shifou == -1) {
			System.out.print("号码有误");
			return -1;
		} else if (shifou < 43) {
			System.out.print("移动");
			return 1;
		} else if (shifou < 65) {
			System.out.print("联通");
			return 2;
		} else if (shifou > 65) {
			System.out.print("电信");
			return 3;
		}
		return -1;
	}

	/**
	 * MD5 加密
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static Boolean isPhoneNum(String num) {
		Pattern p = Pattern.compile("1\\d{10}");
		Matcher m = p.matcher(num);
		boolean b = m.matches();
		return b;
	}

	/***
	 * 获取时间错
	 * **/
	public static String getTime() {
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		time = sf.format(new Date());
		return time;

	}

	/***
	 * 获取时间错
	 * **/
	public static String getTimeNoss() {
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
		time = sf.format(new Date());

		return time;

	}

	public static String getTimeNoss(Date d) {
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
		time = sf.format(d);

		return time;

	}

	public static String formateTime(String s) {
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d = sf.parse(s);
			time = getTimeNoss(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}

	public static String getTimeNoss(String s) {
		String time = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			Date d = sf.parse(s);
			time = getTimeNoss(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}

	/**
	 * list深copy
	 * 
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List deepCopy(List src) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			List dest = (List) in.readObject();
			return dest;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String cut(String str) {
		str = str.replace(" ", "");
		return str;
	}

	// 还原11位手机号 包括去除“-”
	public static String GetNumber(String num2) {
		String num;
		if (num2 != null) {
			// System.out.println(num2);
			num2 = num2.replaceAll("-", "");
			num = num2.replaceAll(" ", "");
			if (num.startsWith("+86")) {
				num = num.substring(3);
			} else if (num.startsWith("86")) {
				num = num.substring(2);
			} else if (num.startsWith("17909")) {
				num = num.substring(5);
			} else if (num.startsWith("12593")) {
				num = num.substring(5);
			}
		} else {
			num = "";
		}
		return num;
	}

	// 过滤掉特殊字符
	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String inputFilter(String str) {
		String regEx = "[\\u4e00-\\u9fa5]|[a-zA-Z_ 0-9.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String ret = "";
		while (m.find()) {
			ret += m.group();
		}
		return ret;
	}

	// 获得汉语拼音首字母
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

}
