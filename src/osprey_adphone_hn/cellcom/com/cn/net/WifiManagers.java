package osprey_adphone_hn.cellcom.com.cn.net;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.global.MyApp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;

public class WifiManagers {
	
	private static WifiManagers managers;
    private WifiManager wifiManager;// 声明管理对象

    private WifiInfo wifiInfo;// Wifi信息

    private List<ScanResult> scanResultList; // 扫描出来的网络连接列表

    private List<WifiConfiguration> wifiConfigList;// 网络配置列表

    private WifiLock wifiLock;// Wifi锁
    private ScanWifiTask scanWifiTask;
    private boolean isScanning = false;

    public WifiManagers(Context context) {
        this.wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);// 获取Wifi服务

        // 得到Wifi信息
        this.wifiInfo = wifiManager.getConnectionInfo();// 得到连接信息

    }
    
    public static WifiManagers getInstance(){
    	if(managers == null){
    		managers = new WifiManagers(MyApp.app);
    	}
    	return managers;
    }
    
    public boolean getWifiStatus()
    {
         return wifiManager.isWifiEnabled();
    }

    // 打开/关闭 wifi
    public boolean openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            return wifiManager.setWifiEnabled(true);
        } else {
            return false;
        }

    }

    public boolean closeWifi() {
        if (!wifiManager.isWifiEnabled()) {
            return true;
        } else {
            return wifiManager.setWifiEnabled(false);
        }
    }

    // 锁定/解锁wifi
    // 其实锁定WiFI就是判断wifi是否建立成功，在这里使用的是held，握手的意思acquire 得到！
    public void lockWifi() {

        wifiLock.acquire();

    }


    public void unLockWifi() {
        if (!wifiLock.isHeld()) {
            wifiLock.release(); // 释放资源
        }
    }

    // 我本来是写在构造函数中了，但是考虑到不是每次都会使用Wifi锁，所以干脆自己建立一个方法！需要时调用，建立就OK
    public void createWifiLock() {
        wifiLock = wifiManager.createWifiLock("flyfly"); // 创建一个锁的标志
    }

    // 扫描网络

    public void startScan() { 
    	if(!isScanning){
        	scanWifiTask = new ScanWifiTask();
    		scanWifiTask.execute();
    	}
    }

    public List<ScanResult> getWifiList() {
        return scanResultList;
    }

    public List<WifiConfiguration> getWifiConfigList() {
        return wifiConfigList;
    }

    // 获取扫描列表
    public StringBuilder lookUpscan() {
        StringBuilder scanBuilder = new StringBuilder();

        for (int i = 0; i < scanResultList.size(); i++) {
            scanBuilder.append("编号：" + (i + 1));
            scanBuilder.append(scanResultList.get(i).toString());  //所有信息
            scanBuilder.append("\n");
        }

        return scanBuilder;
    }
    //获取指定信号的强度
    public int getLevel(int NetId)
    {
        return scanResultList.get(NetId).level;
    }

    // 获取本机Mac地址
    public String getMac() {
        return (wifiInfo == null) ? "" : wifiInfo.getMacAddress();
    }

    public String getBSSID() {
    	
        return (wifiInfo == null) ? null : wifiInfo.getBSSID();
    }

    public String getSSID() {
        return (wifiInfo == null) ? null : ContextUtil.checkSSid(wifiInfo.getSSID());
    }

    // 返回当前连接的网络的ID
    public int getCurrentNetId() {
        return (wifiInfo == null) ? null : wifiInfo.getNetworkId();
    }

    // 返回所有信息
    public String getwifiInfo() {
        return (wifiInfo == null) ? null : wifiInfo.toString();
    }

    // 获取IP地址
    public int getIP() {
        return (wifiInfo == null) ? null : wifiInfo.getIpAddress();
    }

    // 添加一个连接
    public boolean addNetWordLink(WifiConfiguration config) {
        int NetId = wifiManager.addNetwork(config);
        return wifiManager.enableNetwork(NetId, true);
    }

    // 禁用一个链接
    public boolean disableNetWordLick(int NetId) {
        wifiManager.disableNetwork(NetId);
        return wifiManager.disconnect();
    }

    // 移除一个链接
    public boolean removeNetworkLink(int NetId) {
        return wifiManager.removeNetwork(NetId);
    }
    //不显示SSID
    public void hiddenSSID(int NetId)
    {
        wifiConfigList.get(NetId).hiddenSSID=true;
    }
    //显示SSID
    public void displaySSID(int NetId)
    {
        wifiConfigList.get(NetId).hiddenSSID=false;
    }
    
    //通过ssid获取ScanResult
    public ScanResult getScanResult(String ssid){
    	return CommonUtils.getScanResult(scanResultList, ssid);
    }
    
    public class ScanWifiTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			isScanning = true;
	        wifiManager.startScan();
	        wifiInfo = wifiManager.getConnectionInfo();
	        scanResultList = wifiManager.getScanResults(); // 扫描返回结果列表

	        wifiConfigList = wifiManager.getConfiguredNetworks(); // 扫描配置列表
			return 0;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setAction(OsConstants.JSH.SCAN_WIFI_END);
			MyApp.app.sendBroadcast(intent);
			isScanning = false;
		};
    }
}
