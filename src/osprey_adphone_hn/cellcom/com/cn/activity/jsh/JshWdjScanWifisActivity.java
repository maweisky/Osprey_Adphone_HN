package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWdjWifiListAdapter;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.thread.WifiScanCallBack;
import p2p.cellcom.com.cn.thread.WifiScanTask;

public class JshWdjScanWifisActivity extends ActivityFrame{
	
	private ListView listView;
	private List<ScanResult> wifis = new ArrayList<ScanResult>();
	private JshWdjWifiListAdapter adapter;

	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;
	private TextView tv_empty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_scan_wifis);
		AppendTitleBody1();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.listview);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) findViewById(R.id.loadingImageView);
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		
		initAdapter();
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		tv_empty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!WifiManagers.getInstance().getWifiStatus()){
					ShowMsg("请先打开wifi功能！");
				}else{
					WifiManagers.getInstance().startScan();
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				data.putExtra("ssid", wifis.get(position).SSID);
				setResult(Activity.RESULT_OK, data);
				finish();	
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		animationDrawable = (AnimationDrawable) imageView_loading.getBackground();
		SetTopBarTitle("设备附近的无线网络");
		regFliter();
		if(!WifiManagers.getInstance().getWifiStatus()){
			ShowMsg("请先打开wifi功能！");
		}else{
			showLoading();
			WifiManagers.getInstance().startScan();
		}
		
	}
	
	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshWdjWifiListAdapter(this, wifis);
		listView.setAdapter(adapter);
	}
	
	private void showLoading() {
		// TODO Auto-generated method stub
		ll_loading.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		tv_empty.setVisibility(View.GONE);
		animationDrawable.start();
	}
	
	private void hideLoading1() {
		// TODO Auto-generated method stub
		tv_empty.setVisibility(View.VISIBLE);
		ll_loading.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		animationDrawable.stop();		
	}
	
	private void hideLoading2() {
		// TODO Auto-generated method stub
		listView.setVisibility(View.VISIBLE);
		tv_empty.setVisibility(View.GONE);
		ll_loading.setVisibility(View.GONE);
		animationDrawable.stop();		
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
		filter.addAction(OsConstants.JSH.SCAN_WIFI_END);
		registerReceiver(mReceiver, filter);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(OsConstants.JSH.SCAN_WIFI_END)){
				if(WifiManagers.getInstance().getWifiList() != null && WifiManagers.getInstance().getWifiList().size() > 0){
					wifis.clear();
					wifis.addAll(WifiManagers.getInstance().getWifiList());
					hideLoading2();
					adapter.notifyDataSetChanged();
					ShowMsg("搜索wifi热点成功！");
				}else{
					hideLoading1();
					ShowMsg("暂无wifi热点！");
				}
			}
		}
	};

}
