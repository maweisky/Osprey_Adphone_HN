package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.broadcast.MyPhoneBroadcastReceiver;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.LoopJazzyPagerAdapter;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

/**
 * 电话煲 拨号页面
 * 
 * @author ma
 * 
 */
public class DhbBhpCallActivity extends Activity {
	public final static String TAG = "osprey_adphone_hn.cellcom.com.cn.activity.dhb.MyPhone";
	public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合
	private int currentItem = 0;// 当前索引
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private List<Adv> advs;
	private TextView phonetv;
	private TextView nametv;
//	private boolean iscall = true;
	private String phone;
	private String cname;
	private FinalBitmap finalBitmap;
//	private MyCount myCount;
	private LoopJazzyPagerAdapter jazzyPagerAdapter;
	private MyPhoneBroadcastReceiver myPhoneBroadcastReceiver;
	private MediaPlayer player;
	private ImageView returniv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.os_dhb_bh_activity);
		initView();
		initData();
		initListener();
		callphone();
	}

	// 按钮1-注册广播
	public void registerThis() {
		myPhoneBroadcastReceiver = new MyPhoneBroadcastReceiver(
				DhbBhpCallActivity.this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(B_PHONE_STATE);
		intentFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(myPhoneBroadcastReceiver, intentFilter);
	}

	// 按钮2-撤销广播
	public void unregisterThis() {
		unregisterReceiver(myPhoneBroadcastReceiver);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
//			myCount = new MyCount(3000, 1000);
//			myCount.start();
			startMediaPlayer(R.raw.calling);
		}
	}

	/**
	 * 拨号声音
	 * 
	 * @param res
	 */
	public void startMediaPlayer(int res) {
		player = MediaPlayer.create(this, res);
		player.setLooping(false);
		player.start();
	}

	public void stopMediaPlayer() {
		// TODO Auto-generated method stub
		if (player != null && player.isPlaying()) {
			player.stop();
		}
	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
//			if (iscall) {
//				callphone();
//			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		phonetv = (TextView) findViewById(R.id.phonetv);
		nametv=(TextView)findViewById(R.id.nametv);
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		// mJazzy.setFadeEnabled(true);
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		returniv=(ImageView)findViewById(R.id.returniv);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		returniv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (myCount != null) {					
//					myCount.cancel();
//				}
				DhbBhpCallActivity.this.finish();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		phone = getIntent().getStringExtra("phone");
		cname = getIntent().getStringExtra("cname");
		phonetv.setText(phone);
		nametv.setText(cname);
		finalBitmap = FinalBitmap.create(DhbBhpCallActivity.this);
		view_img = new ArrayList<View>();
		jazzyPagerAdapter = new LoopJazzyPagerAdapter(view_img, mJazzy, true);
		mJazzy.setAdapter(jazzyPagerAdapter);
		getAdv();
		registerThis();
	}

	// 从资源中获取Bitmap
	public Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	private void getAdv() {
		mJazzy.setVisibility(View.GONE);
		String uid = SharepreferenceUtil.readString(DhbBhpCallActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("pos", "8");
		HttpHelper.getInstances(DhbBhpCallActivity.this).send(
				FlowConsts.YYW_GETGG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(DhbBhpCallActivity.this,
									advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						advs = advComm.getBody();
						initJazzViewPager();
						mJazzy.setVisibility(View.VISIBLE);
					}
				});
	}

	private void callphone() {
		String uid = SharepreferenceUtil.readString(DhbBhpCallActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("callname", cname);
		HttpHelper.getInstances(DhbBhpCallActivity.this).send(
				FlowConsts.YYW_CALLPHONE, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {
					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
//						stopMediaPlayer();
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
//						stopMediaPlayer();
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(DhbBhpCallActivity.this,
									advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = getLayoutInflater().inflate(
						R.layout.os_dhb_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setScaleType(ScaleType.FIT_XY);
				img.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img.setImageResource(R.drawable.os_dhb_callbg);
				finalBitmap.display(img, advs.get(i).getMeitiurl(), BitmapFactory.decodeResource(getResources(), R.drawable.os_dhb_callbg));
//				finalBitmap.display(img, advs.get(i).getMeitiurl());
				view_img.add(view);
			}
		}
		if (view_img.size() > 0) {
			if (view_img.size() == 1) {
				mJazzy.setFadeEnabled(false);
			} else {
				mJazzy.setFadeEnabled(true);
			}
			jazzyPagerAdapter.setList(view_img);
			// mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(0);
		}
		// 创建定时器
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		// 首次启动时3秒后开始执行，接下来3秒执行一次
		scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 1, 1,
				TimeUnit.SECONDS);
	}

	/**
	 * 
	 * 创建自动滚动广告线程
	 * 
	 */
	class ViewpagerTask implements Runnable {
		@Override
		public void run() {
			synchronized (mJazzy) {
				if (view_img.size() > 1) {
					currentItem = (currentItem + 1)
							% jazzyPagerAdapter.getCount();
					handler.sendEmptyMessage(0);
				}
			}
		}
	}

	/**
	 * 
	 * 更新广告图片
	 * 
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 根据viewpager里图片的 角标设置当前要显示的图片
			mJazzy.setCurrentItem(currentItem);
		}
	};

	protected void onPause() {
		super.onPause();
//		if (myCount != null) {
//			myCount.cancel();
//		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
		DhbBhpCallActivity.this.finish();
		stopMediaPlayer();
		unregisterThis();
	}
}