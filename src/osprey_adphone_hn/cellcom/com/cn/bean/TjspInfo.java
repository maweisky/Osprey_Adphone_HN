package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class TjspInfo implements Serializable{
	
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
	private int leftnum;
	@Element(required = false)
	private String simpleinfo;
	@Element(required = false)
	private String discountprice;
	
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
	public int getLeftnum() {
		return leftnum;
	}
	public void setLeftnum(int leftnum) {
		this.leftnum = leftnum;
	}
	public String getSimpleinfo() {
		return simpleinfo;
	}
	public void setSimpleinfo(String simpleinfo) {
		this.simpleinfo = simpleinfo;
	}
	public String getDiscountprice() {
		return discountprice;
	}
	public void setDiscountprice(String discountprice) {
		this.discountprice = discountprice;
	}
	

}
