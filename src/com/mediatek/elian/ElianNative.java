package com.mediatek.elian;

public class ElianNative {

	public native int GetLibVersion();

	public native int GetProtoVersion();

	public native int InitSmartConnection(String paramString, int paramInt1, int paramInt2);
	
	/**
	 * 
	 * @param paramString1 输入wifi名称（ssid）
	 * @param paramString2 输入wifi密码（password）
	 * @param paramString3 输入使用这名称（可以为""）
	 * @param paramByte 输入wifi密码加密类型（type）
	 * @return
	 */
	public native int StartSmartConnection(String paramString1, String paramString2, String paramString3, byte paramByte);

	public native int StopSmartConnection();

	static {
		System.loadLibrary("elianjni");
	}

	// public static boolean LoadLib()
	// {
	// try
	// {
	// System.loadLibrary("elianjni");
	// return true;
	// }
	// catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
	// {
	// System.err.println("WARNING: Could not load elianjni library!");
	// }
	// return false;
	// }
}
