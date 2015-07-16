package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.JshWgInfo;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JshWgInfoAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	List<JshWgInfo> infos;
	public JshWgInfoAdapter(Context context, List<JshWgInfo> list){
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
		if(convertView==null){
			convertView = inflater.inflate(R.layout.os_jsh_wdj_wg_info_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_info_flag = (ImageView) convertView.findViewById(R.id.iv_info_flag);
			viewHolder.iv_line = (ImageView) convertView.findViewById(R.id.iv_line);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(infos.get(position).getType()!=null&&infos.get(position).getType().trim().equals("1")){
			viewHolder.iv_info_flag.setBackgroundResource(R.drawable.os_jsh_wg_info_icon);
			viewHolder.tv_name.setText("物管信息");
			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.qunqing));
			viewHolder.iv_line.setBackgroundResource(R.drawable.os_list_wg_info_flag);
		}else{
			viewHolder.iv_info_flag.setBackgroundResource(R.drawable.os_jsh_private_info_icon);
			viewHolder.tv_name.setText("私信");
			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.orange));
			viewHolder.iv_line.setBackgroundResource(R.drawable.os_list_private_info_flag);
		}
		viewHolder.tv_time.setText(ContextUtil.formateTime(infos.get(position).getLogtime()));
		viewHolder.tv_content.setText(infos.get(position).getInfo());
		return convertView;
	}
	
	static class ViewHolder{
		private TextView tv_name;
		private ImageView iv_info_flag,iv_line;
		private TextView tv_time,tv_content;
	}

}
