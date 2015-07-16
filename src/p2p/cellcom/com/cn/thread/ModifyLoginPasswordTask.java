package p2p.cellcom.com.cn.thread;

import org.json.JSONObject;

import p2p.cellcom.com.cn.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;

import com.p2p.core.network.ModifyLoginPasswordResult;
import com.p2p.core.network.NetManager;

public class ModifyLoginPasswordTask extends AsyncTask {
	String threeNum;
	String sessionId;
	String oldPwd;
	String newPwd;
	String rePwd;
	Context context;
	ModifyLoginPasswordCallBack callBack;

	public ModifyLoginPasswordTask(Context context, String threeNum,
			String sessionId, String oldPwd, String newPwd, String rePwd,
			ModifyLoginPasswordCallBack callBack) {
		this.threeNum = threeNum;
		this.sessionId = sessionId;
		this.oldPwd = oldPwd;
		this.newPwd = newPwd;
		this.rePwd = rePwd;
		this.context = context;
		this.callBack = callBack;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Utils.sleepThread(1000);
		return NetManager.getInstance(context).modifyLoginPassword(threeNum,
				sessionId, oldPwd, newPwd, rePwd);
	}

	public static void startTask(Context context, String threeNum,
			String sessionId, String oldPwd, String newPwd, String rePwd,
			ModifyLoginPasswordCallBack callBack) {
		new ModifyLoginPasswordTask(context, threeNum, sessionId, oldPwd,
				newPwd, rePwd, callBack).execute();
	}

	@Override
	protected void onPostExecute(Object object) {
		// TODO Auto-generated method stub
		ModifyLoginPasswordResult result = NetManager
				.createModifyLoginPasswordResult((JSONObject) object);
		callBack.onPostExecute(result);
	}

	public interface ModifyLoginPasswordCallBack {
		public abstract void onPostExecute(ModifyLoginPasswordResult result);
	}

}