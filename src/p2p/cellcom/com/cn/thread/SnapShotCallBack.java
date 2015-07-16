package p2p.cellcom.com.cn.thread;

import java.io.File;
import java.util.List;

public interface SnapShotCallBack {

	public abstract void onStart();

	public abstract void onSuccess(List<File> list);
}
