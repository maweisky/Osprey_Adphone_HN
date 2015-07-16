package osprey_adphone_hn.cellcom.com.cn.widget;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionSheet {

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionSheet() {
	}

	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param type
	 *            1.上传照片 2.预览下载
	 * @return
	 */
	public static Dialog showSheet(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, String type) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		final TextView take_picture = (TextView) layout
				.findViewById(R.id.take_picture);
		final TextView choose_local = (TextView) layout
				.findViewById(R.id.choose_local);
		final TextView tv_download = (TextView) layout
				.findViewById(R.id.tv_download);
		final TextView tv_preview = (TextView) layout
				.findViewById(R.id.tv_preview);
		final TextView delete_pic = (TextView) layout
				.findViewById(R.id.delete_pic);
		final TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		if (type.equals("1")) {
			take_picture.setVisibility(View.VISIBLE);
			choose_local.setVisibility(View.VISIBLE);
			delete_pic.setVisibility(View.GONE);
			tv_download.setVisibility(View.GONE);
			tv_preview.setVisibility(View.GONE);
		} else if (type.equals("2")) {
			take_picture.setVisibility(View.GONE);
			choose_local.setVisibility(View.GONE);
			delete_pic.setVisibility(View.GONE);
			tv_download.setVisibility(View.VISIBLE);
			tv_preview.setVisibility(View.VISIBLE);
		}
		take_picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 拍照
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});

		choose_local.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 选择本地照片
				actionSheetSelected.onClick(2);
				dlg.dismiss();
			}
		});

		tv_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 预览
				actionSheetSelected.onClick(4);
				dlg.dismiss();
			}
		});

		tv_preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 下载
				actionSheetSelected.onClick(3);
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(6);
				dlg.dismiss();
			}
		});

		delete_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(5);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
