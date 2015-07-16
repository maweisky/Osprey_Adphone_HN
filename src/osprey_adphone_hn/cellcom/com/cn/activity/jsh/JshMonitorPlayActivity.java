package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.RecordInfo;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.FileUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu.OnMenuButtonClickListener;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu.OnMenuItemClickListener;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.caldroid.CaldroidFragment;
import osprey_adphone_hn.cellcom.com.cn.widget.caldroid.CaldroidListener;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.global.P2PConnect;
import p2p.cellcom.com.cn.utils.PhoneWatcher;
import p2p.cellcom.com.cn.utils.T;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.MediaPlayer;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;
import com.p2p.core.P2PInterface.IP2P;
/**
 * 视频播放
 * @author Administrator
 *
 */
public class JshMonitorPlayActivity extends BaseMonitorActivity implements
		OnClickListener, IP2P {
	Context mContext;
	boolean isRegFilter = false;
	boolean mIsCloseVoice = false;
	int type;
	private ImageView iv_push;
	private ArcMenu photorecordarc;
	private ImageView invisibleiv;
	private LinearLayout ll_operation, ll_sp_operation;
	TextView close_voice, record, send_voice;
	TextView sp_record, sp_send_voice;
	private TextView sp_phone, photo, phototv, close_video;
	private TextView sp_set, set;
	private ImageView sp_close_voice;
	private TextView text_number;
	boolean is_send_voice = false;// 是否在对讲
	ImageView img_reverse;
	AudioManager mAudioManager = null;
	int mCurrentVolume, mMaxVolume;
	PhoneWatcher mPhoneWatcher;
	LinearLayout layout_voice_state;
	LinearLayout control_top;
	ImageView voice_state;
	TextView video_mode_hd, video_mode_sd, video_mode_ld;
	boolean isControlShow = false;
	boolean isReject = false;
	boolean isHD = false;
	int current_video_mode;
	String callId;
	String password;
	int USR_CMD_CAR_DIR_CTL = 7;
	private Handler mhandler = new Handler();

	private RelativeLayout /*rl_device,*/ rl_sp_device;
	private ImageView /*iv_left, iv_right,*/ iv_sp_left, iv_sp_right;
	private HorizontalScrollView /*hv_device,*/ hv_sp_device;
	private LinearLayout /*ll_device, */ll_sp_device;

	private FinalBitmap finalBitmap;

	private String deviceId = "";
	private String contactName = "";

	private boolean isLx = true;// 是否在录像

	// 当前屏幕方向
	boolean isLand = false;

	private View include_;
	private LinearLayout llAppBack, llAppSet;
	private TextView tvTopTitle;
	private Button os_jsh_wdj_lshf;
	private CaldroidFragment dialogCaldroidFragment;
	private ImageView photo_iv;
	private ImageView record_iv;
	private int height;
	private int width;
	private int leftoffset;
	private DisplayMetrics dm;
	private CustomProgressDialog m_ProgressDialog;
	private String recordpath="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		P2PConnect.setPlaying(true);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.p2p_monitor);
		type = this.getIntent().getIntExtra("type", -1);
		deviceId = getIntent().getStringExtra("deviceid");
		contactName = getIntent().getStringExtra("contactName");
		mContext = this;
		finalBitmap = FinalBitmap.create(this);
		if (ContextUtil.getHeith(this) > ContextUtil.getWidth(this)) {
			isLand = false;
		} else {
			isLand = true;
		}
		initLandComponent(savedInstanceState);
		regFilter();
		startWatcher();
		if (mAudioManager == null) {
			mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		}
//		if (mAudioManager != null) {
//			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//		}
		mCurrentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (mCurrentVolume > 0) {
			mIsCloseVoice = true;
			if (isLand) {
				close_voice.setCompoundDrawablesWithIntrinsicBounds(
						null,
						getResources().getDrawable(
								R.drawable.os_p2p_call_sound_out_s_landicon),
						null, null);
			} else {
				sp_close_voice
						.setImageResource(R.drawable.os_p2p_call_sound_out_s_icon);
			}
		} else {
			mIsCloseVoice = false;
			if (isLand) {
				close_voice.setCompoundDrawablesWithIntrinsicBounds(
						null,
						getResources().getDrawable(
								R.drawable.os_p2p_call_sound_out_landicon),
						null, null);
			} else {
				sp_close_voice
						.setImageResource(R.drawable.os_p2p_call_sound_out_icon);
			}
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

	public void initLandComponent(final Bundle savedInstanceState) {
		pView = (P2PView) findViewById(R.id.pView);
		this.initP2PView(P2PConnect.getCurrentDeviceType());
		setMute(true);

		control_top = (LinearLayout) findViewById(R.id.control_top);
		layout_voice_state = (LinearLayout) findViewById(R.id.layout_voice_state);

		include_ = (View) findViewById(R.id.include_);
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		llAppSet = (LinearLayout) findViewById(R.id.llAppSet);
		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
		tvTopTitle.setText(contactName);
		close_video = (TextView) findViewById(R.id.close_video);
		ll_operation = (LinearLayout) findViewById(R.id.ll_operation);
		iv_push = (ImageView) findViewById(R.id.iv_push);
		record = (TextView) findViewById(R.id.record);
		phototv = (TextView) findViewById(R.id.phototv);
		close_voice = (TextView) findViewById(R.id.close_voice);
		send_voice = (TextView) findViewById(R.id.send_voice);
		sp_phone = (TextView) findViewById(R.id.sp_phone);
		sp_record = (TextView) findViewById(R.id.sp_record);
		photorecordarc = (ArcMenu) findViewById(R.id.photorecordarc);
		ll_sp_operation = (LinearLayout) findViewById(R.id.ll_sp_operation);
		sp_close_voice = (ImageView) findViewById(R.id.sp_close_voice);
		sp_send_voice = (TextView) findViewById(R.id.sp_send_voice);
		sp_set = (TextView) findViewById(R.id.sp_set);
		text_number = (TextView) findViewById(R.id.text_number);
		text_number.setText("当前观看人数为：" + P2PConnect.getNumber());
		photo_iv = (ImageView) findViewById(R.id.photo_iv);
		record_iv = (ImageView) findViewById(R.id.record_iv);
		invisibleiv = (ImageView) findViewById(R.id.invisibleiv);
		invisibleiv.setVisibility(View.INVISIBLE);
		

		voice_state = (ImageView) findViewById(R.id.voice_state);

		// 全屏
//		rl_device = (RelativeLayout) findViewById(R.id.rl_device);
//		iv_left = (ImageView) findViewById(R.id.iv_left);
//		iv_right = (ImageView) findViewById(R.id.iv_right);
//		hv_device = (HorizontalScrollView) findViewById(R.id.hv_device);
//		ll_device = (LinearLayout) findViewById(R.id.ll_device);
		os_jsh_wdj_lshf = (Button) findViewById(R.id.lshfbtn);
		photo = (TextView) findViewById(R.id.photo);
		// 竖屏
		rl_sp_device = (RelativeLayout) findViewById(R.id.rl_sp_device);
		iv_sp_left = (ImageView) findViewById(R.id.iv_sp_left);
		iv_sp_right = (ImageView) findViewById(R.id.iv_sp_right);
		hv_sp_device = (HorizontalScrollView) findViewById(R.id.hv_sp_device);
		ll_sp_device = (LinearLayout) findViewById(R.id.ll_sp_device);
		set = (TextView) findViewById(R.id.set);
		if (isLand) {
			ll_operation.setVisibility(View.VISIBLE);
			os_jsh_wdj_lshf.setVisibility(View.GONE);
			ll_sp_operation.setVisibility(View.GONE);
			include_.setVisibility(View.GONE);
		} else {
			ll_operation.setVisibility(View.GONE);
			os_jsh_wdj_lshf.setVisibility(View.VISIBLE);
			ll_sp_operation.setVisibility(View.VISIBLE);
			include_.setVisibility(View.VISIBLE);
		}
		photorecordarc
				.setOnMenuButtonClickListener(new OnMenuButtonClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (photorecordarc.isOpen()) {
							iv_push.setVisibility(View.VISIBLE);
						} else {
							iv_push.setVisibility(View.GONE);
						}
					}
				});

		photorecordarc
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public void onClick(View view, int pos) {
						// TODO Auto-generated method stub
						if (photorecordarc.isOpen()) {
							iv_push.setVisibility(View.GONE);
						} else {
							iv_push.setVisibility(View.VISIBLE);
						}
						switch (pos) {
						case 2:
							captureScreen();
							break;
						case 3:
//							Toast.makeText(JshMonitorPlayActivity.this, "正在建设中", Toast.LENGTH_SHORT).show();
							record();
							break;
						default:
							break;
						}
					}
				});

		iv_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (photorecordarc.isOpen()) {
					iv_push.setVisibility(View.GONE);
					photorecordarc.toggleMenu(300);
				}
			}
		});

		phototv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureScreen();
			}
		});
//		iv_left.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				hv_device.arrowScroll(View.FOCUS_LEFT);
//			}
//		});
//		iv_right.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				hv_device.arrowScroll(View.FOCUS_RIGHT);
//			}
//		});

		iv_sp_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hv_sp_device.arrowScroll(View.FOCUS_LEFT);
			}
		});
		iv_sp_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hv_sp_device.arrowScroll(View.FOCUS_RIGHT);
			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		sp_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JshMonitorPlayActivity.this,
						JshInfoZpjFragmentActivity.class);
				startActivity(intent);
			}
		});

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
					// Setup arguments
					Bundle bundle = new Bundle();
					// Setup dialogTitle
					dialogCaldroidFragment.setArguments(bundle);
				}

				dialogCaldroidFragment.show(getSupportFragmentManager(),
						dialogTag);
			}
		});

		close_video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLand = false;
				ll_operation.setVisibility(View.GONE);
				sp_send_voice.setVisibility(View.VISIBLE);
				os_jsh_wdj_lshf.setVisibility(View.VISIBLE);
				ll_sp_operation.setVisibility(View.VISIBLE);
//				sp_record.setVisibility(View.VISIBLE);
				photorecordarc.setVisibility(View.VISIBLE);
				sp_phone.setVisibility(View.VISIBLE);
				sp_set.setVisibility(View.VISIBLE);
				include_.setVisibility(View.GONE);
				rl_sp_device.setVisibility(RelativeLayout.GONE);
				btmPORTRAITlayout();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
		sp_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reject();
				Intent setIntent = new Intent();
				setIntent.setClass(JshMonitorPlayActivity.this,
						JshWdjSetActivity.class);
				setIntent.putExtra("contact",
						FList.getInstance().isDevice(deviceId));
				setIntent.putExtra("type", P2PValue.DeviceType.NPC);
				startActivity(setIntent);
			}
		});
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reject();
				Intent setIntent = new Intent();
				setIntent.setClass(JshMonitorPlayActivity.this,
						JshWdjSetActivity.class);
				setIntent.putExtra("contact",
						FList.getInstance().isDevice(deviceId));
				setIntent.putExtra("type", P2PValue.DeviceType.NPC);
				startActivity(setIntent);
			}
		});
		// 全屏布局水平切换页面加载
//		for (int i = 0; i < FList.getInstance().list().size(); i++) {
//			final int position = i;
//			View view = getLayoutInflater().inflate(
//					R.layout.os_monitor_play_device_layout, ll_device, false);
//			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
//					ContextUtil.dip2px(this, 70), ContextUtil.dip2px(this, 50));
//			ll.setMargins(5, 5, 5, 5);
//			ImageView img = (ImageView) view.findViewById(R.id.iv_);
//			img.setScaleType(ScaleType.FIT_XY);
//			img.setLayoutParams(ll);
//			finalBitmap.display(img, FList.getInstance().list().get(i)
//					.getServerImgUrl());
//			img.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (FList.getInstance().getState(
//							FList.getInstance().get(position).getDeviceId()) != Constants.DeviceState.ONLINE) {
//						Toast.makeText(JshMonitorPlayActivity.this, "该设备不在线",
//								Toast.LENGTH_SHORT).show();
//					} else if (FList.getInstance().list().get(position)
//							.getDeviceId().equals(deviceId)) {
//						Toast.makeText(JshMonitorPlayActivity.this,
//								"当前播放的监控已是该设备", Toast.LENGTH_SHORT).show();
//					} else {
//						changeControl();
//						if (m_ProgressDialog == null
//								|| (m_ProgressDialog != null && !m_ProgressDialog
//										.isShowing())) {
//							ShowProgressDialog(R.string.app_progress);
//						}
//						boolean isOutCall = true;
//						String callId = FList.getInstance().get(position)
//								.getDeviceId();
//						deviceId = callId;
//						String contactName = FList.getInstance().get(position)
//								.getDeviceName();
//						JshMonitorPlayActivity.this.contactName = contactName;
//						type = 1;
//						String password = FList.getInstance().get(position)
//								.getDevicePassword();
//						P2PConnect
//								.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
//						P2PConnect.setCurrent_call_id(callId);
//
//						String push_mesg = NpcCommon.mThreeNum
//								+ ":"
//								+ mContext.getResources().getString(
//										R.string.p2p_call_push_mesg);
//						P2PHandler.getInstance().call(NpcCommon.mThreeNum,
//								password, true, type, callId, null, push_mesg);
//					}
//				}
//			});
//			ll_device.addView(view);
//		}

		// 竖屏布局水平切换页面加载
		for (int i = 0; i < FList.getInstance().list().size(); i++) {
			final int position = i;
			View view = getLayoutInflater()
					.inflate(R.layout.os_monitor_play_device_layout,
							ll_sp_device, false);
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					ContextUtil.dip2px(this, 70), ContextUtil.dip2px(this, 50));
			ll.setMargins(5, 5, 5, 5);
			ImageView img = (ImageView) view.findViewById(R.id.iv_);
			img.setScaleType(ScaleType.FIT_XY);
			img.setLayoutParams(ll);
			finalBitmap.display(img, FList.getInstance().list().get(i)
					.getServerImgUrl());
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (FList.getInstance().getState(
							FList.getInstance().get(position).getDeviceId()) != Constants.DeviceState.ONLINE) {
						Toast.makeText(JshMonitorPlayActivity.this, "该设备不在线",
								Toast.LENGTH_SHORT).show();
					} else if (FList.getInstance().list().get(position)
							.getDeviceId().equals(deviceId)) {
						Toast.makeText(JshMonitorPlayActivity.this,
								"当前播放的监控已是该设备", Toast.LENGTH_SHORT).show();
					} else {
						changeControl();
						if (m_ProgressDialog == null
								|| (m_ProgressDialog != null && !m_ProgressDialog
										.isShowing())) {
							ShowProgressDialog(R.string.app_progress);
						}
						String callId = FList.getInstance().get(position)
								.getDeviceId();
						deviceId = callId;
						String contactName = FList.getInstance().get(position)
								.getDeviceName();
						JshMonitorPlayActivity.this.contactName = contactName;
						type = 1;
						String password = FList.getInstance().get(position)
								.getDevicePassword();
						P2PConnect
								.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
						P2PConnect.setCurrent_call_id(callId);

						String push_mesg = NpcCommon.mThreeNum
								+ ":"
								+ mContext.getResources().getString(
										R.string.p2p_call_push_mesg);
						P2PHandler.getInstance().call(NpcCommon.mThreeNum,
								password, true, type, callId, null, push_mesg);
					}
				}
			});
			ll_sp_device.addView(view);
		}

		video_mode_hd = (TextView) findViewById(R.id.video_mode_hd);
		video_mode_sd = (TextView) findViewById(R.id.video_mode_sd);
		video_mode_ld = (TextView) findViewById(R.id.video_mode_ld);
		// current_video_mode = P2PConnect.getMode();
		// updateVideoModeText(current_video_mode);
//		current_video_mode = P2PValue.VideoMode.VIDEO_MODE_SD;
		current_video_mode = P2PValue.VideoMode.VIDEO_MODE_LD;
		P2PHandler.getInstance().setVideoMode(current_video_mode);
		updateVideoModeText(current_video_mode);
		if (P2PConnect.getCurrentDeviceType() == P2PValue.DeviceType.IPC) {
			video_mode_hd.setVisibility(RelativeLayout.VISIBLE);
		} else {
			video_mode_hd.setVisibility(RelativeLayout.GONE);
		}

		final AnimationDrawable anim = (AnimationDrawable) voice_state
				.getDrawable();
		OnPreDrawListener opdl = new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				anim.start();
				return true;
			}
		};
		voice_state.getViewTreeObserver().addOnPreDrawListener(opdl);

		llAppBack.setOnClickListener(this);
		record.setOnClickListener(this);
		close_voice.setOnClickListener(this);
		send_voice.setOnClickListener(this);
		// sp_record.setOnClickListener(this);
		sp_close_voice.setOnClickListener(this);
		sp_send_voice.setOnClickListener(this);
		video_mode_hd.setOnClickListener(this);
		video_mode_sd.setOnClickListener(this);
		video_mode_ld.setOnClickListener(this);
		os_jsh_wdj_lshf.setOnClickListener(this);
	}

	public CustomProgressDialog ShowProgressDialog(int p_MessageResID) {
		m_ProgressDialog = CustomProgressDialog.createDialog(this);
		m_ProgressDialog.setMessage(getString(p_MessageResID));
		m_ProgressDialog.setCanceledOnTouchOutside(false);
		m_ProgressDialog.show();
		return m_ProgressDialog;
	}

	public void DismissProgressDialog() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
		}
	}

	private void record() {
		// 录像
		if (isLx) {
			isLx = false;
			recordpath=FileUtil.initSDCardDirByRecordVideo(JshMonitorPlayActivity.this);
			recordpath=recordpath+File.separator+System.currentTimeMillis()+".mp4";
			Log.e("error", "recordpath==>"+recordpath);
			new Thread(new Runnable() {
              
              @Override
              public void run() {
                // TODO Auto-generated method stub
                P2PHandler.getInstance().setRecvAVDataEnable(true);
                MediaPlayer.getInstance().setAVFilePath(recordpath);
                MediaPlayer.getInstance().startRecoder();
              }
            }).start();
			
			Toast.makeText(JshMonitorPlayActivity.this, "开始录像",Toast.LENGTH_SHORT).show();
		} else {
		  new Thread(new Runnable() {
		    @Override
		    public void run() {
		      // TODO Auto-generated method stub
		      P2PHandler.getInstance().setRecvAVDataEnable(false);
	          MediaPlayer.getInstance().stopRecoder();
		    }
		  }).start();
			
			Toast.makeText(JshMonitorPlayActivity.this, "录像结束",Toast.LENGTH_SHORT).show();
			String account=SharepreferenceUtil.readString(JshMonitorPlayActivity.this, SharepreferenceUtil.fileName,
					"account");
			FinalDb finalDb=FinalDb.create(JshMonitorPlayActivity.this);
			RecordInfo recordInfo=new RecordInfo();
			recordInfo.setAccount(account);
			recordInfo.setPath(recordpath);
			recordInfo.setVideoName(contactName);
			recordInfo.setDate(ContextUtil.getCurrentTime());
			finalDb.save(recordInfo);
			isLx = true;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			btmlayout();
		}
	}

	private void btmPORTRAITlayout() {
		int width = sp_send_voice.getMeasuredWidth();

		leftoffset = width / 2;
		dm = new DisplayMetrics();
		JshMonitorPlayActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp1.setMargins(dm.heightPixels / 5 - leftoffset, 0, 0, 0);
		sp_send_voice.setLayoutParams(lp1);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.setMargins(dm.heightPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_record.setLayoutParams(lp2);

		RelativeLayout.LayoutParams lparc2 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lparc2.setMargins(dm.heightPixels * 2 / 5 - leftoffset,
				ContextUtil.dip2px(JshMonitorPlayActivity.this, 20), 0, 0);
		lparc2.addRule(RelativeLayout.BELOW, R.id.rl_sp_device);
		photorecordarc.setLayoutParams(lparc2);

		LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp3.setMargins(dm.heightPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_phone.setLayoutParams(lp3);

		LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp4.setMargins(dm.heightPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_set.setLayoutParams(lp4);
	}

	private void btmlayout() {
		height = sp_send_voice.getMeasuredHeight();
		width = sp_send_voice.getMeasuredWidth();

		leftoffset = width / 2;
		dm = new DisplayMetrics();
		JshMonitorPlayActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp1.setMargins(dm.widthPixels / 5 - leftoffset, 0, 0, 0);
		sp_send_voice.setLayoutParams(lp1);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.setMargins(dm.widthPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_record.setLayoutParams(lp2);

		RelativeLayout.LayoutParams lparc2 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lparc2.setMargins(dm.widthPixels * 2 / 5 - leftoffset,
				ContextUtil.dip2px(JshMonitorPlayActivity.this, 20), 0, 0);
		lparc2.addRule(RelativeLayout.BELOW, R.id.rl_sp_device);
		photorecordarc.setLayoutParams(lparc2);

		LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp3.setMargins(dm.widthPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_phone.setLayoutParams(lp3);

		LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp4.setMargins(dm.widthPixels / 5 - 2 * leftoffset, 0, 0, 0);
		sp_set.setLayoutParams(lp4);
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			dialogCaldroidFragment.dismiss();
			Intent intent = new Intent(JshMonitorPlayActivity.this,
					JshPlayBackListDayActivity.class);
			intent.putExtra("contact", FList.getInstance().isDevice(deviceId));
			intent.putExtra("date", date);
			startActivity(intent);
		}

		@Override
		public void onChangeMonth(int month, int year) {
			// String text = "month: " + month + " year: " + year;
			// Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void onLongClickDate(Date date, View view) {
			// Toast.makeText(getApplicationContext(),
			// "Long click " + formatter.format(date), Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void onCaldroidViewCreated() {
		}

	};

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.P2P_REJECT);
		filter.addAction(Constants.P2P.P2P_READY);
		filter.addAction(Constants.P2P.P2P_MONITOR_NUMBER_CHANGE);
		filter.addAction(Constants.P2P.P2P_RESOLUTION_CHANGE);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		try {
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				isLand = true;
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				isLand = false;
			}
			if (isLand) {
				ll_operation.setVisibility(View.GONE);
				ll_sp_operation.setVisibility(View.GONE);
				include_.setVisibility(View.GONE);
				rl_sp_device.setVisibility(RelativeLayout.GONE);
				os_jsh_wdj_lshf.setVisibility(View.GONE);
				sp_close_voice.setVisibility(View.GONE);
				close_video.setVisibility(View.VISIBLE);
			} else {
				ll_operation.setVisibility(View.GONE);
				ll_sp_operation.setVisibility(View.VISIBLE);
				include_.setVisibility(View.VISIBLE);
//				rl_device.setVisibility(RelativeLayout.GONE);
				os_jsh_wdj_lshf.setVisibility(View.VISIBLE);
				sp_close_voice.setVisibility(View.VISIBLE);
				close_video.setVisibility(View.GONE);
			}
		} catch (Exception ex) {
		}
		mHandler.sendEmptyMessageDelayed(0, 500);
	}

	public Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub.
			pView.updateScreenOrientation();
			return false;
		}
	});

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				reject();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				reject();
			} else if (intent.getAction().equals(
					Constants.P2P.P2P_RESOLUTION_CHANGE)) {
				int mode = intent.getIntExtra("mode", -1);
				if (mode != -1) {
					current_video_mode = mode;
					updateVideoModeText(current_video_mode);
				}
			} else if (intent.getAction().equals(Constants.P2P.P2P_READY)) {
				if (type == Constants.P2P_TYPE.P2P_TYPE_CALL) {
					// intentCall.setClass(mContext, VideoActivity.class);
				} else if (type == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
					DismissProgressDialog();
					tvTopTitle.setText(contactName);
					// JshMonitorPlayActivity.this.initP2PView(P2PConnect.getCurrentDeviceType());
					// setMute(true);
					current_video_mode = P2PValue.VideoMode.VIDEO_MODE_SD;
					P2PHandler.getInstance().setVideoMode(current_video_mode);
					updateVideoModeText(current_video_mode);
					if (P2PConnect.getCurrentDeviceType() == P2PValue.DeviceType.IPC) {
						video_mode_hd.setVisibility(RelativeLayout.VISIBLE);
					} else {
						video_mode_hd.setVisibility(RelativeLayout.GONE);
					}
				}
			} else if (intent.getAction().equals(
					Constants.P2P.P2P_MONITOR_NUMBER_CHANGE)) {
				int number = intent.getIntExtra("number", -1);
				if (number != -1) {
					text_number.setText("当前观看人数为: " + P2PConnect.getNumber());
				}
			}
		}
	};

	public void changeControl() {
		if (isControlShow) {
			isControlShow = false;
			// Animation anim2 = AnimationUtils.loadAnimation(this,
			// R.anim.slide_out_top);
			Animation anim2 = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out);
			anim2.setDuration(200);
			control_top.startAnimation(anim2);
			control_top.setVisibility(RelativeLayout.GONE);
			if (isLand) {
				ll_operation.startAnimation(anim2);
				ll_operation.setVisibility(View.GONE);
//				rl_device.startAnimation(anim2);
//				rl_device.setVisibility(RelativeLayout.GONE);
			} else {
				rl_sp_device.startAnimation(anim2);
				rl_sp_device.setVisibility(RelativeLayout.GONE);
			}
		} else {
			isControlShow = true;
			control_top.setVisibility(RelativeLayout.VISIBLE);
			// Animation anim2 = AnimationUtils.loadAnimation(this,
			// R.anim.slide_in_bottom);
			Animation anim2 = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in);
			anim2.setDuration(200);
			control_top.startAnimation(anim2);
			if (isLand) {
				ll_operation.setVisibility(View.VISIBLE);
				ll_operation.startAnimation(anim2);
//				rl_device.setVisibility(RelativeLayout.VISIBLE);
//				rl_device.startAnimation(anim2);
			} else {
				rl_sp_device.setVisibility(RelativeLayout.VISIBLE);
				rl_sp_device.startAnimation(anim2);
			}
		}
	}

	public void updateVideoModeText(int mode) {
		if (mode == P2PValue.VideoMode.VIDEO_MODE_HD) {
			video_mode_hd.setTextColor(mContext.getResources().getColor(
					R.color.blue));
			video_mode_sd.setTextColor(mContext.getResources().getColor(
					R.color.white));
			video_mode_ld.setTextColor(mContext.getResources().getColor(
					R.color.white));
		} else if (mode == P2PValue.VideoMode.VIDEO_MODE_SD) {
			video_mode_hd.setTextColor(mContext.getResources().getColor(
					R.color.white));
			video_mode_sd.setTextColor(mContext.getResources().getColor(
					R.color.blue));
			video_mode_ld.setTextColor(mContext.getResources().getColor(
					R.color.white));
		} else if (mode == P2PValue.VideoMode.VIDEO_MODE_LD) {
			video_mode_hd.setTextColor(mContext.getResources().getColor(
					R.color.white));
			video_mode_sd.setTextColor(mContext.getResources().getColor(
					R.color.white));
			video_mode_ld.setTextColor(mContext.getResources().getColor(
					R.color.blue));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.llAppBack:
			reject();
			break;
		case R.id.video_mode_hd:
			// 高清
			if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_HD) {
				current_video_mode = P2PValue.VideoMode.VIDEO_MODE_HD;
				P2PHandler.getInstance().setVideoMode(
						P2PValue.VideoMode.VIDEO_MODE_HD);
				updateVideoModeText(current_video_mode);
			}
			break;
		case R.id.video_mode_sd:
			// 标清
			if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_SD) {
				current_video_mode = P2PValue.VideoMode.VIDEO_MODE_SD;
				P2PHandler.getInstance().setVideoMode(
						P2PValue.VideoMode.VIDEO_MODE_SD);
				updateVideoModeText(current_video_mode);
			}
			break;
		case R.id.video_mode_ld:
			// 流畅
			if (current_video_mode != P2PValue.VideoMode.VIDEO_MODE_LD) {
				current_video_mode = P2PValue.VideoMode.VIDEO_MODE_LD;
				P2PHandler.getInstance().setVideoMode(
						P2PValue.VideoMode.VIDEO_MODE_LD);
				updateVideoModeText(current_video_mode);
			}
			break;
		case R.id.record:
			break;
		case R.id.close_voice:
			// 收音
			if (mIsCloseVoice) {
				mIsCloseVoice = false;
				if (isLand) {
					close_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_call_sound_out_landicon),
							null, null);
				} else {
					close_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_call_sound_out_icon),
							null, null);
				}
				if (mAudioManager != null) {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
							0);
				}
			} else {
				mIsCloseVoice = true;
				if (isLand) {
					close_voice
							.setCompoundDrawablesWithIntrinsicBounds(
									null,
									getResources()
											.getDrawable(
													R.drawable.os_p2p_call_sound_out_s_landicon),
									null, null);
				} else {
					close_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_call_sound_out_s_icon),
							null, null);
				}

				if (mCurrentVolume == 0) {
					mCurrentVolume = 1;
				}
				if (mAudioManager != null) {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							mCurrentVolume, 0);
				}
			}
			break;
		case R.id.send_voice:
			// 对讲
			if (!is_send_voice) {
				layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
				setMute(false);
				if (isLand) {
					send_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_send_audio_landicon),
							null, null);
				} else {
					send_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_send_audio_icon), null,
							null);
				}

				is_send_voice = true;
			} else {
				if (isLand) {
					send_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_speak_landicon), null,
							null);
				} else {
					send_voice.setCompoundDrawablesWithIntrinsicBounds(
							null,
							getResources().getDrawable(
									R.drawable.os_p2p_send_audio_off_icon),
							null, null);
				}

				mhandler.postDelayed(mrunnable, 1000);
				is_send_voice = false;
			}
			break;
		case R.id.sp_record:
			break;
		case R.id.sp_close_voice:
			// 收音
			if (mIsCloseVoice) {
				mIsCloseVoice = false;
				sp_close_voice
						.setImageResource(R.drawable.os_p2p_call_sound_out_icon);
				if (mAudioManager != null) {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
							0);
				}
			} else {
				mIsCloseVoice = true;
				sp_close_voice
						.setImageResource(R.drawable.os_p2p_call_sound_out_s_icon);

				if (mCurrentVolume == 0) {
					mCurrentVolume = 1;
				}
				if (mAudioManager != null) {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							mCurrentVolume, 0);
				}
			}
			break;
		case R.id.sp_send_voice:
			// 对讲
			if (!is_send_voice) {
				layout_voice_state.setVisibility(RelativeLayout.VISIBLE);
				setMute(false);
				sp_send_voice.setCompoundDrawablesWithIntrinsicBounds(
						null,
						getResources().getDrawable(
								R.drawable.os_p2p_send_audio_icon), null, null);
				is_send_voice = true;
			} else {
				sp_send_voice.setCompoundDrawablesWithIntrinsicBounds(
						null,
						getResources().getDrawable(
								R.drawable.os_p2p_send_audio_off_icon), null,
						null);
				mhandler.postDelayed(mrunnable, 1000);
				is_send_voice = false;
			}
			break;
		case R.id.lshfbtn:
			reject();
			Intent sp_playback = new Intent();
			sp_playback.setClass(JshMonitorPlayActivity.this,
					JshPlayBackListActivity.class);
			sp_playback.putExtra("contact",
					FList.getInstance().isDevice(deviceId));
			startActivity(sp_playback);
			break;
		}
	}

	Runnable mrunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			layout_voice_state.setVisibility(RelativeLayout.GONE);
			setMute(true);
		}
	};

	@Override
	public void onBackPressed() {
		reject();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			mCurrentVolume++;
			if (mCurrentVolume > mMaxVolume) {
				mCurrentVolume = mMaxVolume;
			}
			return false;
		} else if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			mCurrentVolume--;
			if (mCurrentVolume < 0) {
				mCurrentVolume = 0;
			}
			return false;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mAudioManager != null) {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					mCurrentVolume, 0);
		}
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}

		if (null != mPhoneWatcher) {
			mPhoneWatcher.stopWatcher();
		}
		P2PConnect.setPlaying(false);

		Intent refreshContans = new Intent();
		refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
		mContext.sendBroadcast(refreshContans);
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_MONITORACTIVITY;
	}

	@Override
	protected void onP2PViewSingleTap() {
		// TODO Auto-generated method stub
		changeControl();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isControlShow) {
					changeControl();
				}
			}
		}, 30000);
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

	@Override
	protected void onCaptureScreenResult(boolean isSuccess) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			// Capture success
			T.showShort(mContext, R.string.os_capture_success);
		} else {
			T.showShort(mContext, R.string.os_capture_failed);
		}
	}

	public void reject() {
		if (!isReject) {
			isReject = true;
			P2PHandler.getInstance().reject();
			finish();
		}
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				T.showShort(mContext, R.string.os_press_again_monitor);
				exitTime = System.currentTimeMillis();
			} else {
				isControlShow = false;
				reject();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void vCalling(boolean isOutCall, String threeNumber, int type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vReject(int reason_code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vAccept() {
		// TODO Auto-generated method stub

	}

	@Override
	public void vConnectReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public void vAllarming(String srcId, int type,
			boolean isSupportExternAlarm, int iGroup, int iItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vChangeVideoMask(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetPlayBackPos(int length, int currentPos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetPlayBackStatus(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vGXNotifyFlag(int flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetPlaySize(int iWidth, int iHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vRetPlayNumber(int iNumber) {
		// TODO Auto-generated method stub

	}

//	String path = ContextUtil.getSdCardPath();

	String lxpath = "";

	@Override
	public void vRecvAudioVideoData(byte[] AudioBuffer, int AudioLen,
			int AudioFrames, long AudioPTS, byte[] VideoBuffer, int VideoLen,
			long VideoPTS) {
		// TODO Auto-generated method stub
//		LogMgr.showLog("vRecvAudioVideoData==>" + VideoBuffer);
//		if (isLx) {
//			if (!lxpath.equals("")) {
//				FileOutputStream fos = null;
//				try {
//					if (VideoLen > 0) {
//						fos = new FileOutputStream(path);
//						fos.write(VideoBuffer);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally {
//					try {
//						if (fos != null) {
//							fos.close();
//						}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}
}
