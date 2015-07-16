package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbGrzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbSyzxActivity;
import p2p.cellcom.com.cn.bean.Device;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JshFragmentActivity extends FragmentActivity {
	private RelativeLayout wdjTab;// 我的家选项卡
	private RelativeLayout infoTab;// 信息选项卡
	private RelativeLayout syzxTab;// 商业中心选项卡
	private RelativeLayout grzxTab;// 个人中心选项卡

	private ImageView wdjTabIcon;
	private ImageView infoTabIcon;
	private ImageView syzxTabIcon;
	private ImageView grzxTabIcon;

	private TextView wdjTabLabel;
	private TextView infoTabLabel;
	private TextView syzxTabLabel;
	private TextView grzxTabLabel;
	private JshWdjActivity jshWdjActivity;
	private JshInfoActivity jshInfoActivity;
	private DhbSyzxActivity dhbSyzxActivity;
	private DhbGrzxActivity dhbGrzxActivity;

	private String wdj = "WDJ";
	private String jsh = "JSH";
	private String syzx = "SYZX";
	private String grzx = "GRZX";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_jsh_fragment);
		initView();
		initData();
		jshWdjActivity = new JshWdjActivity();
		showFragment(jshWdjActivity, wdj);
		initListener();
	}

	private void initData() {
		// TODO Auto-generated method stub
		wdjTabLabel.setText("我的家");
		infoTabLabel.setText("信息");
		syzxTabLabel.setText("商业中心");
		grzxTabLabel.setText("个人中心");
		wdjTabLabel.setTextColor(getResources().getColor(R.color.orange));
		wdjTabIcon
				.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_selected);
		infoTabIcon
				.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
		syzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
		grzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);
	}

	private void initView() {
		// TODO Auto-generated method stub
		wdjTab = (RelativeLayout) findViewById(R.id.wdjTab);
		infoTab = (RelativeLayout) findViewById(R.id.infoTab);
		syzxTab = (RelativeLayout) findViewById(R.id.syzxTab);
		grzxTab = (RelativeLayout) findViewById(R.id.grzxTab);

		wdjTabIcon = (ImageView) findViewById(R.id.wdjTabIcon);
		infoTabIcon = (ImageView) findViewById(R.id.infoTabIcon);
		syzxTabIcon = (ImageView) findViewById(R.id.syzxTabIcon);
		grzxTabIcon = (ImageView) findViewById(R.id.grzxTabIcon);

		wdjTabLabel = (TextView) findViewById(R.id.wdjTabLabel);
		infoTabLabel = (TextView) findViewById(R.id.infoTabLabel);
		syzxTabLabel = (TextView) findViewById(R.id.syzxTabLabel);
		grzxTabLabel = (TextView) findViewById(R.id.grzxTabLabel);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		// 我的家
		wdjTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (jshWdjActivity == null) {
					jshWdjActivity = new JshWdjActivity();
				}
				hiddleFragment(jshInfoActivity, jsh);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(jshWdjActivity, wdj);
				wdjTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_selected);
				infoTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdjTabLabel.setTextColor(getResources().getColor(R.color.orange));
				infoTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 信息
		infoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (jshInfoActivity == null) {
					jshInfoActivity = new JshInfoActivity();
				}
				hiddleFragment(jshWdjActivity, wdj);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(jshInfoActivity, jsh);
				wdjTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_unselected);
				infoTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_selected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdjTabLabel.setTextColor(getResources().getColor(R.color.gray));
				infoTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				// Toast.makeText(JshFragmentActivity.this, "功能正在开发中...",
				// Toast.LENGTH_SHORT).show();
			}
		});
		// 商业中心
		syzxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dhbSyzxActivity == null) {
					dhbSyzxActivity = new DhbSyzxActivity();
				}
				hiddleFragment(jshWdjActivity, wdj);
				hiddleFragment(jshInfoActivity, jsh);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(dhbSyzxActivity, syzx);
				wdjTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_unselected);
				infoTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_selected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdjTabLabel.setTextColor(getResources().getColor(R.color.gray));
				infoTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 个人中心
		grzxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dhbGrzxActivity == null) {
					dhbGrzxActivity = new DhbGrzxActivity();
				}
				hiddleFragment(jshWdjActivity, wdj);
				hiddleFragment(jshInfoActivity, jsh);
				hiddleFragment(dhbSyzxActivity, syzx);
				showFragment(dhbGrzxActivity, grzx);
				wdjTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_unselected);
				infoTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_selected);

				wdjTabLabel.setTextColor(getResources().getColor(R.color.gray));
				infoTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		getParent().onKeyDown(keyCode, event);
		return false;
	}

	// public void changeFragment(Fragment f,String tag) {
	// changeFragment(f, tag);
	// }

	// public void initFragment(Fragment f) {
	// changeFragment(f, true);
	// }

	private void hiddleFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		if (fragment != null && manager.findFragmentByTag(tag) != null) {
			transaction.hide(fragment);
			transaction.commitAllowingStateLoss();
		}
	}

	private void showFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		if (manager.findFragmentByTag(tag) == null) {
			transaction.add(R.id.simple_fragment, fragment, tag);
		}
		transaction.show(fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		// transaction.replace(R.id.simple_fragment, fragment);
		// if (!init)
		// transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	public void onDes(){
		if(jshWdjActivity!=null){
			jshWdjActivity.onDes();
		}
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (jshWdjActivity == null) {
			transaction.remove(jshWdjActivity);
		}
		if (jshInfoActivity == null) {
			transaction.remove(jshInfoActivity);
		}
		if (dhbSyzxActivity == null) {
			transaction.remove(dhbSyzxActivity);
		}
		if (dhbGrzxActivity == null) {
			transaction.remove(dhbGrzxActivity);
		}
	}
	// 设置
	public void jshSetDevice(Device device) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.jshSetDevice(device);
		}
	}

	// 编辑
	public void jshEditDevice(String deviceId) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.jshEditDevice(deviceId);
		}
	}

	public void jshShowDeleteDialog(int position) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.showDeleteDialog(position);
		}
	}

	// 回放
	public void jshRecord(Device device) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.jshRecord(device);
		}
	}

	// 播放
	public void jshPlay(Device device) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.jshPlay(device);
		}
	}

	// 删除设备
	public void DeleteDevice(int position) {
		// TODO Auto-generated method stub
		if (jshWdjActivity != null) {
			jshWdjActivity.showDeleteDialog(position);
		}
	}

	public void reflesh() {
		// TODO Auto-generated method stub
		if (dhbGrzxActivity != null) {
			dhbGrzxActivity.reflesh();
		}
	}
}