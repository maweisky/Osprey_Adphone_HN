package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshInfoRecordVideoListAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshInfoSnapShotListAdapter;
import p2p.cellcom.com.cn.bean.RecordVideoDB;
import p2p.cellcom.com.cn.db.DBManager;
import p2p.cellcom.com.cn.thread.SnapShotCallBack;
import p2p.cellcom.com.cn.thread.SnapShotTask;

public class JshInfoRecordVideoListActivity extends ActivityFrame{
	
	private GridView gridview;
	private JshInfoRecordVideoListAdapter adapter;
	private List<RecordVideoDB> lists = new ArrayList<RecordVideoDB>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_info_snapshot);
		AppendTitleBody1();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		gridview = (GridView) findViewById(R.id.gridview);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ShowMsg("暂不支持播放本地录像！");
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle("录像信息");
		initAdapter();
		getRecordVideos();
	}
	
	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshInfoRecordVideoListAdapter(this, lists);
		gridview.setAdapter(adapter);
	}
	
	private void getRecordVideos() {
		// TODO Auto-generated method stub
		List<RecordVideoDB> list = DBManager.getRecordVideos(this);
		lists.clear();
		if(list != null && list.size() > 0){
			lists.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}

}
