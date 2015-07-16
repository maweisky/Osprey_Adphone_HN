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

public class ActionShareSheet {

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionShareSheet() {
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
				R.layout.actionsharesheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		final TextView savepic_tv = (TextView) layout
				.findViewById(R.id.savepic_tv);
		final TextView share_tv = (TextView) layout
				.findViewById(R.id.share_tv);
		final TextView cancel=(TextView)layout.findViewById(R.id.cancel);
		savepic_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 拍照
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});

		share_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 选择本地照片
				actionSheetSelected.onClick(2);
				dlg.dismiss();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
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
