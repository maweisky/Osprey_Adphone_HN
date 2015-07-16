package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class SyzxKykNewListResult {
	@Element(required = false)
	private String totalNum;
	@ElementList(required = false, type = SyzxKykNewList.class)
	private List<SyzxKykNewList> data = new ArrayList<SyzxKykNewList>();

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public List<SyzxKykNewList> getData() {
		return data;
	}

	public void setData(List<SyzxKykNewList> data) {
		this.data = data;
	}
}
