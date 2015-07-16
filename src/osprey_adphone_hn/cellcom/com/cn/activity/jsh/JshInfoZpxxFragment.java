package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.adapter.JshInfoSnapShotListAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.SnapInfo;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.FList;
import p2p.cellcom.com.cn.thread.SnapShotCallBack;
import p2p.cellcom.com.cn.thread.SnapShotTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

public class JshInfoZpxxFragment extends FragmentBase {
	private JshInfoZpjFragmentActivity jshInfoZpjFragmentActivity;
	private GridView gridview;
	private JshInfoSnapShotListAdapter adapter;
	private ArrayList<SnapInfo> lists = new ArrayList<SnapInfo>();
	private SnapShotTask snapShotTask;
	private AlertDialog deleteDialog;
	private int selectedItem;
	
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
				Intent intent=new Intent(jshInfoZpjFragmentActivity,JshInfoZpxxGallery.class);
				intent.putExtra("position", position);
				intent.putExtra("value", lists);
				startActivity(intent);
//				createGalleryDialog(position);
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
		initAdapter();
		updateData();
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		adapter = new JshInfoSnapShotListAdapter(getActivity(), lists);
		gridview.setAdapter(adapter);
	}

	public void createGalleryDialog(final int position) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.os_jsh_info_snapshot_dialog_gallery, null);
		LinearLayout ll_back = (LinearLayout) view.findViewById(R.id.llAppBack);
		LinearLayout ll_set = (LinearLayout) view.findViewById(R.id.llAppSet);
		MarqueeText tv_title = (MarqueeText) view.findViewById(R.id.tvTopTitle);
		tv_title.setText(R.string.os_jsh_zpj);
		ll_set.setVisibility(View.GONE);
		ll_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteDialog.dismiss();
			}
		});
		final ImageSwitcher switcher = (ImageSwitcher) view.findViewById(R.id.img_container);
		switcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				ImageView view = new ImageView(getActivity());
				view.setScaleType(ScaleType.FIT_CENTER);
				view.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				LogMgr.showLog(Runtime.getRuntime().totalMemory() + "-------------0");
				return view;
			}

		});
		final GestureDetector gd = new GestureDetector(getActivity(),
				new OnGestureListener() {

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
						if ((distance > 0) && (Math.abs(distance) > 30)) {
							if (++selectedItem < lists.size()) {
								switcher.setInAnimation(AnimationUtils
										.loadAnimation(getActivity(),
												R.anim.slide_in_right_100));
								switcher.setOutAnimation(AnimationUtils
										.loadAnimation(getActivity(),
												R.anim.slide_out_left_100));
								switcher.setImageDrawable(new BitmapDrawable(
										getResources(), lists.get(selectedItem)
												.getPath()));
							} else {
								selectedItem = lists.size() - 1;
							}

							LogMgr.showLog(Runtime.getRuntime().totalMemory()
									+ "---------1");
						} else if ((distance < 0) && (Math.abs(distance) > 30)) {
							if (--selectedItem >= 0) {
								switcher.setInAnimation(AnimationUtils
										.loadAnimation(getActivity(),
												R.anim.slide_in_left_100));
								switcher.setOutAnimation(AnimationUtils
										.loadAnimation(getActivity(),
												R.anim.slide_out_right_100));
								switcher.setImageDrawable(new BitmapDrawable(
										getResources(), lists.get(selectedItem)
												.getPath()));
							} else {
								selectedItem = 0;
							}

							LogMgr.showLog(Runtime.getRuntime().totalMemory()
									+ "------2");
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
		switcher.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				gd.onTouchEvent(arg1);
				return true;
			}

		});
		selectedItem = position;
		switcher.setImageDrawable(new BitmapDrawable(getResources(), lists.get(
				selectedItem).getPath()));
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		deleteDialog = builder.create();
		deleteDialog.show();
		deleteDialog.setContentView(view);
		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
		params.width = ContextUtil.getWidth(getActivity());
		params.height = ContextUtil.getHeith(getActivity());
		view.setLayoutParams(params);
	}


	private void updateData() {
		// TODO Auto-generated method stub
		snapShotTask = new SnapShotTask(new SnapShotCallBack() {

			@Override
			public void onSuccess(List<File> list) {
				// TODO Auto-generated method stub
				lists.clear();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					File file = (File) iterator.next();
					SnapInfo info = new SnapInfo();
					String deviceId = file.getName().substring(0,
							file.getName().indexOf("_"));
					Device device = FList.getInstance().isDevice(deviceId);
					String date = file.getName().substring(
							file.getName().indexOf("_") + 1,
							file.getName().indexOf(".jpg"));
					if (device != null) {
						info.setName(device.getDeviceName());
					} else {
						info.setName(deviceId);
					}
					info.setDate(date);
					info.setPath(file.getPath());
					lists.add(info);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}
		});
		snapShotTask.execute();
	}
}
