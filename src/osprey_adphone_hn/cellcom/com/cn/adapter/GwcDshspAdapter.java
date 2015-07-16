package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.adapter.GwcDfhspAdapter.ViewHolder;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpList;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GwcDshspAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	List<JfscSpList> infos;
	private FinalBitmap finalBitmap;
	Bitmap loadingBitmap;

	public GwcDshspAdapter(Context context, List<JfscSpList> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.infos = list;
		this.finalBitmap = FinalBitmap.create(context);
		this.loadingBitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.os_img_default_icon);
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
			convertView = inflater.inflate(R.layout.os_dshsp_item, null);
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
			viewHolder.btn_qrfh = (FButton) convertView
					.findViewById(R.id.btn_qrfh);
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
		viewHolder.tv_sp_num.setText("x " + infos.get(position).getNum());
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
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_thumbnail;
		private TextView tv_name, tv_jifen, tv_type, tv_sp_num;
		private TextView tv_sp_totalnum, tv_sp_totaljf;
		private FButton btn_qrfh;
	}
}