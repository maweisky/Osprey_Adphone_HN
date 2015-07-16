package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCz;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCzComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdComm;
import osprey_adphone_hn.cellcom.com.cn.bean.KykAdResult;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 体验卡充值
 * @author wma
 *
 */
public class DhbGrzxTykczFragment extends FragmentBase {
  private DhbGrzxZhczActivity act;
  private TextView tyket;
  private Button czbtn;

  @Override
  public void onAttach(Activity activity) {
    // TODO Auto-generated method stub
    super.onAttach(activity);
    act = (DhbGrzxZhczActivity) activity;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.os_grzx_tykczfragment, container, false);
    initView(view);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onActivityCreated(savedInstanceState);
    initListener();
  }

  /**
   * 初始化视图
   */
  private void initView(View v) {
    tyket = (EditText) v.findViewById(R.id.tyket);
    czbtn=(Button)v.findViewById(R.id.czbtn);
  }

  /**
   * 初始化监听
   */
  private void initListener() {
    czbtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        String card = tyket.getText().toString();
        if (TextUtils.isEmpty(card)) {
          Toast.makeText(act, "请输入卡号", Toast.LENGTH_SHORT).show();
          return;
        }
        getSysPrize(card);
      }
    });
  }

  /**
   * 扫一扫获取奖品
   */
  private void getSysPrize(String card) {
    if (SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName, "uid",
        "").equals("")) {
      Intent loginintent = new Intent(act, LoginActivity.class);
      startActivity(loginintent);
      return;
    }
    CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
    cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
        SharepreferenceUtil.fileName, "uid", ""));
    cellComAjaxParams.put("card", card);
    HttpHelper.getInstances(act).send(FlowConsts.YYW_SAO_GETWIN,
        cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
        new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

          @Override
          public void onSuccess(CellComAjaxResult cellComAjaxResult) {
            // TODO Auto-generated method stub
            DismissProgressDialog();
            GrzxCzComm sysPrizeComm =
                cellComAjaxResult.read(GrzxCzComm.class, CellComAjaxResult.ParseType.GSON);
            if (!FlowConsts.STATUE_1.equals(sysPrizeComm.getReturnCode())) {
              Toast.makeText(act, sysPrizeComm.getReturnMessage(),
                  Toast.LENGTH_SHORT).show();
              return;
            }
            if ("Y".equalsIgnoreCase(sysPrizeComm.getBody().getIfwin())) {
              getNextAd(sysPrizeComm.getBody().getGgid(), sysPrizeComm.getBody());
            } else {
              Toast.makeText(act, sysPrizeComm.getReturnMessage(),
                  Toast.LENGTH_SHORT).show();
            }
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
   * 获取下一个广告 ggid 广告ID isCurrentGg 是否当前广告 0、当前1、下一个广告 isLookMore 是否查看更多广告图片
   */
  private void getNextAd(final String ggid,
          final GrzxCz grzxCz) {
      if (SharepreferenceUtil.readString(act,
              SharepreferenceUtil.fileName, "uid", "").equals("")) {
          Intent loginintent = new Intent(act,
                  LoginActivity.class);
          startActivity(loginintent);
          return;
      }
      CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
      cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
        act, SharepreferenceUtil.fileName, "uid", ""));
      cellComAjaxParams.put("ggid", ggid);
      cellComAjaxParams.put("next", "0");
      cellComAjaxParams.put("level", "1");
      HttpHelper.getInstances(act).send(
              FlowConsts.YYW_GETLOOKGGINFO, cellComAjaxParams,
              CellComAjaxHttp.HttpWayMode.POST,
              new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

                  @Override
                  public void onSuccess(CellComAjaxResult cellComAjaxResult) {
                      // TODO Auto-generated method stub
                      DismissProgressDialog();
                      KykAdComm kykAdComm = cellComAjaxResult.read(
                              KykAdComm.class,
                              CellComAjaxResult.ParseType.GSON);
                      if (!FlowConsts.STATUE_1.equals(kykAdComm
                              .getReturnCode())) {
                          Toast.makeText(act,
                                  kykAdComm.getReturnMessage(),
                                  Toast.LENGTH_SHORT).show();
                          return;
                      }

                      KykAdResult kykadresult = kykAdComm.getBody();
                      Intent intent = new Intent(act,
                              SysAdShowActivity.class);
                      intent.putExtra("kykadresult", kykadresult);
                      intent.putExtra("grzxCz", grzxCz);
                      startActivity(intent);
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
