package p2p.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "Device_Table")
public class DeviceDB {

	private int id;// 数据库id
	private String account;// 用户帐号
	private String deviceid;// 设备id
	private String password;// 摄像头密码
	private String dname;// 摄像头密码
	// private String picurl;//摄像头封面名称 设备id+"_"+时间长度

	public DeviceDB() {
	}

	public DeviceDB(String account, String deviceid, String password, String dname) {
		this.account = account;
		this.deviceid = deviceid;
		this.password = password;
		this.dname = dname;
		// this.picurl = picurl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	// public String getPicurl() {
	// return picurl;
	// }
	// public void setPicurl(String picurl) {
	// this.picurl = picurl;
	// }

	public static DeviceDB converDeviceDB(String account, Device device) {
		return new DeviceDB(account, device.getDeviceId(),
				device.getDevicePassword(), device.getDeviceName());
	}

}
