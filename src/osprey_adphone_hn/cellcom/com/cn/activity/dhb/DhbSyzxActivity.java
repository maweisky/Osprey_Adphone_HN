package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.csh.CshFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.JshFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.bean.TjspInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.TjspInfoResultComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ImageUtil;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu.OnMenuButtonClickListener;
import osprey_adphone_hn.cellcom.com.cn.widget.ArcMenu.OnMenuItemClickListener;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
/**
 * 商业中心
 * @author Administrator
 *
 */
public class DhbSyzxActivity extends Fragment{
	private Activity act;
	private JazzyViewPager mJazzy;
	private List<View> view_img;// 装载广告图片的集合
	private List<View> dots; // 图片标题正文的那些点
	private int[] adimgs = { R.drawable.os_dhb_syzx_ad_icon1,
			R.drawable.os_dhb_syzx_ad_icon2, R.drawable.os_dhb_syzx_ad_icon3 };
	private LinearLayout dots_ll;// 装载点的布局
	private LinearLayout.LayoutParams ll = null;
	private int currentItem = 0;// 当前索引
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FrameLayout fl_ad;
	private List<Adv> advs = new ArrayList<Adv>();

//	private RelativeLayout ll_kyk, ll_sys, ll_yyy, ll_zyz;

	private FinalBitmap finalBitmap;
	
	private ImageView iv_push, iv_bg, iv_img1, iv_img2;
	private ArcMenu arcMenu;
	private Button btn_cs, btn_zq, btn_yq, btn_cj;
	private TextView tv_hyp, tv_name1, tv_jf1, tv_name2, tv_jf2;
	private LinearLayout ll_sp1, ll_sp2;
	private int pageid = 1;
	private int totalnum = 0;
	private List<TjspInfo> list = new ArrayList<TjspInfo>();

//	private JazzyGridView mJazzyGridView;
//	private SyzxKykTypeAdapter adapter;
//	private List<SyzxKykType> syzxKykType = new ArrayList<SyzxKykType>();
//	private TextView tv_empty;

	// 加载框
//	private LinearLayout ll_loading;
//	private ImageView imageView_loading;
//	private AnimationDrawable animationDrawable;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (activity instanceof DhbFragmentActivity) {
			act = (DhbFragmentActivity) activity;
		} else if (activity instanceof JshFragmentActivity) {
			act = (JshFragmentActivity) activity;
		} else {
			act = (CshFragmentActivity) activity;
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_dhb_syzx_activity, container,	false);
		initView(v, savedInstanceState);
		initAd(v);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		initData();
	}
	

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
		finalBitmap = FinalBitmap.create(act);
		iv_push = (ImageView) v.findViewById(R.id.iv_push);
		iv_bg = (ImageView) v.findViewById(R.id.iv_bg);
		iv_img1 = (ImageView) v.findViewById(R.id.iv_img1);
		iv_img2 = (ImageView) v.findViewById(R.id.iv_img2);
		arcMenu = (ArcMenu) v.findViewById(R.id.id_arcmenu);
		btn_zq = (Button) v.findViewById(R.id.btn_zq);
		btn_cs = (Button) v.findViewById(R.id.btn_cs);
		btn_yq = (Button) v.findViewById(R.id.btn_yq);
		btn_cj = (Button) v.findViewById(R.id.btn_cj);
		tv_hyp = (TextView) v.findViewById(R.id.tv_hyp);
		tv_name1 = (TextView) v.findViewById(R.id.tv_name1);
		tv_name2 = (TextView) v.findViewById(R.id.tv_name2);
		tv_jf1 = (TextView) v.findViewById(R.id.tv_jf1);
		tv_jf2 = (TextView) v.findViewById(R.id.tv_jf2);
		ll_sp1 = (LinearLayout) v.findViewById(R.id.ll_sp1);
		ll_sp2 = (LinearLayout) v.findViewById(R.id.ll_sp2);
		mJazzy = (JazzyViewPager) v.findViewById(R.id.jazzy_viewpager);

		setArMemuPosition();
//		addPreDraw();
		
		// loadingBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.os_img_default_icon);
//		// 看一看
//		ll_kyk = (RelativeLayout) v.findViewById(R.id.ll_kyk);
//		// 扫一扫
//		ll_sys = (RelativeLayout) v.findViewById(R.id.ll_sys);
//		// 摇一摇
//		ll_yyy = (RelativeLayout) v.findViewById(R.id.ll_yyy);
//		// 转一转
//		ll_zyz = (RelativeLayout) v.findViewById(R.id.ll_zyz);
//
//		tv_empty = (TextView) v.findViewById(R.id.tv_empty);
//		mJazzyGridView = (JazzyGridView) v.findViewById(android.R.id.list);
//		mJazzyGridView.setTransitionEffect(JazzyHelper.HELIX);
//		adapter = new SyzxKykTypeAdapter(act, syzxKykType);
//		mJazzyGridView.setAdapter(adapter);
//
//		ll_loading = (LinearLayout) v.findViewById(R.id.ll_loading);
//		imageView_loading = (ImageView) v.findViewById(R.id.loadingImageView);
//		animationDrawable = (AnimationDrawable) imageView_loading
//				.getBackground();
		// ll_fdc = (LinearLayout)findViewById(R.id.ll_fdc);
		// ll_qc = (LinearLayout)findViewById(R.id.ll_qc);
		// ll_spbj = (LinearLayout)findViewById(R.id.ll_spbj);
		// ll_xjd = (LinearLayout)findViewById(R.id.ll_xjd);
		// ll_ylmr = (LinearLayout)findViewById(R.id.ll_ylmr);
		// ll_jjsh = (LinearLayout)findViewById(R.id.ll_jjsh);
		// ll_smcp = (LinearLayout)findViewById(R.id.ll_smcp);
		// ll_lycx = (LinearLayout)findViewById(R.id.ll_lycx);
		//
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// Rotate3DAnimation rotation_fdc = new Rotate3DAnimation(-90, 0,
		// ll_fdc.getWidth()/2, ll_fdc.getHeight()/2, 0.0f, false);
		// rotation_fdc.setDuration(500);
		// rotation_fdc.setFillAfter(true);
		// ll_fdc.startAnimation(rotation_fdc);
		// ll_fdc.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_qc = new Rotate3DAnimation(-90, 0,
		// ll_qc.getWidth()/2, ll_qc.getHeight()/2, 0.0f, false);
		// rotation_qc.setDuration(500);
		// rotation_qc.setFillAfter(true);
		// ll_qc.startAnimation(rotation_qc);
		// ll_qc.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_spbj = new Rotate3DAnimation(-90, 0,
		// ll_spbj.getWidth()/2, ll_spbj.getHeight()/2, 0.0f, false);
		// rotation_spbj.setDuration(500);
		// rotation_spbj.setFillAfter(true);
		// ll_spbj.startAnimation(rotation_spbj);
		// ll_spbj.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_xjd = new Rotate3DAnimation(-90, 0,
		// ll_xjd.getWidth()/2, ll_xjd.getHeight()/2, 0.0f, false);
		// rotation_xjd.setDuration(500);
		// rotation_xjd.setFillAfter(true);
		// ll_xjd.startAnimation(rotation_xjd);
		// ll_xjd.setVisibility(View.VISIBLE);
		//
		// rotation_xjd.setInterpolator(new AccelerateInterpolator());
		// // 设置监听
		// rotation_xjd.setAnimationListener(new AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// Rotate3DAnimation rotation_ylmr = new Rotate3DAnimation(-90, 0,
		// ll_ylmr.getWidth()/2, ll_ylmr.getHeight()/2, 0.0f, false);
		// rotation_ylmr.setDuration(500);
		// rotation_ylmr.setFillAfter(true);
		// ll_ylmr.startAnimation(rotation_ylmr);
		// ll_ylmr.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_jjsh = new Rotate3DAnimation(-90, 0,
		// ll_jjsh.getWidth()/2, ll_jjsh.getHeight()/2, 0.0f, false);
		// rotation_jjsh.setDuration(500);
		// rotation_jjsh.setFillAfter(true);
		// ll_jjsh.startAnimation(rotation_jjsh);
		// ll_jjsh.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_smcp = new Rotate3DAnimation(-90, 0,
		// ll_smcp.getWidth()/2, ll_smcp.getHeight()/2, 0.0f, false);
		// rotation_smcp.setDuration(500);
		// rotation_smcp.setFillAfter(true);
		// ll_smcp.startAnimation(rotation_smcp);
		// ll_smcp.setVisibility(View.VISIBLE);
		//
		// Rotate3DAnimation rotation_lycx = new Rotate3DAnimation(-90, 0,
		// ll_lycx.getWidth()/2, ll_lycx.getHeight()/2, 0.0f, false);
		// rotation_lycx.setDuration(500);
		// rotation_lycx.setFillAfter(true);
		// ll_lycx.startAnimation(rotation_lycx);
		// ll_lycx.setVisibility(View.VISIBLE);
		//
		// }
		// });
		// }
		// }, 300);

	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		//赚钱
		arcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void onClick(View view, int pos) {
				// TODO Auto-generated method stub
				LogMgr.showLog("arcMenu is menu item click! pos------->" + pos);
				if(arcMenu.isOpen()){
					iv_push.setVisibility(View.GONE);
				}else{
					iv_push.setVisibility(View.VISIBLE);
				}
				String title = "";
				String typeid = "";
				switch (pos) {
				case 1:
					title = "赚积分";
					typeid = "1";
					Intent intentZjf = new Intent(act, DhbSyzxKykActivity.class);
					intentZjf.putExtra("title", title);
					intentZjf.putExtra("typeid", typeid);
	                startActivity(intentZjf);
					break;
				case 2:
					title = "抢红包";
					typeid = "3";
					Intent intentQhb = new Intent(act, DhbSyzxQhbActivity.class);
                    intentQhb.putExtra("title", title);
                    intentQhb.putExtra("typeid", typeid);
                    startActivity(intentQhb);
					break;
				case 3:
					title = "赚话费";
					typeid = "2";
					Intent intentZhf = new Intent(act, DhbSyzxKykActivity.class);
					intentZhf.putExtra("title", title);
					intentZhf.putExtra("typeid", typeid);
	                startActivity(intentZhf);
					break;
				default:
					break;
				}
				
			}
		});
		arcMenu.setOnMenuButtonClickListener(new OnMenuButtonClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				LogMgr.showLog("arcMenu is menu button click!");
				if(arcMenu.isOpen()){
					iv_push.setVisibility(View.VISIBLE);
				}else{
					iv_push.setVisibility(View.GONE);
				}
			}
		});
		iv_push.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(arcMenu.isOpen()){
					iv_push.setVisibility(View.GONE);
					arcMenu.toggleMenu(300);
				}
			}
		});
		
		btn_yq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act, DhbSyzxShakeActivity.class);
				startActivity(intent);
			}
		});
		btn_cj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act, DhbSyzxZyzActivity.class);
				startActivity(intent);
			}
		});
		tv_hyp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((totalnum - 2 * pageid) <= 0){
//					Toast.makeText(act, "已经是最后一批特价商品了！", Toast.LENGTH_SHORT).show();
//					return;
					pageid = 1;
				}else{
					pageid++;
				}
				getTjspInfo();
			}
		});
		ll_sp1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list.size() > 0) {
					Intent intent = new Intent(act, DhbSyzxTjspDetailActivity.class);
					intent.putExtra("tjspInfo", list.get(0));
					startActivity(intent);
				}
			}
		});
		ll_sp2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list.size() > 1) {
					Intent intent = new Intent(act, DhbSyzxTjspDetailActivity.class);
					intent.putExtra("tjspInfo", list.get(1));
					startActivity(intent);
				}
			}
		});
		// 看一看
//		ll_kyk.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		// 扫一扫
//		ll_sys.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(act, CaptureActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		// 摇一摇
//		ll_yyy.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(act, DhbSyzxShakeListActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		// 转一转
//		ll_zyz.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(act, DhbSyzxZyzActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		mJazzyGridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				syzxKykType.get(position).setIfnew("N");//将本地新数据标示修改成N已读
//				adapter.notifyDataSetChanged();
//				Intent intent = new Intent(act, DhbSyzxKykActivity.class);
//				intent.putExtra("title", syzxKykType.get(position)
//						.getTypename());
//				intent.putExtra("typeid", syzxKykType.get(position).getTypeid());
//				startActivity(intent);
//			}
//		});
//
//		tv_empty.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				getAdv();
//				getKykType();
//			}
//		});

		// //房地产
		// ll_fdc.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看房地产");
		// intent.putExtra("typeid", "1");
		// startActivity(intent);
		// }
		// });
		// //汽车
		// ll_qc.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看汽车");
		// intent.putExtra("typeid", "2");
		// startActivity(intent);
		// }
		// });
		// //食品保健
		// ll_spbj.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看食品保健");
		// intent.putExtra("typeid", "3");
		// startActivity(intent);
		// }
		// });
		// //小家电
		// ll_xjd.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看小家电");
		// intent.putExtra("typeid", "4");
		// startActivity(intent);
		// }
		// });
		// //医疗美容
		// ll_ylmr.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看医疗美容");
		// intent.putExtra("typeid", "5");
		// startActivity(intent);
		// }
		// });
		// //家居生活
		// ll_jjsh.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看家居生活");
		// intent.putExtra("typeid", "6");
		// startActivity(intent);
		// }
		// });
		// //数码产品
		// ll_smcp.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看数码产品");
		// intent.putExtra("typeid", "7");
		// startActivity(intent);
		// }
		// });
		// //旅游出行
		// ll_lycx.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(act,DhbSyzxKykActivity.class);
		// intent.putExtra("title", "看一看旅游出行");
		// intent.putExtra("typeid", "8");
		// startActivity(intent);
		// }
		// });

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
//		if (ContextUtil.getHeith(act) <= 480) {
//			// if(ContextUtil.getWidth(act)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 90);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 800) {
//			// if(ContextUtil.getWidth(act)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 140);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 860) {
//			// if(ContextUtil.getWidth(this)<=480)
//		  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 150);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 960) {
//			// if(ContextUtil.getWidth(act)<=480)
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 180);
//			fl_ad.setLayoutParams(linearParams);
//		} else if (ContextUtil.getHeith(act) <= 1280) {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 200);
//			fl_ad.setLayoutParams(linearParams);
//		} else {
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
//					.getLayoutParams();
//			linearParams.height = ContextUtil.dip2px(act, 210);
//			fl_ad.setLayoutParams(linearParams);
//		}
	  LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
          .getLayoutParams();
      linearParams.width=ContextUtil.getWidth(act);
      linearParams.height = linearParams.width/2;
      fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(act, finalBitmap, mJazzy, R.drawable.os_login_topicon);
		getTjspInfo();
		getAdv();
//		getKykType();
	}

//	/**
//	 * 获取看一看类型
//	 */
//	private void getKykType() {
//		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
//		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
//				SharepreferenceUtil.fileName, "uid", ""));
//		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETKYKTYPE,
//				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
//				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {
//
//					@Override
//					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
//						// TODO Auto-generated method stub
//						SyzxKykTypeComm syzxKykTypeComm = cellComAjaxResult
//								.read(SyzxKykTypeComm.class,
//										CellComAjaxResult.ParseType.GSON);
//						if (!FlowConsts.STATUE_1.equals(syzxKykTypeComm
//								.getReturnCode())) {
//							Toast.makeText(act,
//									syzxKykTypeComm.getReturnMessage(),
//									Toast.LENGTH_SHORT).show();
//							return;
//						}
//						tv_empty.setVisibility(View.GONE);
//						ll_loading.setVisibility(View.GONE);
//						mJazzyGridView.setVisibility(View.VISIBLE);
//						syzxKykType.clear();
//						syzxKykType.addAll(syzxKykTypeComm.getBody());
//						// adapter.setInfos(syzxKykType);
//						adapter.notifyDataSetChanged();
//					}
//
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//						super.onStart();
//						if (animationDrawable != null) {
//							ll_loading.setVisibility(View.VISIBLE);
//							tv_empty.setVisibility(View.GONE);
//							mJazzyGridView.setVisibility(View.GONE);
//							animationDrawable.start();
//						}
//					}
//
//					@Override
//					public void onFailure(Throwable t, String strMsg) {
//						// TODO Auto-generated method stub
//						super.onFailure(t, strMsg);
//
//						tv_empty.setVisibility(View.VISIBLE);
//						mJazzyGridView.setVisibility(View.GONE);
//						ll_loading.setVisibility(View.GONE);
//					}
//				});
//	}

	/**
	 * 获取广告数据
	 */
	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("pos", "7");
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETGG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(act, advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						advs = advComm.getBody();
						initJazzViewPager();
					}
				});
	}
	
	private void setArMemuPosition() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(dm.widthPixels / 15, ContextUtil.dip2px(act, 15), 0, 0);
		arcMenu.setLayoutParams(lp);
	}

	/**
	 * 初始化广告
	 */
	private void initAd(View v) {
		fl_ad = (FrameLayout) v.findViewById(R.id.fl_ad);
		dots_ll = (LinearLayout) v.findViewById(R.id.ll_dot);
		ll = new LinearLayout.LayoutParams(ContextUtil.dip2px(act, 8),
				ContextUtil.dip2px(act, 8));
		ll.setMargins(ContextUtil.dip2px(act, 1.5f), 0,
				ContextUtil.dip2px(act, 1.5f), 0);
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		dots = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = act.getLayoutInflater().inflate(
						R.layout.app_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, advs.get(i).getMeitiurl());
				final int tempPos=i;
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!TextUtils.isEmpty(advs.get(tempPos).getLinkurl())){
							Intent intent = new Intent(act, WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos).getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
			}

		} else {
			for (int i = 0; i < adimgs.length; i++) {
				View view = act.getLayoutInflater().inflate(
						R.layout.app_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setBackgroundResource(adimgs[i]);
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
			}
		}
		if (view_img.size() > 0) {
			mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(0);
		}

		mJazzy.setOnPageChangeListener(new MyJazzyViewPager());
		mJazzy.setPageMargin(30);
		// 创建定时器
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		// 首次启动时3秒后开始执行，接下来3秒执行一次
		scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 3, 5,
				TimeUnit.SECONDS);
	}

	/**
	 * 
	 * 路口截图改变ViewPager监听
	 * 
	 */
	public class MyJazzyViewPager implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mJazzy.setCurrentItem(position);
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.app_dot_normal);
			dots.get(position)
					.setBackgroundResource(R.drawable.app_dot_focused);
			oldPosition = position;
		}
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
				currentItem = (currentItem + 1) % view_img.size();
				handler.sendEmptyMessage(0);
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
		super.onPause();
	}

	
	/**
	 * 获取特价商品
	 */
	private void getTjspInfo() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("pageid", "" + pageid);
		HttpHelper.getInstances(act).send(FlowConsts.YYW_SHANGPIN_DISCOUNT,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						TjspInfoResultComm tjspInfoComm = cellComAjaxResult.read(TjspInfoResultComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(tjspInfoComm.getReturnCode())) {
							Toast.makeText(act, tjspInfoComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						totalnum = tjspInfoComm.getBody().getTotalnum();
						list.clear();
						list.addAll(tjspInfoComm.getBody().getData());
						initTjspView();
						
					}
				});
	}
	
	private void initTjspView() {
		// TODO Auto-generated method stub
		ll_sp1.setVisibility(View.INVISIBLE);
		ll_sp2.setVisibility(View.INVISIBLE);
		switch (list.size()) {
		case 2:
			finalBitmap.display(iv_img2, list.get(1).getSmallpic());
			tv_name2.setText(list.get(1).getTitle());
			String price2;
			if("0".equals(list.get(1).getDiscountprice())){
				price2=list.get(1).getJifen();
			}else{
				price2=list.get(1).getDiscountprice();
			}
			tv_jf2.setText("需要：" + price2 + "积分");
			ll_sp2.setVisibility(View.VISIBLE);
		case 1:
			finalBitmap.display(iv_img1, list.get(0).getSmallpic());
			tv_name1.setText(list.get(0).getTitle());
			String price;
			if("0".equals(list.get(0).getDiscountprice())){
				price=list.get(0).getJifen();
			}else{
				price=list.get(0).getDiscountprice();
			}
			tv_jf1.setText("需要：" + price + "积分");
			ll_sp1.setVisibility(View.VISIBLE);
		case 0:			
			break;

		default:
			break;
		}
	}
	
	private void addPreDraw() {
		// TODO Auto-generated method stub
		iv_bg.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				LogMgr.showLog("执行了吗？");
                Drawable drawable = ImageUtil.blur(act, iv_bg);
                iv_push.setBackground(drawable);				
			}
		});
//		iv_bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//			
//			@Override
//			public boolean onPreDraw() {
//				// TODO Auto-generated method stub
////				iv_bg.getViewTreeObserver().removeOnPreDrawListener(this);
////				if(iv_push.getVisibility() == View.VISIBLE){					
////					iv_bg.buildDrawingCache();	
////	                Bitmap bmp = iv_bg.getDrawingCache();
//	                Drawable drawable = ImageUtil.blur(act, iv_bg);
//	                iv_push.setBackground(drawable);
////				}
//                return true;
//			}
//		});
	}

//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		// TODO Auto-generated method stub
//		LogMgr.showLog("------------onTouch----------");
//		if(v.getId() == arcMenu.getId() && !arcMenu.isOpen()){
//			iv_push.setVisibility(View.VISIBLE);
//		}else{
//			arcMenu.toggleMenu(300);
//			iv_push.setVisibility(View.GONE);
//		}
//		return false;
//	}

//	public void reflesh() {
//		// TODO Auto-generated method stub
//		getKykType();
//	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}
