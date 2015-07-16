package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class AdvShow {
	@Element(required = false)
	private String ifshow;
	@Element(required = false)
	private String imgurl;

	public String getIfshow() {
		return ifshow;
	}

	public void setIfshow(String ifshow) {
		this.ifshow = ifshow;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
}