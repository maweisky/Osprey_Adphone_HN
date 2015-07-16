package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykNewList;
import osprey_adphone_hn.cellcom.com.cn.db.DBHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SyzxKykAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	List<SyzxKykNewList> infos;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;
	private FinalDb finalDb;

	public SyzxKykAdapter(Context context, List<SyzxKykNewList> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(context);
		this.finalDb = DBHelper.getIntance(context);
		BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.os_img_default_icon);
	}

	public List<SyzxKykNewList> getInfos() {
		return infos;
	}

	public void setInfos(List<SyzxKykNewList> infos) {
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
		final int selectId = position;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_dhb_syzx_kyk_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_thumbnail = (ImageView) convertView
					.findViewById(R.id.iv_thumbnail);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_describe = (MarqueeText) convertView
					.findViewById(R.id.tv_describe);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		List<SyzxKykNewList> syzxKykList = finalDb.findAllByWhere(SyzxKykNewList.class, "ggid = '"+infos.get(position)
//				.getGgid()+"'");
//		if(syzxKykList.size()==1){
//			if(syzxKykList.get(0).getIfnew()!=null&&syzxKykList.get(0).getIfnew().equals("Y")){
//				viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.gray));
//			}else{
//				viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.black));
//			}
//		}
		if(infos.get(position).getIfnew()!=null&&infos.get(position).getIfnew().equals("Y")){
			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.gray));
		}else{
			viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		if (infos.get(position).getSmallpic() != null
				&& (infos.get(position).getSmallpic().contains(".jpg") || infos
						.get(position).getSmallpic().contains(".png"))) {
			finalBitmap.display(viewHolder.iv_thumbnail, infos.get(position)
					.getSmallpic(), loadingBitmap, loadingBitmap, false);
		}
		viewHolder.tv_name.setText(infos.get(position).getTitle());
		viewHolder.tv_describe.setText(infos.get(position).getInfo());
		if (infos.get(position).getLogtime() != null
				&& infos.get(position).getLogtime().contains(":")) {
			viewHolder.tv_time.setText(infos
					.get(position)
					.getLogtime()
					.substring(0,
							infos.get(position).getLogtime().lastIndexOf(":")));
		} else {
			viewHolder.tv_time.setText(infos.get(position).getLogtime());
		}
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_time;
		private MarqueeText tv_describe;
	}

}
