package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import com.google.zxing.WriterException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.thread.UDPHelper;
import p2p.cellcom.com.cn.utils.EncodingHandler;

public class JshWdjEwmActivity extends ActivityFrame{
	
	private ImageView iv_ewm;
	private Button btn_hear;
	private LinearLayout ll_help;
	private UDPHelper helper;
	private String ssid, password;
	
	private WifiManagers managers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_ewm);
		AppendTitleBody3();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		iv_ewm = (ImageView) findViewById(R.id.iv_ewm);
		btn_hear = (Button) findViewById(R.id.btn_hear);
		ll_help = (LinearLayout) findViewById(R.id.ll_help);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		btn_hear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				helper.StopListen();
				Intent it=new Intent();				
				it.setAction(Constants.Action.SETTING_WIFI_SUCCESS);
				sendBroadcast(it);
				finish();
			}
		});
		ll_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHelpDialog();
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		ssid = getIntent().getStringExtra("ssid");
		password = getIntent().getStringExtra("password");
		managers = new WifiManagers(this);
		createEwm(ssid, password);
		maxVoice();
		initUDPHelper();
		regFliter();
	}
	
	private void createEwm(String ssid, String password){
		LayoutParams parms = (LayoutParams)iv_ewm.getLayoutParams();
		try {
    		String QRinfo="EnCtYpE_ePyTcNeEsSiD"+ssid+"dIsSeCoDe"+password+"eDoC";
			Bitmap qrCodeBitmap=EncodingHandler.createQRCode(QRinfo ,parms.width);
			iv_ewm.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void maxVoice() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
	}
	
	private void initUDPHelper(){
		if(!WifiManagers.getInstance().getWifiStatus()){
			return;
		}
		if(helper == null){
			helper = new UDPHelper(9988);
		}
		helper.setCallBack(new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case UDPHelper.HANDLER_MESSAGE_BIND_ERROR:
					Log.e("my","HANDLER_MESSAGE_BIND_ERROR");
					break;
				case UDPHelper.HANDLER_MESSAGE_RECEIVE_MSG:
					Log.e("my","HANDLER_MESSAGE_RECEIVE_MSG");
					helper.StopListen();
					OpenActivity(JshWdjSearchDevicesActivity.class);
					break; 
				}
			}
			
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(helper != null){
			helper.IsThreadDisable = true;
		}
		unregisterReceiver(mReceiver);
	}
	
	private void regFliter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
		filter.addAction(OsConstants.JSH.SETTIING_WIFI_SUCCESS);
		filter.addAction(OsConstants.JSH.SCAN_WIFI_END);
		registerReceiver(mReceiver, filter);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(OsConstants.JSH.ADD_DEVICES_SUCCES) || intent.getAction().equals(OsConstants.JSH.SETTIING_WIFI_SUCCESS)){
				finish();
			}
		}
	};
	
	private void showHelpDialog() {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(this, R.style.helpDialog);
		View view = LayoutInflater.from(this).inflate(R.layout.os_jsh_wdj_ewm_help, null);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
