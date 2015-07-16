package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SyzxQhbResultNewListResult {
	@Element(required = false)
	private String state;
	@Element(required = false)
	private String robsum;
	@Element(required = false)
    private String message;
	@Element(required = false)
    private String moneytype;
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getRobsum() {
    return robsum;
  }
  public void setRobsum(String robsum) {
    this.robsum = robsum;
  }
  public String getMoneytype() {
    return moneytype;
  }
  public void setMoneytype(String moneytype) {
    this.moneytype = moneytype;
  }
}
