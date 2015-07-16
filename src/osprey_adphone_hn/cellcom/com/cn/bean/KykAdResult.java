package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class KykAdResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String nextggid;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money1;
	@Element(required = false)
	private String money2;
	@ElementList(required = false, type = KykAd.class)
	private List<KykAd> data = new ArrayList<KykAd>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNextggid() {
		return nextggid;
	}

	public void setNextggid(String nextggid) {
		this.nextggid = nextggid;
	}

	public String getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}

	public String getMoney1() {
		return money1;
	}

	public void setMoney1(String money1) {
		this.money1 = money1;
	}

	public String getMoney2() {
		return money2;
	}

	public void setMoney2(String money2) {
		this.money2 = money2;
	}

	public List<KykAd> getData() {
		return data;
	}

	public void setData(List<KykAd> data) {
		this.data = data;
	}
}
