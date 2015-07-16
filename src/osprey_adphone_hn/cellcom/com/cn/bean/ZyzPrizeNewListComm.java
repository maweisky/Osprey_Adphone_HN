package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ZyzPrizeNewListComm {
	@Element(required = false)
	private String returnCode;
	@Element(required = false)
	private String returnMessage;
	@Element(required = false)
	private String time;
	@Element(required = false)
	private String signInfo;
	@Element(required = false)
	private String systemNo;
	@Element(required = false)
	private String clientId;
	@Element(required = false)
	private ZyzPrizeNewListResult body = new ZyzPrizeNewListResult();

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public ZyzPrizeNewListResult getBody() {
		return body;
	}

	public void setBody(ZyzPrizeNewListResult body) {
		this.body = body;
	}
}
