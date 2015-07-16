package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class JfscSpListDetailResult {
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String info;
	@ElementList(required = false, type = JfscSpListDetail.class)
	private List<JfscSpListDetail> picurl = new ArrayList<JfscSpListDetail>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<JfscSpListDetail> getPicurl() {
		return picurl;
	}

	public void setPicurl(List<JfscSpListDetail> picurl) {
		this.picurl = picurl;
	}
}
