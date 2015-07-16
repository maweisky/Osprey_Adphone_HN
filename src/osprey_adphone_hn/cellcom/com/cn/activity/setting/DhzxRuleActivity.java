package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import android.os.Bundle;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;

public class DhzxRuleActivity extends ActivityFrame{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhzx_rule_activity);
		AppendTitleBody1();
		SetTopBarTitle("兑换规则");
	}
}
