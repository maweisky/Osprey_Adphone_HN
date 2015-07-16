package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JshWdjWifiListAdapter extends BaseAdapter{
	
	private Context context;
	private List<ScanResult> list;
	private LayoutInflater inflater;
	
	public JshWdjWifiListAdapter(Context context, List<ScanResult> list) {
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
			convertView = inflater.inflate(R.layout.os_jsh_wdj_scan_wifis_item, null);
			holder.tv_ssid = (TextView) convertView.findViewById(R.id.tv_ssid);
			holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
			holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		ScanResult result = list.get(position);
		holder.tv_ssid.setText(result.SSID);
		if(TextUtils.isEmpty(result.capabilities)){
			holder.iv_lock.setVisibility(View.INVISIBLE);
		}
		switch (result.level) {
		case 1:
			holder.iv_level.setBackgroundResource(R.drawable.os_jsh_wdj_wifi_list_item_level_1);
			break;
		case 2:
			holder.iv_level.setBackgroundResource(R.drawable.os_jsh_wdj_wifi_list_item_level_2);
			break;
		case 3:
			holder.iv_level.setBackgroundResource(R.drawable.os_jsh_wdj_wifi_list_item_level_3);
			break;
		case 4:
			holder.iv_level.setBackgroundResource(R.drawable.os_jsh_wdj_wifi_list_item_level_4);
			break;
		default:
			holder.iv_level.setBackgroundResource(R.drawable.os_jsh_wdj_wifi_list_item_level_1);
			break;
		}
		return convertView;
	}
	
	public class Holder{
		TextView tv_ssid;
		ImageView iv_lock, iv_level;
	}

}
