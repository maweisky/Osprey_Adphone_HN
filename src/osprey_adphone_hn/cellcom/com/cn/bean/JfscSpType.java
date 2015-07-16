package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class JfscSpType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String vid;
	@Element(required = false)
	private String name;

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
