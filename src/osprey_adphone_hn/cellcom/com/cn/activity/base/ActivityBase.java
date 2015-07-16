package osprey_adphone_hn.cellcom.com.cn.activity.base;

import java.lang.reflect.Field;

import com.umeng.analytics.MobclickAgent;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.LoadProgressDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ActivityBase extends FragmentActivity {
	private boolean isRun;
	protected static final int SHOW_TIME = 1;
	private CustomProgressDialog m_ProgressDialog;
	private LoadProgressDialog loadProgressDialog;

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public void ShowMsg(String pMsg) {
		Toast.makeText(this, pMsg, SHOW_TIME).show();
	}

	public void ShowMsg(int p_ResID) {
		Toast.makeText(this, p_ResID, SHOW_TIME).show();
	}

	protected void OpenActivity(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
	}

	protected void OpenActivityForResult(Class<?> pClass, int requestCode) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivityForResult(_Intent, requestCode);
	}

	protected void OpenActivityFinsh(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
		this.finish();
	}

	protected LayoutInflater GetLayouInflater() {
		LayoutInflater _LayoutInflater = LayoutInflater.from(this);
		return _LayoutInflater;
	}

	public void SetAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field _Field = pDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			_Field.setAccessible(true);
			_Field.set(pDialog, pIsClose);
		} catch (Exception e) {
		}
	}

	protected Dialog ShowAlertDialog(int p_TitelResID, String p_Message,
			final MyDialogInterface p_ClickListener) {
		String _Title = getResources().getString(p_TitelResID);
		return ShowAlertDialog(_Title, p_Message, p_ClickListener);
	}

	protected Dialog ShowAlertDialog(String p_Title, String p_Message,
			final MyDialogInterface p_ClickListener) {
		if(isRun){
			final Dialog dialog = new Dialog(this, R.style.loadingdialog);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.app_alertdialog_popup, null);
			final int cFullFillWidth = 10000;
			layout.setMinimumWidth(cFullFillWidth);
			final TextView iv_popup_title = (TextView) layout
					.findViewById(R.id.iv_popup_title);
			final TextView tv_content = (TextView) layout
					.findViewById(R.id.tv_content);
			final TextView tv_cancel = (TextView) layout
					.findViewById(R.id.tv_cancel);
			final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
			iv_popup_title.setText(p_Title);
			tv_content.setText(p_Message);
			tv_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					p_ClickListener.onClick(dialog);
					dialog.dismiss();
				}
			});

			Window w = dialog.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.CENTER;
			dialog.onWindowAttributesChanged(lp);
			dialog.setCanceledOnTouchOutside(true);

			dialog.setContentView(layout);
			dialog.show();

			return dialog;
		}else{
			return null;
		}
	}

	protected Dialog ShowAlertDialog(String p_Title, String p_Message,
			final MyDialogInterface p_ClickListener,
			final MyDialogInterface c_ClickListener) {
		if(isRun){
			final Dialog dialog = new Dialog(this, R.style.loadingdialog);
			LayoutInflater inflater = (LayoutInflater) this
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
			tv_content.setText(p_Message);
			tv_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					c_ClickListener.onClick(dialog);
					dialog.dismiss();
				}
			});

			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					p_ClickListener.onClick(dialog);
					dialog.dismiss();
				}
			});

			Window w = dialog.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.CENTER;
			dialog.onWindowAttributesChanged(lp);
			dialog.setCanceledOnTouchOutside(true);

			dialog.setContentView(layout);
			dialog.show();

			return dialog;
		}else{
			return null;
		}
		
	}

	public interface MyDialogInterface {
		public void onClick(DialogInterface dialog);
	}

	// protected AlertDialog ShowViewAlertDialog(String p_Title, View view,
	// DialogInterface.OnClickListener p_ClickListener) {
	// return new AlertDialog.Builder(this).setTitle(p_Title).setView(view)
	// .setPositiveButton(R.string.app_ButtonTextYes, p_ClickListener)
	// .setNegativeButton(R.string.app_ButtonTextNo, null).show();
	// }

	protected Dialog ShowViewAlertDialog(String p_Title, View view,
			final MyDialogInterface p_ClickListener) {
		if(isRun){
			final Dialog dialog = new Dialog(this, R.style.loadingdialog);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.app_alertdialog_popup, null);
			final int cFullFillWidth = 10000;
			layout.setMinimumWidth(cFullFillWidth);
			final TextView iv_popup_title = (TextView) layout
					.findViewById(R.id.iv_popup_title);
			final LinearLayout ll_content = (LinearLayout) layout
					.findViewById(R.id.ll_content);
			final TextView tv_cancel = (TextView) layout
					.findViewById(R.id.tv_cancel);
			final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
			iv_popup_title.setText(p_Title);
			ll_content.removeAllViews();
			view.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ll_content.addView(view);
			tv_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			tv_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					p_ClickListener.onClick(dialog);
					dialog.dismiss();
				}
			});

			Window w = dialog.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.CENTER;
			dialog.onWindowAttributesChanged(lp);
			dialog.setCanceledOnTouchOutside(true);

			dialog.setContentView(layout);
			dialog.show();

			return dialog;
		}else{
			return null;
		}
		
	}

	public CustomProgressDialog ShowProgressDialog(int p_MessageResID) {
	  if(isRun){
	    if(m_ProgressDialog==null ){
          m_ProgressDialog = CustomProgressDialog.createDialog(this);
          m_ProgressDialog.setMessage(getString(p_MessageResID));
          m_ProgressDialog.setCanceledOnTouchOutside(false);
          m_ProgressDialog.show();
      }
      if(m_ProgressDialog!=null && !m_ProgressDialog.isShowing()){
          m_ProgressDialog.setMessage(getString(p_MessageResID));
          m_ProgressDialog.setCanceledOnTouchOutside(false);
          m_ProgressDialog.show();
      }
	  }
		return m_ProgressDialog;
	}

	public LoadProgressDialog ShowLoadingProgressDialog(int p_MessageResID) {
	  if(isRun){
	    loadProgressDialog = LoadProgressDialog.createDialog(this);
        loadProgressDialog.setMessage(getString(p_MessageResID));
        loadProgressDialog.setCanceledOnTouchOutside(false);
        loadProgressDialog.show();
	  }
		return loadProgressDialog;
	}

	public void DismissLoadingProgressDialog() {
		if (loadProgressDialog != null) {
			loadProgressDialog.dismiss();
		}
	}

	public void DismissProgressDialog() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		onExit();
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		onRun();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	protected void onExit() {
		// TODO Auto-generated method stub
		isRun=false;
	}

	protected void onRun() {
		// TODO Auto-generated method stub
		isRun=true;
	}
		
}
