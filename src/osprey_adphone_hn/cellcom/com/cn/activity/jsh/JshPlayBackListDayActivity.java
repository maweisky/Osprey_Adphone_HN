package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.RecordAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.caldroid.CaldroidFragment;
import osprey_adphone_hn.cellcom.com.cn.widget.caldroid.CaldroidListener;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.P2PConnect;
import p2p.cellcom.com.cn.utils.T;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.p2p.core.P2PHandler;

public class JshPlayBackListDayActivity extends ActivityFrame {
	public Context mContext;
	Device contact;
	boolean isDpShow = false;
	boolean isSearchLayoutShow = false;
	boolean receiverIsReg;
	int selected_Date;

	private List<String> list = new ArrayList<String>();
	int waitload = 0;
	private JazzyListView listview;
	private RecordAdapter adapter;
	private LinearLayout llAppSet;
	private CaldroidFragment dialogCaldroidFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.activity_playback_listday);
		AppendTitleBody4();
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_hf));
		mContext = this;
		contact = (Device) getIntent().getSerializableExtra("contact");
		Date date = (Date) getIntent().getSerializableExtra("date");
		initComponent();
		regFilter();
		searchByTime(date);
		initListener(savedInstanceState);
	}

	private void initListener(final Bundle savedInstanceState) {
		llAppSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogCaldroidFragment = new CaldroidFragment();
				dialogCaldroidFragment.setCaldroidListener(listener);

				// If activity is recovered from rotation
				final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
				if (savedInstanceState != null) {
					dialogCaldroidFragment.restoreDialogStatesFromKey(
							getSupportFragmentManager(), savedInstanceState,
							"DIALOG_CALDROID_SAVED_STATE", dialogTag);
					Bundle args = dialogCaldroidFragment.getArguments();
					if (args == null) {
						args = new Bundle();
						dialogCaldroidFragment.setArguments(args);
					}
				} else {
					Bundle bundle = new Bundle();
					dialogCaldroidFragment.setArguments(bundle);
				}
				dialogCaldroidFragment.show(getSupportFragmentManager(),
						dialogTag);
			}
		});
	
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String filename = adapter.getList().get(arg2);
				CustomProgressDialog customProgressDialog = ShowProgressDialog(R.string.hsc_progress);
				customProgressDialog.setCanceledOnTouchOutside(false);
				customProgressDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface arg0, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
							P2PHandler.getInstance().reject();
						}
						return false;
					}
				});
				P2PConnect.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
				P2PConnect.setCurrent_call_id(contact.getDeviceId());
				P2PHandler.getInstance().playbackConnect(contact.getDeviceId(),
						contact.getDevicePassword(), filename, arg2);
			}
		});
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			dialogCaldroidFragment.dismiss();
			searchByTime(date);
		}

		@Override
		public void onChangeMonth(int month, int year) {
		}

		@Override
		public void onLongClickDate(Date date, View view) {
		}

		@Override
		public void onCaldroidViewCreated() {
		}

	};

	public void initComponent() {
		llAppSet = (LinearLayout) findViewById(R.id.llAppSet);
		listview = (JazzyListView) findViewById(R.id.listview);
		listview.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new RecordAdapter(mContext, list);
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_GET_PLAYBACK_FILES);
		filter.addAction(Constants.P2P.RET_GET_PLAYBACK_FILES);
		filter.addAction(Constants.P2P.P2P_ACCEPT);
		filter.addAction(Constants.P2P.P2P_READY);
		filter.addAction(Constants.P2P.P2P_REJECT);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		this.registerReceiver(receiver, filter);
		receiverIsReg = true;
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constants.P2P.RET_GET_PLAYBACK_FILES)) {
				String[] names = (String[]) intent
						.getCharSequenceArrayExtra("recordList");
				for (String str : names) {
					Log.e("playback", "recordList==>"+str);
					list.add(str);
				}
				if (waitload > 0) {
					Log.e("playback", "listview.setAdapter(adapter)");
					listview.setAdapter(adapter);
					DismissProgressDialog();
					waitload--;
				} else {
					Intent it = new Intent();
					it.setAction(Constants.Action.REPEAT_LOADING_DATA);
					sendBroadcast(it);
				}
			} else if (intent.getAction().equals(
					Constants.P2P.ACK_RET_GET_PLAYBACK_FILES)) {
				DismissProgressDialog();
				int result = intent.getIntExtra("result", -1);
				if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
					finish();
					T.showShort(mContext, R.string.os_password_error);
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
					T.showShort(mContext, R.string.os_net_error);
					waitload = 0;
				} else if (result == Constants.P2P_SET.ACK_RESULT.ACK_INSUFFICIENT_PERMISSIONS) {
					finish();
					T.showShort(mContext, R.string.os_insufficient_permissions);
				}
			} else if (intent.getAction().equals(Constants.P2P.P2P_ACCEPT)) {
				P2PHandler.getInstance().openAudioAndStartPlaying();
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
				DismissProgressDialog();
				Intent intentCall = new Intent();
				intentCall.setClass(JshPlayBackListDayActivity.this,
						PlayBackActivity.class);
				intentCall.putExtra("type",
						Constants.P2P_TYPE.P2P_TYPE_PLAYBACK);
				intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCall);
			} else if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				P2PHandler.getInstance().reject();
			}
		}
	};

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (receiverIsReg) {
			receiverIsReg = false;
			this.unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		P2PConnect.setPlayBack(true);
		if (!receiverIsReg) {
			regFilter();
		}
	}

	public void onDestroy() {
		super.onDestroy();
		P2PConnect.setPlayBack(false);
	}

	public void searchByTime(Date date) {
		waitload++;
		if (contact.getDevicePassword() == null
				|| contact.getDevicePassword().equals("")) {
			finish();
			T.showShort(mContext, R.string.os_password_error);
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date start = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date end = calendar.getTime();
		RecordAdapter.setStartTime(start);
		Log.e("playback", "startDate==>"+sdf.format(start));
		Log.e("playback", "endDate==>"+sdf.format(end));
		ShowProgressDialog(R.string.hsc_progress);
		P2PHandler.getInstance().getRecordFiles(contact.getDeviceId(),
				contact.getDevicePassword(), start, end);
	}
}
