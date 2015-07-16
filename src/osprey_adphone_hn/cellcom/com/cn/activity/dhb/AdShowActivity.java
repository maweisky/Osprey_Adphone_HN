package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.DescribeComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class AdShowActivity extends ActivityFrame implements OnActionSheetSelected {
	private LinearLayout llAppBack;
	private MarqueeText tvTopTitle;

	private RelativeLayout rl_money_anim;
	private LinearLayout ll_money_anim;
	private TextView tv_add_money_num;
	private ImageView iv_money_box;

	private FinalBitmap finalBitmap;

	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;

	private JazzyViewPager mJazzy;
	private List<View> dots; // 图片标题正文的那些点
	private LinearLayout dots_ll;
	private List<View> view_img;// 装载广告图片的集合
	private int currentItem = 0;// 当前索引

	// private ImageView iv_left_arrow, iv_right_arrow;

	KykAdResult kykadresult;

	private String currentGgid;// 当前广告ID
	private LinearLayout.LayoutParams ll = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.os_ad_show_activity);
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

		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) imageView_loading
				.getBackground();

		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
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
		ll = new LinearLayout.LayoutParams(ContextUtil.dip2px(
				AdShowActivity.this, 8), ContextUtil.dip2px(
				AdShowActivity.this, 8));
		ll.setMargins(ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0,
				ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0);
		if (kykadresult != null && kykadresult.getMoney2() != null
				&& !kykadresult.getMoney2().trim().equals("")) {
			if (kykadresult.getMoneytype().equals("1")) {
			    iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
				tv_add_money_num.setText("+" + kykadresult.getMoney2() + "积分");
			} else if (kykadresult.getMoneytype().equals("2")) {
			  iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
				tv_add_money_num.setText("+" + kykadresult.getMoney2() + "话费");
			} else if (kykadresult.getMoneytype().equals("3")) {
			  iv_money_box.setBackgroundResource(R.drawable.os_hb_box);
				tv_add_money_num.setText("+" + kykadresult.getMoney2() + "银元");
			}
		}
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		dots = new ArrayList<View>();
		if (kykadresult.getData().size() > 0) {
			for (int i = 0; i < kykadresult.getData().size(); i++) {
				View view = getLayoutInflater().inflate(
						R.layout.os_syzx_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, kykadresult.getData().get(i)
						.getMeitiurl());
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(AdShowActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(
							ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0,
							ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(AdShowActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(
							ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0,
							ContextUtil.dip2px(AdShowActivity.this, 1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
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
			// mJazzy.setCurrentItem(position);
			// currentItem = position;
			// oldPosition = position;
			mJazzy.setCurrentItem(position);
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.app_dot_normal);
			dots.get(position)
					.setBackgroundResource(R.drawable.app_dot_focused);
			oldPosition = position;
			if (position == (kykadresult.getData().size() - 1)) {
				getPrize(currentGgid);
			}
		}
	}

	/**
	 * 领取奖品
	 */
	private void getPrize(String ggid) {
		if (SharepreferenceUtil.readString(AdShowActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(AdShowActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				AdShowActivity.this, SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("ggid", ggid);
		cellComAjaxParams.put("grade", "2");
		HttpHelper.getInstances(AdShowActivity.this).send(
				FlowConsts.YYW_GETLOOKMONEY, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
						DescribeComm describeComm = cellComAjaxResult.read(
								DescribeComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(describeComm
								.getReturnCode())) {
							Toast.makeText(AdShowActivity.this,
									describeComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (rl_money_anim.getVisibility() == View.GONE) {
							rl_money_anim.setVisibility(View.VISIBLE);
						}
						// 播放动画，完毕后播放下一条广告
						ll_money_anim.removeAllViews();
						final FlakeView flakeView = new FlakeView(
								AdShowActivity.this);
						ll_money_anim.addView(flakeView);
						AnimationUtil.addScaleAnimation(tv_add_money_num, 1500);
						AnimationUtil.fadeInAnimation(AdShowActivity.this,
								iv_money_box);
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
								if (kykadresult != null
										&& kykadresult.getMoney2() != null
										&& !kykadresult.getMoney2().trim()
												.equals("")) {
									if (kykadresult.getMoneytype().equals("1")) {
										// 刷新本地积分
										CommonUtils.refreshLocalCaichan(
												AdShowActivity.this, "1",
												kykadresult.getMoney2());
									} else if (kykadresult.getMoneytype()
											.equals("2")) {
										// 刷新本地话费
										CommonUtils.refreshLocalCaichan(
												AdShowActivity.this, "2",
												kykadresult.getMoney2());
									} else if (kykadresult.getMoneytype()
											.equals("3")) {
										// 刷新本地银元
										CommonUtils.refreshLocalCaichan(
												AdShowActivity.this, "3",
												kykadresult.getMoney2());
									}
								}
								if (kykadresult.getMoneytype().equals("1")) {
									AlertDialogPopupWindow.showSheet(
											AdShowActivity.this,
											AdShowActivity.this, "您本次看一看获得"
													+ kykadresult.getMoney2()
													+ "积分", 1);
								} else if (kykadresult.getMoneytype().equals(
										"2")) {
									AlertDialogPopupWindow.showSheet(
											AdShowActivity.this,
											AdShowActivity.this, "您本次看一看获得"
													+ kykadresult.getMoney2()
													+ "话费", 1);
								} else if (kykadresult.getMoneytype().equals(
										"3")) {
									AlertDialogPopupWindow.showSheet(
											AdShowActivity.this,
											AdShowActivity.this, "您本次看一看获得"
													+ kykadresult.getMoney2()
													+ "银元", 1);
								}
							}
						}, 1500);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ll_loading.setVisibility(View.VISIBLE);
						if (animationDrawable != null) {
							animationDrawable.start();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
					}
				});
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
			Intent intent = new Intent();
			intent.putExtra("currentGgid", currentGgid);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
