package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrize {
	@Element(required = false)
	private String ifmoney;
	@Element(required = false)
	private String jpid;
	@Element(required = false)
	private String jptype;

	public String getIfmoney() {
		return ifmoney;
	}

	public void setIfmoney(String ifmoney) {
		this.ifmoney = ifmoney;
	}

	public String getJpid() {
		return jpid;
	}

	public void setJpid(String jpid) {
		this.jpid = jpid;
	}

	public String getJptype() {
		return jptype;
	}

	public void setJptype(String jptype) {
		this.jptype = jptype;
	}
}
