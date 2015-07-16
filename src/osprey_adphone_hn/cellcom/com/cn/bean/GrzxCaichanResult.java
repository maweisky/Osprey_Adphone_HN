package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class GrzxCaichanResult {
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money;
	@Element(required = false)
	private String leijimoney;
	@Element(required = false)
	private String duihuantimes;
	@Element(required = false)
	private String vip;
	@ElementList(required = false, type = GrzxCaichan.class)
	private List<GrzxCaichan> data = new ArrayList<GrzxCaichan>();

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

	public String getLeijimoney() {
		return leijimoney;
	}

	public void setLeijimoney(String leijimoney) {
		this.leijimoney = leijimoney;
	}

	public String getDuihuantimes() {
		return duihuantimes;
	}

	public void setDuihuantimes(String duihuantimes) {
		this.duihuantimes = duihuantimes;
	}

	public List<GrzxCaichan> getData() {
		return data;
	}

	public void setData(List<GrzxCaichan> data) {
		this.data = data;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}
}
