package osprey_adphone_hn.cellcom.com.cn.activity.base;

import java.lang.reflect.Field;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class FragmentBase extends Fragment {

	protected static final int SHOW_TIME = 1;
	private Activity activity;
	private CustomProgressDialog m_ProgressDialog;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	public void ShowMsg(String pMsg) {
		Toast.makeText(activity, pMsg, SHOW_TIME).show();
	}

	public void ShowMsg(int p_ResID) {
		Toast.makeText(activity, p_ResID, SHOW_TIME).show();
	}

	protected void OpenActivity(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivity(_Intent);
	}

	protected void OpenActivityForResult(Class<?> pClass, int requestCode) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivityForResult(_Intent, requestCode);
	}

	protected void OpenActivityFinsh(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivity(_Intent);
		activity.finish();
	}

	protected LayoutInflater GetLayouInflater() {
		LayoutInflater _LayoutInflater = LayoutInflater.from(activity);
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
		final Dialog dialog = new Dialog(activity, R.style.loadingdialog);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.app_alertdialog_popup, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView iv_popup_title=(TextView)layout.findViewById(R.id.iv_popup_title);
		final TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
		final TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
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
	}
	
	protected Dialog ShowAlertDialogCsh(String p_Title, String p_Message,
			final MyDialogInterface p_ClickListener) {
		final Dialog dialog = new Dialog(activity, R.style.loadingdialog);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.app_alertdialogcsh_popup, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView iv_popup_title=(TextView)layout.findViewById(R.id.iv_popup_title);
		final TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
		final TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
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
	}

	protected Dialog ShowViewAlertDialog(String p_Title, View view,
			final MyDialogInterface p_ClickListener) {
		final Dialog dialog = new Dialog(activity, R.style.loadingdialog);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.app_alertdialog_popup, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView iv_popup_title=(TextView)layout.findViewById(R.id.iv_popup_title);
		final LinearLayout ll_content = (LinearLayout) layout.findViewById(R.id.ll_content);
		final TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
		final TextView tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		iv_popup_title.setText(p_Title);
		ll_content.removeAllViews();
		view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
	}

	public CustomProgressDialog ShowProgressDialog(int p_MessageResID) {
		if(m_ProgressDialog==null ){
			m_ProgressDialog = CustomProgressDialog.createDialog(activity);
			m_ProgressDialog.setMessage(getString(p_MessageResID));
			m_ProgressDialog.setCanceledOnTouchOutside(false);
			m_ProgressDialog.show();
			Log.e("FragmentBase", "ShowProgressDialog m_ProgressDialog-->"+m_ProgressDialog);
			Log.e("FragmentBase", "FragmentBase-->"+FragmentBase.this);
		}
		if(m_ProgressDialog!=null && !m_ProgressDialog.isShowing()){
			m_ProgressDialog.setMessage(getString(p_MessageResID));
			m_ProgressDialog.setCanceledOnTouchOutside(false);
			m_ProgressDialog.show();
			m_ProgressDialog.animationDrawable.start();
			Log.e("FragmentBase", "ShowProgressDialog m_ProgressDialog-->"+m_ProgressDialog);
			Log.e("FragmentBase", "FragmentBase-->"+FragmentBase.this);
		}
		return m_ProgressDialog ;
	}

	public void DismissProgressDialog() {
		Log.e("FragmentBase", "DismissProgressDialog m_ProgressDialog-->"+m_ProgressDialog);
		Log.e("FragmentBase", "FragmentBase-->"+FragmentBase.this);
		if (m_ProgressDialog != null) {
			if(m_ProgressDialog.animationDrawable!=null){
				m_ProgressDialog.animationDrawable.stop();
			}
			m_ProgressDialog.dismiss();
		}
	}
}