package osprey_adphone_hn.cellcom.com.cn.util;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.telephony.TelephonyManager;

public class PhoneUtils {
	/**
	 * 根据传入的TelephonyManager来取得系统的ITelephony实例.
	 * @param telephony
	 * @return 系统的ITelephony实例
	 * @throws Exception
	 */
	public static ITelephony getITelephony(TelephonyManager telephony) throws Exception { 
        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony"); 
        getITelephonyMethod.setAccessible(true);//私有化函数也能使用 
        return (ITelephony)getITelephonyMethod.invoke(telephony); 
    }
}
