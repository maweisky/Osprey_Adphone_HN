package osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.effect;

import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyEffect;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class SlideInEffect implements JazzyEffect {
	@Override
	public void initView(View item, int position, int scrollDirection) {
		ViewHelper
				.setTranslationY(item, item.getHeight() / 2 * scrollDirection);
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		animator.translationYBy(-item.getHeight() / 2 * scrollDirection);
	}
}
