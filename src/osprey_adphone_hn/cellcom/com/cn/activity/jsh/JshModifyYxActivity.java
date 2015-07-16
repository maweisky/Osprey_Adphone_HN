package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.p2p.core.P2PHandler;

/**
 * 修改邮箱
 * 
 * @author wma
 * 
 */
public class JshModifyYxActivity extends ActivityFrame {
	private EditText newyx;
	private Button subbtn;

	private Device mContact;
	private String email_name;
	private boolean isRegFilter = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendTitleBody2();
		HideupdateSet();
		AppendMainBody(R.layout.os_grzl_modifyyx);
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_xgbjyx));
		InitView();
		InitData();
		InitListener();
		regFilter();
	}

	private void InitData() {
		// TODO Auto-generated method stub
		mContact = (Device) getIntent().getSerializableExtra("contact");
		email_name = getIntent().getStringExtra("email");
		if (!email_name.equals("Unbound") && !email_name.equals("未绑定")
				&& !email_name.equals("未綁定")) {
			newyx.setText(email_name);
		}
	}

	private void InitListener() {
		// TODO Auto-generated method stub
		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = newyx.getText().toString();
				if ("".equals(email.trim())) {
					// T.showShort(mContext, R.string.input_email);
					P2PHandler.getInstance().setAlarmEmail(
							mContact.getDeviceId(),
							mContact.getDevicePassword(), "0");
					return;
				}

				if (email.length() > 31 || email.length() < 5) {
					ShowMsg(R.string.os_email_too_long);
					return;
				}
				ShowProgressDialog(R.string.hsc_progress);
				P2PHandler.getInstance().setAlarmEmail(mContact.getDeviceId(),
						mContact.getDevicePassword(), email);
			}
		});
	}

	private void InitView() {
		// TODO Auto-generated method stub
		newyx = (EditText) findViewById(R.id.newyx);
		subbtn = (Button) findViewById(R.id.subbtn);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
		filter.addAction(Constants.P2P.RET_SET_ALARM_EMAIL);
		JshModifyYxActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_SET_ALARM_EMAIL)) {
				DismissProgressDialog();
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ALARM_EMAIL_SET.SETTING_SUCCESS) {
					ShowMsg(R.string.os_modify_success);
					finish();
				} else if (result == 15) {
					ShowMsg(R.string.os_email_format_error);
				} else if (result == -1) {
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_ALARM_EMAIL)) {
				DismissProgressDialog();
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					finish();
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshModifyYxActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					ShowMsg(R.string.os_net_error_operator_fault);
				}
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			JshModifyYxActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}
}