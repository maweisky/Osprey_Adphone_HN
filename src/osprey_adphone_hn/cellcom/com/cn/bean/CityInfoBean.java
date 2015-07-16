package osprey_adphone_hn.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Table;

import org.simpleframework.xml.Element;
@Table(name="jshcityinfo")
public class CityInfoBean {
	@Element(required = false)
	private int id;
	@Element(required = false)
	private String pid;//对应的pid
	@Element(required = false)
	private String cid;
	@Element(required = false)
	private String cityname;
	@Element(required = false)
	private String areacode;
	@Element(required = false)
	private String aname;
	@Element(required = false)
	private String aid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
}
