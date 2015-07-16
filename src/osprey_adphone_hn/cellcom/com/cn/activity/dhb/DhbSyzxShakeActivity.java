package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.YyyPrizeNewComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.ShakeListener;
import osprey_adphone_hn.cellcom.com.cn.widget.ShakeListener.OnShakeListener;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class DhbSyzxShakeActivity extends ActivityFrame implements
		OnActionSheetSelected {

	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;

//	private String currentGgid = "";
	private String largepic;// 大图地址
	private RelativeLayout advrl;
	private RelativeLayout rl_money_anim;
	private LinearLayout ll_money_anim;
	private TextView tv_add_money_num;
	private ImageView iv_money_box;

	private FinalBitmap finalBitmap;

	// 加载框
	private LinearLayout ll_loading;
	private ImageView imageView_loading;
	private AnimationDrawable animationDrawable;
	
	private ImageView shakeBg;
	private ImageView iv_ad;
	/**
	 * 20150428新增
	 */
	private LinearLayout ll_ad;
	private TextView tv_ljgd;
	private String adlink = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_syzx_snake_activity);
		AppendTitleBody1();
		isShowSlidingMenu(false);
		HideSet();
//		receiveIntentData();
		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// 初始化震动
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		// 初始化传感器监听
		mShakeListener = new ShakeListener(DhbSyzxShakeActivity.this);
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);

		finalBitmap = FinalBitmap.create(this);
		rl_money_anim = (RelativeLayout) findViewById(R.id.rl_money_anim);
		ll_money_anim = (LinearLayout) findViewById(R.id.ll_money_anim);
		tv_add_money_num = (TextView) findViewById(R.id.tv_add_money_num);
		iv_money_box = (ImageView) findViewById(R.id.iv_money_box);

		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		imageView_loading = (ImageView) findViewById(R.id.loadingImageView);
		animationDrawable = (AnimationDrawable) imageView_loading.getBackground();
		advrl=(RelativeLayout)findViewById(R.id.advrl);
		ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
		iv_ad = (ImageView) findViewById(R.id.iv_ad);
		tv_ljgd = (TextView) findViewById(R.id.tv_ljgd);
		
		shakeBg=(ImageView)findViewById(R.id.shakeBg);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		
		findViewById(R.id.llAppBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ll_ad.getVisibility() == View.VISIBLE){
					ll_ad.setVisibility(View.GONE);
					mShakeListener.start();
				}else{
					finish();
				}
			}
		});

		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				startAnim(); // 开始 摇一摇手掌动画
				mShakeListener.stop();

				startVibrato(R.raw.shake_sound_male); // 开始 震动

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						getYyyPrizeNew();
					}
				}, 2000);
			}
		});
		tv_ljgd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAdWeb();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		SetTopBarTitle(getResources().getString(R.string.os_dhb_syzx_yq));
		if (largepic != null
				&& (largepic.contains(".jpg") || largepic.contains(".png"))) {
			finalBitmap.display(iv_ad, largepic);
		}
		BitMapUtil.getImgOpt(DhbSyzxShakeActivity.this, finalBitmap, shakeBg, R.drawable.app_shakehideimg_man);
		BitMapUtil.getImgOpt(DhbSyzxShakeActivity.this, finalBitmap, advrl, R.drawable.os_dhb_adv);
//		BitMapUtil.getImgOpt(DhbSyzxShakeActivity.this, finalBitmap, iv_money_box, R.drawable.os_coins_box);
		
	}

	/**
	 * 摇一摇领取奖品
	 * 旧流程接口 YYW_GETYAO
	 * 新流程接口 YYW_GETYAO_NEW
	 * 20150428放弃使用该方法：由于流程修改，旧接口不再使用
	 */
//	private void getYyyPrize(String ggid) {
//		if (SharepreferenceUtil.readString(DhbSyzxShakeActivity.this,
//				SharepreferenceUtil.fileName, "uid", "").equals("")) {
//			Intent loginintent = new Intent(DhbSyzxShakeActivity.this,
//					LoginActivity.class);
//			startActivity(loginintent);
//			return;
//		}
//		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
//		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
//				DhbSyzxShakeActivity.this, SharepreferenceUtil.fileName, "uid",
//				""));
//		cellComAjaxParams.put("ggid", ggid);
//		HttpHelper.getInstances(DhbSyzxShakeActivity.this).send(
//				FlowConsts.YYW_GETYAO_NEW, cellComAjaxParams,
//				CellComAjaxHttp.HttpWayMode.POST,
//				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {
//
//					@Override
//					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
//						// TODO Auto-generated method stub
//						if (animationDrawable != null) {
//							animationDrawable.stop();
//						}
//						ll_loading.setVisibility(View.GONE);
//						final YyyPrizeComm yyyPrizeComm = cellComAjaxResult
//								.read(YyyPrizeComm.class,
//										CellComAjaxResult.ParseType.GSON);
//						if (!FlowConsts.STATUE_1.equals(yyyPrizeComm
//								.getReturnCode())) {
//							Toast.makeText(DhbSyzxShakeActivity.this,
//									yyyPrizeComm.getReturnMessage(),
//									Toast.LENGTH_SHORT).show();
//							return;
//						}
//						iv_ad.setVisibility(View.VISIBLE);
//						mVibrator.cancel();
//						if (yyyPrizeComm.getBody().getIfmoney() != null
//								&& yyyPrizeComm.getBody().getIfmoney().trim()
//										.equals("Y")) {
//							if (yyyPrizeComm.getBody().getMoneytype()
//									.equals("1")) {
//								tv_add_money_num.setText("+"
//										+ yyyPrizeComm.getBody().getMoney()
//										+ "积分");
//								// 刷新本地积分
//								CommonUtils.refreshLocalCaichan(
//										DhbSyzxShakeActivity.this, "1",
//										yyyPrizeComm.getBody().getMoney());
//							} else if (yyyPrizeComm.getBody().getMoneytype()
//									.equals("2")) {
//								tv_add_money_num.setText("+"
//										+ yyyPrizeComm.getBody().getMoney()
//										+ "话费");
//								// 刷新本地话费
//								CommonUtils.refreshLocalCaichan(
//										DhbSyzxShakeActivity.this, "2",
//										yyyPrizeComm.getBody().getMoney());
//							} else if (yyyPrizeComm.getBody().getMoneytype()
//									.equals("3")) {
//								tv_add_money_num.setText("+"
//										+ yyyPrizeComm.getBody().getMoney()
//										+ "银元");
//								// 刷新本地银元
//								CommonUtils.refreshLocalCaichan(
//										DhbSyzxShakeActivity.this, "3",
//										yyyPrizeComm.getBody().getMoney());
//							}
//							if (rl_money_anim.getVisibility() == View.GONE) {
//								rl_money_anim.setVisibility(View.VISIBLE);
//							}
//							// 播放动画，完毕后播放下一条广告
//							ll_money_anim.removeAllViews();
//							final FlakeView flakeView = new FlakeView(
//									DhbSyzxShakeActivity.this);
//							ll_money_anim.addView(flakeView);
//							AnimationUtil.addScaleAnimation(tv_add_money_num,
//									1500);
//							AnimationUtil.fadeInAnimation(
//									DhbSyzxShakeActivity.this, iv_money_box);
//							startVibrato(R.raw.shake_match);
//							new Handler().postDelayed(new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									flakeView.pause();
//									if (rl_money_anim.getVisibility() == View.VISIBLE) {
//										rl_money_anim.setVisibility(View.GONE);
//									}
//									ll_money_anim.removeAllViews();
//									if (yyyPrizeComm.getBody().getMoneytype()
//											.equals("1")) {
//										AlertDialogPopupWindow.showSheet(
//												DhbSyzxShakeActivity.this,
//												DhbSyzxShakeActivity.this,
//												"您本次摇一摇获得了"
//														+ yyyPrizeComm
//																.getBody()
//																.getMoney()
//														+ "积分", 1);
//									} else if (yyyPrizeComm.getBody()
//											.getMoneytype().equals("2")) {
//										AlertDialogPopupWindow.showSheet(
//												DhbSyzxShakeActivity.this,
//												DhbSyzxShakeActivity.this,
//												"您本次摇一摇获得了"
//														+ yyyPrizeComm
//																.getBody()
//																.getMoney()
//														+ "话费", 1);
//									} else if (yyyPrizeComm.getBody()
//											.getMoneytype().equals("3")) {
//										AlertDialogPopupWindow.showSheet(
//												DhbSyzxShakeActivity.this,
//												DhbSyzxShakeActivity.this,
//												"您本次摇一摇获得了"
//														+ yyyPrizeComm
//																.getBody()
//																.getMoney()
//														+ "银元", 1);
//									}
//								}
//							}, 1500);
//						} else {
//							AlertDialogPopupWindow.showSheet(
//									DhbSyzxShakeActivity.this,
//									DhbSyzxShakeActivity.this, "真遗憾，这次木有奖励T-T",
//									1);
//						}
//					}
//
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//						super.onStart();
//						ll_loading.setVisibility(View.VISIBLE);
//						if (animationDrawable != null) {
//							animationDrawable.start();
//						}
//					}
//
//					@Override
//					public void onFailure(Throwable t, String strMsg) {
//						// TODO Auto-generated method stub
//						super.onFailure(t, strMsg);
//						if (animationDrawable != null) {
//							animationDrawable.stop();
//						}
//						ll_loading.setVisibility(View.GONE);
//					}
//				});
//	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato(int res) {
		MediaPlayer player;
		player = MediaPlayer.create(this, res);
		player.setLooping(false);
		player.start();

		// 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == event.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_DOWN){
			if(iv_ad.getVisibility() == View.VISIBLE){
				iv_ad.setVisibility(View.GONE);
				mShakeListener.start();
				return false;
			}else{
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 摇一摇领取奖品
	 * 旧流程接口 YYW_GETYAO
	 * 新流程接口 YYW_GETYAO_NEW
	 * 20150428新版摇一摇调用方法
	 * 最新流程接口 YYW_GETYAO_NEW2
	 */
	private void getYyyPrizeNew() {
		if (SharepreferenceUtil.readString(DhbSyzxShakeActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxShakeActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxShakeActivity.this, SharepreferenceUtil.fileName, "uid", ""));
//		cellComAjaxParams.put("ggid", ggid);
		HttpHelper.getInstances(DhbSyzxShakeActivity.this).send(
				FlowConsts.YYW_GETYAO_NEW2, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
						}
						ll_loading.setVisibility(View.GONE);
						final YyyPrizeNewComm yyyPrizeNewComm = cellComAjaxResult
								.read(YyyPrizeNewComm.class, CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(yyyPrizeNewComm	.getReturnCode())) {
//							Toast.makeText(DhbSyzxShakeActivity.this, yyyPrizeNewComm.getReturnMessage(),
//									Toast.LENGTH_SHORT).show();
							ShowMsg(yyyPrizeNewComm.getReturnMessage());
							return;
						}
						ll_ad.setVisibility(View.VISIBLE);
						tv_ljgd.setVisibility(View.GONE);
						largepic = yyyPrizeNewComm.getBody().getPicurl();
						adlink = yyyPrizeNewComm.getBody().getAdlink();
						if (largepic != null && (largepic.contains(".jpg") || largepic.contains(".png"))) {
							finalBitmap.display(iv_ad, largepic);
						}
						mVibrator.cancel();
						if (yyyPrizeNewComm.getBody().getIfwin() != null
								&& yyyPrizeNewComm.getBody().getIfwin().trim().equals("Y")) {
							if (yyyPrizeNewComm.getBody().getVerytype() == 1) {
								tv_add_money_num.setText("+" + yyyPrizeNewComm.getBody().getVerynum() + "积分");
								// 刷新本地积分
								CommonUtils.refreshLocalCaichan(DhbSyzxShakeActivity.this, "1", yyyPrizeNewComm.getBody().getVerynum() + "");
							} else if (yyyPrizeNewComm.getBody().getVerytype() == 2) {
								tv_add_money_num.setText("+"
										+ yyyPrizeNewComm.getBody().getVerynum() + "话费");
								// 刷新本地话费
								CommonUtils.refreshLocalCaichan( DhbSyzxShakeActivity.this, "2",
										yyyPrizeNewComm.getBody().getVerynum() + "");
							} else if (yyyPrizeNewComm.getBody().getVerytype() == 3) {
								tv_add_money_num.setText("+" + yyyPrizeNewComm.getBody().getVerynum() + "银元");
								// 刷新本地银元
								CommonUtils.refreshLocalCaichan(DhbSyzxShakeActivity.this, "3",
										yyyPrizeNewComm.getBody().getVerynum() + "");
							}
							if (rl_money_anim.getVisibility() == View.GONE) {
								rl_money_anim.setVisibility(View.VISIBLE);
							}
							// 播放动画，完毕后播放下一条广告
							ll_money_anim.removeAllViews();
							final FlakeView flakeView = new FlakeView(	DhbSyzxShakeActivity.this);
							ll_money_anim.addView(flakeView);
							AnimationUtil.addScaleAnimation(tv_add_money_num, 1500);
							AnimationUtil.fadeInAnimation(DhbSyzxShakeActivity.this, iv_money_box);
							startVibrato(R.raw.shake_match);
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									flakeView.pause();
									if (rl_money_anim.getVisibility() == View.VISIBLE) {
										rl_money_anim.setVisibility(View.GONE);
									}
									ll_money_anim.removeAllViews();
									tv_ljgd.setVisibility(View.VISIBLE);
									if (yyyPrizeNewComm.getBody().getVerytype() == 1) {
										AlertDialogPopupWindow.showSheet(DhbSyzxShakeActivity.this, DhbSyzxShakeActivity.this,
												"您本次摇一摇获得了" + yyyPrizeNewComm.getBody().getVerynum() + "积分", 1);
									} else if (yyyPrizeNewComm.getBody().getVerytype() == 2) {
										AlertDialogPopupWindow.showSheet(DhbSyzxShakeActivity.this, DhbSyzxShakeActivity.this,
												"您本次摇一摇获得了" + yyyPrizeNewComm.getBody().getVerynum() + "话费", 1);
									} else if (yyyPrizeNewComm.getBody().getVerytype() == 3) {
										AlertDialogPopupWindow.showSheet(DhbSyzxShakeActivity.this, DhbSyzxShakeActivity.this,
												"您本次摇一摇获得了" + yyyPrizeNewComm.getBody().getVerynum()+ "银元", 1);
									}
								}
							}, 1500);
						} else {
							tv_ljgd.setVisibility(View.VISIBLE);
							AlertDialogPopupWindow.showSheet(DhbSyzxShakeActivity.this,
									DhbSyzxShakeActivity.this, "真遗憾，这次木有奖励T-T", 1);
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
	
	//跳转到广告官网
	private void startAdWeb() {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(adlink)){
			ShowMsg("暂无更多信息！");
			return;
		}
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra("url", adlink);
		startActivity(intent);
	}
}
