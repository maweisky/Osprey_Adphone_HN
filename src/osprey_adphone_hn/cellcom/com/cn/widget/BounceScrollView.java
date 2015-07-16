package osprey_adphone_hn.cellcom.com.cn.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * scrollview的动画效果实
 * 
 * @author tuojian
 * 
 */
public class BounceScrollView extends ScrollView {

	private final float minDis = 5f;
	private final int bounceInterval = 10;
	private static final float SCROLL_RESISTANCE = 1.2f;
	// 子layout或�?控件
	private View inner;

	// 按下时的Y坐标
	private float originY;

	// inner头部与scrollview顶部的原始距�?
	private int originHeaderPadding;
	// inner尾部与scrollview底部的原始距�?
	private int originFooterPadding;

	// inner头部与scrollview顶部的距�?
	private int headerOffset;
	// inner尾部与scrollview底部的距�?
	private int footerOffset;

	private final String Msg_Data_key = "padding";
	private final int Msg_Header_Padding = 1;
	private final int Msg_Footer_Padding = 2;
	private final int Msg_Reset_Padding = 3;

	public BounceScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BounceScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
			originHeaderPadding = inner.getPaddingTop();
			originFooterPadding = inner.getPaddingBottom();
			headerOffset = 0;
			footerOffset = 0;
			this.setScrollbarFadingEnabled(true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (inner != null)
			if (!commOnTouchEvent(ev))
				return super.onTouchEvent(ev);

		return super.onTouchEvent(ev);
	}

	public boolean commOnTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			originY = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			originY = ev.getY();
			bouceBack();
			// resetInnder();
			// if (isNeedAnimation()) {
			// animation();
			// }
			break;
		case MotionEvent.ACTION_MOVE:

			float nowY = ev.getY();
			int deltaY = (int) (nowY - originY);

			// 为防止抖动，每次上拉或�?下拉时给定一个初始�?，为1
			if ((deltaY != 0) && footerOffset == 0 && headerOffset == 0) {
				if (deltaY > 0)
					deltaY = 1;
				else
					deltaY = -1;
				originY = nowY - deltaY;
			}

			// 判断上拉还是下拉
			// 上拉时，顶部offset必须�?，否则会重置，反之亦�?
			if (deltaY > 0 && footerOffset == 0) {
				float dif = Math.abs(deltaY) - headerOffset;

				if (dif > 0)
					dif /= SCROLL_RESISTANCE;

				headerOffset += Math.round(dif);
				setHeadPadding(headerOffset);

			} else if (deltaY < 0 && headerOffset == 0) {

				float dif = Math.abs(deltaY) - footerOffset;

				if (dif > 0)
					dif /= SCROLL_RESISTANCE;

				footerOffset += Math.round(dif);
				setFootPadding(footerOffset);
			} else {
				// 此处为onscrllview内部滑动事件
				resetInnder();
			}
			return true;

		default:
			break;
		}

		return false;
	}

	/**
	 * mainly setting the padding top
	 */
	private void setHeadPadding(int padding) {

		headerOffset = padding;
		inner.setPadding(0, originHeaderPadding + headerOffset, 0,
				inner.getPaddingBottom());
	}

	/**
	 * mainly setting the padding top
	 */
	private void setFootPadding(int padding) {

		footerOffset = padding;

		inner.setPadding(0, inner.getPaddingTop(), 0, originFooterPadding
				+ footerOffset);
	}

	private void resetInnder() {
		headerOffset = 0;
		footerOffset = 0;
		inner.setPadding(0, originHeaderPadding, 0, originFooterPadding);
	}

	private Handler backHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Msg_Header_Padding:
				setHeadPadding(msg.getData().getInt(Msg_Data_key));
				break;
			case Msg_Footer_Padding:
				setFootPadding(msg.getData().getInt(Msg_Data_key));
				break;
			case Msg_Reset_Padding:
				resetInnder();
				break;
			}
		}
	};

	public void bouceBack() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean foot = false;
				int padding = 0;
				if (footerOffset > minDis) {
					padding = footerOffset;
					foot = true;
				} else if (headerOffset > minDis) {
					padding = headerOffset;
					foot = false;
				}
				while (padding > minDis) {

					padding = Math.round(padding / SCROLL_RESISTANCE);

					Message msg = new Message();
					Bundle bund = new Bundle();
					bund.putInt(Msg_Data_key, padding);
					msg.setData(bund);

					if (foot)
						msg.what = Msg_Footer_Padding;
					else
						msg.what = Msg_Header_Padding;

					backHandler.sendMessage(msg);
					try {
						Thread.sleep(bounceInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				backHandler.sendEmptyMessage(Msg_Reset_Padding);
			}
		}).start();
	}

}
