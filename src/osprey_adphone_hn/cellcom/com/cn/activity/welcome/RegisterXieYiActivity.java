package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.os.Bundle;

/**
 * 协议
 * 
 * @author ma
 * 
 */
public class RegisterXieYiActivity extends ActivityFrame {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// HideIcon();
		AppendTitleBody1();
		HideSet();
		AppendMainBody(R.layout.app_welcome_registerxieyi);
		isShowSlidingMenu(false);
		SetTopBarTitle(getResources().getString(R.string.hsc_welcom_registerxy));
		InitView();
		InitData();
		InitListener();
	}

	private void InitView() {
		// TODO Auto-generated method stub

	}

	private void InitData() {
		// TODO Auto-generated method stub
	}

	private void InitListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
