package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class TjspInfoResult {
	
	@Element(required = false)
	private int totalnum;
	@ElementList(required = false, type = TjspInfo.class)
	private List<TjspInfo> data = new ArrayList<TjspInfo>();
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	public List<TjspInfo> getData() {
		return data;
	}
	public void setData(List<TjspInfo> data) {
		this.data = data;
	}
	

}
