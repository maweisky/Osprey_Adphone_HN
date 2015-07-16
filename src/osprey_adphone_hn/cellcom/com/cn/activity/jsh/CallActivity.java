package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.bean.NearlyTell;
import p2p.cellcom.com.cn.db.DataManager;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.global.P2PConnect;
import p2p.cellcom.com.cn.utils.PhoneWatcher;
import p2p.cellcom.com.cn.utils.T;
import p2p.cellcom.com.cn.utils.Utils;
import p2p.cellcom.com.cn.widget.HeaderView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.BaseCallActivity;
import com.p2p.core.P2PHandler;

public class CallActivity extends BaseCallActivity implements OnClickListener {
	// test svn
	Context mContext;
	PhoneWatcher mPhoneWatcher;
	TextView reject_text, title_text;
	RelativeLayout /*accept,*/ reject /*layout_accept*/;
	boolean isOutCall;
	HeaderView header_img;
	String callId;
	String contactName;
	String ipFlag;
	int type;
	String password;
	boolean isRegFilter = false;

	boolean isAccept = false;
	boolean isReject = false;
	private ImageView loadingImageView;
	private AnimationDrawable animationDrawable;
	FinalBitmap finalBitmap;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		mContext = this;
		setContentView(R.layout.activity_call);

		finalBitmap = FinalBitmap.create(this);
		isOutCall = this.getIntent().getBooleanExtra("isOutCall", false);
		callId = this.getIntent().getStringExtra("callId");
		contactName = this.getIntent().getStringExtra("contactName");
		ipFlag = this.getIntent().getStringExtra("ipFlag");
		type = this.getIntent().getIntExtra("type", -1);
		password = this.getIntent().getStringExtra("password");
		
		if (!Utils.hasDigit(callId)) {
			if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
				T.showShort(mContext, R.string.monitor_id_must_include_digit);
			} else {
				T.showShort(mContext, R.string.call_id_must_include_digit);
			}

			finish();
		} else {
//			if(password!=null&&!password.equals("null")&&!password.equals("")){
//				P2PHandler.getInstance().checkPassword(callId, password);
//			}
			P2PConnect.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
			P2PConnect.setCurrent_call_id(callId);
			initComponent();
			regFilter();
			startWatcher();

			String push_mesg = NpcCommon.mThreeNum
					+ ":"
					+ mContext.getResources().getString(
							R.string.p2p_call_push_mesg);
			// if (!P2PHandler.getInstance().call(NpcCommon.mThreeNum, password,
			// isOutCall, type, callId, ipFlag, push_mesg)) {
			// finish();
			// }
			P2PHandler.getInstance().call(NpcCommon.mThreeNum, password,
					isOutCall, type, callId, ipFlag, push_mesg);
		}
	}

	private void showLoading() {
		if (animationDrawable != null) {
			animationDrawable.start();
		}
	}

	private void hideLoading() {
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}
	
	@Override
	public void onHomePressed() {
		// TODO Auto-generated method stub
		super.onHomePressed();
		reject();
	}

	private void startWatcher() {
		mPhoneWatcher = new PhoneWatcher(mContext);
		mPhoneWatcher
				.setOnCommingCallListener(new PhoneWatcher.OnCommingCallListener() {

					@Override
					public void onCommingCall() {
						// TODO Auto-generated method stub
						reject();
					}
				});
		mPhoneWatcher.startWatcher();
	}

	public void initComponent() {
		/*accept = (RelativeLayout) findViewById(R.id.accept);
		layout_accept = (RelativeLayout) findViewById(R.id.layout_accept);*/
		reject = (RelativeLayout) findViewById(R.id.reject);
		reject_text = (TextView) findViewById(R.id.reject_text);
		title_text = (TextView) findViewById(R.id.title_text);
		header_img = (HeaderView) findViewById(R.id.header_img);
		loadingImageView=(ImageView)findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) loadingImageView
				.getBackground();
		
		OnPreDrawListener opdl = new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				animationDrawable.start();
				return true;
			}
		};
		loadingImageView.getViewTreeObserver().addOnPreDrawListener(opdl);
		if(FList.getInstance().isDevice(callId).getServerImgUrl()!=null&&
				(FList.getInstance().isDevice(callId).getServerImgUrl().contains(".jpg")||
						FList.getInstance().isDevice(callId).getServerImgUrl().contains(".png"))){
			finalBitmap.display(header_img, FList.getInstance().isDevice(callId).getServerImgUrl());
		}
		if (isOutCall) {
			reject_text.setText(R.string.hungup);
//			layout_accept.setVisibility(RelativeLayout.GONE);
			if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
				if (contactName != null && !contactName.equals("")) {
					title_text.setText(contactName);
				} else {
					title_text.setText(callId);
				}
			} else {
				if (contactName != null && !contactName.equals("")) {
					title_text.setText(contactName);
				} else {
					title_text.setText(callId);
				}
			}
		} else {
			reject_text.setText(R.string.reject);
//			layout_accept.setVisibility(RelativeLayout.VISIBLE);
			
			Device contact=FList.getInstance().isDevice(callId);
	      if(contact!=null){
	    	  title_text.setText(callId);
	      }else{
//	    	  title_text.setText(contact.contactName);
	      }
	       
		}
//		accept.setOnClickListener(this);
		reject.setOnClickListener(this);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.P2P_ACCEPT);
		filter.addAction(Constants.P2P.P2P_READY);
		filter.addAction(Constants.P2P.P2P_REJECT);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Constants.P2P.ACK_RET_CHECK_PASSWORD);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
				P2PHandler.getInstance().openAudioAndStartPlaying();
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
				Intent intentCall = new Intent();
				if (type == Constants.P2P_TYPE.P2P_TYPE_CALL) {
//					intentCall.setClass(mContext, VideoActivity.class);
				} else if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
					intentCall.setClass(mContext, JshMonitorPlayActivity.class);
				}
				intentCall.putExtra("type", type);
				intentCall.putExtra("deviceid", callId);
				intentCall.putExtra("contactName", contactName);
				intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intentCall);
				finish();
			} else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				reject();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				reject();
			}
			if (intent.getAction().equals(Constants.P2P.ACK_RET_CHECK_PASSWORD)) {
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					Toast.makeText(CallActivity.this, R.string.os_password_error, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	@Override
	public void onBackPressed() {
		reject();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.accept:
			if (!isAccept) {
				isAccept = true;
				P2PHandler.getInstance().accept();
			}
			break;*/
		case R.id.reject:
			reject();
			break;
		}
	}

	public void reject() {
		if (!isReject) {
			isReject = true;
			P2PHandler.getInstance().reject();
//			if (!activity_stack
//					.containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
//				Intent i = new Intent(CallActivity.this, MainActivity.class);
//				startActivity(i);
//			}
			finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}

		if (null != mPhoneWatcher) {
			mPhoneWatcher.stopWatcher();
		}
//		insertNearly();
	}

	public void insertNearly() {
		NearlyTell nearlyTell = new NearlyTell();
		nearlyTell.activeUser = NpcCommon.mThreeNum;
		nearlyTell.tellId = callId;
		nearlyTell.tellTime = String.valueOf(System.currentTimeMillis());
		nearlyTell.tellState = type;
		if (isOutCall && isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_OUT_REJECT;
		} else if (isOutCall && !isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_OUT_ACCEPT;
		} else if (!isOutCall && isReject) {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_IN_REJECT;
		} else {
			nearlyTell.tellState = NearlyTell.TELL_STATE_CALL_IN_ACCEPT;
		}
		DataManager.insertNearlyTell(mContext, nearlyTell);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_CALLACTIVITY;
	}

	@Override
	protected void onGoBack() {
		// TODO Auto-generated method stub
		MyApp.app.showNotification();
	}

	@Override
	protected void onGoFront() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

	@Override
	protected void onExit() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

}
