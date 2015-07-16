package p2p.cellcom.com.cn.thread;

import java.util.List;

import android.net.wifi.ScanResult;

public interface WifiScanCallBack {

	public abstract void onStart();

	public abstract void onSuccess(List<ScanResult> list);
	public abstract void onFailed();
}
