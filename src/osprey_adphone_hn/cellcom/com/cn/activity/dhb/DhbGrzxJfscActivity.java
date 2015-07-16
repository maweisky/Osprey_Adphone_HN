package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxJfscGridAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxJfscListAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.JfscSpTypeAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.SpinerItemAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpList;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpListComm;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpType;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpTypeComm;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxYyyList;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxYyyListComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyGridView;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.AddAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.ItemClick;

public class DhbGrzxJfscActivity extends ActivityFrame {
	private EditText et_keyword;
	private FButton btn_search;

	// 商品分类
	private LinearLayout ll_sp_type;
	private TextView tv_sp_type;
	// 积分范围
	private LinearLayout ll_jf_type;
	private TextView tv_jf_type;
	//综合排序
	private LinearLayout ll_zh_sort;
	private TextView tv_zh_sort;

	private ImageView iv_show_mode;

	private JazzyListView mJazzyListView;
	private JazzyGridView mJazzyGridView;
	private GrzxJfscListAdapter listviewadapter;
	private GrzxJfscGridAdapter gridviewadapter;
	private List<JfscSpList> jfscSpList = new ArrayList<JfscSpList>();
	private List<JfscSpList> searchJfscSpList = new ArrayList<JfscSpList>();
	private TextView tv_empty;

	private ListViewPopupWindow sptype_popup;
	private List<JfscSpType> jfscSpTypeList = new ArrayList<JfscSpType>();
	private JfscSpTypeAdapter sptype_adapter;
	private ListViewPopupWindow jffw_popup;
	private String[] jffwstr = { "全部范围", "1000以下", "1000-5000", "5000-10000",
			"10000以上" };
	private ListViewPopupWindow zhsort_popup;
	private String[] sortstr = { "综合排序", "积分从高到低", "积分从低到高"};

	private String currentListMode = "ListView";// 列表模式 ListView/GridView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_grzx_jfsc_activity);
		AppendTitleBody1();
		HideSet();
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_dhb_grzx_jfsc));
		initView();
		initListener();
		initData();

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Rect frame = new Rect();    
				
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
				
				int statusHeight = frame.top; 
				int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
				System.out.println("frame.top---"+contentTop+"-->"+statusHeight);
			}
		}, 500);
		
		
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		et_keyword = (EditText) findViewById(R.id.et_keyword);
		btn_search = (FButton) findViewById(R.id.btn_search);

		ll_sp_type = (LinearLayout) findViewById(R.id.ll_sp_type);
		tv_sp_type = (TextView) findViewById(R.id.tv_sp_type);
		tv_sp_type.setTag("0");
		ll_jf_type = (LinearLayout) findViewById(R.id.ll_jf_type);
		tv_jf_type = (TextView) findViewById(R.id.tv_jf_type);
		tv_jf_type.setTag("0");
		ll_zh_sort = (LinearLayout) findViewById(R.id.ll_zh_sort);
		tv_zh_sort = (TextView) findViewById(R.id.tv_zh_sort);
		tv_zh_sort.setTag("");
		iv_show_mode = (ImageView) findViewById(R.id.iv_show_mode);

		mJazzyListView = (JazzyListView) findViewById(R.id.listview);
		mJazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
		listviewadapter = new GrzxJfscListAdapter(this, jfscSpList);
		mJazzyListView.setAdapter(listviewadapter);

		mJazzyGridView = (JazzyGridView) findViewById(android.R.id.list);
		mJazzyGridView.setTransitionEffect(JazzyHelper.HELIX);
		gridviewadapter = new GrzxJfscGridAdapter(this, jfscSpList);
		mJazzyGridView.setAdapter(gridviewadapter);

		sptype_popup = new ListViewPopupWindow(this);
		sptype_popup.setAddAdapter(new AddAdapter() {

			@Override
			public void addAdapter(ListView listview) {
				// TODO Auto-generated method stub
				sptype_adapter = new JfscSpTypeAdapter(
						DhbGrzxJfscActivity.this, jfscSpTypeList);
				listview.setAdapter(sptype_adapter);
			}
		});

		jffw_popup = new ListViewPopupWindow(this);
		jffw_popup.setAddAdapter(new AddAdapter() {

			@Override
			public void addAdapter(ListView listview) {
				// TODO Auto-generated method stub
				listview.setAdapter(new SpinerItemAdapter(
						DhbGrzxJfscActivity.this, jffwstr));
			}
		});
		
		zhsort_popup = new ListViewPopupWindow(this);
		zhsort_popup.setAddAdapter(new AddAdapter() {

			@Override
			public void addAdapter(ListView listview) {
				// TODO Auto-generated method stub
				listview.setAdapter(new SpinerItemAdapter(
						DhbGrzxJfscActivity.this, sortstr));
			}
		});
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_keyword.getText().toString().equals("")) {
					if(currentListMode.equals("ListView")){
						if (jfscSpList.size() > 0) {
							listviewadapter.notifyDataSetChanged();
						} else {
							tv_empty.setVisibility(View.VISIBLE);
							mJazzyListView.setVisibility(View.GONE);
						}
					}else{
						if (jfscSpList.size() > 0) {
							gridviewadapter.notifyDataSetChanged();
						} else {
							tv_empty.setVisibility(View.VISIBLE);
							mJazzyGridView.setVisibility(View.GONE);
						}
					}
				} else {
					searchJfscSpList.clear();
					for (JfscSpList jfscSpListbean : jfscSpList) {
						if (jfscSpListbean.getTitle().contains(
								et_keyword.getText().toString())) {
							searchJfscSpList.add(jfscSpListbean);
						}
					}
					if(currentListMode.equals("ListView")){
						if (searchJfscSpList.size() > 0) {
							listviewadapter.setInfos(searchJfscSpList);
							listviewadapter.notifyDataSetChanged();
						} else {
							tv_empty.setVisibility(View.VISIBLE);
							mJazzyListView.setVisibility(View.GONE);
						}
					}else{
						if (searchJfscSpList.size() > 0) {
							gridviewadapter.setInfos(searchJfscSpList);
							gridviewadapter.notifyDataSetChanged();
						} else {
							tv_empty.setVisibility(View.VISIBLE);
							mJazzyGridView.setVisibility(View.GONE);
						}
					}
				}
			}
		});
		// 商品分类
		ll_sp_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sptype_popup.showAsDropDown(ll_sp_type, 0, 1,
						ll_sp_type.getWidth(), LayoutParams.WRAP_CONTENT);
			}
		});
		sptype_popup.setItemClick(new ItemClick() {

			@Override
			public void setOnItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				tv_sp_type.setText(jfscSpTypeList.get(position).getName());
				tv_sp_type.setTag(jfscSpTypeList.get(position).getVid());
				sptype_popup.dimissPopupwindow();
				getJfscInfos();
			}
		});

		// 积分范围
		ll_jf_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jffw_popup.showAsDropDown(ll_jf_type, 0, 1,
						ll_jf_type.getWidth(), LayoutParams.WRAP_CONTENT);
			}
		});
		jffw_popup.setItemClick(new ItemClick() {

			@Override
			public void setOnItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				tv_jf_type.setText(jffwstr[position]);
				tv_jf_type.setTag((position + ""));
				jffw_popup.dimissPopupwindow();
				getJfscInfos();
			}
		});
		
		//综合排序
		ll_zh_sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zhsort_popup.showAsDropDown(ll_zh_sort, 0, 1,
						ll_zh_sort.getWidth(), LayoutParams.WRAP_CONTENT);
			}
		});
		zhsort_popup.setItemClick(new ItemClick() {
			
			@Override
			public void setOnItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				tv_zh_sort.setText(sortstr[position]);
				if(position == 0){
					tv_zh_sort.setTag("");
				}else{
					tv_zh_sort.setTag((position + ""));
				}
				zhsort_popup.dimissPopupwindow();
				getJfscInfos();
			}
		});

		// 切换展示模式
		iv_show_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AnimationUtil.addScaleAnimation(iv_show_mode, 500);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (currentListMode.equals("ListView")) {
							mJazzyGridView.setVisibility(View.VISIBLE);
							mJazzyListView.setVisibility(View.GONE);
							iv_show_mode
									.setBackgroundResource(R.drawable.os_gridview_mode_icon);
							currentListMode = "GridView";
						} else {
							mJazzyGridView.setVisibility(View.GONE);
							mJazzyListView.setVisibility(View.VISIBLE);
							iv_show_mode
									.setBackgroundResource(R.drawable.os_listview_mode_icon);
							currentListMode = "ListView";
						}
					}
				}, 500);
			}
		});

		tv_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_keyword.setText("");
				getJfscInfos();
			}
		});

		mJazzyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DhbGrzxJfscActivity.this,
						DhbGrzxJfscDetailActivity.class);
				intent.putExtra("jfscSpListBean",
						(JfscSpList) parent.getItemAtPosition(position));
				startActivity(intent);
			}
		});

		mJazzyGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DhbGrzxJfscActivity.this,
						DhbGrzxJfscDetailActivity.class);
				intent.putExtra("jfscSpListBean",
						(JfscSpList) parent.getItemAtPosition(position));
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		getSptypeInfos();
		getJfscInfos();
	}

	/**
	 * 获取积分商城商品分类数据
	 */
	private void getSptypeInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbGrzxJfscActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		HttpHelper.getInstances(DhbGrzxJfscActivity.this).send(
				FlowConsts.YYW_SHOP_GETVARIETYLIST, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();

					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						JfscSpTypeComm JfscSpTypeComm = arg0.read(
								JfscSpTypeComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = JfscSpTypeComm.getReturnCode();
						String msg = JfscSpTypeComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						try {
							jfscSpTypeList.clear();
							jfscSpTypeList.addAll(JfscSpTypeComm.getBody());
							JfscSpType jfscSpType = new JfscSpType();
							jfscSpType.setName("全部分类");
							jfscSpType.setVid("0");
							jfscSpTypeList.add(0, jfscSpType);
							sptype_adapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取积分商城列表数据
	 */
	private void getJfscInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbGrzxJfscActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("vid", tv_sp_type.getTag().toString());
		cellComAjaxParams.put("jfscope", tv_jf_type.getTag().toString());
		cellComAjaxParams.put("jfsort", tv_zh_sort.getTag().toString());
		HttpHelper.getInstances(DhbGrzxJfscActivity.this).send(
				FlowConsts.YYW_SHANGPIN_LIST, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
						tv_empty.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						tv_empty.setVisibility(View.VISIBLE);
						if (currentListMode.equals("ListView")) {
							mJazzyListView.setVisibility(View.GONE);
						} else if (currentListMode.equals("GridView")) {
							mJazzyGridView.setVisibility(View.GONE);
						}
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						JfscSpListComm jfscSpListComm = arg0.read(
								JfscSpListComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = jfscSpListComm.getReturnCode();
						String msg = jfscSpListComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							tv_empty.setVisibility(View.VISIBLE);
							if (currentListMode.equals("ListView")) {
								mJazzyListView.setVisibility(View.GONE);
							} else if (currentListMode.equals("GridView")) {
								mJazzyGridView.setVisibility(View.GONE);
							}
							return;
						}
						try {
							jfscSpList.clear();
							jfscSpList.addAll(jfscSpListComm.getBody());
							listviewadapter.notifyDataSetChanged();
							gridviewadapter.notifyDataSetChanged();
							if (jfscSpList.size() > 0) {
								tv_empty.setVisibility(View.GONE);
								if (currentListMode.equals("ListView")) {
									mJazzyListView.setVisibility(View.VISIBLE);
								} else if (currentListMode.equals("GridView")) {
									mJazzyGridView.setVisibility(View.VISIBLE);
								}
							} else {
								tv_empty.setVisibility(View.VISIBLE);
								if (currentListMode.equals("ListView")) {
									mJazzyListView.setVisibility(View.GONE);
								} else if (currentListMode.equals("GridView")) {
									mJazzyGridView.setVisibility(View.GONE);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
