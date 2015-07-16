package osprey_adphone_hn.cellcom.com.cn.adapter;


import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.Province;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.AbstractWheelAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProvinceAdapter extends AbstractWheelAdapter{
	Context context;
	List<Province> provinces;
	public ProvinceAdapter(Context context, List<Province> provinces){
		this.context = context;
		this.provinces = provinces;
	}
	public void setList(List<Province> provinces) {
		// TODO Auto-generated method stub
		this.provinces=provinces;
		this.notifyDataInvalidatedEvent();
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return provinces.size();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view==null){
			view = LayoutInflater.from(context).inflate(R.layout.list_wheel_date_item, null);
		}
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(provinces.get(index).getName());
		return view;
	}
}
