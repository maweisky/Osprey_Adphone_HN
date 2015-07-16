package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshWgInfoAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.JshWgInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.JshWgInfoComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class JshWgInfoActivity extends ActivityFrame{
	private JazzyListView listview;
	private JshWgInfoAdapter adapter;
	private List<JshWgInfo> wginfolist = new ArrayList<JshWgInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wg_info_activity);
		AppendTitleBody1();
		SetTopBarTitle("物管信息");
		initView();
		initListener();
		getWgInfo();
	}
	
	/**
	 * 初始化视图
	 */
	private void initView(){
		listview = (JazzyListView)findViewById(R.id.listview);
		listview.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new JshWgInfoAdapter(this, wginfolist);
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
	
	/**
	 * 获取物管信息
	 */
	private void getWgInfo() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(JshWgInfoActivity.this,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("type", "");
		cellComAjaxParams.put("gid", "1");
		HttpHelper.getInstances(JshWgInfoActivity.this).send(FlowConsts.YYW_GETWGINFO,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						JshWgInfoComm jshWgInfoComm = cellComAjaxResult
								.read(JshWgInfoComm.class,
										CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(jshWgInfoComm
								.getReturnCode())) {
							Toast.makeText(JshWgInfoActivity.this,
									jshWgInfoComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						wginfolist.addAll(jshWgInfoComm.getBody());
						adapter.notifyDataSetChanged();
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
