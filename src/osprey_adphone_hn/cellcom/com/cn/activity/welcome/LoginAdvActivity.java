package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

/**
 * 登陆欢迎界面
 * 
 * @author wma
 * 
 */
public class LoginAdvActivity extends ActivityFrame {
	/** Called when the activity is first created. */

	private PowerManager.WakeLock wakeLock;
	private TextView timetv;
	private String picAdvpath;
	private ImageView adviv;
	private FinalBitmap finalBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_welcome_loginlayout);
		initView();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		finalBitmap = FinalBitmap.create(LoginAdvActivity.this);
		String picAdvpath = SharepreferenceUtil.readString(
				LoginAdvActivity.this, SharepreferenceUtil.fileName,
				"picAdvpath");
		if (TextUtils.isEmpty(picAdvpath)) {
			BitMapUtil.getImgOpt(LoginAdvActivity.this, finalBitmap, adviv, R.drawable.app_loginadv_adv1);
//			adviv.setImageResource(R.drawable.app_loginadv_adv1);
		} else {
			finalBitmap.display(
					adviv,
					picAdvpath);
		}
		getAdv();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			MyCount myCount = new MyCount(3000, 1000);
			myCount.start();
			timetv.setText("03");
		}
	}

	// 从资源中获取Bitmap
	public Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	private void initView() {
		// TODO Auto-generated method stub
		timetv = (TextView) findViewById(R.id.timetv);
		adviv = (ImageView) findViewById(R.id.adviv);
	}

	private void getAdv() {
		String uid = SharepreferenceUtil.readString(LoginAdvActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("pos", "3");
		HttpHelper.getInstances(LoginAdvActivity.this).send(
				FlowConsts.YYW_GETGG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							return;
						}
						if (advComm.getBody().size() > 0) {
							picAdvpath = ContextUtil
									.initSDCardDir(LoginAdvActivity.this);
							String url = advComm.getBody().get(0).getMeitiurl();
							final String picName = url.substring(url
									.lastIndexOf("/") + 1);// 接口名称
							picAdvpath = picAdvpath + File.separator + picName;
							HttpHelper.getInstances(LoginAdvActivity.this)
									.downLoad(url, picAdvpath, true,
											new NetCallBack<File>() {

												@Override
												public void onSuccess(File t) {
													// TODO Auto-generated
													// method stub
													SharepreferenceUtil
															.write(LoginAdvActivity.this,
																	SharepreferenceUtil.fileName,
																	"picAdvpath",
																	picAdvpath);
												}
											});
						}
					}
				});
	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Intent intent = new Intent(LoginAdvActivity.this,
					PreMainActivity.class);
			startActivity(intent);
			LoginAdvActivity.this.finish();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			timetv.setText("0" + millisUntilFinished / 1000);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
		if (wakeLock != null) {
			wakeLock.release();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出程序弹出框
	 */
	private void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("确定要退出本程序吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				LoginAdvActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
