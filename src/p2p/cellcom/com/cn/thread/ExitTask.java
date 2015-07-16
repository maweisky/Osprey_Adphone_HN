package p2p.cellcom.com.cn.thread;

import p2p.cellcom.com.cn.bean.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.p2p.core.network.NetManager;

public class ExitTask extends AsyncTask {
	Account account;
	Context mContext;
	ExitCallBack callBack;

	public ExitTask(Context mContext, Account account, ExitCallBack callBack) {
		this.account = account;
		this.mContext = mContext;
		this.callBack = callBack;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		if(account!=null){	
			return NetManager.getInstance(mContext).exit_application(
					account.three_number, account.sessionId);
		}else{
			return 0;
		}
	}

	public static void startTask(Context context, Account account,
			ExitCallBack callBack) {
		new ExitTask(context, account, callBack).execute();
	}

	@Override
	protected void onPostExecute(Object object) {
		// TODO Auto-generated method stub
		int result = (Integer) object;
		callBack.onPostExecute(result);
	}

	public interface ExitCallBack {
		public abstract void onPostExecute(int result);
	}
}