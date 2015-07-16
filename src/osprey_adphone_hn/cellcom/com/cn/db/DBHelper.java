package osprey_adphone_hn.cellcom.com.cn.db;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.bean.DeviceDB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
	private static int version = 2;
	private static FinalDb finalDb;
	private static final String DEVICE_DATA = "DEVICE_DATA";
	private static final String DEVICE_TABLE = "Device_Table";
	private static final String SNAPSHOT_TABLE = "SnapShot_Table";
	private static final String RECORDVIDEO_TABLE = "RecordVideo_Table";
	private static final String CALLTLOG_TABLE = "CallLog_Table";
	private static final String KYKINFO_TABLE = "Kykinfo_Table";
	private static final String KYKINFO_TABLE2 = "Kykinfo_Table2";
	private static final String JSHPROVINCE = "jshprovince";
	private static final String JSHCITYINFO = "jshcityinfo";
	private static final String RECORDINFO = "record_info";

	public static synchronized FinalDb getIntance(Context context) {
		if (null == finalDb) {
			finalDb = FinalDb.create(context, DEVICE_DATA, false, version,
					new DbUpdateListener() {

						@Override
						public void onUpgrade(SQLiteDatabase db,
								int oldVersion, int newVersion) {
							// TODO Auto-generated method stub
						}
					});
			 finalDb.findAllByWhere(DeviceDB.class, " account = '18388231406' ");
		}
		return finalDb;
	}
}
