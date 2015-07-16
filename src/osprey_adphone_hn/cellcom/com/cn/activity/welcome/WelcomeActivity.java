package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.HttpHandler;

import org.afinal.simplecache.NetUtils;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.HyyActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.LoginComm;
import osprey_adphone_hn.cellcom.com.cn.bean.RegisterComm;
import osprey_adphone_hn.cellcom.com.cn.bus.DownLoadManager;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.service.IndexService;
import osprey_adphone_hn.cellcom.com.cn.util.AESEncoding;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.db.SharedPreferencesManager;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.thread.LoginTask;
import p2p.cellcom.com.cn.thread.LoginTask.LoginCallBack;
import p2p.cellcom.com.cn.thread.RegisterTask;
import p2p.cellcom.com.cn.thread.RegisterTask.RegisterCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;
import com.testin.agent.TestinAgent;

/**
 * 欢迎界面
 * 
 * @author wma
 * 
 */
public class WelcomeActivity extends Activity {
	/** Called when the activity is first created. */
	private LinearLayout welcomell;
	private boolean flag = true;
	private boolean isOver = false;
	private UpdateReceiver receiver;
	private ProgressBar mProgress;
	private int progress;
	private HttpHandler<File> handler = null;
	private String target;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private PowerManager.WakeLock wakeLock;
	private FinalBitmap finalBitmap;
	private String picWelcomepath;
	private String remeberpwdstr;
	private String pwd;
	private String account;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_welcome_layout);
		connectService();
		initView();
		TestinAgent.init(WelcomeActivity.this,
				"bad3c3c6ecea66630d0653683ddbe475", "test");
		String isFirstLogin = SharepreferenceUtil.readString(
				WelcomeActivity.this, SharepreferenceUtil.fileName,
				"is_first_login");
		if (isFirstLogin == null || "".equalsIgnoreCase(isFirstLogin)) {// 首次使用
			Intent intent2 = new Intent(WelcomeActivity.this, HyyActivity.class);
			startActivityForResult(intent2, 1234);
		} else {
			initData();
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		finalBitmap = FinalBitmap.create(WelcomeActivity.this);
		remeberpwdstr = SharepreferenceUtil.readString(WelcomeActivity.this,
				SharepreferenceUtil.fileName, "remeberpwd");
		try {
			pwd = AESEncoding.Decrypt(SharepreferenceUtil.readString(
					WelcomeActivity.this, SharepreferenceUtil.fileName, "pwd"),
					FlowConsts.key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		account = SharepreferenceUtil.readString(WelcomeActivity.this,
				SharepreferenceUtil.fileName, "account");
		String picWelcomepath = SharepreferenceUtil.readString(
				WelcomeActivity.this, SharepreferenceUtil.fileName,
				"picWelcomepath");
		if (TextUtils.isEmpty(picWelcomepath)) {
			BitMapUtil.getImgOpt(WelcomeActivity.this, finalBitmap, welcomell, R.drawable.app_welcome);
//			welcomell.setBackgroundResource(R.drawable.app_welcome);
		} else {
			finalBitmap.display(welcomell, picWelcomepath);
		}
		IndexThread thread = new IndexThread();
		thread.start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		welcomell = (LinearLayout) findViewById(R.id.welcomell);
	}

	// 从资源中获取Bitmap
	public Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	/** 连接服务器 **/
	private void connectService() {
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		// // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// // 与下方代码中 PushManager.setNotificationBuilder(this, 1,
		// cBuilder)中的第二个参数对应
		// CustomPushNotificationBuilder cBuilder = new
		// CustomPushNotificationBuilder(
		// getApplicationContext(), resource.getIdentifier(
		// "notification_custom_builder", "layout", pkgName),
		// resource.getIdentifier("notification_icon", "id", pkgName),
		// resource.getIdentifier("notification_title", "id", pkgName),
		// resource.getIdentifier("notification_text", "id", pkgName));
		// cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		// cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
		// | Notification.DEFAULT_VIBRATE);
		// cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		// cBuilder.setLayoutDrawable(resource.getIdentifier(
		// "simple_notification_icon", "drawable", pkgName));
		// PushManager.setNotificationBuilder(this, 1, cBuilder);
	}

	class IndexThread extends Thread {
		@Override
		public void run() {
			Message msg;
			msg = new Message();
			msg.what = 88;
			myHandler.sendMessage(msg);
			int maxThreadCount = 0;
			while (flag) {
				for (int i = 0; i < 5; i++) {
					try {
						Thread.sleep(200);
						maxThreadCount++;
						// LogMgr.showLog("休息时间： "+maxThreadCount);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (maxThreadCount >= 300) {
					flag = false;
					if (!isOver) {
						msg = new Message();
						msg.what = 81;
						myHandler.sendMessage(msg);
					}
				}
			}
		}
	}

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 88:
				initService("load");
				break;
			case 81:
				Toast.makeText(WelcomeActivity.this, "连接超时！", Toast.LENGTH_LONG)
						.show();
				WelcomeActivity.this.finish();
				break;
			}
		}
	};

	// 启动服务
	public void initService(String flag) {
		// 自动登录继续闪屏
		if (receiver != null) {
			this.unregisterReceiver(receiver);
			receiver = null;
		}
		receiver = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("osprey_adphone_hn.cellcom.com.cn");
		this.registerReceiver(receiver, filter);

		Intent intent = new Intent(this, IndexService.class);
		intent.putExtra("flag", flag);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver != null) {
			WelcomeActivity.this.unregisterReceiver(receiver);
		}
	}

	/**
	 * 接收获取系统参数返回的结果
	 * 
	 * @author Administrator
	 */
	public class UpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			isOver = true;
			flag = false;
			int Result = intent.getIntExtra("Result", 0);
			String message = intent.getStringExtra("message");
			String upgrademsg = intent.getStringExtra("upgrademsg");
			boolean ispush = intent.getBooleanExtra("ispush", false);
			downloadPic();
			if (Result == 0) {// 系统参数成功
				SharepreferenceUtil.write(WelcomeActivity.this,
						SharepreferenceUtil.fileName, "is_first_login",
						"I am login.");
				if ("Y".equalsIgnoreCase(remeberpwdstr)) {
					login(account, pwd);
				} else {
					Intent loginintent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(loginintent);
					WelcomeActivity.this.finish();
				}
			} else if (Result == -10) {// 强制升级
				showUpgradeForce(upgrademsg);
			} else if (Result == -11) {// 提示更新
				showUpgradeTip(upgrademsg);
			} else {
				Toast.makeText(WelcomeActivity.this, "未知错误码",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 登录
	private void login(final String account, final String pwd) {
		try {
			SharepreferenceUtil.write(WelcomeActivity.this,
					SharepreferenceUtil.fileName, "temppwd",
					AESEncoding.Encrypt(pwd, FlowConsts.key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("passwd", ContextUtil.encodeMD5(pwd));
		HttpHelper.getInstances(WelcomeActivity.this).send(
				FlowConsts.YYW_LOGIN, cellComAjaxParams,
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
						LoginComm loginComm = arg0.read(LoginComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = loginComm.getReturnCode();
						String msg = loginComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							Intent loginintent = new Intent(
									WelcomeActivity.this, LoginActivity.class);
							startActivity(loginintent);
							WelcomeActivity.this.finish();
							Toast.makeText(WelcomeActivity.this, msg,
									Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							SharepreferenceUtil.write(WelcomeActivity.this,
									SharepreferenceUtil.fileName, "pwd",
									AESEncoding.Encrypt(pwd, FlowConsts.key));
							SharepreferenceUtil.write(WelcomeActivity.this,
									SharepreferenceUtil.fileName, "account",
									account);
							if (loginComm.getBody().size() > 0) {
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName, "uid",
										loginComm.getBody().get(0).getUid());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName,
										"headpicurl", loginComm.getBody()
												.get(0).getHeadpicurl());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName, "huafei",
										loginComm.getBody().get(0).getHuafei());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName, "jifen",
										loginComm.getBody().get(0).getJifen());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName,
										"usermom", loginComm.getBody().get(0)
												.getUsermom());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName,
										"username", loginComm.getBody().get(0)
												.getUsername());
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName,
										"yinyuan", loginComm.getBody().get(0)
												.getYinyuan());
								String cuemail = loginComm.getBody().get(0)
										.getCuemail();
								SharepreferenceUtil.write(WelcomeActivity.this,
										SharepreferenceUtil.fileName,
										"cuemail", cuemail);
								String pwd2cu = SharepreferenceUtil.readString(
										WelcomeActivity.this,
										SharepreferenceUtil.fileName, "pwd2cu");
								cellcom.com.cn.util.SharepreferenceUtil
										.saveData(WelcomeActivity.this,
												new String[][] { {
														"token",
														loginComm.getBody()
																.get(0)
																.getToken() } });
								// if (TextUtils.isEmpty(cuemail)) {
								// registerVideo("newyywapp" + account
								// + "@yywapp.com", pwd2cu);
								// } else {
								// loginVideo(cuemail, pwd2cu);
								// }
								// 无网络也可登陆
								if (!NetUtils.isConnected(WelcomeActivity.this)) {
									Intent loginintent = new Intent(
											WelcomeActivity.this,
											PreMainActivity.class);
									startActivity(loginintent);
									WelcomeActivity.this.finish();
								} else {
									loginVideo("newyywapp" + account
											+ "@yywapp.com", pwd2cu);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void registerVideo(final String email, final String pwd) {
		RegisterTask.startTask(WelcomeActivity.this, email, pwd,
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
		HttpHelper.getInstances(WelcomeActivity.this).send(
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
							// ShowMsg(msg);
							return;
						}
						String pwd2cu = SharepreferenceUtil.readString(
								WelcomeActivity.this,
								SharepreferenceUtil.fileName, "pwd2cu");
						loginVideo(countettxt, pwd2cu);
					}
				});
	}

	private void loginVideo(final String account, final String pwd) {
		LoginTask.startTask(WelcomeActivity.this, account, pwd,
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
											WelcomeActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTNAME_EMAIL,
											account);
							SharedPreferencesManager
									.getInstance()
									.putData(
											WelcomeActivity.this,
											SharedPreferencesManager.SP_FILE_GWELL,
											SharedPreferencesManager.KEY_RECENTPASS_EMAIL,
											pwd);
							SharedPreferencesManager.getInstance()
									.putRecentLoginType(WelcomeActivity.this,
											Constants.LoginType.EMAIL);

							String codeStr1 = String.valueOf(Long
									.parseLong(result.rCode1));
							String codeStr2 = String.valueOf(Long
									.parseLong(result.rCode2));
							Account act = AccountPersist.getInstance()
									.getActiveAccountInfo(WelcomeActivity.this);
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
									WelcomeActivity.this, act);
							NpcCommon.mThreeNum = AccountPersist.getInstance()
									.getActiveAccountInfo(WelcomeActivity.this).three_number;
							Intent loginintent = new Intent(
									WelcomeActivity.this, PreMainActivity.class);
							startActivity(loginintent);
							WelcomeActivity.this.finish();
							break;
						case NetManager.LOGIN_USER_UNEXIST:
							registerVideo(account, pwd);
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

	private void downloadPic() {
		// TODO Auto-generated method stub
		picWelcomepath = ContextUtil.initSDCardDir(WelcomeActivity.this);
		String startgg = SharepreferenceUtil.readString(WelcomeActivity.this,
				SharepreferenceUtil.fileName, "startgg");
		final String picName = startgg.substring(startgg.lastIndexOf("/") + 1);// 接口名称
		picWelcomepath = picWelcomepath + File.separator + picName;
		HttpHelper.getInstances(WelcomeActivity.this).downLoad(startgg,
				picWelcomepath, true, new NetCallBack<File>() {

					@Override
					public void onSuccess(File t) {
						// TODO Auto-generated
						// method stub
						SharepreferenceUtil.write(WelcomeActivity.this,
								SharepreferenceUtil.fileName, "picWelcomepath",
								picWelcomepath);
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
						| PowerManager.ON_AFTER_RELEASE, "MyActivity");
		wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flag = false;
		if (receiver != null) {
			this.unregisterReceiver(receiver);
			receiver = null;
		}
		if (wakeLock != null) {
			wakeLock.release();
		}
		Intent intentService = new Intent(WelcomeActivity.this,
				IndexService.class);
		stopService(intentService);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	protected Dialog ShowAlertDialog(String p_Title, String p_Message,
			final MyDialogInterface p_ClickListener) {
		final Dialog dialog = new Dialog(this, R.style.loadingdialog);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.app_alertdialog_popup, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		final TextView tv_content = (TextView) layout
				.findViewById(R.id.tv_content);
		final TextView tv_cancel = (TextView) layout
				.findViewById(R.id.tv_cancel);
		final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		tv_content.setText(p_Message);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		tv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				p_ClickListener.onClick(dialog);
				dialog.dismiss();
			}
		});

		Window w = dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		dialog.onWindowAttributesChanged(lp);
		dialog.setCanceledOnTouchOutside(true);

		dialog.setContentView(layout);
		dialog.show();

		return dialog;
	}

	/**
	 * 退出程序弹出框
	 */
	private void exitDialog() {
		ShowAlertDialog("真的要走吗?再看看吧！", "真的要走吗?再看看吧！", new MyDialogInterface() {

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
		// WelcomeActivity.this.finish();
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

	/**
	 * 提示升级提示框
	 * 
	 * @param upgrademsg
	 */
	public void showUpgradeTip(String upgrademsg) {
		String pTitle = "升级提示";
		AlertDialog.Builder builder = new AlertDialog.Builder(
				WelcomeActivity.this);
		builder.setTitle(pTitle);
		builder.setMessage(upgrademsg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showCustomMessageOK();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if ("Y".equalsIgnoreCase(remeberpwdstr)) {
					login(account, pwd);
				} else {
					Intent loginintent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					startActivity(loginintent);
					WelcomeActivity.this.finish();
				}
				// OpenActivityFinsh(LoginActivity.class);
			}
		});

		builder.show();
	}

	/**
	 * 强制升级提示框
	 * 
	 * @param upgrademsg
	 */
	public void showUpgradeForce(String upgrademsg) {
		String pTitle = "升级提示";
		AlertDialog.Builder builder = new AlertDialog.Builder(
				WelcomeActivity.this);
		builder.setTitle(pTitle);
		builder.setMessage(upgrademsg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showCustomMessageOK();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				WelcomeActivity.this.finish();
			}
		});
		builder.show();
	}

	/**
	 * 弹出更新提示框
	 */
	private void showCustomMessageOK() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				WelcomeActivity.this);
		builder.setTitle("更新");
		final LayoutInflater inflater = LayoutInflater
				.from(WelcomeActivity.this);
		View view = inflater.inflate(R.layout.app_welcome_download, null);
		TextView dialog_message = (TextView) view
				.findViewById(R.id.dialog_message);
		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		builder.setView(view);

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				handler.stop();
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.show();
		downLoadApk(dialog_message, dialog);
	}

	/**
	 * 下载apk
	 * 
	 * @param dialog_message
	 */
	private void downLoadApk(TextView dialog_message, final AlertDialog dialog) {
		final DownLoadManager updateManager = new DownLoadManager();
		String url = SharepreferenceUtil.readString(WelcomeActivity.this,
				SharepreferenceUtil.fileName, "downloadurl");
		target = updateManager.createApkTarget(WelcomeActivity.this);

		if (TextUtils.isEmpty(target)) {
			Uri myBlogUri = null;
			if (url == null || "".equalsIgnoreCase(url)) {
				url = "http://219.137.26.249:8087/ospreynew/apk/osprey_hn.apk";
			}
			LogMgr.showLog("down from utl=" + url);
			myBlogUri = Uri.parse(url);
			Intent returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri);
			WelcomeActivity.this.startActivity(returnIt);
			WelcomeActivity.this.finish();
		} else {
			LogMgr.showLog("target:" + target);
			String apkName = url.substring(url.lastIndexOf("/") + 1);// 接口名称
			target = target + apkName;
			File apkfile = new File(target);
			if (apkfile.exists()) {
				apkfile.delete();
			}
			dialog_message.setText("　正在下载最新版安装包,该包存放路径为" + target
					+ "，如在安装过程中出现问题请手动到该包存放路径中进行安装，谢谢。");
			handler = updateManager.downLoadApk(WelcomeActivity.this, url,
					target, true, new NetCallBack<File>() {
						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							if (strMsg.contains("416")) {
								installApk(target);
								dialog.dismiss();
								WelcomeActivity.this.finish();
							}
						}

						@Override
						public void onLoading(long count, long current) {
							// TODO Auto-generated method stub
							super.onLoading(count, current);
							progress = (int) (((float) current / count) * 100);
							// 更新进度
							mHandler.sendEmptyMessage(DOWN_UPDATE);
						}

						@Override
						public void onSuccess(File arg0) {
							// TODO Auto-generated method stub
							// mHandler.sendEmptyMessage(DOWN_OVER);
							installApk(target);
							dialog.dismiss();
							WelcomeActivity.this.finish();
						}
					});
		}
	}

	/**
	 * 安装指定地址(filePath)的apk
	 */
	private void installApk(String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				Object object = msg.obj;
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:

				break;
			default:
				break;
			}
		};
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				initData();
			}
		}
	};
}
