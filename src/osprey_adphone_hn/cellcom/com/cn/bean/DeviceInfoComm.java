package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import p2p.cellcom.com.cn.bean.Device;

public class DeviceInfoComm {

	@Element(required = false)
	private String returnCode;
	@Element(required = false)
	private String returnMessage;
	@ElementList(required = false, type = DeviceInfo.class)
	private List<DeviceInfo> body = new ArrayList<DeviceInfo>();

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

	public List<DeviceInfo> getBody() {
		return body;
	}

	public void setBody(List<DeviceInfo> body) {
		this.body = body;
	}

}
