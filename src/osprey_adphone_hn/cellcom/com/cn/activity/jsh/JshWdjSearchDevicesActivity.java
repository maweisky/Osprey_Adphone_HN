package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWdjSearchDeviceAdapter;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.WifiManagers;
import osprey_adphone_hn.cellcom.com.cn.widget.xlistview.XListView;
import osprey_adphone_hn.cellcom.com.cn.widget.xlistview.XListView.IXListViewListener;
import p2p.cellcom.com.cn.bean.LocalDevice;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JshWdjSearchDevicesActivity extends ActivityFrame implements IXListViewListener{
	
	private XListView listView;
	private List<LocalDevice> list = new ArrayList<LocalDevice>();
	private JshWdjSearchDeviceAdapter adapter;
	
	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;
	private TextView tv_empty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_search_device);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		HideSet();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		//加载框
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) imageView_loading.getBackground();		
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		//设备列表
		listView = (XListView) findViewById(R.id.listview);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		tv_empty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLoading();
				FList.getInstance().searchLocalDevice();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JshWdjSearchDevicesActivity.this, JshWdjEditActivity.class);
				intent.putExtra("deviceId", ((LocalDevice)parent.getItemAtPosition(position)).getContactId());
				startActivity(intent);
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		String title = getIntent().getStringExtra("title");
		if(TextUtils.isEmpty(title)){
			title = "搜索局域网";
		}
		SetTopBarTitle(title);		
		initAdapter();
		regFliter();
		if(WifiManagers.getInstance().getWifiStatus()){
			FList.getInstance().searchLocalDevice();
			showLoading();
		}else{
			hideLoading1();
			ShowMsg("请先启动wifi！");
		}
	}
	
	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshWdjSearchDeviceAdapter(this, list);
		listView.setAdapter(adapter);
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
		filter.addAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
		filter.addAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
		registerReceiver(mReceiver, filter);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Constants.Action.LOCAL_DEVICE_SEARCH_END)){
				if(FList.getInstance().getSetPasswordLocalDevices().size() > 0){
					list.clear();
					list.addAll(FList.getInstance().getSetPasswordLocalDevices());
					adapter.notifyDataSetChanged();
					hideLoading2();
				}else{
					hideLoading1();
				}
			}else if(intent.getAction().equals(OsConstants.JSH.ADD_DEVICES_SUCCES)){
				finish();
			}
		}
	};
	
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
	public void onRefresh() {
		// TODO Auto-generated method stub
		 FList.getInstance().searchLocalDevice();
		 onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}
}
