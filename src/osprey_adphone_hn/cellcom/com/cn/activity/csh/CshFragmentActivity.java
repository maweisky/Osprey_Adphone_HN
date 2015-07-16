package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbGrzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbSyzxActivity;
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
import android.widget.Toast;

public class CshFragmentActivity extends FragmentActivity {
	private RelativeLayout wdcTab;// 我的车选项卡
	private RelativeLayout fwxxTab;// 服务信息选项卡
	private RelativeLayout syzxTab;// 商业中心选项卡
	private RelativeLayout grzxTab;// 个人中心选项卡

	private ImageView wdcTabIcon;
	private ImageView fwxxTabIcon;
	private ImageView syzxTabIcon;
	private ImageView grzxTabIcon;

	private TextView wdcTabLabel;
	private TextView fwxxTabLabel;
	private TextView syzxTabLabel;
	private TextView grzxTabLabel;

	private CshWdcActivity wdcActivity;
	private CshFwxxActivity cshFwxxActivity;
	private DhbSyzxActivity dhbSyzxActivity;
	private DhbGrzxActivity dhbGrzxActivity;

	private String csh="CSH";
	private String fwxx="FWXX";
	private String syzx="SYZX";
	private String grzx="GRZX";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_csh_fragment);
		initView();
		initData();
		wdcActivity = new CshWdcActivity();
		showFragment(wdcActivity, csh);
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		// 我的车
		wdcTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(wdcActivity==null){					
					wdcActivity = new CshWdcActivity();
				}
				// replaceFragment(wdcActivity, "wdc");
				hiddleFragment(cshFwxxActivity, fwxx);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(wdcActivity, csh);
				wdcTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_selected);
				fwxxTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdcTabLabel.setTextColor(getResources().getColor(R.color.orange));
				fwxxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 信息
		fwxxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cshFwxxActivity==null){					
					cshFwxxActivity = new CshFwxxActivity();
				}
				hiddleFragment(wdcActivity, csh);
				hiddleFragment(dhbSyzxActivity, syzx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(cshFwxxActivity,fwxx);
				wdcTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_unselected);
				fwxxTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_selected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdcTabLabel.setTextColor(getResources().getColor(R.color.gray));
				fwxxTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				Toast.makeText(CshFragmentActivity.this, "功能正在开发中...",
						Toast.LENGTH_SHORT).show();
			}
		});
		// 商业中心
		syzxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(dhbSyzxActivity==null){					
					dhbSyzxActivity = new DhbSyzxActivity();
				}
				hiddleFragment(wdcActivity, csh);
				hiddleFragment(cshFwxxActivity, fwxx);
				hiddleFragment(dhbGrzxActivity, grzx);
				showFragment(dhbSyzxActivity,syzx);
				wdcTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_unselected);
				fwxxTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_selected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdcTabLabel.setTextColor(getResources().getColor(R.color.gray));
				fwxxTabLabel
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
				if(dhbGrzxActivity==null){					
					dhbGrzxActivity = new DhbGrzxActivity();
				}
				hiddleFragment(wdcActivity, csh);
				hiddleFragment(cshFwxxActivity, fwxx);
				hiddleFragment(dhbSyzxActivity, syzx);
				showFragment(dhbGrzxActivity,grzx);
				wdcTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_unselected);
				fwxxTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_selected);

				wdcTabLabel.setTextColor(getResources().getColor(R.color.gray));
				fwxxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		wdcTabLabel.setText("我的车");
		fwxxTabLabel.setText("服务信息");
		syzxTabLabel.setText("商业中心");
		grzxTabLabel.setText("个人中心");
		wdcTabLabel.setTextColor(getResources().getColor(R.color.orange));
		wdcTabIcon
				.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_selected);
		fwxxTabIcon
				.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
		syzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
		grzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);
	}

	private void initView() {
		// TODO Auto-generated method stub
		wdcTab = (RelativeLayout) findViewById(R.id.wdcTab);
		fwxxTab = (RelativeLayout) findViewById(R.id.fwxxTab);
		syzxTab = (RelativeLayout) findViewById(R.id.syzxTab);
		grzxTab = (RelativeLayout) findViewById(R.id.grzxTab);

		wdcTabIcon = (ImageView) findViewById(R.id.wdcTabIcon);
		fwxxTabIcon = (ImageView) findViewById(R.id.fwxxTabIcon);
		syzxTabIcon = (ImageView) findViewById(R.id.syzxTabIcon);
		grzxTabIcon = (ImageView) findViewById(R.id.grzxTabIcon);

		wdcTabLabel = (TextView) findViewById(R.id.wdcTabLabel);
		fwxxTabLabel = (TextView) findViewById(R.id.fwxxTabLabel);
		syzxTabLabel = (TextView) findViewById(R.id.syzxTabLabel);
		grzxTabLabel = (TextView) findViewById(R.id.grzxTabLabel);
	}

	private void hiddleFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		if(fragment!=null && manager.findFragmentByTag(tag)!=null){			
			transaction.hide(fragment);
			transaction.commitAllowingStateLoss();
		}
		
	}
	
	private void showFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		if(fragment!=null && manager.findFragmentByTag(tag)==null){
			transaction.add(R.id.simple_fragment,fragment, tag);
		}
		transaction.show(fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		
		
		
//		transaction.replace(R.id.simple_fragment, fragment);
//		if (!init)
//			transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		getParent().onKeyDown(keyCode, event);
		return false;
	}
	
	public void onDes(){
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (wdcActivity == null) {
			transaction.remove(wdcActivity);
		}
		if (cshFwxxActivity == null) {
			transaction.remove(cshFwxxActivity);
		}
		if (dhbSyzxActivity == null) {
			transaction.remove(dhbSyzxActivity);
		}
		if (dhbGrzxActivity == null) {
			transaction.remove(dhbGrzxActivity);
		}
	}
	
	public void reflesh() {
		// TODO Auto-generated method stub
		if(dhbGrzxActivity!=null){
			dhbGrzxActivity.reflesh();
		}
	}
}
