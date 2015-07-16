package cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class DESBeanBase {
	@Element(required = false)
	private String state;
	@Element(required = false)
	private String errorcode;
	@Element(required = false)
	private String msg;
	@ElementList(required = false , type = DESBean.class)
	private List<DESBean> parambuf=new ArrayList<DESBean>();
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<DESBean> getParambuf() {
		return parambuf;
	}

	public void setParambuf(List<DESBean> parambuf) {
		this.parambuf = parambuf;
	}

}
