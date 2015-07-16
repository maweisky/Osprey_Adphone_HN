package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.adapter.SyzxKykAdapter.ViewHolder;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpList;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GrzxJfscListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	List<JfscSpList> infos;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	public GrzxJfscListAdapter(Context context, List<JfscSpList> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(context);
		this.loadingBitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.os_img_default_icon);
	}

	public List<JfscSpList> getInfos() {
		return infos;
	}

	public void setInfos(List<JfscSpList> infos) {
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
			convertView = inflater.inflate(
					R.layout.os_dhb_grzx_jfsc_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_thumbnail = (ImageView) convertView
					.findViewById(R.id.iv_thumbnail);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_describe = (MarqueeText) convertView
					.findViewById(R.id.tv_describe);
			viewHolder.tv_sp_num = (TextView) convertView
					.findViewById(R.id.tv_sp_num);
			viewHolder.tv_jf_num = (TextView) convertView
					.findViewById(R.id.tv_jf_num);
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
		if (infos.get(position).getSimpleinfo().length() > 12) {
			viewHolder.tv_describe.setText(infos.get(position).getSimpleinfo()
					.substring(0, 12)
					+ "...");
		} else {
			viewHolder.tv_describe.setText(infos.get(position).getSimpleinfo());
		}
		viewHolder.tv_sp_num.setText(infos.get(position).getLeftnum() + "件");
		viewHolder.tv_jf_num.setText("需要积分：" + infos.get(position).getJifen());
		SpannableString span = new SpannableString(
				viewHolder.tv_jf_num.getText());
		span.setSpan(
				new ForegroundColorSpan(mContext.getResources().getColor(
						R.color.orange)), 5, viewHolder.tv_jf_num.getText()
						.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		viewHolder.tv_jf_num.setText(span);
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_sp_num, tv_jf_num;
		private MarqueeText tv_describe;
	}

}
