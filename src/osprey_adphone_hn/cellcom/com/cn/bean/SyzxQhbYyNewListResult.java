package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class SyzxQhbYyNewListResult {
	@Element(required = false)
	private String state;
	@Element(required = false)
	private String message;
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
	
}
