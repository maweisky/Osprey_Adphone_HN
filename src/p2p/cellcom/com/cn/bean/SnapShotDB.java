package p2p.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="SnapShot_Table")//视频截图用数据类
public class SnapShotDB {
	
	private int id;//数据库ID
	private String account;//用户名
	private String deviceId;//设备ID
	private String imageName;//截图名称（年月日时分秒）
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
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	

}
