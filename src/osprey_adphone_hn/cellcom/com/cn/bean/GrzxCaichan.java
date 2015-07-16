package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 个人财产，包括积分、话费、银元
 * 
 * @author Administrator
 * 
 */
@Root
public class GrzxCaichan {
	@Element(required = false)
	private String info;
	@Element(required = false)
	private String logtime;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
}
