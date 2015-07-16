package osprey_adphone_hn.cellcom.com.cn.db;

import java.util.Iterator;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.bean.CallLogBean;
import osprey_adphone_hn.cellcom.com.cn.bean.CallLogInfo;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.content.Context;

public class DBManager {
	
	public static void saveCallLogs(Context context, List<CallLogInfo> list){
		String account = SharepreferenceUtil.readString(context,SharepreferenceUtil.fileName, "account");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			CallLogInfo callLogInfo = (CallLogInfo) iterator.next();
			callLogInfo.setAccount(account);
			DBHelper.getIntance(context).save(callLogInfo);
		}
	}
	
	public static void saveCallLog(Context context, CallLogBean callLogInfo){
		String account = SharepreferenceUtil.readString(context, SharepreferenceUtil.fileName, "account");
		callLogInfo.setAccount(account);
		DBHelper.getIntance(context).save(callLogInfo);
	}
	
	public static void deleteCallLog(Context context){
		String account = SharepreferenceUtil.readString(context, SharepreferenceUtil.fileName, "account");
		DBHelper.getIntance(context).deleteByWhere(CallLogInfo.class, "account = '" + account +"'");
	}
	
	public static List<CallLogBean> getCallLogs(Context context){
		String account = SharepreferenceUtil.readString(context, SharepreferenceUtil.fileName, "account");
		List<CallLogBean> list = DBHelper.getIntance(context).findAllByWhere(CallLogBean.class, "account = '" + account +"'");
		return list;
	}

}
