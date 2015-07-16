package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import osprey_adphone_hn.cellcom.com.cn.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CshFwxxActivity extends Fragment {
	private CshFragmentActivity act;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		act = (CshFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_csh_fwxx_activity, container,
				false);
		initView(v, savedInstanceState);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {

	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
	  
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}
