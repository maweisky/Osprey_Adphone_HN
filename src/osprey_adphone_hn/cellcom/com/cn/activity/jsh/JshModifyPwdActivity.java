package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.db.DBManager;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

/**
 * 修改设备密码
 * 
 * @author wma
 * 
 */
public class JshModifyPwdActivity extends ActivityFrame {
	private TextView videotv;
	private ImageView video_img;
	private EditText old_pwd;
	private EditText new_pwd;
	private EditText re_new_pwd;
	private Button subbtn;

	private Device device;
	private boolean isRegFilter = false;
	private String password_new;
	private FinalBitmap finalBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendTitleBody2();
		HideupdateSet();
		AppendMainBody(R.layout.os_grzl_modifydevicepwd);
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz_mmsz));
		InitView();
		InitData();
		InitListener();
		regFilter();
	}

	private void InitData() {
		// TODO Auto-generated method stub
		device = (Device) getIntent().getSerializableExtra("contact");
		finalBitmap = FinalBitmap.create(JshModifyPwdActivity.this);
		if (!TextUtils.isEmpty(device.getServerImgUrl())) {
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
	}

	private void InitListener() {
		// TODO Auto-generated method stub
		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password_old = old_pwd.getText().toString();
				password_new = new_pwd.getText().toString();
				String password_re_new = re_new_pwd.getText().toString();
				if("".equals(password_old.trim())){
					ShowMsg(R.string.os_input_old_device_pwd);
					return;
				}
				
				if(password_old.length()>10){
					ShowMsg(R.string.os_old_pwd_too_long);
					return;
				}
				
				if("".equals(password_new.trim())){
					ShowMsg(R.string.os_input_new_device_pwd);
					return;
				}
				
				
				if(password_new.length()>10){
					ShowMsg(R.string.os_new_pwd_too_long);
					return;
				}
				
				if(!Utils.isNumeric(password_new)||password_new.charAt(0)=='0'){
					ShowMsg(R.string.os_contact_pwd_must_digit);
					return;
				}
				
				if("".equals(password_re_new.trim())){
					ShowMsg(R.string.os_input_re_new_device_pwd);
					return;
				}
				
				if(!password_re_new.equals(password_new)){
					ShowMsg(R.string.os_pwd_inconsistence);
					return;
				}
				
				ShowProgressDialog(R.string.hsc_progress);
				P2PHandler.getInstance().setDevicePassword(device.getDeviceId(), password_old, password_new);
			}
		});
	}

	private void InitView() {
		// TODO Auto-generated method stub
		videotv = (TextView) findViewById(R.id.videotv);
		video_img = (ImageView) findViewById(R.id.video_img);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		re_new_pwd = (EditText) findViewById(R.id.re_new_pwd);
		subbtn = (Button) findViewById(R.id.subbtn);
	}

	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_SET_DEVICE_PASSWORD);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		JshModifyPwdActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals(Constants.P2P.RET_SET_DEVICE_PASSWORD)){
				int result = intent.getIntExtra("result", -1);
				DismissProgressDialog();
				if(result==Constants.P2P_SET.DEVICE_PASSWORD_SET.SETTING_SUCCESS){
					device.setDevicePassword(password_new);
//					FList.getInstance().update(device);
					DBManager.saveDevice(JshModifyPwdActivity.this, device);					
					Intent refreshContans = new Intent();
					refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
					refreshContans.putExtra("contact", device);
					JshModifyPwdActivity.this.sendBroadcast(refreshContans);
					ShowMsg( R.string.os_modify_success);
					finish();
				}else{
					ShowMsg( R.string.os_operator_error);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					DismissProgressDialog();
					ShowMsg( R.string.os_old_pwd_error);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					ShowMsg( R.string.os_net_error_operator_fault);
				}
			}else if(intent.getAction().equals(Constants.P2P.RET_DEVICE_NOT_SUPPORT)){
				finish();
				ShowMsg( R.string.os_not_support);
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			JshModifyPwdActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}
}