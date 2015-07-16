package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWdjPagerAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.DeviceInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.DeviceInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.SysComm;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.bean.LocalDevice;
import p2p.cellcom.com.cn.bean.Message;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.thread.MainThread;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxHttp.HttpWayMode;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.CellComAjaxResult.ParseType;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
/**
 * 家生活-我的家
 * @author Administrator
 *
 */
public class JshWdjActivity extends FragmentBase {

	private JshFragmentActivity act;

	private JazzyViewPager mJazzy;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem;
	private ArrayList<View> view_img;
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FinalBitmap finalBitmap;
	private FrameLayout fl_ad;

	private List<DeviceInfo> list = new ArrayList<DeviceInfo>();
	private List<View> dotList = new ArrayList<View>();
//	private FrameLayout fl_devices;
	private ImageView iv_add;
	private JazzyViewPager jvp_device;
	private JshWdjPagerAdapter jazzyAdapter;
	private LinearLayout ll_dots;
	private int dotPosition = -1;

	private boolean isRegFilter = false;
	private boolean isActive;
//	private Device device;
	
	private boolean isCancelLoading;
	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;
	private CustomProgressDialog customProgressDialog;
	private boolean isFirstRefresh = true;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (JshFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_jsh_wdj_activity, container,	false);
		initView(v, savedInstanceState);
		regFilter();
		if(isFirstRefresh){
			isFirstRefresh = !isFirstRefresh;
			FList flist = FList.getInstance();
			flist.updateOnlineState();
			flist.searchLocalDevice();
		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		MainThread.setOpenThread(false);
		isActive = false;
	}

	public void onDes() {
		// TODO Auto-generated method stub
		if (isRegFilter) {
			act.unregisterReceiver(mReceiver);
		}
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.REFRESH_CONTANTS);
		filter.addAction(Constants.Action.GET_FRIENDS_STATE);
		filter.addAction(Constants.Action.LOCAL_DEVICE_SEARCH_END);
		filter.addAction(Constants.Action.ACTION_NETWORK_CHANGE);
		filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
		filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
		filter.addAction(Constants.Action.SETTING_WIFI_SUCCESS);
		filter.addAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
		filter.addAction(OsConstants.JSH.REFRUSH_DEVICES_NOTICE);
		filter.addAction(OsConstants.JSH.REFRUSH_ADAPTER_NOTICE);
		act.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(OsConstants.JSH.REFRUSH_DEVICES_NOTICE)
					|| intent.getAction().equals(OsConstants.JSH.ADD_DEVICES_SUCCES)) {
				getDeviceInfos();
			} else if (intent.getAction().equals(	Constants.Action.GET_FRIENDS_STATE)) {
				LogMgr.showLog("检查状态成功！");
//				listAdapter.notifyDataSetChanged();
				jazzyAdapter.notifyDataSetChanged();
			} else if (intent.getAction().equals(OsConstants.JSH.REFRUSH_ADAPTER_NOTICE)) {
				LogMgr.showLog("更新状态成功！");
				jazzyAdapter.notifyDataSetChanged();
				updateOnlineState();
			} else if (intent.getAction().equals(	Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
				if (!isActive) {
					DismissProgressDialog();
					return;
				}
				int result = intent.getIntExtra("result", -1);
				if (!isCancelLoading) {
					LogMgr.showLog("ACK_RET_CHECK_PASSWORD, deviceID---------->" + OsConstants.deviceID);
					Device device = FList.getInstance().isDevice(OsConstants.deviceID);
					if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS 	&& OsConstants.isClickSet) {
						DismissProgressDialog();
						LogMgr.showLog("set device, device.getDeviceId()---------->" + device.getDeviceId());
						Intent control = new Intent();
						control.setClass(act, JshWdjSetActivity.class);
						control.putExtra("contact", device);
						control.putExtra("type", P2PValue.DeviceType.NPC);
						OsConstants.isClickSet = false;
						act.startActivity(control);
					}else if(result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS && OsConstants.isClickRecord){
						DismissProgressDialog();
						LogMgr.showLog("record device, device.getDeviceId()---------->" + device.getDeviceId());
//						Device device = FList.getInstance().isDevice(deviceID);
						Intent playback = new Intent();
						playback.setClass(act, JshPlayBackListActivity.class);
						playback.putExtra("contact", device);
						OsConstants.isClickRecord=false;
						act.startActivity(playback);
					}else if(result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS && OsConstants.isClickPlay){
						DismissProgressDialog();
						LogMgr.showLog("play device, device.getDeviceId()---------->" + device.getDeviceId());
						Intent monitor = new Intent();
						monitor.setClass(context, CallActivity.class);
						monitor.putExtra("callId", device.getDeviceId());
						monitor.putExtra("contactName", device.getDeviceName());
						monitor.putExtra("password", device.getDevicePassword());
						monitor.putExtra("isOutCall", true);
						monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
						OsConstants.isClickPlay=false;
						act.startActivity(monitor);
					}else if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
						DismissProgressDialog();
						LogMgr.showLog("ACK_PWD_ERROR, device.getDeviceId()---------->" + device.getDeviceId());
						device.setDefenceState(Constants.DefenceState.DEFENCE_STATE_WARNING_PWD);
						jazzyAdapter.notifyDataSetChanged();
						ShowMsg(R.string.os_password_error);
					} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
						LogMgr.showLog("ACK_NET_ERROR, device.getDeviceId()---------->" + device.getDeviceId());

						if (TextUtils.isEmpty(device.getDevicePassword())
										|| "null".equalsIgnoreCase(device.getDevicePassword())) {
							DismissProgressDialog();
						}else{							
							P2PHandler.getInstance().checkPassword(device.getDeviceId(), device.getDevicePassword());
						}
					} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
						DismissProgressDialog();
						ShowMsg(R.string.os_insufficient_permissions);
					}
				}else{
					DismissProgressDialog();
				}
			} else if (intent.getAction().equals(	Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
				int state = intent.getIntExtra("state", -1);
				String deviceId = intent.getStringExtra("deviceId");
				Device device = FList.getInstance().isDevice(deviceId);
				
				if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_NET) {
					if (null != device && device.isClickGetDefenceState()) {
						ShowMsg("网络异常！");
					}
				} else if (state == Constants.DefenceState.DEFENCE_STATE_WARNING_PWD) {
					if (null != device && device.isClickGetDefenceState()) {
						ShowMsg("密码错误！");
					}
				} else if (state == Constants.DefenceState.DEFENCE_NO_PERMISSION) {
					if (null != device && device.isClickGetDefenceState()) {
						ShowMsg("暂无权限！");
					}
				}

				if (null != device && device.isClickGetDefenceState()) {
					FList.getInstance().setIsClickGetDefenceState(deviceId,	false);
				}
				jazzyAdapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constants.Action.SETTING_WIFI_SUCCESS)){
				FList flist = FList.getInstance();
				flist.updateOnlineState();
				flist.searchLocalDevice();
			}
		}
	};

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
//		fl_devices = (FrameLayout) v.findViewById(R.id.fl_devices);
		mJazzy = (JazzyViewPager) v.findViewById(R.id.viewpager);
		jvp_device = (JazzyViewPager) v.findViewById(R.id.jvp_device);
//		listView = (ListView) v.findViewById(R.id.listview);
		iv_add = (ImageView) v.findViewById(R.id.iv_add);
		fl_ad = (FrameLayout) v.findViewById(R.id.fl_ad);
		dots_ll = (LinearLayout) v.findViewById(R.id.ll_dot);
		ll_dots = (LinearLayout) v.findViewById(R.id.ll_dots);

		ll_loading = (LinearLayout) v.findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) v.findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) imageView_loading
				.getBackground();

//		listView.addFooterView(initFooterView());

		initAdapter();
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(JshWdjAddDeviceActivity.class);
			}
		});
		jvp_device.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showDeleteDialog(dotPosition);
				return false;
			}
		});
		jvp_device.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				dotList.get(dotPosition).setBackgroundResource(R.drawable.app_dot_normal);
				dotList.get(position).setBackgroundResource(R.drawable.app_dot_focused);
				dotPosition = position;
			}
			
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		MainThread.setOpenThread(true);
		isActive = true;
		finalBitmap = FinalBitmap.create(act);
//		if (ContextUtil.getHeith(act) <= 480) {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 60);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 800) {
//			// if(ContextUtil.getWidth(this)<=480)
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 960) {
//			// if(ContextUtil.getWidth(this)<=480)
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 1280) {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 200);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 210);
//			fl_ad.setLayoutParams(linearParams);
//		}
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) fl_ad
            .getLayoutParams();
        linearParams.width=ContextUtil.getWidth(act);
        linearParams.height = linearParams.width/2;
        fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(act, finalBitmap, mJazzy, R.drawable.os_login_topicon);
		getAdv();
		getDeviceInfos();
	}

	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("pos", "5");
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETGG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(act, advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						advs = advComm.getBody();
						initJazzViewPager();
					}
				});
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				ContextUtil.dip2px(act, 8), ContextUtil.dip2px(act, 8));
		ll.setMargins(ContextUtil.dip2px(act, 1.5f), 0,
				ContextUtil.dip2px(act, 1.5f), 0);
		dots = new ArrayList<View>();
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = act.getLayoutInflater().inflate(
						R.layout.os_main_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setAdjustViewBounds(true);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, advs.get(i).getMeitiurl());
				final int tempPos=i;
				img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!TextUtils.isEmpty(advs.get(tempPos).getLinkurl())){
							Intent intent = new Intent(act, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
			}
		}
		if (view_img.size() > 0) {
			mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(0);
		}

		mJazzy.setOnPageChangeListener(new MyJazzyViewPager());
		// mJazzy.setPageMargin(30);
		// 创建定时器
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		// 首次启动时3秒后开始执行，接下来3秒执行一次
		scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 3, 5,
				TimeUnit.SECONDS);
	}

	/**
	 * 
	 * 路口截图改变ViewPager监听
	 * 
	 */
	public class MyJazzyViewPager implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
//			mJazzy.setCurrentItem(position);
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.app_dot_normal);
			dots.get(position)
					.setBackgroundResource(R.drawable.app_dot_focused);
			oldPosition = position;
		}
	}

	/**
	 * 
	 * 创建自动滚动广告线程
	 * 
	 */
	class ViewpagerTask implements Runnable {
		@Override
		public void run() {
			synchronized (mJazzy) {
				currentItem = (currentItem + 1) % view_img.size();
				handler.sendEmptyMessage(0);
			}
		}
	}

	/**
	 * 更新广告图片
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 根据viewpager里图片的 角标设置当前要显示的图片
			mJazzy.setCurrentItem(currentItem);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		jvp_device.removeAllViews();
		jazzyAdapter = new JshWdjPagerAdapter(getActivity(), FList.getInstance().list(),jvp_device);
		jvp_device.setAdapter(jazzyAdapter);
		initDots();
//		listAdapter = new JshWdjAdapter(getActivity(), FList.getInstance()	.list());
//		listView.setAdapter(listAdapter);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("JshWdjActivity","JshWdjActivity onResume deviceID------------->" + OsConstants.deviceID);
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
	}

	private void getDeviceInfos() {
		// TODO Auto-generated method stub
		String uid = SharepreferenceUtil.readString(getActivity(),
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETDEVICELIST,
				cellComAjaxParams, HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						showLoading();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						// TODO Auto-generated method stub
						try {
							DeviceInfoComm comm = t.read(DeviceInfoComm.class,
									ParseType.GSON);
							if (!comm.getReturnCode().equals("1")) {
								Toast.makeText(act, comm.getReturnMessage(),
										Toast.LENGTH_SHORT).show();
								return;
							}
							list.clear();
							if (comm.getBody().size() <= 0) {
								ShowMsg("暂无设备！");
							} else {
								list.addAll(comm.getBody());
//								ShowMsg("获取设备列表成功！");
							}
						} catch (Exception e) {
							// TODO: handle exception
							ShowMsg("服务器请求失败!");
						} finally {
							FList.getInstance().initList(list);
							initAdapter();
							hideLoading();
							updateOnlineState();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						hideLoading();
						ShowMsg("服务器请求失败!");
					}
				});
	}

	private void deleteDevice(final int position) {
		// TODO Auto-generated method stub
		final String deviceId = FList.getInstance().get(position).getDeviceId();
		String uid = SharepreferenceUtil.readString(getActivity(),
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("did", deviceId);
		HttpHelper.getInstances(getActivity()).send(FlowConsts.YYW_DELDEVICE,
				cellComAjaxParams, HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.app_progress);
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						try {
							SysComm comm = t.read(SysComm.class, ParseType.GSON);
							if (!comm.getReturnCode().equals("1")) {
								ShowMsg(comm.getReturnMessage());
								return;
							}
							
							FList.getInstance().delete(position);
							initAdapter();
//							jazzyAdapter.notifyDataSetChanged();
//							initDots();
							ShowMsg("删除设备成功！");
						} catch (Exception e) {
							// TODO: handle exception
							LogMgr.showLog("delete device error------------->" + e.toString());
							ShowMsg("服务器请求失败！");
						} finally {
							DismissProgressDialog();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						ShowMsg("服务器请求失败！");
					}
				});
	}

	private void showLoading() {
		if (animationDrawable != null) {
			ll_loading.setVisibility(View.VISIBLE);
//			listView.setVisibility(View.GONE);
//			fl_devices.setVisibility(View.GONE);
			animationDrawable.start();
		}
	}

	private void hideLoading() {
		if (animationDrawable != null) {
			ll_loading.setVisibility(View.GONE);
//			listView.setVisibility(View.VISIBLE);
//			fl_devices.setVisibility(View.VISIBLE);
			animationDrawable.stop();
		}
	}

	// 编辑
	public void jshEditDevice(String deviceId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), JshWdjEditActivity.class);
		intent.putExtra("deviceId", deviceId);
		startActivityForResult(intent, 1001);
	}

	public void showDeleteDialog(final int position) {
		if(FList.getInstance().list().size()>position){	
			ShowAlertDialog("删除", "确定要删除该设备吗？",
					new MyDialogInterface() {

						@Override
						public void onClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
//							String deviceId = FList.getInstance().get(position).getDeviceId();
							deleteDevice(position);
						}
					});
		}
	}

	private void updateOnlineState() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FList.getInstance().updateOnlineState();
			}
		}).start();
	}

	// 回放
	public void jshRecord(Device device) {
		OsConstants.deviceID = device.getDeviceId();
		LogMgr.showLog("record, deviceID------------->" + OsConstants.deviceID);
		LocalDevice localDevice = FList.getInstance().isContactUnSetPassword(device.getDeviceId());
		if (null != localDevice
				|| TextUtils.isEmpty(device.getDevicePassword())
				|| "null".equalsIgnoreCase(device.getDevicePassword())) {
			Device saveContact = new Device();
			saveContact.setDeviceId(device.getDeviceId());
			saveContact.setDeviceType(device.getDeviceType());
			saveContact.setMessageCount(0);
			saveContact.setActiveUser(NpcCommon.mThreeNum);

			Intent modify = new Intent(act, JshWdjEditActivity.class);
			modify.putExtra("isCreatePassword", true);
			modify.putExtra("deviceId", saveContact.getDeviceId());
			act.startActivity(modify);
		} else {
			customProgressDialog = ShowProgressDialog(R.string.hsc_progress);
			customProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					OsConstants.isClickRecord = false;
					isCancelLoading = true;
				}
			});
			OsConstants.isClickRecord = true;
			P2PHandler.getInstance().checkPassword(device.getDeviceId(),
					device.getDevicePassword());
			isCancelLoading = false;
		}
	}

	// 设置
	public void jshSetDevice(final Device device) {
		OsConstants.deviceID = device.getDeviceId();
		LogMgr.showLog("set, deviceID------------->" + OsConstants.deviceID);
		LocalDevice localDevice = FList.getInstance().isContactUnSetPassword(
				device.getDeviceId());
		if (null != localDevice
				|| TextUtils.isEmpty(device.getDevicePassword())
				|| "null".equalsIgnoreCase(device.getDevicePassword())) {
			Device saveContact = new Device();
			saveContact.setDeviceId(device.getDeviceId());
			saveContact.setDeviceType(device.getDeviceType());
			saveContact.setMessageCount(0);
			saveContact.setActiveUser(NpcCommon.mThreeNum);

			Intent modify = new Intent(act, JshWdjEditActivity.class);
			modify.putExtra("isCreatePassword", true);
			modify.putExtra("deviceId", saveContact.getDeviceId());
			act.startActivity(modify);
		} else {
			customProgressDialog = ShowProgressDialog(R.string.hsc_progress);
			customProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					OsConstants.isClickSet = false;
					isCancelLoading = true;
				}
			});
			OsConstants.isClickSet = true;
			P2PHandler.getInstance().checkPassword(device.getDeviceId(),
					device.getDevicePassword());
			isCancelLoading = false;
		}
	}
	//播放
	public void jshPlay(Device device) {
		// TODO Auto-generated method stub
		OsConstants.deviceID = device.getDeviceId();
		LogMgr.showLog("play, deviceID------------->" + OsConstants.deviceID);
		LocalDevice localDevice = FList.getInstance().isContactUnSetPassword(	device.getDeviceId());
		if (null != localDevice || TextUtils.isEmpty(device.getDevicePassword())
				|| "null".equalsIgnoreCase(device.getDevicePassword())) {
			Device saveContact = new Device();
			saveContact.setDeviceId(device.getDeviceId());
			saveContact.setDeviceType(device.getDeviceType());
			saveContact.setMessageCount(0);
			saveContact.setActiveUser(NpcCommon.mThreeNum);

			Intent modify = new Intent(act, JshWdjEditActivity.class);
			modify.putExtra("isCreatePassword", true);
			modify.putExtra("deviceId", saveContact.getDeviceId());
			act.startActivity(modify);
		} else {
			customProgressDialog = ShowProgressDialog(R.string.hsc_progress);
			customProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					OsConstants.isClickPlay = false;
					isCancelLoading = true;
				}
			});
			OsConstants.isClickPlay = true;
			P2PHandler.getInstance().checkPassword(device.getDeviceId(),
					device.getDevicePassword());
			isCancelLoading = false;
		}
	}
	
	private void initDots() {
		// TODO Auto-generated method stub
		dotList.clear();
		ll_dots.removeAllViews();
		dotPosition = 0;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ContextUtil.dip2px(act, 8), ContextUtil.dip2px(act, 8));
		lp.setMargins(ContextUtil.dip2px(act, 1.5f), 0,	ContextUtil.dip2px(act, 1.5f), 0);
		int count = FList.getInstance().list().size();
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(getActivity());
			iv_dot.setLayoutParams(lp);
			iv_dot.setBackgroundResource(R.drawable.app_dot_normal);
			if(i == 0){
				jvp_device.setCurrentItem(0);
				iv_dot.setBackgroundResource(R.drawable.app_dot_focused);
			}
			ll_dots.addView(iv_dot);
			dotList.add(iv_dot);
		}
	}
}