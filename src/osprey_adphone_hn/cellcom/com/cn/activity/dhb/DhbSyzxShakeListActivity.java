package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.SyzxYyyAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxYyyList;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxYyyListComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class DhbSyzxShakeListActivity extends ActivityFrame {
	private EditText et_keyword;
	private FButton btn_search;
	private JazzyListView mJazzyListView;
	private SyzxYyyAdapter adapter;
	private List<SyzxYyyList> syzxYyyList = new ArrayList<SyzxYyyList>();
	private List<SyzxYyyList> searchSyzxYyyList = new ArrayList<SyzxYyyList>();
	private TextView tv_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_syzx_snake_list_activity);
		AppendTitleBody1();
		SetTopBarTitle(getResources().getString(
				R.string.os_dhb_syzx_yyy_indicator));
		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		et_keyword = (EditText) findViewById(R.id.et_keyword);
		btn_search = (FButton) findViewById(R.id.btn_search);
		mJazzyListView = (JazzyListView) findViewById(R.id.listview);
		mJazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new SyzxYyyAdapter(this, syzxYyyList);
		mJazzyListView.setAdapter(adapter);
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
					if (syzxYyyList.size() > 0) {
						adapter.notifyDataSetChanged();
					} else {
						tv_empty.setVisibility(View.VISIBLE);
						mJazzyListView.setVisibility(View.GONE);
					}
				} else {
					searchSyzxYyyList.clear();
					for (SyzxYyyList syzxYyyListbean : syzxYyyList) {
						if (syzxYyyListbean.getTitle().contains(
								et_keyword.getText().toString())) {
							searchSyzxYyyList.add(syzxYyyListbean);
						}
					}
					if (searchSyzxYyyList.size() > 0) {
						adapter.setInfos(searchSyzxYyyList);
						adapter.notifyDataSetChanged();
					} else {
						tv_empty.setVisibility(View.VISIBLE);
						mJazzyListView.setVisibility(View.GONE);
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

		mJazzyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ShowProgressDialog(R.string.hsc_progress);
		syzxYyyList.clear();
		tv_empty.setVisibility(View.GONE);
		mJazzyListView.setVisibility(View.VISIBLE);
		getYyyggInfos();
	}

	/**
	 * 获取摇一摇广告列表数据
	 */
	private void getYyyggInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxShakeListActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		HttpHelper.getInstances(DhbSyzxShakeListActivity.this).send(
				FlowConsts.YYW_GETYAOGGLIST, cellComAjaxParams,
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
						mJazzyListView.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						SyzxYyyListComm syzxYyyListComm = arg0.read(
								SyzxYyyListComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = syzxYyyListComm.getReturnCode();
						String msg = syzxYyyListComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							tv_empty.setVisibility(View.VISIBLE);
							mJazzyListView.setVisibility(View.GONE);
							return;
						}
						try {
							tv_empty.setVisibility(View.GONE);
							mJazzyListView.setVisibility(View.VISIBLE);
							syzxYyyList.addAll(syzxYyyListComm.getBody());
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取下一个广告 ggid 广告ID
	 */
	public void getNextAd(final String ggid, final String largepic) {
		if (SharepreferenceUtil.readString(DhbSyzxShakeListActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxShakeListActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxShakeListActivity.this, SharepreferenceUtil.fileName,
				"uid", ""));
		cellComAjaxParams.put("ggid", ggid);
		cellComAjaxParams.put("next", "0");
		cellComAjaxParams.put("level", "1");
		HttpHelper.getInstances(DhbSyzxShakeListActivity.this).send(
				FlowConsts.YYW_GETLOOKGGINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						KykAdComm kykAdComm = cellComAjaxResult.read(
								KykAdComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(kykAdComm
								.getReturnCode())) {
							Toast.makeText(DhbSyzxShakeListActivity.this,
									kykAdComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}

						KykAdResult kykadresult = kykAdComm.getBody();
						Intent intent = new Intent(
								DhbSyzxShakeListActivity.this,
								YyyAdShowActivity.class);
						intent.putExtra("kykadresult", kykadresult);
						intent.putExtra("currentGgid", ggid);
						intent.putExtra("largepic", largepic);
						startActivity(intent);

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
					}
				});
	}

}
