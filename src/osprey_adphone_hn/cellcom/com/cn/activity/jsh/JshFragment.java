package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbBhpActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbContactActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbGrzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbSyzxActivity;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class JshFragment extends Fragment {
	private JshFragmentActivity act;

	private TabHost mHost;
	private LocalActivityManager lam;

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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (JshFragmentActivity) activity;
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
		View v = inflater.inflate(R.layout.os_jsh_fragment, container, false);
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
		// 我的家
		wdjTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(0);
				wdjTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_selected);
				infoTabIcon
						.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
				syzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
				grzxTabIcon
						.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);

				wdjTabLabel.setTextColor(getResources().getColor(R.color.blue));
				infoTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				syzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				grzxTabLabel
						.setTextColor(getResources().getColor(R.color.gray));
				Toast.makeText(act, "功能正在开发中...", Toast.LENGTH_SHORT).show();
			}
		});
		// 信息
		infoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHost.setCurrentTab(1);
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
		wdjTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		wdjTabLabel = (TextView) wdjTab.findViewById(R.id.tab_label);
		wdjTabIcon = (ImageView) wdjTab.findViewById(R.id.tab_icon);
		wdjTabLabel.setText("我的家");

		infoTab = (RelativeLayout) LayoutInflater.from(act).inflate(
				R.layout.os_main_bottom_menu, null);
		infoTabLabel = (TextView) infoTab.findViewById(R.id.tab_label);
		infoTabIcon = (ImageView) infoTab.findViewById(R.id.tab_icon);
		infoTabLabel.setText("信息");

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
		mHost.addTab(mHost.newTabSpec("dhb_wdj").setIndicator(wdjTab)
				.setContent(new Intent(act, JshWdjActivity.class)));
		mHost.addTab(mHost.newTabSpec("dhb_contact").setIndicator(infoTab)
				.setContent(new Intent(act, JshInfoActivity.class)));

		mHost.addTab(mHost.newTabSpec("dhb_syzx").setIndicator(syzxTab)
				.setContent(new Intent(act, DhbSyzxActivity.class)));
		mHost.addTab(mHost.newTabSpec("dhb_grzx").setIndicator(grzxTab)
				.setContent(new Intent(act, DhbGrzxActivity.class)));
		mHost.setCurrentTab(0);
		wdjTabLabel.setTextColor(getResources().getColor(R.color.blue));
		wdjTabIcon
				.setBackgroundResource(R.drawable.os_jsh_bottom_wdj_icon_selected);
		infoTabIcon
				.setBackgroundResource(R.drawable.os_jsh_bottom_info_icon_unselected);
		syzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_syzx_icon_unselected);
		grzxTabIcon
				.setBackgroundResource(R.drawable.os_dhb_bottom_grzx_icon_unselected);
	}

}
