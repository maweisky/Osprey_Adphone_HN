package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Sys {
	@Element(required = false)
	private String serviceurl;
	@Element(required = false)
	private String service;
	@Element(required = false)
	private String version;
	@Element(required = false)
	private String downurl;
	@Element(required = false)
	private String minversion;
	@Element(required = false)
	private String key;
	@Element(required = false)
	private String startgg;
	@Element(required = false)
	private String kfphone;
	@Element(required = false)
	private String pwd2cu;
	@Element(required = false)
	private String areaversion;
	
	public String getServiceurl() {
		return serviceurl;
	}

	public void setServiceurl(String serviceurl) {
		this.serviceurl = serviceurl;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMinversion() {
		return minversion;
	}

	public void setMinversion(String minversion) {
		this.minversion = minversion;
	}

	public String getStartgg() {
		return startgg;
	}

	public void setStartgg(String startgg) {
		this.startgg = startgg;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKfphone() {
		return kfphone;
	}

	public void setKfphone(String kfphone) {
		this.kfphone = kfphone;
	}

	public String getPwd2cu() {
		return pwd2cu;
	}

	public void setPwd2cu(String pwd2cu) {
		this.pwd2cu = pwd2cu;
	}

	public String getAreaversion() {
		return areaversion;
	}

	public void setAreaversion(String areaversion) {
		this.areaversion = areaversion;
	}

}
