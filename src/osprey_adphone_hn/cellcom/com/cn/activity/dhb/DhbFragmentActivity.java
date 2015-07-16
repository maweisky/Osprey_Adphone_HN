package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DhbFragmentActivity extends FragmentActivity {
	private DhbBhpActivity dhbBhpActivity;
	private DhbContactActivity dhbContactActivity;
	private DhbSyzxActivity dhbSyzxActivity;
	private DhbGrzxActivity dhbGrzxActivity;
	private BroadcastReceiver receiveBroadCast;

	private RelativeLayout bhpTab;// 拨号盘选项卡
	private RelativeLayout contactTab;// 通讯录选项卡
	private RelativeLayout syzxTab;// 商业中心选项卡
	private RelativeLayout grzxTab;// 个人中心选项卡

	private ImageView bhpTabIcon;
	private ImageView contactTabIcon;
	private ImageView syzxTabIcon;
	private ImageView grzxTabIcon;

	private TextView bhpTabLabel;
	private TextView contactTabLabel;
	private TextView syzxTabLabel;
	private TextView grzxTabLabel;

	private String bhp = "DHB";
	private String contact = "CONTACT";
	private String syzx = "SYZX";
	private String grzx = "GRZX";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_dhb_fragment);
		initView();
		initData();
		dhbBhpActivity = new DhbBhpActivity();
		showFragment(dhbBhpActivity, bhp);
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		// 拨号盘
		bhpTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hiddleFragment(dhbContactActivity, contact);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(dhbBhpActivity, bhp);
				dhbBhpActivity.controlActivity();
				if (dhbBhpActivity.isIsshow()) {
					bhpTabIcon
							.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_downselected);
				} else {
					bhpTabIcon
							.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_upselected);
				}
				contactTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_contact_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				bhpTabLabel.setTextColor(getResources()
						.getColor(R.color.orange));
				contactTabLabel.setTextColor(getResources().getColor(
						R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 通讯录
		contactTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dhbContactActivity == null) {
					dhbContactActivity = new DhbContactActivity();
				}
				hiddleFragment(dhbBhpActivity, bhp);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(dhbContactActivity, contact);
				dhbBhpActivity.hideKeyboard();
				bhpTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_unselected);
				contactTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_contact_icon_selected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				bhpTabLabel.setTextColor(getResources().getColor(R.color.gray));
				contactTabLabel.setTextColor(getResources().getColor(
						R.color.orange));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
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
				hiddleFragment(dhbBhpActivity, bhp);
				hiddleFragment(dhbContactActivity, contact);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(dhbSyzxActivity, syzx);
				dhbBhpActivity.hideKeyboard();
				bhpTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_unselected);
				contactTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_contact_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_selected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				bhpTabLabel.setTextColor(getResources().getColor(R.color.gray));
				contactTabLabel.setTextColor(getResources().getColor(
						R.color.gray));
				syzxTabLabel.setTextColor(getResources().getColor(
						R.color.orange));
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
				hiddleFragment(dhbBhpActivity, bhp);
				hiddleFragment(dhbContactActivity, contact);
				hiddleFragment(dhbSyzxActivity, syzx);
				showFragment(dhbGrzxActivity, grzx);
				dhbBhpActivity.hideKeyboard();
				bhpTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_unselected);
				contactTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_contact_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_selected);

				bhpTabLabel.setTextColor(getResources().getColor(R.color.gray));
				contactTabLabel.setTextColor(getResources().getColor(
						R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel.setTextColor(getResources().getColor(
						R.color.orange));
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		bhpTab = (RelativeLayout) findViewById(R.id.bhpTab);
		contactTab = (RelativeLayout) findViewById(R.id.contactTab);
		syzxTab = (RelativeLayout) findViewById(R.id.syzxTab);
		grzxTab = (RelativeLayout) findViewById(R.id.grzxTab);

		bhpTabIcon = (ImageView) findViewById(R.id.bhpTabIcon);
		contactTabIcon = (ImageView) findViewById(R.id.contactTabIcon);
		syzxTabIcon = (ImageView) findViewById(R.id.syzxTabIcon);
		grzxTabIcon = (ImageView) findViewById(R.id.grzxTabIcon);

		bhpTabLabel = (TextView) findViewById(R.id.bhpTabLabel);
		contactTabLabel = (TextView) findViewById(R.id.contactTabLabel);
		syzxTabLabel = (TextView) findViewById(R.id.syzxTabLabel);
		grzxTabLabel = (TextView) findViewById(R.id.grzxTabLabel);
	}

	private void initData() {
		// TODO Auto-generated method stub
		receiveBroadCast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				iconDeal();
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction("osprey_adphone_hn.cellcom.com.cn.keyborad");
		// 只有持有相同的action的接受者才能接收此广播
		registerReceiver(receiveBroadCast, filter);

		bhpTabLabel.setText("拨号盘");
		contactTabLabel.setText("通讯录");
		syzxTabLabel.setText("商业中心");
		grzxTabLabel.setText("个人中心");

		bhpTabLabel.setTextColor(getResources().getColor(R.color.orange));
		bhpTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_downselected);
		contactTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_contact_icon_unselected);
		syzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
		grzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);
	}

	public void iconDeal() {
		// TODO Auto-generated method stub
		if (dhbBhpActivity.isIsshow()) {
			bhpTabIcon
					.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_downselected);
		} else {
			bhpTabIcon
					.setBackgroundResource(R.drawable.os_dhb_bottom_bhp_icon_upselected);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiveBroadCast != null) {
			unregisterReceiver(receiveBroadCast);
		}
	}

	public void onDes() {
		// TODO Auto-generated method stub
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (dhbBhpActivity == null) {
			transaction.remove(dhbBhpActivity);
		}
		if (dhbContactActivity == null) {
			transaction.remove(dhbContactActivity);
		}
		if (dhbSyzxActivity == null) {
			transaction.remove(dhbSyzxActivity);
		}
		if (dhbGrzxActivity == null) {
			transaction.remove(dhbGrzxActivity);
		}
	}

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

	public void reflesh() {
		// TODO Auto-generated method stub
		if (dhbContactActivity != null) {
			dhbContactActivity.reflesh();
		}
		if (dhbBhpActivity != null) {
			dhbBhpActivity.reflesh();
		}
		if (dhbGrzxActivity != null) {
			dhbGrzxActivity.reflesh();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		getParent().onKeyDown(keyCode, event);
		return false;
	}
}
