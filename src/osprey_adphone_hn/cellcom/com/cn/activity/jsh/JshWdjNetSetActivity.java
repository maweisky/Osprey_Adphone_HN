package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.WifiAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.LoadProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.MyListView;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.thread.DelayThread;
import p2p.cellcom.com.cn.utils.T;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;

/**
 * 网络设置
 * 
 * @author wma
 * 
 */
public class JshWdjNetSetActivity extends ActivityFrame implements
		OnClickListener {
	private ImageView video_img;
	private TextView videotv;
	private Device device;
	private boolean isRegFilter = false;
	RelativeLayout net_type_bar, list_wifi_bar;
	LinearLayout net_type_radio, list_wifi_content;
	ProgressBar progressBar_net_type, progressBar_list_wifi;
	RadioButton radio_one, radio_two;

	WifiAdapter mAdapter;
	MyListView list;
	int last_net_type;
	int last_modify_net_type;
	int last_modify_wifi_type;
	String last_modify_wifi_name;
	String last_modify_wifi_password;
	FinalBitmap finalBitmap;
	private LoadProgressDialog loadWifiProgressDialog;
	private LoadProgressDialog loadNetTypeProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_netset);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		initView();
		initData();
		initLis();
		regFilter();

		showProgress_net_type();

		P2PHandler.getInstance().getNpcSettings(device.getDeviceId(),
				device.getDevicePassword());
	}

	private void initLis() {
		// TODO Auto-generated method stub
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz_wlsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");
		finalBitmap = FinalBitmap.create(JshWdjNetSetActivity.this);
		if (!TextUtils.isEmpty(device.getServerImgUrl())) {
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
	}

	private void initView() {
		// TODO Auto-generated method stub
		videotv = (TextView) findViewById(R.id.videotv);
		video_img = (ImageView) findViewById(R.id.video_img);

		net_type_bar = (RelativeLayout) findViewById(R.id.net_type_bar);
		list_wifi_bar = (RelativeLayout) findViewById(R.id.list_wifi_bar);

		net_type_radio = (LinearLayout) findViewById(R.id.net_type_radio);
		list_wifi_content = (LinearLayout) findViewById(R.id.list_wifi_content);

		progressBar_net_type = (ProgressBar) findViewById(R.id.progressBar_net_type);
		progressBar_list_wifi = (ProgressBar) findViewById(R.id.progressBar_list_wifi);

		list = (MyListView) findViewById(R.id.list_wifi);
		mAdapter = new WifiAdapter(JshWdjNetSetActivity.this);
		list.setAdapter(mAdapter);

		radio_one = (RadioButton) findViewById(R.id.radio_one);
		radio_two = (RadioButton) findViewById(R.id.radio_two);
		radio_one.setOnClickListener(this);
		radio_two.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();

		filter.addAction(Constants.Action.CLOSE_INPUT_DIALOG);
		filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

		filter.addAction(Constants.P2P.ACK_RET_SET_NET_TYPE);
		filter.addAction(Constants.P2P.RET_SET_NET_TYPE);
		filter.addAction(Constants.P2P.RET_GET_NET_TYPE);

		filter.addAction(Constants.P2P.ACK_RET_SET_WIFI);
		filter.addAction(Constants.P2P.ACK_RET_GET_WIFI);
		filter.addAction(Constants.P2P.RET_SET_WIFI);
		filter.addAction(Constants.P2P.RET_GET_WIFI);

		JshWdjNetSetActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.Action.CLOSE_INPUT_DIALOG)) {
				// if(null!=dialog_input){
				// dialog_input.hide(dialog_input_mask);
				// }
			} else if (intent.getAction()
					.equals(Constants.P2P.RET_GET_NET_TYPE)) {
				int type = intent.getIntExtra("type", -1);
				if (type == Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIRED) {
					last_net_type = Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIRED;
					radio_one.setChecked(true);
					if (device.getDeviceType() == P2PValue.DeviceType.DOORBELL
							|| device.getDeviceType() == P2PValue.DeviceType.IPC) {
						showProgressWiFiList();
						P2PHandler.getInstance().getWifiList(
								device.getDeviceId(),
								device.getDevicePassword());
					} else {
						hideWiFiList();
					}
				} else if (type == Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI) {
					last_net_type = Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI;
					radio_two.setChecked(true);
					showProgressWiFiList();
					P2PHandler.getInstance().getWifiList(device.getDeviceId(),
							device.getDevicePassword());
				}
				showNetType();
				setRadioEnable(true);
			} else if (intent.getAction()
					.equals(Constants.P2P.RET_SET_NET_TYPE)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.NET_TYPE_SET.SETTING_SUCCESS) {
					last_net_type = last_modify_net_type;
					if (last_modify_net_type == Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI) {
						showProgressWiFiList();
						P2PHandler.getInstance().getWifiList(
								device.getDeviceId(),
								device.getDevicePassword());
						radio_two.setChecked(true);
					} else {
						hideWiFiList();
						radio_one.setChecked(true);
					}
					T.showShort(JshWdjNetSetActivity.this,
							R.string.os_modify_success);
				} else {
					if (last_net_type == Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI) {
						showProgressWiFiList();
						radio_two.setChecked(true);
					} else {
						hideWiFiList();
						radio_one.setChecked(true);
					}
					T.showShort(JshWdjNetSetActivity.this,
							R.string.os_operator_error);
				}
				showNetType();
				setRadioEnable(true);
			} else if (intent.getAction().equals(Constants.P2P.RET_GET_WIFI)) {
				int iCurrentId = intent.getIntExtra("iCurrentId", 0);
				int iCount = intent.getIntExtra("iCount", 0);
				int[] iType = intent.getIntArrayExtra("iType");
				int[] iStrength = intent.getIntArrayExtra("iStrength");
				String[] names = intent.getStringArrayExtra("names");
				mAdapter.updateData(iCurrentId, iCount, iType, iStrength, names);
				showWiFiList();
				list.setSelection(iCurrentId);
			} else if (intent.getAction().equals(Constants.P2P.RET_SET_WIFI)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.WIFI_SET.SETTING_SUCCESS) {

				} else if (result == 20) {
					T.showShort(JshWdjNetSetActivity.this,
							R.string.os_wifi_pwd_format_error);
				} else {
					T.showShort(JshWdjNetSetActivity.this,
							R.string.os_operator_error);
				}

			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjNetSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get npc settings");
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_NET_TYPE)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjNetSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set npc settings net type");
					
					if (null != loadNetTypeProgressDialog && loadNetTypeProgressDialog.isShowing()) {
						P2PHandler.getInstance().setNetType(
								device.getDeviceId(),
								device.getDevicePassword(),
								last_modify_net_type);
					}
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
					DismissLoadingProgressDialog();
					hideWiFiList();
					showProgress_net_type();
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
					setRadioEnable(true);
				}
			} else if (intent.getAction()
					.equals(Constants.P2P.ACK_RET_GET_WIFI)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjNetSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get wifi list");
					P2PHandler.getInstance().getWifiList(device.getDeviceId(),
							device.getDevicePassword());
				}
			} else if (intent.getAction()
					.equals(Constants.P2P.ACK_RET_SET_WIFI)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjNetSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set wifi");
					if (null != loadWifiProgressDialog && loadWifiProgressDialog.isShowing()) {
						P2PHandler.getInstance().setWifi(device.getDeviceId(),
								device.getDevicePassword(),
								last_modify_wifi_type, last_modify_wifi_name,
								last_modify_wifi_password);
					}
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
					DismissLoadingProgressDialog();
					hideWiFiList();
					showProgress_net_type();
					P2PHandler.getInstance().getNpcSettings(
							device.getDeviceId(), device.getDevicePassword());
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radio_one:
			changeNetType(Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIRED);
			break;
		case R.id.radio_two:
			changeNetType(Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI);
			break;
		}
	}

	public void changeNetType(final int type) {
		ShowAlertDialog(
				JshWdjNetSetActivity.this.getResources().getString(
						R.string.os_warning),
				JshWdjNetSetActivity.this.getResources().getString(
						R.string.os_modify_net_warning),
				new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						loadNetTypeProgressDialog =  ShowLoadingProgressDialog(R.string.os_verification);
						new DelayThread(
								Constants.SettingConfig.SETTING_CLICK_TIME_DELAY,
								new DelayThread.OnRunListener() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										last_modify_net_type = type;
										P2PHandler.getInstance().setNetType(
												device.getDeviceId(),
												device.getDevicePassword(),
												type);
									}
								}).start();
						setRadioEnable(false);
					}
				}, new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						switch (last_net_type) {
						case Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIFI:
							radio_two.setChecked(true);
							dialog.dismiss();
							break;
						case Constants.P2P_SET.NET_TYPE_SET.NET_TYPE_WIRED:
							radio_one.setChecked(true);
							dialog.dismiss();
							break;
						}
					}
				});
	}

	public void setRadioEnable(boolean bool) {
		if (bool) {
			radio_one.setEnabled(true);
			radio_two.setEnabled(true);
		} else {
			radio_one.setEnabled(false);
			radio_two.setEnabled(false);
		}
	}

	public void showProgress_net_type() {
		progressBar_net_type.setVisibility(RelativeLayout.VISIBLE);
		net_type_radio.setVisibility(RelativeLayout.GONE);
	}

	public void showNetType() {
		progressBar_net_type.setVisibility(RelativeLayout.GONE);
		net_type_radio.setVisibility(RelativeLayout.VISIBLE);
	}

	public void hideWiFiList() {
		list_wifi_bar.setVisibility(RelativeLayout.GONE);
		list_wifi_content.setVisibility(RelativeLayout.GONE);
	}

	public void showProgressWiFiList() {
		list_wifi_bar.setVisibility(RelativeLayout.VISIBLE);
		progressBar_list_wifi.setVisibility(RelativeLayout.VISIBLE);
		list_wifi_content.setVisibility(RelativeLayout.GONE);
	}

	public void showWiFiList() {
		list_wifi_bar.setVisibility(RelativeLayout.VISIBLE);
		progressBar_list_wifi.setVisibility(RelativeLayout.GONE);
		list_wifi_content.setVisibility(RelativeLayout.VISIBLE);
	}

	public void showModfyWifi(final int type, final String name) {
		final View view = LayoutInflater.from(JshWdjNetSetActivity.this)
				.inflate(R.layout.dialog_input, null);
		ShowViewAlertDialog(
				JshWdjNetSetActivity.this.getResources().getString(
						R.string.os_change_wifi)
						+ "(" + name + ")", view,
				new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						EditText pwdet = (EditText) view
								.findViewById(R.id.pwdet);
						String password = pwdet.getText().toString();
						if (type != 0) {
							if ("".equals(password.trim())) {
								T.showShort(JshWdjNetSetActivity.this,
										R.string.os_input_wifi_pwd);
								return;
							}
						}
						dialog.dismiss();
						loadWifiProgressDialog =  ShowLoadingProgressDialog(R.string.os_verification);
						last_modify_wifi_type = type;
						last_modify_wifi_name = name;
						last_modify_wifi_password = password;
						if (type == 0) {
							P2PHandler.getInstance()
									.setWifi(device.getDeviceId(),
											device.getDevicePassword(), type,
											name, "0");
						} else {
							P2PHandler.getInstance().setWifi(
									device.getDeviceId(),
									device.getDevicePassword(), type, name,
									password);
						}
					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent it = new Intent();
		it.setAction(Constants.Action.CONTROL_BACK);
		JshWdjNetSetActivity.this.sendBroadcast(it);
		if (isRegFilter) {
			JshWdjNetSetActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	public void onitemClick(final int position, final int[] iType,
			final String[] names) {
		// TODO Auto-generated method stub
		ShowAlertDialog(
				JshWdjNetSetActivity.this.getResources().getString(
						R.string.os_warning),
				JshWdjNetSetActivity.this.getResources().getString(
						R.string.os_modify_net_warning),
				new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						JshWdjNetSetActivity.this.showModfyWifi(
								iType[position], names[position]);
					}
				});
	}
}