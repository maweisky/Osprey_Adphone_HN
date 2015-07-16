package osprey_adphone_hn.cellcom.com.cn.adapter;


import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.City;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.AbstractWheelAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProvinceCityAdapter extends AbstractWheelAdapter{
	Context context;
	List<City> cities;
	public ProvinceCityAdapter(Context context, List<City> cities){
		this.context = context;
		this.cities = cities;
	}
	public void setList(List<City> cities) {
		// TODO Auto-generated method stub
		this.cities=cities;
		this.notifyDataInvalidatedEvent();
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return cities.size();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view==null){
			view = LayoutInflater.from(context).inflate(R.layout.list_wheel_date_item, null);
		}
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(cities.get(index).getName());
		return view;
	}
}
