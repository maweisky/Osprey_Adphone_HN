package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbGrzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbSyzxActivity;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class CshFragment extends Fragment {
	private CshFragmentActivity act;

	private TabHost mHost;
	private LocalActivityManager lam;

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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (CshFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_csh_fragment, container, false);
		initView(v, savedInstanceState);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		// 我的车
		wdcTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(0);
				wdcTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_selected);
				fwxxTabIcon
						.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdcTabLabel.setTextColor(getResources().getColor(R.color.blue));
				fwxxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				Toast.makeText(act, "功能正在开发中...", Toast.LENGTH_SHORT).show();
			}
		});
		// 信息
		fwxxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(1);
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
						.setTextColor(getResources().getColor(R.color.blue));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				Toast.makeText(act, "功能正在开发中...", Toast.LENGTH_SHORT).show();
			}
		});
		// 商业中心
		syzxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(2);
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
						.setTextColor(getResources().getColor(R.color.blue));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 个人中心
		grzxTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(3);
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
						.setTextColor(getResources().getColor(R.color.blue));
			}
		});
	}

	/**
	 * 初始化控件
	 * 
	 * @param v
	 */
	private void initView(View v, Bundle inState) {
		// TODO Auto-generated method stub
		mHost = (TabHost) v.findViewById(android.R.id.tabhost);
		wdcTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		wdcTabLabel = (TextView) wdcTab.findViewById(R.id.tab_label);
		wdcTabIcon = (ImageView) wdcTab.findViewById(R.id.tab_icon);
		wdcTabLabel.setText("我的车");

		fwxxTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		fwxxTabLabel = (TextView) fwxxTab.findViewById(R.id.tab_label);
		fwxxTabIcon = (ImageView) fwxxTab.findViewById(R.id.tab_icon);
		fwxxTabLabel.setText("服务信息");

		syzxTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		syzxTabLabel = (TextView) syzxTab.findViewById(R.id.tab_label);
		syzxTabIcon = (ImageView) syzxTab.findViewById(R.id.tab_icon);
		syzxTabLabel.setText("商业中心");

		grzxTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		grzxTabLabel = (TextView) grzxTab.findViewById(R.id.tab_label);
		grzxTabIcon = (ImageView) grzxTab.findViewById(R.id.tab_icon);
		grzxTabLabel.setText("个人中心");

		lam = new LocalActivityManager(act, false);
		lam.dispatchCreate(inState);
		mHost.setup(lam);
		mHost.addTab(mHost.newTabSpec("dhb_wdj").setIndicator(wdcTab)
				.setContent(new Intent(act, CshWdcActivity.class)));
		mHost.addTab(mHost.newTabSpec("dhb_contact").setIndicator(fwxxTab)
				.setContent(new Intent(act, CshFwxxActivity.class)));

		mHost.addTab(mHost.newTabSpec("dhb_syzx").setIndicator(syzxTab)
				.setContent(new Intent(act, DhbSyzxActivity.class)));
		mHost.addTab(mHost.newTabSpec("dhb_grzx").setIndicator(grzxTab)
				.setContent(new Intent(act, DhbGrzxActivity.class)));
		mHost.setCurrentTab(0);
		wdcTabLabel.setTextColor(getResources().getColor(R.color.blue));
		wdcTabIcon
				.setBackgroundResource(R.drawable.os_csh_bottom_wdc_icon_selected);
		fwxxTabIcon
				.setBackgroundResource(R.drawable.os_csh_bottom_fwxx_icon_unselected);
		syzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
		grzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);
		Toast.makeText(act, "功能正在开发中...", Toast.LENGTH_SHORT).show();
	}
}
