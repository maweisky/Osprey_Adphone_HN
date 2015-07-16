package osprey_adphone_hn.cellcom.com.cn.zxing.activity;

import java.io.IOException;
import java.util.Vector;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.zxing.camera.CameraManager;
import osprey_adphone_hn.cellcom.com.cn.zxing.decoding.CaptureActivityHandler;
import osprey_adphone_hn.cellcom.com.cn.zxing.decoding.CaptureAddActivityHandler;
import osprey_adphone_hn.cellcom.com.cn.zxing.decoding.InactivityTimer;
import osprey_adphone_hn.cellcom.com.cn.zxing.view.ViewfinderView;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class CaptureAddActivity extends ActivityFrame implements Callback {
	private LinearLayout llAppBack, llAppSet;

	private CaptureAddActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.app_add_capture);
		HideHeadBar();
		initView();
		initListener();
		initData();

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// ��ʼ�� CameraManager
		CameraManager.init(getApplication());
		llAppBack = (LinearLayout) findViewById(R.id.llAppBack);
		llAppSet = (LinearLayout) findViewById(R.id.llAppSet);
		llAppSet.setVisibility(View.GONE);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		isShowSlidingMenu(false);
		inactivityTimer = new InactivityTimer(this);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		 llAppSet.setOnClickListener(new onSetListener());
		llAppBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		SetTopBarTitle(getString(R.string.app_add_capture_smewm));
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * �������һϵ�г�ʼ�����view�Ĺ��
		 */
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		/**
		 * ������ǿ����Ƿ����������ģʽ������ǾͲ�����������ǣ��򲻲���
		 */
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		/**
		 * ��ʼ������
		 */
		initBeepSound();
		/**
		 * �Ƿ���
		 */
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * �ص����ص�����̣߳����looper������message
		 */
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();// �ص����
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * ��ʼ�����
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			/**
			 * �½�������handler
			 */
			handler = new CaptureAddActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/**
		 * ��ʼ�����
		 */
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	/**
	 * ������ʾ��view
	 */
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	/**
	 * ���ش���������handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * ���view����ǰɨ��ɹ���ͼƬ
	 */
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		/**
		 * ���¼�ʱ
		 */
		inactivityTimer.onActivity();
		/**
		 * �������Ƶ�view��
		 */
		// viewfinderView.drawResultBitmap(barcode);
		/**
		 * ����jeep����
		 */
		playBeepSoundAndVibrate();
		/**
		 * ��ʾ�����ַ�
		 */
		if (!obj.getText().toString().equals("")) {
			// 处理扫描结果
			Intent data = new Intent();
			data.putExtra("deviceId", obj.getText().toString());
			setResult(RESULT_OK, data);
			finish();
		} else {
			// showCrouton("您扫描的二维码无效");
			Toast.makeText(CaptureAddActivity.this, "您扫描的二维码无效",
					Toast.LENGTH_SHORT).show();
		}

		// Intent intent = new Intent(CaptureActivity.this,
		// IndexActivity.class);
		// this.startActivity(intent);
		// this.finish();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	/**
	 * ��ʱ��
	 */
	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		/**
		 * ��������
		 */
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		/**
		 * ��
		 */
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}