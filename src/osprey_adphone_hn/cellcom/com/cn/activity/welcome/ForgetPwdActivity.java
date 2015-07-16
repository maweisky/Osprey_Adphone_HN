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
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.RegisterComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AESEncoding;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.RegExpValidator;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SmsUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.util.LogMgr;

/**
 * 忘记密码
 * 
 * @author ma
 * 
 */
public class ForgetPwdActivity extends ActivityFrame {
	private EditText countet;
	private EditText pwdet;
	private EditText pwdconfirmet;
	private EditText yamet;
	private Button subbtn;
	private Button yzmbtn;

	private BroadcastReceiver counterActionReceiver;

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
		AppendMainBody(R.layout.app_welcome_forgetpwd);
		isShowSlidingMenu(false);

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
							yamet.setText(yzm);
						}
						LogMgr.showLog("发送人：" + senderNumber + "  短信内容："
								+ msgTxt + "接受时间：" + receiveTime);
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

	private void InitData() {
		// TODO Auto-generated method stub
		finalBitmap = FinalBitmap.create(this);
		myCount = new MyCount(60000, 1000);
//		if (ContextUtil.getHeith(this) <= 480) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil
//					.dip2px(ForgetPwdActivity.this, 80);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 800) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(ForgetPwdActivity.this,
//					140);
//			fl_ad.setLayoutParams(linearParams);
//		}else if (ContextUtil.getHeith(this) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(this, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 960) {
//			// if(ContextUtil.getWidth(this)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(ForgetPwdActivity.this,
//					180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(this) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(ForgetPwdActivity.this,
//					220);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(ForgetPwdActivity.this,
//					220);
//			fl_ad.setLayoutParams(linearParams);
//		}
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
            .getLayoutParams();
        linearParams.width=ContextUtil.getWidth(ForgetPwdActivity.this);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(ForgetPwdActivity.this, finalBitmap, mJazzy, R.drawable.os_login_topicon);
		getAdv();
	}

	private void InitListener() {
		// TODO Auto-generated method stub
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
		// 提交
		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String countettxt = countet.getText().toString();
				String pwdettxt = pwdet.getText().toString();
				String pwdconfirmettxt = pwdconfirmet.getText().toString();
				String yamettxt = yamet.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg("请输入手机号码");
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg("手机号码格式错误");
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
				modifyPwd(countettxt, pwdettxt, yamettxt);
			}
		});
	}

	private void InitView() {
		// TODO Auto-generated method stub
		countet = (EditText) findViewById(R.id.countet);
		pwdet = (EditText) findViewById(R.id.pwdet);
		pwdconfirmet = (EditText) findViewById(R.id.pwdconfirmet);
		yamet = (EditText) findViewById(R.id.yamet);

		subbtn = (Button) findViewById(R.id.subbtn);
		yzmbtn = (Button) findViewById(R.id.yzmbtn);
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_viewpager);
		dots_ll = (LinearLayout) findViewById(R.id.ll_dot);
		fl_ad = (FrameLayout) findViewById(R.id.fl_ad);

	}
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			yzmbtn.setEnabled(true);  
			yzmbtn.setText(ForgetPwdActivity.this.getResources().getString(R.string.hsc_welcom_forgetpwdregyzmbtn));
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
	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", "");
		cellComAjaxParams.put("pos", "2");
		HttpHelper.getInstances(ForgetPwdActivity.this).send(
				FlowConsts.YYW_GETGG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(ForgetPwdActivity.this,
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
				View view = ForgetPwdActivity.this.getLayoutInflater().inflate(
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
							Intent intent = new Intent(ForgetPwdActivity.this, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(ForgetPwdActivity.this);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(
							ContextUtil.dip2px(ForgetPwdActivity.this, 1.5f),
							0,
							ContextUtil.dip2px(ForgetPwdActivity.this, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(ForgetPwdActivity.this);
					dot.setLayoutParams(ll);
					dot.setPadding(
							ContextUtil.dip2px(ForgetPwdActivity.this, 1.5f),
							0,
							ContextUtil.dip2px(ForgetPwdActivity.this, 1.5f), 0);
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
		cellComAjaxParams.put("method", "2");
		HttpHelper.getInstances(ForgetPwdActivity.this).send(
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

	public void modifyPwd(final String countettxt, final String pwdettxt,
			final String yamettxt) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", countettxt);
		cellComAjaxParams.put("passwd", ContextUtil.encodeMD5(pwdettxt));
		cellComAjaxParams.put("verifysms", yamettxt);
		cellComAjaxParams.put("method", "2");
		HttpHelper.getInstances(ForgetPwdActivity.this).send(
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
						try {
							SharepreferenceUtil.write(ForgetPwdActivity.this,
									SharepreferenceUtil.fileName, "account",
									countettxt);
							SharepreferenceUtil.write(ForgetPwdActivity.this,
									SharepreferenceUtil.fileName, "pwd",
									AESEncoding.Encrypt(pwdettxt,
											FlowConsts.key));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent intent = new Intent(ForgetPwdActivity.this,
								LoginActivity.class);
						intent.putExtra("account", countettxt);
						intent.putExtra("pwd", pwdettxt);
						setResult(RESULT_OK, intent);
						ForgetPwdActivity.this.finish();
						ShowMsg("密码修改成功");
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
			Intent intent = new Intent(ForgetPwdActivity.this,
					LoginActivity.class);
			setResult(RESULT_CANCELED, intent);
			ForgetPwdActivity.this.finish();
		}
		return false;
	}
}
