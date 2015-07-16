package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class JfscSpList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String id;
	@Element(required = false)
	private String cid;
	@Element(required = false)
	private String smallpic;
	@Element(required = false)
	private String jifen;
	@Element(required = false)
	private String type;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String leftnum;
	@Element(required = false)
	private String simpleinfo;
	@Element(required = false)
	private String num;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getSmallpic() {
		return smallpic;
	}

	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeftnum() {
		return leftnum;
	}

	public void setLeftnum(String leftnum) {
		this.leftnum = leftnum;
	}

	public String getSimpleinfo() {
		return simpleinfo;
	}

	public void setSimpleinfo(String simpleinfo) {
		this.simpleinfo = simpleinfo;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}
