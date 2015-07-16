package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWdjAddAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.AddItem;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.zxing.activity.CaptureAddActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class JshWdjAddOnlineActivity extends ActivityFrame {

	private ListView listView;
	private JshWdjAddAdapter adapter;
	private List<AddItem> list = new ArrayList<AddItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_add);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		HideSet();
		initView();
		initListener();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.listview);
		initAdapter();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					OpenActivityForResult(CaptureAddActivity.class, 1001);
					break;
				case 1:
					OpenActivity(JshWdjEditActivity.class);
					break;
				case 2:
					OpenActivity(JshWdjSearchDevicesActivity.class);
					break;

				default:
					break;
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_add_ylwsb));
		
//		AddItem addItem1 = new AddItem();
//		addItem1.setName(getString(R.string.os_jsh_wdj_add_ldss));
//		addItem1.setDrawableID(R.drawable.os_jsh_wdj_add_ldss);
//		list.add(addItem1);

//		AddItem addItem2 = new AddItem();
//		addItem2.setName(getString(R.string.os_jsh_wdj_add_wifi));
//		addItem2.setDrawableID(R.drawable.os_jsh_wdj_add_wifi);
//		list.add(addItem2);
		AddItem addItem3 = new AddItem();
		addItem3.setName(getString(R.string.os_jsh_wdj_add_smewm));
		addItem3.setDrawableID(R.drawable.os_jsh_wdj_add_smewm);
		list.add(addItem3);
		AddItem addItem4 = new AddItem();
		addItem4.setName(getString(R.string.os_jsh_wdj_add_sdtj));
		addItem4.setDrawableID(R.drawable.os_jsh_wdj_add_sdtj);
		list.add(addItem4);
		AddItem addItem5 = new AddItem();
		addItem5.setName(getString(R.string.os_jsh_wdj_add_ssjyw));
		addItem5.setDrawableID(R.drawable.os_jsh_wdj_add_ssjyw);
		list.add(addItem5);
		adapter.notifyDataSetChanged();
		regFliter();

	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshWdjAddAdapter(this, list);
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
		case 1001:
			Intent intent = new Intent(this, JshWdjEditActivity.class);
			intent.putExtra("deviceId", data.getStringExtra("deviceId"));
			startActivity(intent);
			break;

		default:
			break;
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
