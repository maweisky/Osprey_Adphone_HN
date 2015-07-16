package p2p.cellcom.com.cn.bean;

import java.io.Serializable;

import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;

import android.text.TextUtils;

import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.NpcCommon;

public class Device implements Serializable, Comparable {
	// id
	private int id;
	// 设备名称
	private String deviceName;
	// 设备ID
	private String deviceId;
	// 设备监控密码 注意：不是登陆密码，只有当设备类型为设备才有
	private String devicePassword;
	// 设备类型
	private int deviceType;
	// 此设备发来多少条未读消息
	private int messageCount;
	// 此设备封面图片的服务端地址
	private String serverImgUrl;
	// //此设备封面图片名称
	// private String localImg;
	// 当前登录的用户
	private String activeUser;
	// 在线状态 不保存数据库
	private int onLineState = Constants.DeviceState.OFFLINE;
	// 布放状态不保存数据库
	private int defenceState = Constants.DefenceState.DEFENCE_STATE_LOADING;
	// 记录是否是点击获取布放状态 不保存数据库
	private boolean isClickGetDefenceState = false;
	// 是否展开列表
	private boolean isExpand = false;
	// 此设备所选为家庭
	private String addr;

	// 按在线状态排序
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		Device o = (Device) arg0;
		if (o.onLineState > this.onLineState) {
			return 1;
		} else if (o.onLineState < this.onLineState) {
			return -1;
		} else {
			return 0;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		LogMgr.showLog("设置了DeviceId--------------->" + deviceId );
		this.deviceId = deviceId;
	}

	public String getDevicePassword() {
		return devicePassword;
	}

	public void setDevicePassword(String devicePassword) {
		this.devicePassword = devicePassword;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public String getServerImgUrl() {
		return serverImgUrl;
	}

	public void setServerImgUrl(String serverImgUrl) {
		this.serverImgUrl = serverImgUrl;
	}

	// public String getLocalImg() {
	// return localImg;
	// }
	// public void setLocalImg(String localImg) {
	// this.localImg = localImg;
	// }
	public String getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(String activeUser) {
		this.activeUser = activeUser;
	}

	public int getOnLineState() {
		return onLineState;
	}

	public void setOnLineState(int onLineState) {
		this.onLineState = onLineState;
	}

	public int getDefenceState() {
		return defenceState;
	}

	public void setDefenceState(int defenceState) {
		LogMgr.showLog("deviceId--->" + deviceId + "，defenceState----------->" + defenceState);
		this.defenceState = defenceState;
	}

	public boolean isClickGetDefenceState() {
		return isClickGetDefenceState;
	}

	public void setClickGetDefenceState(boolean isClickGetDefenceState) {
		this.isClickGetDefenceState = isClickGetDefenceState;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
