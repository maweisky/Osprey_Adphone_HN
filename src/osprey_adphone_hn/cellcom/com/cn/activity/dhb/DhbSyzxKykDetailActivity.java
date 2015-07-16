package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.KykAdTypeAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.DescribeComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykNewList;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykType;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxKykTypeComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.AddAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.ItemClick;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 商业中心详情
 * @author Administrator
 *
 */
public class DhbSyzxKykDetailActivity extends Activity {
	private LinearLayout llAppBack, ll_look_other;
	private MarqueeText tvTopTitle;
	private TextView tv_detail;
	private ScrollView sv_detail;
	private ImageView iv_ad;
	// private LinearLayout ll_btn;
//	private FButton btn_get, btn_getmore;
	private RelativeLayout rl_money_anim;
	private LinearLayout ll_money_anim;
	private TextView tv_add_money_num;
	private ImageView iv_money_box;

	private SyzxKykNewList syzxKykList;// 初始进到界面时的广告数据
	private FinalBitmap finalBitmap;

	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;

	private List<SyzxKykType> syzxKykTypeList = new ArrayList<SyzxKykType>();
	private KykAdTypeAdapter adapter;
	private ListViewPopupWindow popup;

	// 下一条广告数据
	KykAdResult kykadresult;

	private String currentGgid;// 当前广告ID

	private String typeid = "";// 广告类型ID
	private String moneytype;
	private String money1;
	private String money2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.os_dhb_syzx_kyk_detail_activity);
		receiveIntentData();
		initView();
		initListener();
		initData();
	}

	/**
	 * 接收Intent数据
	 */
	private void receiveIntentData() {
		if (getIntent().getSerializableExtra("syzxKykListBean") != null) {
			syzxKykList = (SyzxKykNewList) getIntent().getSerializableExtra(
					"syzxKykListBean");
			if(syzxKykList!=null){				
				moneytype=syzxKykList.getMoneytype();
				money1=syzxKykList.getMoney1();
				money2=syzxKykList.getMoney2();
			}
		}
		if (getIntent().getStringExtra("typeid") != null) {
			typeid = getIntent().getStringExtra("typeid");
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		ll_look_other = (LinearLayout) findViewById(R.id.ll_look_other);
		tvTopTitle = (MarqueeText) findViewById(R.id.tvTopTitle);

		tv_detail = (TextView) findViewById(R.id.tv_detail);
		sv_detail = (ScrollView) findViewById(R.id.sv_detail);
		iv_ad = (ImageView) findViewById(R.id.iv_ad);
		// ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
		// btn_get = (FButton) findViewById(R.id.btn_get);
		// btn_getmore = (FButton) findViewById(R.id.btn_getmore);
		rl_money_anim = (RelativeLayout) findViewById(R.id.rl_money_anim);
		ll_money_anim = (LinearLayout) findViewById(R.id.ll_money_anim);
		tv_add_money_num = (TextView) findViewById(R.id.tv_add_money_num);
		iv_money_box = (ImageView) findViewById(R.id.iv_money_box);

		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) imageView_loading
				.getBackground();

		popup = new ListViewPopupWindow(this);
		popup.setAddAdapter(new AddAdapter() {

			@Override
			public void addAdapter(ListView listview) {
				// TODO Auto-generated method stub
				adapter = new KykAdTypeAdapter(DhbSyzxKykDetailActivity.this,
						syzxKykTypeList);
				listview.setAdapter(adapter);
			}
		});
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		llAppBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ll_look_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (syzxKykTypeList.size() > 0) {
					popup.showAsDropDown(ll_look_other,
							-ll_look_other.getWidth(), 1,
							ll_look_other.getWidth() * 2,
							LayoutParams.WRAP_CONTENT);
				} else {
					if (animationDrawable != null) {
						ll_loading.setVisibility(View.VISIBLE);
						animationDrawable.start();
					}
					getKykType(true);
				}
			}
		});

		popup.setItemClick(new ItemClick() {

			@Override
			public void setOnItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				popup.dimissPopupwindow();
				// getKykInfos(((SyzxKykType) arg0.getItemAtPosition(position))
				// .getTypeid());
				if (((SyzxKykType) arg0.getItemAtPosition(position))
						.getVerytype().equals(typeid)) {
				} else {
					Intent intent = new Intent();
					intent.putExtra("title", ((SyzxKykType) arg0
							.getItemAtPosition(position)).getTypename());
					intent.putExtra("typeid", ((SyzxKykType) arg0
							.getItemAtPosition(position)).getVerytype());
					setResult(RESULT_OK, intent);
				}
				finish();
			}
		});

		iv_ad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (iv_ad.getDrawable() != null) {
					initAdv();
				}
			}
		});

	}

	// 显示宣传图片
	private void initAdv() {
		final Dialog ad = new Dialog(this, R.style.Transparent);
		View v = LayoutInflater.from(this).inflate(R.layout.os_dhb_syzxadv,
				null);
		RelativeLayout mainrl=(RelativeLayout)v.findViewById(R.id.mainrl);
		Button topiv = (Button) v.findViewById(R.id.topiv);
		Button bottomiv = (Button) v.findViewById(R.id.bottomiv);
		topiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
				getPrize(currentGgid);
			}
		});
		bottomiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
				if (rl_money_anim.getVisibility() == View.VISIBLE) {
					rl_money_anim.setVisibility(View.GONE);
				}
				ll_money_anim.removeAllViews();
				getNextAd(currentGgid, "0", true);
			}
		});
		
		mainrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});

		if (money1 != null
				&& !money1.trim().equals("")) {
			if (moneytype.equals("1")) {
				topiv.setText("立即领取    " + money1
						+ "积分\n观看下一条广告");
				tv_add_money_num.setText("+" + money1 + "积分");
			} else if (moneytype.equals("2")) {
				topiv.setText("立即领取    " + money1
						+ "话费\n观看下一条广告");
				tv_add_money_num.setText("+" + money1 + "话费");
			} else if (moneytype.equals("3")) {
				topiv.setText("立即领取    " + money1
						+ "银元\n观看下一条广告");
				tv_add_money_num.setText("+" + money1 + "银元");
			}
		} else {
			if (moneytype.equals("1")) {
				topiv.setText("当前广告没有积分领取\n观看下一条广告");
			} else if (moneytype.equals("2")) {
				topiv.setText("当前广告没有话费领取\n观看下一条广告");
			} else if (moneytype.equals("3")) {
				topiv.setText("当前广告没有银元领取\n观看下一条广告");
			}
		}

		if (money2!= null
				&& !money2.trim().equals("")) {
			bottomiv.setEnabled(true);
			if (moneytype.equals("1")) {
				bottomiv.setText("赚取更多    " + money2
						+ "积分\n观看下一条广告");
			} else if (moneytype.equals("2")) {
				bottomiv.setText("赚取更多    " + money2
						+ "话费\n观看下一条广告");
			} else if (moneytype.equals("3")) {
				bottomiv.setText("赚取更多    " + money2
						+ "银元\n观看下一条广告");
			}
		} else {
			bottomiv.setEnabled(false);
		}
		ad.setContentView(v);
		ad.show();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if("3".equals(typeid)){
			iv_money_box.setBackgroundResource(R.drawable.os_hb_box);
		}else if("1".equals(typeid)){
			iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
		}else if("2".equals(typeid)){
			iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
		}
		currentGgid = syzxKykList.getGgid();
		tvTopTitle.setText(syzxKykList.getTitle());
		if (syzxKykList != null && syzxKykList.getInfo() != null
				&& !syzxKykList.getInfo().trim().equals("")) {
			tv_detail.setText(syzxKykList.getInfo());
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (tv_detail.getHeight() >= ContextUtil.dip2px(
							DhbSyzxKykDetailActivity.this, 85)) {
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.FILL_PARENT,
								ContextUtil.dip2px(
										DhbSyzxKykDetailActivity.this, 85));
						lp.addRule(RelativeLayout.BELOW, R.id.tv_xxjs);
						lp.setMargins(ContextUtil.dip2px(
								DhbSyzxKykDetailActivity.this, 10), ContextUtil
								.dip2px(DhbSyzxKykDetailActivity.this, 5),
								ContextUtil.dip2px(
										DhbSyzxKykDetailActivity.this, 10),
								ContextUtil.dip2px(
										DhbSyzxKykDetailActivity.this, 10));
						sv_detail.setLayoutParams(lp);
					}
				}
			}, 300);
		} else {
			tv_detail.setText("暂无");
		}
		if (syzxKykList.getLargepic() != null
				&& (syzxKykList.getLargepic().contains(".jpg") || syzxKykList
						.getLargepic().contains(".png"))) {
			finalBitmap.display(iv_ad, syzxKykList.getLargepic(),
					ContextUtil.getWidth(this),
					(int) (ContextUtil.getWidth(this) * 1.8), false);
		}
		getKykType(false);
	}

	/**
	 * 获取看一看类型
	 */
	private void getKykType(final boolean isShowPopup) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxKykDetailActivity.this, SharepreferenceUtil.fileName,
				"uid", ""));
		HttpHelper.getInstances(DhbSyzxKykDetailActivity.this).send(
				FlowConsts.YYW_GETKYKTYPE, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
						SyzxKykTypeComm syzxKykTypeComm = cellComAjaxResult
								.read(SyzxKykTypeComm.class,
										CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(syzxKykTypeComm
								.getReturnCode())) {
							Toast.makeText(DhbSyzxKykDetailActivity.this,
									syzxKykTypeComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						syzxKykTypeList.clear();
						syzxKykTypeList.addAll(syzxKykTypeComm.getBody());
						adapter.notifyDataSetChanged();
						if (isShowPopup) {
							popup.showAsDropDown(ll_look_other,
									-ll_look_other.getWidth(), 1,
									ll_look_other.getWidth() * 2,
									LayoutParams.WRAP_CONTENT);
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 获取下一个广告 ggid 广告ID isCurrentGg 是否当前广告 0、当前1、下一个广告 isLookMore 是否查看更多广告图片
	 */
	private void getNextAd(String ggid, String isCurrentGg,
			final boolean isLookMore) {
		if (SharepreferenceUtil.readString(DhbSyzxKykDetailActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxKykDetailActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxKykDetailActivity.this, SharepreferenceUtil.fileName,
				"uid", ""));
		cellComAjaxParams.put("ggid", ggid);
		cellComAjaxParams.put("next", isCurrentGg);
		cellComAjaxParams.put("level", "1");
		HttpHelper.getInstances(DhbSyzxKykDetailActivity.this).send(
				FlowConsts.YYW_GETLOOKGGINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
						KykAdComm kykAdComm = cellComAjaxResult.read(
								KykAdComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(kykAdComm
								.getReturnCode())) {
							Toast.makeText(DhbSyzxKykDetailActivity.this,
									kykAdComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						kykadresult = kykAdComm.getBody();
						if (isLookMore) {
							Intent intent = new Intent(
									DhbSyzxKykDetailActivity.this,
									AdShowActivity.class);
							intent.putExtra("kykadresult", kykadresult);
							intent.putExtra("currentGgid", currentGgid);
							startActivityForResult(intent, 0);
						} else {
							if (kykadresult.getNextggid() != null
									&& !kykadresult.getNextggid().trim()
											.equals("")) {
								currentGgid = kykadresult.getNextggid();
								tvTopTitle.setText(kykadresult.getTitle());
								if (kykadresult != null
										&& kykadresult.getData().size() > 0
										&& kykadresult.getData().get(0)
												.getInfo() != null
										&& !kykadresult.getData().get(0)
												.getInfo().trim().equals("")) {
									tv_detail.setText(kykadresult.getData()
											.get(0).getInfo());
								}
								if (kykadresult != null
										&& kykadresult.getData().size() > 0
										&& (kykadresult.getData().get(0)
												.getMeitiurl().contains(".jpg") || kykadresult
												.getData().get(0).getMeitiurl()
												.contains(".png"))) {
									finalBitmap.display(
											iv_ad,
											kykadresult.getData().get(0)
													.getMeitiurl(),
											ContextUtil
													.getWidth(DhbSyzxKykDetailActivity.this),
											(int) (ContextUtil
													.getWidth(DhbSyzxKykDetailActivity.this) * 1.8),
											false);
								}
								if (kykadresult != null
										&& kykadresult.getMoney1() != null
										&& !kykadresult.getMoney1().trim()
												.equals("")) {
									moneytype=kykadresult.getMoneytype();
									money1=kykadresult.getMoney1();
								} else {
									moneytype=kykadresult.getMoneytype();
									money1="";
								}

								if (kykadresult != null
										&& kykadresult.getMoney2() != null
										&& !kykadresult.getMoney2().trim()
												.equals("")) {
									moneytype=kykadresult.getMoneytype();
									money2=kykadresult.getMoney2();
								} else {
									moneytype=kykadresult.getMoneytype();
									money2="";
								}
							} else {
								Toast.makeText(DhbSyzxKykDetailActivity.this,
										"广告已播放完，暂时没有下一条广告了~",
										Toast.LENGTH_SHORT).show();
							}
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ll_loading.setVisibility(View.VISIBLE);
						if (animationDrawable != null) {
							animationDrawable.start();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 领取奖品
	 */
	private void getPrize(String ggid) {
		if (SharepreferenceUtil.readString(DhbSyzxKykDetailActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxKykDetailActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxKykDetailActivity.this, SharepreferenceUtil.fileName,
				"uid", ""));
		cellComAjaxParams.put("ggid", ggid);
		cellComAjaxParams.put("grade", "1");
		HttpHelper.getInstances(DhbSyzxKykDetailActivity.this).send(
				FlowConsts.YYW_GETLOOKMONEY, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
						DescribeComm describeComm = cellComAjaxResult.read(
								DescribeComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(describeComm
								.getReturnCode())) {
							Toast.makeText(DhbSyzxKykDetailActivity.this,
									describeComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (rl_money_anim.getVisibility() == View.GONE) {
							rl_money_anim.setVisibility(View.VISIBLE);
						}
						if (currentGgid.equals(syzxKykList.getGgid())) {// 当前广告为初始进入界面时的广告
							if (syzxKykList != null
									&& syzxKykList.getMoney1() != null
									&& !syzxKykList.getMoney1().trim()
											.equals("")) {
								if (syzxKykList.getMoneytype().equals("1")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "1",
											syzxKykList.getMoney1());
								} else if (syzxKykList.getMoneytype().equals(
										"2")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "2",
											syzxKykList.getMoney1());
								} else if (syzxKykList.getMoneytype().equals(
										"3")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "3",
											syzxKykList.getMoney1());
								}

								// 播放动画，完毕后播放下一条广告
								ll_money_anim.removeAllViews();
								final FlakeView flakeView = new FlakeView(
										DhbSyzxKykDetailActivity.this);
								ll_money_anim.addView(flakeView);
								AnimationUtil.addScaleAnimation(
										tv_add_money_num, 1500);
								AnimationUtil.fadeInAnimation(
										DhbSyzxKykDetailActivity.this,
										iv_money_box);
								startMediaPlayer(R.raw.shake_match);
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										flakeView.pause();
										if (rl_money_anim.getVisibility() == View.VISIBLE) {
											rl_money_anim
													.setVisibility(View.GONE);
										}
										ll_money_anim.removeAllViews();
										getNextAd(currentGgid, "1", false);
									}
								}, 1500);
							} else {
								if (syzxKykList != null) {
									// 直接播放下一条广告
									getNextAd(currentGgid, "1", false);
								}
							}
						} else {
							if (kykadresult != null
									&& kykadresult.getMoney1() != null
									&& !kykadresult.getMoney1().trim()
											.equals("")) {
								if (kykadresult.getMoneytype().equals("1")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "1",
											kykadresult.getMoney1());
								} else if (kykadresult.getMoneytype().equals(
										"2")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "2",
											kykadresult.getMoney1());
								} else if (kykadresult.getMoneytype().equals(
										"3")) {
									CommonUtils.refreshLocalCaichan(
											DhbSyzxKykDetailActivity.this, "3",
											kykadresult.getMoney1());
								}

								// 播放动画，完毕后播放下一条广告
								ll_money_anim.removeAllViews();
								final FlakeView flakeView = new FlakeView(
										DhbSyzxKykDetailActivity.this);
								ll_money_anim.addView(flakeView);
								AnimationUtil.addScaleAnimation(
										tv_add_money_num, 1500);
								AnimationUtil.fadeInAnimation(
										DhbSyzxKykDetailActivity.this,
										iv_money_box);
								startMediaPlayer(R.raw.shake_match);
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										flakeView.pause();
										if (rl_money_anim.getVisibility() == View.VISIBLE) {
											rl_money_anim
													.setVisibility(View.GONE);
										}
										ll_money_anim.removeAllViews();
										getNextAd(currentGgid, "1", false);
									}
								}, 1500);
							} else {
								if (syzxKykList != null) {
									// 直接播放下一条广告
									getNextAd(currentGgid, "1", false);
								}
							}
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ll_loading.setVisibility(View.VISIBLE);
						if (animationDrawable != null) {
							animationDrawable.start();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 获奖声音
	 * 
	 * @param res
	 */
	public void startMediaPlayer(int res) {
		MediaPlayer player;
		player = MediaPlayer.create(this, res);
		player.setLooping(false);
		player.start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 0) {
				if (data != null) {
					if (data.getStringExtra("currentGgid") != null) {

					}
					getNextAd(data.getStringExtra("currentGgid"), "1", false);
				}
			}
		}
	}
}
