package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;

import org.afinal.simplecache.NetUtils;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.LoginComm;
import osprey_adphone_hn.cellcom.com.cn.bean.RegisterComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AESEncoding;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.RegExpValidator;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.SlipButton;
import osprey_adphone_hn.cellcom.com.cn.widget.SlipButton.OnChangedListener;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.db.SharedPreferencesManager;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.thread.LoginTask;
import p2p.cellcom.com.cn.thread.LoginTask.LoginCallBack;
import p2p.cellcom.com.cn.thread.RegisterTask;
import p2p.cellcom.com.cn.thread.RegisterTask.RegisterCallBack;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;

/**
 * 登陆
 * 
 * @author ma
 * 
 */
public class LoginActivity extends ActivityFrame {
	// private InputMethodRelativeLayout loginrl;
	// private InputMethodManager inputMethodManager;
	private EditText countet;
	private EditText pwdet;
	private TextView forgetpwdtv;
	private SlipButton rememberpwd;
	private Button loginbtn, registerbtn;

	private boolean isremeberpwd;
	private boolean isautologin;
	private String remeberpwdstr;
	private final int RegisterCode = 1000;
	private final int ForgetPwdCode = 1111;

	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FinalBitmap finalBitmap;
	private JazzyViewPager mJazzy;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem;
	private ArrayList<View> view_img;
	private FrameLayout fl_ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendTitleBody();
		HideSet();
		AppendMainBody(R.layout.app_welcom_login);
		isShowSlidingMenu(false);
		InitView();
		InitListeners();
		InitData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
	}

	private void InitData() {
		// TODO Auto-generated method stub
		// inputMethodManager = (InputMethodManager)
		// getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
		remeberpwdstr = SharepreferenceUtil.readString(LoginActivity.this,
				SharepreferenceUtil.fileName, "remeberpwd");
		String pwd = "";
		try {
			pwd = AESEncoding.Decrypt(SharepreferenceUtil.readString(
					LoginActivity.this, SharepreferenceUtil.fileName, "pwd"),
					FlowConsts.key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String acount = SharepreferenceUtil.readString(LoginActivity.this,
				SharepreferenceUtil.fileName, "account");
		String benjiaccount = SharepreferenceUtil.readString(
				LoginActivity.this, SharepreferenceUtil.fileName,
				"benjiaccount");
		SharepreferenceUtil.write(LoginActivity.this,
				SharepreferenceUtil.fileName, "accounthistory", acount);
		if (TextUtils.isEmpty(remeberpwdstr)) {
			rememberpwd.setCheck(true);
			isremeberpwd = true;
		}
		// if(!TextUtils.isEmpty(benjiaccount)){
		// registerbtn.setVisibility(View.GONE);
		// }else{
		// registerbtn.setVisibility(View.VISIBLE);
		// }
		if ("Y".equalsIgnoreCase(remeberpwdstr)) {
			countet.setText(acount);
			pwdet.setText(pwd);
			rememberpwd.setCheck(true);
			isremeberpwd = true;
		}
		if (rememberpwd.isSelected()) {
			countet.setText(acount);
			pwdet.setText(pwd);
			rememberpwd.setCheck(true);
			isremeberpwd = true;
		}

		forgetpwdtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		finalBitmap = FinalBitmap.create(this);
//		if (ContextUtil.getHeith(this) <= 480) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(LoginActivity.this, 100);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 800) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(LoginActivity.this, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(this, 150);
//			fl_ad.setLayoutParams(linearParams);
//		}else if (ContextUtil.getHeith(this) <= 960) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(LoginActivity.this, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(LoginActivity.this, 220);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(LoginActivity.this, 220);
//			fl_ad.setLayoutParams(linearParams);
//		}
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
            .getLayoutParams();
        linearParams.width=ContextUtil.getWidth(LoginActivity.this);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(LoginActivity.this, finalBitmap, mJazzy, R.drawable.os_login_topicon);
		getAdv();
	}

	private void InitView() {
		// TODO Auto-generated method stub
		// loginrl = (InputMethodRelativeLayout) findViewById(R.id.loginrl);
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		countet = (EditText) findViewById(R.id.countet);
		pwdet = (EditText) findViewById(R.id.pwdet);
		rememberpwd = (SlipButton) findViewById(R.id.rememberpwd);
		loginbtn = (Button) findViewById(R.id.loginbtn);
		registerbtn = (Button) findViewById(R.id.registerbtn);
		forgetpwdtv = (TextView) findViewById(R.id.tv_forget_pwd);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
		fl_ad = (FrameLayout) findViewById(R.id.fl_ad);
	}

	private void InitListeners() {
		// TODO Auto-generated method stub
		registerbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, RegisterCode);
			}
		});
		loginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String countettxt = countet.getText().toString();
				String pwdettxt = pwdet.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg("请输入帐号");
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg("手机号码格式错误");
					return;
				}
				if (TextUtils.isEmpty(pwdettxt)) {
					ShowMsg("请输入密码");
					return;
				}
				login(countettxt, pwdettxt);
			}
		});
		forgetpwdtv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				forgetpwdtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
				forgetpwdtv.setTextColor(Color.BLUE);
				Intent intent = new Intent(LoginActivity.this,
						ForgetPwdActivity.class);
				startActivityForResult(intent, ForgetPwdCode);
			}
		});
		rememberpwd.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				isremeberpwd = CheckState;
			}
		});
		// loginrl.setOnSizeChangedListener(new OnSizeChangedListener() {
		//
		// @Override
		// public void onSizeChange(boolean flag) {
		// // TODO Auto-generated method stub
		// if (flag) {
		// if (ContextUtil.getHeith(LoginActivity.this) <= 480) {
		// loginrl.setPadding(0, -ContextUtil.dip2px(LoginActivity.this, 100),
		// 0, 0);
		// } else if (ContextUtil.getHeith(LoginActivity.this) <= 800) {
		// loginrl.setPadding(0, -ContextUtil.dip2px(LoginActivity.this, 140),
		// 0, 0);
		// } else if (ContextUtil.getHeith(LoginActivity.this) <= 900) {
		// loginrl.setPadding(0, -ContextUtil.dip2px(LoginActivity.this, 140),
		// 0, 0);
		// } else if (ContextUtil.getHeith(LoginActivity.this) <= 1280) {
		// loginrl.setPadding(0, -ContextUtil.dip2px(LoginActivity.this, 240),
		// 0, 0);
		// } else {
		// loginrl.setPadding(0, -ContextUtil.dip2px(LoginActivity.this, 240),
		// 0, 0);
		// }
		// } else
		// loginrl.setPadding(0, 0, 0, 0);
		// }
		// });
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// if (event.getAction() == MotionEvent.ACTION_UP) {
	// inputMethodManager.hideSoftInputFromWindow(getWindow()
	// .getDecorView().getWindowToken(), 0);
	// }
	// return super.onTouchEvent(event);
	// }

	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", "");
		cellComAjaxParams.put("pos", "1");
		HttpHelper.getInstances(LoginActivity.this).send(FlowConsts.YYW_GETGG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(LoginActivity.this,
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
				ContextUtil.dip2px(this, 8), ContextUtil.dip2px(this, 8));
		ll.setMargins(ContextUtil.dip2px(this, 1.5f), 0,
				ContextUtil.dip2px(this, 1.5f), 0);
		dots = new ArrayList<View>();
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = LoginActivity.this.getLayoutInflater().inflate(
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
							Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(LoginActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(
							ContextUtil.dip2px(LoginActivity.this, 1.5f), 0,
							ContextUtil.dip2px(LoginActivity.this, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(LoginActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(
							ContextUtil.dip2px(LoginActivity.this, 1.5f), 0,
							ContextUtil.dip2px(LoginActivity.this, 1.5f), 0);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RegisterCode) {
			if (resultCode == RESULT_OK) {
				String acount = data.getStringExtra("account");
				String pwd = data.getStringExtra("pwd");
				countet.setText(acount);
				pwdet.setText(pwd);
			}
		}
		if (requestCode == ForgetPwdCode) {
			forgetpwdtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
			if (resultCode == RESULT_OK) {
				String acount = data.getStringExtra("account");
				String pwd = data.getStringExtra("pwd");
				countet.setText(acount);
				pwdet.setText(pwd);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			exitDialog();
		}
		return false;
	}

	// 登录
	private void login(final String account, final String pwd) {
		try {
			SharepreferenceUtil.write(LoginActivity.this,
					SharepreferenceUtil.fileName, "temppwd",
					AESEncoding.Encrypt(pwd, FlowConsts.key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("passwd", ContextUtil.encodeMD5(pwd));
		HttpHelper.getInstances(LoginActivity.this).send(FlowConsts.YYW_LOGIN,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.hsc_progress);
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
						LoginComm loginComm = arg0.read(LoginComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = loginComm.getReturnCode();
						String msg = loginComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							DismissProgressDialog();
							ShowMsg(msg);
							return;
						}
						try {
							SharepreferenceUtil.write(LoginActivity.this,
									SharepreferenceUtil.fileName, "pwd",
									AESEncoding.Encrypt(pwd, FlowConsts.key));
							SharepreferenceUtil.write(LoginActivity.this,
									SharepreferenceUtil.fileName, "account",
									account);
							SharepreferenceUtil.write(LoginActivity.this,
									SharepreferenceUtil.fileName, "remeberpwd",
									isremeberpwd ? "Y" : "N");
							SharepreferenceUtil.write(LoginActivity.this,
									SharepreferenceUtil.fileName, "autologin",
									isautologin ? "Y" : "N");
							if (loginComm.getBody().size() > 0) {
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName, "uid",
										loginComm.getBody().get(0).getUid());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName,
										"headpicurl", loginComm.getBody()
												.get(0).getHeadpicurl());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName, "huafei",
										loginComm.getBody().get(0).getHuafei());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName, "jifen",
										loginComm.getBody().get(0).getJifen());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName,
										"usermom", loginComm.getBody().get(0)
												.getUsermom());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName,
										"username", loginComm.getBody().get(0)
												.getUsername());
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName,
										"yinyuan", loginComm.getBody().get(0)
												.getYinyuan());
								String cuemail = loginComm.getBody().get(0)
										.getCuemail();
								SharepreferenceUtil.write(LoginActivity.this,
										SharepreferenceUtil.fileName,
										"cuemail", cuemail);
								String pwd2cu = SharepreferenceUtil.readString(
										LoginActivity.this,
										SharepreferenceUtil.fileName, "pwd2cu");
								cellcom.com.cn.util.SharepreferenceUtil
										.saveData(LoginActivity.this,
												new String[][] { {
														"token",
														loginComm.getBody()
																.get(0)
																.getToken() } });
								
								String cityname = loginComm.getBody().get(0)
										.getCityname();
								// if(TextUtils.isEmpty(cuemail)){
								// registerVideo("newyywapp"+account +
								// "@yywapp.com", pwd2cu);
								// }else{
								// loginVideo(cuemail, pwd2cu);
								// }
								
								if("鱼鹰".equals(cityname)){
									//开启定位
									
								}else{
									SharepreferenceUtil.write(LoginActivity.this,
											SharepreferenceUtil.fileName,
											"cityname", cityname);
								}
								//无网络也可登陆
								if(!NetUtils.isConnected(LoginActivity.this)){
									Intent loginintent = new Intent(
											LoginActivity.this, PreMainActivity.class);
									startActivity(loginintent);
									LoginActivity.this.finish();
								}else{
									loginVideo("newyywapp" + account
											+ "@yywapp.com", pwd2cu);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String yywcontact = SharepreferenceUtil.readString(
								LoginActivity.this,
								SharepreferenceUtil.fileName, "yywcontact");
						if (!"Y".equals(yywcontact)) {
//							addContact();
							SharepreferenceUtil.write(LoginActivity.this,
									SharepreferenceUtil.fileName, "yywcontact",
									"Y");
						}
					}
				});
	}

	private void loginVideo(final String account, final String pwd) {
		LoginTask.startTask(LoginActivity.this, account, pwd,
				new LoginCallBack() {

					@Override
					public void onPostExecute(LoginResult result) {
						// TODO Auto-generated method stub
						switch (Integer.parseInt(result.error_code)) {
						case NetManager.SESSION_ID_ERROR:
							DismissProgressDialog();
							ShowMsg(getResources().getString(
									R.string.os_session_id_error));
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_session_id_error));
							break;
						case NetManager.CONNECT_CHANGE:
							loginVideo(account, pwd);
							return;
						case NetManager.LOGIN_SUCCESS:
							DismissProgressDialog();
							SharedPreferencesManager
									.getInstance()
									.putData(
											LoginActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTNAME_EMAIL,
											account);
							SharedPreferencesManager
									.getInstance()
									.putData(
											LoginActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTPASS_EMAIL,
											pwd);
							SharedPreferencesManager.getInstance()
									.putRecentLoginType(LoginActivity.this,
											Constants.LoginType.EMAIL);

							String codeStr1 = String.valueOf(Long
									.parseLong(result.rCode1));
							String codeStr2 = String.valueOf(Long
									.parseLong(result.rCode2));
							Account act = AccountPersist.getInstance()
									.getActiveAccountInfo(LoginActivity.this);
							if (null == act) {
								act = new Account();
							}
							act.three_number = result.contactId;
							act.phone = result.phone;
							act.email = result.email;
							act.sessionId = result.sessionId;
							act.rCode1 = codeStr1;
							act.rCode2 = codeStr2;
							act.countryCode = result.countryCode;
							AccountPersist.getInstance().setActiveAccount(
									LoginActivity.this, act);
							NpcCommon.mThreeNum = AccountPersist.getInstance()
									.getActiveAccountInfo(LoginActivity.this).three_number;
							Intent intent = new Intent(LoginActivity.this,
									LoginAdvActivity.class);
							startActivity(intent);
							LoginActivity.this.finish();
							break;
						case NetManager.LOGIN_USER_UNEXIST:
							DismissProgressDialog();
							registerVideo(account, pwd);
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_account_no_exist));
							break;
						case NetManager.LOGIN_PWD_ERROR:
							DismissProgressDialog();
							ShowMsg(getResources().getString(
									R.string.os_password_error));
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_password_error));
							break;
						default:
							DismissProgressDialog();
							ShowMsg(getResources().getString(
									R.string.os_loginfail));
							LogMgr.showLog("loginVideo==>"
									+ getResources().getString(
											R.string.os_loginfail));
							break;
						}
					}
				});
	}

	private void registerVideo(final String email, final String pwd) {
		RegisterTask.startTask(LoginActivity.this, email, pwd,
				new RegisterCallBack() {

					@Override
					public void onPostExecute(RegisterResult result) {
						// TODO Auto-generated method stub
						switch (Integer.parseInt(result.error_code)) {
						case NetManager.SESSION_ID_ERROR:
							LogMgr.showLog("registerVideo==>"
									+ getResources().getString(
											R.string.os_session_id_error));
							break;
						case NetManager.CONNECT_CHANGE:
							registerVideo(email, pwd);
							return;
						case NetManager.REGISTER_SUCCESS:
							register2Cu(email);
							break;
						case NetManager.REGISTER_EMAIL_USED:
							LogMgr.showLog("registerVideo==>"
									+ getResources().getString(
											R.string.os_email_used));
							break;
						case NetManager.REGISTER_EMAIL_FORMAT_ERROR:
							LogMgr.showLog("registerVideo==>"
									+ getResources().getString(
											R.string.os_email_format_error));
							break;
						case NetManager.REGISTER_PASSWORD_NO_MATCH:
							LogMgr.showLog("registerVideo==>REGISTER_PASSWORD_NO_MATCH");
							break;
						default:
							LogMgr.showLog("registerVideo==>"
									+ getResources().getString(
											R.string.os_operator_error));
							break;
						}
					}
				});
	}

	public void register2Cu(final String countettxt) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", "");
		cellComAjaxParams.put("email", countettxt);
		HttpHelper.getInstances(LoginActivity.this).send(FlowConsts.YYW_REG2CU,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						RegisterComm registerComm = arg0.read(
								RegisterComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = registerComm.getReturnCode();
						String msg = registerComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							// ShowMsg(msg);
							return;
						}
						String pwd2cu = SharepreferenceUtil.readString(
								LoginActivity.this,
								SharepreferenceUtil.fileName, "pwd2cu");
						loginVideo(countettxt, pwd2cu);
					}
				});
	}

	// 查询指定电话的联系人姓名，邮箱
	public boolean existContactByNumber() throws Exception {
		boolean isexist = false;
		String number = "02083931104";
		Uri uri = Uri
				.parse("content://com.android.contacts/data/phones/filter/"
						+ number);
		ContentResolver resolver = LoginActivity.this.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "display_name" },
				null, null, null);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(0);
			isexist = true;
		}
		cursor.close();
		return isexist;
	}

	private void addContact() {
		// TODO Auto-generated method stub
		try {
			if (!existContactByNumber()) {
				ContentValues values = new ContentValues();
				Uri rawContactUri = LoginActivity.this.getContentResolver()
						.insert(RawContacts.CONTENT_URI, values);
				long rawContactId = ContentUris.parseId(rawContactUri);
				// 加入姓名数据
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
				values.put(StructuredName.DISPLAY_NAME, "鱼鹰网");
				LoginActivity.this.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
				// 加入头像数据
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
				// 从资源文件中获取头像
				Bitmap sourceBitmap = BitmapFactory.decodeResource(
						LoginActivity.this.getResources(),
						R.drawable.ic_launcher);
				// 将Bitmap压缩成PNG编码，质量为100%存储
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				byte[] b = os.toByteArray();
				values.put(Photo.PHOTO, b);
				LoginActivity.this.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
				// 加入电话数据
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				values.put(Phone.NUMBER, "02083931104");
				values.put(Phone.TYPE, Phone.TYPE_WORK);
				LoginActivity.this.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 退出
	private void exitDialog() {
		ShowAlertDialog("温馨提示", "真的要走吗?再看看吧！", new MyDialogInterface() {

			@Override
			public void onClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(R.drawable.ic_launcher);
		// builder.setTitle("确定要退出本程序吗?");
		// builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// finish();
		// System.exit(0);
		// }
		// });
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// dialog.cancel();
		// }
		// });
		// AlertDialog dialog = builder.create();
		// dialog.show();
	}

}
