package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrizeListResult {
	@Element(required = false)
	private String icon;
	@ElementList(required = false, type = ZyzPrizeList.class)
	private List<ZyzPrizeList> data = new ArrayList<ZyzPrizeList>();

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<ZyzPrizeList> getData() {
		return data;
	}

	public void setData(List<ZyzPrizeList> data) {
		this.data = data;
	}
}
