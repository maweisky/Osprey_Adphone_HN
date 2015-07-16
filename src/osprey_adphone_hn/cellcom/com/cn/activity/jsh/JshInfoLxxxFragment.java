package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.util.ArrayList;

import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshInfoLxListAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.RecordInfo;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class JshInfoLxxxFragment extends FragmentBase{
	private JshInfoZpjFragmentActivity jshInfoZpjFragmentActivity;
	private GridView gridview;
	private JshInfoLxListAdapter adapter;
	private ArrayList<RecordInfo> lists = new ArrayList<RecordInfo>();
	private FinalDb finalDb;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		jshInfoZpjFragmentActivity=(JshInfoZpjFragmentActivity)activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.os_jsh_info_snapshot, container,	false);
		initView(v);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		initData();
	}

	private void initView(View v) {
		// TODO Auto-generated method stub
		gridview = (GridView) v.findViewById(R.id.gridview);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(lists.get(position).getPath())){
					File file=new File(lists.get(position).getPath());
					if(file.exists()){
						Intent intent = new Intent(Intent.ACTION_VIEW);
		                intent.setDataAndType(Uri.parse(lists.get(position).getPath()), "video/mp4");
		                startActivity(intent);
					}else{
						Toast.makeText(jshInfoZpjFragmentActivity, "录像文件不存在", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(jshInfoZpjFragmentActivity, "录像文件不存在", Toast.LENGTH_SHORT).show();
				}
			}
		});
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				ShowAlertDialog("删除", "确定要删除吗?", new MyDialogInterface() {

					@Override
					public void onClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						File f = new File(lists.get(position).getPath());
						try {
							f.delete();
							finalDb.deleteByWhere(RecordInfo.class, " id="+lists.get(position).getId());
							updateData();
						} catch (Exception e) {
						}
					}
				});
				return false;
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		finalDb=FinalDb.create(jshInfoZpjFragmentActivity);
		updateData();
		initAdapter();
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshInfoLxListAdapter(jshInfoZpjFragmentActivity, lists);
		gridview.setAdapter(adapter);
	}

	private void updateData() {
		// TODO Auto-generated method stub
		lists=(ArrayList<RecordInfo>)finalDb.findAll(RecordInfo.class);
		if(adapter!=null){		  
		  adapter.setList(lists);
		}
	}
}
