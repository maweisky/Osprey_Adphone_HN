package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCz;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 商业中心广告页显示
 * @author Administrator
 *
 */
public class SysAdShowActivity extends ActivityFrame implements
		OnActionSheetSelected {
	private LinearLayout llAppBack;
	private MarqueeText tvTopTitle;

	private RelativeLayout rl_money_anim;
	private LinearLayout ll_money_anim;
	private TextView tv_add_money_num;
	private ImageView iv_money_box;
	private FinalBitmap finalBitmap;

	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合
	private int currentItem = 0;// 当前索引

//	private ImageView iv_left_arrow, iv_right_arrow;

	KykAdResult kykadresult;
	GrzxCz grzxCz;
	
	boolean isLj = false;//是否已领奖

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.os_sys_ad_show_activity);
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
		if (getIntent().getSerializableExtra("grzxCz") != null) {
			grzxCz= (GrzxCz) getIntent().getSerializableExtra(
					"grzxCz");
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		tvTopTitle = (MarqueeText) findViewById(R.id.tvTopTitle);

		rl_money_anim = (RelativeLayout) findViewById(R.id.rl_money_anim);
		ll_money_anim = (LinearLayout) findViewById(R.id.ll_money_anim);
		tv_add_money_num = (TextView) findViewById(R.id.tv_add_money_num);
		iv_money_box = (ImageView) findViewById(R.id.iv_money_box);
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		mJazzy.setTransitionEffect(TransitionEffect.Standard);

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
		tvTopTitle.setText(kykadresult.getTitle());
				if (grzxCz.getVerytype().equals("1")) {
					tv_add_money_num.setText("+" + grzxCz.getVerynum()
							+ "积分");
				} else if (grzxCz.getVerytype().equals("2")) {
					tv_add_money_num.setText("+" + grzxCz.getVerynum()
							+ "话费");
				} else if (grzxCz.getVerytype().equals("3")) {
					tv_add_money_num.setText("+" + grzxCz.getVerynum()
							+ "银元");
				}
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
		if(kykadresult.getData().size()==1){
			if(!isLj){
				getPrize();
			}
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
			mJazzy.setCurrentItem(position);
			currentItem = position;
			oldPosition = position;
			if (position == 0) {
//				iv_left_arrow.setVisibility(View.GONE);
//				iv_right_arrow.setVisibility(View.VISIBLE);
//				AnimationUtil.infiniteFadeInAnimation(SysAdShowActivity.this,
//						iv_right_arrow);
			} else if (position == (kykadresult.getData().size() - 1)) {
//				iv_left_arrow.setVisibility(View.VISIBLE);
//				iv_right_arrow.setVisibility(View.GONE);
//				AnimationUtil.infiniteFadeInAnimation(SysAdShowActivity.this,
//						iv_left_arrow);
//				if (sysPrizeResult != null
//						&& sysPrizeResult.getIfmoney() != null
//						&& sysPrizeResult.getIfmoney().equals("Y")) {
//					if(!isLj){
//						getPrize();
//					}
//				} else {
//					if(!isLj){
//						AlertDialogPopupWindow.showSheet(SysAdShowActivity.this,
//								SysAdShowActivity.this, "很遗憾，您本次扫一扫获得奖品", 1);
//					}
//				}
				if(!isLj){
					getPrize();
				}
			} else {
//				iv_left_arrow.setVisibility(View.VISIBLE);
//				iv_right_arrow.setVisibility(View.VISIBLE);
//				AnimationUtil.infiniteFadeInAnimation(SysAdShowActivity.this,
//						iv_left_arrow);
//				AnimationUtil.infiniteFadeInAnimation(SysAdShowActivity.this,
//						iv_right_arrow);
			}
		}
	}

	/**
	 * 领取奖品
	 */
	private void getPrize() {
		if (rl_money_anim.getVisibility() == View.GONE) {
			rl_money_anim.setVisibility(View.VISIBLE);
		}
		// 播放动画，完毕后播放下一条广告
		ll_money_anim.removeAllViews();
		final FlakeView flakeView = new FlakeView(SysAdShowActivity.this);
		ll_money_anim.addView(flakeView);
		AnimationUtil.addScaleAnimation(tv_add_money_num, 1500);
		AnimationUtil.fadeInAnimation(SysAdShowActivity.this, iv_money_box);
		startMediaPlayer(R.raw.shake_match);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				flakeView.pause();
				if (rl_money_anim.getVisibility() == View.VISIBLE) {
					rl_money_anim.setVisibility(View.GONE);
				}
				ll_money_anim.removeAllViews();
				if (grzxCz.getVerytype().equals("1")) {
					AlertDialogPopupWindow.showSheet(SysAdShowActivity.this,
							SysAdShowActivity.this,
							"恭喜您获得由"+grzxCz.getCompanyname()+"提供的"+ grzxCz.getVerynum() + "积分", 1);
				} else if (grzxCz.getVerytype().equals("2")) {
					AlertDialogPopupWindow.showSheet(SysAdShowActivity.this,
							SysAdShowActivity.this,
							"恭喜您获得由" +grzxCz.getCompanyname()+"提供的"+ grzxCz.getVerynum() + "话费", 1);
				} else if (grzxCz.getVerytype().equals("3")) {
					AlertDialogPopupWindow.showSheet(SysAdShowActivity.this,
							SysAdShowActivity.this,
							"恭喜您获得由" +grzxCz.getCompanyname()+"提供的"+ grzxCz.getVerynum() + "银元", 1);
				}
				isLj = true;
			}
		}, 1500);
	}

	/**
	 * 获奖声音
	 * 
	 * @param res
	 */
	public void startMediaPlayer(int res) {
		MediaPlayer player;
		player = MediaPlayer.create(this, res);
		player.setLooping(false);
		player.start();

	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			isLj = true;
		}
	}
}