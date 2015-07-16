package p2p.cellcom.com.cn.listener;

import java.util.ArrayList;

import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;

import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.bean.Message;
import p2p.cellcom.com.cn.bean.SysMessage;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PInterface.ISetting;

public class SettingListener implements ISetting {
	String TAG = "SDK";

	/* ***************************************************************
	 * 检查密码 开始
	 */
	@Override
	public void ACK_vRetCheckDevicePassword(int msgId, int result) {
		// TODO Auto-generated method stub
		LogMgr.showLog("ACK_vRetCheckDevicePassword:" + result);
		// if(result==Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS){
		// FList.getInstance().setDefenceState(threeNum, state)
		// }
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 检查密码 结束 ****************************************************************
	 */

	/* ***************************************************************
	 * 获取设备各种设置回调 开始
	 */
	@Override
	public void ACK_vRetGetNpcSettings(String deviceId, int msgId, int result) {
		// TODO Auto-generated method stub
		LogMgr.showLog("ACK_vRetGetNpcSettings:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetDefenceStates(String deviceId, int msgId, int result) {
		// TODO Auto-generated method stub
		LogMgr.showLog("ACK_vRetGetDefenceStates:" + result);
		if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_STATE_WARNING_NET);
			Intent i = new Intent();
			i.putExtra("state",	Constants.DefenceState.DEFENCE_STATE_WARNING_NET);
			i.putExtra("deviceId", deviceId);
			i.setAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
			MyApp.app.sendBroadcast(i);
		} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_STATE_WARNING_PWD);
			Intent i = new Intent();
			i.putExtra("state", Constants.DefenceState.DEFENCE_STATE_WARNING_PWD);
			i.putExtra("deviceId", deviceId);
			i.setAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
			MyApp.app.sendBroadcast(i);
		} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_NO_PERMISSION);
		}
	}

	/*
	 * 获取设备各种设置回调 结束
	 * ****************************************************************
	 */

	/* ***************************************************************
	 * 时间设置相关回调 开始
	 */

	@Override
	public void vRetGetDeviceTimeResult(String time) {
		// 获取设备时间回调
		Log.e(TAG, "vRetGetDeviceTimeResult:" + time);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_TIME);
		i.putExtra("time", time);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetDeviceTimeResult(int result) {
		// 设置设备时间回调
		Log.e(TAG, "vRetSetDeviceTimeResult:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.RET_SET_TIME);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetDeviceTime(int msgId, int result) {
		// 设置设备时间ACK回调
		Log.e(TAG, "ACK_vRetSetDeviceTime:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_TIME);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetDeviceTime(int msgId, int result) {
		// 获取设备时间ACK回调
		Log.e(TAG, "ACK_vRetGetDeviceTime:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_TIME);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 时间设置相关回调 结束
	 * ****************************************************************
	 */

	/* ***************************************************************
	 * 设置视频格式相关回调 开始
	 */
	@Override
	public void ACK_vRetSetNpcSettingsVideoFormat(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsVideoFormat:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_VIDEO_FORMAT);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetVideoFormatResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetVideoFormatResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_VIDEO_FORMAT);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetVideoFormatResult(int type) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetVideoFormatResult:" + type);
		Intent format_type = new Intent();
		format_type.setAction(Constants.P2P.RET_GET_VIDEO_FORMAT);
		format_type.putExtra("type", type);
		MyApp.app.sendBroadcast(format_type);
	}

	/*
	 * 设置视频格式相关回调 结束
	 * ****************************************************************
	 */

	/* ***************************************************************
	 * 设置设备音量大小相关回调 开始
	 */
	@Override
	public void ACK_vRetSetNpcSettingsVideoVolume(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsVideoVolume:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_VIDEO_VOLUME);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetVolumeResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetVolumeResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_VIDEO_VOLUME);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetVideoVolumeResult(int value) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetVideoVolumeResult:" + value);
		Intent volume = new Intent();
		volume.setAction(Constants.P2P.RET_GET_VIDEO_VOLUME);
		volume.putExtra("value", value);
		MyApp.app.sendBroadcast(volume);
	}

	/*
	 * 设置设备音量大小相关回调 结束
	 * ****************************************************************
	 */

	/* ***************************************************************
	 * 修改设备密码相关回调 开始
	 */
	@Override
	public void ACK_vRetSetDevicePassword(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetDevicePassword:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetDevicePasswordResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetDevicePasswordResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_DEVICE_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 修改设备密码相关回调 结束
	 * ****************************************************************
	 */

	/*
	 * 设置网络类型 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetNpcSettingsNetType(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsNetType:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_NET_TYPE);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetNetTypeResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetNetTypeResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_NET_TYPE);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetNetTypeResult(int type) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetNetTypeResult:" + type);
		Intent net_type = new Intent();
		net_type.setAction(Constants.P2P.RET_GET_NET_TYPE);
		net_type.putExtra("type", type);
		MyApp.app.sendBroadcast(net_type);
	}

	/*
	 * 设置网络类型 结束
	 * ****************************************************************
	 */

	/*
	 * 设置WIFI 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetWifi(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetWifi:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_WIFI);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetWifiList(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetWifiList:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_WIFI);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetWifiResult(int result, int currentId, int count,
			int[] types, int[] strengths, String[] names) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetWifiResult:" + result + ":" + currentId);
		if (result == 1) {
			Intent i = new Intent();
			i.setAction(Constants.P2P.RET_GET_WIFI);
			i.putExtra("iCurrentId", currentId);
			i.putExtra("iCount", count);
			i.putExtra("iType", types);
			i.putExtra("iStrength", strengths);
			i.putExtra("names", names);
			MyApp.app.sendBroadcast(i);
		} else {
			Intent i = new Intent();
			i.putExtra("result", result);
			i.setAction(Constants.P2P.RET_SET_WIFI);
			MyApp.app.sendBroadcast(i);
		}
	}

	/*
	 * 设置WIFI 结束
	 * ****************************************************************
	 */

	/*
	 * 设置绑定报警ID 开始
	 * ****************************************************************
	 */

	@Override
	public void ACK_vRetSetAlarmBindId(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetAlarmBindId:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetAlarmBindId(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetAlarmBindId:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetBindAlarmIdResult(int result, int maxCount, String[] data) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetBindAlarmIdResult:" + result);
		if (result == 1) {
			Intent alarmId = new Intent();
			alarmId.setAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
			alarmId.putExtra("data", data);
			alarmId.putExtra("max_count", maxCount);
			MyApp.app.sendBroadcast(alarmId);
		} else {
			Intent i = new Intent();
			i.putExtra("result", result);
			i.setAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
			MyApp.app.sendBroadcast(i);
		}
	}

	/*
	 * 设置绑定报警ID 结束
	 * ****************************************************************
	 */

	/*
	 * 设置报警邮箱 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetAlarmEmail(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetAlarmEmail:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_ALARM_EMAIL);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetAlarmEmail(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetAlarmEmail:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_ALARM_EMAIL);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetAlarmEmailResult(int result, String email) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetAlarmEmailResult:" + result + ":" + email);
		if (result == 1) {
			Intent i = new Intent();
			i.setAction(Constants.P2P.RET_GET_ALARM_EMAIL);
			i.putExtra("email", email);
			MyApp.app.sendBroadcast(i);
		} else {
			Intent i = new Intent();
			i.putExtra("result", result);
			i.setAction(Constants.P2P.RET_SET_ALARM_EMAIL);
			MyApp.app.sendBroadcast(i);
		}
	}

	/*
	 * 设置报警邮箱 结束
	 * ****************************************************************
	 */

	/*
	 * 设置移动侦测相关 开始
	 * ****************************************************************
	 */

	@Override
	public void ACK_vRetSetNpcSettingsMotion(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsMotion:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_MOTION);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetMotionResult(int state) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetMotionResult:" + state);
		Intent motion = new Intent();
		motion.setAction(Constants.P2P.RET_GET_MOTION);
		motion.putExtra("motionState", state);
		MyApp.app.sendBroadcast(motion);
	}

	@Override
	public void vRetSetMotionResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetMotionResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_MOTION);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置移动侦测相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置蜂鸣器相关 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetNpcSettingsBuzzer(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsBuzzer:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_BUZZER);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetBuzzerResult(int state) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetBuzzerResult:" + state);
		Intent buzzer = new Intent();
		buzzer.setAction(Constants.P2P.RET_GET_BUZZER);
		buzzer.putExtra("buzzerState", state);
		MyApp.app.sendBroadcast(buzzer);
	}

	@Override
	public void vRetSetBuzzerResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetBuzzerResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_BUZZER);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置蜂鸣器相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置录像模式相关 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetNpcSettingsRecordType(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsRecordType:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_RECORD_TYPE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetRecordTypeResult(int type) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetRecordTypeResult:" + type);
		Intent record_type = new Intent();
		record_type.setAction(Constants.P2P.RET_GET_RECORD_TYPE);
		record_type.putExtra("type", type);
		MyApp.app.sendBroadcast(record_type);
	}

	@Override
	public void vRetSetRecordTypeResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetRecordTypeResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_RECORD_TYPE);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置录像模式相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置录像时长相关 开始
	 * ****************************************************************
	 */

	@Override
	public void ACK_vRetSetNpcSettingsRecordTime(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsRecordTime:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_RECORD_TIME);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetRecordTimeResult(int time) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetRecordTimeResult:" + time);
		Intent record_time = new Intent();
		record_time.setAction(Constants.P2P.RET_GET_RECORD_TIME);
		record_time.putExtra("time", time);
		MyApp.app.sendBroadcast(record_time);
	}

	@Override
	public void vRetSetRecordTimeResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetRecordTimeResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_RECORD_TIME);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置录像时长相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置录像计划时间 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetNpcSettingsRecordPlanTime(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetNpcSettingsRecordPlanTime:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_RECORD_PLAN_TIME);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetRecordPlanTimeResult(String time) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetRecordPlanTimeResult:" + time);
		Intent plan_time = new Intent();
		plan_time.setAction(Constants.P2P.RET_GET_RECORD_PLAN_TIME);
		plan_time.putExtra("time", time);
		MyApp.app.sendBroadcast(plan_time);
	}

	@Override
	public void vRetSetRecordPlanTimeResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetRecordPlanTimeResult:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_RECORD_PLAN_TIME);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置录像计划时间 结束
	 * ****************************************************************
	 */

	/*
	 * 防区设置相关 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetDefenceArea(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetDefenceArea:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_SET_DEFENCE_AREA);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetClearDefenceAreaState(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetClearDefenceAreaState:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_CLEAR_DEFENCE_AREA);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetClearDefenceAreaState(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetClearDefenceAreaState:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.RET_CLEAR_DEFENCE_AREA);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetDefenceArea(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetDefenceArea:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_DEFENCE_AREA);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetDefenceAreaResult(int result, ArrayList<int[]> data,
			int group, int item) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetDefenceAreaResult:" + result);
		if (result == 1) {
			Intent i = new Intent();
			i.setAction(Constants.P2P.RET_GET_DEFENCE_AREA);
			i.putExtra("data", data);
			MyApp.app.sendBroadcast(i);
		} else {
			Intent i = new Intent();
			i.putExtra("result", result);
			i.setAction(Constants.P2P.RET_SET_DEFENCE_AREA);
			i.putExtra("group", group);
			i.putExtra("item", item);
			MyApp.app.sendBroadcast(i);
		}
	}

	/*
	 * 防区设置相关 结束
	 * ****************************************************************
	 */

	/*
	 * 远程设置相关 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetRemoteDefence(String deviceId, int msgId, int result) {
		// TODO Auto-generated method stub
		LogMgr.showLog("ACK_vRetSetRemoteDefence:" + result);
		if(TextUtils.isEmpty(deviceId)){
			LogMgr.showLog("ACK_vRetSetRemoteDefence, deviceId--------------->null");
		}else{
			LogMgr.showLog("ACK_vRetSetRemoteDefence, deviceId--------------->"  + deviceId);
		}
		if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
			Device device = FList.getInstance().isDevice(deviceId);
			if (null != device) {
				P2PHandler.getInstance().getNpcSettings(device.getDeviceId(), 	device.getDevicePassword());
			}

		} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_STATE_WARNING_NET);
			Intent i = new Intent();
			i.putExtra("state", Constants.DefenceState.DEFENCE_STATE_WARNING_NET);
			i.putExtra("deviceId", deviceId);
			i.setAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
			MyApp.app.sendBroadcast(i);

		} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_STATE_WARNING_PWD);
			Intent i = new Intent();
			i.putExtra("state", Constants.DefenceState.DEFENCE_STATE_WARNING_PWD);
			i.putExtra("deviceId", deviceId);
			i.setAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
			MyApp.app.sendBroadcast(i);
		} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_NO_PERMISSION);
		}
	}

	@Override
	public void ACK_vRetSetRemoteRecord(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetRemoteRecord:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.RET_SET_REMOTE_RECORD);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetRemoteDefenceResult(String deviceId, int state) {
		// TODO Auto-generated method stub
		LogMgr.showLog("vRetGetRemoteDefenceResult:" + state);
		if (state == Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON) {
			FList.getInstance().setDefenceState(deviceId, Constants.DefenceState.DEFENCE_STATE_ON);
		} else {
			FList.getInstance().setDefenceState(deviceId,	Constants.DefenceState.DEFENCE_STATE_OFF);
		}

		Intent defence = new Intent();
		defence.setAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
		defence.putExtra("state", state);
		defence.putExtra("deviceId", deviceId);
		MyApp.app.sendBroadcast(defence);
	}

	@Override
	public void vRetGetRemoteRecordResult(int state) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetRemoteRecordResult:" + state);
		Intent record = new Intent();
		record.setAction(Constants.P2P.RET_GET_REMOTE_RECORD);
		record.putExtra("state", state);
		MyApp.app.sendBroadcast(record);
	}

	@Override
	public void vRetSetRemoteDefenceResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetRemoteDefenceResult:" + result);
		Intent defence = new Intent();
		defence.setAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
		defence.putExtra("state", result);
		MyApp.app.sendBroadcast(defence);
	}

	@Override
	public void vRetSetRemoteRecordResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetRemoteRecordResult:" + result);
		Intent record = new Intent();
		record.setAction(Constants.P2P.RET_SET_REMOTE_RECORD);
		record.putExtra("state", result);
		MyApp.app.sendBroadcast(record);
	}

	/*
	 * 远程设置相关 结束
	 * ****************************************************************
	 */

	/*
	 * 设置设备初始密码（仅当设备出厂化未设置密码时可用） 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetSetInitPassword(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetSetInitPassword:" + result);
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_INIT_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetInitPasswordResult(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetSetInitPasswordResult******:" + result);

		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_INIT_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设置设备初始密码（仅当设备出厂化未设置密码时可用） 结束
	 * ****************************************************************
	 */

	/*
	 * 设备检查更新 开始
	 * ****************************************************************
	 */
	@Override
	public void ACK_vRetGetDeviceVersion(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetDeviceVersion:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_DEVICE_INFO);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetDeviceVersion(int result, String cur_version,
			int iUbootVersion, int iKernelVersion, int iRootfsVersion) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetDeviceVersion:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.putExtra("cur_version", cur_version);
		i.putExtra("iUbootVersion", iUbootVersion);
		i.putExtra("iKernelVersion", iKernelVersion);
		i.putExtra("iRootfsVersion", iRootfsVersion);
		i.setAction(Constants.P2P.RET_GET_DEVICE_INFO);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetCheckDeviceUpdate(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetCheckDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_CHECK_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetCheckDeviceUpdate(int result, String cur_version,
			String upg_version) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetCheckDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.putExtra("cur_version", cur_version);
		i.putExtra("upg_version", upg_version);
		i.setAction(Constants.P2P.RET_CHECK_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetDoDeviceUpdate(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetDoDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_DO_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetDoDeviceUpdate(int result, int value) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetDoDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.putExtra("value", value);
		i.setAction(Constants.P2P.RET_DO_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetCancelDeviceUpdate(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetCancelDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_CANCEL_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetCancelDeviceUpdate(int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetCancelDeviceUpdate:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.RET_CHECK_DEVICE_UPDATE);
		MyApp.app.sendBroadcast(i);
	}

	/*
	 * 设备检查更新 结束
	 * ****************************************************************
	 */

	@Override
	public void ACK_vRetGetRecordFileList(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e(TAG, "ACK_vRetGetRecordFileList:" + result);
		Intent i = new Intent();
		i.putExtra("result", result);
		i.setAction(Constants.P2P.ACK_RET_GET_PLAYBACK_FILES);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetRecordFiles(String[] names) {
		// TODO Auto-generated method stub
		Log.e(TAG, "vRetGetRecordFiles:");
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_PLAYBACK_FILES);
		i.putExtra("recordList", names);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetFriendStatus(int count, String[] deviceIDs,	int[] status, int[] types) {
		LogMgr.showLog("vRetGetFriendStatus:" + count);
		for (int i = 0; i < count; i++) {
			LogMgr.showLog("deviceID--------->" + deviceIDs[i] + "，status-------->" + status[i] + "，type-------->" + types[i]);
			FList.getInstance().setState(deviceIDs[i], status[i]);
			if (deviceIDs[i].charAt(0) == '0') {
				FList.getInstance().setType(deviceIDs[i], P2PValue.DeviceType.PHONE);
			} else {
				if (status[i] == Constants.DeviceState.ONLINE) {
					FList.getInstance().setType(deviceIDs[i], types[i]);
				}
			}
		}
		// TODO Auto-generated method stub
		FList.getInstance().sort();
		FList.getInstance().getDefenceState();
		Intent friends = new Intent();
		friends.setAction(Constants.Action.GET_FRIENDS_STATE);
		MyApp.app.sendBroadcast(friends);
	}

	@Override
	public void ACK_vRetMessage(int msgId, int result) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.Action.RECEIVE_MSG);
		i.putExtra("msgFlag", msgId + "");
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetMessage(String deviceId, String msgStr) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(deviceId)){
			LogMgr.showLog("vRetMessage, deviceId--------------->null");
		}else{
			LogMgr.showLog("vRetMessage, deviceId--------------->"  + deviceId);
		}
		Device device = FList.getInstance().isDevice(deviceId);
		if (device == null) {
			// 没加好友返回
			return;
		}
		Message msg = new Message();
		msg.activeUser = NpcCommon.mThreeNum;
		msg.fromId = deviceId;
		msg.toId = NpcCommon.mThreeNum;
		msg.msg = msgStr;
		msg.msgTime = String.valueOf(System.currentTimeMillis());
		msg.msgFlag = String.valueOf(-1);
		msg.msgState = String.valueOf(Constants.MessageType.READED);
		device.setMessageCount(device.getMessageCount() + 1);

//		FList.getInstance().update(device);

		// DataManager.insertMessage(MyApp.app, msg);
		// Intent i = new Intent();
		// i.setAction(MessageActivity.RECEIVER_MSG);
		Intent k = new Intent();
		k.setAction(Constants.Action.REFRESH_CONTANTS);
		// MyApp.app.sendBroadcast(i);
		// MyApp.app.sendBroadcast(k);
		// MusicManger.getInstance().playMsgMusic();
	}

	@Override
	public void vRetSysMessage(String msg) {
		// TODO Auto-generated method stub
		SysMessage sysMessage = new SysMessage();
		sysMessage.activeUser = NpcCommon.mThreeNum;
		sysMessage.msg = msg;
		sysMessage.msg_time = String.valueOf(System.currentTimeMillis());
		sysMessage.msgState = SysMessage.MESSAGE_STATE_NO_READ;
		sysMessage.msgType = SysMessage.MESSAGE_TYPE_ADMIN;
		// DataManager.insertSysMessage(MyApp.app, sysMessage);
		// Intent i = new Intent();
		// i.setAction(SysMsgActivity.REFRESH);
		// MyApp.app.sendBroadcast(i);
		// Intent k = new Intent();
		// k.setAction(Constants.Action.RECEIVE_SYS_MSG);
		// MyApp.app.sendBroadcast(k);
	}

	@Override
	public void vRetCustomCmd(String deviceId, String cmd) {
		// TODO Auto-generated method stub
		Log.e("my", cmd);
		Log.e("cus_cmd", cmd);
	}

	@Override
	public void ACK_vRetCustomCmd(int msgId, int result) {
		// TODO Auto-generated method stub
		Log.e("my", result + "");
	}

	@Override
	public void vRetDeviceNotSupport() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetImageReverse(int msgId, int result) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_VRET_SET_IMAGEREVERSE);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetSetImageReverse(int result) {
	}

	@Override
	public void vRetGetImageReverseResult(int type) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_IMAGE_REVERSE);
		i.putExtra("type", type);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetInfraredSwitch(int msgId, int result) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_INFRARED_SWITCH);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetGetInfraredSwitch(int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_INFRARED_SWITCH);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetInfraredSwitch(int result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK_vRetSetWiredAlarmInput(int msgId, int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void ACK_vRetSetWiredAlarmOut(int msgId, int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetAutomaticUpgrade(int msgId, int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_AUTOMATIC_UPGRADE);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetGetWiredAlarmInput(int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_WIRED_ALARM_INPUT);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetGetWiredAlarmOut(int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_WIRED_ALARM_OUT);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetGetAutomaticUpgrade(int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_AUTOMATIC_UPGRAD);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetSetWiredAlarmInput(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetSetWiredAlarmOut(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetSetAutomaticUpgrade(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK_VRetSetVisitorDevicePassword(int msgId, int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_VISITOR_DEVICE_PASSWORD);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetSetVisitorDevicePassword(int result) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_VISITOR_DEVICE_PASSWORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void ACK_vRetSetTimeZone(int msgId, int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_TIME_ZONE);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetGetTimeZone(int state) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_TIME_ZONE);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);

	}

	@Override
	public void vRetSetTimeZone(int result) {

	}

	@Override
	public void vRetGetSdCard(int result1, int result2, int SDcardID, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_SD_CARD_CAPACITY);
		i.putExtra("total_capacity", result1);
		i.putExtra("remain_capacity", result2);
		i.putExtra("SDcardID", SDcardID);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
		Log.e("sdid", SDcardID + "");
	}

	@Override
	public void ACK_vRetGetSDCard(int msgId, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_GET_SD_CARD_CAPACITY);
		i.putExtra("result", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSdFormat(int msgId, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_GET_SD_CARD_FORMAT);
		i.putExtra("result", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSdFormat(int result) {
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_SD_CARD_FORMAT);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void VRetGetUsb(int result1, int result2, int SDcardID, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_USB_CAPACITY);
		i.putExtra("total_capacity", result1);
		i.putExtra("remain_capacity", result2);
		i.putExtra("SDcardID", SDcardID);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetGPIO(int msgId, int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK_vRetSetGPIO1_0(int msgId, int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetGetAudioDeviceType(int type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetSetGPIO(int result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ACK_vRetSetPreRecord(int msgId, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_PRE_RECORD);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetPreRecord(int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_PRE_RECORD);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetPreRecord(int result) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_PRE_RECORD);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetGetSensorSwitchs(int msgId, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_GET_SENSOR_SWITCH);
		i.putExtra("result", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void ACK_vRetSetSensorSwitchs(int msgId, int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.ACK_RET_SET_SENSOR_SWITCH);
		i.putExtra("result", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetGetSensorSwitchs(int result, ArrayList<int[]> data) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_GET_SENSOR_SWITCH);
		i.putExtra("result", result);
		i.putExtra("data", data);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetSetSensorSwitchs(int result) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.RET_SET_SENSOR_SWITCH);
		i.putExtra("result", result);
		MyApp.app.sendBroadcast(i);
	}

}
