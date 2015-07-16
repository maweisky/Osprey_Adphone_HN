package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.SafetyInfoComm;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.global.AccountPersist;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SafetyInfoAlarmPicShow extends Activity{
	private LinearLayout llAppBack, llAppSet;
	private MarqueeText tvTopTitle;

	private FinalBitmap finalBitmap;

	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合

	String title = "报警图片";
	
	List<SafetyInfoComm> safetyinfolist = new ArrayList<SafetyInfoComm>();
	Account account;
	int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.os_alarmpic_show_activity);
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
		if(getIntent().getSerializableExtra("safetyinfolist")!=null){
			safetyinfolist = (List<SafetyInfoComm>) getIntent().getSerializableExtra("safetyinfolist");
		}
		if(getIntent().getIntExtra("position", -1)!=-1){
			position = getIntent().getIntExtra("position", -1);
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		llAppSet = (LinearLayout) findViewById(R.id.llAppSet);
		llAppSet.setVisibility(View.GONE);
		tvTopTitle = (MarqueeText) findViewById(R.id.tvTopTitle);

		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		account = AccountPersist.getInstance().getActiveAccountInfo(this);
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

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tvTopTitle.setText(title);
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (safetyinfolist.size() > 0) {
			for (int i = 0; i < safetyinfolist.size(); i++) {
				View view = getLayoutInflater().inflate(R.layout.os_jsh_ad_item,  null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
//				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.MATCH_PARENT, dm.widthPixels / 640 * 480);
//				lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//				img.setLayoutParams(lp);
				TextView tv_loading = (TextView) view.findViewById(R.id.tv_loading);
				if(safetyinfolist.get(i).getAlarmpicUrl()!=null&&!safetyinfolist.get(i).getAlarmpicUrl().trim().equals("")){
					tv_loading.setVisibility(View.VISIBLE);
					img.setScaleType(ScaleType.FIT_XY);
					if(account!=null){						
						String imgurl = safetyinfolist.get(i).getAlarmpicUrl()+
								"&UserId="+
								String.valueOf(Integer.parseInt(account.three_number)|0x80000000)+"&SessionID=" +
								account.sessionId+"&Option=1";
						LogMgr.showLog("imgurl------------->" + imgurl);
						finalBitmap.display(img, imgurl);
					}
				}else{
					tv_loading.setVisibility(View.GONE);
//					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//					img.setLayoutParams(rl);
					img.setScaleType(ScaleType.CENTER);
					img.setBackgroundResource(R.drawable.image_download_fail_icon);
				}
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				view_img.add(view);
			}

		}
		if (view_img.size() > 0) {
			mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(position);
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
			
		}
	}



}