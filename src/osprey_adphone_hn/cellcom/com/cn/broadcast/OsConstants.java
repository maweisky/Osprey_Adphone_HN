package osprey_adphone_hn.cellcom.com.cn.broadcast;

public class OsConstants {
	
	private static final String PACKAGE_NAME = "osprey_adphone_hn.cellcom.com.cn.";
	public static boolean isClickSet = false;//该标记用来表示是否点击了设备的设置按钮， true为启动，false为关闭
	public static boolean isClickRecord = false;//该标记用来表示是否点击了设备的回放按钮， true为启动，false为关闭
	public static boolean isClickPlay = false;//该标记用来表示是否点击了设备的播放按钮， true为启动，false为关闭
	public static String deviceID;
	public class JSH{
		public static final String ADD_DEVICES_SUCCES = PACKAGE_NAME + "ADD_DEVICES_SUCCES";
		public static final String REFRUSH_DEVICES_NOTICE = PACKAGE_NAME + "REFRUSH_DEVICES_NOTICE";
		public static final String REFRUSH_ADAPTER_NOTICE = PACKAGE_NAME + "REFRUSH_ADAPTER_NOTICE";
		public static final String SETTIING_WIFI_SUCCESS = PACKAGE_NAME + "SETTIING_WIFI_SUCCESS";
		public static final String SCAN_WIFI_END = PACKAGE_NAME + "SCAN_WIFI_END";
	}
	
	
	

}
