package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
/**
 * 广告页面显示
 * @author Administrator
 *
 */
public class YyyAdShowActivity extends Activity {
	private LinearLayout llAppBack;
	private MarqueeText tvTopTitle;

	private FinalBitmap finalBitmap;

	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合
	private int currentItem = 0;// 当前索引

	private ImageView iv_left_arrow, iv_right_arrow;

	KykAdResult kykadresult;

	private String currentGgid;// 当前广告ID
	private String largepic;// 大图地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.os_yyy_ad_show_activity);
		receiveIntentData();
		initView();
		initListener();
		initData();
		initJazzViewPager();
	}

	/**
	 * 接收Intent数据
	 */
	private void receiveIntentData() {
		if (getIntent().getSerializableExtra("kykadresult") != null) {
			kykadresult = (KykAdResult) getIntent().getSerializableExtra(
					"kykadresult");
		}
		if (getIntent().getStringExtra("currentGgid") != null) {
			currentGgid = getIntent().getStringExtra("currentGgid");
		}
		if (getIntent().getStringExtra("largepic") != null) {
			largepic = getIntent().getStringExtra("largepic");
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		tvTopTitle = (MarqueeText) findViewById(R.id.tvTopTitle);

		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		mJazzy.setTransitionEffect(TransitionEffect.Standard);

		iv_left_arrow = (ImageView) findViewById(R.id.iv_left_arrow);
		iv_left_arrow.setVisibility(View.GONE);
		iv_right_arrow = (ImageView) findViewById(R.id.iv_right_arrow);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
						iv_right_arrow);
			}
		}, 300);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		llAppBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		iv_left_arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentItem >= 1) {
					mJazzy.setCurrentItem((currentItem - 1));
				}
			}
		});

		iv_right_arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentItem < kykadresult.getData().size() - 1) {
					mJazzy.setCurrentItem((currentItem + 1));
				} else if (currentItem == (kykadresult.getData().size() - 1)) {
					Intent intent = new Intent(YyyAdShowActivity.this,
							DhbSyzxShakeActivity.class);
					intent.putExtra("currentGgid", currentGgid);
					intent.putExtra("largepic", largepic);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tvTopTitle.setText(kykadresult.getTitle());
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (kykadresult.getData().size() > 0) {
			for (int i = 0; i < kykadresult.getData().size(); i++) {
				View view = getLayoutInflater().inflate(R.layout.os_syzx_ad_item,
						null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, kykadresult.getData().get(i)
						.getMeitiurl());
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
				view_img.add(view);
			}

		}
		if (view_img.size() > 0) {
			mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(0);
		}

		mJazzy.setOnPageChangeListener(new MyJazzyViewPager());
		mJazzy.setPageMargin(30);
	}

	/**
	 * 
	 * 路口截图改变ViewPager监听
	 * 
	 */
	public class MyJazzyViewPager implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			System.out.println("路口图片位置------>" + position);
			mJazzy.setCurrentItem(position);
			currentItem = position;
			oldPosition = position;
			if (position == 0) {
				iv_left_arrow.setVisibility(View.GONE);
				iv_right_arrow.setVisibility(View.VISIBLE);
				AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
						iv_right_arrow);
			}/*
			 * else if(position == (kykadresult.getData().size()-1)){
			 * iv_left_arrow.setVisibility(View.VISIBLE);
			 * iv_right_arrow.setVisibility(View.VISIBLE);
			 * AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
			 * iv_left_arrow);
			 * AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
			 * iv_right_arrow); }
			 */else {
				iv_left_arrow.setVisibility(View.VISIBLE);
				iv_right_arrow.setVisibility(View.VISIBLE);
				AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
						iv_left_arrow);
				AnimationUtil.infiniteFadeInAnimation(YyyAdShowActivity.this,
						iv_right_arrow);
			}
		}
	}

}
