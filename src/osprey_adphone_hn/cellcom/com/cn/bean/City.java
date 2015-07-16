package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

public class City {

	private String CityID;
	private String name;
	private String ProID;
	private String CitySort;
	private List<Area> list=new ArrayList<Area>();
	public String getCityID() {
		return CityID;
	}
	public void setCityID(String cityID) {
		CityID = cityID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProID() {
		return ProID;
	}
	public void setProID(String proID) {
		ProID = proID;
	}
	public String getCitySort() {
		return CitySort;
	}
	public void setCitySort(String citySort) {
		CitySort = citySort;
	}
	public List<Area> getList() {
		return list;
	}
	public void setList(List<Area> list) {
		this.list = list;
	}
}
