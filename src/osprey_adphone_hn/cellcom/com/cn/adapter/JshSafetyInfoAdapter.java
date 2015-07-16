package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.io.Serializable;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.CallActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.jsh.SafetyInfoAlarmPicShow;
import osprey_adphone_hn.cellcom.com.cn.bean.SafetyInfoComm;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.Constants;
import p2p.cellcom.com.cn.global.FList;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JshSafetyInfoAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
//	List<AlarmRecord> infos;//本地数据
	List<SafetyInfoComm> safetyInfoList;//网络最新数据
	private FinalBitmap finalBitmap;
	Account account;
	public JshSafetyInfoAdapter(Context context,/*List<AlarmRecord> list*/List<SafetyInfoComm> safetyInfoList){
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.safetyInfoList = safetyInfoList;
		this.finalBitmap = FinalBitmap.create(context);
		this.account = AccountPersist.getInstance().getActiveAccountInfo(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return safetyInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return safetyInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int selectId = position;
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.os_jsh_wdj_safety_info_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_alarm = (ImageView) convertView.findViewById(R.id.iv_alarm);
			viewHolder.iv_list_select_flag = (ImageView) convertView.findViewById(R.id.iv_list_select_flag);
			viewHolder.tv_alarm_time = (TextView) convertView.findViewById(R.id.tv_alarm_time);
			viewHolder.tv_alarm_device_name = (MarqueeText) convertView.findViewById(R.id.tv_alarm_device_name);
			viewHolder.tv_look_device = (TextView) convertView.findViewById(R.id.tv_look_device);
			viewHolder.tv_look_device_lx = (TextView) convertView.findViewById(R.id.tv_look_device_lx);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Device device = FList.getInstance().isDevice(safetyInfoList.get(selectId).getDeviceId());
		if (!TextUtils.isEmpty(device.getServerImgUrl()) && checkUrl(device.getServerImgUrl())) {
			finalBitmap.display(viewHolder.iv_alarm, device.getServerImgUrl());
		} else {
			viewHolder.iv_alarm.setImageResource(R.drawable.os_jsh_wdj_pager_item_bg);
		}
//		if(safetyInfoList.get(position).getAlarmpicUrl()!=null){
//			String imgurl; = safetyInfoList.get(position).getAlarmpicUrl()+
//					"&UserId="+
//					String.valueOf(Integer.parseInt(account.three_number)|0x80000000)+"&SessionID=" +
//					account.sessionId+"&Option=0";
//			System.out.println("imgurl---"+safetyInfoList.get(position).getAlarmpicUrl()+"-->"+imgurl);
//			finalBitmap.display(viewHolder.iv_alarm, imgurl);
//			
//		}
		if(safetyInfoList.get(selectId).getDeviceId()!=null&&FList.getInstance().isDevice(safetyInfoList.get(position).getDeviceId())!=null){
			viewHolder.tv_alarm_device_name.setText(FList.getInstance().isDevice(safetyInfoList.get(position).getDeviceId()).getDeviceName());
		}
		viewHolder.tv_alarm_time.setText(safetyInfoList.get(position).getAlarmTime());
		viewHolder.tv_look_device.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (FList.getInstance().isDevice(safetyInfoList.get(selectId).getDeviceId()).getOnLineState() == Constants.DeviceState.ONLINE){
					Intent monitor = new Intent();
					monitor.setClass(mContext, CallActivity.class);
					monitor.putExtra("callId", safetyInfoList.get(selectId).getDeviceId());
					monitor.putExtra("contactName", FList.getInstance().isDevice(safetyInfoList.get(selectId).getDeviceId()).getDeviceName()!=null?
							FList.getInstance().isDevice(safetyInfoList.get(selectId).getDeviceId()).getDeviceName():"");
					monitor.putExtra("password", FList.getInstance().isDevice(safetyInfoList.get(selectId).getDeviceId()).getDevicePassword());
					monitor.putExtra("isOutCall", true);
					monitor.putExtra("type", Constants.P2P_TYPE.P2P_TYPE_MONITOR);
					mContext.startActivity(monitor);
				}else{
					Toast.makeText(mContext, "当前设备已离线不能播放", Toast.LENGTH_SHORT).show();
				}
			}
		});
		viewHolder.tv_look_device_lx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(safetyInfoList.get(selectId).getAlarmpicUrl()!=null){
					String imgurl = safetyInfoList.get(selectId).getAlarmpicUrl()+
							"&UserId="+
							String.valueOf(Integer.parseInt(account.three_number)|0x80000000)+"&SessionID=" +
							account.sessionId+"&Option=0";
					System.out.println("imgurl---"+safetyInfoList.get(selectId).getAlarmpicUrl()+"-->"+imgurl);
					Intent intent = new Intent(mContext,SafetyInfoAlarmPicShow.class);
					intent.putExtra("safetyinfolist", (Serializable)safetyInfoList);
					intent.putExtra("position", selectId);
					mContext.startActivity(intent);				
				}else{
					((ActivityFrame)mContext).ShowMsg("暂无报警图片！");
				}
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		private TextView tv_alarm_time;
		private MarqueeText tv_alarm_device_name;
		private ImageView iv_list_select_flag,iv_alarm;
		private TextView tv_look_device,tv_look_device_lx;
	}
	
	private boolean checkUrl(String serverImgUrl) {
		if (serverImgUrl.contains(".jpg") || serverImgUrl.contains(".png")) {
			return true;
		}
		return false;
	}
//	public void updateData(){
//		this.infos = DataManager.findAlarmRecordByActiveUser(mContext, NpcCommon.mThreeNum);
//	}

}
