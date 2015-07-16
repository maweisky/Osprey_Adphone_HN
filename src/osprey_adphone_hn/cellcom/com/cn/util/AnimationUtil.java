package osprey_adphone_hn.cellcom.com.cn.util;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class AnimationUtil {
	/**
	 * ��ImageView
	 * 
	 * @param view
	 */
	public static void addScaleAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				0.9f, 0.8f, 0.7f, 0.8f, 0.9f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(500);
		set.start();
	}

	public static void addScaleAnimation(View view, int time) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				0.9f, 0.8f, 0.7f, 0.8f, 0.9f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(time);
		set.start();
	}

	/**
	 * 平移效果
	 * 
	 * @param view
	 */
	public static void tranXAnimation(Context context, final View view,
			int startX, int endX) {
		ObjectAnimator xAnim = ObjectAnimator.ofFloat(view, "x", startX, endX)
				.setDuration(700);
		xAnim.setInterpolator(new LinearInterpolator());
		AnimatorSet set = new AnimatorSet();
		set.playTogether(xAnim);
		set.start();
	}

	/**
	 * 淡入效果
	 * 
	 * @param view
	 */
	public static void fadeInAnimation(Context context, final View view) {
		ValueAnimator fader = (ValueAnimator) AnimatorInflater.loadAnimator(
				context, R.anim.os_view_fade_in);
		fader.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				view.getBackground()
						.setAlpha(
								(int) ((Float) animation.getAnimatedValue() * 255f + .5f));
			}
		});
		AnimatorSet set = new AnimatorSet();
		set.playTogether(fader);
		set.start();
	}

	/**
	 * 循环淡入效果
	 * 
	 * @param view
	 */
	public static void infiniteFadeInAnimation(Context context, final View view) {
		ValueAnimator fader = (ValueAnimator) AnimatorInflater.loadAnimator(
				context, R.anim.os_view_fade_in_repeat);
		fader.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				view.getBackground()
						.setAlpha(
								(int) ((Float) animation.getAnimatedValue() * 255f + .5f));
			}
		});
		AnimatorSet set = new AnimatorSet();
		set.playTogether(fader);
		set.start();
	}

	/**
	 * 淡出效果
	 * 
	 * @param view
	 */
	public static void fadeOutAnimation(Context context, final View view) {
		ValueAnimator fader = (ValueAnimator) AnimatorInflater.loadAnimator(
				context, R.anim.os_view_fade_in);
		fader.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				view.getBackground()
						.setAlpha(
								(int) ((Float) animation.getAnimatedValue() * 255f + .5f));
			}
		});
		AnimatorSet set = new AnimatorSet();
		set.playTogether(fader);
		set.start();
	}
}
