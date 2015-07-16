package p2p.cellcom.com.cn.listener;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.db.SharedPreferencesManager;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.P2PConnect;
import p2p.cellcom.com.cn.utils.MusicManger;
import android.content.Intent;
import android.util.Log;

import com.p2p.core.P2PValue;
import com.p2p.core.P2PInterface.IP2P;

public class P2PListener implements IP2P {

	@Override
	public void vCalling(boolean isOutCall, String threeNumber, int type) {
		// TODO Auto-generated method stub
		if (isOutCall) {
			P2PConnect.vCalling(true, type);
		} else {
//			int c_muteState = SharedPreferencesManager.getInstance()	.getCMuteState(MyApp.app);
//			 if(c_muteState==1){
//				 MusicManger.getInstance().playCommingMusic();
//			 }
//
//			 int c_vibrateState =
//					 SharedPreferencesManager.getInstance().getCVibrateState(MyApp.app);
//			 if(c_vibrateState==1){
//				 MusicManger.getInstance().Vibrate();
//			 }

			P2PConnect.setCurrent_call_id(threeNumber);

			P2PConnect.vCalling(false, type);
		}
	}

	@Override
	public void vReject(int reason_code) {
		// TODO Auto-generated method stub
		String reason = "";
		switch (reason_code) {
		case 0:
			reason = MyApp.app.getResources()
					.getString(R.string.os_pw_incrrect);
			break;
		case 1:
			reason = MyApp.app.getResources().getString(R.string.os_busy);
			break;
		case 2:
			reason = MyApp.app.getResources().getString(R.string.os_none);
			break;
		case 3:
			reason = MyApp.app.getResources()
					.getString(R.string.os_id_disabled);
			break;
		case 4:
			reason = MyApp.app.getResources()
					.getString(R.string.os_id_overdate);
			break;
		case 5:
			reason = MyApp.app.getResources().getString(
					R.string.os_id_inactived);
			break;
		case 6:
			reason = MyApp.app.getResources().getString(R.string.os_offline);
			break;
		case 7:
			reason = MyApp.app.getResources().getString(R.string.os_powerdown);
			break;
		case 8:
			reason = MyApp.app.getResources().getString(R.string.os_nohelper);
			break;
		case 9:
			reason = MyApp.app.getResources().getString(R.string.os_hungup);
			break;
		case 10:
			reason = MyApp.app.getResources().getString(R.string.os_timeout);
			break;
		case 11:
			reason = MyApp.app.getResources().getString(R.string.os_no_body);
			break;
		case 12:
			reason = MyApp.app.getResources().getString(
					R.string.os_internal_error);
			break;
		case 13:
			reason = MyApp.app.getResources().getString(R.string.os_conn_fail);
			break;
		case 14:
			reason = MyApp.app.getResources()
					.getString(R.string.os_not_support);
			break;
		}
		P2PConnect.vReject(reason);
	}

	@Override
	public void vAccept() {
		// TODO Auto-generated method stub
		P2PConnect.vAccept();
	}

	@Override
	public void vConnectReady() {
		// TODO Auto-generated method stub
		P2PConnect.vConnectReady();
	}

	@Override
	public void vAllarming(String srcId, int type,
			boolean isSupportExternAlarm, int iGroup, int iItem) {
		// TODO Auto-generated method stub
		P2PConnect.vAllarming(Integer.parseInt(srcId), type,
				isSupportExternAlarm, iGroup, iItem);
	}

	@Override
	public void vChangeVideoMask(int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Constants.P2P.P2P_CHANGE_IMAGE_TRANSFER);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetPlayBackPos(int length, int currentPos) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.PLAYBACK_CHANGE_SEEK);
		i.putExtra("max", length);
		i.putExtra("current", currentPos);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetPlayBackStatus(int state) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setAction(Constants.P2P.PLAYBACK_CHANGE_STATE);
		i.putExtra("state", state);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vGXNotifyFlag(int flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetPlaySize(int iWidth, int iHeight) {
		// TODO Auto-generated method stub
		Log.e("my", "vRetPlaySize:" + iWidth + "-" + iHeight);

		Intent i = new Intent();
		i.setAction(Constants.P2P.P2P_RESOLUTION_CHANGE);
		if (iWidth == 1280) {
			P2PConnect.setMode(P2PValue.VideoMode.VIDEO_MODE_HD);
			i.putExtra("mode", P2PValue.VideoMode.VIDEO_MODE_HD);
		} else if (iWidth == 640) {
			P2PConnect.setMode(P2PValue.VideoMode.VIDEO_MODE_SD);
			i.putExtra("mode", P2PValue.VideoMode.VIDEO_MODE_SD);
		} else if (iWidth == 320) {
			P2PConnect.setMode(P2PValue.VideoMode.VIDEO_MODE_LD);
			i.putExtra("mode", P2PValue.VideoMode.VIDEO_MODE_LD);
		}
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRetPlayNumber(int iNumber) {
		// TODO Auto-generated method stub
		Log.e("my", "vRetPlayNumber:" + iNumber);
		P2PConnect.setNumber(iNumber);
		Intent i = new Intent();
		i.setAction(Constants.P2P.P2P_MONITOR_NUMBER_CHANGE);
		i.putExtra("number", iNumber);
		MyApp.app.sendBroadcast(i);
	}

	@Override
	public void vRecvAudioVideoData(byte[] AudioBuffer, int AudioLen,
			int AudioFrames, long AudioPTS, byte[] VideoBuffer, int VideoLen,
			long VideoPTS) {
		// TODO Auto-generated method stub
		LogMgr.showLog("vRecvAudioVideoData==>"+VideoBuffer);
	}

}
