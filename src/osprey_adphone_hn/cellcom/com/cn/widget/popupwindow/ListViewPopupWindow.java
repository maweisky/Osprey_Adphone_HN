package osprey_adphone_hn.cellcom.com.cn.widget.popupwindow;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ListViewPopupWindow {
	private Context context;
	private PopupWindow popupWindow = null;
	private ListView listview;

	AddAdapter adapter;
	ItemClick itemClick;

	public ListViewPopupWindow(Context context) {
		this.context = context;
		initView();
	}

	public void setAddAdapter(AddAdapter adapter) {
		this.adapter = adapter;
		if (adapter != null) {
			adapter.addAdapter(listview);
		}
	}

	public void setItemClick(ItemClick itemClick) {
		this.itemClick = itemClick;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.os_listview_popup, null);

		// 数据列表
		listview = (ListView) view.findViewById(R.id.listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (itemClick != null) {
					itemClick.setOnItemClick(arg0, view, position, arg3);
				}
			}
		});

		if (popupWindow == null) {
			popupWindow = new PopupWindow(context);
		}
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setContentView(view);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
	}

	// 此接口可动态加载自定义适配器
	public interface AddAdapter {
		public void addAdapter(ListView listview);
	}

	// 列表点击事件
	public interface ItemClick {
		public void setOnItemClick(AdapterView<?> arg0, View view,
				int position, long arg3);
	}

	/**
	 * 显示popupwindow
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public void show(View parent, int gravity, int x, int y, int width,
			int height) {
		popupWindow.setHeight(height);
		popupWindow.setWidth(width);
		popupWindow.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 显示popupwindow
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public void show(View parent, int gravity, int x, int y) {
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		popupWindow.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 
	 * @param parent
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void showAsDropDown(View parent, int x, int y, int width, int height) {
		popupWindow.setHeight(height);
		popupWindow.setWidth(width);
		popupWindow.showAsDropDown(parent, x, y);
	}

	/**
	 * 消除popupwindow
	 */
	public void dimissPopupwindow() {
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	/**
	 * 设置窗口列表背景
	 */
	public void setPopupWindowBackground(int resId) {
		listview.setBackgroundResource(resId);
	}

}
