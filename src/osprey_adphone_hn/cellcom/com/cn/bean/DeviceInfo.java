package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class DeviceInfo {

	@Element(required = false)
	private String deviceid;
	@Element(required = false)
	private String dname;
	@Element(required = false)
	private String picurl;
	@Element(required = false)
	private String addr;

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
