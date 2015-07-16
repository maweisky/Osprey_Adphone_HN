package cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class DESBean {
	@Element(required = false)
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
