package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

/**
 * 录像设置
 * 
 * @author wma
 * 
 */
public class JshWdjLxSetActivity extends ActivityFrame {
	private ImageView video_img;
	private FinalBitmap finalBitmap;
	private RadioButton blxrb;
	private RadioButton qthlxrb;
	private RadioButton bjlxrb;
	private TextView videotv;
	private Device device;
	private int cur_modify_motion_state;
	private int cur_modify_buzzer_state;

	private boolean isRegFilter = false;

	private int cur_modify_record_type;
	private int last_modify_record;
	String cur_modify_plan_time;

	private int recordtype;
	private int recordtime;
	private String timeplan;

	private boolean blx;
	private boolean bjlx;
	private boolean qthlx;

	private int remote_recordstate;
	private int cur_modify_record_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_lxset);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		initView();
		initData();
		initListener();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			JshWdjLxSetActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
		Intent it = new Intent();
		it.setAction(Constants.Action.CONTROL_BACK);
		JshWdjLxSetActivity.this.sendBroadcast(it);
	}

	private void initView() {
		// TODO Auto-generated method stub
		video_img = (ImageView) findViewById(R.id.video_img);
		videotv = (TextView) findViewById(R.id.videotv);
		blxrb = (RadioButton) findViewById(R.id.blxrb);
		qthlxrb = (RadioButton) findViewById(R.id.qthlxrb);
		bjlxrb = (RadioButton) findViewById(R.id.bjlxrb);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

		filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_TYPE);
		filter.addAction(Constants.P2P.RET_SET_RECORD_TYPE);
		filter.addAction(Constants.P2P.RET_GET_RECORD_TYPE);

		filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_TIME);
		filter.addAction(Constants.P2P.RET_SET_RECORD_TIME);
		filter.addAction(Constants.P2P.RET_GET_RECORD_TIME);

		filter.addAction(Constants.P2P.ACK_RET_SET_RECORD_PLAN_TIME);
		filter.addAction(Constants.P2P.RET_SET_RECORD_PLAN_TIME);
		filter.addAction(Constants.P2P.RET_GET_RECORD_PLAN_TIME);

		filter.addAction(Constants.P2P.ACK_RET_SET_REMOTE_RECORD);
		filter.addAction(Constants.P2P.RET_SET_REMOTE_RECORD);
		filter.addAction(Constants.P2P.RET_GET_REMOTE_RECORD);
		filter.addAction(Constants.P2P.RET_GET_PRE_RECORD);
		filter.addAction(Constants.P2P.ACK_RET_SET_PRE_RECORD);
		filter.addAction(Constants.P2P.RET_SET_PRE_RECORD);
		JshWdjLxSetActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get npc settings");
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_RECORD_TYPE)) {
				recordtype = intent.getIntExtra("type", -1);
				updateRecordType();
				showRecordType();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_RECORD_TYPE)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.RECORD_TYPE_SET.SETTING_SUCCESS) {
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
					ShowMsg(R.string.os_modify_success);
				} else {
					showRecordType();
					DismissProgressDialog();
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_RECORD_TIME)) {
				recordtime = intent.getIntExtra("time", -1);
				updateRecordTime();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_RECORD_TIME)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
					ShowMsg(R.string.os_modify_success);
				} else {
					DismissProgressDialog();
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_RECORD_PLAN_TIME)) {
				timeplan = intent.getStringExtra("time");
				updateRecordPlan();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_RECORD_PLAN_TIME)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.RECORD_PLAN_TIME_SET.SETTING_SUCCESS) {
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
					ShowMsg(R.string.os_modify_success);
				} else {
					DismissProgressDialog();
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_RECORD_TYPE)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings record type");
					P2PHandler.getInstance().setRecordType(
							device.getDeviceId(), device.getDevicePassword(),
							cur_modify_record_type);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_RECORD_TIME)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings record time");
					P2PHandler.getInstance().setRecordType(
							device.getDeviceId(), device.getDevicePassword(),
							cur_modify_record_time);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_RECORD_PLAN_TIME)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my",
							"net error resend:set npc settings record plan time");
					P2PHandler.getInstance().setRecordPlanTime(
							device.getDeviceId(), device.getDevicePassword(),
							cur_modify_plan_time);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_REMOTE_RECORD)) {
				remote_recordstate = intent.getIntExtra("state", -1);
				updateRecord();
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_REMOTE_RECORD)) {
				int state = intent.getIntExtra("state", -1);
				P2PHandler.getInstance().getNpcSettings(device.getDeviceId(),
						device.getDevicePassword());
				// updateRecord(state);
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_REMOTE_RECORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set remote record");
					P2PHandler.getInstance().setRemoteRecord(
							device.getDeviceId(), device.getDevicePassword(),
							last_modify_record);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_PRE_RECORD)) {
				int state = intent.getIntExtra("state", -1);
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_PRE_RECORD)) {
				int result = intent.getIntExtra("state", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjLxSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings record type");
					// P2PHandler.getInstance().setPreRecord(device.getDeviceId(),
					// device.getDevicePassword(),last_modify_pre_record);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_PRE_RECORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
					ShowMsg(R.string.os_modify_success);
				} else if (result == 83) {
					ShowMsg(R.string.os_operator_error);
				}
			}
		}
	};

	public void updateRecord() {
		if (remote_recordstate == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_ON) {
		} else {
			if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL) {
				DismissProgressDialog();
				blxrb.setChecked(true);
			}
		}
	}

	public void updateRecordTime() {
		if (recordtime == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_ONE_MINUTE) {
		} else if (recordtime == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
			if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
				DismissProgressDialog();
				bjlxrb.setChecked(true);
			}
		} else if (recordtime == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_THREE_MINUTE) {
		}
	}

	public void updateRecordPlan() {
		if ("01:00-00:59".equals(timeplan)) {
			if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER) {
				DismissProgressDialog();
				qthlxrb.setChecked(true);
			}
		} 
	}

	void updateRecordType() {
		if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL) {
			if (remote_recordstate == Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF) {
				DismissProgressDialog();
				blxrb.setChecked(true);
			} else {
				if (blx) {
					last_modify_record = Constants.P2P_SET.REMOTE_RECORD_SET.RECORD_SWITCH_OFF;
					P2PHandler.getInstance().setRemoteRecord(
							device.getDeviceId(), device.getDevicePassword(),
							last_modify_record);
				}
			}
		} else if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM) {
			if (recordtime == Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE) {
				DismissProgressDialog();
				bjlxrb.setChecked(true);
			} else {
				if (bjlx) {
					cur_modify_record_time = Constants.P2P_SET.RECORD_TIME_SET.RECORD_TIME_TWO_MINUTE;
					P2PHandler.getInstance().setRecordTime(
							device.getDeviceId(), device.getDevicePassword(),
							cur_modify_record_time);
				}
			}
		} else if (recordtype == Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER) {
			if ("01:00-00:59".equals(timeplan)) {
				DismissProgressDialog();
				qthlxrb.setChecked(true);
			} else {
				if (qthlx) {
					cur_modify_plan_time = "01:00-00:59";
					P2PHandler.getInstance().setRecordPlanTime(
							device.getDeviceId(), device.getDevicePassword(),
							cur_modify_plan_time);
				}
			}
		}
	}

	public void showRecordType() {
		blxrb.setEnabled(true);
		qthlxrb.setEnabled(true);
		bjlxrb.setEnabled(true);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		// 不录像
		blxrb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				blx = true;
				qthlx= false;
				bjlx = false;
				blxrb.setEnabled(false);
				qthlxrb.setEnabled(false);
				bjlxrb.setEnabled(false);
				cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_MANUAL;
				CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
				customProgressDialog.setCanceledOnTouchOutside(false);
				customProgressDialog.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
							JshWdjLxSetActivity.this.finish();
						}
						return false;
					}
				});
				P2PHandler.getInstance().setRecordType(device.getDeviceId(),
						device.getDevicePassword(), cur_modify_record_type);
			}
		});
		//报警录像
		bjlxrb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qthlx= false;
				bjlx = true;
				blx  = false;
				blxrb.setEnabled(false);
				qthlxrb.setEnabled(false);
				bjlxrb.setEnabled(false);
				cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_ALARM;
				CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
				customProgressDialog.setCanceledOnTouchOutside(false);
				customProgressDialog.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
							JshWdjLxSetActivity.this.finish();
						}
						return false;
					}
				});
				P2PHandler.getInstance().setRecordType(device.getDeviceId(),
						device.getDevicePassword(), cur_modify_record_type);
			}
		});
		//全天候
		qthlxrb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qthlx= true;
				bjlx = false;
				blx  = false;
				blxrb.setEnabled(false);
				qthlxrb.setEnabled(false);
				bjlxrb.setEnabled(false);
				CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
				customProgressDialog.setCanceledOnTouchOutside(false);
				customProgressDialog.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
							JshWdjLxSetActivity.this.finish();
						}
						return false;
					}
				});
				cur_modify_record_type = Constants.P2P_SET.RECORD_TYPE_SET.RECORD_TYPE_TIMER;
				P2PHandler.getInstance().setRecordType(device.getDeviceId(),
						device.getDevicePassword(), cur_modify_record_type);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz_lxsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");
		finalBitmap = FinalBitmap.create(JshWdjLxSetActivity.this);
		if (!TextUtils.isEmpty(device.getServerImgUrl())) {
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
		CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
					JshWdjLxSetActivity.this.finish();
				}
				return false;
			}
		});
		regFilter();
		P2PHandler.getInstance().getNpcSettings(device.getDeviceId(),
				device.getDevicePassword());
	}
}