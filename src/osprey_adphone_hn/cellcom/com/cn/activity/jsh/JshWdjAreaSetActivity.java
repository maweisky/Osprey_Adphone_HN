package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.BaseExpandableListAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.AreaChild;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

/**
 * 录像防区设置
 * 
 * @author wma
 * 
 */
public class JshWdjAreaSetActivity extends ActivityFrame {
	private ImageView video_img;
	private FinalBitmap finalBitmap;
	private TextView videotv;
	private Device device;
	private ExpandableListView arealv;
	private boolean isRegFilter = false;
	private int current_type;
	private int current_group;
	private int current_item;
	private boolean isSurpport = true;
	private BaseExpandableListAdapter baseExpandableListAdapter;

	private int currentSwitch;
	//进入此界面且无任何防区 消失加载框
	private boolean isFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_areaset);
		AppendTitleBody1();
		HideSet();
		initView();
		initData();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		video_img = (ImageView) findViewById(R.id.video_img);
		videotv = (TextView) findViewById(R.id.videotv);
		arealv = (ExpandableListView) findViewById(R.id.arealv);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_CLEAR_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_SET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_GET_DEFENCE_AREA);
		filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		filter.addAction(Constants.P2P.ACK_RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.ACK_RET_SET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_GET_SENSOR_SWITCH);
		filter.addAction(Constants.P2P.RET_SET_SENSOR_SWITCH);

		JshWdjAreaSetActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.RET_GET_DEFENCE_AREA)) {
				ArrayList<int[]> data = (ArrayList<int[]>) intent
						.getSerializableExtra("data");
				initData(data);
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.DEFENCE_AREA_SET.SETTING_SUCCESS) {
					if (current_type == Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR) {
						grayButton(current_group, current_item);
						DismissProgressDialog();
						ShowMsg(R.string.os_clear_success);
					} else {
						lightButton(current_group, current_item);
						ShowMsg(R.string.os_learning_success);
					}
				} else if (result == 30) {
					DismissProgressDialog();
					grayButton(current_group, current_item);
					ShowMsg(R.string.os_clear_success);
				} else if (result == 32) {
					DismissProgressDialog();
					int group = intent.getIntExtra("group", -1);
					int item = intent.getIntExtra("item", -1);
					Log.e("my", "group:" + group + " item:" + item);
					ShowMsg(Utils.getDefenceAreaByGroup(
							JshWdjAreaSetActivity.this, group)
							+ ":"
							+ (item + 1)
							+ " "
							+ JshWdjAreaSetActivity.this.getResources()
									.getString(R.string.os_channel)
							+ " "
							+ JshWdjAreaSetActivity.this.getResources()
									.getString(R.string.os_has_been_learning));
				} else if (result == 41) {
					DismissProgressDialog();
					JshWdjAreaSetActivity.this.finish();
					ShowMsg(R.string.os_device_unsupport_defence_area);
				} else {
					DismissProgressDialog();
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_CLEAR_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					grayButton(current_group, 0);
					grayButton(current_group, 1);
					grayButton(current_group, 2);
					grayButton(current_group, 3);
					grayButton(current_group, 4);
					grayButton(current_group, 5);
					grayButton(current_group, 6);
					grayButton(current_group, 7);
					ShowMsg(R.string.os_clear_success);
				} else {
					ShowMsg(R.string.os_operator_error);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
				isSurpport = false;
				// T.showShort(mContext, R.string.not_surpport_sensor);
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjAreaSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:get defence area");
					P2PHandler.getInstance().getDefenceArea(
							device.getDeviceId(), device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjAreaSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set defence area");
					P2PHandler.getInstance().setDefenceAreaState(
							device.getDeviceId(), device.getDevicePassword(),
							current_group, current_item, current_type);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjAreaSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:clear defence area");
					P2PHandler.getInstance().clearDefenceAreaState(
							device.getDeviceId(), device.getDevicePassword(),
							current_group);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjAreaSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					Log.e("my", "net error resend:set defence area");
					P2PHandler.getInstance().getDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword());
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_SET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjAreaSetActivity.this.sendBroadcast(i);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							currentSwitch, current_group, current_item);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_GET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				ArrayList<int[]> sensors = (ArrayList<int[]>) intent
						.getSerializableExtra("data");
				if (result == 1) {
					initSensorSwitch(sensors);
				} else if (result == 41) {
					// T.showShort(mContext,
					// R.string.device_unsupport_defence_area);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.RET_SET_SENSOR_SWITCH)) {
				int result = intent.getIntExtra("result", -1);
				if (result == 0) {
					graySwitch(current_group, current_item, currentSwitch);
				} else if (result == 41) {
				} else {
				}
			}
		}
	};

	public void initSensorSwitch(ArrayList<int[]> sensors) {
		for (int i = 0; i < sensors.size(); i++) {
			int[] sensor = sensors.get(i);
			for (int j = 0; j < sensor.length; j++) {
				graySwitch(i, j, sensor[7 - j]);
			}
		}
	}

	public void graySwitch(final int i, final int j, final int isOpen) {
		if (isOpen == 1) {
			// 已开启
			if (i > 4) {
				return;
			}
			AreaChild areaChild = (AreaChild) baseExpandableListAdapter.getChild(i,
					0);
			if (j == 0) {
				//是否设置该区域
				if(areaChild.getChildItem1().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem1().setIsopen(true);
			}else if(j==1){
				//是否设置该区域
				if(areaChild.getChildItem2().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem2().setIsopen(true);
			}else if(j==2){
				//是否设置该区域
				if(areaChild.getChildItem3().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem3().setIsopen(true);
			}else if(j==3){
				//是否设置该区域
				if(areaChild.getChildItem4().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem4().setIsopen(true);
			}else if(j==4){
				//是否设置该区域
				if(areaChild.getChildItem5().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem5().setIsopen(true);
			}else if(j==5){
				//是否设置该区域
				if(areaChild.getChildItem6().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem6().setIsopen(true);
			}else if(j==6){
				//是否设置该区域
				if(areaChild.getChildItem7().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem7().setIsopen(true);
			}else if(j==7){
				//是否设置该区域
				if(areaChild.getChildItem8().isIsselect()){
					DismissProgressDialog();
				}
				areaChild.getChildItem8().setIsopen(true);
			}
		} else {
			// 已关闭
			currentSwitch = 1;
			current_group = i;
			current_item = j;
			P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
					device.getDeviceId(), device.getDevicePassword(),
					currentSwitch, current_group, current_item);
		}
	}

	public void study(final int group, final int item) {
		ShowAlertDialog(
				JshWdjAreaSetActivity.this.getResources().getString(
						R.string.os_learing_code),
				JshWdjAreaSetActivity.this.getResources().getString(
						R.string.os_learing_code_prompt),
				new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN;
						current_group = group;
						current_item = item;
						CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
						customProgressDialog.setCanceledOnTouchOutside(false);
						customProgressDialog.setOnKeyListener(new OnKeyListener() {
							
							@Override
							public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
									JshWdjAreaSetActivity.this.finish();
								}
								return false;
							}
						});
						P2PHandler
								.getInstance()
								.setDefenceAreaState(
										device.getDeviceId(),
										device.getDevicePassword(),
										group,
										item,
										Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN);
					}
				});
	}

	public void grayButton(final int i, final int j) {
		if (i > 4) {
			return;
		}
		AreaChild areaChild = (AreaChild) baseExpandableListAdapter.getChild(i,
				0);
		if (j == 0) {
			areaChild.getChildItem1().setIsselect(false);
		} else if (j == 1) {
			areaChild.getChildItem2().setIsselect(false);
		} else if (j == 2) {
			areaChild.getChildItem3().setIsselect(false);
		} else if (j == 3) {
			areaChild.getChildItem4().setIsselect(false);
		} else if (j == 4) {
			areaChild.getChildItem5().setIsselect(false);
		} else if (j == 5) {
			areaChild.getChildItem6().setIsselect(false);
		} else if (j == 6) {
			areaChild.getChildItem7().setIsselect(false);
		} else if (j == 7) {
			areaChild.getChildItem8().setIsselect(false);
		} else if (j == 8) {
			areaChild.getChildItem9().setIsselect(false);
		}
		baseExpandableListAdapter.notifyDataSetChanged();
	}

	public void lightButton(final int i, final int j) {
		if (i > 4) {
			return;
		}
		AreaChild areaChild = (AreaChild) baseExpandableListAdapter.getChild(i,
				0);
		if (j == 0) {
			if(i!=0&&isSurpport==true){
				//判断传感器开关是否开启
				if(areaChild.getChildItem1().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				//遥控及不支持的设备无需判断传感器开关是否开启
				DismissProgressDialog();
			}
			areaChild.getChildItem1().setIsselect(true);
		} else if (j == 1) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem2().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem2().setIsselect(true);
		} else if (j == 2) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem3().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem3().setIsselect(true);
		} else if (j == 3) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem4().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem4().setIsselect(true);
		} else if (j == 4) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem5().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem5().setIsselect(true);
		} else if (j == 5) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem6().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem6().setIsselect(true);
		} else if (j == 6) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem7().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem7().setIsselect(true);
		} else if (j == 7) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem8().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem8().setIsselect(true);
		} else if (j == 8) {
			if(i!=0&&isSurpport==true){
				if(areaChild.getChildItem9().isIsopen()){
					DismissProgressDialog();
				}else{
					P2PHandler.getInstance().setDefenceAreaAlarmSwitch(
							device.getDeviceId(), device.getDevicePassword(),
							1, i, j);
				}
			}else{
				DismissProgressDialog();
			}
			areaChild.getChildItem9().setIsselect(true);
		}
		baseExpandableListAdapter.notifyDataSetChanged();
	}

	public void clear(final int group, final int item) {
		ShowAlertDialog(
				JshWdjAreaSetActivity.this.getResources().getString(
						R.string.os_clear_code), JshWdjAreaSetActivity.this
						.getResources()
						.getString(R.string.os_clear_code_prompt),
				new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR;
						current_group = group;
						current_item = item;
						CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
						customProgressDialog.setCanceledOnTouchOutside(false);
						customProgressDialog.setOnKeyListener(new OnKeyListener() {
							
							@Override
							public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
									JshWdjAreaSetActivity.this.finish();
								}
								return false;
							}
						});
						P2PHandler
								.getInstance()
								.setDefenceAreaState(
										device.getDeviceId(),
										device.getDevicePassword(),
										group,
										item,
										Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR);
					}
				});
	}

	public void initData(ArrayList<int[]> data) {
		boolean isAllGray=true;
		for (int i = 0; i < data.size(); i++) {
			int[] status = data.get(i);
			for (int j = 0; j < status.length; j++) {

				if (status[j] == 1) {
					grayButton(i, j);
				} else {
					isAllGray=false;
					lightButton(i, j);
				}
			}
		}
		if(isFirst&isAllGray){
			isFirst=false;
			DismissProgressDialog();
		}
	}

	private void initListener() {
		// TODO Auto-generated method stub
		arealv.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < arealv.getCount(); i++) {
					if (groupPosition != i) {
						arealv.collapseGroup(i);
					}
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");

		finalBitmap = FinalBitmap.create(JshWdjAreaSetActivity.this);
		if (!TextUtils.isEmpty(device.getServerImgUrl())) {
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());

		baseExpandableListAdapter = new BaseExpandableListAdapter(
				JshWdjAreaSetActivity.this);
		arealv.setAdapter(baseExpandableListAdapter);
		regFilter();
		isFirst=true;
		CustomProgressDialog customProgressDialog=ShowProgressDialog(R.string.hsc_progress);
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
					JshWdjAreaSetActivity.this.finish();
				}
				return false;
			}
		});
		P2PHandler.getInstance().getDefenceAreaAlarmSwitch(
				device.getDeviceId(), device.getDevicePassword());
		P2PHandler.getInstance().getDefenceArea(device.getDeviceId(),
				device.getDevicePassword());

		arealv.setGroupIndicator(null);
		arealv.expandGroup(0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isRegFilter) {
			JshWdjAreaSetActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}

	public void childClick(int groupp, int childp) {
		// TODO Auto-generated method stub
		AreaChild areaChild = (AreaChild) baseExpandableListAdapter.getChild(
				groupp, 0);
		if (childp == 0) {
			if (areaChild.getChildItem1().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 1) {
			if (areaChild.getChildItem2().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 2) {
			if (areaChild.getChildItem3().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 3) {
			if (areaChild.getChildItem4().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 4) {
			if (areaChild.getChildItem5().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 5) {
			if (areaChild.getChildItem6().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 6) {
			if (areaChild.getChildItem7().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 7) {
			if (areaChild.getChildItem8().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		} else if (childp == 8) {
			if (areaChild.getChildItem9().isIsselect()) {
				clear(groupp, childp);
			} else {
				study(groupp, childp);
			}
		}
	}
}