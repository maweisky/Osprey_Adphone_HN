package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.adapter.RecordAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.CustomProgressDialog;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.global.P2PConnect;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.p2p.core.MediaPlayer;
import com.p2p.core.P2PHandler;

public class RecordListFragment extends FragmentBase {
	JazzyListView list_record;
	Device contact;
	String[] names;
	AlertDialog load_record;
	View load_view;
	LayoutInflater inflater;
	RecordAdapter adapter;
	boolean isDialogShowing = false;
    boolean isRegFilter=false;
    List<String> list=new ArrayList<String>();
    private Activity activity;
    CustomProgressDialog customProgressDialog ;
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	this.activity=activity;
    	super.onAttach(activity);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.fragment_record, container, false);
		initComponent(view);
		return view;
	}
	public void initComponent(View view){
		list_record = (JazzyListView) view.findViewById(R.id.list_record);
		list_record.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new RecordAdapter(activity,list);
		list_record.setAdapter(adapter);
		list_record.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String filename = adapter.getList().get(arg2);
				customProgressDialog = ShowProgressDialog(R.string.hsc_progress);
				customProgressDialog.setCanceledOnTouchOutside(false);
				customProgressDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface arg0, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
							P2PHandler.getInstance().reject();
						}
						return false;
					}
				});
				P2PConnect.setCurrent_state(P2PConnect.P2P_STATE_CALLING);
				P2PConnect.setCurrent_call_id(contact.getDeviceId());
				P2PHandler.getInstance().playbackConnect(contact.getDeviceId(),
						contact.getDevicePassword(), filename, arg2);
			}
		});
	}
	
	public void cancelDialog(){
		load_record.cancel();
		isDialogShowing = false;
		MediaPlayer.getInstance().native_p2p_hungup();
	}

	public void setList(List<String> list){
		this.list = list;
		if(adapter!=null){			
			this.adapter.setList(list);
		}
	}
	
	public void dismissProgressDialog(){
		DismissProgressDialog();
	}
	
	public void setUser(Device contact){
		this.contact = contact;
	}
	
	public void closeDialog(){
		if(null!=load_record){
			load_record.cancel();
			isDialogShowing = false;
		}
	}
	
}
