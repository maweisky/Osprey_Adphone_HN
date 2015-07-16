package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;

public class JshWdjAddDeviceActivity extends ActivityFrame{
	
	private LinearLayout ll_wlwsb, ll_ylwsb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_add_device);
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
		unregisterReceiver(mReceiver);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		ll_wlwsb = (LinearLayout) findViewById(R.id.ll_wlwsb);
		ll_ylwsb = (LinearLayout) findViewById(R.id.ll_ylwsb);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		ll_wlwsb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isShow = SharepreferenceUtil.readBoolean(JshWdjAddDeviceActivity.this, SharepreferenceUtil.fileName, "ldss_show", true);
				if(isShow){
					OpenActivity(JshWdjLdssIntroduceActivity.class);
				}else{
					OpenActivity(JshWdjLdssSetActivity.class);
				}
			}
		});
		
		ll_ylwsb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(JshWdjAddOnlineActivity.class);
				
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.os_jsh_wdj_add_tjsxt));
		regFliter();
	}
	
	//注册
	private void regFliter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
		filter.addAction(OsConstants.JSH.SETTIING_WIFI_SUCCESS);
		registerReceiver(mReceiver, filter);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(OsConstants.JSH.ADD_DEVICES_SUCCES)
					|| intent.getAction().equals(OsConstants.JSH.SETTIING_WIFI_SUCCESS)) {
				finish();
			}
		}
	};

}
