package p2p.cellcom.com.cn.global;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.bean.DeviceInfo;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.bean.DeviceDB;
import p2p.cellcom.com.cn.bean.LocalDevice;
import p2p.cellcom.com.cn.db.DBManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.shake.ShakeManager;

public class FList {
	private static FList manager = null;
	private List<Device> lists = new ArrayList<Device>();
//	private HashMap<String, Device> maps = new HashMap<String, Device>();
//	private Hashtable<String, Device> maps = new Hashtable<String, Device>();
	private List<LocalDevice> localdevices = new ArrayList<LocalDevice>();
	private List<LocalDevice> tempLocalDevices = new ArrayList<LocalDevice>();

	public FList() {
		lists.clear();
		localdevices.clear();
//		maps.clear();
		List<DeviceDB> tempListDB = DBManager.getDevicesByAccount(MyApp.app);
		for (Iterator iterator = tempListDB.iterator(); iterator.hasNext();) {
			DeviceDB deviceDB = (DeviceDB) iterator.next();
			if(deviceDB != null){
				Device device = new Device();
				device.setDeviceId(deviceDB.getDeviceid());
				device.setDeviceName(deviceDB.getDname());
				if (!TextUtils.isEmpty(deviceDB.getPassword())) {
					device.setDevicePassword(deviceDB.getPassword());
				}
				if(!TextUtils.isEmpty(NpcCommon.mThreeNum)){
					device.setActiveUser(NpcCommon.mThreeNum);
				}
//				maps.put(device.getDeviceId(), device);
				lists.add(device);
			}
		}
	}

	public static FList getInstance() {
		if (manager == null) {
			manager = new FList();
		}
		return manager;
	}

	public synchronized void initList(List<DeviceInfo> list) {
		List<Device> tempList = new ArrayList<Device>();
		HashMap<String, Device> tempMaps = new HashMap<String, Device>();
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			Device device = (Device) iterator.next();
			tempMaps.put(device.getDeviceId(), device);
		}
//		tempMaps.putAll(maps);
//		maps.clear();
		for (DeviceInfo deviceInfo : list) {
			DeviceDB deviceDB = null;
			Device device = tempMaps.get(deviceInfo.getDeviceid());
			if (device == null) {
				device = new Device();
			}
			deviceDB = DBManager.getDeviceDB(MyApp.app, deviceInfo.getDeviceid());				
			device.setDeviceId(deviceInfo.getDeviceid());
			if (deviceDB != null && !TextUtils.isEmpty(deviceDB.getPassword())) {
				device.setDevicePassword(deviceDB.getPassword());
			}
			if(!TextUtils.isEmpty(NpcCommon.mThreeNum)){
				device.setActiveUser(NpcCommon.mThreeNum);
			}
			device.setDeviceName(deviceInfo.getDname());
			device.setAddr(deviceInfo.getAddr());
			if(!TextUtils.isEmpty(deviceInfo.getPicurl())){
				device.setServerImgUrl(deviceInfo.getPicurl());
			}
			
			tempList.add(device);
//			maps.put(device.getDeviceId(), device);
			DBManager.saveDevice(MyApp.app, device);
		}
		tempMaps.clear();
		lists.clear();
		lists.addAll(tempList);
		tempList.clear();
//		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
//			Device device = (Device) iterator.next();
//			maps.put(device.getDeviceId(), device);
//			DBManager.saveDevice(MyApp.app, device);
//		}
	}

	public List<Device> list() {
		return lists;
	}

//	public Hashtable<String, Device> map() {
//		return maps;
//	}

	public Device get(int position) {
		if (position >= lists.size()) {
			return null;
		} else {
			return lists.get(position);
		}
	}

	public int getType(String deviceId) {
//		Device device = maps.get(deviceId);
		Device device = isDevice(deviceId);
		if (null == device) {
			return P2PValue.DeviceType.UNKNOWN;
		} else {
			return device.getDeviceType();
		}

	}

	public void setType(String deviceId, int type) {
//		Device device = maps.get(deviceId);
		Device device = isDevice(deviceId);
		if (null != device) {
			device.setDeviceType(type);
			DBManager.saveDevice(MyApp.app, device);
		}
	}

	public int getState(String deviceId) {
//		Device device = maps.get(deviceId);		
		Device device = isDevice(deviceId);
		if (null == device) {
			return Constants.DeviceState.OFFLINE;
		} else {
			return device.getOnLineState();
		}
	}

	public void setState(String deviceId, int state) {
//		Device device = maps.get(deviceId);
		Device device = isDevice(deviceId);
		if (null != device) {
			device.setOnLineState(state);
		}
	}

	public void setDefenceState(String deviceId, int state) {
//		Device device = maps.get(deviceId);
		Device device = isDevice(deviceId);
		if (null != device) {
			device.setDefenceState(state);
		}
	}

	public void setIsClickGetDefenceState(String deviceId, boolean bool) {
//		Device device = maps.get(deviceId);
		Device device = isDevice(deviceId);
		if (null != device) {
			device.setClickGetDefenceState(bool);
		}
	}

	public int size() {
		return lists.size();
	}

	public void sort() {
//		Collections.sort(lists);
	}

	public void delete(Device device, int position, Handler handler) {
//		maps.remove(device.getDeviceId());
		lists.remove(position);
		DBManager.deleteDevice(MyApp.app, device.getDeviceId());
		handler.sendEmptyMessage(0);

		Intent refreshNearlyTell = new Intent();		
		refreshNearlyTell.setAction(Constants.Action.ACTION_REFRESH_NEARLY_TELL);
		MyApp.app.sendBroadcast(refreshNearlyTell);
	}
	
	public void delete(Device device, int position) {
//		maps.remove(device.getDeviceId());
		lists.remove(position);
		DBManager.deleteDevice(MyApp.app, device.getDeviceId());
	}
	
	public void delete(int position) {
//		maps.remove(lists.get(position).getDeviceId());
		DBManager.deleteDevice(MyApp.app, lists.get(position).getDeviceId());
		lists.remove(position);
	}

	public void insert(Device device) {
		Log.e("flist", "insert");
		lists.add(device);
//		maps.put(device.getDeviceId(), device);
		DBManager.saveDevice(MyApp.app, device);
		String[] contactIds = new String[] { device.getDeviceId() };
		P2PHandler.getInstance().getFriendStatus(contactIds);
	}

//	public synchronized void update(Device device) {
//		Device tempDevice = maps.get(device.getDeviceId());
//		tempDevice.setDeviceName(device.getDeviceName());
//		tempDevice.setDefenceState(device.getDefenceState());
//		tempDevice.setAddr(device.getAddr());
//		tempDevice.set
//		DBManager.saveDevice(MyApp.app, device);
//	}

	public Device isDevice(String deviceId) {
//		LogMgr.showLog("maps.size()------------->" + maps.size());
//		Device device = maps.get(deviceid);
		for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
			Device tempDevice = (Device) iterator.next();
			if(deviceId.equalsIgnoreCase(tempDevice.getDeviceId())){
				return tempDevice;
			}
		}
		return null;
	}
	
	public int getPosition(String deviceId){
		for (int i = 0; i < size(); i++) {
			if(deviceId.equals(lists.get(i).getDeviceId())){
				return i;
			}
		}
		return -1;
	}
	

	public synchronized void updateOnlineState() {
		// 获取好友在线状态

//		FList flist = FList.getInstance();
		if (lists.size() <= 0) {
			LogMgr.showLog("stop UpdateOnlineState");
			Intent friends = new Intent();
			friends.setAction(Constants.Action.GET_FRIENDS_STATE);
			MyApp.app.sendBroadcast(friends);
			return;
		}
		
		String[] contactIds = new String[lists.size()];
//		List<Device> lists = lists.list();
		int i = 0;
		for (Device device : lists) {
			contactIds[i] = device.getDeviceId();
			i++;
		}
		P2PHandler.getInstance().getFriendStatus(contactIds);
	}

	public void getDefenceState() {
		new Thread() {
			public void run() {
				LogMgr.showLog("线程启动了");
				for (int i = 0; i < lists.size(); i++) {
					Device device = lists.get(i);
					if ((device.getDeviceType() == P2PValue.DeviceType.DOORBELL
							|| device.getDeviceType() == P2PValue.DeviceType.IPC || device	.getDeviceType() == P2PValue.DeviceType.NPC)) {
						P2PHandler.getInstance().getDefenceStates(device.getDeviceId(), device.getDevicePassword());
					}
				}
			}
		}.start();

	}

	public synchronized void searchLocalDevice() {
		ShakeManager.getInstance().setSearchTime(5000);
		ShakeManager.getInstance().setHandler(mHandler);
		if (ShakeManager.getInstance().shaking()) {
			tempLocalDevices.clear();
		}
	}

	public void updateLocalDeviceWithLocalFriends() {
		List<LocalDevice> removeList = new ArrayList<LocalDevice>();
		for (LocalDevice localDevice : localdevices) {
			LogMgr.showLog("updateLocalDeviceWithLocalFriends");
			if (null != manager.isDevice(localDevice.getContactId())) {
				removeList.add(localDevice);
			}
		}

		for (LocalDevice localDevice : removeList) {
			localdevices.remove(localDevice);
		}
	}

	public List<LocalDevice> getLocalDevices() {
		return this.localdevices;
	}

	public List<LocalDevice> getUnsetPasswordLocalDevices() {
		List<LocalDevice> datas = new ArrayList<LocalDevice>();

		for (LocalDevice device : this.localdevices) {
			int flag = device.flag;
			if (flag == Constants.DeviceFlag.UNSET_PASSWORD) {
				datas.add(device);
			}
		}
		return datas;
	}

	public List<LocalDevice> getSetPasswordLocalDevices() {
		List<LocalDevice> datas = new ArrayList<LocalDevice>();
		for (LocalDevice device : localdevices) {
			int flag = device.flag;
			if (flag == Constants.DeviceFlag.ALREADY_SET_PASSWORD) {
				datas.add(device);
			}
		}
		return datas;
	}

	public LocalDevice isContactUnSetPassword(String deviceId) {
		LogMgr.showLog("isContactUnSetPassword");
		if (null == this.isDevice(deviceId)) {
			return null;
		}

		for (LocalDevice device : this.localdevices) {
			if (device.contactId.equals(deviceId)) {
				if (device.flag == Constants.DeviceFlag.UNSET_PASSWORD) {
					return device;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public void updateLocalDeviceFlag(String contactId, int flag) {
		for (LocalDevice device : this.localdevices) {
			if (device.contactId.equals(contactId)) {
				device.flag = flag;
				return;
			}
		}
	}

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ShakeManager.HANDLE_ID_SEARCH_END:
				localdevices.clear();
				for (LocalDevice localDevice : tempLocalDevices) {
					Log.e("my", "localDevice:" + localDevice.contactId);
					localdevices.add(localDevice);
				}
				Intent i = new Intent();
				i.setAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
				MyApp.app.sendBroadcast(i);
				break;
			case ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO:
				Bundle bundle = msg.getData();
				String id = bundle.getString("id");
				String name = bundle.getString("name");
				int flag = bundle.getInt("flag", Constants.DeviceFlag.ALREADY_SET_PASSWORD);
				int type = bundle.getInt("type", P2PValue.DeviceType.UNKNOWN);
				
				if(TextUtils.isEmpty(id)){
					LogMgr.showLog("HANDLE_ID_RECEIVE_DEVICE_INFO, deviceId--------------->null");
				}else{
					LogMgr.showLog("HANDLE_ID_RECEIVE_DEVICE_INFO, deviceId--------------->"  + id);
				}
				if (null == isDevice(id)) {
					InetAddress address = (InetAddress) bundle.getSerializable("address");
					LocalDevice localDevice = new LocalDevice();
					localDevice.setContactId(id);
					localDevice.setFlag(flag);
					localDevice.setType(type);
					localDevice.setAddress(address);

					if (!tempLocalDevices.contains(localDevice)) {
						tempLocalDevices.add(localDevice);
					}

				}
				break;

			}
			return false;
		}
	});
}
