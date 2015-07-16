package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.AddItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JshWdjAddAdapter extends BaseAdapter {

	private Context context;
	private List<AddItem> list;
	private LayoutInflater inflater;

	public JshWdjAddAdapter(Context context, List<AddItem> list) {
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
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.os_jsh_wdj_add_item, null);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		AddItem item = list.get(position);
		holder.iv_icon.setBackgroundResource(item.getDrawableID());
		holder.tv_name.setText(item.getName());
		return convertView;
	}

	public class Holder {
		ImageView iv_icon;
		TextView tv_name;
	}

}
