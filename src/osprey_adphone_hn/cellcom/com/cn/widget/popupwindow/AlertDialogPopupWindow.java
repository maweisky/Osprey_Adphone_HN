package osprey_adphone_hn.cellcom.com.cn.widget.popupwindow;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertDialogPopupWindow {
	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param text
	 *            dialog提示文本内容
	 * @param operationFlag
	 *            操作标记 用于区分不同的操作
	 * @param confirmText
	 *            确认操作标签内容
	 * @param cancelText
	 *            取消操作标签内容
	 * @return
	 */
	public static Dialog showSheet(ActivityFrame activityFrame,
			final OnActionSheetSelected actionSheetSelected, String text,
			final int operationFlag, String confirmText, String cancelText) {
		if(activityFrame.isRun()){
			final Dialog dlg = new Dialog(activityFrame, R.style.loadingdialog);
			LayoutInflater inflater = (LayoutInflater) activityFrame
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.app_alertdialog_popup, null);
			final int cFullFillWidth = 10000;
			layout.setMinimumWidth(cFullFillWidth);

			final TextView tv_content = (TextView) layout
					.findViewById(R.id.tv_content);
			final TextView tv_cancel = (TextView) layout
					.findViewById(R.id.tv_cancel);
			final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
			if (confirmText != null && !confirmText.trim().equals("")) {
				tv_ok.setText(confirmText);
			}
			if (cancelText != null && !cancelText.trim().equals("")) {
				tv_cancel.setText(cancelText);
			}
			tv_content.setText(text);
			tv_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dlg.dismiss();
					actionSheetSelected.onClick(-100);// 进行取消操作时回调值为-100
				}
			});

			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					actionSheetSelected.onClick(operationFlag);
					dlg.dismiss();
				}
			});

			Window w = dlg.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.CENTER;
			dlg.onWindowAttributesChanged(lp);
			dlg.setCanceledOnTouchOutside(true);

			dlg.setContentView(layout);
			dlg.show();

			return dlg;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param text
	 *            dialog提示文本内容
	 * @param operationFlag
	 *            操作标记 用于区分不同的操作
	 * @param confirmText
	 *            确认操作标签内容
	 * @param cancelText
	 *            取消操作标签内容
	 * @return
	 */
	public static Dialog showSheet(ActivityFrame activityFrame,
			final OnActionSheetSelected actionSheetSelected, String text,
			final int operationFlag) {
		if(activityFrame.isRun()){
			final Dialog dlg = new Dialog(activityFrame, R.style.loadingdialog);
			LayoutInflater inflater = (LayoutInflater) activityFrame
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.app_alertdialog_popup1, null);
			final int cFullFillWidth = 10000;
			layout.setMinimumWidth(cFullFillWidth);

			final TextView tv_content = (TextView) layout
					.findViewById(R.id.tv_content);
			final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
			tv_content.setText(text);

			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					actionSheetSelected.onClick(operationFlag);
					dlg.dismiss();
				}
			});

			Window w = dlg.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.CENTER;
			dlg.onWindowAttributesChanged(lp);
			dlg.setCanceledOnTouchOutside(true);

			dlg.setContentView(layout);
			dlg.show();
			return dlg;
		}else{
			return null;
		}
	}

}
