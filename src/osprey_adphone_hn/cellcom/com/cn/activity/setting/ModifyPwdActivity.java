package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.bean.LoginComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.AESEncoding;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.MyApp;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.thread.ModifyLoginPasswordTask;
import p2p.cellcom.com.cn.thread.ModifyLoginPasswordTask.ModifyLoginPasswordCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import com.p2p.core.network.ModifyLoginPasswordResult;
import com.p2p.core.network.NetManager;

//修改密码
public class ModifyPwdActivity extends ActivityFrame {
	private EditText oldpwd;
	private EditText newpwdet;
	private EditText pwdconfirmet;
	private Button subbtn;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendTitleBody2();
		HideupdateSet();
		AppendMainBody(R.layout.os_grzl_modifypwd);
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.os_dhb_grzx_xgmm));
		InitView();
		InitData();
		InitListener();
	}

	private void InitData() {
		// TODO Auto-generated method stub
	}

	private void InitListener() {
		// TODO Auto-generated method stub
		subbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oldpwdtxt = oldpwd.getText().toString();
				String newpwdettxt = newpwdet.getText().toString();
				String pwdconfirmettxt = pwdconfirmet.getText().toString();
				if (TextUtils.isEmpty(oldpwdtxt)) {
					ShowMsg("请输入原密码");
					return;
				}
				if (TextUtils.isEmpty(newpwdettxt)) {
					ShowMsg("请输入新密码");
					return;
				}
				if (TextUtils.isEmpty(pwdconfirmettxt)) {
					ShowMsg("请再次输入新密码");
					return;
				}
				if (!newpwdettxt.equals(pwdconfirmettxt)) {
					ShowMsg("两次输入密码不一致");
					return;
				}
				modifyPwd(oldpwdtxt, newpwdettxt, oldpwdtxt);
			}
		});
	}

	private void InitView() {
		// TODO Auto-generated method stub
		oldpwd = (EditText) findViewById(R.id.oldpwd);
		newpwdet = (EditText) findViewById(R.id.newpwdet);
		pwdconfirmet = (EditText) findViewById(R.id.pwdconfirmet);
		subbtn = (Button) findViewById(R.id.subbtn);
		intent = getIntent();
	}

	private void modifyPwd(String oldpwdtxt, final String newpwdettxt,
			final String oldpwdettxt) {
		// TODO Auto-generated method stub
		String uid = SharepreferenceUtil.readString(ModifyPwdActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("oldpwd", ContextUtil.encodeMD5(oldpwdettxt));
		cellComAjaxParams.put("newpwd", ContextUtil.encodeMD5(newpwdettxt));
		HttpHelper.getInstances(ModifyPwdActivity.this).send(
				FlowConsts.YYW_MODIFYPWD, cellComAjaxParams,
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
						LoginComm modifyPwdComm = arg0.read(LoginComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = modifyPwdComm.getReturnCode();
						String msg = modifyPwdComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						try {
							SharepreferenceUtil.write(ModifyPwdActivity.this,
									SharepreferenceUtil.fileName, "pwd",
									AESEncoding.Encrypt(newpwdettxt,
											FlowConsts.key));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setResult(RESULT_OK, intent);
						ModifyPwdActivity.this.finish();
						ShowMsg("密码修改成功");
//						startTask(oldpwdettxt, newpwdettxt, newpwdettxt);
					}
				});
	}

	private void startTask(final String oldPwd, final String newPwd,
			final String rePwd) {
		// TODO Auto-generated method stub
		final Account account = AccountPersist.getInstance()
				.getActiveAccountInfo(ModifyPwdActivity.this);
		ModifyLoginPasswordTask.startTask(ModifyPwdActivity.this,
				NpcCommon.mThreeNum, account.sessionId, oldPwd, newPwd, rePwd,
				new ModifyLoginPasswordCallBack() {

					@Override
					public void onPostExecute(ModifyLoginPasswordResult result) {
						// TODO Auto-generated method stub
						switch (Integer.parseInt(result.error_code)) {
						case NetManager.SESSION_ID_ERROR:
							Intent i = new Intent();
							i.setAction(Constants.Action.SESSION_ID_ERROR);
							MyApp.app.sendBroadcast(i);
							break;
						case NetManager.CONNECT_CHANGE:
							startTask(oldPwd, newPwd, rePwd);
							return;
						case NetManager.MODIFY_LOGIN_PWD_SUCCESS:
							Account account = AccountPersist.getInstance()
									.getActiveAccountInfo(
											ModifyPwdActivity.this);
							if (null == account) {
								account = new Account();
							}
							account.sessionId = result.sessionId;
							AccountPersist.getInstance().setActiveAccount(
									ModifyPwdActivity.this, account);
							LogMgr.showLog("ModifyLoginPasswordVideo==>"
									+ ModifyPwdActivity.this
											.getResources()
											.getString(
													R.string.os_modify_pwd_success));
							Intent canel = new Intent();
							canel.setAction(Constants.Action.ACTION_SWITCH_USER);
							ModifyPwdActivity.this.sendBroadcast(canel);
							setResult(RESULT_OK, intent);
							ModifyPwdActivity.this.finish();
							ShowMsg("密码修改成功");
							break;
						case NetManager.MODIFY_LOGIN_PWD_INCONSISTENCE:
							LogMgr.showLog("ModifyLoginPasswordVideo==>"
									+ ModifyPwdActivity.this
											.getResources()
											.getString(
													R.string.os_pwd_inconsistence));
							break;
						case NetManager.MODIFY_LOGIN_PWD_OLD_PWD_ERROR:
							LogMgr.showLog("ModifyLoginPasswordVideo==>"
									+ ModifyPwdActivity.this.getResources()
											.getString(
													R.string.os_old_pwd_error));
							break;
						default:
							LogMgr.showLog("ModifyLoginPasswordVideo==>"
									+ ModifyPwdActivity.this.getResources()
											.getString(
													R.string.os_operator_error));
							break;
						}
					}
				});
	}
}