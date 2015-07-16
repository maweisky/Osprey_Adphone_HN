package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrizeNewListResult {
	@Element(required = false)
	private String imgurl;
	@Element(required = false)
	private int neednum;
	@Element(required = false)
    private String needtype;
	@ElementList(required = false, type = ZyzPrizeNewList.class)
	private List<ZyzPrizeNewList> data = new ArrayList<ZyzPrizeNewList>();

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public int getNeednum() {
		return neednum;
	}

	public void setNeednum(int neednum) {
		this.neednum = neednum;
	}

	public List<ZyzPrizeNewList> getData() {
		return data;
	}

	public void setData(List<ZyzPrizeNewList> data) {
		this.data = data;
	}

  public String getNeedtype() {
    return needtype;
  }

  public void setNeedtype(String needtype) {
    this.needtype = needtype;
  }

  
	
	
}
