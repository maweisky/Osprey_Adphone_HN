package p2p.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="RecordVideo_Table")
public class RecordVideoDB {
	
	private int id;//数据库ID
	private String account;//用户ID
	private String deviceId;//设备ID
	private String videoName;//视频名称（年月日时分秒）
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
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	
	

}
