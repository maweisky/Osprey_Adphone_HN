package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbGrzxJfscDetailActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.GwcDfspAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpList;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpListComm;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class DfspFragment extends Fragment {
	private ActivityFrame act;
	private TextView tv_empty;
	private JazzyListView mJazzyListView;
	private LinearLayout ll_loading;
	private ImageView loadingImageView;
	private AnimationDrawable anima;
	private List<JfscSpList> jfscSpList = new ArrayList<JfscSpList>();
	private GwcDfspAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(activity instanceof GwcActivity){				
			act = (GwcActivity) activity;
		}else if(activity instanceof WdddActivity){
			act = (WdddActivity) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.os_dfsp_fragment, container,
				false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		getGwcDfspInfos();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 初始化视图
	 */
	private void initView(View v) {
		ll_loading = (LinearLayout) v.findViewById(R.id.ll_loading);
		loadingImageView = (ImageView) v.findViewById(R.id.loadingImageView);
		anima = (AnimationDrawable) loadingImageView.getBackground();
		tv_empty = (TextView) v.findViewById(R.id.tv_empty);
		mJazzyListView = (JazzyListView) v.findViewById(R.id.listview);
		mJazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new GwcDfspAdapter(act, jfscSpList);
		mJazzyListView.setAdapter(adapter);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		tv_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getGwcDfspInfos();
			}
		});

		mJazzyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act, DhbGrzxJfscDetailActivity.class);
				intent.putExtra("jfscSpListBean",
						(JfscSpList) parent.getItemAtPosition(position));
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取购物车待付商品列表数据
	 */
	public void getGwcDfspInfos(){
		if(SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName, "uid", "").equals("")){
			Intent loginintent = new Intent(act,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("state", "1");
		HttpHelper.getInstances(act).send(FlowConsts.YYW_SHANGPIN_LOOK,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				tv_empty.setVisibility(View.GONE);
				ll_loading.setVisibility(View.VISIBLE);
				if (anima != null) {
					anima.start();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				ll_loading.setVisibility(View.GONE);
				tv_empty.setVisibility(View.VISIBLE);
				mJazzyListView.setVisibility(View.GONE);
				if (anima != null) {
					anima.stop();
				}
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				// TODO Auto-generated method stub
				ll_loading.setVisibility(View.GONE);
				if (anima != null) {
					anima.stop();
				}
				JfscSpListComm jfscSpListComm = arg0.read(
						JfscSpListComm.class,
						CellComAjaxResult.ParseType.GSON);
				String state = jfscSpListComm.getReturnCode();
				String msg = jfscSpListComm.getReturnMessage();
				if (!FlowConsts.STATUE_1.equals(state)) {
					Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
					tv_empty.setVisibility(View.VISIBLE);
					mJazzyListView.setVisibility(View.GONE);
					return;
				}
				try {
					jfscSpList.clear();
					jfscSpList.addAll(jfscSpListComm.getBody());
					adapter.notifyDataSetChanged();
					if (jfscSpList.size() > 0) {
						tv_empty.setVisibility(View.GONE);
						mJazzyListView.setVisibility(View.VISIBLE);
					} else {
						tv_empty.setVisibility(View.VISIBLE);
						mJazzyListView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 修改购物车待付商品数量
	 */
	public void updateGwcDfspNum(String cid, String num) {
		if (SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(act, LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("cid", cid);
		cellComAjaxParams.put("num", num);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_SHANGPIN_UPDATE,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				tv_empty.setVisibility(View.GONE);
				ll_loading.setVisibility(View.VISIBLE);
				if (anima != null) {
					anima.start();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				ll_loading.setVisibility(View.GONE);
				tv_empty.setVisibility(View.VISIBLE);
				if (anima != null) {
					anima.stop();
				}
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				// TODO Auto-generated method stub
				ll_loading.setVisibility(View.GONE);
				if (anima != null) {
					anima.stop();
				}
				JfscSpListComm jfscSpListComm = arg0.read(
						JfscSpListComm.class,
						CellComAjaxResult.ParseType.GSON);
				String state = jfscSpListComm.getReturnCode();
				String msg = jfscSpListComm.getReturnMessage();
				if (!FlowConsts.STATUE_1.equals(state)) {
					Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
					tv_empty.setVisibility(View.VISIBLE);
					return;
				}
				try {
					getGwcDfspInfos();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 删除购物车待付商品数据
	 */
	public void delGwcDfspInfos(String cid) {
		if (SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(act, LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("cid", cid);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_SHANGPIN_DEL,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				tv_empty.setVisibility(View.GONE);
				ll_loading.setVisibility(View.VISIBLE);
				if (anima != null) {
					anima.start();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				ll_loading.setVisibility(View.GONE);
				tv_empty.setVisibility(View.VISIBLE);
				if (anima != null) {
					anima.stop();
				}
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				// TODO Auto-generated method stub
				ll_loading.setVisibility(View.GONE);
				if (anima != null) {
					anima.stop();
				}
				JfscSpListComm jfscSpListComm = arg0.read(
						JfscSpListComm.class,
						CellComAjaxResult.ParseType.GSON);
				String state = jfscSpListComm.getReturnCode();
				String msg = jfscSpListComm.getReturnMessage();
				if (!FlowConsts.STATUE_1.equals(state)) {
					Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
					tv_empty.setVisibility(View.VISIBLE);
					return;
				}
				try {
					getGwcDfspInfos();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 购物车待付商品支付
	 */
	public void gwcDfspPay(String cid,final String num){
		if(SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName, "uid", "").equals("")){
			Intent loginintent = new Intent(act,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("cid", cid);
		cellComAjaxParams.put("num", num);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_SHANGPIN_ORDER,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				ll_loading.setVisibility(View.VISIBLE);
				if(anima!=null){
					anima.start();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				ll_loading.setVisibility(View.GONE);
				if(anima!=null){
					anima.stop();
				}
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				// TODO Auto-generated method stub
				ll_loading.setVisibility(View.GONE);
				if(anima!=null){
					anima.stop();
				}
				JfscSpListComm jfscSpListComm = arg0.read(JfscSpListComm.class,
						CellComAjaxResult.ParseType.GSON);
				String state = jfscSpListComm.getReturnCode();
				String msg = jfscSpListComm.getReturnMessage();
				if (!FlowConsts.STATUE_1.equals(state)) {
					Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					//支付后刷新本地积分
					SharepreferenceUtil.write(
							act,
							SharepreferenceUtil.fileName,
							"jifen",
							(Float.parseFloat(SharepreferenceUtil.readString(
									act, SharepreferenceUtil.fileName,
									"jifen")) - Float.parseFloat(num))
									+ "");
					getGwcDfspInfos();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 获取个人资料
	public void getGrzl(final int selectId) {
		String uid = SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(act).send(
				FlowConsts.YYW_USERINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ll_loading.setVisibility(View.VISIBLE);
						if(anima!=null){
							anima.start();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						ll_loading.setVisibility(View.GONE);
						if(anima!=null){
							anima.stop();
						}
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						ll_loading.setVisibility(View.GONE);
						if(anima!=null){
							anima.stop();
						}
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
							return;
						}
						if(userInfoComm.getBody().getContact()!=null&&!userInfoComm.getBody().getContact().equals("")&&
								userInfoComm.getBody().getAddress()!=null&&!userInfoComm.getBody().getAddress().equals("")&&
								userInfoComm.getBody().getFulladdress()!=null&&!userInfoComm.getBody().getFulladdress().equals("")&&
								userInfoComm.getBody().getAreacode()!=null&&!userInfoComm.getBody().getAreacode().equals("")){
							String totalnum = "";
							if (jfscSpList.get(selectId).getNum() != null
									&& !jfscSpList.get(selectId).getNum().trim().equals("")
									&& jfscSpList.get(selectId).getJifen() != null
									&& !jfscSpList.get(selectId).getJifen().equals("")) {
								totalnum = (Integer.parseInt(jfscSpList.get(
										selectId).getNum()) * Integer.parseInt(jfscSpList.get(selectId)
										.getJifen())) + "";
							}
							if(act instanceof GwcActivity){
								((GwcActivity)act).showSheet(act, jfscSpList.get(selectId).getCid(), totalnum,jfscSpList.get(selectId).getNum());
							}else if(act instanceof WdddActivity){
								((WdddActivity)act).showSheet(act, jfscSpList.get(selectId).getCid(), totalnum,jfscSpList.get(selectId).getNum());
							}
							
						}else{
							CommonUtils.setCurrentChildMenuActivity("grzl");
							Intent intent = new Intent(act,
									GrzlActivity.class);
							startActivityForResult(intent, 1234);
						}
					}
				});
	}

}
