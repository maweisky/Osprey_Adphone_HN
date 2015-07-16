package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Company;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainAdvPagerAdapter extends PagerAdapter {

	private Context context;
	private List<Company> list;
	private JazzyViewPager mJazzy;
	private Bitmap defaultBitmap;
	private FinalBitmap fb;
	private LayoutInflater mInflater;

	public MainAdvPagerAdapter(Context context, JazzyViewPager jazzy,
			List<Company> list) {
		this.context = context;
		this.list = list;
		this.mJazzy = jazzy;
		defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.os_jsh_wdj_pager_item_bg);
		fb = FinalBitmap.create(context);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view = mJazzy.findViewFromObject(position);
		if (view == null) {
			LogMgr.showLog("view is null");
			view = initView(position);
			mJazzy.setObjectForPosition(view, position);
		}
		initListener(view, position);
		initData(view, position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		LogMgr.showLog("view is remove");
		View view = mJazzy.findViewFromObject(position);
		container.removeView(view);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count=list.size();
		if(count%6==0){
			return count/6;
		}else{
			return count/6+1;
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	private View initView(int position) {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		View view = mInflater.inflate(R.layout.os_main_adv_pager_item, null);
		holder.iv1 = (ImageView) view.findViewById(R.id.iv1);
		holder.iv2 = (ImageView) view.findViewById(R.id.iv2);
		holder.iv3 = (ImageView) view.findViewById(R.id.iv3);
		holder.iv4 = (ImageView) view.findViewById(R.id.iv4);
		holder.iv5 = (ImageView) view.findViewById(R.id.iv5);
		holder.iv6 = (ImageView) view.findViewById(R.id.iv6);
		holder.tv1=(TextView)view.findViewById(R.id.tv1);
		holder.tv2=(TextView)view.findViewById(R.id.tv2);
		holder.tv3=(TextView)view.findViewById(R.id.tv3);
		holder.tv4=(TextView)view.findViewById(R.id.tv4);
		holder.tv5=(TextView)view.findViewById(R.id.tv5);
		holder.tv6=(TextView)view.findViewById(R.id.tv6);
		
		holder.ll1=(LinearLayout)view.findViewById(R.id.ll1);
		holder.ll2=(LinearLayout)view.findViewById(R.id.ll2);
		holder.ll3=(LinearLayout)view.findViewById(R.id.ll3);
		holder.ll4=(LinearLayout)view.findViewById(R.id.ll4);
		holder.ll5=(LinearLayout)view.findViewById(R.id.ll5);
		holder.ll6=(LinearLayout)view.findViewById(R.id.ll6);
		
		holder.tvline1=(TextView)view.findViewById(R.id.tvline1);
		holder.tvline2=(TextView)view.findViewById(R.id.tvline2);
		holder.vlinetv=(TextView)view.findViewById(R.id.vlinetv);
		holder.tvline4=(TextView)view.findViewById(R.id.tvline4);
		holder.tvline5=(TextView)view.findViewById(R.id.tvline5);
		view.setTag(holder);
		
		if (ContextUtil.getHeith(context) <= 480) {
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.iv1
					.getLayoutParams();
			linearParams.setMargins(0, 0, 0, 0);
			holder.iv1.setLayoutParams(linearParams);
			holder.iv2.setLayoutParams(linearParams);
			holder.iv3.setLayoutParams(linearParams);
			holder.iv4.setLayoutParams(linearParams);
			holder.iv5.setLayoutParams(linearParams);
			holder.iv6.setLayoutParams(linearParams);
		} else if (ContextUtil.getHeith(context) <= 800) {
			// if(ContextUtil.getWidth(this)<=480)
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.iv1
					.getLayoutParams();
			linearParams.setMargins(0, 0, 0, 0);
			holder.iv1.setLayoutParams(linearParams);
			holder.iv2.setLayoutParams(linearParams);
			holder.iv3.setLayoutParams(linearParams);
			holder.iv4.setLayoutParams(linearParams);
			holder.iv5.setLayoutParams(linearParams);
			holder.iv6.setLayoutParams(linearParams);
		} else if (ContextUtil.getHeith(context) <= 960) {
			// if(ContextUtil.getWidth(this)<=480)
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.iv1
					.getLayoutParams();
			linearParams.setMargins(0, 0, 0, 0);
			holder.iv1.setLayoutParams(linearParams);
			holder.iv2.setLayoutParams(linearParams);
			holder.iv3.setLayoutParams(linearParams);
			holder.iv4.setLayoutParams(linearParams);
			holder.iv5.setLayoutParams(linearParams);
			holder.iv6.setLayoutParams(linearParams);
		} else if (ContextUtil.getHeith(context) <= 1280) {
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.iv1
					.getLayoutParams();
			linearParams.setMargins(ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8));
			holder.iv1.setLayoutParams(linearParams);
			holder.iv2.setLayoutParams(linearParams);
			holder.iv3.setLayoutParams(linearParams);
			holder.iv4.setLayoutParams(linearParams);
			holder.iv5.setLayoutParams(linearParams);
			holder.iv6.setLayoutParams(linearParams);
		} else {
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.iv1
					.getLayoutParams();
			linearParams.setMargins(ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8), ContextUtil.dip2px(context, 8));
			holder.iv1.setLayoutParams(linearParams);
			holder.iv2.setLayoutParams(linearParams);
			holder.iv3.setLayoutParams(linearParams);
			holder.iv4.setLayoutParams(linearParams);
			holder.iv5.setLayoutParams(linearParams);
			holder.iv6.setLayoutParams(linearParams);
		}
		return view;
	}

	public void initListener(View view, final int position) {
		// TODO Auto-generated method stub
		final Holder holder = (Holder) view.getTag();
		holder.ll1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,WebViewActivity.class);
				intent.putExtra("url", list.get(position*6).getSiteurl());
				context.startActivity(intent);
			}
		});
		holder.ll2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(list.size()>position*6+1){		
					Intent intent=new Intent(context,WebViewActivity.class);
					intent.putExtra("url", list.get(position*6+1).getSiteurl());	
					context.startActivity(intent);
				}
			}
		});
		holder.ll3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(list.size()>position*6+2){	
					Intent intent=new Intent(context,WebViewActivity.class);
					intent.putExtra("url", list.get(position*6+2).getSiteurl());
					context.startActivity(intent);
				}
			}
		});
		holder.ll4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(list.size()>position*6+3){	
					Intent intent=new Intent(context,WebViewActivity.class);
					intent.putExtra("url", list.get(position*6+3).getSiteurl());
					context.startActivity(intent);
				}
			}
		});
		holder.ll5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(list.size()>position*6+4){	
					Intent intent=new Intent(context,WebViewActivity.class);
					intent.putExtra("url", list.get(position*6+4).getSiteurl());
					context.startActivity(intent);
				}
			}
		});
		holder.ll6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(list.size()>position*6+5){	
					Intent intent=new Intent(context,WebViewActivity.class);
					intent.putExtra("url", list.get(position*6+5).getSiteurl());
					context.startActivity(intent);
				}
			}
		});
	}

	public void initData(View view, final int position) {
		// TODO Auto-generated method stub
		final Holder holder = (Holder) view.getTag();
		fb.display(holder.iv1, list.get(position*6).getLogourl());
		holder.tv1.setText(list.get(position*6).getTitle());
		holder.tvline1.setVisibility(View.GONE);
		holder.tvline2.setVisibility(View.GONE);
		holder.vlinetv.setVisibility(View.GONE);
		holder.tvline4.setVisibility(View.GONE);
		holder.tvline5.setVisibility(View.GONE);
		if(list.size()>position*6+1){			
			fb.display(holder.iv2, list.get(position*6+1).getLogourl());
			holder.tv2.setText(list.get(position*6+1).getTitle());
			holder.tvline1.setVisibility(View.VISIBLE);
			holder.ll2.setBackgroundResource(R.drawable.app_base_selector1);
		}else{
			holder.tvline2.setVisibility(View.GONE);
			holder.vlinetv.setVisibility(View.GONE);
			holder.tvline4.setVisibility(View.GONE);
			holder.tvline5.setVisibility(View.GONE);
			holder.ll2.setBackgroundResource(R.color.transparent);
		}
		if(list.size()>position*6+2){			
			fb.display(holder.iv3, list.get(position*6+2).getLogourl());
			holder.tv3.setText(list.get(position*6+2).getTitle());
			holder.tvline1.setVisibility(View.VISIBLE);
			holder.tvline2.setVisibility(View.VISIBLE);
			holder.ll3.setBackgroundResource(R.drawable.app_base_selector1);
		}else{
			holder.vlinetv.setVisibility(View.GONE);
			holder.tvline4.setVisibility(View.GONE);
			holder.tvline5.setVisibility(View.GONE);
			holder.ll3.setBackgroundResource(R.color.transparent);
		}
		if(list.size()>position*6+3){
			fb.display(holder.iv4, list.get(position*6+3).getLogourl());
			holder.tv4.setText(list.get(position*6+3).getTitle());
			holder.tvline1.setVisibility(View.VISIBLE);
			holder.tvline2.setVisibility(View.VISIBLE);
			holder.vlinetv.setVisibility(View.VISIBLE);
			holder.ll4.setBackgroundResource(R.drawable.app_base_selector1);
		}else{
			holder.tvline4.setVisibility(View.GONE);
			holder.tvline5.setVisibility(View.GONE);
			holder.ll4.setBackgroundResource(R.color.transparent);
		}
		if(list.size()>position*6+4){
			fb.display(holder.iv5, list.get(position*6+4).getLogourl());
			holder.tv5.setText(list.get(position*6+4).getTitle());
			holder.tvline1.setVisibility(View.VISIBLE);
			holder.tvline2.setVisibility(View.VISIBLE);
			holder.vlinetv.setVisibility(View.VISIBLE);
			holder.tvline4.setVisibility(View.VISIBLE);
			holder.ll5.setBackgroundResource(R.drawable.app_base_selector1);
		}else{
			holder.tvline5.setVisibility(View.GONE);
			holder.ll5.setBackgroundResource(R.color.transparent);
		}
		if(list.size()>position*6+5){
			fb.display(holder.iv6, list.get(position*6+5).getLogourl());
			holder.tv6.setText(list.get(position*6+5).getTitle());
			holder.tvline1.setVisibility(View.VISIBLE);
			holder.tvline2.setVisibility(View.VISIBLE);
			holder.vlinetv.setVisibility(View.VISIBLE);
			holder.tvline4.setVisibility(View.VISIBLE);
			holder.tvline5.setVisibility(View.VISIBLE);
			holder.ll6.setBackgroundResource(R.drawable.app_base_selector1);
		}else{
			holder.ll6.setBackgroundResource(R.color.transparent);
		}
	}

	public class Holder {
		TextView tv1,tv2,tv3,tv4,tv5,tv6;
		ImageView iv1, iv2, iv3, iv4, iv5, iv6;
		LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
		TextView tvline1,tvline2,vlinetv,tvline4,tvline5;
	}
}
