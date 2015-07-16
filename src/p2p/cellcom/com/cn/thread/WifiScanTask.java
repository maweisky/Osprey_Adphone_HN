package p2p.cellcom.com.cn.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiScanTask extends AsyncTask<Integer, Integer, Integer> {

	private Context context;
	private List<ScanResult> list = new ArrayList<ScanResult>();
	private WifiScanCallBack callBack;
	private WifiManagers wifiManagers;

	public WifiScanTask(Context context, WifiScanCallBack callBack) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callBack = callBack;
		wifiManagers = new WifiManagers(context);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callBack.onStart();
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		if(wifiManagers.closeWifi()){
			return -1;
		}
		list.clear();
		wifiManagers.startScan();
		List<ScanResult> wifis = wifiManagers.getWifiList();
		for (Iterator iterator = wifis.iterator(); iterator.hasNext();) {
			ScanResult scanResult = (ScanResult) iterator.next();
			list.add(scanResult);
		}
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result >= 0){
			callBack.onSuccess(list);
		}else{
			callBack.onFailed();
		}
	}

}
