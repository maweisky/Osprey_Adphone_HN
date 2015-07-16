package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import p2p.cellcom.com.cn.bean.AlarmRecord;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.db.DataManager;
import p2p.cellcom.com.cn.db.SharedPreferencesManager;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.global.P2PConnect;
import p2p.cellcom.com.cn.utils.MusicManger;
import p2p.cellcom.com.cn.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
/**
 * 告警
 * @author Administrator
 *
 */
public class AlarmActivity extends ActivityFrame implements OnClickListener {

	Context mContext;
	TextView monitor_btn, ignore_btn, shield_btn;
	TextView alarm_mingc_text,alarm_id_text, alarm_type_text;
	ImageView alarm_img;
	int alarm_id, alarm_type, group, item;
	boolean isSupport;
	LinearLayout layout_area_chanel;
	TextView area_text, chanel_text;
	LinearLayout alarm_input, alarm_dialog;
	TextView alarm_go;
	EditText mPassword;
	boolean isAlarm;
	boolean hasContact = false;
	boolean isRegFilter = false;
	TextView tv_info, tv_type;
	private Device device;
	private FinalBitmap finalBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (P2PConnect.isPlaying()) {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		mContext = this;
		alarm_id = getIntent().getIntExtra("alarm_id", 0);
		alarm_type = getIntent().getIntExtra("alarm_type", 0);
		isSupport = getIntent().getBooleanExtra("isSupport", false);
		group = getIntent().getIntExtra("group", 0);
		item = getIntent().getIntExtra("item", 0);
		
		setContentView(R.layout.os_jsh_alarm);
		finalBitmap=FinalBitmap.create(AlarmActivity.this);
		device = FList.getInstance().isDevice(String.valueOf(alarm_id));
		initComponent();
		regFilter();
		loadMusicAndVibrate();

		insertAlarmRecord();
	}

	public void insertAlarmRecord() {
		AlarmRecord alarmRecord = new AlarmRecord();
		alarmRecord.alarmTime = String.valueOf(System.currentTimeMillis());
		alarmRecord.deviceId = String.valueOf(alarm_id);
		alarmRecord.alarmType = alarm_type;
		alarmRecord.activeUser = NpcCommon.mThreeNum;
		if ((alarm_type == P2PValue.AlarmType.EXTERNAL_ALARM || alarm_type == P2PValue.AlarmType.LOW_VOL_ALARM)
				&& isSupport) {
			alarmRecord.group = group;
			alarmRecord.item = item;
		} else {
			alarmRecord.group = -1;
			alarmRecord.item = -1;
		}
		DataManager.insertAlarmRecord(mContext, alarmRecord);
		Intent i = new Intent();
		i.setAction(Constants.Action.REFRESH_ALARM_RECORD);
		mContext.sendBroadcast(i);
	}

	public void loadMusicAndVibrate() {
		isAlarm = true;
		int a_muteState = SharedPreferencesManager.getInstance().getAMuteState(
				MyApp.app);
		if (a_muteState == 1) {
			MusicManger.getInstance().playAlarmMusic();
		}

		int a_vibrateState = SharedPreferencesManager.getInstance()
				.getAVibrateState(MyApp.app);
		if (a_vibrateState == 1) {
			new Thread() {
				public void run() {
					while (isAlarm) {
						MusicManger.getInstance().Vibrate();
						Utils.sleepThread(100);
					}
					MusicManger.getInstance().stopVibrate();

				}
			}.start();
		}
	}

	public void initComponent() {
		monitor_btn = (TextView) findViewById(R.id.monitor_btn);
		ignore_btn = (TextView) findViewById(R.id.ignore_btn);
		shield_btn = (TextView) findViewById(R.id.shield_btn);
		alarm_id_text = (TextView) findViewById(R.id.alarm_id_text);
		alarm_type_text = (TextView) findViewById(R.id.alarm_type_text);
		alarm_go = (TextView) findViewById(R.id.alarm_go);
		alarm_mingc_text=(TextView)findViewById(R.id.alarm_mingc_text);
		tv_info = (TextView) findViewById(R.id.tv_info);
		tv_type = (TextView) findViewById(R.id.tv_type);
		alarm_go.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					alarm_go.setTextColor(mContext.getResources().getColor(
							R.color.white));
					break;
				case MotionEvent.ACTION_UP:
					alarm_go.setTextColor(mContext.getResources().getColor(
							R.color.gray));
					break;
				}
				return false;
			}
		});
		alarm_input = (LinearLayout) findViewById(R.id.alarm_input);
		alarm_img = (ImageView) findViewById(R.id.alarm_img);
		mPassword = (EditText) findViewById(R.id.password);
		mPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
		mPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
//		final AnimationDrawable anim = (AnimationDrawable) alarm_img
//				.getDrawable();
//		OnPreDrawListener opdl = new OnPreDrawListener() {
//			@Override
//			public boolean onPreDraw() {
//				anim.start();
//				return true;
//			}
//		};
//		alarm_img.getViewTreeObserver().addOnPreDrawListener(opdl);
		alarm_dialog = (LinearLayout) findViewById(R.id.alarm_dialog);
		alarm_dialog.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_right));
		alarm_id_text.setText(String.valueOf(alarm_id));

		layout_area_chanel = (LinearLayout) findViewById(R.id.layout_area_chanel);
		area_text = (TextView) findViewById(R.id.area_text);
		chanel_text = (TextView) findViewById(R.id.chanel_text);

		switch (alarm_type) {
		case P2PValue.AlarmType.EXTERNAL_ALARM:
			alarm_mingc_text.setText(device.getDeviceName());
			finalBitmap.display(alarm_img, device.getServerImgUrl());
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_allarm_type1);
			tv_type.setText(R.string.os_allarm_type);
			if (isSupport) {
				layout_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				area_text.setText(mContext.getResources().getString(
						R.string.os_area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				chanel_text.setText(mContext.getResources().getString(
						R.string.os_channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.MOTION_DECT_ALARM:
			alarm_mingc_text.setText(device.getDeviceName());
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_allarm_type2);
			tv_type.setText(R.string.os_allarm_type);
			finalBitmap.display(alarm_img, device.getServerImgUrl());
			break;
		case P2PValue.AlarmType.EMERGENCY_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_allarm_type3);
			tv_type.setText(R.string.os_allarm_type);
			break;

		case P2PValue.AlarmType.LOW_VOL_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_low_voltage_alarm);
			tv_type.setText(R.string.os_allarm_type);
			if (isSupport) {
				layout_area_chanel.setVisibility(RelativeLayout.VISIBLE);
				area_text.setText(mContext.getResources().getString(
						R.string.os_area)
						+ ":" + Utils.getDefenceAreaByGroup(mContext, group));
				chanel_text.setText(mContext.getResources().getString(
						R.string.os_channel)
						+ ":" + (item + 1));
			}
			break;
		case P2PValue.AlarmType.PIR_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_allarm_type4);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.EXT_LINE_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_allarm_type5);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.DEFENCE:
			alarm_mingc_text.setText(device.getDeviceName());
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_defence);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.NO_DEFENCE:
			alarm_mingc_text.setText(device.getDeviceName());
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_no_defence);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.BATTERY_LOW_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_battery_low_alarm);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH:
			alarm_type_text.setText(R.string.os_door_bell);
			tv_info.setText(R.string.os_visitor_messge);
			tv_type.setText(R.string.os_allarm_type);
			break;
		case P2PValue.AlarmType.RECORD_FAILED_ALARM:
			tv_info.setText(R.string.os_alarm_info);
			alarm_type_text.setText(R.string.os_record_failed);
			tv_type.setText(R.string.os_allarm_type);
			break;
		default:
			tv_info.setText(R.string.os_alarm_info);
			tv_type.setText(R.string.os_not_know);
			alarm_type_text.setText(String.valueOf(alarm_type));
			break;
		}

		alarm_go.setOnClickListener(this);
		monitor_btn.setOnClickListener(this);
		ignore_btn.setOnClickListener(this);
		shield_btn.setOnClickListener(this);
	}

	public void regFilter() {
		isRegFilter = true;
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.CHANGE_ALARM_MESSAGE);
		registerReceiver(br, filter);
	}

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.CHANGE_ALARM_MESSAGE)) {
				int alarm_id1 = intent.getIntExtra("alarm_id", 0);
				int alarm_type1 = intent.getIntExtra("alarm_type", 0);
				boolean isSupport1 = intent.getBooleanExtra("isSupport", false);
				int group1 = intent.getIntExtra("group", 0);
				int item1 = intent.getIntExtra("item", 0);
				alarm_id_text.setText(String.valueOf(alarm_id1));
				switch (alarm_type1) {
				case P2PValue.AlarmType.EXTERNAL_ALARM:
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_allarm_type1);
					tv_type.setText(R.string.os_allarm_type);
					if (isSupport1) {
						layout_area_chanel
								.setVisibility(RelativeLayout.VISIBLE);
						area_text
								.setText(mContext.getResources().getString(
										R.string.os_area)
										+ ":"
										+ Utils.getDefenceAreaByGroup(mContext,
												group1));
						chanel_text.setText(mContext.getResources().getString(
								R.string.os_channel)
								+ ":" + (item1 + 1));
					}
					break;
				case P2PValue.AlarmType.MOTION_DECT_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_allarm_type2);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.EMERGENCY_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_allarm_type3);
					tv_type.setText(R.string.os_allarm_type);
					break;

				case P2PValue.AlarmType.LOW_VOL_ALARM:
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_low_voltage_alarm);
					tv_type.setText(R.string.os_allarm_type);
					if (isSupport1) {
						layout_area_chanel
								.setVisibility(RelativeLayout.VISIBLE);
						area_text
								.setText(mContext.getResources().getString(
										R.string.os_area)
										+ ":"
										+ Utils.getDefenceAreaByGroup(mContext,
												group1));
						chanel_text.setText(mContext.getResources().getString(
								R.string.os_channel)
								+ ":" + (item1 + 1));
					}
					break;
				case P2PValue.AlarmType.PIR_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_allarm_type4);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.EXT_LINE_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_allarm_type5);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.DEFENCE:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_defence);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.NO_DEFENCE:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_no_defence);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.BATTERY_LOW_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_battery_low_alarm);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.ALARM_TYPE_DOORBELL_PUSH:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					alarm_type_text.setText(R.string.os_door_bell);
					tv_info.setText(R.string.os_visitor_messge);
					tv_type.setText(R.string.os_allarm_type);
					break;
				case P2PValue.AlarmType.RECORD_FAILED_ALARM:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					alarm_type_text.setText(R.string.os_record_failed);
					tv_type.setText(R.string.os_allarm_type);
					break;
				default:
					layout_area_chanel.setVisibility(RelativeLayout.GONE);
					tv_info.setText(R.string.os_alarm_info);
					tv_type.setText(R.string.os_not_know);
					alarm_type_text.setText(String.valueOf(alarm_type));
					break;
				}

			}
		}
	};
	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			finish();
			String[] data = (String[]) msg.obj;
			Intent monitor = new Intent();
			monitor.setClass(mContext, CallActivity.class);
			monitor.putExtra("callId", data[0]);
			monitor.putExtra("password", data[1]);
			monitor.putExtra("contactName", device.getDeviceName());
			monitor.putExtra("isOutCall", true);
			monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
			startActivity(monitor);
			return false;
		}
	});

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ignore_btn:
			int timeInterval = SharedPreferencesManager.getInstance()
					.getAlarmTimeInterval(mContext);
			ShowMsg(mContext.getResources().getString(
					R.string.os_ignore_alarm_prompt_start)
					+ " "
					+ timeInterval
					+ " "
					+ mContext.getResources().getString(
							R.string.os_ignore_alarm_prompt_end));
			finish();
			break;
		case R.id.monitor_btn:
			if (null != device) {
				hasContact = true;
				P2PConnect.vReject("");
				new Thread() {
					public void run() {
						while (true) {
							if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
								Message msg = new Message();
								String[] data = new String[] {
										device.getDeviceId(),
										device.getDevicePassword() };
								msg.obj = data;
								handler.sendMessage(msg);
								break;
							}
							Utils.sleepThread(500);
						}
					}
				}.start();
			}

			if (!hasContact) {
				if (alarm_input.getVisibility() == RelativeLayout.VISIBLE) {
					return;
				}

				alarm_input.setVisibility(RelativeLayout.VISIBLE);
				alarm_input.requestFocus();
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.slide_in_right);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						InputMethodManager m = (InputMethodManager) alarm_input
								.getContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

				});
				alarm_input.startAnimation(anim);
			}
			break;
		case R.id.alarm_go:
			final String password = mPassword.getText().toString();
			if (password.trim().equals("")) {
				ShowMsg(R.string.os_input_monitor_pwd);
				return;
			}

			if (password.length() > 9) {
				ShowMsg(R.string.os_password_length_error);
				return;
			}

			P2PConnect.vReject("");

			new Thread() {
				public void run() {
					while (true) {
						if (P2PConnect.getCurrent_state() == P2PConnect.P2P_STATE_NONE) {
							Message msg = new Message();
							String[] data = new String[] {
									String.valueOf(alarm_id), password };
							msg.obj = data;
							handler.sendMessage(msg);
							break;
						}
						Utils.sleepThread(500);
					}
				}
			}.start();
			break;
		case R.id.shield_btn:
			ShowAlertDialog(
					mContext.getResources().getString(R.string.os_shielded),
					mContext.getResources().getString(
							R.string.os_shielded_alarm_promp),
					new MyDialogInterface() {

						@Override
						public void onClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							if(TextUtils.isEmpty(device.getDeviceId())){
								ShowMsg("设备id不可为空");
								return;
							}
							if(TextUtils.isEmpty(device.getDevicePassword())){
								ShowMsg("设备密码不可为空");
								return;
							}
							P2PHandler.getInstance().setRemoteDefence(device.getDeviceId(), device.getDevicePassword(),
									Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
							AlarmActivity.this.finish();
						}
					});
			break;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferencesManager.getInstance().putIgnoreAlarmTime(mContext,
				System.currentTimeMillis());
		MusicManger.getInstance().stop();
		isAlarm = false;
		P2PConnect.vEndAllarm();
		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		P2PConnect.setAlarm(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		P2PConnect.setAlarm(false);
		if (isRegFilter = true) {
			mContext.unregisterReceiver(br);
		}
	}

}
