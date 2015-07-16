package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrizeList {
	@Element(required = false)
	private String jpid;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money;
	@Element(required = false)
	private String idx;

	public String getJpid() {
		return jpid;
	}

	public void setJpid(String jpid) {
		this.jpid = jpid;
	}

	public String getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}
}
