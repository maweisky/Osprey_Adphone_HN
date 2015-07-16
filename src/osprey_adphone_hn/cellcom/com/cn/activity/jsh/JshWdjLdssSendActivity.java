package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.Timer;
import java.util.TimerTask;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.thread.UDPHelper;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mediatek.elian.AirContractManager;

/**
 * 雷达搜索
 * 
 * @author Administrator
 * 
 */
public class JshWdjLdssSendActivity extends ActivityFrame {

	private ImageView iv_ldss;
	private Button btn_tdl;
	private String ssid, password;
	private AirContractManager manager;
	private UDPHelper helper;
	private Animation anin_ldss;
	private char[] mark = new char[] { '"' };
	private Timer mTimer;
	private long TimeOut;
	public Handler myhandler = new Handler();
	private int time;
	private boolean isTimerCancel = true;
	boolean isReceive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_ldss_send);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		initView();
		initListener();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_ldss = (ImageView) findViewById(R.id.iv_ldss);
		btn_tdl = (Button) findViewById(R.id.btn_tdl);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (manager != null) {
			manager.stopAirContract();
		}
		if (helper != null) {
			helper.StopListen();
		}
		if (!isTimerCancel) {
			cancleTimer();
		}
	}

	private void initListener() {
		// TODO Auto-generated method stub
		btn_tdl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JshWdjLdssSendActivity.this,
						JshWdjSearchDevicesActivity.class);
				intent.putExtra("title",
						getString(R.string.os_jsh_wdj_add_ldss));
				startActivity(intent);
				finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.os_jsh_wdj_add_ldss));
		initAnimation();
		initUDPListener();
//		sendWifiInfo();
	}

	private void initAnimation() {
		// TODO Auto-generated method stub
		anin_ldss = AnimationUtils.loadAnimation(this, R.anim.ldss_scan);
		iv_ldss.setAnimation(anin_ldss);
	}

	private void initUDPListener() {
		// TODO Auto-generated method stub
		TimeOut = 110 * 1000;
		excuteTimer();
		if (helper == null) {
			helper = new UDPHelper(9988);
		}
		helper.setCallBack(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case UDPHelper.HANDLER_MESSAGE_BIND_ERROR:
					Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
					break;
				case UDPHelper.HANDLER_MESSAGE_RECEIVE_MSG:
					Log.e("my", "HANDLER_MESSAGE_RECEIVE_MSG");
					isReceive = true;
					String deviceID = String.valueOf(msg.arg1);
					if (!TextUtils.isEmpty(deviceID) && deviceID.length() >= 6) {
						Intent intent = new Intent();
						if (FList.getInstance().isDevice(deviceID) == null) {
							intent.setClass(JshWdjLdssSendActivity.this,
									JshWdjEditActivity.class);
							intent.putExtra("deviceId", deviceID);
							startActivity(intent);
							finish();
						} else {
							ShowMsg("该设备已添加！");
							intent.setAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
							sendBroadcast(intent);
							finish();
						}
					}
					// helper.StopListen();
					// OpenActivity(JshWdjSearchDevicesActivity.class);
					break;
				}
			}

		});
		helper.StartListen();
		myhandler.postDelayed(mrunnable, TimeOut);
	}

	public Runnable mrunnable = new Runnable() {

		@Override
		public void run() {
			if (!isReceive) {
				Toast.makeText(JshWdjLdssSendActivity.this, "设置wifi失败",
						Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			}
		}
	};

	private void excuteTimer() {
		mTimer = new Timer();
		TimerTask mTask = new TimerTask() {
			@Override
			public void run() {
				if (time < 3) {
					sendWifiHandler.sendEmptyMessage(0);
				} else {
					sendWifiHandler.sendEmptyMessage(1);
				}
			}
		};
		mTimer.schedule(mTask, 500, 30 * 1000);
		isTimerCancel = false;
	}

	private Handler sendWifiHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message arg0) {
			switch (arg0.what) {
			case 0:
				time++;
				sendWifiInfo();
				break;
			case 1:
				cancleTimer();
				break;
			case 2:
				stopSendWifi();
				break;

			default:
				break;
			}
			return false;
		}
	});

	/**
	 * 停止发包
	 */
	private void stopSendWifi() {
		if (manager != null) {
			manager.stopAirContract();
		}
	}

	private void sendWifiInfo() {
		ssid = getIntent().getStringExtra("ssid");
		password = getIntent().getStringExtra("password");
		ScanResult result = (ScanResult) getIntent().getParcelableExtra(
				"wifiInfo");
		if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(password)) {
			anin_ldss.start();
			if (manager == null) {
				manager = new AirContractManager();
			}
			manager.startAirContract(ssid, password, result);
		} else {
			ShowMsg("雷达搜索失败！");
		}
		sendWifiHandler.postDelayed(stopRunnable, 20 * 1000);
	}

	public Runnable stopRunnable = new Runnable() {
		@Override
		public void run() {
			sendWifiHandler.sendEmptyMessage(2);
		}
	};

	private void cancleTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			isTimerCancel = true;
		}
	}
}
