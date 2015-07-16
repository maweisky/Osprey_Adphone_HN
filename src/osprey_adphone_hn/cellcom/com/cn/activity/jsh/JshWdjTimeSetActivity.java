package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.DateNumericAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.OnWheelScrollListener;
import osprey_adphone_hn.cellcom.com.cn.widget.wheel.WheelView;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.thread.DelayThread;
import p2p.cellcom.com.cn.utils.T;
import p2p.cellcom.com.cn.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

/**
 * @function 时间设置界面
 * @author 马伟 
 * @date 2015-3-30
 */
public class JshWdjTimeSetActivity extends ActivityFrame implements OnClickListener{
	private ImageView video_img;
	private FinalBitmap finalBitmap;
	private TextView videotv;
	private Device device;
	private boolean isRegFilter = false;
	WheelView date_year,date_month,date_day,date_hour,date_minute,w_urban;
	private Button setbtn;
//	RelativeLayout setting_urban_title;
//	TextView time_text;
//	ProgressBar progressBar;
	
	String cur_modify_time;
	int current_urban;
	Button bt_set_timezone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_timeset);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		initView();
		initData();
		regFilter();
		P2PHandler.getInstance().getDeviceTime(device.getDeviceId(), device.getDevicePassword());
		P2PHandler.getInstance().getNpcSettings(device.getDeviceId(), device.getDevicePassword());
	}

	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getResources().getString(R.string.os_jsh_wdj_sbsz_sjsz));
		device = (Device) getIntent().getExtras().getSerializable("contact");
		finalBitmap = FinalBitmap.create(JshWdjTimeSetActivity.this);
		if (!TextUtils.isEmpty(device.getServerImgUrl())) {
			finalBitmap.display(video_img, device.getServerImgUrl());
		}
		videotv.setText(device.getDeviceName());
	}

	private void initView() {
		// TODO Auto-generated method stub
		video_img = (ImageView) findViewById(R.id.video_img);
		videotv = (TextView) findViewById(R.id.videotv);
		
		setbtn=(Button)findViewById(R.id.setbtn);
//		time_text = (TextView) findViewById(R.id.time_text);
//		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		setbtn.setEnabled(false);
		setbtn.setOnClickListener(this);
		
		timeShow(new Date());
		bt_set_timezone=(Button)findViewById(R.id.bt_set_timezone);
		bt_set_timezone.setOnClickListener(this);
//		setting_urban_title=(RelativeLayout)findViewById(R.id.setting_urban_title);
	}

	private void timeShow(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// year
        int curYear = calendar.get(Calendar.YEAR);
		date_year = (WheelView) findViewById(R.id.date_year);
		date_year.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 2010, 2036));
		date_year.setCurrentItem(curYear-2010);
		date_year.addScrollingListener(scrolledListener);
		date_year.setCyclic(true);
		
		int curMonth = calendar.get(Calendar.MONTH)+1;
		date_month = (WheelView) findViewById(R.id.date_month);
		date_month.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 12));
		date_month.setCurrentItem(curMonth-1);
		date_month.addScrollingListener(scrolledListener);
		date_month.setCyclic(true);
		
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		date_day = (WheelView) findViewById(R.id.date_day);
		date_day.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 31));
		date_day.setCurrentItem(curDay-1);
		date_day.addScrollingListener(scrolledListener);
		date_day.setCyclic(true);
		
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		date_hour = (WheelView) findViewById(R.id.date_hour);
		date_hour.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 0, 23));
		date_hour.setCurrentItem(curHour);
		date_hour.setCyclic(true);
		
		int curMinute = calendar.get(Calendar.MINUTE);
		date_minute = (WheelView) findViewById(R.id.date_minute);
		date_minute.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 0, 59));
		date_minute.setCurrentItem(curMinute);
		date_minute.setCyclic(true);
		
		w_urban=(WheelView)findViewById(R.id.w_urban);
		w_urban.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, -11, 12));
		w_urban.setCyclic(true);
	}

	private boolean wheelScrolled = false;
	  
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
	      public void onScrollingStarted(WheelView wheel) {
	          wheelScrolled = true;
	          updateStatus();
	      }
	      public void onScrollingFinished(WheelView wheel) {
	          wheelScrolled = false;
	          updateStatus();
	      }
	  };

	
	public void updateStatus(){
		int year = date_year.getCurrentItem()+2010;
		int month = date_month.getCurrentItem()+1;
		
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
			date_day.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 31));
		}else if(month==2){
			
			boolean isLeapYear = false;
			if(year%100==0){
				if(year%400==0){
					isLeapYear = true;
				}else{
					isLeapYear = false;
				}
			}else{
				if(year%4==0){
					isLeapYear = true;
				}else{
					isLeapYear = false;
				}
			}
			if(isLeapYear){
				if(date_day.getCurrentItem()>28){
					date_day.scroll(30, 2000);
	    		}
				date_day.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 29));
			}else{
				if(date_day.getCurrentItem()>27){
					date_day.scroll(30, 2000);
	    		}
				date_day.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 28));
			}
			
		}else{
			if(date_day.getCurrentItem()>29){
				date_day.scroll(30, 2000);
			}
			date_day.setViewAdapter(new DateNumericAdapter(JshWdjTimeSetActivity.this, 1, 30));
		}
		
	}
	
	public void regFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.ACK_RET_SET_TIME);
		filter.addAction(Constants.P2P.ACK_RET_GET_TIME);
		filter.addAction(Constants.P2P.RET_SET_TIME);
		filter.addAction(Constants.P2P.RET_GET_TIME);
		filter.addAction(Constants.P2P.RET_GET_TIME_ZONE);
		filter.addAction(Constants.P2P.ACK_RET_SET_TIME_ZONE);
		JshWdjTimeSetActivity.this.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction().equals(Constants.P2P.RET_GET_TIME)){
				String time = intent.getStringExtra("time");
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date;
				try {
					date = simpleDateFormat.parse(time);
					timeShow(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DismissProgressDialog();
//				progressBar.setVisibility(RelativeLayout.GONE);
				setbtn.setEnabled(true);
			}else if(intent.getAction().equals(Constants.P2P.RET_SET_TIME)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.DEVICE_TIME_SET.SETTING_SUCCESS){
					SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date date;
					try {
						date = simpleDateFormat.parse(cur_modify_time);
						timeShow(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DismissProgressDialog();
//					progressBar.setVisibility(RelativeLayout.GONE);
					setbtn.setEnabled(true);
					JshWdjTimeSetActivity.this.finish();
					T.showShort(JshWdjTimeSetActivity.this, R.string.os_modify_success);
				}else{
					DismissProgressDialog();
//					progressBar.setVisibility(RelativeLayout.GONE);
					setbtn.setEnabled(true);
					T.showShort(JshWdjTimeSetActivity.this, R.string.os_operator_error);
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_GET_TIME)){
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjTimeSetActivity.this.sendBroadcast(i);
					
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:get npc time");
					P2PHandler.getInstance().getDeviceTime(device.getDeviceId(), device.getDevicePassword());
				}
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_TIME)){
				
				int result = intent.getIntExtra("result", -1);
				if(result==Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR){
					Intent i = new Intent();
					i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
					JshWdjTimeSetActivity.this.sendBroadcast(i);
				}else if(result==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					Log.e("my","net error resend:set npc time");
					P2PHandler.getInstance().setDeviceTime(device.getDeviceId(), device.getDevicePassword(), cur_modify_time);
				}
				
			}else if(intent.getAction().equals(Constants.P2P.RET_GET_TIME_ZONE)){
				 int timezone=intent.getIntExtra("state", -1);
				 if(timezone!=-1){
//					 setting_urban_title.setVisibility(RelativeLayout.VISIBLE);
				 }
				 w_urban.setCurrentItem(timezone);
			}else if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_TIME_ZONE)){
				int state=intent.getIntExtra("state",-1);
				if(state==Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS){
					T.showShort(JshWdjTimeSetActivity.this,R.string.os_timezone_success);
					P2PHandler.getInstance().getDeviceTime(device.getDeviceId(), device.getDevicePassword());
				}else if(state==Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR){
					P2PHandler.getInstance().setTimeZone(device.getDeviceId(),device.getDeviceName(),current_urban);
				}
			}
		}
	};
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.setbtn:
			ShowProgressDialog(R.string.hsc_progress);
//			progressBar.setVisibility(RelativeLayout.VISIBLE);
			setbtn.setEnabled(false);
			new DelayThread(Constants.SettingConfig.SETTING_CLICK_TIME_DELAY,new DelayThread.OnRunListener() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					cur_modify_time = Utils.convertDeviceTime(
							date_year.getCurrentItem()+10,
							date_month.getCurrentItem()+1,
							date_day.getCurrentItem()+1,
							date_hour.getCurrentItem(), 
							date_minute.getCurrentItem());
					P2PHandler.getInstance().setDeviceTime(device.getDeviceId(), device.getDevicePassword(), cur_modify_time);
				}
			}).start();
			break;
		case R.id.bt_set_timezone:
			current_urban=w_urban.getCurrentItem();
			P2PHandler.getInstance().setTimeZone(device.getDeviceId(), device.getDevicePassword(), current_urban);	
		    break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Intent it=new Intent();
		it.setAction(Constants.Action.CONTROL_BACK);
		JshWdjTimeSetActivity.this.sendBroadcast(it);
		if(isRegFilter){
			JshWdjTimeSetActivity.this.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
	}
}