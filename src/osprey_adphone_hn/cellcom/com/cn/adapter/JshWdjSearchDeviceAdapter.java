package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;

import p2p.cellcom.com.cn.bean.LocalDevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JshWdjSearchDeviceAdapter extends BaseAdapter{
	
	private Context context;
	private List<LocalDevice> list;
	private LayoutInflater inflater;
	
	public JshWdjSearchDeviceAdapter(Context context, List<LocalDevice> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.os_jsh_wdj_search_device_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.tv_name.setText(list.get(position).getContactId());
		return convertView;
	}
	
	public class Holder{
		TextView tv_name;
	}

}
