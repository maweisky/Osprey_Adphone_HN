package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.zxing.activity.CaptureActivity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 账号充值
 * @author Administrator
 *
 */

public class DhbGrzxZhczActivity extends ActivityFrame {
	private ViewPager mPager;// 页卡内容
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>(); // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView tv_title1, tv_title4;// 页卡头标
	private LinearLayout llAppSys;
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
		setContentView(R.layout.os_grzxcz_activity);
		initView();
		initListener();
		initViewPager();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		((TextView) findViewById(R.id.tvTopTitle)).setText(getResources().getString(R.string.os_dhb_grzx_zhcz));
		tv_title1 = (TextView) findViewById(R.id.tv_title1);
		tv_title4 = (TextView) findViewById(R.id.tv_title4);
		llAppSys=(LinearLayout)findViewById(R.id.llAppSys);
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
				tv_title1.setTextColor(getResources().getColor(R.color.orange));
				tv_title4.setTextColor(getResources().getColor(R.color.gray));
				mPager.setCurrentItem(0);
			}
		});

		tv_title4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_title1.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.orange));
				mPager.setCurrentItem(1);
			}
		});
		llAppSys.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent=new Intent(DhbGrzxZhczActivity.this,CaptureActivity.class);
            startActivity(intent);
          }
        });
	}

	/**
	 * 初始化ViewPager
	 */
	DhbGrzxXjczFragment dhbGrzxXjczFragment;
	DhbGrzxTykczFragment dhbGrzxTykczFragment;

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
		dhbGrzxXjczFragment = new DhbGrzxXjczFragment();
		dhbGrzxTykczFragment = new DhbGrzxTykczFragment();
		mFragments.add(dhbGrzxXjczFragment);
		mFragments.add(dhbGrzxTykczFragment);
		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setOffscreenPageLimit(3);
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), mFragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
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

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {// arg0为当前页
			case 0:
				tv_title1.setTextColor(getResources().getColor(R.color.orange));
				tv_title4.setTextColor(getResources().getColor(R.color.gray));
				if (currIndex == 1) { // currIndex为之前页
					animation = new TranslateAnimation(one, 0, 0, 0);
				} 
				break;
			case 1:
				tv_title1.setTextColor(getResources().getColor(R.color.gray));
				tv_title4.setTextColor(getResources().getColor(R.color.orange));
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				break;
			}
			currIndex = arg0;
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
}
