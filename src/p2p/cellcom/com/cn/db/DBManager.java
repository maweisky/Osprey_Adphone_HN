package p2p.cellcom.com.cn.db;

import java.io.File;
import java.text.Bidi;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.db.DBHelper;
import osprey_adphone_hn.cellcom.com.cn.util.FileUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ImageUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.bean.DeviceDB;
import p2p.cellcom.com.cn.bean.RecordVideoDB;
import p2p.cellcom.com.cn.bean.SnapShotDB;
import android.content.Context;
import android.graphics.Bitmap;

public class DBManager {

	// 保存device的数据到本地数据库
	public static void saveDevice(Context context, Device device) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		DeviceDB deviceDB = DeviceDB.converDeviceDB(account, device);
		saveDevice(context, deviceDB);
	}

	// 保存device的数据到本地数据库
	public static void saveDevice(Context context, DeviceDB deviceDB) {
		int id = checkDevice(context, deviceDB.getDeviceid());
		if (id > 0) {
			LogMgr.showLog("update device");
			deviceDB.setId(id);
			DBHelper.getIntance(context).update(
					deviceDB,
					"account = '" + deviceDB.getAccount()
							+ "' and deviceid = '" + deviceDB.getDeviceid()
							+ "'");
		} else {
			LogMgr.showLog("save device");
			DBHelper.getIntance(context).save(deviceDB);
		}
	}

	// 从本地数据库获取device的数据
	public static DeviceDB getDeviceDB(Context context, String deviceId) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		List<DeviceDB> list = DBHelper.getIntance(context)
				.findAllByWhere(
						DeviceDB.class,
						"account = '" + account + "' and deviceid = '"
								+ deviceId + "'");
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 从本地数据库获取device的数据
	public static void deleteDevice(Context context, String deviceId) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		DBHelper.getIntance(context)
				.deleteByWhere(
						DeviceDB.class,
						"account = '" + account + "' and deviceid = '"
								+ deviceId + "'");
	}

	// 保存截图到数据库和本地
	public static int saveSnapShot(Context context, String deviceId,
			String imageName, Bitmap bitmap) {
		String path = FileUtil.initSDCardDirBySnapShot(context)
				+ File.separator;
		if (imageName.contains(".jpg")) {
			path = path + imageName;
		} else {
			path = path + imageName + ".jpg";
		}
		int result = ImageUtil.savePhotoBySnapShot(context, bitmap, 320, 240,
				path);
		if (result == 0) {
			String account = SharepreferenceUtil.readString(context,
					SharepreferenceUtil.fileName, "account");
			SnapShotDB snapShotDB = new SnapShotDB();
			snapShotDB.setAccount(account);
			snapShotDB.setDeviceId(deviceId);
			snapShotDB.setImageName(imageName);
			DBHelper.getIntance(context).save(snapShotDB);
		}
		return result;
	}

	// 保存截图到数据库和本地，可传宽高
	public static int saveSnapShot(Context context, String deviceId,
			String imageName, Bitmap bitmap, int width, int height) {
		String path = FileUtil.initSDCardDirBySnapShot(context)
				+ File.separator;
		if (imageName.contains(".jpg")) {
			path = path + imageName;
		} else {
			path = path + imageName + ".jpg";
		}
		int result = ImageUtil.savePhotoBySnapShot(context, bitmap, width,
				height, path);
		if (result == 0) {
			String account = SharepreferenceUtil.readString(context,
					SharepreferenceUtil.fileName, "account");
			SnapShotDB snapShotDB = new SnapShotDB();
			snapShotDB.setAccount(account);
			snapShotDB.setDeviceId(deviceId);
			snapShotDB.setImageName(imageName);
			DBHelper.getIntance(context).save(snapShotDB);
		}
		return result;
	}

	// 获取该帐号的截图信息
	public static List<SnapShotDB> getSnapshots(Context context) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		return DBHelper.getIntance(context).findAllByWhere(SnapShotDB.class,
				"account = '" + account + "'");
	}

	// 该帐号删除截图信息
	public static void deleteSnapShot(Context context, SnapShotDB snapShotDB) {
		DBHelper.getIntance(context).delete(snapShotDB);
		String path = FileUtil.initSDCardDirBySnapShot(context)
				+ File.separator;
		if (snapShotDB.getImageName().contains(".jpg")) {
			path = path + snapShotDB.getImageName();
		} else {
			path = path + snapShotDB.getImageName() + ".jpg";
		}
		if (FileUtil.isExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	// 保存截图到数据库和本地，可传宽高
	public static void saveRecordVideo(Context context, String deviceId,
			String videoName) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		RecordVideoDB recordVideoDB = new RecordVideoDB();
		recordVideoDB.setAccount(account);
		recordVideoDB.setDeviceId(deviceId);
		recordVideoDB.setVideoName(videoName);
		DBHelper.getIntance(context).save(recordVideoDB);
	}

	// 获取该帐号的截图信息
	public static List<RecordVideoDB> getRecordVideos(Context context) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		return DBHelper.getIntance(context).findAllByWhere(RecordVideoDB.class,
				"account = '" + account + "'");
	}

	// 该帐号删除截图信息
	public static void deleteSnapShot(Context context,
			RecordVideoDB recordVideoDB) {
		DBHelper.getIntance(context).delete(recordVideoDB);
		String path = FileUtil.initSDCardDirBySnapShot(context)
				+ File.separator;
		if (recordVideoDB.getVideoName().contains(".av")) {
			path = path + recordVideoDB.getVideoName();
		} else {
			path = path + recordVideoDB.getVideoName() + ".av";
		}
		if (FileUtil.isExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	/**
	 * 检查这个设备的数据是否保存到本地数据库
	 * 
	 * @param context
	 * @param deviceid
	 * @return 如果有则返回这一行数据的id，没有则返回-1
	 */
	public static int checkDevice(Context context, String deviceid) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		List<DeviceDB> list = DBHelper.getIntance(context)
				.findAllByWhere(
						DeviceDB.class,
						"account = '" + account + "' and deviceid = '"
								+ deviceid + "'");
		if (list == null || list.size() <= 0) {
			return -1;
		} else {
			return list.get(0).getId();
		}
	}

	public static List<DeviceDB> getDevicesByAccount(Context context) {
		String account = SharepreferenceUtil.readString(context,
				SharepreferenceUtil.fileName, "account");
		List<DeviceDB> list = DBHelper.getIntance(context).findAllByWhere(
				DeviceDB.class, "account = '" + account + "'");
		return list;
	}
}
