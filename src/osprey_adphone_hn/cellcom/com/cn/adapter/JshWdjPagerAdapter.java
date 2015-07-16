package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.JshFragmentActivity;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.P2PHandler;

public class JshWdjPagerAdapter extends PagerAdapter {

	private Context context;
	private List<Device> list;
//	private Bitmap defaultBitmap;
	private FinalBitmap fb;
	private LayoutInflater mInflater;
	private JazzyViewPager mJazzy;
	public JshWdjPagerAdapter(Context context, List<Device> list, JazzyViewPager jazzy) {
		this.context = context;
		this.list = list;
		this.mJazzy=jazzy;
//		defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.os_jsh_wdj_pager_item_bg);
		fb = FinalBitmap.create(context);
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view = mJazzy.findViewFromObject(position);
		if (view == null) {
			LogMgr.showLog("view is null");
			view = initView();
			mJazzy.setObjectForPosition(view, position);
		}
		initListener(view,list.get(position) );
		initData(view, list.get(position));
		container.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub	
		View view = mJazzy.findViewFromObject(position);
		container.removeView(view);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}
	
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}
	
	private View initView() {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		View view = mInflater.inflate(R.layout.os_jsh_wdj_pager_item, null);
		holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
		holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
		holder.tv_error = (TextView) view.findViewById(R.id.tv_error);
//		holder.ll_error = (LinearLayout) view.findViewById(R.id.ll_error);
		holder.ll_bf = (LinearLayout) view.findViewById(R.id.ll_bf);
		holder.mainrl = (RelativeLayout) view.findViewById(R.id.mainrl);
//		holder.iv_state = (ImageView) view.findViewById(R.id.iv_state);
//		holder.iv_error = (ImageView) view.findViewById(R.id.iv_error);
		holder.iv_bf = (ImageView) view.findViewById(R.id.iv_bf);
		holder.iv_play = (ImageView) view.findViewById(R.id.iv_play);
		holder.iv_push = (ImageView) view.findViewById(R.id.iv_push);
		holder.iv_back = (ImageView) view.findViewById(R.id.iv_back);
		holder.iv_set = (ImageView) view.findViewById(R.id.iv_set);
		holder.iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
		holder.pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
		view.setTag(holder);
		return view;
	}
	
	public void initListener(View view, final Device device) {
		// TODO Auto-generated method stub
		final Holder holder = (Holder)view.getTag();
		view.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				LogMgr.showLog("view onLongClick");
				((JshFragmentActivity)context).DeleteDevice(FList.getInstance().getPosition(device.getDeviceId()));
				return false;
			}
		});
		holder.iv_bf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(device.isClickGetDefenceState()){
					return;
				}
				if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_ON){
					device.setClickGetDefenceState(true);
					P2PHandler.getInstance().setRemoteDefence(device.getDeviceId(), device.getDevicePassword(),
							Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);
					notifyDataSetChanged();
				}else if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_OFF){
					device.setClickGetDefenceState(true);
					P2PHandler.getInstance().setRemoteDefence(device.getDeviceId(), device.getDevicePassword(),
							Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);	
					notifyDataSetChanged();				
				}
			}
		});
		holder.iv_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (device.getOnLineState() == Constants.DeviceState.ONLINE){
					((JshFragmentActivity)context).jshPlay(device);
				}else{
					Toast.makeText(context, "当前设备已离线不能播放", Toast.LENGTH_SHORT).show();
				}
			}
		});
		holder.iv_push.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				if(device.isExpand()){
					holder.iv_push.setBackgroundResource(android.R.color.transparent);
					holder.iv_back.setVisibility(View.GONE);
					holder.iv_set.setVisibility(View.GONE);
					holder.iv_edit.setVisibility(View.GONE);
					device.setExpand(false);
				}else{
					holder.iv_push.setBackgroundResource(R.color.translucent_8);
					holder.iv_back.setVisibility(View.VISIBLE);
					holder.iv_set.setVisibility(View.VISIBLE);
					holder.iv_edit.setVisibility(View.VISIBLE);
					device.setExpand(true);
				}
			}
		});
		holder.iv_push.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				LogMgr.showLog("iv_push onLongClick");
				((JshFragmentActivity)context).DeleteDevice(FList.getInstance().getPosition(device.getDeviceId()));
				return false;
			}
		});
		holder.iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (device.getOnLineState() == Constants.DeviceState.ONLINE){
					((JshFragmentActivity)context).jshRecord(device);
				}else{
					Toast.makeText(context, "当前设备已离线不能播放", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		holder.iv_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (device.getOnLineState() == Constants.DeviceState.ONLINE){
					((JshFragmentActivity)context).jshSetDevice(device);
				}else{
					Toast.makeText(context, "当前设备已离线不能设置", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		holder.iv_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((JshFragmentActivity)context).jshEditDevice(device.getDeviceId());
			}
		});
	}
	
	public void initData(View view, final Device device) {
		// TODO Auto-generated method stub
		final Holder holder = (Holder)view.getTag();
		if (!TextUtils.isEmpty(device.getServerImgUrl()) && checkUrl(device.getServerImgUrl())) {
			fb.display(holder.mainrl, device.getServerImgUrl()/*, defaultBitmap, defaultBitmap, false*/);
		} else {
			holder.mainrl.setBackgroundResource(R.drawable.os_jsh_wdj_pager_item_bg);
		}
		holder.tv_name.setText(device.getDeviceName());
		LogMgr.showLog("device.getOnLineState()：" + device.getOnLineState());
		holder.iv_push.setBackgroundResource(android.R.color.transparent);
		holder.iv_back.setVisibility(View.GONE);
		holder.iv_set.setVisibility(View.GONE);
		holder.iv_edit.setVisibility(View.GONE);
		device.setExpand(false);
		LogMgr.showLog("device.getDefenceState()：" + device.getDefenceState());
		if (device.getOnLineState() == Constants.DeviceState.ONLINE) {
			Drawable drawable= context.getResources().getDrawable(R.drawable.os_jsh_wdj_item_state_online);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.tv_state.setCompoundDrawables(drawable,null,null,null);
			holder.tv_state.setText(context.getString(R.string.os_jsh_wdj_online));
			 if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_LOADING ){
					holder.tv_error.setVisibility(View.GONE);
					holder.ll_bf.setVisibility(View.GONE);
			}else {
				holder.tv_error.setVisibility(View.GONE);
				holder.ll_bf.setVisibility(View.GONE);
				if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_WARNING_NET ){
					holder.tv_error.setText(context.getString(R.string.os_jsh_wdj_wlyc));
					holder.tv_error.setVisibility(View.VISIBLE);
					holder.ll_bf.setVisibility(View.GONE);
				}else if(device.getDefenceState() ==Constants.DefenceState.DEFENCE_STATE_WARNING_PWD){
					holder.tv_error.setText(context.getString(R.string.os_jsh_wdj_mmcw));
					holder.tv_error.setVisibility(View.VISIBLE);
					holder.ll_bf.setVisibility(View.GONE);
				}else if(device.getDefenceState() ==Constants.DefenceState.DEFENCE_NO_PERMISSION){
					holder.tv_error.setText(context.getString(R.string.os_jsh_wdj_zwqx));
					holder.tv_error.setVisibility(View.VISIBLE);
					holder.ll_bf.setVisibility(View.GONE);
				}else {
					if(device.isClickGetDefenceState()){
						holder.pb_loading.setVisibility(View.VISIBLE);
						holder.iv_bf.setVisibility(View.GONE);		
					}else{
						holder.iv_bf.setVisibility(View.VISIBLE);			
						holder.pb_loading.setVisibility(View.GONE);			
					}
					if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_OFF){
						holder.iv_bf.setBackgroundResource(R.drawable.os_jsh_wdj_item_bf_off);
						holder.tv_error.setVisibility(View.GONE);
						holder.ll_bf.setVisibility(View.VISIBLE);
					}else if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_ON){					
						holder.iv_bf.setBackgroundResource(R.drawable.os_jsh_wdj_item_bf_on);
						holder.tv_error.setVisibility(View.GONE);
						holder.ll_bf.setVisibility(View.VISIBLE);
					}
				}				
			}
		} else {
			Drawable drawable= context.getResources().getDrawable(R.drawable.os_jsh_wdj_item_state_offline);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.tv_state.setCompoundDrawables(drawable,null,null,null);
			holder.tv_state.setText(context	.getString(R.string.os_jsh_wdj_offline));
			holder.tv_error.setVisibility(View.GONE);
			holder.ll_bf.setVisibility(View.GONE);
		}
//		if(device.getDefenceState() == Constants.DefenceState.DEFENCE_STATE_LOADING){
//			holder.iv_state.setBackgroundResource(R.drawable.os_jsh_wdj_item_state_loading);
//			holder.tv_state.setText(context	.getString(R.string.os_jsh_wdj_loading));
//		}
	}
	
	private boolean checkUrl(String serverImgUrl) {
		if (serverImgUrl.contains(".jpg") || serverImgUrl.contains(".png")) {
			return true;
		}
		return false;
	}
	
//	public void deleteView(int position){
//		deletePosition = position;		
//	}
//
//	private void removeView(){
//		if(deletePosition > 0){
//			views.remove(deletePosition);
//			deletePosition = -1;
//		}
//	}
	
	public class Holder{
		TextView tv_name, tv_state, tv_error;
		LinearLayout /*ll_error, */ll_bf;
		ImageView /*iv_cover,*/ /*iv_state,*/ /*iv_error,*/ iv_bf, iv_play, iv_push, iv_back, iv_set, iv_edit;
		ProgressBar pb_loading;
		RelativeLayout mainrl;
	}
}
