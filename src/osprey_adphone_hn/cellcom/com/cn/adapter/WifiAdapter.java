package osprey_adphone_hn.cellcom.com.cn.adapter;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.JshWdjNetSetActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {
	JshWdjNetSetActivity jshWdjNetSetActivity;
	int iCurrentId;
	int iCount;
	int[] iType;
	int[] iStrength;
	String[] names;

	public WifiAdapter(JshWdjNetSetActivity jshWdjNetSetActivity) {
		this.jshWdjNetSetActivity = jshWdjNetSetActivity;
		this.iCount = 0;
	}

	public WifiAdapter(JshWdjNetSetActivity jshWdjNetSetActivity, int iCount,
			int[] iType, int[] iStrength, String[] names) {
		this.jshWdjNetSetActivity = jshWdjNetSetActivity;
		this.iCount = iCount;
		this.iType = iType;
		this.iStrength = iStrength;
		this.names = names;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return iCount;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final int tempposition = position;
		View view = arg1;
		if (null == view) {
			view = LayoutInflater.from(jshWdjNetSetActivity).inflate(
					R.layout.list_wifi_item, null);
		}

		TextView name = (TextView) view.findViewById(R.id.name);
		ImageView wifi_strength = (ImageView) view
				.findViewById(R.id.wifi_strength);
		ImageView choose_img = (ImageView) view.findViewById(R.id.choose_img);
		ImageView wifi_type = (ImageView) view.findViewById(R.id.wifi_type);
		if (iType[position] == 0) {
			wifi_type.setVisibility(ImageView.GONE);
		} else {
			wifi_type.setVisibility(ImageView.VISIBLE);
		}
		try {
			name.setText(names[position]);
		} catch (Exception e) {
			name.setText("");
		}

		if (position == iCurrentId) {
			choose_img.setVisibility(RelativeLayout.VISIBLE);
		} else {
			choose_img.setVisibility(RelativeLayout.GONE);
		}
		switch (iStrength[position]) {
		case 0:
			wifi_strength.setImageResource(R.drawable.ic_strength1);
			break;
		case 1:
			wifi_strength.setImageResource(R.drawable.ic_strength2);
			break;
		case 2:
			wifi_strength.setImageResource(R.drawable.ic_strength3);
			break;
		case 3:
			wifi_strength.setImageResource(R.drawable.ic_strength4);
			break;
		case 4:
			wifi_strength.setImageResource(R.drawable.ic_strength5);
			break;
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jshWdjNetSetActivity.onitemClick(tempposition, iType, names);
			}
		});
		return view;
	}

	public void updateData(int iCurrentId, int iCount, int[] iType,
			int[] iStrength, String[] names) {
		this.iCurrentId = iCurrentId;
		this.iCount = iCount;
		this.iType = iType;
		this.iStrength = iStrength;
		this.names = names;
		notifyDataSetChanged();
	}
}