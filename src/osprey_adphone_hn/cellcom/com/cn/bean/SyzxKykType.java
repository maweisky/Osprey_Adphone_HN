package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SyzxKykType {
	@Element(required = false)
	private String verytype;
	@Element(required = false)
	private String typename;


	public String getVerytype() {
		return verytype;
	}

	public void setVerytype(String verytype) {
		this.verytype = verytype;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

}
