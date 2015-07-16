package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class KykAd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String meitiurl;
	@Element(required = false)
	private String linkurl;
	@Element(required = false)
	private String info;

	public String getMeitiurl() {
		return meitiurl;
	}

	public void setMeitiurl(String meitiurl) {
		this.meitiurl = meitiurl;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
