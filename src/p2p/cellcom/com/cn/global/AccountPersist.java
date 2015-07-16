package p2p.cellcom.com.cn.global;

import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.utils.MusicManger;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

//save account data
public class AccountPersist {
	public static final String ACCOUNT_SHARED_PREFERENCE = "account_shared";
	public static AccountPersist manager = null;

	private AccountPersist() {
	}

	public synchronized static AccountPersist getInstance() {
		if (null == manager) {
			synchronized (MusicManger.class) {
				if (null == manager) {
					manager = new AccountPersist();
				}
			}
		}
		return manager;
	}

	public static String mACCOUNTPWD = "account_pwd";
	public static final String ACC_INFO_3C = "account_info_3c";
	public static final String ACC_INFO_PHONE = "account_info_phone";
	public static final String ACC_INFO_EMAIL = "account_info_email";
	public static final String ACC_INFO_SESSION_ID = "account_info_session_id";
	public static final String ACC_INFO_COUNTRY_CODE = "account_info_country_code";
	public static final String CODE1 = "code1";
	public static final String CODE2 = "code2";
	public static final String ACTIVE = "active";

	public void setActiveAccount(Context context, Account account) {

		SharedPreferences preference = context.getSharedPreferences(
				ACCOUNT_SHARED_PREFERENCE, context.MODE_PRIVATE);
		Editor editor = preference.edit();
		Log.e("AccountPersist", "context="+context.getClass());
		Log.e("AccountPersist", "setaccount.three_number="+account.three_number);
		Log.e("AccountPersist", "setaccount.phone="+account.phone);
		Log.e("AccountPersist", "setaccount.email="+account.email);
		Log.e("AccountPersist", "setaccount.sessionId="+account.sessionId);
		Log.e("AccountPersist", "setaccount.rCode1="+account.rCode1);
		Log.e("AccountPersist", "setaccount.rCode2="+account.rCode2);
		Log.e("AccountPersist", "setaccount.countryCode="+account.countryCode);
		editor.putString(ACC_INFO_3C, account.three_number);
		editor.putString(ACC_INFO_PHONE, account.phone);
		editor.putString(ACC_INFO_EMAIL, account.email);
		editor.putString(ACC_INFO_SESSION_ID, account.sessionId);
		editor.putString(CODE1, account.rCode1);
		editor.putString(CODE2, account.rCode2);
		editor.putString(ACC_INFO_COUNTRY_CODE, account.countryCode);
		editor.commit();
	}

	public Account getActiveAccountInfo(Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				ACCOUNT_SHARED_PREFERENCE, context.MODE_PRIVATE);
		Editor editor = preference.edit();
		String three_number = preference.getString(ACC_INFO_3C, "");
		String phone = preference.getString(ACC_INFO_PHONE, "");
		String email = preference.getString(ACC_INFO_EMAIL, "");
		String sessionId = preference.getString(ACC_INFO_SESSION_ID, "");
		String code1 = preference.getString(CODE1, "");
		String code2 = preference.getString(CODE2, "");
		String countryCode = preference.getString(ACC_INFO_COUNTRY_CODE, "");
		editor.commit();
		Log.e("AccountPersist", "getaccount.three_number="+three_number);
		Log.e("AccountPersist", "getaccount.phone="+phone);
		Log.e("AccountPersist", "getaccount.email="+email);
		Log.e("AccountPersist", "getaccount.sessionId="+sessionId);
		Log.e("AccountPersist", "getaccount.rCode1="+code1);
		Log.e("AccountPersist", "getaccount.rCode2="+code2);
		Log.e("AccountPersist", "getaccount.countryCode="+countryCode);
		if (three_number.equals("") ||three_number.equalsIgnoreCase("null")) {
			return null;
		} else {
			return new Account(three_number, email, phone, sessionId, code1,
					code2, countryCode);
		}
	}

}
