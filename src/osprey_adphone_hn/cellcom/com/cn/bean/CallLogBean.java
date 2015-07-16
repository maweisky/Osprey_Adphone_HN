package osprey_adphone_hn.cellcom.com.cn.bean;

/**
 * ͨ
 * @author Administrator
 * 
 */
public class CallLogBean {

	private int id;
	private String account;
	private String callname; //
	private String callnum; //
	private String date; //
	private int type; //
	private int count; //
	private String state;
	private String logtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCallname() {
		return callname;
	}

	public void setCallname(String callname) {
		this.callname = callname;
	}

	public String getCallnum() {
		return callnum;
	}

	public void setCallnum(String callnum) {
		this.callnum = callnum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
