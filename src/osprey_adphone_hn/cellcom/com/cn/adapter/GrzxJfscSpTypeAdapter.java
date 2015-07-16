package osprey_adphone_hn.cellcom.com.cn.adapter;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * 积分商城详情页商品类型适配器
 * 
 * @author Administrator
 * 
 */
public class GrzxJfscSpTypeAdapter extends BaseAdapter {

	private Context context;
	private String[][] infos;
	public SelectCallBack callback;

	public GrzxJfscSpTypeAdapter(Context context, String[][] str) {
		this.context = context;
		this.infos = str;
	}

	public void setSelectCallBack(SelectCallBack callback) {
		this.callback = callback;
	}

	public String[][] getInfos() {
		return infos;
	}

	public void setInfos(String[][] infos) {
		this.infos = infos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos[position][0];
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.os_dhb_grzx_jfsc_detail_type_item, null);
		}
		Button btn_level = (Button) convertView.findViewById(R.id.btn_type);
		btn_level.setText("银色拉丝");
		if (infos[position][1].equals(position + "")) {
			System.out.println("position---->" + position);
			btn_level.setSelected(true);
			btn_level.setTextColor(context.getResources().getColor(
					R.color.white));
		} else {
			System.out.println("selectId-------->" + selectId);
			btn_level.setSelected(false);
			btn_level.setTextColor(context.getResources().getColor(
					R.color.black));
		}
		btn_level.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.isSelector(selectId);
					notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}

	public interface SelectCallBack {
		public void isSelector(int position);
	}

}
