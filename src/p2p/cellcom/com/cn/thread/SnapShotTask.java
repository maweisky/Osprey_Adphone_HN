package p2p.cellcom.com.cn.thread;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Environment;

public class SnapShotTask extends AsyncTask<Integer, Integer, Integer>{
	
	private List<File> list;
	private SnapShotCallBack callBack;
	public SnapShotTask(SnapShotCallBack callBack) {
		// TODO Auto-generated constructor stub
		this.list = new ArrayList<File>();
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callBack.onStart();
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		String path = Environment.getExternalStorageDirectory().getPath()+"/screenshot";
		File file = new File(path);
		FileFilter filter = new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.getName().endsWith(".jpg")){
					return true;
				}else{
					return false;
				}				
			}
		};
		File[] data = file.listFiles(filter);
		if(null==data){
			data = new File[0];
		}else{
			for (int i = 0; i < data.length; i++) {
				list.add(data[i]);
			}
		}
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callBack.onSuccess(list);
	}
}
