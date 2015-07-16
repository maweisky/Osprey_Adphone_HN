package osprey_adphone_hn.cellcom.com.cn.widget;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class RightSlidingMenu extends HorizontalScrollView {
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuLeftPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;

	LinearLayout wrapper;

	private boolean isSliding = true;

	public RightSlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public RightSlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = this.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.RightSlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.RightSlidingMenu_leftPadding:
				// 默认50
				mMenuLeftPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
				break;
			}
		}
		a.recycle();
	}

	public RightSlidingMenu(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			wrapper = (LinearLayout) getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuLeftPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			System.out.println("mMenuWidth---->" + mMenuWidth);
			// 将菜单隐藏
			this.scrollTo(0, 0);
			once = true;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (isSliding) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}
	}

	public boolean isSliding() {
		return isSliding;
	}

	public void setSliding(boolean isSliding) {
		this.isSliding = isSliding;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isSliding){
			int action = ev.getAction();
			switch (action) {
			// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();

				if (scrollX > mHalfMenuWidth) {
					System.out.println("mMenuWidth>---->" + mMenuWidth);
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = true;
				} else {
					System.out.println("mMenuWidth<---->" + mMenuWidth);
					this.smoothScrollTo(0, 0);
					isOpen = false;
				}

				return true;
			}
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (isOpen) {
			this.smoothScrollTo(0, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状态
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
//		if(isSliding){
			float scale = l * 1.0f / mMenuWidth;
			System.out.println("scale--->" + scale);
			float leftScale = 1 - 0.2f * scale;
			float rightScale = 0.7f + scale * 0.3f;
			
			wrapper.getBackground().setAlpha((int) ((1 + scale) * 255 + 0.5));
			
			ViewHelper.setScaleX(mMenu, rightScale);
			ViewHelper.setScaleY(mMenu, rightScale);
			ViewHelper.setAlpha(mMenu, 0.1f + 0.9f * scale);
			ViewHelper.setTranslationX(mMenu, 0);
			
			ViewHelper.setPivotX(mContent, mContent.getWidth() / 2);
			ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
			ViewHelper.setScaleX(mContent, leftScale);
			// if(leftScale>=0.99999999){
			// }else{
			// ViewHelper.setScaleX(mContent, leftScale*1.15f);
			// }
			ViewHelper.setScaleY(mContent, leftScale);
//		}

	}
	
	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		// TODO Auto-generated method stub
		if(isSliding){
			return super.computeScrollDeltaToGetChildRectOnScreen(rect);
		}else{
			return 0;
		}
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

}
