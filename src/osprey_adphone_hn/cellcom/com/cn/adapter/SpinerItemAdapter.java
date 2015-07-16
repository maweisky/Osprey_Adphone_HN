package osprey_adphone_hn.cellcom.com.cn.adapter;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinerItemAdapter extends BaseAdapter {
	private Context context;
	private String[] infos = null;
	LayoutInflater inflater;

	public SpinerItemAdapter(Context mContext, String[] list) {
		this.infos = list;
		this.context = mContext;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_spiner_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(infos[position]);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name;
	}
}