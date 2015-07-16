package osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyJazzyPagerAdapter extends PagerAdapter {
	private List<View> viewPagerList;
	private JazzyViewPager mJazzy;
	public MyJazzyPagerAdapter(List<View> viewpagerlist, JazzyViewPager jazzy) {
		this.viewPagerList = viewpagerlist;
		this.mJazzy = jazzy;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.addView(viewPagerList.get(position));
		mJazzy.setObjectForPosition(viewPagerList.get(position), position);
		return viewPagerList.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		container.removeView(viewPagerList.get(position));
	}

	@Override
	public int getCount() {
		return viewPagerList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}
}