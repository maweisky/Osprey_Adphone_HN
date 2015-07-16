package p2p.cellcom.com.cn.thread;

import org.json.JSONObject;

import p2p.cellcom.com.cn.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;

import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;

public class LoginTask extends AsyncTask {
	String username;
	String password;
	Context context;
	LoginCallBack callBack;

	public LoginTask(Context context, String username, String password,
			LoginCallBack callBack) {
		this.context = context;
		this.username = username;
		this.password = password;
		this.callBack = callBack;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Utils.sleepThread(1000);
		return NetManager.getInstance(context).login(username, password);
	}

	public static void startTask(Context context, String username,
			String password, LoginCallBack callBack) {
		new LoginTask(context, username, password, callBack).execute();
	}

	@Override
	protected void onPostExecute(Object object) {
		// TODO Auto-generated method stub
		LoginResult result = NetManager.createLoginResult((JSONObject) object);
		callBack.onPostExecute(result);
	}

	public interface LoginCallBack {
		public abstract void onPostExecute(LoginResult result);
	}

}
