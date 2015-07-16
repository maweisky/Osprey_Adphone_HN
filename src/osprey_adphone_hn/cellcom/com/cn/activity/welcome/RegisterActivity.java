package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.RegisterComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AESEncoding;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.RegExpValidator;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SmsUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.thread.RegisterTask;
import p2p.cellcom.com.cn.thread.RegisterTask.RegisterCallBack;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;

//注册
public class RegisterActivity extends ActivityFrame {
	private EditText countet;
	private EditText pwdet;
	private EditText pwdconfirmet;
	private EditText yzmet;
	private Button subbtn;
	private Button yzmbtn;
	private TextView xytv;
	private CheckBox xieyicb;
	private BroadcastReceiver counterActionReceiver;
	private boolean xieyiCheck;
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FinalBitmap finalBitmap;
	private JazzyViewPager mJazzy;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem;
	private ArrayList<View> view_img;
	private FrameLayout fl_ad;
	private MyCount myCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendTitleBody();
		HideSet();
		AppendMainBody(R.layout.app_welcome_register);
		isShowSlidingMenu(false);
		String str = "null";
		str = str.equalsIgnoreCase("null") ? "0" : str;
		// 接收短信广播
		counterActionReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				SmsMessage msg = null;
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] pdusObj = (Object[]) bundle.get("pdus");
					for (Object p : pdusObj) {
						msg = SmsMessage.createFromPdu((byte[]) p);

						String msgTxt = msg.getMessageBody();// 得到消息的内容

						Date date = new Date(msg.getTimestampMillis());// 时间
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String receiveTime = format.format(date);
						String senderNumber = msg.getOriginatingAddress();

						if (msgTxt != null
								&& msgTxt.indexOf(SmsUtil.SMS_CONTENT) != -1) {
							int index = SmsUtil.SMS_CONTENT.length();
							String yzm = msgTxt.substring(index, index
									+ SmsUtil.SMS_YZMCD);
							yzmet.setText(yzm);
						}
						return;
					}
					return;
				}
			}
		};
		IntentFilter counterActionFilter = new IntentFilter(
				SmsUtil.SMS_RECEIVED);
		registerReceiver(counterActionReceiver, counterActionFilter);

		InitView();
		InitData();
		InitListener();
	}
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			yzmbtn.setEnabled(true);  
			yzmbtn.setText(RegisterActivity.this.getResources().getString(R.string.hsc_welcom_forgetpwdregyzmbtn));
			yzmbtn.setBackgroundResource(R.drawable.app_forgetpwd_btnbg);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int second=(int)(millisUntilFinished / 1000);
			if(second<=9){				
				yzmbtn.setText("0" + second+"秒");
			}else{
				yzmbtn.setText(""+second+"秒");
			}
		}
	}

	private void InitData() {
		// TODO Auto-generated method stub
		finalBitmap = FinalBitmap.create(this);
		myCount = new MyCount(60000, 1000);
//		if (ContextUtil.getHeith(this) <= 480) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(RegisterActivity.this, 80);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 800) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil
//					.dip2px(RegisterActivity.this, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(RegisterActivity.this) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(RegisterActivity.this, 150);
//			fl_ad.setLayoutParams(linearParams);
//		}else if (ContextUtil.getHeith(this) <= 960) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil
//					.dip2px(RegisterActivity.this, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil
//					.dip2px(RegisterActivity.this, 220);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil
//					.dip2px(RegisterActivity.this, 220);
//			fl_ad.setLayoutParams(linearParams);
//		}
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
            .getLayoutParams();
        linearParams.width=ContextUtil.getWidth(RegisterActivity.this);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(RegisterActivity.this, finalBitmap, mJazzy, R.drawable.os_login_topicon);
		getAdv();
	}

	private void InitListener() {
		// TODO Auto-generated method stub
		xieyicb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				xieyiCheck = isChecked;
			}
		});
		// 协议
		xytv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(RegisterXieYiActivity.class);
			}
		});
		// 验证码
		yzmbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String countettxt = countet.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg("请输入手机号码");
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg("手机号码格式错误");
					return;
				}
				getYzm(countettxt);
			}
		});
		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String countettxt = countet.getText().toString();
				String pwdettxt = pwdet.getText().toString();
				String pwdconfirmettxt = pwdconfirmet.getText().toString();
				String yamettxt = yzmet.getText().toString();

				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg("请输入手机号码");
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg("手机号码格式错误");
					return;
				}
				if (TextUtils.isEmpty(yamettxt)) {
					ShowMsg("请输入验证码");
					return;
				}
				if (TextUtils.isEmpty(pwdettxt)) {
					ShowMsg("请输入新密码");
					return;
				}
				if (TextUtils.isEmpty(pwdconfirmettxt)) {
					ShowMsg("请再次输入密码");
					return;
				}
				if (!pwdettxt.equals(pwdconfirmettxt)) {
					ShowMsg("两次输入密码不一致");
					return;
				}
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg("请输入验证码");
					return;
				}
				if (!xieyiCheck) {
					ShowMsg("请先阅读使用规则，并同意");
					return;
				}
				register(countettxt, pwdettxt, yamettxt);
			}
		});
	}

	private void InitView() {
		// TODO Auto-generated method stub
		countet = (EditText) findViewById(R.id.countet);
		pwdet = (EditText) findViewById(R.id.pwdet);
		pwdconfirmet = (EditText) findViewById(R.id.pwdconfirmet);
		yzmet = (EditText) findViewById(R.id.yzmet);
		xytv = (TextView) findViewById(R.id.xytv);
		subbtn = (Button) findViewById(R.id.subbtn);
		yzmbtn = (Button) findViewById(R.id.yzmbtn);
		xieyicb = (CheckBox) findViewById(R.id.xieyicb);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
		fl_ad = (FrameLayout) findViewById(R.id.fl_ad);
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
	}

	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", "");
		cellComAjaxParams.put("pos", "2");
		HttpHelper.getInstances(RegisterActivity.this).send(
				FlowConsts.YYW_GETGG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(RegisterActivity.this,
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
				View view = RegisterActivity.this.getLayoutInflater().inflate(
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
							Intent intent = new Intent(RegisterActivity.this, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(RegisterActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(
							ContextUtil.dip2px(RegisterActivity.this, 1.5f), 0,
							ContextUtil.dip2px(RegisterActivity.this, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(RegisterActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(
							ContextUtil.dip2px(RegisterActivity.this, 1.5f), 0,
							ContextUtil.dip2px(RegisterActivity.this, 1.5f), 0);
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

	public void getYzm(String acount) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", acount);
		cellComAjaxParams.put("method", "1");
		HttpHelper.getInstances(RegisterActivity.this).send(
				FlowConsts.YYW_SENDVERIFSMS, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
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
						DismissProgressDialog();
						RegisterComm registerComm = arg0.read(
								RegisterComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = registerComm.getReturnCode();
						String msg = registerComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						yzmbtn.setBackgroundResource(R.drawable.app_forgetpwdbtng);
						yzmbtn.setEnabled(false);  
						myCount.start();
						ShowMsg("验证码获取成功");
					}
				});
	}

	public void register(final String countettxt, final String pwdettxt,
			final String yamettxt) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", countettxt);
		cellComAjaxParams.put("passwd", ContextUtil.encodeMD5(pwdettxt));
		cellComAjaxParams.put("verifysms", yamettxt);
		cellComAjaxParams.put("method", "1");
		HttpHelper.getInstances(RegisterActivity.this).send(
				FlowConsts.YYW_REGISTER, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
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
						RegisterComm registerComm = arg0.read(
								RegisterComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = registerComm.getReturnCode();
						String msg = registerComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							DismissProgressDialog();
							ShowMsg(msg);
							return;
						}
						try {
							SharepreferenceUtil.write(RegisterActivity.this,
									SharepreferenceUtil.fileName, "pwd",
									AESEncoding.Encrypt(pwdettxt,
											FlowConsts.key));
							SharepreferenceUtil.write(RegisterActivity.this,
									SharepreferenceUtil.fileName, "account",
									countettxt);
							SharepreferenceUtil.write(RegisterActivity.this,
									SharepreferenceUtil.fileName,
									"benjiaccount", countettxt);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String pwd2cu=SharepreferenceUtil.readString(RegisterActivity.this,
								SharepreferenceUtil.fileName , "pwd2cu");
						registerVideo("newyywapp"+countettxt + "@yywapp.com",countettxt, pwd2cu,pwdettxt);
					}
				});
	}
	
	public void register2Cu(final String email,final String account) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", account);
		cellComAjaxParams.put("email", email);
		HttpHelper.getInstances(RegisterActivity.this).send(
				FlowConsts.YYW_REG2CU, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
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
//							ShowMsg(msg);
							return;
						}
					}
				});
	}

	private void registerVideo(final String email,final String account, final String pwd2cu, final String pwd) {
		RegisterTask.startTask(RegisterActivity.this, email, pwd2cu,
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
							registerVideo(email,account,pwd2cu,pwd);
							return;
						case NetManager.REGISTER_SUCCESS:
							register2Cu(email,account);
							DismissProgressDialog();
							Intent intent = new Intent(RegisterActivity.this,
									LoginActivity.class);
							intent.putExtra("account",account);
							intent.putExtra("pwd", pwd);
							setResult(RESULT_OK, intent);
							RegisterActivity.this.finish();
							ShowMsg("注册成功");
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(myCount!=null){
			myCount.cancel();//方法结束
		}
		this.unregisterReceiver(counterActionReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			setResult(RESULT_CANCELED, intent);
			RegisterActivity.this.finish();
		}
		return false;
	}
}
