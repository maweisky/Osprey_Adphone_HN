package osprey_adphone_hn.cellcom.com.cn.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.adapter.ProvinceAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.ProvinceCityAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.ProvinceCityAreaAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.Area;
import osprey_adphone_hn.cellcom.com.cn.bean.AreaMgr;
import osprey_adphone_hn.cellcom.com.cn.bean.City;
import osprey_adphone_hn.cellcom.com.cn.bean.CityMgr;
import osprey_adphone_hn.cellcom.com.cn.bean.Cityinfo;
import osprey_adphone_hn.cellcom.com.cn.bean.Province;
import osprey_adphone_hn.cellcom.com.cn.bean.ProvinceMgr;
import osprey_adphone_hn.cellcom.com.cn.util.FileUtil;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CitySheet {
	private Button subbtn;
	private WheelView provinceWheelView;
	private WheelView cityWheelView;
	private WheelView areaWheelView;
	private ProvinceAdapter provinceAdapter;
	private ProvinceCityAdapter cityAdapter;
	private ProvinceCityAreaAdapter counyAdapter;

	private ProvinceMgr provinceMgr;
	private CityMgr cityMgr;
	private AreaMgr areaMgr;
	
	private List<City> citiesselect;
	private List<Area> areasselect;

	private Province provincebean;
	private City citybean;
	private Area areabean;

	private SheetCallBack sheetCallBack;
	private String addr;

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	public CitySheet(Context context) {
	}

	public void setSheetCallBack(SheetCallBack sheetCallBack) {
		this.sheetCallBack = sheetCallBack;
	}

	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param type
	 *            1.上传照片 2.预览下载
	 * @return
	 */
	public Dialog showSheet(Context context, TextView textView) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.os_grzl_cityselect, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		provinceWheelView = (WheelView) layout.findViewById(R.id.province_wv);
		cityWheelView = (WheelView) layout.findViewById(R.id.city_wv);
		areaWheelView = (WheelView) layout.findViewById(R.id.area_wv);
		subbtn = (Button) layout.findViewById(R.id.subbtn);
		getaddressinfo(context);
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);

		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.dismiss();
				sheetCallBack.callback(addr);
			}
		});
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	// 获取城市信息
	private void getaddressinfo(Context context) {
		// TODO Auto-generated method stub
		String province = FileUtil.readAssets(context, "province.json");
		String city = FileUtil.readAssets(context, "city.json");
		String cityarea = FileUtil.readAssets(context, "cityarea.json");
		
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		provinceMgr = (ProvinceMgr)gson.fromJson(province, ProvinceMgr.class);
		cityMgr = (CityMgr)gson.fromJson(city, CityMgr.class);
		areaMgr = (AreaMgr)gson.fromJson(cityarea, AreaMgr.class);
		
		provinceAdapter = new ProvinceAdapter(context, provinceMgr.getData());
		provinceWheelView.setViewAdapter(provinceAdapter);
		provinceWheelView.setCurrentItem(0);
		provinceWheelView.addScrollingListener(provincescrolledListener);
		provinceWheelView.setCyclic(true);
		provincebean = provinceMgr.getData().get(0);
		citiesselect=getCitys(provincebean.getProID());
		
		cityAdapter = new ProvinceCityAdapter(context, citiesselect);
		cityWheelView.setViewAdapter(cityAdapter);
		cityWheelView.addScrollingListener(cityscrolledListener);
		cityWheelView.setCyclic(true);
		if(citiesselect!=null && citiesselect.size()>0){			
			citybean = citiesselect.get(0);
			cityWheelView.setCurrentItem(0);
		}
		areasselect=getCityArea(citybean.getCityID());

		counyAdapter = new ProvinceCityAreaAdapter(context, areasselect);
		areaWheelView.setViewAdapter(counyAdapter);
		areaWheelView.addScrollingListener(counyscrolledListener);
		areaWheelView.setCyclic(true);
		if(areasselect!=null && areasselect.size()>0){	
			areabean=areasselect.get(0);
			areaWheelView.setCurrentItem(0);
		}
		updateSearchEdit();
	}

	private List<City> getCitys(String ProID){
		List<City> cities=new ArrayList<City>();
		for (int i = 0; i < cityMgr.getData().size(); i++) {
			if(ProID.equals(cityMgr.getData().get(i).getProID())){
				cities.add(cityMgr.getData().get(i));
			}
		}
		return cities;
	}
	
	private List<Area> getCityArea(String CityID){
		List<Area> areas=new ArrayList<Area>(); 
		for (int i = 0; i < areaMgr.getData().size(); i++) {
			if(CityID.equals(areaMgr.getData().get(i).getCityID())){
				areas.add(areaMgr.getData().get(i));
			}
		}
		return areas;
	}
	// Wheel scrolled listener
	OnWheelScrollListener provincescrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
//			provincebean = provinceMgr.getData().get(provinceWheelView.getCurrentItem());
//			List<City> cities=getCitys(provincebean.getProID());
//			cityAdapter.setList(cities);
		}

		public void onScrollingFinished(WheelView wheel) {
			provincebean = provinceMgr.getData().get(provinceWheelView.getCurrentItem());
			citiesselect=getCitys(provincebean.getProID());
			cityAdapter.setList(citiesselect);
			//刷新选择的城市
			if(citiesselect!=null && citiesselect.size()>0){				
				citybean = citiesselect.get(0);
				areasselect=getCityArea(citybean.getCityID());
				counyAdapter.setList(areasselect);
			}
			if(areasselect!=null && areasselect.size()>0){				
				areabean=areasselect.get(0);
			}
			updateSearchEdit();
		}
	};

	// Wheel scrolled listener
	OnWheelScrollListener cityscrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			
		}

		public void onScrollingFinished(WheelView wheel) {
			citybean = citiesselect.get(cityWheelView.getCurrentItem());
			areasselect=getCityArea(citybean.getCityID());
			counyAdapter.setList(areasselect);
			//刷新选择的区域
			if(areasselect!=null && areasselect.size()>0){		
				areabean=areasselect.get(0);
			}
			updateSearchEdit();
		}
	};
	// Wheel scrolled listener
	OnWheelScrollListener counyscrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			// updateSearchEdit();
		}

		public void onScrollingFinished(WheelView wheel) {
			areabean=areasselect.get(areaWheelView.getCurrentItem());
			updateSearchEdit();
		}
	};

	public void updateSearchEdit() {
		String provinceName = provincebean!=null?provincebean.getName():"";
		String cityName = citybean!=null?citybean.getName():"";
		String counyName = areabean!=null?areabean.getDisName():"";
		if(provinceName.equalsIgnoreCase(cityName)){
			addr=cityName+counyName;
		}else{
			if("直辖市".equals(provinceName)){
				addr=cityName+counyName;
			}else{
				addr=provinceName+cityName+counyName;
			}
		}
	}

	public interface SheetCallBack {
		public void callback(String addr);
	}

	public static class JSONParser {
		public ArrayList<String> city_list_code = new ArrayList<String>();

		public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
			List<Cityinfo> list = new ArrayList<Cityinfo>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				Cityinfo cityinfo = new Cityinfo();

				cityinfo.setCity_name(entry.getValue().getAsString());
				cityinfo.setId(entry.getKey());
				list.add(cityinfo);
			}
			return list;
		}

		public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
				String JSONString, String key) {
			HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				List<Cityinfo> list = new ArrayList<Cityinfo>();
				JsonArray array = entry.getValue().getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					Cityinfo cityinfo = new Cityinfo();
					cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
							.getAsString());
					cityinfo.setId(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					city_list_code.add(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					list.add(cityinfo);
				}
				hashMap.put(entry.getKey(), list);
			}
			return hashMap;
		}
	}
}
