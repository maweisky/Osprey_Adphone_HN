package osprey_adphone_hn.cellcom.com.cn.bean;


/**
 * 子项
 * @author Administrator
 *
 */
public class ChildItem {
	private String name;
	private boolean isselect;
	private boolean isopen;
	public ChildItem(String name,boolean isselect) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.isselect=isselect;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}

	public boolean isIsopen() {
		return isopen;
	}

	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}
}
