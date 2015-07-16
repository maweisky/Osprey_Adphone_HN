package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.afinal.simplecache.NetUtils;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.adapter.ContactListAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.DialAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.CallLogBean;
import osprey_adphone_hn.cellcom.com.cn.bean.CallLogInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.ContactBean;
import osprey_adphone_hn.cellcom.com.cn.db.DBManager;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.util.Tool;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp.HttpWayMode;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.CellComAjaxResult.ParseType;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.LogMgr;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 电话煲
 * 
 * @author ma
 * 
 */
public class DhbBhpActivity extends FragmentBase implements OnClickListener {
	private DhbFragmentActivity act;
	private LinearLayout mKeyboardView;
	private ListView callLogListView;
	private AsyncQueryHandler asyncQuery;
	private DialAdapter adapter;
	private List<CallLogBean> callLogs = new ArrayList<CallLogBean>();

	private AsyncQueryHandler contactasyncQueryHandler; // 异步查询数据库类对象
	private ContactListAdapter contactListAdapter;
	private Filter mFilter;
	private List<ContactBean> list = new ArrayList<ContactBean>();
	private Map<Integer, ContactBean> contactIdMap = null;

	// private List<CallLogInfo> callList = new ArrayList<CallLogInfo>();

	private Button btn9, btn8, btn7, btn6, btn5, btn4, btn3, btn2, btn1, btn0,
			xbtn, jbtn;
	private ImageView addiv;
	private ImageView phoneiv;
	private ImageView deleteiv;
	private ImageView keyboariv;
	private EditText keyboard_et;
	private boolean isshow = false;
	private FinalBitmap finalBitmap;

	private static final int DTMF_DURATION_MS = 120; // 声音的播放时间
	private Object mToneGeneratorLock = new Object(); // 监视器对象锁
	private ToneGenerator mToneGenerator; // 声音产生器
	private static boolean mDTMFToneEnabled; // 系统参数“按键操作音”标志位

	public boolean isIsshow() {
		return isshow;
	}

	public void setIsshow(boolean isshow) {
		this.isshow = isshow;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (DhbFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_dhb_bhp_activity, container,
				false);
		initView(v, savedInstanceState);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}

	/**
	 * 播放按键声音
	 */
	private void playTone(int tone) {
		if (!mDTMFToneEnabled) {
			return;
		}
		AudioManager audioManager = (AudioManager) act
				.getSystemService(Context.AUDIO_SERVICE);
		int ringerMode = audioManager.getRingerMode();
		if (ringerMode == AudioManager.RINGER_MODE_SILENT
				|| ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
			// 静音或者震动时不发出声音
			return;
		}
		synchronized (mToneGeneratorLock) {
			if (mToneGenerator == null) {
				return;
			}
			mToneGenerator.startTone(tone, DTMF_DURATION_MS); // 发出声音
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
		keyboard_et = (EditText) v.findViewById(R.id.keyboard_et);
		mKeyboardView = (LinearLayout) v.findViewById(R.id.mKeyboardView);
		callLogListView = (ListView) v.findViewById(R.id.call_log_list);

		addiv = (ImageView) v.findViewById(R.id.addiv);
		phoneiv = (ImageView) v.findViewById(R.id.phoneiv);
		deleteiv = (ImageView) v.findViewById(R.id.deleteiv);
		keyboariv = (ImageView) v.findViewById(R.id.keyboariv);
		xbtn = (Button) v.findViewById(R.id.xbtn);
		jbtn = (Button) v.findViewById(R.id.jbtn);
		btn0 = (Button) v.findViewById(R.id.btn0);
		btn1 = (Button) v.findViewById(R.id.btn1);
		btn2 = (Button) v.findViewById(R.id.btn2);
		btn3 = (Button) v.findViewById(R.id.btn3);
		btn4 = (Button) v.findViewById(R.id.btn4);
		btn5 = (Button) v.findViewById(R.id.btn5);
		btn6 = (Button) v.findViewById(R.id.btn6);
		btn7 = (Button) v.findViewById(R.id.btn7);
		btn8 = (Button) v.findViewById(R.id.btn8);
		btn9 = (Button) v.findViewById(R.id.btn9);

	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		keyboariv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(); // Itent就是我们要发送的内容
				intent.setAction("osprey_adphone_hn.cellcom.com.cn.keyborad"); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
				act.sendBroadcast(intent); // 发送广播
				hideKeyboardWithAnimation();
			}
		});
		keyboard_et.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		callLogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				String huafei = SharepreferenceUtil.readString(act,
						SharepreferenceUtil.fileName, "huafei", "");
				if (TextUtils.isEmpty(huafei)
						|| Float.parseFloat(huafei) <= 0.3) {
					ShowAlertDialog("余额不足", "请兑换话费或参加活动",
							new MyDialogInterface() {

								@Override
								public void onClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
				} else {
					// 无网络拨打系统电话
					if (!NetUtils.isConnected(act)) {
						// 用intent启动拨打电话
						ShowAlertDialog("温馨提示", "暂无网络，将用本机呼叫",
								new MyDialogInterface() {

									@Override
									public void onClick(DialogInterface dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										if (parent.getItemAtPosition(position) instanceof CallLogBean) {
											Intent intent = new Intent(
													Intent.ACTION_CALL,
													Uri.parse("tel:"
															+ ((CallLogBean) parent
																	.getItemAtPosition(position))
																	.getCallnum()));
											startActivity(intent);
										} else {
											Intent intent = new Intent(
													Intent.ACTION_CALL,
													Uri.parse("tel:"
															+ ((ContactBean) parent
																	.getItemAtPosition(position))
																	.getPhoneNum()));
											startActivity(intent);
										}
									}
								});
					} else {
						final Intent intent = new Intent(act,
								DhbBhpCallActivity.class);
						if (parent.getItemAtPosition(position) instanceof CallLogBean) {
							intent.putExtra("phone", ((CallLogBean) parent
									.getItemAtPosition(position)).getCallnum());
							intent.putExtra("cname", ((CallLogBean) parent
									.getItemAtPosition(position)).getCallname());
							startActivity(intent);
						} else {
							intent.putExtra("phone", ((ContactBean) parent
									.getItemAtPosition(position)).getPhoneNum());
							intent.putExtra("cname", ((ContactBean) parent
									.getItemAtPosition(position))
									.getDesplayName());
							startActivity(intent);
						}
					}
				}
			}
		});
		callLogListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
					break;
				}
				LogMgr.showLog("scrollState=>" + scrollState);
				Intent intent = new Intent(); // Itent就是我们要发送的内容
				intent.setAction("osprey_adphone_hn.cellcom.com.cn.keyborad"); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
				act.sendBroadcast(intent); // 发送广播
				hideKeyboardWithAnimation();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		keyboard_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				doAfterTextChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		deleteiv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String number = keyboard_et.getText().toString();
				if (number != null && number.length() > 0) {
					keyboard_et.setText("");
					keyboard_et.setSelection("".length());
				}
				return false;
			}
		});
	}

	public boolean enoughToFilter() {
		return keyboard_et.getText().length() > 0;
	}

	private void doAfterTextChanged() {
		if (enoughToFilter()) {
			callLogListView.setAdapter(contactListAdapter);
			if (mFilter != null) {
				mFilter.filter(keyboard_et.getText().toString().trim());
			}
		} else {
			callLogListView.setAdapter(adapter);
			// if (mFilter != null) {
			// mFilter.filter(null);
			// }
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		finalBitmap = FinalBitmap.create(act);
		showKeyboardWithAnimation();
		isshow = true;
		// 获取通话记录(暂不使用本地通话记录)
		asyncQuery = new MyAsyncQueryHandler(act.getContentResolver());
		Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
		// 查询的列
		String[] projection = { CallLog.Calls.DATE, // 日期
				CallLog.Calls.NUMBER, // 号码
				CallLog.Calls.TYPE, // 类型
				CallLog.Calls.CACHED_NAME, // 名字
				CallLog.Calls._ID, // id
		};
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		// 从服务端获取用户通话记录
		// setAdapter();
		// getUserCallLogInfoFromDB();
		// getUserCallLogs();

		// 获取联系人
		contactListAdapter = new ContactListAdapter(act, list, null);
		mFilter = contactListAdapter.getFilter();
		// 实例化
		contactasyncQueryHandler = new ContactAsyncQueryHandler(
				act.getContentResolver());
		Uri uricontact = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projectioncontact = {
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		// 按照sort_key升序查詢
		contactasyncQueryHandler
				.startQuery(0, null, uricontact, projectioncontact, null, null,
						"sort_key COLLATE LOCALIZED asc");

		jbtn.setOnClickListener(this);
		xbtn.setOnClickListener(this);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		deleteiv.setOnClickListener(this);
		phoneiv.setOnClickListener(this);
		addiv.setOnClickListener(this);

		// 按键声音播放设置及初始化
		try {
			// 获取系统参数“按键操作音”是否开启
			mDTMFToneEnabled = Settings.System.getInt(act.getContentResolver(),
					Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
			synchronized (mToneGeneratorLock) {
				if (mDTMFToneEnabled && mToneGenerator == null) {
					mToneGenerator = new ToneGenerator(
							AudioManager.STREAM_DTMF, 80); // 设置声音的大小
					act.setVolumeControlStream(AudioManager.STREAM_DTMF);
				}
			}
		} catch (Exception e) {
			mDTMFToneEnabled = false;
			mToneGenerator = null;
		}
	}

	public void hideKeyboard() {
		hideKeyboardWithAnimation();
	}

	private void hideKeyboardWithAnimation() {
		if (isshow) {
			ObjectAnimator
					.ofFloat(mKeyboardView, "translationY", 0,
							mKeyboardView.getHeight()).setDuration(1000)
					.start();
			isshow = false;
		}
	}

	/***
	 * Mostra la tastiera a schermo con una animazione di slide dal basso
	 */
	private void showKeyboardWithAnimation() {
		if (!isshow) {
			if (mKeyboardView != null) {
				ObjectAnimator
						.ofFloat(mKeyboardView, "translationY",
								mKeyboardView.getHeight(), 0).setDuration(1000)
						.start();
				isshow = true;
			}
		}
	}

	public void showWithAnimation(Animation animation) {
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mKeyboardView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

		mKeyboardView.setAnimation(animation);
	}

	public void hidleWithAnimation(Animation animation) {
		// mKeyboardView.setAnimation(animation);
		mKeyboardView.setVisibility(View.GONE);
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				callLogs = new ArrayList<CallLogBean>();
				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
				Date date;
				cursor.moveToFirst(); // 游标移动到第一项
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					date = new Date(cursor.getLong(cursor
							.getColumnIndex(CallLog.Calls.DATE)));
					String number = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.NUMBER));
					int type = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls.TYPE));
					String cachedName = cursor.getString(cursor
							.getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称与电话号码，如果它的存在
					int id = cursor.getInt(cursor
							.getColumnIndex(CallLog.Calls._ID));

					CallLogBean callLogBean = new CallLogBean();
					callLogBean.setId(id);
					callLogBean.setCallnum(Tool.GetNumber(number));
					callLogBean.setCallname(Tool.GetNumber(cachedName));
					if (null == cachedName || "".equals(cachedName)) {
						callLogBean.setCallname(number);
					}
					callLogBean.setType(type);
					callLogBean.setDate(sfd.format(date));

					callLogs.add(callLogBean);
				}
				if (callLogs.size() > 0) {
					setAdapter();
				}
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class ContactAsyncQueryHandler extends AsyncQueryHandler {

		public ContactAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Integer, ContactBean>();
				list = new ArrayList<ContactBean>();
				cursor.moveToFirst(); // 游标移动到第一项
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					if (contactIdMap.containsKey(contactId)) {
						// 无操作
					} else {
						// 创建联系人对象
						ContactBean contact = new ContactBean();
						contact.setDesplayName(Tool.GetNumber(name));
						contact.setPosdesplayName(Tool.GetNumber(name));
						contact.setPhoneNum(Tool.GetNumber(number));
						contact.setPosphoneNum(Tool.GetNumber(number));
						contact.setSortKey(sortKey);
						contact.setPhotoId(photoId);
						contact.setLookUpKey(lookUpKey);
						list.add(contact);

						contactIdMap.put(contactId, contact);
					}
				}
				if (list.size() > 0) {
					contactListAdapter.setList(list);
				}
			}

			super.onQueryComplete(token, cookie, cursor);
		}
	}

	private void setAdapter() {
		adapter = new DialAdapter(act, this, finalBitmap, callLogs);
		callLogListView.setAdapter(adapter);
	}

	public void controlActivity() {
		// TODO Auto-generated method stub
		if (isshow) {
			hideKeyboardWithAnimation();
		} else {
			showKeyboardWithAnimation();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String number = keyboard_et.getText().toString();
		switch (v.getId()) {
		case R.id.btn1:
			playTone(ToneGenerator.TONE_DTMF_1);
			spellNum("1");
			break;
		case R.id.btn2:
			playTone(ToneGenerator.TONE_DTMF_2);
			spellNum("2");
			break;
		case R.id.btn3:
			playTone(ToneGenerator.TONE_DTMF_3);
			spellNum("3");
			break;
		case R.id.btn4:
			playTone(ToneGenerator.TONE_DTMF_4);
			spellNum("4");
			break;
		case R.id.btn5:
			playTone(ToneGenerator.TONE_DTMF_5);
			spellNum("5");
			break;
		case R.id.btn6:
			playTone(ToneGenerator.TONE_DTMF_6);
			spellNum("6");
			break;
		case R.id.btn7:
			playTone(ToneGenerator.TONE_DTMF_7);
			spellNum("7");
			break;
		case R.id.btn8:
			playTone(ToneGenerator.TONE_DTMF_8);
			spellNum("8");
			break;
		case R.id.btn9:
			playTone(ToneGenerator.TONE_DTMF_9);
			spellNum("9");
			break;
		case R.id.jbtn: // #
			playTone(ToneGenerator.TONE_DTMF_P);
			spellNum("#");
			break;
		case R.id.xbtn:
			playTone(ToneGenerator.TONE_DTMF_S);
			spellNum("*");
			break;
		case R.id.btn0:
			playTone(ToneGenerator.TONE_DTMF_0);
			spellNum("0");
			break;
		case R.id.addiv:
			Intent intentcontact = new Intent(Intent.ACTION_INSERT);
			intentcontact.setType("vnd.android.cursor.dir/person");
			intentcontact.setType("vnd.android.cursor.dir/contact");
			intentcontact.setType("vnd.android.cursor.dir/raw_contact");
			intentcontact.putExtra(Intents.Insert.PHONE, number);
			startActivity(intentcontact);
			break;
		case R.id.phoneiv:
			if (!TextUtils.isEmpty(keyboard_et.getText().toString())) {
				String huafei = SharepreferenceUtil.readString(act,
						SharepreferenceUtil.fileName, "huafei", "");
				if (TextUtils.isEmpty(huafei)
						|| Float.parseFloat(huafei) <= 0.3) {
					ShowAlertDialog("余额不足", "请兑换话费或参加活动",
							new MyDialogInterface() {

								@Override
								public void onClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
				} else {
					// 无网络拨打系统电话
					if (!NetUtils.isConnected(act)) {
						// 用intent启动拨打电话
							Intent intent = new Intent(
									Intent.ACTION_CALL,
									Uri.parse("tel:"
											+ keyboard_et.getText().toString()));
							startActivity(intent);
					} else {
						final Intent intent = new Intent(act,
								DhbBhpCallActivity.class);
						intent.putExtra("cname", "");
						intent.putExtra("phone", keyboard_et
								.getText().toString());
						startActivity(intent);
					}
				}
			} else {
				ShowMsg("请输入号码");
			}
			break;
		case R.id.deleteiv:// 删除键
			if (number != null && number.length() > 0) {
				number = number.substring(0, number.length() - 1);
				keyboard_et.setText(number);
				keyboard_et.setSelection(number.length());
			}
			break;
		/*
		 * case R.id.button_vedio: if (number!=null && number.trim().length()>0)
		 * { Contact contact =
		 * DataManager.findContactByActiveUserAndContactId(mContext,
		 * NpcCommon.mThreeNum, number); if (contact != null &&
		 * contact.contactId != null && !contact.contactPassword.equals("")) {
		 * Intent monitor =new Intent(); monitor.setClass(mContext,
		 * CallActivity.class); monitor.putExtra("callId", number);
		 * monitor.putExtra("contactName", contact.contactName);
		 * monitor.putExtra("password", contact.contactPassword);
		 * monitor.putExtra("isOutCall", true); monitor.putExtra("type",
		 * Constants.P2P_TYPE.P2P_TYPE_MONITOR); startActivity(monitor); }else {
		 * showInputPwd(); } }else{ T.showShort(mContext, R.string.dialog_tip);
		 * } break; case R.id.button_vedio_phone: break;
		 */
		default:
			break;
		}
	}

	private void spellNum(String value) {
		int selectIndex = 0;

		String number = keyboard_et.getText().toString();
		if (number != null && number.length() > 0) {
			if (number.length() >= 17) {
				return;
			}
			int cursorIndex = keyboard_et.getSelectionEnd();
			if (0 == keyboard_et.getSelectionEnd()) {
				number = number + value;
				selectIndex = number.length();
			} else if (0 < keyboard_et.getSelectionEnd()
					&& cursorIndex < number.length()) {
				String startNum = number.substring(0, cursorIndex);
				String endNum = number.substring(cursorIndex);
				number = startNum + value + endNum;
				selectIndex = cursorIndex + 1;
			} else if (0 < keyboard_et.getSelectionEnd()
					&& cursorIndex == number.length()) {
				number = number + value;
				selectIndex = number.length();
			} else {
				number = number + value;
			}
		} else {
			number = value;
			selectIndex = number.length();
		}
		keyboard_et.setText(number);
		keyboard_et.setSelection(number.length());
	}

	// 从服务端获取用户通话记录
	private void getUserCallLogs() {
		// TODO Auto-generated method stub
		String uid = SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETCALL_LOGLIST,
				cellComAjaxParams, HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						// TODO Auto-generated method stub
						try {
							CallLogInfoComm comm = t.read(
									CallLogInfoComm.class, ParseType.GSON);
							if (!comm.getReturnCode().equals("1")) {
								Toast.makeText(act, comm.getReturnMessage(),
										Toast.LENGTH_SHORT).show();
								return;
							}
							if (comm.getBody().size() <= 0) {
								ShowMsg("暂无通话记录！");
							} else {
								mergeCallLogInfo(comm.getBody());
								// ShowMsg("获取通话记录成功！");
							}
						} catch (Exception e) {
							// TODO: handle exception
							ShowMsg("服务器请求失败!");
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
					}
				});
	}

	// 把重复的用户通话记录合并
	private void mergeCallLogInfo(List<CallLogBean> list) {
		callLogs.clear();
		DBManager.deleteCallLog(act);
		// CallLogInfo previousCallLogInfo = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			CallLogBean callLogInfo = list.get(i);
			// boolean oneDay = true;
			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "yyyy-MM-dd HH:mm:ss" );
			// try {
			// Date date1 = sdf.parse(previousCallLogInfo.getLogtime());
			// Date date2 = sdf.parse(previousCallLogInfo.getLogtime());
			// if( date1.getYear() != date2.getYear() || date1.getMonth() !=
			// date2.getMonth() || date1.getDate() == date2.getDate()){
			// oneDay = false;
			// }
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// if(callLogInfo.getCallnum().equalsIgnoreCase(previousCallLogInfo.getCallnum())
			// && callLogInfo.getType() == previousCallLogInfo.getType() &&
			// oneDay){
			//
			// callLogInfo.setCount(previousCallLogInfo.getCount() + 1);
			// }else{
			// DBManager.saveCallLog(getActivity(), previousCallLogInfo);
			// callList.add(previousCallLogInfo);
			// }
			// if(i == list.size() - 1){
			// DBManager.saveCallLog(getActivity(), callLogInfo);
			// callList.add(callLogInfo);
			// }else{
			// previousCallLogInfo = callLogInfo;
			// }
			callLogInfo.setCallnum(Tool.GetNumber(callLogInfo.getCallnum()));
			callLogInfo.setCallname(Tool.GetNumber(callLogInfo.getCallname()));
			if (null == callLogInfo.getCallname()
					|| "".equals(callLogInfo.getCallname())) {
				callLogInfo.setCallname(callLogInfo.getCallnum());
			}
			// callLogInfo.setDate(sfd.format(date));

			DBManager.saveCallLog(act, callLogInfo);
			callLogs.add(callLogInfo);
		}
		adapter.notifyDataSetChanged();
	}

	private void getUserCallLogInfoFromDB() {
		// TODO Auto-generated method stub
		List<CallLogBean> callLogInfos = DBManager.getCallLogs(act);
		if (callLogInfos != null && callLogInfos.size() > 0) {
			callLogs.clear();
			callLogs.addAll(callLogInfos);
			adapter.notifyDataSetChanged();
		}
	}

	public void reflesh() {
		// TODO Auto-generated method stub
		// getUserCallLogs();
	}

	public void callPhone(final CallLogBean callLog) {
		// TODO Auto-generated method stub
//		ShowAlertDialog("是否拨号", callLog.getCallnum(), new MyDialogInterface() {
//
//			@Override
//			public void onClick(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		String huafei = SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "huafei", "");
		if (TextUtils.isEmpty(huafei)
				|| Float.parseFloat(huafei) <= 0.3) {
			ShowAlertDialog("余额不足", "请兑换话费或参加活动",
					new MyDialogInterface() {

						@Override
						public void onClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		} else {
			// 无网络拨打系统电话
			if (!NetUtils.isConnected(act)) {
				// 用intent启动拨打电话
				ShowAlertDialog("温馨提示", "暂无网络，将用本机呼叫",
						new MyDialogInterface() {

							@Override
							public void onClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Intent intent = new Intent(
										Intent.ACTION_CALL,
										Uri.parse("tel:"
												+ callLog.getCallnum()));
								startActivity(intent);
							}
						});
			} else {
				Intent intent = new Intent(act, DhbBhpCallActivity.class);
				intent.putExtra("phone", callLog.getCallnum());
				intent.putExtra("cname", callLog.getCallname());
				act.startActivity(intent);
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}
