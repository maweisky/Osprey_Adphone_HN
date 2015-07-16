package osprey_adphone_hn.cellcom.com.cn.activity.welcome;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;

public class GuideActivity extends ActivityFrame implements OnGestureListener,
		OnClickListener {

	private ViewFlipper viewFlipper;
	private GestureDetector gestureDetector;
	private Button btnNext1;
	private Button btnPre2;
	private Button btnNext2;
	private Button btnPre3;
	private Button btnNext3;
	private Button btnPre4;
	private Button btnNext4;
	private Button btnPre5;
	private Button btnBegin;

	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureDetector = new GestureDetector(this);
		AppendMainBody(R.layout.app_welcome_guide);
		HideHeadBar();
		InitView();
		InitData();
		InitListener();
		// btnNext1 = (Button) findViewById(R.id.btn_next1);
		// btnPre2 = (Button) findViewById(R.id.btn_pre2);
		// btnNext2 = (Button) findViewById(R.id.btn_next2);
		// btnPre3 = (Button) findViewById(R.id.btn_pre3);
		// btnNext3 = (Button) findViewById(R.id.btn_next3);
		// btnPre4 = (Button) findViewById(R.id.btn_pre4);

		// btnPre5 = (Button) findViewById(R.id.btn_pre5);
		// btnBegin = (Button) findViewById(R.id.btn_begin);

		// btnNext1.setOnClickListener(this);
		// btnPre2.setOnClickListener(this);
		// btnNext2.setOnClickListener(this);
		// btnPre3.setOnClickListener(this);
		// btnNext3.setOnClickListener(this);
		// btnPre4.setOnClickListener(this);
		// btnPre5.setOnClickListener(this);
		// btnBegin.setOnClickListener(this);
	}

	private void InitListener() {
		// TODO Auto-generated method stub
		btnNext4.setOnClickListener(this);
	}

	private void InitData() {
		// TODO Auto-generated method stub
		intent = getIntent();
	}

	private void InitView() {
		// TODO Auto-generated method stub
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		btnNext4 = (Button) findViewById(R.id.btn_next4);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btn_next1:
		// case R.id.btn_next2:
		// case R.id.btn_next3:
		// case R.id.btn_next4:
		// if (viewFlipper.getCurrentView().getId() != R.id.layout5)
		// this.viewFlipper.showNext();
		// break;
		case R.id.btn_next4:
			GuideActivity.this.setResult(RESULT_OK, intent);
			GuideActivity.this.finish();
			break;
		// case R.id.btn_begin:
		// OpenActivityFinsh(LoginActivity.class);
		// break;
		// case R.id.btn_pre2:
		// case R.id.btn_pre3:
		// case R.id.btn_pre4:
		// case R.id.btn_pre5:
		// if (viewFlipper.getCurrentView().getId() != R.id.layout1)
		// viewFlipper.showPrevious();
		// break;
		// default:
		// break;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			if (viewFlipper.getCurrentView().getId() == R.id.layout4) {
				// 翻到了最后一页
				GuideActivity.this.setResult(RESULT_OK, intent);
				GuideActivity.this.finish();
			} else {
				viewFlipper.showNext();
			}
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			if (viewFlipper.getCurrentView().getId() != R.id.layout1)
				// 翻到了第一页
				viewFlipper.showPrevious();
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
