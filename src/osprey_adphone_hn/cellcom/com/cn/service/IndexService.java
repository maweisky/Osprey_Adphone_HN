package osprey_adphone_hn.cellcom.com.cn.service;

import osprey_adphone_hn.cellcom.com.cn.bean.Sys;
import osprey_adphone_hn.cellcom.com.cn.bean.SysComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

public class IndexService extends Service {

	Context act;
	protected int Result = 0;
	String flag = "";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		act = getBaseContext();
		if (intent != null) {
			flag = intent.getStringExtra("flag");
		}
		getsysparam();
	}

	private void sendMsg(int Result, String message, String upgrademsg) {
		Intent intent = new Intent("osprey_adphone_hn.cellcom.com.cn");
		intent.putExtra("Result", Result);
		intent.putExtra("message", message);
		intent.putExtra("upgrademsg", upgrademsg);
		this.sendBroadcast(intent);
	}

	private void getsysparam() {
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETSYSDATA,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						Toast.makeText(act, strMsg, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						SysComm sysComm = cellComAjaxResult.read(SysComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(sysComm.getReturnCode())) {
							Toast.makeText(act, sysComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (sysComm.getBody().size() > 0) {
							Sys sys = sysComm.getBody().get(0);
							String serviceUrl = sys.getServiceurl();
							String service = sys.getService();
							String downloadurl = sys.getDownurl();
							String version = sys.getVersion();
							String minversion = sys.getMinversion();
							String startgg = sys.getStartgg();
							String key = sys.getKey();
							String pwd2cu = sys.getPwd2cu();
							String kfphone = sys.getKfphone();
							String areaversion = sys.getAreaversion();
							Double oldversion = Double.parseDouble(ContextUtil
									.getAppVersionName(act)[0]);
							FlowConsts.refleshIp(serviceUrl);
							SharepreferenceUtil.write(act,
									SharepreferenceUtil.fileName,
									"downloadurl", downloadurl);
							SharepreferenceUtil.write(act,
									SharepreferenceUtil.fileName, "startgg",
									startgg);
							SharepreferenceUtil.write(act,
									SharepreferenceUtil.fileName, "pwd2cu",
									pwd2cu);
							SharepreferenceUtil.write(act,
									SharepreferenceUtil.fileName, "kfphone",
									kfphone);
							SharepreferenceUtil.write(act, SharepreferenceUtil.fileName, "areaversion", areaversion);
							if (Double.parseDouble(minversion) > oldversion) {
								// 强制升级
								Result = -10;
							} else if (Double.parseDouble(version) > oldversion) {
								Result = -11;
							}
							cellcom.com.cn.util.SharepreferenceUtil.saveData(
									act, new String[][] { { "keystr", key } });
							cellcom.com.cn.util.SharepreferenceUtil.saveData(
									act, new String[][] { { "deskey", key } });
							cellcom.com.cn.util.SharepreferenceUtil.saveData(
									act,
									new String[][] { { "service", service } });
							sendMsg(Result, "", "");
						}
					}
				});
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}