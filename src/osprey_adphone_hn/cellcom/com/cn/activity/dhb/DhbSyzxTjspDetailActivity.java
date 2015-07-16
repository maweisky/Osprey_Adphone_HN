package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.GwcActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxJfscSpTypeAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxJfscSpTypeAdapter.SelectCallBack;
import osprey_adphone_hn.cellcom.com.cn.bean.DescribeComm;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpListDetail;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpListDetailComm;
import osprey_adphone_hn.cellcom.com.cn.bean.TjspInfo;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.AutoGridView;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class DhbSyzxTjspDetailActivity extends ActivityFrame implements
		OnActionSheetSelected {
	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合
	private List<View> dots; // 图片标题正文的那些点
	private int[] adimgs = { R.drawable.os_dhb_syzx_ad_icon1,
			R.drawable.os_dhb_syzx_ad_icon2, R.drawable.os_dhb_syzx_ad_icon3 };
	private LinearLayout dots_ll;// 装载点的布局
	private LinearLayout.LayoutParams ll = null;
	private int currentItem = 0;// 当前索引
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FrameLayout fl_ad;
	private TextView tv_yhjf;
	private TextView tv_sp_name, tv_sp_describe, tv_jf, tv_kc_num;
	private EditText et_sp_num;
	private ImageView iv_add, iv_minus, iv_add_to_gwc;
	private AutoGridView mGridView;
	private GrzxJfscSpTypeAdapter adapter;
	private List<JfscSpListDetail> jfscSpListDetailList = new ArrayList<JfscSpListDetail>();

	private TextView tv_empty;
//	private JfscSpList jfscSpListBean;
	private TjspInfo tjspInfo;
	private FinalBitmap finalBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_grzx_jfsc_detail_activity);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		SetTopBarTitle("商品详情");
		receiveIntentData();
		initView();
		initListener();
		initAd();
		initData();
	}

	private void receiveIntentData() {
//		if (getIntent().getSerializableExtra("jfscSpListBean") != null) {
//			jfscSpListBean = (JfscSpList) getIntent().getSerializableExtra(
//					"jfscSpListBean");
//		}
		tjspInfo = (TjspInfo)getIntent().getSerializableExtra("tjspInfo");
	}

	/**
	 * 初始化控件
	 */
	// 第一个参数是等级名称 第二个参数是选择位置 标记当前选择等级的位置
	private String[][] levelstr = { { "银色拉丝", "" }, { "银色拉丝", "" },
			{ "银色拉丝", "" }, { "银色拉丝", "" }, { "银色拉丝", "" } };

	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		tv_yhjf=(TextView)findViewById(R.id.tv_yhjf);
		tv_sp_name = (TextView) findViewById(R.id.tv_sp_name);
		tv_sp_describe = (TextView) findViewById(R.id.tv_sp_describe);
		tv_jf = (TextView) findViewById(R.id.tv_jf);
		tv_kc_num = (TextView) findViewById(R.id.tv_kc_num);
		et_sp_num = (EditText) findViewById(R.id.et_sp_num);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_minus = (ImageView) findViewById(R.id.iv_minus);
		iv_add_to_gwc = (ImageView) findViewById(R.id.iv_add_to_gwc);
		mGridView = (AutoGridView) findViewById(R.id.auto_gridview);

		adapter = new GrzxJfscSpTypeAdapter(this, levelstr);
		mGridView.setAdapter(adapter);
		adapter.setSelectCallBack(new SelectCallBack() {

			@Override
			public void isSelector(int position) {
				// TODO Auto-generated method stub
				for (int i = 0; i < levelstr.length; i++) {
					if (i == position) {
						levelstr[position][1] = position + "";
					} else {
						levelstr[i][1] = "";
					}
				}
				adapter.setInfos(levelstr);
			}
		});

		tv_empty = (TextView) findViewById(R.id.tv_empty);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_sp_num.getText().toString().equals("")) {
					int num = Integer.parseInt(et_sp_num.getText().toString());
					et_sp_num.setText((++num) + "");
				} else {
					et_sp_num.setText("1");
				}
			}
		});

		iv_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_sp_num.getText().toString().equals("")) {
					int num = Integer.parseInt(et_sp_num.getText().toString());
					if (num > 1) {
						et_sp_num.setText((--num) + "");
					} else {
						ShowMsg("最少兑换商品数为1~");
					}
				} else {
					et_sp_num.setText("1");
					ShowMsg("最少兑换商品数为1~");
				}
			}
		});

		iv_add_to_gwc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_sp_num.getText().toString().equals("")) {
					ShowMsg("请输入兑换数量~");
				} else {
					addToGwc();
				}
			}
		});

		tv_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_empty.setVisibility(View.GONE);
				getJfscDetailInfos();
			}
		});

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
//		if (ContextUtil.getHeith(this) <= 480) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(
//					DhbSyzxTjspDetailActivity.this, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 800) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(
//					DhbSyzxTjspDetailActivity.this, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(this, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 960) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(
//					DhbSyzxTjspDetailActivity.this, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(
//					DhbSyzxTjspDetailActivity.this, 220);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(
//					DhbSyzxTjspDetailActivity.this, 240);
//			fl_ad.setLayoutParams(linearParams);
//		}
	  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
          .getLayoutParams();
      linearParams.width=ContextUtil.getWidth(DhbSyzxTjspDetailActivity.this);
      linearParams.height = linearParams.width/2;
      fl_ad.setLayoutParams(linearParams);
//		if (jfscSpListBean != null) {
//			tv_sp_name.setText(jfscSpListBean.getTitle());
//			tv_sp_describe.setText(jfscSpListBean.getSimpleinfo());
//			tv_jf.setText(jfscSpListBean.getJifen());
//			tv_kc_num.setText(jfscSpListBean.getLeftnum() + "件");
//		}
		
		if (tjspInfo != null) {
			tv_sp_name.setText(tjspInfo.getTitle());
			tv_sp_describe.setText(tjspInfo.getSimpleinfo());
			tv_jf.setText(tjspInfo.getJifen());
			tv_jf.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
			String price;
			if("0".equals(tjspInfo.getDiscountprice())){
				price=tjspInfo.getJifen();
			}else{
				price=tjspInfo.getDiscountprice();
			}
			tv_yhjf.setText(price);
			tv_kc_num.setText(tjspInfo.getLeftnum() + "件");
		}
		getJfscDetailInfos();
	}

	/**
	 * 初始化广告
	 */
	private void initAd() {
		fl_ad = (FrameLayout) findViewById(R.id.fl_ad);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
		ll = new LinearLayout.LayoutParams(ContextUtil.dip2px(this, 8),
				ContextUtil.dip2px(this, 8));
		ll.setMargins(ContextUtil.dip2px(this, 1.5f), 0,
				ContextUtil.dip2px(this, 1.5f), 0);
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		if (dots_ll != null) {
			dots_ll.removeAllViews();
		}
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		dots = new ArrayList<View>();

		if (jfscSpListDetailList.size() > 0) {
			for (int i = 0; i < jfscSpListDetailList.size(); i++) {
				View view = DhbSyzxTjspDetailActivity.this.getLayoutInflater()
						.inflate(R.layout.app_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				if (jfscSpListDetailList.get(i).getPicurl() != null
						&& (jfscSpListDetailList.get(i).getPicurl()
								.contains(".jpg") || jfscSpListDetailList
								.get(i).getPicurl().contains(".png"))) {
					finalBitmap.display(img, jfscSpListDetailList.get(i)
							.getPicurl());
				}
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(
							DhbSyzxTjspDetailActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(
							DhbSyzxTjspDetailActivity.this, 1.5f), 0,
							ContextUtil.dip2px(DhbSyzxTjspDetailActivity.this,
									1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(
							DhbSyzxTjspDetailActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(
							DhbSyzxTjspDetailActivity.this, 1.5f), 0,
							ContextUtil.dip2px(DhbSyzxTjspDetailActivity.this,
									1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
			}
		} else {
			for (int i = 0; i < adimgs.length; i++) {
				View view = DhbSyzxTjspDetailActivity.this.getLayoutInflater()
						.inflate(R.layout.app_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setBackgroundResource(adimgs[i]);
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(
							DhbSyzxTjspDetailActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(
							DhbSyzxTjspDetailActivity.this, 1.5f), 0,
							ContextUtil.dip2px(DhbSyzxTjspDetailActivity.this,
									1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(
							DhbSyzxTjspDetailActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(
							DhbSyzxTjspDetailActivity.this, 1.5f), 0,
							ContextUtil.dip2px(DhbSyzxTjspDetailActivity.this,
									1.5f), 0);
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
			System.out.println("路口图片位置------>" + position);
			mJazzy.setCurrentItem(position);
			currentItem = position;
			// tv_name.setText(titles[position]);
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
	 * 
	 * 更新广告图片
	 * 
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 根据viewpager里图片的 角标设置当前要显示的图片
			mJazzy.setCurrentItem(currentItem);
		}
	};

	protected void onRestart() {
		super.onRestart();
		if (scheduledExecutor != null && scheduledExecutor.isShutdown()) {
			// 创建定时器
			scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
			// 首次启动时3秒后开始执行，接下来3秒执行一次
			scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 3, 5,
					TimeUnit.SECONDS);
		}
	};

	/**
	 * 获取积分商城列表详情数据
	 */
	private void getJfscDetailInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxTjspDetailActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("cid", tjspInfo.getCid());
		HttpHelper.getInstances(DhbSyzxTjspDetailActivity.this).send(
				FlowConsts.YYW_SHANGPIN_DETAIL, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
						tv_empty.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						tv_empty.setVisibility(View.VISIBLE);
//						mGridView.setVisibility(View.GONE);
						initJazzViewPager();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						JfscSpListDetailComm jfscSpListDetailComm = arg0.read(
								JfscSpListDetailComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = jfscSpListDetailComm.getReturnCode();
						String msg = jfscSpListDetailComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							tv_empty.setVisibility(View.VISIBLE);
//							mGridView.setVisibility(View.GONE);
							initJazzViewPager();
							return;
						}
						try {
							jfscSpListDetailList.clear();
							jfscSpListDetailList.addAll(jfscSpListDetailComm
									.getBody().getPicurl());
							if (jfscSpListDetailList.size() > 0) {
								tv_empty.setVisibility(View.GONE);
//								mGridView.setVisibility(View.VISIBLE);
								System.out.println("info------->"
										+ URLDecoder.decode(
												jfscSpListDetailComm.getBody()
														.getInfo(), "UTF-8"));
								tv_sp_describe.setText(URLDecoder.decode(
										jfscSpListDetailComm.getBody()
												.getInfo(), "UTF-8"));
								initJazzViewPager();
							} else {
								tv_empty.setVisibility(View.VISIBLE);
//								mGridView.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 加入购物车
	 */
	private void addToGwc() {
		if (SharepreferenceUtil.readString(DhbSyzxTjspDetailActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxTjspDetailActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxTjspDetailActivity.this, SharepreferenceUtil.fileName,
				"uid", ""));
		cellComAjaxParams.put("cid", tjspInfo.getCid());
		cellComAjaxParams.put("num", et_sp_num.getText().toString());
		HttpHelper.getInstances(DhbSyzxTjspDetailActivity.this).send(
				FlowConsts.YYW_SHANGPIN_ADD, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						DescribeComm describeComm = arg0.read(
								DescribeComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = describeComm.getReturnCode();
						String msg = describeComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							Toast.makeText(DhbSyzxTjspDetailActivity.this, msg,
									Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							AlertDialogPopupWindow.showSheet(
									DhbSyzxTjspDetailActivity.this, DhbSyzxTjspDetailActivity.this,
									"已将商品收入购物车", 1, "查看购物车", "再逛逛");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			Intent intent = new Intent(DhbSyzxTjspDetailActivity.this,
					GwcActivity.class);
			startActivity(intent);
			DhbSyzxTjspDetailActivity.this.finish();
		}
	}
}
