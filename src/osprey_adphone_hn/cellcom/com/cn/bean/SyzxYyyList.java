package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SyzxYyyList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String ggid;
	@Element(required = false)
	private String smallpic;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String info;
	@Element(required = false)
	private String largepic;
	@Element(required = false)
	private String sces;
	@Element(required = false)
	private String logtime;
	@Element(required = false)
	private String begintime;
	@Element(required = false)
	private String endtime;

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getSmallpic() {
		return smallpic;
	}

	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

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

	public String getLargepic() {
		return largepic;
	}

	public void setLargepic(String largepic) {
		this.largepic = largepic;
	}

	public String getSces() {
		return sces;
	}

	public void setSces(String sces) {
		this.sces = sces;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
}
