package osprey_adphone_hn.cellcom.com.cn.activity.base;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.HttpHandler;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.AboutActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.DhzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.GrzlActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.ModifyPwdActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.WdddActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Sys;
import osprey_adphone_hn.cellcom.com.cn.bean.SysComm;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bus.DownLoadManager;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.RightSlidingMenu;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

public class ActivityFrame extends ActivityBase {
	private LinearLayout topll;
	private Intent intent;

	private RightSlidingMenu mMenu;
	// 侧滑菜单子项
	protected ImageView iv_user_img;
	private TextView tv_username;
	private MarqueeText tv_user_id;
	private RelativeLayout rl_grzl, rl_gwc, rl_hyy, rl_rjsj, rl_gy, rl_xgmm,
			rl_fx, rl_tcdl;
	protected TextView tv_kyhf, tv_kyyy, tv_kyjf;
	private Button btn_dhzx;
	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;
	private ProgressBar mProgress;
	private HttpHandler<File> handler = null;
	private String target;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_base_main);
		intent = getIntent();
		initView();
		initListener();
	}

	/**
	 * 初始化视图
	 * 
	 * @author Administrator
	 * 
	 */
	private void initView() {
		topll = (LinearLayout) findViewById(R.id.topll);

		mMenu = (RightSlidingMenu) findViewById(R.id.id_menu);
		iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_user_id = (MarqueeText) findViewById(R.id.tv_user_id);
		btn_dhzx = (Button) findViewById(R.id.btn_dhzx);
		rl_grzl = (RelativeLayout) findViewById(R.id.rl_grzl);
		rl_gwc = (RelativeLayout) findViewById(R.id.rl_gwc);
		rl_hyy = (RelativeLayout) findViewById(R.id.rl_hyy);
		rl_rjsj = (RelativeLayout) findViewById(R.id.rl_rjsj);
		rl_gy = (RelativeLayout) findViewById(R.id.rl_gy);
		rl_xgmm = (RelativeLayout) findViewById(R.id.rl_xgmm);
		rl_fx = (RelativeLayout) findViewById(R.id.rl_fx);
		rl_tcdl = (RelativeLayout) findViewById(R.id.rl_tcdl);
		tv_kyhf = (TextView) findViewById(R.id.tv_kyhf);
		tv_kyyy = (TextView) findViewById(R.id.tv_kyyy);
		tv_kyjf = (TextView) findViewById(R.id.tv_kyjf);

		finalBitmap = FinalBitmap.create(this);
		loadingBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.os_dhb_itempic);
		// 头像赋值
		if (!SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"headpicurl", "").equals("")
				&& (SharepreferenceUtil.readString(this,
						SharepreferenceUtil.fileName, "headpicurl", "")
						.contains(".jpg") || SharepreferenceUtil.readString(
						this, SharepreferenceUtil.fileName, "headpicurl", "")
						.contains(".png"))) {

			finalBitmap.display(iv_user_img, SharepreferenceUtil.readString(
					this, SharepreferenceUtil.fileName, "headpicurl", ""));
		}
		// 账号
		tv_username.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "username", ""));
		tv_user_id.setText("账号："
				+ SharepreferenceUtil.readString(this,
						SharepreferenceUtil.fileName, "account", ""));
		tv_kyhf.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "huafei", ""));
		tv_kyyy.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "yinyuan", ""));
		tv_kyjf.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "jifen", ""));
		// 账号
		tv_username.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "username", ""));
		tv_user_id.setText("账号："
				+ SharepreferenceUtil.readString(this,
						SharepreferenceUtil.fileName, "account", ""));
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"huafei", "").equals("")) {
			tv_kyhf.setText("￥0");
		} else {
			tv_kyhf.setText("￥"
					+ SharepreferenceUtil.readString(this,
							SharepreferenceUtil.fileName, "huafei", ""));
		}
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"yinyuan", "").equals("")) {
			tv_kyyy.setText("0个");
		} else {
			tv_kyyy.setText(SharepreferenceUtil.readString(this,
					SharepreferenceUtil.fileName, "yinyuan", "") + "个");
		}
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"jifen", "").equals("")) {
			tv_kyjf.setText("0分");
		} else {
			tv_kyjf.setText(SharepreferenceUtil.readString(this,
					SharepreferenceUtil.fileName, "jifen", "") + "分");
		}
	}

	/**
	 * 初始化监听
	 * 
	 * @author Administrator
	 * 
	 */
	private void initListener() {
		// 修改密码
		rl_xgmm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(ModifyPwdActivity.class);
			}
		});
		// 个人资料
		rl_grzl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!CommonUtils.getCurrentChildMenuActivity().equals("grzl")) {
					if (!CommonUtils.getCurrentChildMenuActivity().equals("")) {
						finish();
					}
					CommonUtils.setCurrentChildMenuActivity("grzl");
					Intent intent = new Intent(ActivityFrame.this,
							GrzlActivity.class);
					startActivityForResult(intent, 1234);
				} else {
					mMenu.toggle();
				}
			}
		});

		// 购物车
		rl_gwc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!CommonUtils.getCurrentChildMenuActivity().equals("gwc")) {
					if (!CommonUtils.getCurrentChildMenuActivity().equals("")) {
						finish();
					}
					CommonUtils.setCurrentChildMenuActivity("gwc");
					Intent intent = new Intent(ActivityFrame.this,
							WdddActivity.class);
					startActivity(intent);
				} else {
					mMenu.toggle();
				}
			}
		});

		// 欢迎页
		rl_hyy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!CommonUtils.getCurrentChildMenuActivity().equals("hyy")) {
					if (!CommonUtils.getCurrentChildMenuActivity().equals("")) {
						finish();
					}
					CommonUtils.setCurrentChildMenuActivity("hyy");
//					Intent intent = new Intent(ActivityFrame.this,
//							HyyActivity.class);
//					startActivity(intent);
					Intent intent = new Intent(ActivityFrame.this,
                      WebViewActivity.class);
                    intent.putExtra("url", "http://dianliangtech.com/help/app");
                     startActivity(intent);
				} else {
					mMenu.toggle();
				}
			}
		});

		// 软件升级
		rl_rjsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getsysparam();
			}
		});

		// 关于鱼鹰
		rl_gy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!CommonUtils.getCurrentChildMenuActivity().equals("gyyy")) {
					if (!CommonUtils.getCurrentChildMenuActivity().equals("")) {
						finish();
					}
					CommonUtils.setCurrentChildMenuActivity("gyyy");
					Intent intent = new Intent(ActivityFrame.this,
							AboutActivity.class);
					startActivity(intent);
				} else {
					mMenu.toggle();
				}
			}
		});

		// 兑换中心
		btn_dhzx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!CommonUtils.getCurrentChildMenuActivity().equals("dhzx")) {
					if (!CommonUtils.getCurrentChildMenuActivity().equals("")) {
						finish();
					}
					CommonUtils.setCurrentChildMenuActivity("dhzx");
					Intent intent = new Intent(ActivityFrame.this,
							DhzxActivity.class);
					startActivity(intent);
				} else {
					mMenu.toggle();
				}
			}
		});
		// 分享
		rl_fx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showShare();
			}
		});
		// 退出登录
		rl_tcdl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(ActivityFrame.this,
				 LoginActivity.class);
				 startActivity(intent);

//				Intent mIntent = new Intent();
//				mIntent.setClass(ActivityFrame.this, LoginActivity.class);
//				// 这里设置flag还是比较 重要的
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(mIntent);
				ActivityFrame.this.finish();
			}
		});
	}

	private void getsysparam() {
		HttpHelper.getInstances(this).send(FlowConsts.YYW_GETSYSDATA,
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
						Toast.makeText(ActivityFrame.this, strMsg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						SysComm sysComm = cellComAjaxResult.read(SysComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(sysComm.getReturnCode())) {
							Toast.makeText(ActivityFrame.this,
									sysComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (sysComm.getBody().size() > 0) {
							Sys sys = sysComm.getBody().get(0);
							String serviceUrl = sys.getServiceurl();
							String service = sys.getService();
							String downloadurl = sys.getDownurl();
							String version = sys.getVersion();
							String minversion = sys.getMinversion();
							String startgg = sys.getStartgg();
							String key = sys.getKey();
							String pwd2cu = sys.getPwd2cu();
							String kfphone = sys.getKfphone();
							Double oldversion = Double.parseDouble(ContextUtil
									.getAppVersionName(ActivityFrame.this)[0]);
							FlowConsts.refleshIp(serviceUrl);
							SharepreferenceUtil.write(ActivityFrame.this,
									SharepreferenceUtil.fileName,
									"downloadurl", downloadurl);
							SharepreferenceUtil.write(ActivityFrame.this,
									SharepreferenceUtil.fileName, "startgg",
									startgg);
							SharepreferenceUtil.write(ActivityFrame.this,
									SharepreferenceUtil.fileName, "pwd2cu",
									pwd2cu);
							SharepreferenceUtil.write(ActivityFrame.this,
									SharepreferenceUtil.fileName, "kfphone",
									kfphone);
							if (Double.parseDouble(minversion) > oldversion) {
								// 强制升级
							} else if (Double.parseDouble(version) > oldversion) {
								showUpgradeTip("");
							} else {
								ShowMsg("当前已是最新版本");
							}
						}
					}
				});
	}

	/**
	 * 提示升级提示框
	 * 
	 * @param upgrademsg
	 */
	public void showUpgradeTip(String upgrademsg) {
		String pTitle = "升级提示";
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityFrame.this);
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
			}
		});

		builder.show();
	}

	/**
	 * 弹出更新提示框
	 */
	private void showCustomMessageOK() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityFrame.this);
		builder.setTitle("更新");
		final LayoutInflater inflater = LayoutInflater.from(ActivityFrame.this);
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
		String url = SharepreferenceUtil.readString(ActivityFrame.this,
				SharepreferenceUtil.fileName, "downloadurl");
		target = updateManager.createApkTarget(ActivityFrame.this);

		if (TextUtils.isEmpty(target)) {
			Uri myBlogUri = null;
			if (url == null || "".equalsIgnoreCase(url)) {
				url = "http://219.137.26.249:8087/ospreynew/apk/osprey_hn.apk";
			}
			LogMgr.showLog("down from utl=" + url);
			myBlogUri = Uri.parse(url);
			Intent returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri);
			ActivityFrame.this.startActivity(returnIt);
			ActivityFrame.this.finish();
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
			handler = updateManager.downLoadApk(ActivityFrame.this, url,
					target, true, new NetCallBack<File>() {

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
							ActivityFrame.this.finish();
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

	/**
	 * 分享
	 */
	private void showShare() {
		// TODO Auto-generated method stub
		String downloadurl=SharepreferenceUtil.readString(ActivityFrame.this,SharepreferenceUtil.fileName,
				"downloadurl");
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/jpeg");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"欢迎加入鱼鹰，看广告赚话费银元，免费兑换商品，照顾家车安全。请点击下载：http://t.cn/Rw3DqXV");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, "好友分享"));
	}

	private class OnBackListener implements android.view.View.OnClickListener {
		public void onClick(View view) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			setResult(RESULT_CANCELED, intent);
			finish();
		}
	}

	public class onSetListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mMenu.toggle();
		}
	}

	/**
	 * 包含鱼鹰网logo和设置操作顶部栏
	 */
	protected void AppendTitleBody() {
		topll.removeAllViews();
		topll.setVisibility(View.VISIBLE);
		View _View = LayoutInflater.from(this).inflate(R.layout.app_base_top,
				null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		topll.addView(_View, _LayoutParams);
		findViewById(R.id.llAppSet).setOnClickListener(new onSetListener());
	}

	/**
	 * 包含返回和设置操作顶部栏
	 */
	protected void AppendTitleBody1() {
		topll.removeAllViews();
		topll.setVisibility(View.VISIBLE);
		View _View = LayoutInflater.from(this).inflate(R.layout.app_base_top1,
				null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		topll.addView(_View, _LayoutParams);
		findViewById(R.id.llAppBack).setOnClickListener(new OnBackListener());
		findViewById(R.id.llAppSet).setOnClickListener(new onSetListener());
	}

	/**
	 * 包含返回和设置操作顶部栏
	 */
	protected void AppendTitleBody2() {
		topll.removeAllViews();
		topll.setVisibility(View.VISIBLE);
		View _View = LayoutInflater.from(this).inflate(R.layout.app_base_top2,
				null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		topll.addView(_View, _LayoutParams);
		findViewById(R.id.llAppBack).setOnClickListener(new OnBackListener());
	}

	/**
	 * 包含返回和设置操作顶部栏
	 */
	protected void AppendTitleBody3() {
		topll.removeAllViews();
		topll.setVisibility(View.VISIBLE);
		View _View = LayoutInflater.from(this).inflate(R.layout.app_base_top3,
				null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		topll.addView(_View, _LayoutParams);
		findViewById(R.id.llAppBack).setOnClickListener(new OnBackListener());
	}

	/**
	 * 包含返回和设置操作顶部栏
	 */
	protected void AppendTitleBody4() {
		topll.removeAllViews();
		topll.setVisibility(View.VISIBLE);
		View _View = LayoutInflater.from(this).inflate(R.layout.app_base_top4,
				null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		topll.addView(_View, _LayoutParams);
		findViewById(R.id.llAppBack).setOnClickListener(new OnBackListener());
	}

	protected void SetTopBarTitle(String pText) {
		// String _Title = FormatResString(pRestID);
		TextView _tvTitle = (TextView) findViewById(R.id.tvTopTitle);
		// Typeface typeFace =Typeface.createFromAsset(getAssets(),"kt.ttf");
		// _tvTitle.setTypeface(typeFace);
		_tvTitle.setText(pText);
		// ImageView _ImageView = (ImageView) findViewById(R.id.ivBottomIcon);
		// _ImageView.setImageResource(R.drawable.account_book_32x32);
	}

	// protected void HideIcon() {
	// // TODO Auto-generated method stub
	// ivicon.setVisibility(View.GONE);
	// }
	// protected void ShowIcon() {
	// // TODO Auto-generated method stub
	// ivicon.setVisibility(View.VISIBLE);
	// }

	protected void HideupdateSet() {
		// TODO Auto-generated method stub
		if (findViewById(R.id.updatell) != null) {
			findViewById(R.id.updatell).setVisibility(View.GONE);
		}
	}

	protected void HideSet() {
		// TODO Auto-generated method stub
		if (findViewById(R.id.llAppSet) != null) {
			findViewById(R.id.llAppSet).setVisibility(View.GONE);
		}
	}

	protected void ShowSet() {
		// TODO Auto-generated method stub
		if (findViewById(R.id.llAppSet) != null) {
			findViewById(R.id.llAppSet).setVisibility(View.VISIBLE);
		}
	}

	protected void HideHeadBar() {
		// TODO Auto-generated method stub
		topll.setVisibility(View.GONE);
	}

	protected void ShowHeadBar() {
		// TODO Auto-generated method stub
		topll.setVisibility(View.VISIBLE);
	}

	// protected void HideTitleBackButton()
	// {
	// findViewById(R.id.ivAppBack).setVisibility(View.INVISIBLE);
	// }
	// protected void HideFooter() {
	// findViewById(R.id.IncludeBottom).setVisibility(View.GONE);
	// }
	//
	// protected void ShowFooter(){
	// findViewById(R.id.IncludeBottom).setVisibility(View.VISIBLE);
	// }

	protected void AppendMainBody(int pResID) {
		LinearLayout _MainBody = (LinearLayout) findViewById(GetMainBodyLayoutID());

		View _View = LayoutInflater.from(this).inflate(pResID, null);
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		_MainBody.addView(_View, _LayoutParams);
		// _View.setPadding(15,15,15,15);
	}

	protected void AppendMainBody(View pView) {
		LinearLayout _MainBody = (LinearLayout) findViewById(GetMainBodyLayoutID());
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		_MainBody.addView(pView, _LayoutParams);
		// _View.setPadding(15,15,15,15);
	}

	private int GetMainBodyLayoutID() {
		return R.id.layMainBody;
	}

	/**
	 * 是否允许滑动显示侧面菜单
	 */
	protected void isShowSlidingMenu(boolean isShow) {
		mMenu.setSliding(isShow);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				getGrzl();
			}
		}
	}

	// 获取个人资料
	private void getGrzl() {
		uid = SharepreferenceUtil.readString(ActivityFrame.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(ActivityFrame.this).send(
				FlowConsts.YYW_USERINFO, cellComAjaxParams,
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
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						initValue(userInfoComm.getBody(), uid);
					}
				});
	}

	protected void initValue(UserInfo userInfo, String uid) {
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "headpicurl",
				userInfo.getHeadpicurl());
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "huafei", userInfo.getHuafei());
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "jifen", userInfo.getJifen());
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "usermom", userInfo.getUsermom());
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "username",
				userInfo.getUsername());
		SharepreferenceUtil.write(ActivityFrame.this,
				SharepreferenceUtil.fileName, "yinyuan", userInfo.getYinyuan());

		// 头像赋值
		if (!SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"headpicurl", "").equals("")
				&& (SharepreferenceUtil.readString(this,
						SharepreferenceUtil.fileName, "headpicurl", "")
						.contains(".jpg") || SharepreferenceUtil.readString(
						this, SharepreferenceUtil.fileName, "headpicurl", "")
						.contains(".png"))) {

			finalBitmap.display(iv_user_img, SharepreferenceUtil.readString(
					this, SharepreferenceUtil.fileName, "headpicurl", ""));
		}

		if (userInfo.getViplevel() != null
				&& !userInfo.getViplevel().trim().equals("")
				&& !userInfo.getViplevel().trim().equals("0")) {

			SharepreferenceUtil.write(this, SharepreferenceUtil.fileName,
					"viplevel" + uid, userInfo.getViplevel());
		}
		// 账号
		tv_username.setText(SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "username", ""));
		tv_user_id.setText("账号："
				+ SharepreferenceUtil.readString(this,
						SharepreferenceUtil.fileName, "account", ""));
	}

}
