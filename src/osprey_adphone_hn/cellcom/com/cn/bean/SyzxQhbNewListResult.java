package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class SyzxQhbNewListResult {
	@Element(required = false)
	private String totalNum;
	@ElementList(required = false, type = SyzxQhbNewList.class)
	private List<SyzxQhbNewList> redpacketlist = new ArrayList<SyzxQhbNewList>();

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

  public List<SyzxQhbNewList> getRedpacketlist() {
    return redpacketlist;
  }

  public void setRedpacketlist(List<SyzxQhbNewList> redpacketlist) {
    this.redpacketlist = redpacketlist;
  }
}
