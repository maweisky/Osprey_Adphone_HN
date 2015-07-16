package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;

public class JshWdjWifiAddActivity extends ActivityFrame{
	
	private EditText et_ssid, et_password;
	private Button btn_xzwl, btn_next;
	private boolean isCheck = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_wifi_add);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		HideSet();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		et_ssid = (EditText) findViewById(R.id.et_ssid);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_xzwl = (Button) findViewById(R.id.btn_xzwl);
		btn_next = (Button) findViewById(R.id.btn_next);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		btn_xzwl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivityForResult(JshWdjScanWifisActivity.class, 1001);
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!WifiManagers.getInstance().getWifiStatus()){
					ShowMsg("请启动手机wifi！");
					return;
				}
				String ssid = et_ssid.getText().toString();
				if(TextUtils.isEmpty(ssid)){
					ShowMsg("wifi名称不能为空，请输入wifi名称！");
					return;
				}
				String password = et_password.getText().toString();
				if(TextUtils.isEmpty(password)){
					ShowMsg("wifi密码不能为空，请输入wifi密码！");
					return;
				}
				if(!WifiManagers.getInstance().getWifiStatus()){
					ShowMsg("请启动手机wifi！");
				}else{
					ShowProgressDialog(R.string.app_loading);
//					isCheck = true;
//					WifiManagers.getInstance().startScan();
					goToEwm();
				}		
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle("WIFI添加");
		regFliter();
		if(!WifiManagers.getInstance().getWifiStatus()){
			ShowMsg("请启动手机wifi！");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1001 && resultCode == Activity.RESULT_OK){
			et_ssid.setText(data.getStringExtra("ssid"));
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
			if (intent.getAction().equals(OsConstants.JSH.ADD_DEVICES_SUCCES)
					|| intent.getAction().equals(OsConstants.JSH.SETTIING_WIFI_SUCCESS)) {
				finish();
			}else if(intent.getAction().equals(OsConstants.JSH.SCAN_WIFI_END)){
				if(isCheck){
					showNoticeDialog();
				}else{
					et_ssid.setText(WifiManagers.getInstance().getSSID());
				}
			}
		}
	};
	
	private void showNoticeDialog() {
		// TODO Auto-generated method stub
		isCheck = false;
		DismissProgressDialog();
		String ssid = et_ssid.getText().toString();
		LogMgr.showLog("ssid------------------>" + ssid);
		String localSsid = WifiManagers.getInstance().getSSID();
		if(!TextUtils.isEmpty(localSsid)){
			LogMgr.showLog("localSsid------------------>" + localSsid);
		}else{
			LogMgr.showLog("localSsid------------------>null");
		}
		if(TextUtils.isEmpty(localSsid) || !localSsid.equalsIgnoreCase(ssid)){
			ShowAlertDialog("提示", "你所选择的wifi和你手机所处在的wifi不是同一个网络，这样无法使用局域网搜索设备！", 
					new MyDialogInterface() {
				
				@Override
				public void onClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					goToEwm();
				}
			});
		}else{			
			goToEwm();
		}
	}
	
	private void goToEwm() {
		// TODO Auto-generated method stub		
		String ssid = et_ssid.getText().toString();
		String password = et_password.getText().toString();
		Intent intent = new Intent(JshWdjWifiAddActivity.this, JshWdjEwmActivity.class);
		intent.putExtra("ssid", ssid);
		intent.putExtra("password", password);
		startActivity(intent);		
	}

}
