package com.mediatek.elian;

import android.net.wifi.ScanResult;

public class AirContractManager {
	public static final byte AuthModeOpen = 0x0;
	public static final byte AuthModeShared = 0x1;
	public static final byte AuthModeAutoSwitch = 0x2;
	public static final byte AuthModeWPA = 0x3;
	public static final byte AuthModeWPAPSK = 0x4;
	public static final byte AuthModeWPANone = 0x5;
	public static final byte AuthModeWPA2 = 0x6;
	public static final byte AuthModeWPA2PSK = 0x7;
	public static final byte AuthModeWPA1WPA2 = 0x8;
	public static final byte AuthModeWPA1PSKWPA2PSK = 0x9;
	private ElianNative elianNative;

	public AirContractManager() {
		// TODO Auto-generated constructor stub
		elianNative = new ElianNative();
		elianNative.InitSmartConnection(null, 1, 1);
	}

	public void startAirContract(String ssid, String password,	ScanResult scanResult) {
		elianNative.StartSmartConnection(ssid, password, "", getAuthMode(scanResult));
	}

	public void stopAirContract() {
		elianNative.StopSmartConnection();
	}

	// 获取wifi类型
	private byte getAuthMode(ScanResult result) {
		byte authMode;
		boolean WpaPsk = result.capabilities.contains("WPA-PSK");
		boolean Wpa2Psk = result.capabilities.contains("WPA2-PSK");
		boolean Wpa = result.capabilities.contains("WPA-EAP");
		boolean Wpa2 = result.capabilities.contains("WPA2-EAP");
		if (result.capabilities.contains("WEP")) {
			authMode = AirContractManager.AuthModeOpen;
		} else if ((WpaPsk) && (Wpa2Psk)) {
			authMode = AirContractManager.AuthModeWPA1PSKWPA2PSK;
		} else if (Wpa2Psk) {
			authMode = AirContractManager.AuthModeWPA2PSK;
		} else if (WpaPsk) {
			authMode = AirContractManager.AuthModeWPAPSK;
		} else if ((Wpa) && (Wpa2)) {
			authMode = AirContractManager.AuthModeWPA1WPA2;
		} else if (Wpa2) {
			authMode = AirContractManager.AuthModeWPA2;
		} else if (Wpa) {
			authMode = AirContractManager.AuthModeWPA;
		} else {
			authMode = AirContractManager.AuthModeOpen;
		}
		return authMode;
	}
}
