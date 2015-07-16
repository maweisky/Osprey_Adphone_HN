package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class GrzxCz implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String ifwin;
	@Element(required = false)
	private String verytype;
	@Element(required = false)
	private String verynum;
	@Element(required = false)
	private String mediaurl;
	@Element(required = false)
	private String medialink;
	@Element(required = false)
	private String mediainfo;
	@Element(required = false)
	private String ggid;
	@Element(required = false)
	private String companyname;
	public String getIfwin() {
		return ifwin;
	}
	public void setIfwin(String ifwin) {
		this.ifwin = ifwin;
	}
	public String getVerytype() {
		return verytype;
	}
	public void setVerytype(String verytype) {
		this.verytype = verytype;
	}
	public String getVerynum() {
		return verynum;
	}
	public void setVerynum(String verynum) {
		this.verynum = verynum;
	}
	public String getMediaurl() {
		return mediaurl;
	}
	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}
	public String getMedialink() {
		return medialink;
	}
	public void setMedialink(String medialink) {
		this.medialink = medialink;
	}
	public String getMediainfo() {
		return mediainfo;
	}
	public void setMediainfo(String mediainfo) {
		this.mediainfo = mediainfo;
	}
	public String getGgid() {
		return ggid;
	}
	public void setGgid(String ggid) {
		this.ggid = ggid;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
}
