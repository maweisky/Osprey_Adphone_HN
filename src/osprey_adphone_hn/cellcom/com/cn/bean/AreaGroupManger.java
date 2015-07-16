package osprey_adphone_hn.cellcom.com.cn.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 防区group
 * @author Administrator
 *
 */
public class AreaGroupManger {
	private List<AreaGroup> areaGroups=new ArrayList<AreaGroup>();
	public AreaGroupManger() {
		// TODO Auto-generated constructor stub
		AreaGroup areaGroup1=new AreaGroup();
		areaGroup1.setName("遥控");
		ArrayList<AreaChild> areaChilds=new ArrayList<AreaChild>();
		AreaChild areaChild11=new AreaChild(new ChildItem("01",false),new ChildItem("02",false),
				new ChildItem("03",false),new ChildItem("04",false),new ChildItem("05",false),
				new ChildItem("06",false),new ChildItem("07",false),new ChildItem("08",false),new ChildItem("09",false));
		areaChilds.add(areaChild11);
		areaGroup1.setAreaChilds(areaChilds);
		
		AreaGroup areaGroup2=new AreaGroup();
		areaGroup2.setName("大厅");
		ArrayList<AreaChild> areaChilds2=new ArrayList<AreaChild>();
		AreaChild areaChild21=new AreaChild(new ChildItem("01",false),new ChildItem("02",false),
				new ChildItem("03",false),new ChildItem("04",false),new ChildItem("05",false),
				new ChildItem("06",false),new ChildItem("07",false),new ChildItem("08",false),new ChildItem("09",false));
		areaChilds2.add(areaChild21);
		areaGroup2.setAreaChilds(areaChilds2);
		
		AreaGroup areaGroup3=new AreaGroup();
		areaGroup3.setName("窗户");
		ArrayList<AreaChild> areaChilds3=new ArrayList<AreaChild>();
		AreaChild areaChild31=new AreaChild(new ChildItem("01",false),new ChildItem("02",false),
				new ChildItem("03",false),new ChildItem("04",false),new ChildItem("05",false),
				new ChildItem("06",false),new ChildItem("07",false),new ChildItem("08",false),new ChildItem("09",false));
		areaChilds3.add(areaChild31);
		areaGroup3.setAreaChilds(areaChilds3);
		
		AreaGroup areaGroup4=new AreaGroup();
		areaGroup4.setName("阳台");
		ArrayList<AreaChild> areaChilds4=new ArrayList<AreaChild>();
		AreaChild areaChild41=new AreaChild(new ChildItem("01",false),new ChildItem("02",false),
				new ChildItem("03",false),new ChildItem("04",false),new ChildItem("05",false),
				new ChildItem("06",false),new ChildItem("07",false),new ChildItem("08",false),new ChildItem("09",false));
		areaChilds4.add(areaChild41);
		areaGroup4.setAreaChilds(areaChilds4);
		
		AreaGroup areaGroup5=new AreaGroup();
		areaGroup5.setName("卧室");
		ArrayList<AreaChild> areaChilds5=new ArrayList<AreaChild>();
		AreaChild areaChild51=new AreaChild(new ChildItem("01",false),new ChildItem("02",false),
				new ChildItem("03",false),new ChildItem("04",false),new ChildItem("05",false),
				new ChildItem("06",false),new ChildItem("07",false),new ChildItem("08",false),new ChildItem("09",false));
		areaChilds5.add(areaChild51);
		areaGroup5.setAreaChilds(areaChilds5);
		
		areaGroups.add(areaGroup1);
		areaGroups.add(areaGroup2);
		areaGroups.add(areaGroup3);
		areaGroups.add(areaGroup4);
		areaGroups.add(areaGroup5);
	}
	public List<AreaGroup> getAreaGroups() {
		return areaGroups;
	}
	public void setAreaGroups(List<AreaGroup> areaGroups) {
		this.areaGroups = areaGroups;
	}
}