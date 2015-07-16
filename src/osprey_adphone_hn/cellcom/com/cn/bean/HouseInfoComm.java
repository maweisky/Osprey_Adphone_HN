package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class HouseInfoComm {
	
	@Element(required = false)
	private String returnCode;
	@Element(required = false)
	private String returnMessage;
	@ElementList(required = false, type = HouseInfo.class)
	private List<HouseInfo> body = new ArrayList<HouseInfo>();
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public List<HouseInfo> getBody() {
		return body;
	}
	public void setBody(List<HouseInfo> body) {
		this.body = body;
	}
	

}
