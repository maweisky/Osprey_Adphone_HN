package osprey_adphone_hn.cellcom.com.cn.adapter;


import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.Area;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.AbstractWheelAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProvinceCityAreaAdapter extends AbstractWheelAdapter{
	Context context;
	List<Area> areas;
	public ProvinceCityAreaAdapter(Context context, List<Area> areas){
		this.context = context;
		this.areas = areas;
	}
	public void setList(List<Area> areas) {
		// TODO Auto-generated method stub
		this.areas=areas;
		this.notifyDataInvalidatedEvent();
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return areas.size();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view==null){
			view = LayoutInflater.from(context).inflate(R.layout.list_wheel_date_item, null);
		}
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(areas.get(index).getDisName());
		return view;
	}
}
