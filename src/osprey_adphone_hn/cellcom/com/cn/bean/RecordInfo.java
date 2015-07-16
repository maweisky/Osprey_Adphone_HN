package osprey_adphone_hn.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="RecordInfo")
public class RecordInfo {
	@Id
	private int id;
	private String account;
	private String videoName;
	private String date;
	private String path;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
