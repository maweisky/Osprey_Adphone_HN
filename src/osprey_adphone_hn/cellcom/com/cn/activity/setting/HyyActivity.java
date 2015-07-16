package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.widget.viewflow.CircleFlowIndicator;
import osprey_adphone_hn.cellcom.com.cn.widget.viewflow.ViewFlow;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class HyyActivity extends ActivityFrame {
	private ViewFlow viewFlow;
	private FinalBitmap finalBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_hyy_activity);
		HideHeadBar();
		initView();
		initData();
		initListener();
	}

	private void initData() {
		// TODO Auto-generated method stub
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		finalBitmap=FinalBitmap.create(HyyActivity.this);
		viewFlow = (ViewFlow) findViewById(R.id.guide);
		viewFlow.setmSideBuffer(5); // 实际图片张数， 我的ImageAdapter实际图片张数为3
		viewFlow.setAdapter(new AdImageAdapter(this,finalBitmap));

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.guide_dot);
		viewFlow.setFlowIndicator(indic);
		// viewFlow.setTimeSpan(4500);
		// viewFlow.setSelection(2000); //设置初始位置
		// viewFlow.startAutoFlowTimer(); //启动自动播放
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {

	}

	class AdImageAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private FinalBitmap finalBitmap;
		private int[] resId = { R.drawable.guide01, R.drawable.guide02,
				R.drawable.guide03, R.drawable.guide04, R.drawable.guide05 };

		public AdImageAdapter(Context context,FinalBitmap finalBitmap) {
			mContext = context;
			this.finalBitmap=finalBitmap;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return resId.length/* <=1?resId.length:Integer.MAX_VALUE */;
		}

		@Override
		public Object getItem(int position) {
			return resId[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.os_guide_image_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.iv_ad = (ImageView) convertView
						.findViewById(R.id.iv_ad);
				viewHolder.ll_start = (LinearLayout) convertView
						.findViewById(R.id.ll_start);
				viewHolder.iv_start = (ImageView) convertView
						.findViewById(R.id.iv_start);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (resId.length > 0) {
				viewHolder.iv_ad.setAdjustViewBounds(true);
				viewHolder.iv_ad.setScaleType(ScaleType.FIT_XY);
				BitMapUtil.getImgOpt(HyyActivity.this, finalBitmap, viewHolder.iv_ad, resId[position]);
//				viewHolder.iv_ad.setBackgroundResource(resId[position/*
//																	 * %resId.length
//																	 */]);
			}
			if (resId.length - 1 == position) {
				viewHolder.ll_start.setVisibility(View.VISIBLE);
				AnimationUtil.infiniteFadeInAnimation(mContext,
						viewHolder.iv_start);
			} else {
				viewHolder.ll_start.setVisibility(View.GONE);
			}
			viewHolder.iv_start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setResult(RESULT_OK, getIntent());
					HyyActivity.this.finish();
				}
			});
			return convertView;
		}

		class ViewHolder {
			private ImageView iv_ad;
			private LinearLayout ll_start;
			private ImageView iv_start;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (CommonUtils.getCurrentChildMenuActivity().equals("hyy")) {
			CommonUtils.setCurrentChildMenuActivity("");
		}
	}
}
