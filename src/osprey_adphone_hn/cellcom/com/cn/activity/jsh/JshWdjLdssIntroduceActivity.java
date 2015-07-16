package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;

public class JshWdjLdssIntroduceActivity extends ActivityFrame{
	
	private CheckBox cb_show;
	private Button btn_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_ldss_introduce);
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
		cb_show = (CheckBox) findViewById(R.id.cb_show);
		btn_next = (Button) findViewById(R.id.btn_next);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cb_show.isChecked()){
					SharepreferenceUtil.write(JshWdjLdssIntroduceActivity.this, SharepreferenceUtil.fileName, "ldss_show", false);
					OpenActivityFinsh(JshWdjLdssSetActivity.class);
				}else{
					SharepreferenceUtil.write(JshWdjLdssIntroduceActivity.this, SharepreferenceUtil.fileName, "ldss_show", true);
					OpenActivity(JshWdjLdssSetActivity.class);
				}
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.os_jsh_wdj_add_ldss));
		regFliter();
	}
	
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
