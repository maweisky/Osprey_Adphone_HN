package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.io.File;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import osprey_adphone_hn.cellcom.com.cn.R;
import p2p.cellcom.com.cn.bean.RecordVideoDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JshInfoRecordVideoListAdapter extends BaseAdapter{
	
	private Context context;
	private List<RecordVideoDB> list;
	private LayoutInflater inflater;
	private FinalBitmap fb;
	
	public JshInfoRecordVideoListAdapter(Context context, List<RecordVideoDB> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		fb = FinalBitmap.create(context);
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
			convertView = inflater.inflate(R.layout.os_jsh_info_snapshot_item, null);
			holder.rl_snapshot = (RelativeLayout) convertView.findViewById(R.id.rl_snapShot);
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		RecordVideoDB videoDB = list.get(position);
		holder.iv_play.setVisibility(View.VISIBLE);
		holder.iv_img.setImageResource(R.drawable.os_jsh_info_item_icon_1);
		holder.tv_name.setText(videoDB.getVideoName());
		return convertView;
	}
	
	public class Holder{
		RelativeLayout rl_snapshot;
		ImageView iv_img, iv_play;
		TextView tv_name;
	}

}
