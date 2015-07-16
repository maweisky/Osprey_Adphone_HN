package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxHfkAdapter.ViewHolder;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCaichan;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GrzxJfkAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	List<GrzxCaichan> infos;

	public GrzxJfkAdapter(Context context, List<GrzxCaichan> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int selectId = position;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_dhb_grzx_hfk_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_prize = (TextView) convertView
					.findViewById(R.id.tv_prize);
			viewHolder.tv_describe = (TextView) convertView
					.findViewById(R.id.tv_describe);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (infos.get(position).getLogtime() != null
				&& infos.get(position).getLogtime().contains(".")) {
			viewHolder.tv_time.setText(ContextUtil.formateTime(infos.get(
					position).getLogtime()));
		} else {
			viewHolder.tv_time.setText(infos.get(position).getLogtime());
		}
		viewHolder.tv_describe.setText(infos.get(position).getInfo());
		return convertView;
	}

	static class ViewHolder {
		private TextView tv_prize, tv_describe, tv_time;
	}

}
