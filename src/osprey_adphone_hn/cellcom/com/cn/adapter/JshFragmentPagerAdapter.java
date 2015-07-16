package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class JshFragmentPagerAdapter extends FragmentPagerAdapter{
	ArrayList<Fragment> list;
	public JshFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}
}