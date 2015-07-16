package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import java.text.DecimalFormat;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.DhzxRateInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.ZyzPrizeComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 兑换中心
 * @author Administrator
 *
 */
public class DhzxActivity extends ActivityFrame {
	private TextView tv_dhgz;

	private TextView tv_yy1, tv_ye1,tv_yongyou1,tv_dhl1;
	private EditText et_num1;
	private FButton btn_dh1;

	private TextView tv_yy2, tv_ye2,tv_yongyou2,tv_dhl2;
	private EditText et_num2;
	private FButton btn_dh2;

	private TextView tv_jf3, tv_ye3,tv_yongyou3,tv_dhl3;
	private EditText et_num3;
	private FButton btn_dh3;

	private String yongyou_yy = "0";//拥有银元
	private String yongyou_hf = "0";//拥有话费
	private String yongyou_jf = "0";//拥有积分
	
	private String rate1 = "";//银元兑换话费 兑换率
	private String rate2 = "";//银元兑换积分 兑换率
	private String rate3 = "";//积分兑换话费 兑换率
	private String[] ratestr1;
	private String[] ratestr2;
	private String[] ratestr3;

	private DecimalFormat format ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendTitleBody1();
		HideSet();
		AppendMainBody(R.layout.os_dhzx_activity);
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_dhb_grzx_dhzx));
		findViewById(R.id.llAppSet).setVisibility(View.INVISIBLE);
		initView();
		initData();
		initListener();
		getDhl();
		getGrzl();
	}

	private void initData() {
		// TODO Auto-generated method stub
		format = new DecimalFormat("###.##");
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		tv_dhgz = (TextView) findViewById(R.id.tv_dhgz);

		tv_yy1 = (TextView) findViewById(R.id.tv_yy1);
		tv_ye1 = (TextView) findViewById(R.id.tv_ye1);
		tv_yongyou1 = (TextView) findViewById(R.id.tv_yongyou1);
		tv_dhl1 = (TextView) findViewById(R.id.tv_dhl1);
		et_num1 = (EditText) findViewById(R.id.et_num1);
		btn_dh1 = (FButton) findViewById(R.id.btn_dh1);

		tv_yy2 = (TextView) findViewById(R.id.tv_yy2);
		tv_ye2 = (TextView) findViewById(R.id.tv_ye2);
		tv_yongyou2 = (TextView) findViewById(R.id.tv_yongyou2);
		tv_dhl2 = (TextView) findViewById(R.id.tv_dhl2);
		et_num2 = (EditText) findViewById(R.id.et_num2);
		btn_dh2 = (FButton) findViewById(R.id.btn_dh2);

		tv_jf3 = (TextView) findViewById(R.id.tv_jf3);
		tv_ye3 = (TextView) findViewById(R.id.tv_ye3);
		tv_yongyou3 = (TextView) findViewById(R.id.tv_yongyou3);
		tv_dhl3 = (TextView) findViewById(R.id.tv_dhl3);
		et_num3 = (EditText) findViewById(R.id.et_num3);
		btn_dh3 = (FButton) findViewById(R.id.btn_dh3);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// 积分规则
		tv_dhgz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(DhzxRuleActivity.class);
			}
		});

		// 银元-话费 确认兑换
		btn_dh1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(et_num1.getText().toString())) {
					ShowMsg("请输入兑换数量");
				} else {
					if (Float.parseFloat(et_num1.getText().toString()) > Float
							.parseFloat(yongyou_yy)) {
						ShowMsg("您的银元不足以兑换" + tv_yy1.getText().toString().substring(0,
								tv_yy1.getText().toString().indexOf("元"))
								+ "话费");
					} else {
						dhPrize("1", /*et_num1.getText().toString()*/
								tv_yy1.getText().toString().substring(0,
										tv_yy1.getText().toString().indexOf("元")));
					}
				}
			}
		});

		// 银元-积分 确认兑换
		btn_dh2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(et_num2.getText().toString())) {
					ShowMsg("请输入兑换数量");
				} else {
					if (Float.parseFloat(et_num2.getText().toString()) > Float
							.parseFloat(yongyou_yy)) {
						ShowMsg("您的银元不足以兑换" + tv_yy2.getText().toString().substring(0,
								tv_yy2.getText().toString().indexOf("分"))
								+ "积分");
					} else {
						dhPrize("2", /*et_num2.getText().toString()*/
								tv_yy2.getText().toString().substring(0,
										tv_yy2.getText().toString().indexOf("分")));
					}
				}
			}
		});

		// 积分-话费 确认兑换
		btn_dh3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(et_num3.getText().toString())) {
					ShowMsg("请输入兑换数量");
				} else {
					if (Float.parseFloat(et_num3.getText().toString()) > Float
							.parseFloat(yongyou_jf)) {
						ShowMsg("您的积分不足以兑换" + tv_jf3.getText().toString().substring(0,
								tv_jf3.getText().toString().indexOf("元"))
								+ "话费");
					} else {
						dhPrize("3", /*et_num3.getText().toString()*/
								tv_jf3.getText().toString().substring(0,
										tv_jf3.getText().toString().indexOf("元")));
					}
				}
			}
		});

		et_num1.addTextChangedListener(new TextWatcher() {
			private String text = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				text = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(text.trim().equals("")){
					tv_yongyou1.setText(yongyou_yy+"个");
					tv_yy1.setText("0元");
				}else{
					if(Float.parseFloat(text)>Float.parseFloat(yongyou_yy)){
						ShowMsg("您的银元不足~");
						tv_yongyou1.setText(yongyou_yy+"个");
						tv_yy1.setText(((Float.parseFloat(text)/Float.parseFloat(ratestr1[0].trim()))*
								Float.parseFloat(ratestr1[1].trim()))+"元");
					}else if((Float.parseFloat(text)%Float.parseFloat(ratestr1[0].trim()))!=0){
						ShowMsg("请输入"+ratestr1[0]+"的倍数~");
					}else{
						tv_yongyou1.setText((Float.parseFloat(yongyou_yy)-Float.parseFloat(text))+"个");
						tv_yy1.setText(((Float.parseFloat(text)/Float.parseFloat(ratestr1[0].trim()))*
								Float.parseFloat(ratestr1[1].trim()))+"元");
					}
				}
			}
		});

		et_num2.addTextChangedListener(new TextWatcher() {

			private String text = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				text = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(text.trim().equals("")){
					tv_yongyou2.setText(yongyou_yy+"个");
					tv_yy2.setText("0分");
				}else{
					if(Float.parseFloat(text)>Float.parseFloat(yongyou_yy)){
						ShowMsg("您的银元不足~");
						tv_yongyou2.setText(yongyou_yy+"个");
						tv_yy2.setText(((Float.parseFloat(text)/Float.parseFloat(ratestr2[0].trim()))*
								Float.parseFloat(ratestr2[1].trim()))+"分");
					}else if((Float.parseFloat(text)%Float.parseFloat(ratestr2[0].trim()))!=0){
						ShowMsg("请输入"+ratestr2[0]+"的倍数~");
					}else{
						tv_yongyou2.setText((Float.parseFloat(yongyou_yy)-Float.parseFloat(text))+"个");
						tv_yy2.setText(((Float.parseFloat(text)/Float.parseFloat(ratestr2[0].trim()))*
								Float.parseFloat(ratestr2[1].trim()))+"分");
					}
				}
			}
		});

		et_num3.addTextChangedListener(new TextWatcher() {

			private String text = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				text = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(text.trim().equals("")){
					tv_yongyou3.setText(yongyou_jf+"分");
					tv_jf3.setText("0元");
				}else{
					if(Float.parseFloat(text)>Float.parseFloat(yongyou_jf)){
						ShowMsg("您的积分不足~");
						tv_yongyou3.setText(yongyou_jf+"分");
						String temp=format.format((Float.parseFloat(text)/Float.parseFloat(ratestr3[0].trim()))*
								Float.parseFloat(ratestr3[1].trim()));
						tv_jf3.setText(temp+"元");
					}else if((Float.parseFloat(text)%Float.parseFloat(ratestr3[0].trim()))!=0){
						ShowMsg("请输入"+ratestr3[0]+"的倍数~");
					}else{
						tv_yongyou3.setText((Float.parseFloat(yongyou_jf)-Float.parseFloat(text))+"分");
						String temp=format.format(((Float.parseFloat(text)/Float.parseFloat(ratestr3[0].trim()))*
								Float.parseFloat(ratestr3[1].trim())));
						tv_jf3.setText(temp+"元");
					}
				}
			}
		});
	}

	// 获取兑换率
	private void getDhl() {
		String uid = SharepreferenceUtil.readString(DhzxActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(DhzxActivity.this).send(
				FlowConsts.YYW_GETRATE, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
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
						DhzxRateInfoComm dhzxRateInfoComm = arg0.read(
								DhzxRateInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = dhzxRateInfoComm.getReturnCode();
						String msg = dhzxRateInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						rate1 = dhzxRateInfoComm.getBody().getRate1();
						rate2 = dhzxRateInfoComm.getBody().getRate2();
						rate3 = dhzxRateInfoComm.getBody().getRate3();
						if(rate1!=null){
							tv_dhl1.setText(rate1);
							if(rate1.contains(":")){
								ratestr1 = rate1.split(":");
							}
						}
						if(rate2!=null){
							tv_dhl2.setText(rate2);
							if(rate2.contains(":")){
								ratestr2 = rate2.split(":");
							}
						}
						if(rate3!=null){
							tv_dhl3.setText(rate3);
							if(rate3.contains(":")){
								ratestr3 = rate3.split(":");
							}
						}
					}
				});
	}

	// 获取个人资料
	private void getGrzl() {
		final String uid = SharepreferenceUtil.readString(DhzxActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(DhzxActivity.this).send(
				FlowConsts.YYW_USERINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.hsc_progress);
					}

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
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						if (userInfoComm.getBody().getYinyuan() != null
								&& !userInfoComm.getBody().getYinyuan().trim()
								.equals("")) {
							tv_yongyou1.setText(userInfoComm.getBody().getYinyuan()
									.trim()
									+ "个");
							tv_yongyou2.setText(userInfoComm.getBody().getYinyuan()
									.trim()
									+ "个");
							yongyou_yy = userInfoComm.getBody().getYinyuan().trim();
						} else {
							tv_yongyou1.setText(0 + "个");
							tv_yongyou2.setText(0 + "个");
						}
						if (userInfoComm.getBody().getHuafei() != null
								&& !userInfoComm.getBody().getHuafei().trim()
								.equals("")) {
							tv_ye1.setText(userInfoComm.getBody().getHuafei()
									.trim()
									+ "元");
							tv_ye3.setText(userInfoComm.getBody().getHuafei()
									.trim()
									+ "元");
							yongyou_hf = userInfoComm.getBody().getHuafei()
									.trim();
						} else {
							tv_ye1.setText(0 + "元");
							tv_ye3.setText(0 + "元");
						}
						if (userInfoComm.getBody().getJifen() != null
								&& !userInfoComm.getBody().getJifen().trim()
								.equals("")) {
							tv_ye2.setText(userInfoComm.getBody().getJifen()
									.trim()
									+ "分");
							tv_yongyou3.setText(userInfoComm.getBody().getJifen()
									.trim()
									+ "分");
							yongyou_jf = userInfoComm.getBody().getJifen()
									.trim();
						} else {
							tv_ye2.setText(0 + "分");
							tv_yongyou3.setText(0 + "分");
						}
						if (userInfoComm.getBody().getViplevel() != null
								&& !userInfoComm.getBody().getViplevel()
										.trim().equals("")&&
										!userInfoComm.getBody().getViplevel()
										.trim().equals("0")) {
							SharepreferenceUtil.write(DhzxActivity.this,
									SharepreferenceUtil.fileName, "viplevel"+uid, userInfoComm.getBody().getViplevel());
						}
						SharepreferenceUtil.write(
								DhzxActivity.this,
								SharepreferenceUtil.fileName,
								"jifen",
								userInfoComm.getBody().getJifen());
						SharepreferenceUtil.write(
								DhzxActivity.this,
								SharepreferenceUtil.fileName,
								"huafei",
								userInfoComm.getBody().getHuafei());
						SharepreferenceUtil.write(
								DhzxActivity.this,
								SharepreferenceUtil.fileName,
								"yinyuan",
								userInfoComm.getBody().getYinyuan());
					}
				});
	}

	/**
	 * 兑换奖品
	 */
	private void dhPrize(final String type, String num) {
		if (SharepreferenceUtil.readString(DhzxActivity.this,
				SharepreferenceUtil.fileName, "uid", "").equals("")) {
			Intent loginintent = new Intent(DhzxActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(
				DhzxActivity.this, SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("type", type);
		cellComAjaxParams.put("num", num);
		HttpHelper.getInstances(DhzxActivity.this).send(FlowConsts.YYW_DUIHUAN,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onSuccess(CellComAjaxResult cellComAjaxResult) {
				// TODO Auto-generated method stub
				DismissProgressDialog();
				final ZyzPrizeComm zyzPrizeComm = cellComAjaxResult
						.read(ZyzPrizeComm.class,
								CellComAjaxResult.ParseType.GSON);
				if (!FlowConsts.STATUE_1.equals(zyzPrizeComm
						.getReturnCode())) {
					Toast.makeText(DhzxActivity.this,
							zyzPrizeComm.getReturnMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(type.equals("1")){
					et_num1.setText("");
				}else if(type.equals("2")){
					et_num2.setText("");
				}else if(type.equals("3")){
					et_num3.setText("");
				}
				getGrzl();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (CommonUtils.getCurrentChildMenuActivity().equals("dhzx")) {
			CommonUtils.setCurrentChildMenuActivity("");
		}
	}
}
