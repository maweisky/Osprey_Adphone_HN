package p2p.cellcom.com.cn.thread;

import org.json.JSONObject;

import p2p.cellcom.com.cn.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.p2p.core.network.NetManager;
import com.p2p.core.network.RegisterResult;

public class RegisterTask extends AsyncTask {
	private RegisterCallBack callBack;
	private Context context;
	private String VersionFlag;
	private String Email;
	private String CountryCode;
	private String PhoneNO;
	private String Pwd;
	private String RePwd;
	private String VerifyCode;
	private String IgnoreSafeWarning;

	public RegisterTask(Context context, String Email, String Pwd,
			RegisterCallBack callBack) {
		this.context = context;
		this.VersionFlag = "1";
		this.Email = Email;
		this.CountryCode = "1";
		this.PhoneNO = "";
		this.Pwd = Pwd;
		this.RePwd = Pwd;
		this.VerifyCode = "";
		this.IgnoreSafeWarning = "1";
		this.callBack = callBack;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Utils.sleepThread(1000);
		return NetManager.getInstance(context)
				.register(VersionFlag, Email, CountryCode, PhoneNO, Pwd, RePwd,
						VerifyCode, IgnoreSafeWarning);
	}

	@Override
	protected void onPostExecute(Object object) {
		// TODO Auto-generated method stub
		RegisterResult result = NetManager
				.createRegisterResult((JSONObject) object);
		callBack.onPostExecute(result);
	}

	public static void startTask(Context context, String Email, String Pwd,
			RegisterCallBack callBack) {
		new RegisterTask(context, Email, Pwd, callBack).execute();
	}

	public interface RegisterCallBack {
		public abstract void onPostExecute(RegisterResult result);
	}

}
