package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class YyyPrizeNew {
	@Element(required = false)
	private String ifwin;
	@Element(required = false)
	private String showmessage;
	@Element(required = false)
	private int verytype;
	@Element(required = false)
	private int verynum;
	@Element(required = false)
	private String adlink;
	@Element(required = false)
	private String picurl;
	public String getIfwin() {
		return ifwin;
	}
	public void setIfwin(String ifwin) {
		this.ifwin = ifwin;
	}
	public String getShowmessage() {
		return showmessage;
	}
	public void setShowmessage(String showmessage) {
		this.showmessage = showmessage;
	}
	public int getVerytype() {
		return verytype;
	}
	public void setVerytype(int verytype) {
		this.verytype = verytype;
	}
	public int getVerynum() {
		return verynum;
	}
	public void setVerynum(int verynum) {
		this.verynum = verynum;
	}
	public String getAdlink() {
		return adlink;
	}
	public void setAdlink(String adlink) {
		this.adlink = adlink;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
}
