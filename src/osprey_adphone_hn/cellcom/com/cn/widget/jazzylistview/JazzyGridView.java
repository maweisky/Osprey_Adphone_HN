package osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class JazzyGridView extends GridView {

	private final JazzyHelper mHelper;
	private boolean is_AT_MOST = false;

	public JazzyGridView(Context context) {
		super(context);
		mHelper = init(context, null);
	}

	public JazzyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHelper = init(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.JazzyListView);
		is_AT_MOST = a.getBoolean(R.styleable.JazzyListView_is_at_most, false);
		a.recycle();
	}

	public JazzyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHelper = init(context, attrs);
	}

	private JazzyHelper init(Context context, AttributeSet attrs) {
		JazzyHelper helper = new JazzyHelper(context, attrs);
		super.setOnScrollListener(helper);
		return helper;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = heightMeasureSpec;
		if (is_AT_MOST) {
			expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

	/**
	 * @see android.widget.AbsListView#setOnScrollListener
	 */
	@Override
	public final void setOnScrollListener(OnScrollListener l) {
		mHelper.setOnScrollListener(l);
	}

	/**
	 * Sets the desired transition effect.
	 * 
	 * @param transitionEffect
	 *            Numeric constant representing a bundled transition effect.
	 */
	public void setTransitionEffect(int transitionEffect) {
		mHelper.setTransitionEffect(transitionEffect);
	}

	/**
	 * Sets the desired transition effect.
	 * 
	 * @param transitionEffect
	 *            The non-bundled transition provided by the client.
	 */
	public void setTransitionEffect(JazzyEffect transitionEffect) {
		mHelper.setTransitionEffect(transitionEffect);
	}

	/**
	 * Sets whether new items or all items should be animated when they become
	 * visible.
	 * 
	 * @param onlyAnimateNew
	 *            True if only new items should be animated; false otherwise.
	 */
	public void setShouldOnlyAnimateNewItems(boolean onlyAnimateNew) {
		mHelper.setShouldOnlyAnimateNewItems(onlyAnimateNew);
	}
}
