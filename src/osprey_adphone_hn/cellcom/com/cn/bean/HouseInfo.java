package osprey_adphone_hn.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class HouseInfo {
	
	@Element(required = false)
	private String id;//对应的id
	@Element(required = false)
	private String name;//对应的名称
//	@Element(required = false)
//	private String ifzxs;//是否直辖市
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public String getIfzxs() {
//		return ifzxs;
//	}
//	public void setIfzxs(String ifzxs) {
//		this.ifzxs = ifzxs;
//	}
	

}
