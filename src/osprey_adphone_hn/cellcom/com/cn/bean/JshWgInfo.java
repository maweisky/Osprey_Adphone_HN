package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root
public class JshWgInfo {
	@Element(required = false)
	private String type;
	@Element(required = false)
	private String logtime;
	@Element(required = false)
	private String info;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLogtime() {
		return logtime;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
