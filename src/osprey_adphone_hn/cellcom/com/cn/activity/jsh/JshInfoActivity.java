package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.Rotate3DAnimation;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 家生活信息
 * @author Administrator
 *
 */
public class JshInfoActivity extends Fragment {
	private JshFragmentActivity act;
	
	private JazzyViewPager mJazzy;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem;
	private ArrayList<View> view_img;
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FinalBitmap finalBitmap;
	private FrameLayout fl_ad;

	private LinearLayout ll_aq_info,ll_wg_info,ll_af_info, ll_zpj_info;//,ll_zp_info,ll_lx_info;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (JshFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_jsh_info_activity, container,
				false);
		initView(v, savedInstanceState);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
		mJazzy = (JazzyViewPager) v.findViewById(R.id.jazzy_viewpager);
		fl_ad = (FrameLayout) v.findViewById(R.id.fl_ad);
		dots_ll = (LinearLayout) v.findViewById(R.id.ll_dot);
		
		ll_aq_info = (LinearLayout) v.findViewById(R.id.ll_aq_info);
		ll_wg_info = (LinearLayout) v.findViewById(R.id.ll_wg_info);
		ll_af_info = (LinearLayout) v.findViewById(R.id.ll_af_info);
		ll_zpj_info = (LinearLayout) v.findViewById(R.id.ll_zpj_info);
//		ll_zp_info = (LinearLayout) v.findViewById(R.id.ll_zp_info);
//		ll_lx_info = (LinearLayout) v.findViewById(R.id.ll_lx_info);
		
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Rotate3DAnimation rotation_aq_info = new Rotate3DAnimation(-90, 0,
						ll_aq_info.getWidth() / 2, ll_aq_info.getHeight() / 2, 0.0f,
						false);
				rotation_aq_info.setDuration(500);
				rotation_aq_info.setFillAfter(true);
				ll_aq_info.startAnimation(rotation_aq_info);
				ll_aq_info.setVisibility(View.VISIBLE);

				Rotate3DAnimation rotation_wg_info = new Rotate3DAnimation(-90, 0,
						ll_wg_info.getWidth() / 2, ll_wg_info.getHeight() / 2, 0.0f,
						false);
				rotation_wg_info.setDuration(500);
				rotation_wg_info.setFillAfter(true);
				ll_wg_info.startAnimation(rotation_wg_info);
				ll_wg_info.setVisibility(View.VISIBLE);

				Rotate3DAnimation rotation_af_info = new Rotate3DAnimation(-90, 0,
						ll_af_info.getWidth() / 2, ll_af_info.getHeight() / 2, 0.0f,
						false);
				rotation_af_info.setDuration(500);
				rotation_af_info.setFillAfter(true);
				ll_af_info.startAnimation(rotation_af_info);
				ll_af_info.setVisibility(View.VISIBLE);
				
				Rotate3DAnimation rotation_zpj_info = new Rotate3DAnimation(-90, 0,
						ll_zpj_info.getWidth() / 2, ll_zpj_info.getHeight() / 2, 0.0f,
						false);
				rotation_zpj_info.setDuration(500);
				rotation_zpj_info.setFillAfter(true);
				ll_zpj_info.startAnimation(rotation_zpj_info);
				ll_zpj_info.setVisibility(View.VISIBLE);

				rotation_af_info.setInterpolator(new AccelerateInterpolator());
				// 设置监听
				rotation_af_info.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

//						Rotate3DAnimation rotation_zp_info = new Rotate3DAnimation(
//								-90, 0, ll_zp_info.getWidth() / 2, ll_zp_info
//										.getHeight() / 2, 0.0f, false);
//						rotation_zp_info.setDuration(500);
//						rotation_zp_info.setFillAfter(true);
//						ll_zp_info.startAnimation(rotation_zp_info);
//						ll_zp_info.setVisibility(View.VISIBLE);
//
//						Rotate3DAnimation rotation_lx_info = new Rotate3DAnimation(
//								-90, 0, ll_lx_info.getWidth() / 2, ll_lx_info
//										.getHeight() / 2, 0.0f, false);
//						rotation_lx_info.setDuration(500);
//						rotation_lx_info.setFillAfter(true);
//						ll_lx_info.startAnimation(rotation_lx_info);
//						ll_lx_info.setVisibility(View.VISIBLE);

					}
				});
			}
		}, 300);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		//安全信息
		ll_aq_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act,JshSafetyInfoActivity.class);
				startActivity(intent);
			}
		});
		
		//物管信息
		ll_wg_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act,JshWgInfoActivity.class);
				startActivity(intent);
			}
		});
		
		//安防商城
		ll_af_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//照片夹
		ll_zpj_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act,JshInfoZpjFragmentActivity.class);
				startActivity(intent);
			}
		});
		
		//抓拍信息
//		ll_zp_info.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(act,JshInfoSnapShotListActivity.class);
//				startActivity(intent);
//			}
//		});
		
		//录像信息
//		ll_lx_info.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(act,JshInfoRecordVideoListActivity.class);
//				startActivity(intent);
//			}
//		});
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		finalBitmap = FinalBitmap.create(act);
//		if (ContextUtil.getHeith(act) <= 480) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 60);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 800) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 960) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 200);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 210);
//			fl_ad.setLayoutParams(linearParams);
//		}
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
            .getLayoutParams();
        linearParams.width=ContextUtil.getWidth(act);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		getAdv();
	}
	
	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("pos", "5");
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETGG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(act, advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						advs = advComm.getBody();
						initJazzViewPager();
					}
				});
	}
	
	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				ContextUtil.dip2px(act, 8), ContextUtil.dip2px(act, 8));
		ll.setMargins(ContextUtil.dip2px(act, 1.5f), 0,
				ContextUtil.dip2px(act, 1.5f), 0);
		dots = new ArrayList<View>();
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = act.getLayoutInflater().inflate(
						R.layout.os_main_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setAdjustViewBounds(true);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, advs.get(i).getMeitiurl());
				final int tempPos=i;
				img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!TextUtils.isEmpty(advs.get(tempPos).getLinkurl())){
							Intent intent = new Intent(act, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
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
		// mJazzy.setPageMargin(30);
		// 创建定时器
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		// 首次启动时3秒后开始执行，接下来3秒执行一次
		scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 3, 5,
				TimeUnit.SECONDS);
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
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.app_dot_normal);
			dots.get(position)
					.setBackgroundResource(R.drawable.app_dot_focused);
			oldPosition = position;
		}
	}

	/**
	 * 
	 * 创建自动滚动广告线程
	 * 
	 */
	class ViewpagerTask implements Runnable {
		@Override
		public void run() {
			synchronized (mJazzy) {
				currentItem = (currentItem + 1) % view_img.size();
				handler.sendEmptyMessage(0);
			}
		}
	}

	/**
	 * 更新广告图片
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 根据viewpager里图片的 角标设置当前要显示的图片
			mJazzy.setCurrentItem(currentItem);
		}
	};
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}
