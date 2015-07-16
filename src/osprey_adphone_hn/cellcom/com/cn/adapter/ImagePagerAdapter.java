package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbBhpCallActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private DhbBhpCallActivity   dhbBhpCallActivity;
    private List<Adv> advs;
    private FinalBitmap finalBitmap;
    private int           size;
    private boolean       isInfiniteLoop;

    public ImagePagerAdapter(DhbBhpCallActivity   dhbBhpCallActivity, List<Adv> advs,FinalBitmap finalBitmap) {
        this.dhbBhpCallActivity =dhbBhpCallActivity;
        this.finalBitmap=finalBitmap;
        this.advs = advs;
        this.size = advs.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : advs.size();
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = dhbBhpCallActivity.getLayoutInflater().inflate(
					R.layout.app_ad_item, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        finalBitmap.display(
        		 holder.imageView,
				advs.get(position%advs.size()).getMeitiurl()/*,
				getBitmapFromResources(DhbBhpCallActivity.this,
						R.drawable.os_dhb_bhbg)*/);
//        holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}