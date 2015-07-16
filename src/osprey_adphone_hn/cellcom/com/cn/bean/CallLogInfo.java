package osprey_adphone_hn.cellcom.com.cn.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Table;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

import android.text.TextUtils;

@Table(name = "CallLog_Table")
public class CallLogInfo {
	
	private int id;//服务器记录ID
	@Element(required = false)
	private String callnum;//电话号码
	@Element(required = false)
	private String callname;//用户名
	@Element(required = false)
	private int type;//1.呼出 2.呼入
	@Element(required = false)
	private String state;//是否接通 Y.接通 N.未接通
	@Element(required = false)
	private String logtime;//记录时间
	private Date logtimeDate;//记录时间;
	private String account;//用户帐号;
	private int count = 0;//重复次数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCallnum() {
		return callnum;
	}
	public void setCallnum(String callnum) {
		this.callnum = callnum;
	}
	public String getCallname() {
		if(TextUtils.isEmpty(callname)){
			return callnum;
		}
		return callname;
	}
	public void setCallname(String callname) {
		this.callname = callname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			logtimeDate = sdf.parse(logtime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logtime = logtime;
	}	
	public Date getLogtimeDate() {
		return logtimeDate;
	}
	public void setLogtimeDate(Date logtimeDate) {
		this.logtimeDate = logtimeDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	

}
