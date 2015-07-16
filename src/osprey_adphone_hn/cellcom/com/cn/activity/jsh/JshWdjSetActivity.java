package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWdjAddAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.AddItem;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.bean.Device;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 设置
 * 
 * @author wma
 * 
 */
public class JshWdjSetActivity extends ActivityFrame {
	private RelativeLayout toprl;
	private ListView listView;
	private JshWdjAddAdapter adapter;
	private List<AddItem> list = new ArrayList<AddItem>();
	private ImageView video_img;
	private TextView videotv;
	private FinalBitmap finalBitmap;
	private Device device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_set);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		toprl=(RelativeLayout)findViewById(R.id.toprl);
		listView = (ListView) findViewById(R.id.listview);
		video_img = (ImageView) findViewById(R.id.video_img);
		videotv = (TextView) findViewById(R.id.videotv);
		initAdapter();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		toprl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent modify = new Intent(JshWdjSetActivity.this, JshWdjEditActivity.class);
				modify.putExtra("deviceId", device.getDeviceId());
				startActivity(modify);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					//报警设置
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshWdjBjSetActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (position == 1) {
					//录像设置
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshWdjLxSetActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				} else if (position == 2) {
					//防区设置
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshWdjAreaSetActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				}else if(position==3){
					//时间设置
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshWdjTimeSetActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				}else if(position==4){
					//网络设置
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshWdjNetSetActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				}else if (position == 5) {
					//修改密码
					Intent intent = new Intent(JshWdjSetActivity.this,
							JshModifyPwdActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("contact", device);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");
		finalBitmap=FinalBitmap.create(JshWdjSetActivity.this);
		LogMgr.showLog("device==>"+device.getDeviceId());
		if(device!=null&&!TextUtils.isEmpty(device.getServerImgUrl())){		
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
		AddItem addItem1 = new AddItem();
		addItem1.setName(getString(R.string.os_jsh_wdj_sbsz_bjsz));
		addItem1.setDrawableID(R.drawable.os_jsh_wdj_sbsz_bjszicon);
		list.add(addItem1);
		AddItem addItem2 = new AddItem();
		addItem2.setName(getString(R.string.os_jsh_wdj_sbsz_lxsz));
		addItem2.setDrawableID(R.drawable.os_jsh_wdj_sbsz_lxszicon);
		list.add(addItem2);
		AddItem addItem3 = new AddItem();
		addItem3.setName(getString(R.string.os_jsh_wdj_sbsz_fqsz));
		addItem3.setDrawableID(R.drawable.os_jsh_wdj_sbsz_fqszicon);
		list.add(addItem3);
		AddItem addItem4 = new AddItem();
		addItem4.setName(getString(R.string.os_jsh_wdj_sbsz_sjsz));
		addItem4.setDrawableID(R.drawable.os_jsh_wdj_sbsz_sjszicon);
		list.add(addItem4);
		AddItem addItem5 = new AddItem();
		addItem5.setName(getString(R.string.os_jsh_wdj_sbsz_wlsz));
		addItem5.setDrawableID(R.drawable.os_jsh_wdj_sbsz_wlszicon);
		list.add(addItem5);
		AddItem addItem6 = new AddItem();
		addItem6.setName(getString(R.string.os_jsh_wdj_sbsz_mmsz));
		addItem6.setDrawableID(R.drawable.os_jsh_wdj_sbsz_mmszicon);
		list.add(addItem6);
		adapter.notifyDataSetChanged();
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshWdjAddAdapter(this, list);
		listView.setAdapter(adapter);
	}
}