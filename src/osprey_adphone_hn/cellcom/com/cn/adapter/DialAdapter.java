package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbBhpActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.CallLogBean;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialAdapter extends BaseAdapter {

	private DhbBhpActivity dhbBhpActivity;
	private List<CallLogBean> callLogs;
	private LayoutInflater inflater;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	public DialAdapter(DhbFragmentActivity dhbFragmentActivity,DhbBhpActivity dhbBhpActivity,FinalBitmap finalBitmap, List<CallLogBean> callLogs) {
		this.dhbBhpActivity=dhbBhpActivity;
		this.callLogs = callLogs;
		this.finalBitmap = finalBitmap;
		this.inflater = LayoutInflater.from(dhbFragmentActivity);
		loadingBitmap = BitmapFactory.decodeResource(dhbFragmentActivity.getResources(),
				R.drawable.os_dhb_itempic);
	}

	@Override
	public int getCount() {
		return callLogs.size();
	}

	@Override
	public Object getItem(int position) {
		return callLogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.os_dhb_contactrecorditem,
					null);
			holder = new ViewHolder();
			holder.call_type = (ImageView) convertView
					.findViewById(R.id.call_type);
			holder.call_img = (ImageView) convertView
					.findViewById(R.id.call_img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.call_btn = (ImageView) convertView
					.findViewById(R.id.call_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CallLogBean callLog = callLogs.get(position);
		switch (callLog.getType()) {
		case 1:
			holder.call_type.setBackgroundResource(R.drawable.os_dhb_calling);
			break;
		case 2:
			holder.call_type	.setBackgroundResource(R.drawable.os_dhb_callingout);
			break;
		case 3:
			holder.call_type.setBackgroundResource(R.drawable.os_dhb_callinggo);
			break;
		}
		if(!TextUtils.isEmpty(callLog.getCallnum())){			
			finalBitmap.display(holder.call_img, "phone:" + callLog.getCallnum(), loadingBitmap, loadingBitmap, true);
		}
		String count = "";
		if(callLog.getCount() > 0){
			count = "  ("  + count + callLog.getCount() + ")";
		}
		holder.name.setText(callLog.getCallname() + count);
		holder.number.setText(callLog.getCallnum());
		String logtime = callLog.getDate();
//		ContextUtil.getDealTime(logtime)
		holder.time.setText(logtime);
		addViewListener(holder.call_btn, callLog, position);
		return convertView;
	}

	private static class ViewHolder {
		ImageView call_img;
		ImageView call_type;
		TextView name;
		TextView number;
		TextView time;
		ImageView call_btn;
	}

	private void addViewListener(View view, final CallLogBean callLog,
			final int position) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(ctx, DhbBhpCallActivity.class);
//				intent.putExtra("phone", callLog.getCallnum());
//				ctx.startActivity(intent);
				dhbBhpActivity.callPhone(callLog);
			}
		});
	}
}
