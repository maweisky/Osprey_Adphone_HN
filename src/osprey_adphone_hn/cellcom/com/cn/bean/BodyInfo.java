package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

public class BodyInfo {
	@ElementList(required = false, type = ProvinceInfoBean.class)
	private List<ProvinceInfoBean> arealist = new ArrayList<ProvinceInfoBean>();

	public List<ProvinceInfoBean> getArealist() {
		return arealist;
	}

	public void setArealist(List<ProvinceInfoBean> arealist) {
		this.arealist = arealist;
	}
	
}
