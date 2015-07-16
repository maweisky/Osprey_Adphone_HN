package osprey_adphone_hn.cellcom.com.cn.adapter;


import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.HouseInfo;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.AbstractWheelAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EditSheetAdapter extends AbstractWheelAdapter{
	Context context;
	List<HouseInfo> houseInfos;
	public EditSheetAdapter(Context context, List<HouseInfo> houseInfos){
		this.context = context;
		this.houseInfos = houseInfos;
	}
	public void setList(List<HouseInfo> houseInfos) {
		// TODO Auto-generated method stub
		this.houseInfos=houseInfos;
		this.notifyDataInvalidatedEvent();
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return houseInfos.size();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view==null){
			view = LayoutInflater.from(context).inflate(R.layout.list_wheel_date_item, null);
		}
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(houseInfos.get(index).getName());
		return view;
	}
}
