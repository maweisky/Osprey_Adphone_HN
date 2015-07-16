package osprey_adphone_hn.cellcom.com.cn.adapter;


import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxQhbNewList;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SyzxQhbAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	List<SyzxQhbNewList> infos;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	public SyzxQhbAdapter(Context context, List<SyzxQhbNewList> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(context);
		handler.sendEmptyMessage(1);
	}

	public List<SyzxQhbNewList> getInfos() {
		return infos;
	}

	public void setInfos(List<SyzxQhbNewList> infos) {
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
			convertView = inflater.inflate(R.layout.os_dhb_syzx_qhb_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_thumbnail = (ImageView) convertView
					.findViewById(R.id.iv_thumbnail);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_describe = (MarqueeText) convertView
					.findViewById(R.id.tv_describe);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.qhbtv=(TextView)convertView.findViewById(R.id.qhbtv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(infos.get(position).getIfread()!=null&&infos.get(position).getIfread().equals("Y")){
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
		viewHolder.tv_time.setText(ContextUtil.getQhbTime(infos.get(position).getBegintime())+"红包准时抢");
		viewHolder.tv_name.setText(infos.get(position).getTitle());
		if("2".equalsIgnoreCase(infos.get(position).getOrdertype())){
		  //未预约
		  viewHolder.qhbtv.setVisibility(View.GONE);
		  viewHolder.tv_describe.setTextColor(Color.parseColor("#4aaaf0"));
		  viewHolder.tv_describe.setText("参与报名获取抢红包的资格");
		  if("3".equalsIgnoreCase(infos.get(position).getState())){
            //已结束
            if("Y".equalsIgnoreCase(infos.get(position).getIfwin())){
              //是否抢到红包
              viewHolder.tv_describe.setText("您获得红包:"+infos.get(position).getRobsum()+"银元");
            }else{
              viewHolder.tv_describe.setText("您未抢到任何红包");
            }
            viewHolder.tv_time.setVisibility(View.GONE);
            viewHolder.qhbtv.setVisibility(View.VISIBLE);
            viewHolder.qhbtv.setText("已结束");
            viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb_yjs);
          }else{
            viewHolder.qhbtv.setText("抢红包");
            viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb);
            viewHolder.tv_time.setVisibility(View.VISIBLE);
          }
		}else{
		  viewHolder.qhbtv.setVisibility(View.VISIBLE);
		  //已预约
		  if("1".equalsIgnoreCase(infos.get(position).getState())){
		    //未开始
		    viewHolder.tv_describe.setTextColor(Color.parseColor("#FF672E"));
		    viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb);
		    viewHolder.qhbtv.setText("抢红包");
		    viewHolder.tv_describe.setText("倒计时:"+infos.get(position).getTime(1));
		    viewHolder.tv_time.setVisibility(View.VISIBLE);
		  }else if("2".equalsIgnoreCase(infos.get(position).getState())){
		    //正在进行中
		    viewHolder.tv_describe.setTextColor(Color.parseColor("#FF672E"));
		    viewHolder.qhbtv.setText("抢红包");
		    if("Y".equalsIgnoreCase(infos.get(position).getIfwin())){
              //是否抢到红包
		      viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb_yjs);
              viewHolder.tv_describe.setText("您获得红包:"+infos.get(position).getRobsum()+"银元");
              viewHolder.tv_time.setVisibility(View.GONE);
            }else{
              viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb);
              viewHolder.tv_describe.setText("倒计时:"+infos.get(position).getTime(2));
              viewHolder.tv_time.setText("正在进行");
              viewHolder.tv_time.setVisibility(View.VISIBLE);
//              viewHolder.tv_describe.setText("正在进行");
            }
		    
          }else if("3".equalsIgnoreCase(infos.get(position).getState())){
            //已结束
            viewHolder.tv_describe.setTextColor(Color.parseColor("#8E8E8E"));
            if("Y".equalsIgnoreCase(infos.get(position).getIfwin())){
              //是否抢到红包
              viewHolder.tv_describe.setText("您获得红包:"+infos.get(position).getRobsum()+"银元");
            }else{
              viewHolder.tv_describe.setText("您未抢到任何红包");
            }
            viewHolder.qhbtv.setBackgroundResource(R.drawable.os_dhb_syzx_qhb_yjs);
            viewHolder.qhbtv.setText("已结束");
            viewHolder.tv_time.setVisibility(View.GONE);
          } 
		}
		return convertView;
	}
	
	private Handler handler = new Handler(){
      public void handleMessage(android.os.Message msg) {
          switch (msg.what) {
          case 1:
              //②：for循环执行的时间
              SyzxQhbAdapter.this.notifyDataSetChanged();
              handler.sendEmptyMessageDelayed(1, 1000);
              break;
          }
      }
  };
  
	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_time;
		private MarqueeText tv_describe;
		private TextView qhbtv;
	}

}
