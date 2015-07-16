package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.ZyzPrizeNewComm;
import osprey_adphone_hn.cellcom.com.cn.bean.ZyzPrizeNewList;
import osprey_adphone_hn.cellcom.com.cn.bean.ZyzPrizeNewListComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class DhbSyzxZyzActivity extends ActivityFrame implements
		OnActionSheetSelected {
	private TextView tv_name, tv_jf,tv_namedetail;
	private ImageView iv_disc, iv_pointer, iv_start;
	private boolean isPlaying = false;

	private RelativeLayout rl_money_anim;
	private LinearLayout ll_money_anim;
	private TextView tv_add_money_num;
	private ImageView iv_money_box;

	private FinalBitmap finalBitmap;
	private ZyzPrizeNewListComm zyzPrizeNewListComm;
	private List<ZyzPrizeNewList> zyzPrizeList = new ArrayList<ZyzPrizeNewList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_syzx_zyz_activity);
		AppendTitleBody1();
		isShowSlidingMenu(false);
		HideSet();
		SetTopBarTitle(getResources().getString(R.string.os_dhb_syzx_cj));
		findViewById(R.id.llAppSet).setVisibility(View.INVISIBLE);
		initView();
		initListener();
		initData();
//		getZyzInfo();
	}

	/**
	 * 初始化视图
	 */

	private void initView() {
		finalBitmap = FinalBitmap.create(this);
		tv_namedetail=(TextView)findViewById(R.id.tv_namedetail);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_jf = (TextView) findViewById(R.id.tv_jf);
		iv_disc = (ImageView) findViewById(R.id.iv_disc);
		iv_pointer = (ImageView) findViewById(R.id.iv_pointer);
		iv_start = (ImageView) findViewById(R.id.iv_start);

		rl_money_anim = (RelativeLayout) findViewById(R.id.rl_money_anim);
		ll_money_anim = (LinearLayout) findViewById(R.id.ll_money_anim);
		tv_add_money_num = (TextView) findViewById(R.id.tv_add_money_num);
		iv_money_box = (ImageView) findViewById(R.id.iv_money_box);
	}

	/**
	 * 初始化控件
	 */
	private void initListener() {
		iv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isPlaying && zyzPrizeNewListComm!=null) {
				  if("1".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
				    AlertDialogPopupWindow.showSheet(DhbSyzxZyzActivity.this,
                      DhbSyzxZyzActivity.this, "是否支付"+zyzPrizeNewListComm.getBody().getNeednum()+"积分转一转？", 1);
				  }else if("2".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
				    AlertDialogPopupWindow.showSheet(DhbSyzxZyzActivity.this,
                      DhbSyzxZyzActivity.this, "是否支付"+zyzPrizeNewListComm.getBody().getNeednum()+"话费转一转？", 2);
				  }else if("3".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
				    AlertDialogPopupWindow.showSheet(DhbSyzxZyzActivity.this,
                      DhbSyzxZyzActivity.this, "是否支付"+zyzPrizeNewListComm.getBody().getNeednum()+"银元转一转？", 3);
                  }
				}else{
				  Toast.makeText(DhbSyzxZyzActivity.this, "数据获取中,请稍后...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		getGrzl();
		getZyzInfoNew();
	}

	private void zyzAnim(final boolean isWining, float chooseAngle,
			final ZyzPrizeNewComm zyzPrizeNewComm, final String prize) {
		/** 设置旋转动画 */
		RotateAnimation animation = new RotateAnimation(0f,
				(360f * 8 + chooseAngle), Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(3000);// 设置动画持续时间
		/** 常用方法 */
		// animation.setRepeatCount(int repeatCount);//设置重复次数
		animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		animation.setInterpolator(new LinearInterpolator());
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				isPlaying = true;
				Toast.makeText(DhbSyzxZyzActivity.this, "开始抽奖~", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (isWining) {
					tv_add_money_num.setText("+" + prize);
					if (rl_money_anim.getVisibility() == View.GONE) {
						rl_money_anim.setVisibility(View.VISIBLE);
					}
					// 播放动画，完毕后播放下一条广告
					ll_money_anim.removeAllViews();
					final FlakeView flakeView = new FlakeView(DhbSyzxZyzActivity.this);
					ll_money_anim.addView(flakeView);
					AnimationUtil.addScaleAnimation(tv_add_money_num, 1500);
					AnimationUtil.fadeInAnimation(DhbSyzxZyzActivity.this,	iv_money_box);
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
							AlertDialogPopupWindow.showSheet(
									DhbSyzxZyzActivity.this,
									DhbSyzxZyzActivity.this, "您本次转一转获得了" + prize, 4);
						}
					}, 1500);
				} else {
					AlertDialogPopupWindow.showSheet(DhbSyzxZyzActivity.this,
							DhbSyzxZyzActivity.this, "谢谢参与~", 4);
				}
				isPlaying = false;
				// Toast.makeText(DhbSyzxZyzActivity.this, "谢谢参与~",
				// Toast.LENGTH_SHORT).show();
			}
		});
		if (animation != null) {
			iv_pointer.clearAnimation();
			iv_pointer.setAnimation(animation);
		}
	}

	public void startVibrato(int res) {
		MediaPlayer player;
		player = MediaPlayer.create(this, res);
		player.setLooping(false);
		player.start();

	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
		  if("1".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
		    if(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "jifen", "").equals("")){
              ShowMsg("您的积分不足~");
          }else{
              if(Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "jifen"))
                  >=zyzPrizeNewListComm.getBody().getNeednum()){
                  getZyzPrizeNew();
              }else{
                  ShowMsg("您的积分不足~");
              }
          }
          }
		} else if (whichButton == 2) {
		  if("2".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
            if(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
              "huafei", "").equals("")){
              ShowMsg("您的话费不足~");
          }else{
              if(Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                "huafei", ""))>=zyzPrizeNewListComm.getBody().getNeednum()){
                  getZyzPrizeNew();
              }else{
                  ShowMsg("您的话费不足~");
              }
          }
          }
		}else if(whichButton==3){
		  if("3".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
            if(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
              "yinyuan", "").equals("")){
              ShowMsg("您的银元不足~");
          }else{
              if(Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                "yinyuan", ""))>=zyzPrizeNewListComm.getBody().getNeednum()){
                  getZyzPrizeNew();
              }else{
                  ShowMsg("您的银元不足~");
              }
          }
          }
		}else if(whichButton==4){
		  getGrzl();
		}
	}
	
	
    protected void getGrzl() {
        final String uid = SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName, "uid");
        CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
        cellComAjaxParams.put("uid", uid);
        HttpHelper.getInstances(this).send(FlowConsts.YYW_USERINFO, cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
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
                    }

                    @Override
                    public void onSuccess(CellComAjaxResult arg0) {
                        // TODO Auto-generated method stub
                        UserInfoComm userInfoComm = arg0.read(UserInfoComm.class,
                                CellComAjaxResult.ParseType.GSON);
                        String state = userInfoComm.getReturnCode();
                        String msg = userInfoComm.getReturnMessage();
                        if (!FlowConsts.STATUE_1.equals(state)) {
                            ShowMsg(msg);
                            return;
                        }
                        initValue(userInfoComm.getBody(), uid);
                        if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this,
                                SharepreferenceUtil.fileName, "huafei", "").equals("")) {
                            tv_kyhf.setText("￥0");
                        } else {
                            tv_kyhf.setText("￥" + SharepreferenceUtil.readString(
                                            DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "huafei", ""));
                        }
                        if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this,
                                SharepreferenceUtil.fileName, "username", "").equals("")) {
                            tv_name.setText("客户");
                        } else {
                            tv_name.setText(SharepreferenceUtil.readString(
                                            DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "username", ""));
                        }
                        if(zyzPrizeNewListComm!=null && "1".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
                          if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                            "jifen", "").equals("")) {
                            tv_namedetail.setText("积分:");
                              tv_jf.setText("0");
                          } else {
                            tv_namedetail.setText("积分:");
                              tv_jf.setText(""+Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                                  "jifen", "")));
                          }
                        }else if(zyzPrizeNewListComm!=null && "2".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
                          if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                            "huafei", "").equals("")) {
                            tv_namedetail.setText("话费:");
                              tv_jf.setText("￥0");
                          } else {
                            tv_namedetail.setText("话费:");
                              tv_jf.setText("￥"+Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                                  "huafei", "")));
                          }
                        }else if(zyzPrizeNewListComm!=null && "3".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
                          if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                            "yinyuan", "").equals("")) {
                            tv_namedetail.setText("银元:");
                              tv_jf.setText("0");
                          } else {
                            tv_namedetail.setText("银元:");
                              tv_jf.setText(""+Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                                  "yinyuan", "")));
                          }
                        }else{
                          if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                            "huafei", "").equals("")) {
                            tv_namedetail.setText("话费:");
                              tv_jf.setText("￥0");
                          } else {
                            tv_namedetail.setText("话费:");
                              tv_jf.setText("￥"+Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName,
                                  "huafei", "")));
                          }
                        }
                    }
                });
    }
    
	
	/**
	 * 转一转获取奖品信息列表
	 */
	private void getZyzInfoNew() {
		if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxZyzActivity.this, LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "uid", ""));
		HttpHelper.getInstances(DhbSyzxZyzActivity.this).send(
				FlowConsts.YYW_ZYZ_GETLIST, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						zyzPrizeNewListComm = cellComAjaxResult.read(
								ZyzPrizeNewListComm.class, CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(zyzPrizeNewListComm.getReturnCode())) {
							ShowMsg(zyzPrizeNewListComm.getReturnMessage());
							return;
						}
						if (zyzPrizeNewListComm.getBody().getImgurl() != null 
								&& (zyzPrizeNewListComm.getBody().getImgurl().contains(".jpg") 
										|| zyzPrizeNewListComm.getBody().getImgurl().contains(".png"))) {
							finalBitmap.display(iv_disc, zyzPrizeNewListComm	.getBody().getImgurl());
						}
						iv_start.setVisibility(View.VISIBLE);
						zyzPrizeList.addAll(zyzPrizeNewListComm.getBody().getData());
						Collections.sort(zyzPrizeList);
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
	
	/**
	 * 转一转领取奖品
	 */
	private void getZyzPrizeNew() {
		if (SharepreferenceUtil.readString(DhbSyzxZyzActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhbSyzxZyzActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "uid",""));
		HttpHelper.getInstances(DhbSyzxZyzActivity.this).send(
				FlowConsts.YYW_ZYZ_GETWIN, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub						
						DismissProgressDialog();
						if("1".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
						  float jifen = Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "jifen")) - zyzPrizeNewListComm.getBody().getNeednum();
	                        SharepreferenceUtil.write(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "jifen",   jifen+"");
	                        tv_namedetail.setText("积分:");
	                        tv_jf.setText(""+jifen);
                        }else if("2".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
                          float jifen = Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "huafei")) - zyzPrizeNewListComm.getBody().getNeednum();
                          SharepreferenceUtil.write(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "huafei",   jifen+"");
                          tv_namedetail.setText("话费:");
                          tv_jf.setText("￥"+jifen);
                        }else if("3".equalsIgnoreCase(zyzPrizeNewListComm.getBody().getNeedtype())){
                          float jifen = Float.parseFloat(SharepreferenceUtil.readString(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "yinyuan")) - zyzPrizeNewListComm.getBody().getNeednum();
                          SharepreferenceUtil.write(DhbSyzxZyzActivity.this, SharepreferenceUtil.fileName, "yinyuan",   jifen+"");
                          tv_namedetail.setText("银元:");
                          tv_jf.setText(""+jifen);
                        }
						final ZyzPrizeNewComm zyzPrizeNewComm = cellComAjaxResult
								.read(ZyzPrizeNewComm.class, CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(zyzPrizeNewComm.getReturnCode())) {
							ShowMsg(zyzPrizeNewComm.getReturnMessage());
							return;
						}
						int angle = 0;
						String prize = "谢谢参与~";
						boolean isWin = true;
						LogMgr.showLog("zyzPrizeNewComm.getBody().getIdx()--------------->" + zyzPrizeNewComm.getBody().getIdx());
						for (int i = 0; i < zyzPrizeList.size(); i++) {
							LogMgr.showLog("zyzPrizeList.get(" + i + ").getIdx()-------------->" + zyzPrizeList.get(i).getIdx());
							if (zyzPrizeNewComm.getBody().getIdx()== zyzPrizeList.get(i).getIdx()) {
								angle = angle + zyzPrizeList.get(i).getAngle() / 2;
								LogMgr.showLog("angle--------------->" + angle);
								if(zyzPrizeNewComm.getBody().getVerynum() == 0){
									isWin = false;
								}else if (zyzPrizeList.get(i).getVerytype() == 1) {
									prize = zyzPrizeList.get(i).getVerynum() + "积分";
								} else if (zyzPrizeList.get(i).getVerytype() ==2) {
									prize = zyzPrizeList.get(i).getVerynum() + "话费";
								} else if (zyzPrizeList.get(i).getVerytype() == 3) {
									prize = zyzPrizeList.get(i).getVerynum() + "银元";
								}
								break;
							}							
							angle = angle + zyzPrizeList.get(i).getAngle();
							LogMgr.showLog("angle--------------->" + angle);
						}

						//转动动画
						zyzAnim(isWin, angle, zyzPrizeNewComm, prize);
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
