package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root
public class DhzxRateInfo {
	@Element(required = false)
	private String rate1;
	@Element(required = false)
	private String rate2;
	@Element(required = false)
	private String rate3;
	public String getRate1() {
		return rate1;
	}
	public void setRate1(String rate1) {
		this.rate1 = rate1;
	}
	public String getRate2() {
		return rate2;
	}
	public void setRate2(String rate2) {
		this.rate2 = rate2;
	}
	public String getRate3() {
		return rate3;
	}
	public void setRate3(String rate3) {
		this.rate3 = rate3;
	}
}
