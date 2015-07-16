package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Login {
	@Element(required = false)
	private String uid;
	@Element(required = false)
	private String username;
	@Element(required = false)
	private String usermom;
	@Element(required = false)
	private String headpicurl;
	@Element(required = false)
	private String jifen;
	@Element(required = false)
	private String yinyuan;
	@Element(required = false)
	private String huafei;
	@Element(required = false)
	private String cuemail;
	@Element(required = false)
	private String token;
	@Element(required = false)
	private String cityname;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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

	public String getCuemail() {
		return cuemail;
	}

	public void setCuemail(String cuemail) {
		this.cuemail = cuemail;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	
}
