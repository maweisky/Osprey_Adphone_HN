package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.SyzxKykAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykNewList;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykNewListComm;
import osprey_adphone_hn.cellcom.com.cn.db.DBHelper;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.xlistview.XListView;
import osprey_adphone_hn.cellcom.com.cn.widget.xlistview.XListView.IXListViewListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 商业中心 看一看
 * @author Administrator
 *
 */
public class DhbSyzxKykActivity extends ActivityFrame implements
		IXListViewListener {
	private EditText et_keyword;
	private FButton btn_search;
	private XListView xListView;
	private SyzxKykAdapter adapter;
	private List<SyzxKykNewList> syzxKykList = new ArrayList<SyzxKykNewList>();
	private List<SyzxKykNewList> searchSyzxKykList = new ArrayList<SyzxKykNewList>();
	private TextView tv_empty;

	private String typeid = "";
	private int page = 1;// 第几页
	private String totalNumber = "totalNumber";// 数据总条数

	FinalDb finalDb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_syzx_kyk_activity);
		AppendTitleBody1();
		finalDb = DBHelper.getIntance(this);
		receiveIntentData();
		initView();
		initListener();
		initData();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 接收Intent 数据
	 */
	private void receiveIntentData() {
		if (getIntent().getStringExtra("title") != null) {
			SetTopBarTitle(getIntent().getStringExtra("title"));
		}
		if (getIntent().getStringExtra("typeid") != null) {
			typeid = getIntent().getStringExtra("typeid");
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		et_keyword = (EditText) findViewById(R.id.et_keyword);
		btn_search = (FButton) findViewById(R.id.btn_search);
		xListView = (XListView) findViewById(R.id.listview);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		adapter = new SyzxKykAdapter(this, syzxKykList);
		xListView.setAdapter(adapter);
		tv_empty = (TextView) findViewById(R.id.tv_empty);
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {

		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_keyword.getText().toString().equals("")) {
					if (syzxKykList.size() > 0) {
						adapter.notifyDataSetChanged();
					} else {
						tv_empty.setVisibility(View.VISIBLE);
						xListView.setVisibility(View.GONE);
					}
				} else {
					searchSyzxKykList.clear();
					for (SyzxKykNewList syzxKykListbean : syzxKykList) {
						if (syzxKykListbean.getTitle().contains(
								et_keyword.getText().toString())) {
							searchSyzxKykList.add(syzxKykListbean);
						}
					}
					if (searchSyzxKykList.size() > 0) {
						adapter.setInfos(searchSyzxKykList);
						adapter.notifyDataSetChanged();
					} else {
						tv_empty.setVisibility(View.VISIBLE);
						xListView.setVisibility(View.GONE);
					}
				}
			}
		});

		tv_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_keyword.setText("");
				initData();
			}
		});

		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				updateKykinfoTable((SyzxKykNewList) parent.getItemAtPosition(position));
				Intent intent = new Intent(DhbSyzxKykActivity.this,
						DhbSyzxKykDetailActivity.class);
				intent.putExtra("typeid", typeid);
				intent.putExtra("syzxKykListBean",
						(Serializable) parent.getItemAtPosition(position));
				startActivityForResult(intent, 1);
			}
		});
	}
	
	/**
	 * 保存/更新本地数据库中 看一看已读和未读的标识
	 */
	private void updateKykinfoTable(SyzxKykNewList syzxKykList){
		if(finalDb.findAllByWhere(SyzxKykNewList.class, "ggid = '"+syzxKykList.getGgid()+"'").size()==1){
			SyzxKykNewList syzxKykListbean  = new SyzxKykNewList();
			syzxKykListbean.setIfnew("Y");
			finalDb.update(syzxKykListbean, "ggid = "+syzxKykList.getGgid());
		}else{
			SyzxKykNewList syzxKykListbean  = new SyzxKykNewList();
			syzxKykListbean.setGgid(syzxKykList.getGgid());
			syzxKykListbean.setIfnew("Y");
			finalDb.save(syzxKykListbean);
		}
		syzxKykList.setIfnew("Y");
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ShowProgressDialog(R.string.hsc_progress);
		syzxKykList.clear();
		tv_empty.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		getKykInfos();
	}

	/**
	 * 获取看一看列表数据
	 */
	private void getKykInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxKykActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("verytype", typeid);
		cellComAjaxParams.put("pageid", page + "");
		HttpHelper.getInstances(DhbSyzxKykActivity.this).send(
				FlowConsts.YYW_GETLOOKGGLIST_NEW, cellComAjaxParams,
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
						tv_empty.setVisibility(View.VISIBLE);
						xListView.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						SyzxKykNewListComm syzxKykListComm = arg0.read(
								SyzxKykNewListComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = syzxKykListComm.getReturnCode();
						String msg = syzxKykListComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							tv_empty.setVisibility(View.VISIBLE);
							xListView.setVisibility(View.GONE);
							return;
						}
						try {
							tv_empty.setVisibility(View.GONE);
							xListView.setVisibility(View.VISIBLE);
							syzxKykList.addAll(0,syzxKykListComm.getBody()
									.getData());
							totalNumber = syzxKykListComm.getBody()
									.getTotalNum();
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (totalNumber != null
						&& totalNumber.equals((syzxKykList.size() + ""))) {
					ShowMsg("数据已加载完");
					onLoad();
				} else {
					page = page + 1;
					getKykInfos();
					onLoad();
				}
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == 1){
				if(data!=null){
					typeid = data.getStringExtra("typeid");
					SetTopBarTitle(data.getStringExtra("title"));
					initData();
				}
			}
		}
	}
}
