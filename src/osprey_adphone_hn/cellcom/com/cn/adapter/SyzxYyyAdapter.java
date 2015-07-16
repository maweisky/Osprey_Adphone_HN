package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbSyzxShakeActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxYyyList;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SyzxYyyAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	List<SyzxYyyList> infos;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	public SyzxYyyAdapter(Context context, List<SyzxYyyList> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(context);
		BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.os_img_default_icon);
	}

	public List<SyzxYyyList> getInfos() {
		return infos;
	}

	public void setInfos(List<SyzxYyyList> infos) {
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
			convertView = inflater.inflate(R.layout.os_dhb_syzx_yyy_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.iv_thumbnail = (ImageView) convertView
					.findViewById(R.id.iv_thumbnail);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_describe = (MarqueeText) convertView
					.findViewById(R.id.tv_describe);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.btn_yyy = (FButton) convertView
					.findViewById(R.id.btn_yyy);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (infos.get(position).getSmallpic() != null
				&& (infos.get(position).getSmallpic().contains(".jpg") || infos
						.get(position).getSmallpic().contains(".png"))) {
			finalBitmap.display(viewHolder.iv_thumbnail, infos.get(position)
					.getSmallpic(), loadingBitmap, loadingBitmap, false);
		}
		viewHolder.tv_name.setText(infos.get(position).getTitle());
		viewHolder.tv_describe.setText(infos.get(position).getInfo());
		viewHolder.tv_time.setText(ContextUtil.formateTime(infos.get(position).getBegintime()));
		viewHolder.btn_yyy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//旧流程，先跳转至摇一摇广告页，浏览完广告图后才可进入摇一摇界面
//				((DhbSyzxShakeListActivity) mContext).getNextAd(
//						infos.get(selectId).getGgid(), infos.get(selectId)
//						.getLargepic());
				
				//新流程，直接跳转至摇一摇界面
				Intent intent = new Intent(mContext,
						DhbSyzxShakeActivity.class);
				intent.putExtra("currentGgid", infos.get(selectId).getGgid());
				intent.putExtra("largepic", infos.get(selectId)
						.getLargepic());
				mContext.startActivity(intent);
				
//				SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				try {
//					Date date1 = simpledate.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
//					Date date2 = simpledate.parse(ContextUtil.formateTime(infos.get(selectId).getBegintime()));		
//					Date date3 = simpledate.parse(ContextUtil.formateTime(infos.get(selectId).getEndtime()));
//					if(date1.getTime()<date2.getTime()){
//						Toast.makeText(mContext, "活动时间还没到，"+ContextUtil.formateTime(infos.get(selectId).getBegintime())+"开始~", Toast.LENGTH_SHORT).show();
//					}else if(date1.getTime()>date3.getTime()){
//						Toast.makeText(mContext, "活动已结束~", Toast.LENGTH_SHORT).show();
//					}else{
//						((DhbSyzxShakeListActivity) mContext).getNextAd(
//								infos.get(selectId).getGgid(), infos.get(selectId)
//								.getLargepic());
//					}
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_time;
		private MarqueeText tv_describe;
		private FButton btn_yyy;
	}

}
