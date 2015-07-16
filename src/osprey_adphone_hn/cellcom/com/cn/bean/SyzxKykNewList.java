package osprey_adphone_hn.cellcom.com.cn.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import org.simpleframework.xml.Element;
@Table(name="Kykinfo_Table2") //看一看数据表
public class SyzxKykNewList implements Serializable {
	@Id
	private String id;
	@Element(required = false)
	private String ggid;
	@Element(required = false)
	private String smallpic;
	@Element(required = false)
	private String largepic;
	@Element(required = false)
	private String title;
	@Element(required = false)
	private String info;
	@Element(required = false)
	private String secs;
	@Element(required = false)
	private String moneytype;
	@Element(required = false)
	private String money1;
	@Element(required = false)
	private String money2;
	@Element(required = false)
	private String logtime;
//	@Element(required = false)
//	private String isread;//Y已读 N未读
	@Element(required = false)
	private String ifnew;//Y已读 N未读

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getSmallpic() {
		return smallpic;
	}

	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

	public String getLargepic() {
		return largepic;
	}

	public void setLargepic(String largepic) {
		this.largepic = largepic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSecs() {
		return secs;
	}

	public void setSecs(String secs) {
		this.secs = secs;
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

	public String getIfnew() {
		return ifnew;
	}

	public void setIfnew(String ifnew) {
		this.ifnew = ifnew;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
//
//	public String getIsread() {
//		return isread;
//	}
//
//	public void setIsread(String isread) {
//		this.isread = isread;
//	}
}
