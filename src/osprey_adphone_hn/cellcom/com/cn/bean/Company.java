package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Company {
	@Element(required = false)
	private String adid;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String logourl;
	@Element(required = false)
	private String siteurl;
	@Element(required = false)
	private String sortindex;
	@Element(required = false)
	private String totalnum;
	public String getAdid() {
		return adid;
	}
	public void setAdid(String adid) {
		this.adid = adid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogourl() {
		return logourl;
	}
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
	public String getSiteurl() {
		return siteurl;
	}
	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}
	public String getSortindex() {
		return sortindex;
	}
	public void setSortindex(String sortindex) {
		this.sortindex = sortindex;
	}
	public String getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(String totalnum) {
		this.totalnum = totalnum;
	}

}
