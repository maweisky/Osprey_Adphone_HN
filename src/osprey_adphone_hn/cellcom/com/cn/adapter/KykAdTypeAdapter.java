package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KykAdTypeAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	List<SyzxKykType> infos;

	public KykAdTypeAdapter(Context context, List<SyzxKykType> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
	}

	public List<SyzxKykType> getInfos() {
		return infos;
	}

	public void setInfos(List<SyzxKykType> infos) {
		this.infos = infos;
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
		viewHolder.tv_name.setText(infos.get(position).getTypename());
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name;
	}

}