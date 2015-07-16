package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class JshInfoZpjFragmentActivity extends ActivityFrame{
	
	private RadioGroup rg_zpj;
	private RadioButton rb_zpxx, rb_lxxx;
	
	private JshInfoZpxxFragment jshZpxxFragment;
	private JshInfoLxxxFragment jshLxxxFragment;
	
	private int checkId = R.id.rb_zpxx;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_info_zpj);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		HideSet();
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		rg_zpj = (RadioGroup) findViewById(R.id.rg_zpj);
		rb_zpxx = (RadioButton) findViewById(R.id.rb_zpxx);
		rb_lxxx = (RadioButton) findViewById(R.id.rb_lxxx);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		rg_zpj.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				LogMgr.showLog("checkedId------------->" + checkedId);
				LogMgr.showLog("checkId------------->" + checkId);
				if(checkedId == rb_zpxx.getId()){
					if(jshZpxxFragment == null){
						jshZpxxFragment = new JshInfoZpxxFragment();
					}
					hiddleFragment(jshLxxxFragment, "LXXX");
					showFragment(jshZpxxFragment, "ZPXX");
				}else{
					if(jshLxxxFragment == null){
						jshLxxxFragment = new JshInfoLxxxFragment();
					}
					hiddleFragment(jshZpxxFragment, "ZPXX");
					showFragment(jshLxxxFragment, "LXXX");
				}
				checkId = checkedId;
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.os_jsh_zpj));
		jshZpxxFragment = new JshInfoZpxxFragment();
		showFragment(jshZpxxFragment, "ZPXX");
//		rg_zpj.check(rb_zpxx.getId());
	}
	
	
	private void showFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		if(manager.findFragmentByTag(tag)==null){			
			transaction.add(R.id.simple_fragment,fragment, tag);
		}
		transaction.show(fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		
		transaction.commitAllowingStateLoss();
	}
	
	private void hiddleFragment(Fragment fragment, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		if(fragment!=null && manager.findFragmentByTag(tag)!=null){			
			transaction.hide(fragment);
			transaction.commitAllowingStateLoss();
		}
		
	}

}
