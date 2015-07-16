package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import osprey_adphone_hn.cellcom.com.cn.widget.LoadProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 雷达搜索设置
 * @author Administrator
 *
 */
public class JshWdjLdssSetActivity extends ActivityFrame{

	private TextView tv_ssid;
	private EditText et_password;
	private Button btn_next;
	private LoadProgressDialog pDialog;
	private boolean isClick = false;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_ldss_set);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		initView();
		initListener();
		initData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);			
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		tv_ssid = (TextView) findViewById(R.id.tv_ssid);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_next = (Button) findViewById(R.id.btn_next);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkPassword();
				isClick = true;
				checkWifi();
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.os_jsh_wdj_add_ldss));
		regFliter();
		checkWifi();
	}
	
	private void checkPassword() {
		// TODO Auto-generated method stub
		password = et_password.getText().toString();
		if(TextUtils.isEmpty(password)){
			ShowMsg("请输入wifi密码！");
			return;
		}
	}
	
	private void checkWifi(){
		if(WifiManagers.getInstance().getWifiStatus()){
			ShowProgressDialog(R.string.app_loading);
			WifiManagers.getInstance().startScan();
		}else{
			ShowMsg("请先开启手机wifi！");
		}
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
				DismissProgressDialog();
				tv_ssid.setText(WifiManagers.getInstance().getSSID());
				if(isClick){
					String ssid = WifiManagers.getInstance().getSSID();
					if(!TextUtils.isEmpty(ssid)){
						Intent intents = new Intent(JshWdjLdssSetActivity.this, JshWdjLdssSendActivity.class);
						intents.putExtra("ssid", ssid);
						intents.putExtra("password", password);
						intents.putExtra("wifiInfo", WifiManagers.getInstance().getScanResult(ssid));
						startActivityForResult(intents, 1234);
					}else{
						ShowMsg("获取wifi信息失败！");
					}
					isClick = false;
				}
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1234 && resultCode==RESULT_OK){
			LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        //将view对象挂载到那个父元素上，这里没有就为null
	        View view=inflater.inflate(R.layout.dialog_connect_failed, null);
			ShowViewAlertDialog("温馨提示", view, new MyDialogInterface() {
				
				@Override
				public void onClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	};
}
