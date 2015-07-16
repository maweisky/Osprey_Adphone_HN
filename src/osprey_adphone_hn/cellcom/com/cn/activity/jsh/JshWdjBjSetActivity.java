package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.NpcCommon;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

/**
 * 报警设置
 * 
 * @author wma
 * 
 */
public class JshWdjBjSetActivity extends ActivityFrame {
	private ImageView video_img;
	private TextView videotv;
	private FinalBitmap finalBitmap;
	private ImageView bjxxsb;
	private ImageView ydsb;
	private ImageView fmqsb;
	private RelativeLayout bjxxrl;
	private RelativeLayout ydrl;
	private RelativeLayout fmqrl;
	private ProgressBar progressBar_bjxx;
	private ProgressBar progressBar_yd;
	private ProgressBar progressBar_fmq;
	private Device device;
	private int cur_modify_motion_state;
	private int cur_modify_buzzer_state;
	private int buzzer_switch;
	private int motion_switch;

	private RelativeLayout xgyxrl;
	private TextView yxtv;
	private boolean isRegFilter = false;
	private boolean isReceiveAlarm=true;
	
	private String[] new_data;
	private String[] last_bind_data;
	private int max_alarm_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_bjset);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			JshWdjBjSetActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		video_img = (ImageView) findViewById(R.id.video_img);
		videotv = (TextView) findViewById(R.id.videotv);
		bjxxsb=(ImageView)findViewById(R.id.bjxxsb);
		ydsb = (ImageView) findViewById(R.id.ydsb);
		fmqsb = (ImageView) findViewById(R.id.fmqsb);
		progressBar_bjxx=(ProgressBar)findViewById(R.id.progressBar_bjxx);
		progressBar_yd=(ProgressBar)findViewById(R.id.progressBar_yd);
		progressBar_fmq=(ProgressBar)findViewById(R.id.progressBar_fmq);
		bjxxrl=(RelativeLayout)findViewById(R.id.bjxxrl);
		xgyxrl = (RelativeLayout) findViewById(R.id.xgyxrl);
		ydrl=(RelativeLayout)findViewById(R.id.ydrl);
		fmqrl=(RelativeLayout)findViewById(R.id.fmqrl);
		yxtv = (TextView) findViewById(R.id.yxtv);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

		filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
		filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
		filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
		filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);

		// filter.addAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.ACK_RET_GET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.RET_SET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL);

		filter.addAction(Constants.P2P.ACK_RET_SET_MOTION);
		filter.addAction(Constants.P2P.RET_SET_MOTION);
		filter.addAction(Constants.P2P.RET_GET_MOTION);

		filter.addAction(Constants.P2P.ACK_RET_SET_BUZZER);
		filter.addAction(Constants.P2P.RET_SET_BUZZER);
		filter.addAction(Constants.P2P.RET_GET_BUZZER);
		filter.addAction(Constants.P2P.RET_GET_INFRARED_SWITCH);
		filter.addAction(Constants.P2P.ACK_RET_SET_INFRARED_SWITCH);
		filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_INPUT);
		filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_OUT);
		filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT);
		filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT);

		JshWdjBjSetActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)) {
				DismissProgressDialog();
				showImg_receive_alarm();
				String[] data = intent.getStringArrayExtra("data");
				int max_count = intent.getIntExtra("max_count", 0);
				last_bind_data = data;
				max_alarm_count=max_count;
				bjxxrl.setClickable(true);
				int count=0;
				for(int i=0;i<data.length;i++){
					if(data[i].equals(NpcCommon.mThreeNum)){
						bjxxsb.setImageResource(R.drawable.os_jsh_checkboxon);
						isReceiveAlarm=false;
						count=count+1;
						return;
					}
				}
				if(count==0){
					bjxxsb.setImageResource(R.drawable.os_jsh_checkboxoff);
					isReceiveAlarm=true;
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_BIND_ALARM_ID)) {
				int result = intent.getIntExtra("result", -1);
				DismissProgressDialog();
				if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
					P2PHandler.getInstance().getBindAlarmId(device.getDeviceId(),
							device.getDevicePassword());
					ShowMsg(R.string.os_modify_success);
				} else {
//					if (getIsRun()) {
						ShowMsg(R.string.os_operator_error);
//					}
				}
			} else if (intent.getAction().equals(Constants.P2P.RET_GET_ALARM_EMAIL)) {
				DismissProgressDialog();
				String email = intent.getStringExtra("email");
				if (email.equals("") || email.equals("0")) {
					yxtv.setText(R.string.os_unbound);
				} else {
					yxtv.setText(email);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_ALARM_EMAIL)) {
				P2PHandler.getInstance().getAlarmEmail(device.getDeviceId(),
						device.getDevicePassword());
			} else if (intent.getAction().equals(Constants.P2P.RET_GET_MOTION)) {
				DismissProgressDialog();
				int state = intent.getIntExtra("motionState", -1);
				Log.e("my", "RET_GET_MOTION=>"+state);
				if (state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
					motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
					ydsb.setImageResource(R.drawable.os_jsh_checkboxon);
				} else {
					motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
					ydsb.setImageResource(R.drawable.os_jsh_checkboxoff);
				}
				showMotionState();
			} else if (intent.getAction().equals(Constants.P2P.RET_SET_MOTION)) {
				int result = intent.getIntExtra("result", -1);
				Log.e("my", "RET_SET_MOTION=>"+result);
				DismissProgressDialog();
				if (result == Constants.P2P_SET.MOTION_SET.SETTING_SUCCESS) {
					if (cur_modify_motion_state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
						motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
						ydsb.setImageResource(R.drawable.os_jsh_checkboxon);
					} else {
						motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
						ydsb.setImageResource(R.drawable.os_jsh_checkboxoff);
					}
					showMotionState();
					ShowMsg(R.string.os_modify_success);
				} else {
					showMotionState();
					ShowMsg(R.string.os_operator_error);
				}
//				showMotionState();
			} else if (intent.getAction().equals(Constants.P2P.RET_GET_BUZZER)) {
				DismissProgressDialog();
				int state = intent.getIntExtra("buzzerState", -1);
				Log.e("my", "RET_GET_BUZZER=>"+state);
				updateBuzzer(state);
				showBuzzerTime();
			} else if (intent.getAction().equals(Constants.P2P.RET_SET_BUZZER)) {
				DismissProgressDialog();
				int result = intent.getIntExtra("result", -1);
				Log.e("my", "RET_SET_BUZZER=>"+result);
				if (result == Constants.P2P_SET.BUZZER_SET.SETTING_SUCCESS) {
					updateBuzzer(cur_modify_buzzer_state);
					ShowMsg(R.string.os_modify_success);
				} else {
					ShowMsg(R.string.os_operator_error);
				}
				showBuzzerTime();
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get npc settings");
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
				}
			}else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)) {
				int result = intent.getIntExtra("result", -1);

				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set alarm bind id");
					P2PHandler.getInstance().setBindAlarmId(device.getDeviceId(),
							device.getDevicePassword(), new_data.length,
							new_data);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_BIND_ALARM_ID)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get alarm bind id");
					P2PHandler.getInstance().getBindAlarmId(device.getDeviceId(),
							device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_ALARM_EMAIL)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get alarm email");
					P2PHandler.getInstance().getAlarmEmail(
							device.getDeviceId(), device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_MOTION)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings motion");
					P2PHandler.getInstance()
							.setMotion(device.getDeviceId(),
									device.getDevicePassword(),
									cur_modify_motion_state);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_BUZZER)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					DismissProgressDialog();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjBjSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings buzzer");
					P2PHandler.getInstance()
							.setBuzzer(device.getDeviceId(),
									device.getDevicePassword(),
									cur_modify_buzzer_state);
				}
			}
		}
	};

	private void initListener() {
		// TODO Auto-generated method stub
		//报警信息
		bjxxrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress_receive_alarm();
//				ShowProgressDialog(R.string.hsc_progress);
				if(isReceiveAlarm==true){
					if(last_bind_data.length>=max_alarm_count){
						ShowMsg(R.string.os_alarm_push_limit);
						showImg_receive_alarm();
						return;
					}
					new_data = new String[last_bind_data.length+1];
					for(int i=0;i<last_bind_data.length;i++){
						new_data[i] = last_bind_data[i];
					}
					new_data[new_data.length-1] = NpcCommon.mThreeNum;
					P2PHandler.getInstance().setBindAlarmId(device.getDeviceId(), device.getDevicePassword(), new_data.length, new_data);	
				}else{
					new_data = new String[last_bind_data.length-1];
					int count=0;
					for(int i=0;i<last_bind_data.length;i++){
						if(!last_bind_data[i].equals(NpcCommon.mThreeNum)){
							new_data[count]=last_bind_data[i];
							count++;
						}
					}
					if(new_data.length==0){
						new_data=new String[]{"0"};
					}
					P2PHandler.getInstance().setBindAlarmId(device.getDeviceId(), device.getDevicePassword(),new_data.length,new_data);
				}
			}
		});
		
		//修改邮箱
		xgyxrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent modify_email = new Intent(JshWdjBjSetActivity.this,
						JshModifyYxActivity.class);
				modify_email.putExtra("contact", device);
				modify_email.putExtra("email", yxtv.getText().toString());
				JshWdjBjSetActivity.this.startActivity(modify_email);
			}
		});
		//移动侦测
		ydrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress_motion();
//				ShowProgressDialog(R.string.hsc_progress);
				if (motion_switch != Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF) {
					cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
					P2PHandler.getInstance().setMotion(device.getDeviceId(),
							device.getDevicePassword(), cur_modify_motion_state);
				} else {
					cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
					P2PHandler.getInstance().setMotion(device.getDeviceId(),
							device.getDevicePassword(), cur_modify_motion_state);
				}
			}
		});
		//蜂鸣器
		fmqrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress();
//				ShowProgressDialog(R.string.hsc_progress);
				if (buzzer_switch != Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF) {
					cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
				} else {
					cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
				}
				P2PHandler.getInstance().setBuzzer(device.getDeviceId(),
						device.getDevicePassword(), cur_modify_buzzer_state);
			}
		});
	}
	public void showImg_receive_alarm(){
		progressBar_bjxx.setVisibility(ProgressBar.GONE);
		bjxxsb.setVisibility(View.VISIBLE);
	}
	public void showProgress_receive_alarm(){
		progressBar_bjxx.setVisibility(ProgressBar.VISIBLE);
		bjxxsb.setVisibility(View.GONE);
	}
	public void showMotionState() {
		progressBar_yd.setVisibility(RelativeLayout.GONE);
		ydsb.setVisibility(RelativeLayout.VISIBLE);
		ydrl.setEnabled(true);
	}
	public void showProgress_motion() {
		progressBar_yd.setVisibility(RelativeLayout.VISIBLE);
		ydsb.setVisibility(RelativeLayout.GONE);
		ydrl.setEnabled(false);
	}
	public void showProgress() {
		progressBar_fmq.setVisibility(RelativeLayout.VISIBLE);
		fmqsb.setVisibility(RelativeLayout.GONE);
		fmqrl.setEnabled(false);
	}
	public void showBuzzerTime() {
		progressBar_fmq.setVisibility(RelativeLayout.GONE);
		fmqsb.setVisibility(RelativeLayout.VISIBLE);
		fmqrl.setEnabled(true);
	}
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz_bjsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");
		finalBitmap=FinalBitmap.create(JshWdjBjSetActivity.this);
		if(!TextUtils.isEmpty(device.getServerImgUrl())){						
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
		regFilter();
		CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
					JshWdjBjSetActivity.this.finish();
				}
				return false;
			}
		});
		P2PHandler.getInstance().getAlarmEmail(device.getDeviceId(),
				device.getDevicePassword());
		P2PHandler.getInstance().getBindAlarmId(device.getDeviceId(),
				device.getDevicePassword());
		P2PHandler.getInstance().getNpcSettings(device.getDeviceId(),
				device.getDevicePassword());
		bjxxrl.setClickable(false);
	}

	public void updateBuzzer(int state) {
		if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE) {
			buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
			fmqsb.setImageResource(R.drawable.os_jsh_checkboxon);
		} else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE) {
			Log.e("my", "BUZZER_SWITCH_ON_TWO_MINUTE");
			buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE;
			fmqsb.setImageResource(R.drawable.os_jsh_checkboxon);
		} else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE) {
			buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE;
			fmqsb.setImageResource(R.drawable.os_jsh_checkboxon);
		} else {
			buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
			fmqsb.setImageResource(R.drawable.os_jsh_checkboxoff);
		}
	}

}