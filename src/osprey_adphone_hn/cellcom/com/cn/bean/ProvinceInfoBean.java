package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.sqlite.Table;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
@Table(name="jshprovince")
public class ProvinceInfoBean {
	@Element(required = false)
	private int id;
	@Element(required = false)
	private String pname;//对应的name
	@Element(required = false)
	private String pid;//对应的id
	@ElementList(required = false, type = CityInfoBean.class)
	private List<CityInfoBean> data = new ArrayList<CityInfoBean>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<CityInfoBean> getData() {
		return data;
	}
	public void setData(List<CityInfoBean> data) {
		this.data = data;
	}
}
