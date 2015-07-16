package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 防区group
 * @author Administrator
 *
 */
public class AreaGroup {
	private String name;
	private List<AreaChild> areaChilds=new ArrayList<AreaChild>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AreaChild> getAreaChilds() {
		return areaChilds;
	}

	public void setAreaChilds(List<AreaChild> areaChilds) {
		this.areaChilds = areaChilds;
	}
	
}
