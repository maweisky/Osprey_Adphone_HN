package osprey_adphone_hn.cellcom.com.cn.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtil {
	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getAssets().open(fileName);
			if (is != null) {
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}

	// 判断该文件是否存在
	public static boolean isExist(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 初始化SD卡视频设备抓拍路径
	public static String initSDCardDirBySnapShot(Context ctx) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(ctx)
				.getPath() : ctx.getCacheDir().getPath();
		String account = SharepreferenceUtil.readString(ctx,
				SharepreferenceUtil.fileName, "account");
		File PHOTO_DIR = new File(cachePath + File.separator + account
				+ File.separator + "SnapShot");
		PHOTO_DIR.mkdirs();
		String savePath = PHOTO_DIR.getAbsolutePath();
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}

	// 初始化SD卡视频设备录像路径
	public static String initSDCardDirByRecordVideo(Context ctx) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(ctx)
				.getPath() : ctx.getCacheDir().getPath();
		String account = SharepreferenceUtil.readString(ctx,
				SharepreferenceUtil.fileName, "account");
		File PHOTO_DIR = new File(cachePath + File.separator + account
				+ File.separator + "RecordVideo");
		PHOTO_DIR.mkdirs();
		String savePath = PHOTO_DIR.getAbsolutePath();
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return savePath;
	}

	/**
	 * 获取程序外部的缓存目录
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	// 录像视频保存
	public static boolean writeFile(Context context, String fileName,
			byte[] data, int count) {
		if (count <= 0 || data == null) {
			return true;
		}
		String path = initSDCardDirByRecordVideo(context) + fileName;
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data, 0, count);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LogMgr.showLog("写入文件失败！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogMgr.showLog("创建文件失败！");
			return false;
		}
		return true;
	}

}
