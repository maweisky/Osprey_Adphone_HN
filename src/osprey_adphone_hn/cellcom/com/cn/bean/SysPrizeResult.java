package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class SysPrizeResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Element(required = false)
	private String ifmoney;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money;
	@Element(required = false)
	private String cid;
	@Element(required = false)
	private String ggid;
	@ElementList(required = false, type = SysPrize.class)
	private List<SysPrize> data = new ArrayList<SysPrize>();

	public String getIfmoney() {
		return ifmoney;
	}

	public void setIfmoney(String ifmoney) {
		this.ifmoney = ifmoney;
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public List<SysPrize> getData() {
		return data;
	}

	public void setData(List<SysPrize> data) {
		this.data = data;
	}
}
