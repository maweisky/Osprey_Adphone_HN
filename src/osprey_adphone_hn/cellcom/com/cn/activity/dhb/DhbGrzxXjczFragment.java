package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.alipay.PayResult;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.alipay.SignUtils;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.ZfbComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.alipay.sdk.app.PayTask;

public class DhbGrzxXjczFragment extends FragmentBase {
  private DhbGrzxZhczActivity act;
  private TextView phonetv;
  private EditText tyket;
  private Button czbtn;
  
  private static final int SDK_PAY_FLAG = 1;
  private static final int SDK_CHECK_FLAG = 2;

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
    View view = inflater.inflate(R.layout.os_grzx_xjczfragment, container, false);
    initView(view);
    initData();
    return view;
  }

  private void initData() {
    // TODO Auto-generated method stub
    String account = SharepreferenceUtil.readString(act, SharepreferenceUtil.fileName, "account");
    phonetv.setText(account);
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
    phonetv = (TextView) v.findViewById(R.id.phonetv);
    tyket=(EditText)v.findViewById(R.id.tyket);
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
        String tykettxt=tyket.getText().toString();
        if(TextUtils.isEmpty(tykettxt)){
          ShowMsg("请输入充值金额");
          return;
        }
        getPrize(tykettxt);
      }
    });
  }
  /**
   * call alipay sdk pay. 调用SDK支付
   * 
   */
  public void pay(String price,String PARTNER,String SELLER,String TradeNo,String RSA_PRIVATE,String notify_url) {
      // 订单
      String orderInfo = getOrderInfo("充值", "鱼鹰充值", price,PARTNER,SELLER,TradeNo,notify_url);

      // 对订单做RSA 签名
      String sign = sign(orderInfo,RSA_PRIVATE);
      try {
          // 仅需对sign 做URL编码
          sign = URLEncoder.encode(sign, "UTF-8");
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      // 完整的符合支付宝参数规范的订单信息
      final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
              + getSignType();

      Runnable payRunnable = new Runnable() {

          @Override
          public void run() {
              // 构造PayTask 对象
              PayTask alipay = new PayTask(act);
              // 调用支付接口，获取支付结果
              String result = alipay.pay(payInfo);

              Message msg = new Message();
              msg.what = SDK_PAY_FLAG;
              msg.obj = result;
              mHandler.sendMessage(msg);
          }
      };

      // 必须异步调用
      Thread payThread = new Thread(payRunnable);
      payThread.start();
  }
  
  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
        switch (msg.what) {
        case SDK_PAY_FLAG: {
            PayResult payResult = new PayResult((String) msg.obj);
            
            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();
            
            String resultStatus = payResult.getResultStatus();

            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                Toast.makeText(act, "支付成功",
                        Toast.LENGTH_SHORT).show();
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    Toast.makeText(act, "支付结果确认中",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(act, "支付失败",
                            Toast.LENGTH_SHORT).show();

                }
            }
            break;
        }
        case SDK_CHECK_FLAG: {
            Toast.makeText(act, "检查结果为：" + msg.obj,
                    Toast.LENGTH_SHORT).show();
            break;
        }
        default:
            break;
        }
    };
};

  /**
   * get the sign type we use. 获取签名方式
   * 
   */
  public String getSignType() {
      return "sign_type=\"RSA\"";
  }
  /**
   * sign the order info. 对订单信息进行签名
   * 
   * @param content
   *            待签名订单信息
   */
  public String sign(String content,String RSA_PRIVATE) {
      return SignUtils.sign(content, RSA_PRIVATE);
  }
  /**
   * create the order info. 创建订单信息
   * 
   */
  public String getOrderInfo(String subject, String body, String price,String PARTNER,String SELLER,
                             String TradeNo,String notify_url) {
      // 签约合作者身份ID
      String orderInfo = "partner=" + "\"" + PARTNER + "\"";

      // 签约卖家支付宝账号
      orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

      // 商户网站唯一订单号
      orderInfo += "&out_trade_no=" + "\"" + TradeNo + "\"";

      // 商品名称
      orderInfo += "&subject=" + "\"" + subject + "\"";

      // 商品详情
      orderInfo += "&body=" + "\"" + body + "\"";

      // 商品金额
      orderInfo += "&total_fee=" + "\"" + price + "\"";

      // 服务器异步通知页面路径
      orderInfo += "&notify_url=" + "\"" + notify_url
              + "\"";

      // 服务接口名称， 固定值
      orderInfo += "&service=\"mobile.securitypay.pay\"";

      // 支付类型， 固定值
      orderInfo += "&payment_type=\"1\"";

      // 参数编码， 固定值
      orderInfo += "&_input_charset=\"utf-8\"";

      // 设置未付款交易的超时时间
      // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
      // 取值范围：1m～15d。
      // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
      // 该参数数值不接受小数点，如1.5h，可转换为90m。
      orderInfo += "&it_b_pay=\"30m\"";

      // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
      // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

      // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
      orderInfo += "&return_url=\"m.alipay.com\"";

      // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
      // orderInfo += "&paymethod=\"expressGateway\"";

      return orderInfo;
  }
  /**
   * 获取订单号
   */
  private void getPrize(final String moneynum) {
    ShowProgressDialog(R.string.hsc_progress);
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
      cellComAjaxParams.put("moneynum", moneynum);
      HttpHelper.getInstances(act).send(
              FlowConsts.YYW_ZFB_CREATEORDER, cellComAjaxParams,
              CellComAjaxHttp.HttpWayMode.POST,
              new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

                  @Override
                  public void onSuccess(CellComAjaxResult cellComAjaxResult) {
                      // TODO Auto-generated method stub
                    DismissProgressDialog();
                    ZfbComm describeComm = cellComAjaxResult.read(
                      ZfbComm.class,
                              CellComAjaxResult.ParseType.GSON);
                      if (!FlowConsts.STATUE_1.equals(describeComm
                              .getReturnCode())) {
                          Toast.makeText(act,
                                  describeComm.getReturnMessage(),
                                  Toast.LENGTH_SHORT).show();
                          return;
                      }
                      pay(describeComm.getBody().getMoneynum(), 
                        describeComm.getBody().getPid(), 
                        describeComm.getBody().getSellermail(),
                        describeComm.getBody().getOrderno(),
                        describeComm.getBody().getPrivatekey()
                        /*"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKVOnojIBKRKFC4U9lmUr4j5GZKr+AMD1jQTCGfsR4dW+avqGHwB1MUb36g0qa+O1z0j+6I2RMFFRzMjVbjLYapVswhlFunSLdRAnM+tOMlj9bynBPsoJKVgTP84FE3ql0sYoOQl5hZmYHSpQ5x1nDOMlVWlY5h0fUBLznQyWoehAgMBAAECgYAFz73DEUUYgYI0HIiMna2OIzPC9EjD0l61dfXVsXjSSTx9zr0NnwLb6x7xyugsQ9lNTPaLvVytbsISCiv5Uy0bwW81RfUeDGOqcyp1nveNdphxT5Xo1c8g3lsl5g9TtcKxUvh2CXrofH3zuHvDM51UWv/rGsX7pZmoGdmLmucAAQJBANOT9h77ka0u5+8a16egSrao7k/Cn5GVZMM6PxiFQqAn92tKlFuTuiuTdQGUrL/rDX6xhuwKpcWRlKN4x2vY6uECQQDIA6jPnnRr6puPLzb5dx0a3bSPWIiCHoKrvPHET7yA1aaaeN3L/pYS+WtiaE4ATLUg+HZsyDwQG+0LtRGdU/TBAkAxiuoM4zd/aAZjVNO+qqgUEYaMIrSesG7B0DfKFBo/ylwDQZlvwC3N2l+BmUDCR9YHHqbLSsr6clZt2PBJbqfBAkEAgujSPkFJlbBR7F9Mre5mIEVnfkLF5dQuVlUuL/y4NeKgTo45LOEOPJD+1cPK+as7Cb9VU1Gi7jA47+atBroCgQJAH6wuOw7qS8yZ+b/VTAzdY4Vh+r3akLgfrmAXcOXCaMZXuPaBhR866PARmAUljUqyEDy2YD3EOS8aDWi4KDlUfQ=="*/,
                        describeComm.getBody().getNotifyurl());
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
