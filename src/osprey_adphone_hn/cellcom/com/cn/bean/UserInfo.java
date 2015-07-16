package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class UserInfo {
	@Element(required = false)
	private String username;
	@Element(required = false)
	private String usermom;
	@Element(required = false)
	private String headpicurl;
	@Element(required = false)
	private String sex;
	@Element(required = false)
	private String receiver;
	@Element(required = false)
	private String contact;
	@Element(required = false)
	private String address;
	@Element(required = false)
	private String fulladdress;
	@Element(required = false)
	private String areacode;
	@Element(required = false)
	private String jifen;
	@Element(required = false)
	private String yinyuan;
	@Element(required = false)
	private String huafei;
	@Element(required = false)
	private String viplevel;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsermom() {
		return usermom;
	}

	public void setUsermom(String usermom) {
		this.usermom = usermom;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFulladdress() {
		return fulladdress;
	}

	public void setFulladdress(String fulladdress) {
		this.fulladdress = fulladdress;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getHeadpicurl() {
		return headpicurl;
	}

	public void setHeadpicurl(String headpicurl) {
		this.headpicurl = headpicurl;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}

	public String getYinyuan() {
		return yinyuan;
	}

	public void setYinyuan(String yinyuan) {
		this.yinyuan = yinyuan;
	}

	public String getHuafei() {
		return huafei;
	}

	public void setHuafei(String huafei) {
		this.huafei = huafei;
	}

	public String getViplevel() {
		return viplevel;
	}

	public void setViplevel(String viplevel) {
		this.viplevel = viplevel;
	}
}
