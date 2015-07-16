package osprey_adphone_hn.cellcom.com.cn.activity.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.MainAdvPagerAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.Company;
import osprey_adphone_hn.cellcom.com.cn.bean.CompanyAdvComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.bean.Message;
import p2p.cellcom.com.cn.db.DataManager;
import p2p.cellcom.com.cn.db.SharedPreferencesManager;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.global.NpcCommon.NETWORK_TYPE;
import p2p.cellcom.com.cn.listener.P2PListener;
import p2p.cellcom.com.cn.listener.SettingListener;
import p2p.cellcom.com.cn.thread.ExitTask;
import p2p.cellcom.com.cn.thread.ExitTask.ExitCallBack;
import p2p.cellcom.com.cn.thread.GetAccountInfoTask;
import p2p.cellcom.com.cn.thread.GetAccountInfoTask.GetAccountInfoCallBack;
import p2p.cellcom.com.cn.thread.LoginTask;
import p2p.cellcom.com.cn.thread.LoginTask.LoginCallBack;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

import com.p2p.core.P2PHandler;
import com.p2p.core.network.GetAccountInfoResult;
import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;
import com.umeng.analytics.MobclickAgent;

public class PreMainActivity extends ActivityFrame {
	private String uid;
	private List<View> dots; // 图片标题正文的那些点
	private JazzyViewPager mJazzy;
	private ArrayList<View> view_img;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private FinalBitmap finalBitmap;
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private int currentItem;
	private FrameLayout fl_ad;

	private JazzyViewPager advviewpager;
	private LinearLayout advll_dot;
	private MainAdvPagerAdapter mainAdvPagerAdapter;
	private List<View> dotList = new ArrayList<View>();
	private int dotPosition = -1;
	private List<Company> companies;

	private Button dhbbtn;
	private Button jshbtn;
	private Button cshbtn;
	private String dpitype;
	private boolean isRegFilter = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.premain);
		AppendTitleBody();
		initView();
		initData();
		initUM();
		init2cu();
		initListener();
		getAdv();
		getCompanyAdv();
	}

	private void initUM() {
		// TODO Auto-generated method stub
		MobclickAgent.setDebugMode(false);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		MobclickAgent.updateOnlineConfig(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		uid = SharepreferenceUtil.readString(PreMainActivity.this,
				SharepreferenceUtil.fileName, "uid");
		finalBitmap = FinalBitmap.create(PreMainActivity.this);
		if (ContextUtil.getHeith(PreMainActivity.this) <= 480) {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 60);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "l";
		} else if (ContextUtil.getHeith(PreMainActivity.this) <= 800) {
			// if(ContextUtil.getWidth(this)<=480)
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 140);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "l";
		}else if (ContextUtil.getHeith(PreMainActivity.this) <= 860) {
			// if(ContextUtil.getWidth(this)<=480)
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 150);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "l";
		} else if (ContextUtil.getHeith(PreMainActivity.this) <= 960) {
			// if(ContextUtil.getWidth(this)<=480)
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 180);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "m";
		} else if (ContextUtil.getHeith(PreMainActivity.this) <= 1280) {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 200);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "m";
		} else {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(PreMainActivity.this, 210);
//			fl_ad.setLayoutParams(linearParams);
			dpitype = "h";
		}
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
            .getLayoutParams();
		linearParams.width=ContextUtil.getWidth(PreMainActivity.this);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(PreMainActivity.this, finalBitmap, mJazzy, R.drawable.os_login_topicon);
	}

	private void initListener() {
		advviewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (dotList.size() > 0
						&& (dotPosition == -1 || dotPosition != position)) {
					if (dotPosition >= 0) {
						dotList.get(dotPosition).setBackgroundResource(
								R.drawable.app_dot_normal);
					}
					dotList.get(position).setBackgroundResource(
							R.drawable.app_dot_focused);
					dotPosition = position;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		dhbbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PreMainActivity.this,
						MainActivity.class);
				intent.putExtra("tag", "dhb");
				startActivity(intent);
			}
		});

		jshbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PreMainActivity.this,
						MainActivity.class);
				intent.putExtra("tag", "jsh");
				startActivity(intent);
			}
		});

		cshbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PreMainActivity.this,
						MainActivity.class);
				intent.putExtra("tag", "csh");
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mJazzy = (JazzyViewPager) findViewById(R.id.viewpager);
		advviewpager = (JazzyViewPager) findViewById(R.id.advviewpager);
		advll_dot = (LinearLayout) findViewById(R.id.advll_dot);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
		fl_ad = (FrameLayout) findViewById(R.id.fl_ad);

		dhbbtn = (Button) findViewById(R.id.dhbbtn);
		jshbtn = (Button) findViewById(R.id.jshbtn);
		cshbtn = (Button) findViewById(R.id.cshbtn);
	}

	private void init2cu() {
		DataManager.findAlarmMaskByActiveUser(PreMainActivity.this, "");
		NpcCommon.verifyNetwork(PreMainActivity.this);

		regFilter();
		if (!verifyLogin()) {
			String accountstr = SharepreferenceUtil.readString(
					PreMainActivity.this, SharepreferenceUtil.fileName,
					"account");
			String pwd2cu = SharepreferenceUtil.readString(
					PreMainActivity.this, SharepreferenceUtil.fileName,
					"pwd2cu");
			loginVideo("newyywapp" + accountstr + "@yywapp.com", pwd2cu);
		} else {
			FList.getInstance();
			P2PHandler.getInstance().p2pInit(this, new P2PListener(),
					new SettingListener());
			connect();
			getAccountInfo();
		}
	}

	private void getAccountInfo() {
		GetAccountInfoTask.startTask(PreMainActivity.this,
				new GetAccountInfoCallBack() {

					@Override
					public void onPostExecute(GetAccountInfoResult result) {
						// TODO Auto-generated method stub
						switch (Integer.parseInt(result.error_code)) {
						case NetManager.SESSION_ID_ERROR:
							Intent i = new Intent();
							i.setAction(Constants.Action.SESSION_ID_ERROR);
							MyApp.app.sendBroadcast(i);
							break;
						case NetManager.CONNECT_CHANGE:
							getAccountInfo();
							return;
						case NetManager.GET_ACCOUNT_SUCCESS:
							try {
								String email = result.email;
								String phone = result.phone;
								Account account = AccountPersist.getInstance()
										.getActiveAccountInfo(
												PreMainActivity.this);
								if (null == account) {
									account = new Account();
								}
								account.email = email;
								account.phone = phone;
								AccountPersist.getInstance().setActiveAccount(
										PreMainActivity.this, account);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						default:
							break;
						}
					}
				});
	}

	private boolean verifyLogin() {
		Account activeUser = AccountPersist.getInstance().getActiveAccountInfo(
				PreMainActivity.this);

		if (activeUser != null) {
			NpcCommon.mThreeNum = activeUser.three_number;
			return true;
		}
		return false;
	}

	private void loginVideo(final String account, final String pwd) {
		LoginTask.startTask(PreMainActivity.this, account, pwd,
				new LoginCallBack() {

					@Override
					public void onPostExecute(LoginResult result) {
						// TODO Auto-generated method stub
						switch (Integer.parseInt(result.error_code)) {
						case NetManager.SESSION_ID_ERROR:
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_session_id_error));
							break;
						case NetManager.CONNECT_CHANGE:
							loginVideo(account, pwd);
							return;
						case NetManager.LOGIN_SUCCESS:
							SharedPreferencesManager
									.getInstance()
									.putData(
											PreMainActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTNAME_EMAIL,
											account);
							SharedPreferencesManager
									.getInstance()
									.putData(
											PreMainActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTPASS_EMAIL,
											pwd);
							SharedPreferencesManager.getInstance()
									.putRecentLoginType(PreMainActivity.this,
											Constants.LoginType.EMAIL);

							String codeStr1 = String.valueOf(Long
									.parseLong(result.rCode1));
							String codeStr2 = String.valueOf(Long
									.parseLong(result.rCode2));
							Account account = AccountPersist.getInstance()
									.getActiveAccountInfo(PreMainActivity.this);
							if (null == account) {
								account = new Account();
							}
							account.three_number = result.contactId;
							account.phone = result.phone;
							account.email = result.email;
							account.sessionId = result.sessionId;
							account.rCode1 = codeStr1;
							account.rCode2 = codeStr2;
							account.countryCode = result.countryCode;
							AccountPersist.getInstance().setActiveAccount(
									PreMainActivity.this, account);
							NpcCommon.mThreeNum = AccountPersist.getInstance()
									.getActiveAccountInfo(PreMainActivity.this).three_number;
							break;
						case NetManager.LOGIN_USER_UNEXIST:
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_account_no_exist));
							break;
						case NetManager.LOGIN_PWD_ERROR:
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_password_error));
							break;
						default:
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_loginfail));
							break;
						}
					}
				});
	}

	private void connect() {
		LogMgr.showLog("MainService ready start.................");
		Intent service = new Intent(MyApp.MAIN_SERVICE_START);
		startService(service);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.ACTION_NETWORK_CHANGE);
		filter.addAction(Constants.Action.ACTION_SWITCH_USER);
		filter.addAction(Constants.Action.ACTION_EXIT);
		filter.addAction(Constants.Action.RECEIVE_MSG);
		filter.addAction(Constants.Action.RECEIVE_SYS_MSG);
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		filter.addAction(Constants.Action.ACTION_UPDATE);
		filter.addAction(Constants.Action.SESSION_ID_ERROR);
		this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					Constants.Action.ACTION_NETWORK_CHANGE)) {
				boolean isNetConnect = false;
				ConnectivityManager connectivityManager = (ConnectivityManager) PreMainActivity.this
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetInfo = connectivityManager
						.getActiveNetworkInfo();
				if (activeNetInfo != null) {
					if (activeNetInfo.isConnected()) {
						isNetConnect = true;
						LogMgr.showLog("mReceiver==>"
								+ getResources().getString(
										R.string.os_message_net_connect));
					} else {
						LogMgr.showLog("mReceiver==>"
								+ getResources().getString(
										R.string.os_network_error));
					}
					if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						NpcCommon.mNetWorkType = NETWORK_TYPE.NETWORK_WIFI;
					} else {
						NpcCommon.mNetWorkType = NETWORK_TYPE.NETWORK_2GOR3G;
					}
				} else {
					LogMgr.showLog("mReceiver==>"
							+ getResources().getString(
									R.string.os_network_error));
				}

				NpcCommon.setNetWorkState(isNetConnect);

				Intent intentNew = new Intent();
				intentNew.setAction(Constants.Action.NET_WORK_TYPE_CHANGE);
				PreMainActivity.this.sendBroadcast(intentNew);
			} else if (intent.getAction().equals(
					Constants.Action.ACTION_SWITCH_USER)) {
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(PreMainActivity.this);
				exitConn(account);
				AccountPersist.getInstance().setActiveAccount(
						PreMainActivity.this, new Account());
				NpcCommon.mThreeNum = "";
				Intent i = new Intent(MyApp.MAIN_SERVICE_START);
				stopService(i);
				String accountstr = SharepreferenceUtil.readString(
						PreMainActivity.this, SharepreferenceUtil.fileName,
						"account");
				String pwd2cu = SharepreferenceUtil.readString(
						PreMainActivity.this, SharepreferenceUtil.fileName,
						"pwd2cu");
				loginVideo("newyywapp" + accountstr + "@yywapp.com", pwd2cu);
			} else if (intent.getAction().equals(
					Constants.Action.SESSION_ID_ERROR)) {
				Account account = AccountPersist.getInstance()
						.getActiveAccountInfo(PreMainActivity.this);
				exitConn(account);
				AccountPersist.getInstance().setActiveAccount(
						PreMainActivity.this, new Account());
				Intent i = new Intent(MyApp.MAIN_SERVICE_START);
				stopService(i);
				String accountstr = SharepreferenceUtil.readString(
						PreMainActivity.this, SharepreferenceUtil.fileName,
						"account");
				String pwd2cu = SharepreferenceUtil.readString(
						PreMainActivity.this, SharepreferenceUtil.fileName,
						"pwd2cu");
				loginVideo("newyywapp" + accountstr + "@yywapp.com", pwd2cu);
			} else if (intent.getAction().equals(Constants.Action.ACTION_EXIT)) {
				/**
				 * 屏蔽退出确认弹出框
				 */
			} else if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {

			} else if (intent.getAction().equals(Constants.Action.RECEIVE_MSG)) {
				/**
				 * 暂时屏蔽接收消息
				 */
				/*
				 * int result = intent.getIntExtra("result", -1); String msgFlag
				 * = intent.getStringExtra("msgFlag");
				 * if(result==Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS){
				 * DataManager.updateMessageStateByFlag(MainActivity.this,
				 * msgFlag, Constants.MessageType.SEND_SUCCESS); Intent refresh
				 * = new Intent();
				 * refresh.setAction(MessageActivity.REFRESH_MESSAGE);
				 * sendBroadcast(refresh); }else{
				 * DataManager.updateMessageStateByFlag(MainActivity.this,
				 * msgFlag, Constants.MessageType.SEND_FAULT); Intent refresh =
				 * new Intent();
				 * refresh.setAction(MessageActivity.REFRESH_MESSAGE);
				 * sendBroadcast(refresh); }
				 */

			} else if (intent.getAction().equals(
					Constants.Action.RECEIVE_SYS_MSG)) {

			} else if (intent.getAction()
					.equals(Constants.Action.ACTION_UPDATE)) {
				/**
				 * 检测更新屏蔽
				 */
			}
		}
	};

	private void exitConn(final Account account) {
		ExitTask.startTask(PreMainActivity.this, account, new ExitCallBack() {

			@Override
			public void onPostExecute(int result) {
				// TODO Auto-generated method stub
				switch (result) {
				case NetManager.CONNECT_CHANGE:
					exitConn(account);
					return;
				default:
					break;
				}
			}
		});
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		advviewpager.removeAllViews();
		mainAdvPagerAdapter = new MainAdvPagerAdapter(PreMainActivity.this,
				advviewpager, companies);
		advviewpager.setAdapter(mainAdvPagerAdapter);
		initDots();
	}

	private void initDots() {
		// TODO Auto-generated method stub
		dotList.clear();
		advll_dot.removeAllViews();
		dotPosition = -1;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ContextUtil.dip2px(PreMainActivity.this, 8),
				ContextUtil.dip2px(PreMainActivity.this, 8));
		lp.setMargins(ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0,
				ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0);
		int count = companies.size();
		int size = 0;
		if (count % 6 == 0) {
			size = count / 6;
		} else {
			size = count / 6 + 1;
		}
		for (int i = 0; i < size; i++) {
			ImageView iv_dot = new ImageView(PreMainActivity.this);
			iv_dot.setLayoutParams(lp);
			iv_dot.setBackgroundResource(R.drawable.app_dot_normal);
			if (i == 0) {
				iv_dot.setBackgroundResource(R.drawable.app_dot_focused);
			}
			advll_dot.addView(iv_dot);
			dotList.add(iv_dot);
		}
	}

	private void getCompanyAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("pageid", "1");
		cellComAjaxParams.put("dpitype", dpitype);
		HttpHelper.getInstances(PreMainActivity.this).send(
				FlowConsts.YYW_GETAD_NAEMED, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						CompanyAdvComm companyAdvComm = cellComAjaxResult.read(
								CompanyAdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(companyAdvComm
								.getReturnCode())) {
							Toast.makeText(PreMainActivity.this,
									companyAdvComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						companies = companyAdvComm.getBody();
						initAdapter();
					}
				});
	}

	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("pos", "4");
		HttpHelper.getInstances(PreMainActivity.this).send(
				FlowConsts.YYW_GETGG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(PreMainActivity.this,
									advComm.getReturnMessage(),
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
				ContextUtil.dip2px(PreMainActivity.this, 8),
				ContextUtil.dip2px(PreMainActivity.this, 8));
		ll.setMargins(ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0,
				ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0);
		dots = new ArrayList<View>();
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = PreMainActivity.this.getLayoutInflater().inflate(
						R.layout.os_main_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setAdjustViewBounds(true);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, advs.get(i).getMeitiurl());
				final int tempPos = i;
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!TextUtils.isEmpty(advs.get(tempPos).getLinkurl())) {
							Intent intent = new Intent(PreMainActivity.this,
									WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos)
									.getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(PreMainActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(
							ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0,
							ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(PreMainActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(
							ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0,
							ContextUtil.dip2px(PreMainActivity.this, 1.5f), 0);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			exitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 退出
	private void exitDialog() {
		ShowAlertDialog("温馨提示", "真的要走吗?再看看吧！", new MyDialogInterface() {

			@Override
			public void onClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				MobclickAgent.onKillProcess(PreMainActivity.this);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (finalBitmap != null) {
			finalBitmap.onDestroy();
		}
		if (isRegFilter) {
			isRegFilter = false;
			this.unregisterReceiver(mReceiver);
		}
		Account account = AccountPersist.getInstance()
				.getActiveAccountInfo(PreMainActivity.this);
		exitConn(account);
		AccountPersist.getInstance().setActiveAccount(
				PreMainActivity.this, new Account());
		NpcCommon.mThreeNum = "";
		Intent i = new Intent(MyApp.MAIN_SERVICE_START);
		stopService(i);
	}
}
