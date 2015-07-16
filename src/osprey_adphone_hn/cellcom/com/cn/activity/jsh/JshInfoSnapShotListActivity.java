package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;
import android.widget.GridView;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshInfoSnapShotListAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.SnapInfo;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.thread.SnapShotCallBack;
import p2p.cellcom.com.cn.thread.SnapShotTask;

public class JshInfoSnapShotListActivity extends ActivityFrame{
	
	private GridView gridview;
	private JshInfoSnapShotListAdapter adapter;
	private List<SnapInfo> lists = new ArrayList<SnapInfo>();
	private SnapShotTask snapShotTask;
	private AlertDialog deleteDialog;
	private int selectedItem, screenWidth, screenHeight;
	
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
				createGalleryDialog(position);
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle("抓拍信息");
		initAdapter();
		snapShotTask = new SnapShotTask(new SnapShotCallBack() {
			
			@Override
			public void onSuccess(List<File> list) {
				// TODO Auto-generated method stub
				lists.clear();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					File file = (File) iterator.next();
					SnapInfo info = new SnapInfo();
					String deviceId = file.getName().substring(0, file.getName().indexOf("_"));
					Device device = FList.getInstance().isDevice(deviceId);
					String date = file.getName().substring(file.getName().indexOf("_") + 1, file.getName().indexOf(".jpg"));
					if(device != null){
						info.setName(device.getDeviceName());
					}else{
						info.setName(deviceId);
					}
					info.setDate(date);
					info.setPath(file.getPath());
					lists.add(info);
				}
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
		});
		snapShotTask.execute();
	}
	
	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshInfoSnapShotListAdapter(this, lists);
		gridview.setAdapter(adapter);
	}
	
	public void createGalleryDialog(final int position) {
		View view = LayoutInflater.from(this).inflate(R.layout.os_jsh_info_snapshot_dialog_gallery, null);	
		final ImageSwitcher switcher = (ImageSwitcher) view.findViewById(R.id.img_container);		
		switcher.setFactory(new ViewFactory(){

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				ImageView view = new ImageView(getApplicationContext());
				view.setScaleType(ScaleType.FIT_CENTER);
				view.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
				LogMgr.showLog(Runtime.getRuntime().totalMemory()+"-------------0");
				return view;
			}
			
		});	
		final GestureDetector gd = new GestureDetector(this, new OnGestureListener(){

			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				float x1 = arg0.getRawX();
				float x2 = arg1.getRawX();
				float distance = x1 - x2;
				if((distance>0)&&(Math.abs(distance)>30)){
					if(++selectedItem < lists.size()){
						switcher.setInAnimation(AnimationUtils.loadAnimation(JshInfoSnapShotListActivity.this, R.anim.slide_in_right_100));
						switcher.setOutAnimation(AnimationUtils.loadAnimation(JshInfoSnapShotListActivity.this, R.anim.slide_out_left_100));
						switcher.setImageDrawable(new BitmapDrawable(getResources(), lists.get(selectedItem).getPath()));
					}else{
						selectedItem = lists.size() - 1;
					}
					
					LogMgr.showLog(Runtime.getRuntime().totalMemory()+"---------1");
				}else if((distance<0)&&(Math.abs(distance)>30)){
					if(--selectedItem>=0){
						switcher.setInAnimation(AnimationUtils.loadAnimation(JshInfoSnapShotListActivity.this, R.anim.slide_in_left_100));
						switcher.setOutAnimation(AnimationUtils.loadAnimation(JshInfoSnapShotListActivity.this, R.anim.slide_out_right_100));
						switcher.setImageDrawable(new BitmapDrawable(getResources(), lists.get(selectedItem).getPath()));
					}else{
						selectedItem = 0;
					}
					
					LogMgr.showLog(Runtime.getRuntime().totalMemory()+"------2");
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		switcher.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				gd.onTouchEvent(arg1);
				return true;
			}
			
		});
		selectedItem = position;
		switcher.setImageDrawable(new BitmapDrawable(getResources(), lists.get(selectedItem).getPath()));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		deleteDialog = builder.create();
		deleteDialog.show();
		deleteDialog.setContentView(view);
		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
		params.width = ContextUtil.getWidth(this);
		params.height = ContextUtil.getHeith(this);
		view.setLayoutParams(params);
	}

}
