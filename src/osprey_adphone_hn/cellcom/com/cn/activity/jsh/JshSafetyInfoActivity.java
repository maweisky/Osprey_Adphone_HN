package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshSafetyInfoAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.SafetyInfoComm;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.customencryption.EncryptGW;

public class JshSafetyInfoActivity extends ActivityFrame{
	private JazzyListView listview;
	private JshSafetyInfoAdapter adapter;
//	List<AlarmRecord> list = new ArrayList<AlarmRecord>();
	List<SafetyInfoComm> safetyInfoList = new ArrayList<SafetyInfoComm>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_safety_info_activity);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		SetTopBarTitle("安全信息");
		initView();
		initListener();
		regFilter();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView(){
		listview = (JazzyListView)findViewById(R.id.listview);
		listview.setTransitionEffect(JazzyHelper.SLIDE_IN);
//		list = DataManager.findAlarmRecordByActiveUser(this,
//				NpcCommon.mThreeNum);
		adapter = new JshSafetyInfoAdapter(this,safetyInfoList);
		listview.setAdapter(adapter);
	}

	/**
	 * 初始监听
	 */
	private void initListener(){
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action.REFRESH_ALARM_RECORD);
		registerReceiver(mReceiver, filter);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction()
					.equals(Constants.Action.REFRESH_ALARM_RECORD)) {
//				adapter.updateData();
//				adapter.notifyDataSetChanged();
				initData();
			}
		}
	};

	private void initData(){
		ShowProgressDialog(R.string.app_loading);
		new Thread(alarminfo).start();
	}

	Runnable alarminfo =  new Runnable() {
		public void run() {
			for(int i = 0;i<FList.getInstance().list().size();i++){
//				if(FList.getInstance().list().get(i).getDeviceId().equals("1176620")){
//				}
				if(FList.getInstance().list().get(i).getDevicePassword()!=null&&
						!FList.getInstance().list().get(i).getDevicePassword().trim().equals("")){
					getSafetyInfo(FList.getInstance().list().get(i).getDeviceId(),
							FList.getInstance().list().get(i).getDevicePassword());
				}else{
					Message message = new Message();
					message.what = 1;
					message.obj = FList.getInstance().list().get(i).getDeviceId();
					handler.sendMessage(message);
				}				
			}
			LogMgr.showLog("thread is over!");
		}
	};

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0:
//				safetyInfoList.clear();
				String[] message = msg.obj.toString().split(";");
				for(int i = 0;i<message.length;i++){
					String[] alarminfo = message[i].split("&");
					SafetyInfoComm safetyInfoComm = new SafetyInfoComm();
					safetyInfoComm.setMessageId(alarminfo[0]);
					safetyInfoComm.setDeviceId(alarminfo[1]);
					safetyInfoComm.setAlarmTime(alarminfo[2]);
					safetyInfoComm.setAlarmpicUrl(alarminfo[3]);
					safetyInfoComm.setAlarmType(alarminfo[4]);
					safetyInfoComm.setFangqu(alarminfo[5]);
					safetyInfoComm.setCannel(alarminfo[6]);
					safetyInfoComm.setServerreceiveTime(alarminfo[7]);
					safetyInfoList.add(safetyInfoComm);
				}
				DismissProgressDialog();
				adapter.notifyDataSetChanged();
				break;
			case 1:
				DismissProgressDialog();
				Toast.makeText(JshSafetyInfoActivity.this, msg.obj.toString()+
						"设备密码为空，请先去设置密码后再来查看该设备报警信息", Toast.LENGTH_SHORT).show();
				break;
			case 23:
				DismissProgressDialog();
				Toast.makeText(JshSafetyInfoActivity.this,
						msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent();
				i.setAction(Constants.Action.SESSION_ID_ERROR);
				MyApp.app.sendBroadcast(i);
				break;
			case 35:
				DismissProgressDialog();
				Toast.makeText(JshSafetyInfoActivity.this,
						msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	/**
	 * 获取报警信息
	 */
	private void getSafetyInfo(final String deviceId,String VKey) {
		if (SharepreferenceUtil.readString(JshSafetyInfoActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Message message = new Message();
			message.what = 0;
			message.obj = "请先登录~";
			handler.sendMessage(message);
			Intent loginintent = new Intent(JshSafetyInfoActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;		
		}
		String encryption=EncryptGW.getInstance().EncryptGW1(VKey);
		String url = "http://cloudlinks.cn/Alarm/AlarmRecordEx.ashx";
		Account account = AccountPersist.getInstance().getActiveAccountInfo(this);
		if(account==null || account.three_number==null){
			String error = deviceId+"暂无法获取安全信息，请稍后再试";
			Message message = new Message();
			message.what = 35;
			message.obj = error;
			handler.sendMessage(message);
			return;
		}
		// 第一步，创建HttpPost对象 
		HttpPost httpPost = new HttpPost(url); 

		// 设置HTTP POST请求参数必须用NameValuePair对象 
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("UserID", 
				String.valueOf(Integer.parseInt(account.three_number)|0x80000000))); 
		params.add(new BasicNameValuePair("SessionID", account.sessionId)); 
		params.add(new BasicNameValuePair("pageSize", "200")); 
		params.add(new BasicNameValuePair("Option", "2")); 
		params.add(new BasicNameValuePair("SenderList", deviceId)); 
		params.add(new BasicNameValuePair("CheckLevelType", "1")); 
		params.add(new BasicNameValuePair("VKey", encryption)); 
		LogMgr.showLog("url==>"+url+"?UserID="+String.valueOf(Integer.parseInt(account.three_number)|0x80000000)+
		  "&SessionID="+account.sessionId+"&pageSize=200&Option=2&SenderList="+deviceId+"&CheckLevelType=1&VKey="+encryption);
		HttpResponse httpResponse = null; 
		try { 
			// 设置httpPost请求参数 
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
			httpResponse = new DefaultHttpClient().execute(httpPost); 
			//System.out.println(httpResponse.getStatusLine().getStatusCode()); 
			if (httpResponse.getStatusLine().getStatusCode() == 200) { 
				// 第三步，使用getEntity方法活得返回结果 
				String result = EntityUtils.toString(httpResponse.getEntity()); 
				JSONObject jsonobject = new JSONObject(result);
				String error_code = jsonobject.getString("error_code");
				if(error_code!=null&&error_code.trim().equals("0")){
					String RL = jsonobject.getString("RL");
					String Surplus = jsonobject.getString("Surplus");
					Message message = new Message();
					message.what = 0;
					message.obj = RL;
					handler.sendMessage(message);
				}else if(error_code!=null&&error_code.trim().equals("23")){//会话ID不正确
					String error = jsonobject.getString("error");
					Message message = new Message();
					message.what = 23;
					message.obj = error;
					handler.sendMessage(message);
				}else if(error_code!=null&&error_code.trim().equals("35")){
//					String error = deviceId+"设备密码有误，获取不到该设备报警";
				    String error = jsonobject.getString("error");
					Message message = new Message();
					message.what = 35;
					message.obj = error;
					handler.sendMessage(message);
				}
			} 
		} catch (ClientProtocolException e) { 
			e.printStackTrace(); 
			DismissProgressDialog();
		} catch (IOException e) { 
			e.printStackTrace(); 
			DismissProgressDialog();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DismissProgressDialog();
		} finally{
			
		}
		LogMgr.showLog("method has end!");
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver!=null) {
			unregisterReceiver(mReceiver);
		}
		DismissProgressDialog();
	}

}
