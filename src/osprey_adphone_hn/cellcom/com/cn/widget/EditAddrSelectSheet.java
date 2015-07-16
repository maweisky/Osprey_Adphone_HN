package osprey_adphone_hn.cellcom.com.cn.widget;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.EditSheetAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.CityInfoBean;
import osprey_adphone_hn.cellcom.com.cn.bean.EditAddrMgr;
import osprey_adphone_hn.cellcom.com.cn.bean.HouseInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.HouseInfoComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.OnWheelScrollListener;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.WheelView;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import cellcom.com.cn.net.CellComAjaxHttp.HttpWayMode;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.CellComAjaxResult.ParseType;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

//省市区的选择器
public class EditAddrSelectSheet {
	private Context context;
	private Button subbtn;
	private WheelView fristWheelView;
	private WheelView secondWheelView;
	private WheelView thirdWheelView;
	private LinearLayout other_ll;
	private EditSheetAdapter fristAdapter;
	private EditSheetAdapter secondAdapter;
	private EditSheetAdapter thirdAdapter;
	
	private EditAddrMgr addrMgr;

	private SheetCallBack sheetCallBack;
	private String addr;
	private int type = 0;//0是选择省市区， 1是选择小区， 2是选择房号
	private int f_position = 0, s_position = 0, t_position = 0;
	private FinalDb finalDb;
	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	public EditAddrSelectSheet(Context context, EditAddrMgr addrMgr,FinalDb finalDb) {
		this.context = context;
		this.addrMgr = addrMgr;
		this.finalDb =finalDb;
	}

	public void setSheetCallBack(SheetCallBack sheetCallBack) {
		this.sheetCallBack = sheetCallBack;
	}
	
	public interface SheetCallBack {
		public void callback(int type, String addr);
	}

	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param type
	 * @return
	 */
	public Dialog showSheet(final int type) {
		this.type = type;
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.os_jsh_wdj_edit_select, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		fristWheelView = (WheelView) layout.findViewById(R.id.frist_wv);
		secondWheelView = (WheelView) layout.findViewById(R.id.second_wv);
		thirdWheelView = (WheelView) layout.findViewById(R.id.third_wv);
		other_ll = (LinearLayout) layout.findViewById(R.id.other_ll);
		subbtn = (Button) layout.findViewById(R.id.subbtn);
		initAdapter();
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
//		dlg.setCanceledOnTouchOutside(true);

		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.dismiss();
				getAddr();
				sheetCallBack.callback(type, addr);
			}
		});
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	// 获取城市信息
	private void initAdapter() {
		// TODO Auto-generated method stub		
		if(type == 0){
			initFristAdapter(addrMgr.getP_list());
			f_position = addrMgr.getP_position();
			fristWheelView.setCurrentItem(f_position);

			initSecondAdapter(addrMgr.getC_list());
			if(addrMgr.getC_list().size() <= 0){
				secondWheelView.setVisibility(View.INVISIBLE);
			}else{
				s_position = addrMgr.getC_position();
				secondWheelView.setCurrentItem(s_position);
				if(addrMgr.getP_list().get(addrMgr.getP_position()).getName().equals(addrMgr.getC_list().get(addrMgr.getC_position()).getName())){
					secondWheelView.setVisibility(View.GONE);
					other_ll.setVisibility(View.INVISIBLE);
				}else{
					secondWheelView.setCurrentItem(addrMgr.getC_position());
				}
			}

			initThirdAdapter(addrMgr.getA_list());
			if(addrMgr.getA_list().size() <= 0){
				thirdWheelView.setVisibility(View.INVISIBLE);
			}else{
				t_position = addrMgr.getA_position();
				thirdWheelView.setCurrentItem(t_position);
			}

//			getHouseInfos("c", addrMgr.getP_list().get(addrMgr.getP_position()).getId());
		}else if(type == 1){
			initFristAdapter(addrMgr.getG_list());
			f_position = addrMgr.getG_position();
			fristWheelView.setCurrentItem(f_position);
			
			secondWheelView.setVisibility(View.GONE);
			thirdWheelView.setVisibility(View.GONE);
			if(addrMgr.getG_list().size()>addrMgr.getG_position()){				
				getHouseInfos("b", addrMgr.getG_list().get(addrMgr.getG_position()).getId());
			}
		}else if(type == 2){
			initFristAdapter(addrMgr.getB_list());
			f_position = addrMgr.getB_position();
			fristWheelView.setCurrentItem(f_position);
			

			initSecondAdapter(addrMgr.getH_list());
			if(addrMgr.getH_list().size() <= 0){
				secondWheelView.setVisibility(View.INVISIBLE);
			}else{
				s_position = addrMgr.getH_position();
				secondWheelView.setCurrentItem(s_position);
			}
			thirdWheelView.setVisibility(View.GONE);
			if(addrMgr.getB_list().size()>addrMgr.getB_position()){				
				getHouseInfos("h", addrMgr.getB_list().get(addrMgr.getB_position()).getId());
			}
		}
	}
	
	
	
	private void initFristAdapter(List<HouseInfo> list) {
		// TODO Auto-generated method stub
		fristAdapter = new EditSheetAdapter(context, list);
		fristWheelView.setViewAdapter(fristAdapter);
		fristWheelView.addScrollingListener(fristscrolledListener);
		fristWheelView.setCyclic(true);
	}
	
	private void initSecondAdapter(List<HouseInfo> list){
		if(list.size()>0){			
			secondAdapter = new EditSheetAdapter(context, list);
			secondWheelView.setViewAdapter(secondAdapter);
			secondWheelView.addScrollingListener(secondscrolledListener);
			secondWheelView.setCyclic(true);
		}
	}
	
	private void initThirdAdapter(List<HouseInfo> list){
//		List<HouseInfo> lists=new ArrayList<HouseInfo>();
//		HouseInfo houseInfo=new HouseInfo();
//		houseInfo.setId("1");
//		houseInfo.setName("sa");
//		lists.add(houseInfo);
//		thirdAdapter = new EditSheetAdapter(context, list);
		if(list.size()>0){			
			thirdAdapter = new EditSheetAdapter(context, list);
			thirdWheelView.setViewAdapter(thirdAdapter);
			thirdWheelView.addScrollingListener(thirdscrolledListener);
			thirdWheelView.setCyclic(true);
		}
	}

	OnWheelScrollListener fristscrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
		}

		public void onScrollingFinished(WheelView wheel) {
			if (fristWheelView.getCurrentItem() != f_position) {
				f_position = fristWheelView.getCurrentItem();
				s_position=0;
				t_position=0;
				if (type == 0) {
					secondWheelView.setVisibility(View.INVISIBLE);
					thirdWheelView.setVisibility(View.INVISIBLE);
					other_ll.setVisibility(View.GONE);
					List<CityInfoBean> cityInfoBeans=finalDb.findAllByWhere(CityInfoBean.class," pid='"+addrMgr.getP_list().get(f_position).getId()+"'");
					List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
					List<String> list2=new ArrayList<String>();
					for (int i = 0; i < cityInfoBeans.size(); i++) {
						HouseInfo houseInfo=new HouseInfo();
						houseInfo.setId(cityInfoBeans.get(i).getCid());
						houseInfo.setName(cityInfoBeans.get(i).getCityname());
						if(!list2.contains(cityInfoBeans.get(i).getCityname())){					
							list2.add(cityInfoBeans.get(i).getCityname());
							houseInfos.add(houseInfo);
						}
					}
					if(houseInfos.size() > 0){
						addrMgr.addList("c", houseInfos);
//						setHouseInfo("c", houseInfos);
						changeWheel("c");
					}else{
						addrMgr.getC_list().clear();
						addrMgr.getA_list().clear();
						addrMgr.getG_list().clear();
						addrMgr.getB_list().clear();
						addrMgr.getH_list().clear();
					}
//					getHouseInfos("c", addrMgr.getP_list().get(f_position).getId());
				} else if (type == 1) {
					if(addrMgr.getG_list().size()>f_position){						
						getHouseInfos("b", addrMgr.getG_list().get(f_position).getId());
					}else{
						addrMgr.getB_list().clear();
						addrMgr.getH_list().clear();
					}
				} else if (type == 2) {
					secondWheelView.setVisibility(View.INVISIBLE);
					if(addrMgr.getB_list().size()>f_position){						
						getHouseInfos("h", addrMgr.getB_list().get(f_position).getId());
					}else{
						addrMgr.getH_list().clear();
					}
				}
			}
		}
	};

	// Wheel scrolled listener
	OnWheelScrollListener secondscrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			
		}

		public void onScrollingFinished(WheelView wheel) {
			if(s_position != secondWheelView.getCurrentItem()){
				s_position = secondWheelView.getCurrentItem();
				t_position=0;
				if(type == 0){
						thirdWheelView.setVisibility(View.INVISIBLE);
						List<CityInfoBean> cityInfoBeans=finalDb.findAllByWhere(CityInfoBean.class," cid='"+addrMgr.getC_list().get(s_position).getId()+"'");
						List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
						for (int i = 0; i < cityInfoBeans.size(); i++) {
							if(!"null".equals(cityInfoBeans.get(i).getAid())){									
								HouseInfo houseInfo=new HouseInfo();
								houseInfo.setId(cityInfoBeans.get(i).getAid());
								houseInfo.setName(cityInfoBeans.get(i).getAname());
								houseInfos.add(houseInfo);
							}
						}
						if(houseInfos.size() > 0){
							addrMgr.addList("a", houseInfos);
							changeWheel("a");
						}else{
							addrMgr.getA_list().clear();
							addrMgr.getG_list().clear();
							addrMgr.getB_list().clear();
							addrMgr.getH_list().clear();
						}
//						getHouseInfos("a", addrMgr.getC_list().get(s_position).getId());
				}
			}
		}
	};
	// Wheel scrolled listener
	OnWheelScrollListener thirdscrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			
		}

		public void onScrollingFinished(WheelView wheel) {
			t_position = thirdWheelView.getCurrentItem();
			if(type==0){
				if(addrMgr.getA_list().size()>t_position){				
					getHouseInfos("g", addrMgr.getA_list().get(t_position).getId());
				}
			}
		}
	};

	public void getAddr() {		
		if(type == 0){
			addrMgr.setP_position(f_position);
			addrMgr.setC_position(s_position);
			addrMgr.setA_position(t_position);
			if(addrMgr.getP_list().size()>addrMgr.getP_position()){				
				HouseInfo houseInfo = addrMgr.getP_list().get(addrMgr.getP_position());
				if(addrMgr.getP_list().get(addrMgr.getP_position()).getName().equals(addrMgr.getC_list().get(addrMgr.getC_position()).getName())){
					if(addrMgr.getA_list().size()>addrMgr.getA_position()){					
						addr = houseInfo.getName() + addrMgr.getA_list().get(addrMgr.getA_position()).getName();
					}else{
						addr = houseInfo.getName() ;
					}
				}else{
					if(addrMgr.getC_list().size()>addrMgr.getC_position()){	
						if(addrMgr.getA_list().size()>addrMgr.getA_position()){
							addr = houseInfo.getName() + addrMgr.getC_list().get(addrMgr.getC_position()).getName()
									+ addrMgr.getA_list().get(addrMgr.getA_position()).getName();
						}else{
							addr = houseInfo.getName() + addrMgr.getC_list().get(addrMgr.getC_position()).getName();
						}
					}else{
						addr = houseInfo.getName();
					}
				}
			}else{
				addr="";
			}
		}else if(type == 1){
			addrMgr.setG_position(f_position);
			if(addrMgr.getG_list().size()>addrMgr.getG_position()){				
				addr = addrMgr.getG_list().get(addrMgr.getG_position()).getName();
			}else{
				addr="";
			}
		}else if(type == 2){
			addrMgr.setB_position(f_position);
			addrMgr.setH_position(s_position);
			if(addrMgr.getB_list().size()>addrMgr.getB_position()){				
				if(addrMgr.getH_list().size()>addrMgr.getH_position()){
					addr = addrMgr.getB_list().get(addrMgr.getB_position()).getName() + addrMgr.getH_list().get(addrMgr.getH_position()).getName();
				}else{
					addr = addrMgr.getB_list().get(addrMgr.getB_position()).getName();
				}
			}else{
				addr="";
			}
		}
	}
	
	private void getHouseInfos(final String houseType, String typeid) {
		// TODO Auto-generated method stub
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("type", houseType);
		params.put("typeid", typeid);
		HttpHelper.getInstances(context).send(FlowConsts.YYW_GETHOUSE, params, HttpWayMode.POST, new NetCallBack<CellComAjaxResult>() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onSuccess(CellComAjaxResult t) {
				// TODO Auto-generated method stub
				try {
					HouseInfoComm comm = t.read(HouseInfoComm.class, ParseType.GSON);
					if(!comm.getReturnCode().equals("1")){
						((ActivityFrame)context).ShowMsg(comm.getReturnMessage());
						return;
					}
					if(comm.getBody().size() > 0){
						LogMgr.showLog("comm.getBody().size()---------2--------->" + comm.getBody().size());
						if(houseType.equals("b")){
							f_position=0;
						}
						if(houseType.equals("h")){
							s_position=0;
						}
						addrMgr.addList(houseType, comm.getBody());
						changeWheel(houseType);
					}else{
						if(houseType.equals("g")){
							addrMgr.getG_list().clear();
							addrMgr.getB_list().clear();
							addrMgr.getH_list().clear();
						}
						if(houseType.equals("b")){
							addrMgr.getB_list().clear();
							addrMgr.getH_list().clear();
						}
						if(houseType.equals("h")){
							addrMgr.getH_list().clear();
						}
//						((ActivityFrame)context).ShowMsg("暂无相关信息！");
					}
				} catch (Exception e) {
					// TODO: handle exception
					LogMgr.showLog("error--------------->" + e.toString());
					((ActivityFrame)context).ShowMsg("获取数据失败，请重新获取！");
				}
			}
			
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
			}
		});
	}
	
	private void changeWheel(String houseType) {
		// TODO Auto-generated method stub
		if(houseType.equalsIgnoreCase("c")){
			if(addrMgr.getP_list().get(f_position).getName().equals(addrMgr.getC_list().get(0).getName())){
				LogMgr.showLog("addrMgr.getC_list().size()---------6--------->" + addrMgr.getC_list().size());
				secondWheelView.setVisibility(View.GONE);
				other_ll.setVisibility(View.INVISIBLE);				
			}else{
				LogMgr.showLog("addrMgr.getC_list().size()---------4--------->" + addrMgr.getC_list().size());
				secondAdapter.setList(addrMgr.getC_list());
				secondWheelView.setCurrentItem(s_position);
				if(addrMgr.getC_list().size()>0){
					secondWheelView.setVisibility(View.VISIBLE);					
				}else{
					secondWheelView.setVisibility(View.INVISIBLE);
				}
			}
			if(addrMgr.getC_list().size()>s_position){
				List<CityInfoBean> cityInfoBeans=finalDb.findAllByWhere(CityInfoBean.class," cid='"+addrMgr.getC_list().get(s_position).getId()+"'");
				List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
				for (int i = 0; i < cityInfoBeans.size(); i++) {
					if(!"null".equals(cityInfoBeans.get(i).getAid())){						
						HouseInfo houseInfo=new HouseInfo();
						houseInfo.setId(cityInfoBeans.get(i).getAid());
						houseInfo.setName(cityInfoBeans.get(i).getAname());
						houseInfos.add(houseInfo);
					}
				}
				if(houseInfos.size() > 0){
					addrMgr.addList("a", houseInfos);
					changeWheel("a");
//					setHouseInfo("a", houseInfos);
				}else{
					addrMgr.getA_list().clear();
					addrMgr.getG_list().clear();
					addrMgr.getB_list().clear();
					addrMgr.getH_list().clear();
				}
			}
//			getHouseInfos("a", addrMgr.getC_list().get(s_position).getId());
		}else if(houseType.equalsIgnoreCase("a")){
			LogMgr.showLog("addrMgr.getA_list().size()---------5--------->" + addrMgr.getA_list().size());
//			List<HouseInfo> list=new ArrayList<HouseInfo>();
//			for (int i = 0; i < addrMgr.getA_list().size(); i++) {
//				HouseInfo houseInfo=new HouseInfo();
//				houseInfo.setId(addrMgr.getA_list().get(i).getId());
//				houseInfo.setName(addrMgr.getA_list().get(i).getName());
//				list.add(houseInfo);
//			}
			thirdAdapter.setList(addrMgr.getA_list()/*list*/);
			thirdWheelView.setCurrentItem(t_position);
			if(addrMgr.getA_list().size() <= 0){
				thirdWheelView.setVisibility(View.INVISIBLE);
			}else{				
				thirdWheelView.setVisibility(View.VISIBLE);
			}
			if(addrMgr.getA_list().size()>t_position){				
				getHouseInfos("g", addrMgr.getA_list().get(t_position).getId());
			}else{
				addrMgr.getG_list().clear();
				addrMgr.getB_list().clear();
				addrMgr.getH_list().clear();
			}
		}else if(houseType.equalsIgnoreCase("b")){
			if(addrMgr.getB_list().size()>f_position){				
				getHouseInfos("h", addrMgr.getB_list().get(f_position).getId());
			}else{
				addrMgr.getH_list().clear();
			}
		} else if(houseType.equalsIgnoreCase("h")){
			LogMgr.showLog("addrMgr.getH_list().size()---------7--------->" + addrMgr.getH_list().size());
			secondAdapter.setList(addrMgr.getH_list());
			secondWheelView.setCurrentItem(s_position);
			if(type==2){				
				secondWheelView.setVisibility(View.VISIBLE);
			}
		}
	}
}
