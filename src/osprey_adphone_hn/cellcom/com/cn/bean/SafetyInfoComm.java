package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

public class SafetyInfoComm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageId;//消息ID
	private String deviceId;//源ID（设备号）
	private String alarmTime;//报警时间
	private String alarmpicUrl;//报警图片路径
	private String alarmType;//类型
	private String fangqu;//防区
	private String cannel;//通道
	private String serverreceiveTime;//服务器接收时间
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getAlarmpicUrl() {
		return alarmpicUrl;
	}
	public void setAlarmpicUrl(String alarmpicUrl) {
		this.alarmpicUrl = alarmpicUrl;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getFangqu() {
		return fangqu;
	}
	public void setFangqu(String fangqu) {
		this.fangqu = fangqu;
	}
	public String getCannel() {
		return cannel;
	}
	public void setCannel(String cannel) {
		this.cannel = cannel;
	}
	public String getServerreceiveTime() {
		return serverreceiveTime;
	}
	public void setServerreceiveTime(String serverreceiveTime) {
		this.serverreceiveTime = serverreceiveTime;
	}
}
