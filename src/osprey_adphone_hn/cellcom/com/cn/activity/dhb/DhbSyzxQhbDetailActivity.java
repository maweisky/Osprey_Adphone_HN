package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxQhbNewList;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxQhbResultNewListComm;
import osprey_adphone_hn.cellcom.com.cn.bean.SyzxQhbYyNewListComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AnimationUtil;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import osprey_adphone_hn.cellcom.com.cn.widget.fallcoins.FlakeView;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow.OnActionSheetSelected;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

/**
 * 商业中心抢红包详情
 * 
 * @author wma
 * 
 */
public class DhbSyzxQhbDetailActivity extends ActivityFrame implements OnActionSheetSelected {
  private LinearLayout llAppBack;
  private MarqueeText tvTopTitle;
  private ImageView iv_ad;
  private SyzxQhbNewList syzxKykList;// 初始进到界面时的广告数据
  private String typeid;
  private FinalBitmap finalBitmap;
  private Button cybmbtn;
  private TextView cjsjtv;

  private RelativeLayout rl_money_anim;
  private LinearLayout ll_money_anim;
  private TextView tv_add_money_num;
  private ImageView iv_money_box;
  private boolean isBm=false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.os_dhb_syzx_qhb_detail_activity);
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
      syzxKykList = (SyzxQhbNewList) getIntent().getSerializableExtra("syzxKykListBean");
      typeid = getIntent().getStringExtra("typeid");
    }
  }

  /**
   * 初始化视图
   */
  private void initView() {
    finalBitmap = FinalBitmap.create(this);
    llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
    tvTopTitle = (MarqueeText) findViewById(R.id.tvTopTitle);

    iv_ad = (ImageView) findViewById(R.id.iv_ad);

    cybmbtn = (Button) findViewById(R.id.cybmbtn);
    cjsjtv = (TextView) findViewById(R.id.cjsjtv);
    rl_money_anim = (RelativeLayout) findViewById(R.id.rl_money_anim);
    ll_money_anim = (LinearLayout) findViewById(R.id.ll_money_anim);
    tv_add_money_num = (TextView) findViewById(R.id.tv_add_money_num);
    iv_money_box = (ImageView) findViewById(R.id.iv_money_box);
  }

  /**
   * 初始化监听
   */
  private void initListener() {
    llAppBack.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        if(isBm){
          setResult(RESULT_OK, intent);
        }
        finish();
      }
    });
    // 我要报名
    cybmbtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if ("2".equalsIgnoreCase(syzxKykList.getOrdertype())) {
          // 未预约
          // if ("3".equalsIgnoreCase(syzxKykList.getState())) {
          // // 已结束
          // } else {
          // makeRob(syzxKykList.getGgid());
          // }
          makeRob(syzxKykList.getGgid());
        } else {
          // 已预约
          RobredPack(syzxKykList.getGgid());
          // if ("1".equalsIgnoreCase(syzxKykList.getState())) {
          // // 未开始
          // } else if ("2".equalsIgnoreCase(syzxKykList.getState())) {
          // // 正在进行中
          // if ("Y".equalsIgnoreCase(syzxKykList.getIfwin())) {
          // // 是否抢到红包
          // } else {
          // RobredPack(syzxKykList.getGgid());
          // }
          // } else if ("3".equalsIgnoreCase(syzxKykList.getState())) {
          // // 已结束
          // if ("Y".equalsIgnoreCase(syzxKykList.getIfwin())) {
          // // 是否抢到红包
          // } else {}
          // }
        }
      }
    });
  }

  /**
   * 初始化数据
   */
  private void initData() {
    tvTopTitle.setText(syzxKykList.getTitle());
    if (syzxKykList.getLargepic() != null
        && (syzxKykList.getLargepic().contains(".jpg") || syzxKykList.getLargepic()
            .contains(".png"))) {
      finalBitmap.display(iv_ad, syzxKykList.getLargepic(), ContextUtil.getWidth(this),
          (int) (ContextUtil.getWidth(this) * 1.8), false);
    }
    if ("3".equals(typeid)) {
      iv_money_box.setBackgroundResource(R.drawable.os_hb_box);
    } else if ("1".equals(typeid)) {
      iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
    } else if ("2".equals(typeid)) {
      iv_money_box.setBackgroundResource(R.drawable.os_coins_box);
    }
    cjsjtv.setText(ContextUtil.getQhbTime(syzxKykList.getBegintime()) + " 红包准时抢");

    if ("2".equalsIgnoreCase(syzxKykList.getOrdertype())) {
      // 未预约
      if ("3".equalsIgnoreCase(syzxKykList.getState())) {
        // 已结束
        cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
        cybmbtn.setText("已结束");
      } else {
        cybmbtn.setText("参与报名");
        cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbyyselector);
      }
    } else {
      // 已预约
      if ("1".equalsIgnoreCase(syzxKykList.getState())) {
        // 未开始
        cybmbtn.setText("抢红包");
        cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
      } else if ("2".equalsIgnoreCase(syzxKykList.getState())) {
        // 正在进行中
        if ("Y".equalsIgnoreCase(syzxKykList.getIfwin())) {
          // 是否抢到红包
          cybmbtn.setText("抢红包");
          cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
        } else {
          cybmbtn.setText("抢红包");
          cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
        }
      } else if ("3".equalsIgnoreCase(syzxKykList.getState())) {
        // 已结束
        if ("Y".equalsIgnoreCase(syzxKykList.getIfwin())) {
          // 是否抢到红包
          cybmbtn.setText("已结束");
          cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
        } else {
          cybmbtn.setText("已结束");
          cybmbtn.setBackgroundResource(R.drawable.os_dhb_syzx_qhbselector);
        }
      }
    }
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

  /**
   * 获取抢红包数据
   */
  private void makeRob(String ggid) {
    ShowProgressDialog(R.string.hsc_progress);
    if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName, "uid", "").equals("")) {
      Intent loginintent = new Intent(DhbSyzxQhbDetailActivity.this, LoginActivity.class);
      startActivity(loginintent);
      return;
    }
    CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
    cellComAjaxParams.put("uid",
        SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName, "uid", ""));
    cellComAjaxParams.put("ggid", ggid);
    HttpHelper.getInstances(DhbSyzxQhbDetailActivity.this).send(FlowConsts.YYW_MAKEROB,
        cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
        new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

          @Override
          public void onFailure(Throwable t, String strMsg) {
            // TODO Auto-generated method stub
            super.onFailure(t, strMsg);
            DismissProgressDialog();
          }

          @Override
          public void onSuccess(CellComAjaxResult arg0) {
            // TODO Auto-generated method stub
            DismissProgressDialog();
            SyzxQhbYyNewListComm syzxKykListComm =
                arg0.read(SyzxQhbYyNewListComm.class, CellComAjaxResult.ParseType.GSON);
            String state = syzxKykListComm.getReturnCode();
            String msg = syzxKykListComm.getReturnMessage();
            if (!FlowConsts.STATUE_1.equals(state)) {
              ShowMsg(msg);
              return;
            }
            if (!"1".equals(syzxKykListComm.getBody().getState())) {
              ShowMsg(syzxKykListComm.getBody().getMessage());
              return;
            }
            ShowMsg("报名成功");
            isBm = true;
            syzxKykList.setOrdertype("1");
            initData();
          }
        });
  }

  /**
   * 抢红包数据
   */
  private void RobredPack(String ggid) {
    ShowProgressDialog(R.string.hsc_progress);
    if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName, "uid", "").equals("")) {
      Intent loginintent = new Intent(DhbSyzxQhbDetailActivity.this, LoginActivity.class);
      startActivity(loginintent);
      return;
    }
    CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
    cellComAjaxParams.put("uid",
        SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName, "uid", ""));
    cellComAjaxParams.put("ggid", ggid);
    HttpHelper.getInstances(DhbSyzxQhbDetailActivity.this).send(FlowConsts.YYW_ROBREDPACKED,
        cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
        new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

          @Override
          public void onFailure(Throwable t, String strMsg) {
            // TODO Auto-generated method stub
            super.onFailure(t, strMsg);
            DismissProgressDialog();
          }

          @Override
          public void onSuccess(CellComAjaxResult arg0) {
            // TODO Auto-generated method stub
            DismissProgressDialog();
            final SyzxQhbResultNewListComm syzxKykListComm =
                arg0.read(SyzxQhbResultNewListComm.class, CellComAjaxResult.ParseType.GSON);
            String state = syzxKykListComm.getReturnCode();
            String msg = syzxKykListComm.getReturnMessage();
            if (!FlowConsts.STATUE_1.equals(state)) {
              ShowMsg(msg);
              return;
            }
            if (!"3".equals(syzxKykListComm.getBody().getState())) {
              ShowMsg(syzxKykListComm.getBody().getMessage());
              return;
            }
            if (rl_money_anim.getVisibility() == View.GONE) {
              rl_money_anim.setVisibility(View.VISIBLE);
            }
            if (syzxKykListComm != null && syzxKykListComm.getBody().getRobsum() != null
                && !syzxKykListComm.getBody().getRobsum().trim().equals("")) {
              if (syzxKykList.getMoneytype().equals("1")) {
                CommonUtils.refreshLocalCaichan(DhbSyzxQhbDetailActivity.this, "1", syzxKykListComm
                    .getBody().getRobsum());
              } else if (syzxKykList.getMoneytype().equals("2")) {
                CommonUtils.refreshLocalCaichan(DhbSyzxQhbDetailActivity.this, "2", syzxKykListComm
                    .getBody().getRobsum());
              } else if (syzxKykList.getMoneytype().equals("3")) {
                CommonUtils.refreshLocalCaichan(DhbSyzxQhbDetailActivity.this, "3", syzxKykListComm
                    .getBody().getRobsum());
              }

              // 播放动画，完毕后播放下一条广告
              ll_money_anim.removeAllViews();
              final FlakeView flakeView = new FlakeView(DhbSyzxQhbDetailActivity.this);
              ll_money_anim.addView(flakeView);
              AnimationUtil.addScaleAnimation(tv_add_money_num, 1500);
              AnimationUtil.fadeInAnimation(DhbSyzxQhbDetailActivity.this, iv_money_box);
              startMediaPlayer(R.raw.shake_match);
              new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                  // TODO Auto-generated method stub
                  flakeView.pause();
                  if (rl_money_anim.getVisibility() == View.VISIBLE) {
                    rl_money_anim.setVisibility(View.GONE);
                  }
                  ll_money_anim.removeAllViews();
                  if ("1".equalsIgnoreCase(syzxKykListComm.getBody().getMoneytype())) {
                    AlertDialogPopupWindow.showSheet(DhbSyzxQhbDetailActivity.this,
                        DhbSyzxQhbDetailActivity.this, syzxKykListComm.getBody().getMessage(), 1);
                  } else if ("2".equalsIgnoreCase(syzxKykListComm.getBody().getMoneytype())) {
                    AlertDialogPopupWindow.showSheet(DhbSyzxQhbDetailActivity.this,
                        DhbSyzxQhbDetailActivity.this, syzxKykListComm.getBody().getMessage(), 2);
                  } else if ("3".equalsIgnoreCase(syzxKykListComm.getBody().getMoneytype())) {
                    AlertDialogPopupWindow.showSheet(DhbSyzxQhbDetailActivity.this,
                        DhbSyzxQhbDetailActivity.this, syzxKykListComm.getBody().getMessage(), 3);
                  }
                }
              }, 1500);
            } else {}

          }
        });
  }

  @Override
  public void onClick(int whichButton) {
    // TODO Auto-generated method stub
    if (whichButton == 1 || whichButton == 2 || whichButton == 3) {
      Intent intent = new Intent();
      setResult(RESULT_OK, intent);
      finish();
    }
  }
}
