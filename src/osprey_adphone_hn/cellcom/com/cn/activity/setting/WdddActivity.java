package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import java.util.ArrayList;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 我的订单
 * @author Administrator
 *
 */

public class WdddActivity extends ActivityFrame implements
		OnActionSheetSelected {
	private ViewPager mPager;// 页卡内容
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>(); // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView tv_title1/*, tv_title2, tv_title3*/, tv_title4;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	String cid = "";//商品ID
	String id = "";//订单记录ID

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.os_wddd_activity);
		initView();
		initListener();
		initViewPager();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		((TextView) findViewById(R.id.tvTopTitle)).setText(getResources().getString(R.string.os_dhb_grzx_wddd));
		findViewById(R.id.llAppSet).setVisibility(View.INVISIBLE);
		tv_title1 = (TextView) findViewById(R.id.tv_title1);
//		tv_title2 = (TextView) findViewById(R.id.tv_title2);
//		tv_title3 = (TextView) findViewById(R.id.tv_title3);
		tv_title4 = (TextView) findViewById(R.id.tv_title4);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		findViewById(R.id.llAppBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_title1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
				tv_title1.setTextColor(getResources().getColor(R.color.orange));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.gray));
				mPager.setCurrentItem(0);
			}
		});

//		tv_title2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				tv_title2.setTextColor(getResources().getColor(R.color.blue));
//				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
//				tv_title4.setTextColor(getResources().getColor(R.color.gray));
//				mPager.setCurrentItem(1);
//			}
//		});
//
//		tv_title3.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
//				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.blue));
//				tv_title4.setTextColor(getResources().getColor(R.color.gray));
//				mPager.setCurrentItem(2);
//			}
//		});

		tv_title4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.orange));
				mPager.setCurrentItem(3);
			}
		});
	}

	/**
	 * 初始化ViewPager
	 */
	DfspFragment dfspfragment;
//	DfhspFragment dfhspFragment;
//	DshspFragment dshspFragment;
	YgspFragment ygspFragment;

	private void initViewPager() {
		cursor = (ImageView) findViewById(R.id.cursor);
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				ContextUtil.getWidth(this) / 2, 5);
		rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		cursor.setLayoutParams(rl);
		offset = (ContextUtil.getWidth(this) / 2 - cursor.getWidth()) / 2;// 计算偏移量
		bmpW = cursor.getWidth();
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		dfspfragment = new DfspFragment();
//		dfhspFragment = new DfhspFragment();
//		dshspFragment = new DshspFragment();
		ygspFragment = new YgspFragment();
		mFragments.add(dfspfragment);
//		mFragments.add(dfhspFragment);
//		mFragments.add(dshspFragment);
		mFragments.add(ygspFragment);
		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setOffscreenPageLimit(3);
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), mFragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// loadFragment();
	}

	/**
	 * fragment 页面适配器
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragment) {
			super(fm);
			mFragments = fragment;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.vPager, mFragments.get(position)).commit();
			// container.addView(mFragments.get(position), 0);
			return mFragments.get(position);

		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	/**
	 * 页卡变换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//		int two = one * 2;// 页卡1 -> 页卡3 偏移量
//		int three = one * 3;// 页卡1 -> 页卡4 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {// arg0为当前页
			case 0:
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
				tv_title1.setTextColor(getResources().getColor(R.color.orange));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.gray));
				if (currIndex == 1) { // currIndex为之前页
					animation = new TranslateAnimation(one, 0, 0, 0);
				} /*else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}*/
				break;
			case 1:
//				tv_title2.setTextColor(getResources().getColor(R.color.blue));
				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.orange));
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}/* else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}*/
				break;
//			case 2:
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
//				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.blue));
//				tv_title4.setTextColor(getResources().getColor(R.color.gray));
//				if (currIndex == 0) {
//					animation = new TranslateAnimation(offset, two, 0, 0);
//				} else if (currIndex == 1) {
//					animation = new TranslateAnimation(one, two, 0, 0);
//				}
//				if (currIndex == 3) {
//					animation = new TranslateAnimation(three, two, 0, 0);
//				}
//				break;
//			case 3:
//				tv_title2.setTextColor(getResources().getColor(R.color.gray));
//				tv_title1.setTextColor(getResources().getColor(R.color.gray));
//				tv_title3.setTextColor(getResources().getColor(R.color.gray));
//				tv_title4.setTextColor(getResources().getColor(R.color.blue));
//				if (currIndex == 0) {
//					animation = new TranslateAnimation(offset, three, 0, 0);
//				} else if (currIndex == 1) {
//					animation = new TranslateAnimation(one, three, 0, 0);
//				}
//				if (currIndex == 2) {
//					animation = new TranslateAnimation(two, three, 0, 0);
//				}
//				break;
			}
			currIndex = arg0;
			// mPager.setCurrentItem(currIndex);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (CommonUtils.getCurrentChildMenuActivity().equals("gwc")) {
			CommonUtils.setCurrentChildMenuActivity("");
		}
	}

	public void updateGwcDfspNum(String cid, String num) {
		// TODO Auto-generated method stub
		dfspfragment.updateGwcDfspNum(cid, num);
	}

	public void delGwcDfspInfos(String cid) {
		// TODO Auto-generated method stub
		dfspfragment.delGwcDfspInfos(cid);
	}
	
	public void delGwcYgspInfos(String id) {
		// TODO Auto-generated method stub
		ygspFragment.delGwcYgspInfos(id);
	}
	
	public void getGrzl(final int position) {
		dfspfragment.getGrzl(position);
	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			delGwcDfspInfos(getCid());
		}else if(whichButton == 2){
			delGwcYgspInfos(this.getId());
		}
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public Dialog showSheet(Context context,final String cid,final String paytotalnum,final String paynum) {
		final Dialog dlg = new Dialog(context, R.style.loadingdialog);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.os_gwc_pay_dialog, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		
		ImageView iv_back = (ImageView)layout.findViewById(R.id.iv_back);

		final TextView tv_yinyuan = (TextView) layout.findViewById(R.id.tv_yinyuan);
		SpannableString ss_yinyuan = new SpannableString(tv_yinyuan.getText().toString());
		ss_yinyuan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_yinyuan.setText(ss_yinyuan);
		
		final TextView tv_jifen = (TextView) layout.findViewById(R.id.tv_jifen);
		tv_jifen.setText("积分        "+paytotalnum);
		SpannableString ss_jifen = new SpannableString(tv_jifen.getText().toString());
		ss_jifen.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_jifen.setText(ss_jifen);
		
		final TextView tv_huafei = (TextView) layout.findViewById(R.id.tv_huafei);
		SpannableString ss_huafei = new SpannableString(tv_huafei.getText().toString());
		ss_huafei.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_huafei.setText(ss_huafei);
		
		final TextView tv_paynum = (TextView) layout.findViewById(R.id.tv_paynum);
		tv_paynum.setText(paytotalnum+"积分");
		final CheckBox cb_hint = (CheckBox) layout.findViewById(R.id.cb_hint);
		final FButton btn_comfirm_pay = (FButton) layout.findViewById(R.id.btn_comfirm_pay);
		
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dlg.dismiss();
			}
		});

		btn_comfirm_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cb_hint.isChecked()){
					dlg.dismiss();
					dfspfragment.gwcDfspPay(cid, paynum);
				}else{
					Toast.makeText(WdddActivity.this, "您还没勾选支付协议~", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}
}
