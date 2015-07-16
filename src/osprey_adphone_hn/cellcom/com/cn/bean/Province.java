package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

public class Province {

	private String ProID;
	private String name;
	private String ProSort;
	private String ProRemark;
	private List<City> citys=new ArrayList<City>();
	public String getProID() {
		return ProID;
	}
	public void setProID(String proID) {
		ProID = proID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProSort() {
		return ProSort;
	}
	public void setProSort(String proSort) {
		ProSort = proSort;
	}
	public String getProRemark() {
		return ProRemark;
	}
	public void setProRemark(String proRemark) {
		ProRemark = proRemark;
	}
	public List<City> getCitys() {
		return citys;
	}
	public void setCitys(List<City> citys) {
		this.citys = citys;
	}
}
