package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.GwcActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.WdddActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpList;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GwcDfspAdapter extends BaseAdapter {
	private ActivityFrame activityFrame;
	private LayoutInflater inflater;
	List<JfscSpList> infos;
	private FinalBitmap finalBitmap;
	Bitmap loadingBitmap;

	public GwcDfspAdapter(ActivityFrame activityFrame, List<JfscSpList> list) {
		this.activityFrame = activityFrame;
		this.inflater = LayoutInflater.from(activityFrame);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(activityFrame);
		this.loadingBitmap = BitmapFactory.decodeResource(
				activityFrame.getResources(), R.drawable.os_img_default_icon);
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
			convertView = inflater.inflate(R.layout.os_dfsp_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_thumbnail = (ImageView) convertView
					.findViewById(R.id.iv_thumbnail);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_jifen = (TextView) convertView
					.findViewById(R.id.tv_jifen);
			viewHolder.tv_sp_num = (TextView) convertView
					.findViewById(R.id.tv_sp_num);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			viewHolder.tv_sp_totalnum = (TextView) convertView
					.findViewById(R.id.tv_sp_totalnum);
			viewHolder.tv_sp_totaljf = (TextView) convertView
					.findViewById(R.id.tv_sp_totaljf);
			viewHolder.btn_del = (FButton) convertView
					.findViewById(R.id.btn_del);
			viewHolder.btn_pay = (FButton) convertView
					.findViewById(R.id.btn_pay);
			viewHolder.iv_add = (ImageView) convertView
					.findViewById(R.id.iv_add);
			viewHolder.iv_minus = (ImageView) convertView
					.findViewById(R.id.iv_minus);
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
		viewHolder.tv_jifen.setText(infos.get(position).getJifen());
		viewHolder.tv_sp_num.setText(infos.get(position).getNum());
		if (infos.get(position).getNum() != null
				&& !infos.get(position).getNum().trim().equals("")
				&& infos.get(position).getJifen() != null
				&& !infos.get(position).getJifen().equals("")) {
			viewHolder.tv_sp_totaljf.setText((Integer.parseInt(infos.get(
					position).getNum()) * Integer.parseInt(infos.get(position)
					.getJifen())) + "");
		}
		viewHolder.tv_sp_totalnum.setText("共" + infos.get(position).getNum()
				+ "件商品");
		viewHolder.iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (infos.get(selectId).getNum() != null
						&& !infos.get(selectId).getNum().equals("")) {
					int num = Integer.parseInt(infos.get(selectId).getNum());
					if(activityFrame instanceof GwcActivity){	
						((GwcActivity) activityFrame).updateGwcDfspNum(
								infos.get(selectId).getCid(), (num + 1) + "");
					}else if(activityFrame instanceof WdddActivity){
						((WdddActivity) activityFrame).updateGwcDfspNum(
								infos.get(selectId).getCid(), (num + 1) + "");
					}
				} else {
					if(activityFrame instanceof GwcActivity){	
						((GwcActivity) activityFrame).updateGwcDfspNum(
								infos.get(selectId).getCid(), "1");
					}else if(activityFrame instanceof WdddActivity){
						((WdddActivity) activityFrame).updateGwcDfspNum(
								infos.get(selectId).getCid(), "1");
					}
				}
			}
		});
		viewHolder.iv_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (infos.get(selectId).getNum() != null
						&& !infos.get(selectId).getNum().equals("")) {
					int num = Integer.parseInt(infos.get(selectId).getNum());
					if (num > 1) {
						((GwcActivity) activityFrame).updateGwcDfspNum(
								infos.get(selectId).getCid(), (num - 1) + "");
					} else {
						Toast.makeText(activityFrame, "商品数至少为1~", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(activityFrame, "该商品订单已过期或不存在~",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		viewHolder.btn_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(activityFrame instanceof GwcActivity){	
					((GwcActivity) activityFrame).setCid(infos.get(selectId).getCid());
					AlertDialogPopupWindow.showSheet(activityFrame,
							(GwcActivity) activityFrame, "确认删除该订单吗？", 1, "", "");
				}else if(activityFrame instanceof WdddActivity){
					((WdddActivity) activityFrame).setCid(infos.get(selectId).getCid());
					AlertDialogPopupWindow.showSheet(activityFrame,
							(WdddActivity) activityFrame, "确认删除该订单吗？", 1, "", "");
				}
				
				// ((GwcActivity)
				// mContext).delGwcDfspInfos(infos.get(selectId).getCid());
			}
		});
		viewHolder.btn_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(activityFrame instanceof GwcActivity){					
					((GwcActivity) activityFrame).getGrzl(selectId);
				}else if(activityFrame instanceof WdddActivity){
					((WdddActivity) activityFrame).getGrzl(selectId);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_jifen, tv_type, tv_sp_num;
		private RelativeLayout rl_spnum;
		private TextView tv_sp_totalnum, tv_sp_totaljf;
		private FButton btn_del, btn_pay;
		private ImageView iv_add, iv_minus;
	}
}
