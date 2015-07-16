package osprey_adphone_hn.cellcom.com.cn.util;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;

public class CommonUtils {
	private static String currentChildMenuActivity = "";// 记录当前所在子菜单界面，用于避免重复打开子菜单界面问题

	public static String getCurrentChildMenuActivity() {
		return currentChildMenuActivity;
	}

	public static void setCurrentChildMenuActivity(
			String currentChildMenuActivity) {
		CommonUtils.currentChildMenuActivity = currentChildMenuActivity;
	}

	public static void refreshLocalCaichan(Context context, String type,
			String num) {
		if (num != null && !num.trim().equals("")) {
			if (type.equals("1")) {// 刷新本地积分
				if (SharepreferenceUtil.readString(context,
						SharepreferenceUtil.fileName, "jifen", "").equals("")) {
					SharepreferenceUtil.write(context,
							SharepreferenceUtil.fileName, "jifen", num);
				} else {
					SharepreferenceUtil.write(
							context,
							SharepreferenceUtil.fileName,
							"jifen",
							(Float.parseFloat(SharepreferenceUtil.readString(
									context, SharepreferenceUtil.fileName,
									"jifen")) + Float.parseFloat(num))
									+ "");
				}
			} else if (type.equals("2")) {// 刷新本地话费
				if (SharepreferenceUtil.readString(context,
						SharepreferenceUtil.fileName, "huafei", "").equals("")) {
					SharepreferenceUtil.write(context,
							SharepreferenceUtil.fileName, "huafei", num);
				} else {
					SharepreferenceUtil.write(
							context,
							SharepreferenceUtil.fileName,
							"huafei",
							(Float.parseFloat(SharepreferenceUtil.readString(
									context, SharepreferenceUtil.fileName,
									"huafei")) + Float.parseFloat(num))
									+ "");
				}
			} else if (type.equals("3")) {// 刷新本地银元
				if (SharepreferenceUtil.readString(context,
						SharepreferenceUtil.fileName, "yinyuan", "").equals("")) {
					SharepreferenceUtil.write(context,
							SharepreferenceUtil.fileName, "yinyuan", num);
				} else {
					SharepreferenceUtil.write(
							context,
							SharepreferenceUtil.fileName,
							"yinyuan",
							(Float.parseFloat(SharepreferenceUtil.readString(
									context, SharepreferenceUtil.fileName,
									"yinyuan")) + Float.parseFloat(num))
									+ "");
				}
			}
		}
	}
	
	//获取当前wifi
		public static ScanResult getScanResult(List<ScanResult> list, String ssid){
			for(int i = 0x0, len = list.size(); i < len; i = i + 0x1) {
	            ScanResult result = (ScanResult)list.get(i);
	            if(result.SSID.equals(ssid)) {
	                return result;
	            }
	        }
			return null;
		}

}
