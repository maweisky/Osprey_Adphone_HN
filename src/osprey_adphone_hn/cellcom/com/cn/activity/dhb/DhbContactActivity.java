package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afinal.simplecache.NetUtils;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.adapter.ContactListAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.CallLogBean;
import osprey_adphone_hn.cellcom.com.cn.bean.ContactBean;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.util.Tool;
import osprey_adphone_hn.cellcom.com.cn.widget.QuickAlphabeticBar;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * 通讯录
 * 
 * @author ma
 * 
 */
public class DhbContactActivity extends FragmentBase {
	private DhbFragmentActivity act;
	private ContactListAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list = new ArrayList<ContactBean>();
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private QuickAlphabeticBar alphabeticBar; // 快速索引条
	private EditText querycontacet;
	private ImageButton queryCityExit;
	private Filter mFilter;
	private Map<Integer, ContactBean> contactIdMap = null;
	private boolean addData = false;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (DhbFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_dhb_contact_activity, container,
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
		contactList = (ListView) v.findViewById(R.id.contact_list);
		alphabeticBar = (QuickAlphabeticBar) v.findViewById(R.id.fast_scroller);
		querycontacet = (EditText) v.findViewById(R.id.querycontacet);
		queryCityExit = (ImageButton) v.findViewById(R.id.queryCityExit);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Intent intentcontact = new Intent(Intent.ACTION_INSERT);
					intentcontact.setType("vnd.android.cursor.dir/person");
					intentcontact.setType("vnd.android.cursor.dir/contact");
					intentcontact.setType("vnd.android.cursor.dir/raw_contact");
					startActivity(intentcontact);
					addData = true;
				} else {
					String huafei=SharepreferenceUtil.readString(act,
							SharepreferenceUtil.fileName, "huafei", "");
					if(TextUtils.isEmpty(huafei)||Float.parseFloat(huafei)<=0.3){
						ShowAlertDialog("余额不足", "请兑换话费或参加活动",
								new MyDialogInterface() {

									@Override
									public void onClick(DialogInterface dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}else{
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
							final Intent intent = new Intent(act, DhbBhpCallActivity.class);
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
										.getItemAtPosition(position)).getDesplayName());
//								ShowAlertDialog("是否拨号", ((ContactBean) parent
//										.getItemAtPosition(position)).getPhoneNum(), new MyDialogInterface() {
//									
//									@Override
//									public void onClick(DialogInterface dialog) {
//										// TODO Auto-generated method stub
//										startActivity(intent);
//									}
//								});
								startActivity(intent);
							}
						}
						
					}
				}
			}
		});
		querycontacet.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(s)) {
					queryCityExit.setVisibility(View.GONE);
				} else {
					queryCityExit.setVisibility(View.VISIBLE);
				}
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
	}

	public boolean enoughToFilter() {
		return querycontacet.getText().length() > 0;
	}

	private void doAfterTextChanged() {
		if (enoughToFilter()) {
			if (mFilter != null) {
				mFilter.filter(querycontacet.getText().toString().trim());
			}
		} else {
			if (mFilter != null) {
				mFilter.filter(null);
			}
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		View view = LayoutInflater.from(act).inflate(
				R.layout.os_dhb_contactadditem, null);
		contactList.addHeaderView(view);
		// 实例化
		asyncQueryHandler = new MyAsyncQueryHandler(act.getContentResolver());
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	private void refleshData() {
		// TODO Auto-generated method stub
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
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
					adapter = new ContactListAdapter(act, list, alphabeticBar);
					contactList.setAdapter(adapter);
					adapter.setList(list);
					alphabeticBar.init(act);
					alphabeticBar.setListView(contactList);
					alphabeticBar.setHight(alphabeticBar.getHeight());
					alphabeticBar.setVisibility(View.VISIBLE);
					mFilter = adapter.getFilter();
				}
			}

			super.onQueryComplete(token, cookie, cursor);
		}
	}

	public void reflesh() {
		// TODO Auto-generated method stub
		if (addData) {
			refleshData();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}