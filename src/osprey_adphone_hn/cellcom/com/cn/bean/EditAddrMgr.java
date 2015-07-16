package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;

public class EditAddrMgr {
	
	private List<HouseInfo> p_list = new ArrayList<HouseInfo>();
	private List<HouseInfo> c_list = new ArrayList<HouseInfo>();
	private List<HouseInfo> a_list = new ArrayList<HouseInfo>();
	private List<HouseInfo> g_list = new ArrayList<HouseInfo>();
	private List<HouseInfo> b_list = new ArrayList<HouseInfo>();
	private List<HouseInfo> h_list = new ArrayList<HouseInfo>();
	private int p_position = 0;
	private int c_position = 0;
	private int a_position = 0;
	private int g_position = 0;
	private int b_position = 0;
	private int h_position = 0;

	public List<HouseInfo> getP_list() {
		return p_list;
	}

	public void setP_list(List<HouseInfo> p_list) {
		this.p_list = p_list;
	}

	public List<HouseInfo> getC_list() {
		return c_list;
	}

	public void setC_list(List<HouseInfo> c_list) {
		this.c_list = c_list;
	}

	public List<HouseInfo> getA_list() {
		return a_list;
	}

	public void setA_list(List<HouseInfo> a_list) {
		this.a_list = a_list;
	}

	public List<HouseInfo> getG_list() {
		return g_list;
	}

	public void setG_list(List<HouseInfo> g_list) {
		this.g_list = g_list;
	}

	public List<HouseInfo> getB_list() {
		return b_list;
	}

	public void setB_list(List<HouseInfo> b_list) {
		this.b_list = b_list;
	}

	public List<HouseInfo> getH_list() {
		return h_list;
	}

	public void setH_list(List<HouseInfo> h_list) {
		this.h_list = h_list;
	}

	public int getP_position() {
		return p_position;
	}

	public void setP_position(int p_position) {
		this.p_position = p_position;
	}

	public int getC_position() {
		return c_position;
	}

	public void setC_position(int c_position) {
		this.c_position = c_position;
	}

	public int getA_position() {
		return a_position;
	}

	public void setA_position(int a_position) {
		this.a_position = a_position;
	}

	public int getG_position() {
		return g_position;
	}

	public void setG_position(int g_position) {
		this.g_position = g_position;
	}

	public int getB_position() {
		return b_position;
	}

	public void setB_position(int b_position) {
		this.b_position = b_position;
	}

	public int getH_position() {
		return h_position;
	}

	public void setH_position(int h_position) {
		this.h_position = h_position;
	}
	
	//清除list的数据
	public void clearList(String houseType){
		if(houseType.equalsIgnoreCase("p")){
			p_list.clear();
		}else if(houseType.equalsIgnoreCase("c")){
			c_list.clear();
		}else if(houseType.equalsIgnoreCase("a")){
			a_list.clear();
		}else if(houseType.equalsIgnoreCase("g")){
			g_list.clear();
		}else if(houseType.equalsIgnoreCase("b")){
			b_list.clear();
		}else if(houseType.equalsIgnoreCase("h")){
			h_list.clear();
		}
	}
	
	//添加数据到对应的list
	public void addList(String houseType, List<HouseInfo> list){
		if(houseType.equalsIgnoreCase("p")){
			p_list.clear();
			p_list.addAll(list);
			setP_position(0);
			LogMgr.showLog("p_list.size()---------3--------->" + p_list.size());
		}else if(houseType.equalsIgnoreCase("c")){
			c_list.clear();
			c_list.addAll(list);
			setC_position(0);
			LogMgr.showLog("c_list.size()---------3--------->" + c_list.size());
		}else if(houseType.equalsIgnoreCase("a")){
			a_list.clear();
			a_list.addAll(list);
			setA_position(0);
			LogMgr.showLog("a_list.size()---------3--------->" + a_list.size());
		}else if(houseType.equalsIgnoreCase("g")){
			g_list.clear();
			g_list.addAll(list);
			setG_position(0);
			LogMgr.showLog("g_list.size()---------3--------->" + g_list.size());
		}else if(houseType.equalsIgnoreCase("b")){
			b_list.clear();
			b_list.addAll(list);
			setB_position(0);
			LogMgr.showLog("b_list.size()---------3--------->" + b_list.size());
		}else if(houseType.equalsIgnoreCase("h")){
			h_list.clear();
			h_list.addAll(list);
			setH_position(0);
			LogMgr.showLog("h_list.size()---------3--------->" + h_list.size());
		}
	}

}
