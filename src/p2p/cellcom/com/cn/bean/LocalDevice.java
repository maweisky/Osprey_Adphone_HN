package p2p.cellcom.com.cn.bean;

import java.net.InetAddress;

public class LocalDevice {
	public String contactId;
	public int flag;
	public int type;
	public InetAddress address;

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		LocalDevice obj = (LocalDevice) o;
		if (obj.contactId.equals(this.contactId)) {
			return true;
		} else {
			return false;
		}
	}

}
