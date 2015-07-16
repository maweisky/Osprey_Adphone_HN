package osprey_adphone_hn.cellcom.com.cn.bus;

import java.io.File;

import net.tsz.afinal.http.HttpHandler;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.LogMgr;

public class DownLoadManager {
	public HttpHandler<File> downLoadApk(Context context, String path,
			String target, boolean isResume, NetCallBack<File> netCallBack) {
		return HttpHelper.getInstances(context).downLoad(path, target,
				isResume, netCallBack);
	}

	public String createApkTarget(Context context) {
		String savePath = "";
		try {
			String sdPath = getSDPath(context);
			if (sdPath == null || "".equals(sdPath)) {
				Toast.makeText(context, "SD卡不存在", Toast.LENGTH_LONG).show();
			} else {
				if (!sdPath.endsWith("/")) {
					savePath = sdPath + "/cellcom/";
				} else {
					savePath = sdPath + "cellcom/";
				}
				LogMgr.showLog("savePath=" + savePath);
				File file = new File(savePath);
				if (!file.exists()) {
					if (!file.mkdir()) {
						if (!sdPath.endsWith("/")) {
							savePath += "/";
						}
					}
				}
			}
			return savePath;
		} catch (Exception e) {
			LogMgr.showLog("createApkTarget:" + e.getMessage());
		}
		return savePath;
	}

	private String getSDPath(Context context) {
//		File sdDir = null;
//		boolean sdCardExist = Environment.getExternalStorageState().equals(
//				android.os.Environment.MEDIA_MOUNTED);
//		if (sdCardExist) {
//			sdDir = Environment.getExternalStorageDirectory();
//			return sdDir.toString();
//		} else {
//			return "";
//		}
	   String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
        .getExternalStorageState()) ? getExternalCacheDir(context)
        .getPath() : context.getCacheDir().getPath();
        return cachePath;
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

}
