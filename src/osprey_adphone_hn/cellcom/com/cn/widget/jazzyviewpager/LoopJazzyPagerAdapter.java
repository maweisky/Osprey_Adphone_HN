package osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class LoopJazzyPagerAdapter extends PagerAdapter {
	private List<View> viewPagerList;
	private JazzyViewPager mJazzy;
	private boolean isLoop;
	public LoopJazzyPagerAdapter(List<View> viewpagerlist, JazzyViewPager jazzy) {
		this.viewPagerList = viewpagerlist;
		this.mJazzy = jazzy;
	}
	
	public LoopJazzyPagerAdapter(List<View> viewpagerlist, JazzyViewPager jazzy,boolean isLoop) {
		this.viewPagerList = viewpagerlist;
		this.mJazzy = jazzy;
		this.isLoop=isLoop;
	}
	
	public void setList(List<View> viewpagerlist){
		this.viewPagerList=viewpagerlist;
		this.notifyDataSetChanged();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.removeView(viewPagerList.get(position%viewPagerList.size()));
		container.addView(viewPagerList.get(position%viewPagerList.size()));
		mJazzy.setObjectForPosition(viewPagerList.get(position%viewPagerList.size()), position);
		return viewPagerList.get(position%viewPagerList.size());
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		container.removeView(viewPagerList.get(position%viewPagerList.size()));
	}

	@Override
	public int getCount() {
		if(isLoop){
			if(viewPagerList.size()==1){
				return viewPagerList.size();
			}else{
				return 1000;
			}
		}else{
			return viewPagerList.size();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}
}