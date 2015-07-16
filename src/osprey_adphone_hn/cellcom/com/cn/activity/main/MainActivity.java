package osprey_adphone_hn.cellcom.com.cn.activity.main;

import net.tsz.afinal.FinalBitmap;

import org.afinal.simplecache.NetUtils;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.csh.CshFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.JshFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvShowComm;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class MainActivity extends ActivityFrame {

	private TabHost mHost;
	private LocalActivityManager lam;

	private RelativeLayout dhbTab;// 电话煲选项卡
	private RelativeLayout jshTab;// 家生活选项卡
	private RelativeLayout cshTab;// 车生活选项卡
	private TextView dhbTabLabel;
	private TextView jshTabLabel;
	private TextView cshTabLabel;

	
	private FinalBitmap finalBitmap;
	private Dialog ad;
	private MyCount myCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.main);
		AppendTitleBody1();
		SetTopBarTitle(getResources().getString(R.string.app_name));
		lam = new LocalActivityManager(MainActivity.this, false);
		lam.dispatchCreate(savedInstanceState);
		initView(savedInstanceState);
		initData();
		initListener();
	}

	// 获取个人资料
	protected void getGrzl() {
		final String uid = SharepreferenceUtil.readString(MainActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(MainActivity.this).send(
				FlowConsts.YYW_USERINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						initValue(userInfoComm.getBody(),uid);
						if (SharepreferenceUtil.readString(MainActivity.this,
								SharepreferenceUtil.fileName, "huafei", "")
								.equals("")) {
							tv_kyhf.setText("￥0");
						} else {
							tv_kyhf.setText("￥"
									+ SharepreferenceUtil.readString(
											MainActivity.this,
											SharepreferenceUtil.fileName,
											"huafei", ""));
						}
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DhbFragmentActivity dhbactivit = (DhbFragmentActivity) lam
				.getActivity("dhb");
		JshFragmentActivity jshactivit = (JshFragmentActivity) lam
				.getActivity("jsh");
		CshFragmentActivity cshactivit = (CshFragmentActivity) lam
				.getActivity("csh");
		if(dhbactivit!=null){
			dhbactivit.reflesh();
		}
		if(jshactivit!=null){
			jshactivit.reflesh();
		}
		if(cshactivit!=null){
			cshactivit.reflesh();
		}
		getGrzl();
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"huafei", "").equals("")) {
			tv_kyhf.setText("￥0");
		} else {
			tv_kyhf.setText("￥"
					+ SharepreferenceUtil.readString(this,
							SharepreferenceUtil.fileName, "huafei", ""));
		}
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"yinyuan", "").equals("")) {
			tv_kyyy.setText("0个");
		} else {
			tv_kyyy.setText(SharepreferenceUtil.readString(this,
					SharepreferenceUtil.fileName, "yinyuan", "") + "个");
		}
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"jifen", "").equals("")) {
			tv_kyjf.setText("0分");
		} else {
			tv_kyjf.setText(SharepreferenceUtil.readString(this,
					SharepreferenceUtil.fileName, "jifen", "") + "分");
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView(Bundle inState) {
		mHost = (TabHost) findViewById(android.R.id.tabhost);
		dhbTab = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.os_main_top_menu, null);
		dhbTabLabel = (TextView) dhbTab.findViewById(R.id.tab_label);

		dhbTabLabel.setText("电话煲");

		jshTab = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.os_main_top_menu, null);
		jshTabLabel = (TextView) jshTab.findViewById(R.id.tab_label);

		jshTabLabel.setText("家生活");

		cshTab = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.os_main_top_menu, null);
		cshTabLabel = (TextView) cshTab.findViewById(R.id.tab_label);
		cshTabLabel.setText("车生活");

		lam = new LocalActivityManager(MainActivity.this, false);
		lam.dispatchCreate(inState);
		mHost.setup(lam);
		mHost.addTab(mHost
				.newTabSpec("dhb")
				.setIndicator(dhbTab)
				.setContent(
						new Intent(MainActivity.this, DhbFragmentActivity.class)));
		mHost.addTab(mHost
				.newTabSpec("jsh")
				.setIndicator(jshTab)
				.setContent(
						new Intent(MainActivity.this, JshFragmentActivity.class)));

		mHost.addTab(mHost
				.newTabSpec("csh")
				.setIndicator(cshTab)
				.setContent(
						new Intent(MainActivity.this, CshFragmentActivity.class)));
		String tag=getIntent().getStringExtra("tag");
		if("dhb".equals(tag)){
			mHost.setCurrentTab(0);
//			dhbTab.setBackgroundResource(R.color.blue);
//			jshTab.setBackgroundResource(R.color.transparent);
//			cshTab.setBackgroundResource(R.color.transparent);
			
			Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			dhbTabLabel.setCompoundDrawables(null,null,null,drawable);  
			jshTabLabel.setCompoundDrawables(null,null,null,null); 
			cshTabLabel.setCompoundDrawables(null,null,null,null);

			dhbTabLabel
					.setTextColor(getResources().getColor(R.color.orange));
			jshTabLabel.setTextColor(getResources().getColor(R.color.gray));
			cshTabLabel.setTextColor(getResources().getColor(R.color.gray));
		}else if("jsh".equals(tag)){
			mHost.setCurrentTab(1);
//			dhbTab.setBackgroundResource(R.color.transparent);
//			jshTab.setBackgroundResource(R.color.blue);
//			cshTab.setBackgroundResource(R.color.transparent);

			Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			dhbTabLabel.setCompoundDrawables(null,null,null,null);
			jshTabLabel.setCompoundDrawables(null,null,null,drawable);
			cshTabLabel.setCompoundDrawables(null,null,null,null);
			
			dhbTabLabel.setTextColor(getResources().getColor(R.color.gray));
			jshTabLabel
					.setTextColor(getResources().getColor(R.color.orange));
			cshTabLabel.setTextColor(getResources().getColor(R.color.gray));
		}else if("csh".equals(tag)){
			mHost.setCurrentTab(2);
//			dhbTab.setBackgroundResource(R.color.transparent);
//			jshTab.setBackgroundResource(R.color.transparent);
//			cshTab.setBackgroundResource(R.color.blue);

			Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
			/// 这一步必须要做,否则不会显示.  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			dhbTabLabel.setCompoundDrawables(null,null,null,null);
			jshTabLabel.setCompoundDrawables(null,null,null,null);
			cshTabLabel.setCompoundDrawables(null,null,null,drawable);
			
			dhbTabLabel.setTextColor(getResources().getColor(R.color.gray));
			jshTabLabel.setTextColor(getResources().getColor(R.color.gray));
			cshTabLabel
					.setTextColor(getResources().getColor(R.color.orange));
		}
//		dhbTabLabel.setTextColor(getResources().getColor(R.color.white));
//		dhbTab.setBackgroundResource(R.color.blue);
//		jshTab.setBackgroundResource(R.color.transparent);
//		cshTab.setBackgroundResource(R.color.transparent);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// 电话煲
		dhbTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid=SharepreferenceUtil.readString(MainActivity.this, SharepreferenceUtil.fileName, "uid");
				playAdv(uid,"1");
				mHost.setCurrentTab(0);
				Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
				/// 这一步必须要做,否则不会显示.  
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
				dhbTabLabel.setCompoundDrawables(null,null,null,drawable);  
				jshTabLabel.setCompoundDrawables(null,null,null,null); 
				cshTabLabel.setCompoundDrawables(null,null,null,null);
//				dhbTab.setBackgroundResource(R.color.blue);
//				jshTab.setBackgroundResource(R.color.transparent);
//				cshTab.setBackgroundResource(R.color.transparent);

				dhbTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
				jshTabLabel.setTextColor(getResources().getColor(R.color.gray));
				cshTabLabel.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 家生活
		jshTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid=SharepreferenceUtil.readString(MainActivity.this, SharepreferenceUtil.fileName, "uid");
				playAdv(uid,"2");
				mHost.setCurrentTab(1);
//				dhbTab.setBackgroundResource(R.color.transparent);
//				jshTab.setBackgroundResource(R.color.blue);
//				cshTab.setBackgroundResource(R.color.transparent);
				
				Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
				/// 这一步必须要做,否则不会显示.  
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				dhbTabLabel.setCompoundDrawables(null,null,null,null);
				jshTabLabel.setCompoundDrawables(null,null,null,drawable);
				cshTabLabel.setCompoundDrawables(null,null,null,null);
				
				dhbTabLabel.setTextColor(getResources().getColor(R.color.gray));
				jshTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
				cshTabLabel.setTextColor(getResources().getColor(R.color.gray));
			}
		});
		// 车生活
		cshTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid=SharepreferenceUtil.readString(MainActivity.this, SharepreferenceUtil.fileName, "uid");
				playAdv(uid,"3");
				mHost.setCurrentTab(2);
//				dhbTab.setBackgroundResource(R.color.transparent);
//				jshTab.setBackgroundResource(R.color.transparent);
//				cshTab.setBackgroundResource(R.color.blue);

				Drawable drawable= getResources().getDrawable(R.drawable.os_topbar_nav_selected);  
				/// 这一步必须要做,否则不会显示.  
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				dhbTabLabel.setCompoundDrawables(null,null,null,null);
				jshTabLabel.setCompoundDrawables(null,null,null,null);
				cshTabLabel.setCompoundDrawables(null,null,null,drawable);
				
				dhbTabLabel.setTextColor(getResources().getColor(R.color.gray));
				jshTabLabel.setTextColor(getResources().getColor(R.color.gray));
				cshTabLabel
						.setTextColor(getResources().getColor(R.color.orange));
			}
		});
	}

	// 显示宣传图片
	private void initAdv(String url) {
		ad = new Dialog(this, R.style.Transparent);
		View v = LayoutInflater.from(this).inflate(R.layout.mainplan, null);
		final ImageView iv = (ImageView) v.findViewById(R.id.iv);
		finalBitmap.display(iv, url);
//		iv.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ad.cancel();
//			}
//		});
		ad.setContentView(v);
		ad.show();
	}

	// 播放广告
	private void playAdv(final String account,String type) {
		// 无网络拨打系统电话
		if (NetUtils.isConnected(MainActivity.this)) {
			CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
			cellComAjaxParams.put("uid", account);
			cellComAjaxParams.put("type", type);
			HttpHelper.getInstances(MainActivity.this).send(
					FlowConsts.YYW_HOMETIME, cellComAjaxParams,
					CellComAjaxHttp.HttpWayMode.POST,
					new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

						@Override
						public void onSuccess(CellComAjaxResult arg0) {
							// TODO Auto-generated method stub
							AdvShowComm advShowComm = arg0.read(AdvShowComm.class,
									CellComAjaxResult.ParseType.GSON);
							String state = advShowComm.getReturnCode();
							String msg = advShowComm.getReturnMessage();
							if (!FlowConsts.STATUE_1.equals(state)) {
								DismissProgressDialog();
								ShowMsg(msg);
								return;
							}
							myCount.start();
							if("Y".equalsIgnoreCase(advShowComm.getBody().getIfshow())){							
								initAdv(advShowComm.getBody().getImgurl());
							}
//							initAdv(advShowComm.getBody().getImgurl());
						}
					});
		}
	}

	@Override
	public void onBackPressed() {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		MainActivity.this.startActivity(mHomeIntent);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		finalBitmap = FinalBitmap.create(MainActivity.this);
		myCount = new MyCount(3000, 1000);
		
//		String channel=ChannelUtil.getChannel(MainActivity.this);
	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			
			if(ad!=null ){
				ad.cancel();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		JshFragmentActivity jshactivit = (JshFragmentActivity) lam
				.getActivity("jsh");
		if(jshactivit!=null){
			jshactivit.onDes();
		}
		DhbFragmentActivity dhbactivit = (DhbFragmentActivity) lam
				.getActivity("dhb");
		if(dhbactivit!=null){
			dhbactivit.onDes();
		}
		CshFragmentActivity cshactivit = (CshFragmentActivity) lam
				.getActivity("csh");
		if(cshactivit!=null){
			cshactivit.onDes();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			MainActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}