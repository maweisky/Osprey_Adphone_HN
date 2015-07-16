package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class YyyPrize {
	@Element(required = false)
	private String ifmoney;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money;

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
}
