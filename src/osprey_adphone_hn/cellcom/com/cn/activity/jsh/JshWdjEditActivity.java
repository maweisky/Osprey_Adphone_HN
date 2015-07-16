package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.bean.AddrInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.CityInfoBean;
import osprey_adphone_hn.cellcom.com.cn.bean.EditAddrMgr;
import osprey_adphone_hn.cellcom.com.cn.bean.HouseInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.HouseInfoComm;
import osprey_adphone_hn.cellcom.com.cn.bean.ProvinceInfoBean;
import osprey_adphone_hn.cellcom.com.cn.bean.SysComm;
import osprey_adphone_hn.cellcom.com.cn.broadcast.OsConstants;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.util.RegExpValidator;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionSheet;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import osprey_adphone_hn.cellcom.com.cn.widget.EditAddrSelectSheet;
import osprey_adphone_hn.cellcom.com.cn.widget.EditAddrSelectSheet.SheetCallBack;
import p2p.cellcom.com.cn.bean.Device;
import p2p.cellcom.com.cn.db.DBManager;
import p2p.cellcom.com.cn.global.FList;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cellcom.com.cn.net.CellComAjaxHttp.HttpWayMode;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.CellComAjaxResult.ParseType;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

/**
 * 添加及编辑设备界面
 * @author Administrator
 *
 */
public class JshWdjEditActivity extends ActivityFrame implements OnClickListener, OnActionSheetSelected, OnCancelListener{
	
	//设备id，名称，密码
	private EditText et_sbid, et_mc, et_mm;
	
	//设备头像
	private ImageView iv_tx;
	private Button btn_add, btn_confirm;
	
	//弹出选择器
	private EditText et_cs, et_xq, et_fh;
	private EditAddrMgr addrMgr = new EditAddrMgr();
	EditAddrSelectSheet addrSheet;	
	
	private Device device;
	private String deviceId;
	private String[] addr;
	private String optype = "1";//绑定设备为1， 编辑设备为2
	private String ifimg = "N";
	
	//图片加载器
	private Bitmap loadingBitmap;
	private FinalBitmap fb;
	private FinalDb finalDb;

	//封面选择
	private static int CAMERA_REQUEST_CODE = 1;
	private static int GALLERY_REQUEST_CODE = 2;
	private static int CROP_REQUEST_CODE = 3;
	private Bitmap bm;

	private List<ProvinceInfoBean> provinceInfoBeans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_jsh_wdj_edit);
		isShowSlidingMenu(false);
		AppendTitleBody1();
		HideSet();
		initView();
		initListener();
		initData();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
//		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		int id = v.getId();
		if(id == R.id.btn_add){
			ActionSheet.showSheet(this, this, this, "1");
		}else if(id == R.id.btn_confirm){
			CheckData();
		}else if(id == R.id.iv_tx){
			ActionSheet.showSheet(this, this, this, "1");
		}else if(id == R.id.et_cs){
			addrSheet.showSheet(0);
		}else if(id == R.id.et_xq){
			if(TextUtils.isEmpty(et_cs.getText().toString())){
				ShowMsg("请先选择所在省、市、区！");
				return;
			}
			if(addrMgr.getP_list().size() <= 0){
				ShowMsg("请先选择所在省！");
				return;
			}
			if(addrMgr.getC_list().size() <= 0){
				ShowMsg("请先选择所在市！");
				return;
			}
			if(addrMgr.getA_list().size() <= 0){
				ShowMsg("请先选择所在区！");
				return;
			}
			addrSheet.showSheet(1);
		}else if(id == R.id.et_fh){
			if(TextUtils.isEmpty(et_xq.getText().toString())){
				ShowMsg("请先选择所在小区！");
				return;
			}
			addrSheet.showSheet(2);
		}
	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_REQUEST_CODE);
		} else if (whichButton == 2) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, GALLERY_REQUEST_CODE);
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST_CODE) {
			if (data == null) {
				return;
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bm = extras.getParcelable("data");
					Uri uri = saveBitmap(bm);
					startImageZoom(uri);
				}
			}
		} else if (requestCode == GALLERY_REQUEST_CODE) {
			if (data == null) {
				return;
			}
			Uri uri;
			uri = data.getData();
			Uri fileUri = convertUri(uri);
			startImageZoom(fileUri);
		} else if (requestCode == CROP_REQUEST_CODE) {
			if (data == null) {
				return;
			}
			Bundle extras = data.getExtras();
			if (extras == null) {
				return;
			}
			bm = extras.getParcelable("data");
			btn_add.setVisibility(View.GONE);
			iv_tx.setImageBitmap(bm);
			// sendImage(bm);
		}
	}

	private Uri saveBitmap(Bitmap bm) {
		File tmpDir = new File(Environment.getExternalStorageDirectory()
				+ "/com.jikexueyuan.avater");
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		File img = new File(tmpDir.getAbsolutePath() + "/avater.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Uri convertUri(Uri uri) {
		InputStream is = null;
		try {
			is = getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return saveBitmap(bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void startImageZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1.1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 110);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	
	private void initView() {
		// TODO Auto-generated method stub
		et_sbid = (EditText) findViewById(R.id.et_sbid);
		et_mc = (EditText) findViewById(R.id.et_mc);
		et_mm = (EditText) findViewById(R.id.et_mm);
		
		et_cs = (EditText) findViewById(R.id.et_cs);
		et_xq = (EditText) findViewById(R.id.et_xq);
		et_fh = (EditText) findViewById(R.id.et_fh);
		
		iv_tx = (ImageView) findViewById(R.id.iv_tx);
		
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		finalDb=FinalDb.create(this);
		addrSheet = new EditAddrSelectSheet(this, addrMgr,finalDb);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		
		et_cs.setOnClickListener(this);
		et_xq.setOnClickListener(this);
		et_fh.setOnClickListener(this);
		
		addrSheet.setSheetCallBack(new SheetCallBack() {
			
			@Override
			public void callback(int type, String addr) {
				// TODO Auto-generated method stub
				if(type == 0){
					et_cs.setText(addr);
					et_xq.setText("");
					et_fh.setText("");
				}else if(type == 1){
					et_xq.setText(addr);
					et_fh.setText("");
				}else if(type == 2){
					et_fh.setText(addr);
				}
			}
		});
		
		iv_tx.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		fb = FinalBitmap.create(this);
		
		deviceId = getIntent().getStringExtra("deviceId");
		String title = getString(R.string.os_jsh_wdj_edit_tzsxt);
		if(deviceId != null){
			et_sbid.setText(deviceId);
			device = FList.getInstance().isDevice(deviceId);
			if(device != null){
				optype = "2";
				title = getString(R.string.os_jsh_wdj_edit_bjsb);
				et_sbid.setText(device.getDeviceId());
				et_sbid.setEnabled(false);
				et_mc.setText(device.getDeviceName());
				et_mm.setText(device.getDevicePassword());
				android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) iv_tx.getLayoutParams();
				if(!TextUtils.isEmpty(device.getServerImgUrl()) && (device.getServerImgUrl().contains(".jpg") ||device.getServerImgUrl().contains(".png"))){
					if(loadingBitmap == null){
						loadingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.os_jsh_wdj_item_cover);
					}
					fb.display(iv_tx, device.getServerImgUrl(), params.width, params.height, loadingBitmap, loadingBitmap, false);
					btn_add.setVisibility(View.GONE);
					iv_tx.setClickable(true);
				}else{
					btn_add.setVisibility(View.VISIBLE);
					iv_tx.setClickable(false);
				}
				if(!TextUtils.isEmpty(device.getAddr()) && device.getAddr().contains(",")){
					addr = device.getAddr().split(",");
				}
			}
		}
		SetTopBarTitle(title);
//		getHouseInfos("p", "1");
		getProvinceInfos();
	}
	/**
	 * 获取地址信息
	 * @param type 需要获取的类型
	 * @param typeid 对应的上一级id
	 */
	private void getProvinceInfos() {
		// TODO Auto-generated method stub
		String oldareaversion=SharepreferenceUtil.readString(JshWdjEditActivity.this, SharepreferenceUtil.fileName, "oldareaversion");
		final String areaversion=SharepreferenceUtil.readString(JshWdjEditActivity.this, SharepreferenceUtil.fileName, "areaversion");
		if(TextUtils.isEmpty(oldareaversion)||Float.parseFloat(oldareaversion)<Float.parseFloat(areaversion)){
			final String uid = SharepreferenceUtil.readString(JshWdjEditActivity.this,
					SharepreferenceUtil.fileName, "uid");
			CellComAjaxParams params = new CellComAjaxParams();
			params.put("uid", uid);
			HttpHelper.getInstances(this).send(FlowConsts.YYW_GETAREALIST, params, HttpWayMode.POST, new NetCallBack<CellComAjaxResult>() {
				
				@Override
				public void onSuccess(CellComAjaxResult t) {
					// TODO Auto-generated method stub
					try {
						AddrInfoComm comm = t.read(AddrInfoComm.class, ParseType.GSON);
						if(!comm.getReturnCode().equals("1")){
							ShowMsg(comm.getReturnMessage());
							return;
						}
						if(comm.getBody().getArealist().size() > 0){
							SharepreferenceUtil.write(JshWdjEditActivity.this, SharepreferenceUtil.fileName, "oldareaversion", areaversion);
							finalDb.deleteAll(ProvinceInfoBean.class);
							finalDb.deleteAll(CityInfoBean.class);
							for (int i = 0; i < comm.getBody().getArealist().size(); i++) {	
								for (int j = 0; j < comm.getBody().getArealist().get(i).getData().size(); j++) {
									comm.getBody().getArealist().get(i).getData().get(j).setPid(comm.getBody().getArealist().get(i).getPid());
									finalDb.save(comm.getBody().getArealist().get(i).getData().get(j));
								}
								finalDb.save(comm.getBody().getArealist().get(i));
							}
							provinceInfoBeans=finalDb.findAll(ProvinceInfoBean.class);
							List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
							for (int i = 0; i < provinceInfoBeans.size(); i++) {
								HouseInfo houseInfo=new HouseInfo();
								houseInfo.setId(provinceInfoBeans.get(i).getPid());
								houseInfo.setName(provinceInfoBeans.get(i).getPname());
								houseInfos.add(houseInfo);
//								houseInfos.add(provinceInfoBeans.get(i));
							}
							addrMgr.addList("p", houseInfos);
							setHouseInfo("p", houseInfos);
						}else{
							ShowMsg("暂无相关信息！");
						}
					} catch (Exception e) {
						// TODO: handle exception
						LogMgr.showLog("error-------------->" + e.toString());
						ShowMsg("获取数据失败，请重新获取！");
					}
				}
			});
		}else{
			provinceInfoBeans=finalDb.findAll(ProvinceInfoBean.class);
			List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
			for (int i = 0; i < provinceInfoBeans.size(); i++) {
				HouseInfo houseInfo=new HouseInfo();
				houseInfo.setId(provinceInfoBeans.get(i).getPid());
				houseInfo.setName(provinceInfoBeans.get(i).getPname());
				houseInfos.add(houseInfo);
			}
			addrMgr.addList("p", houseInfos);
			setHouseInfo("p", houseInfos);
		}
	}
	/**
	 * 获取地址信息
	 * @param type 需要获取的类型
	 * @param typeid 对应的上一级id
	 */
	private void getHouseInfos(final String type, String typeid) {
		// TODO Auto-generated method stub
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("type", type);
		params.put("typeid", typeid);
		HttpHelper.getInstances(this).send(FlowConsts.YYW_GETHOUSE, params, HttpWayMode.POST, new NetCallBack<CellComAjaxResult>() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onSuccess(CellComAjaxResult t) {
				// TODO Auto-generated method stub
				try {
					HouseInfoComm comm = t.read(HouseInfoComm.class, ParseType.GSON);
					if(!comm.getReturnCode().equals("1")){
						ShowMsg(comm.getReturnMessage());
						return;
					}
					if(comm.getBody().size() > 0){
						addrMgr.addList(type, comm.getBody());
						setHouseInfo(type, comm.getBody());
					}else{
						if(type.equals("g")){
							addrMgr.getG_list().clear();
							addrMgr.getB_list().clear();
							addrMgr.getH_list().clear();
						}
						if(type.equals("b")){
							addrMgr.getB_list().clear();
							addrMgr.getH_list().clear();
						}
						if(type.equals("h")){
							addrMgr.getH_list().clear();
						}
						ShowMsg("暂无相关信息！");
					}
				} catch (Exception e) {
					// TODO: handle exception
					LogMgr.showLog("error-------------->" + e.toString());
					ShowMsg("获取数据失败，请重新获取！");
				}
			}
			
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
			}
		});
	}
	
	private void setHouseInfo(String type, List<HouseInfo> list){
		if(type.equalsIgnoreCase("p")){
			if(addr != null){
				LogMgr.showLog("addr[0]----------------->" + addr[0]);
				addrMgr.setP_position(getHouseInfoPosition(addr[0], addrMgr.getP_list()));
			}
			List<CityInfoBean> cityInfoBeans=finalDb.findAllByWhere(CityInfoBean.class," pid='"+addrMgr.getP_list().get(addrMgr.getP_position()).getId()+"'");
			List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
			List<String> list2=new ArrayList<String>();
			for (int i = 0; i < cityInfoBeans.size(); i++) {
				HouseInfo houseInfo=new HouseInfo();
				houseInfo.setId(cityInfoBeans.get(i).getCid());
				houseInfo.setName(cityInfoBeans.get(i).getCityname());
				if(!list2.contains(cityInfoBeans.get(i).getCityname())){					
					list2.add(cityInfoBeans.get(i).getCityname());
					houseInfos.add(houseInfo);
				}
			}
			if(cityInfoBeans.size() > 0){
				addrMgr.addList("c", houseInfos);
				setHouseInfo("c", houseInfos);
			}else{
				addrMgr.getC_list().clear();
				addrMgr.getA_list().clear();
				addrMgr.getG_list().clear();
				addrMgr.getB_list().clear();
				addrMgr.getH_list().clear();
				ShowMsg("暂无相关信息！");
			}
//			getHouseInfos("c", addrMgr.getP_list().get(addrMgr.getP_position()).getId());
		}else if(type.equalsIgnoreCase("c")){
			if(addr != null){
				addrMgr.setC_position(getHouseInfoPosition(addr[1], addrMgr.getC_list())) ;
			}
			List<CityInfoBean> cityInfoBeans=finalDb.findAllByWhere(CityInfoBean.class," cid='"+addrMgr.getC_list().get(addrMgr.getC_position()).getId()+"'");
			List<HouseInfo> houseInfos=new ArrayList<HouseInfo>();
			for (int i = 0; i < cityInfoBeans.size(); i++) {
				if(!"null".equals(cityInfoBeans.get(i).getAid())){					
					HouseInfo houseInfo=new HouseInfo();
					houseInfo.setId(cityInfoBeans.get(i).getAid());
					houseInfo.setName(cityInfoBeans.get(i).getAname());
					houseInfos.add(houseInfo);
				}
			}
			if(houseInfos.size() > 0){
				addrMgr.addList("a", houseInfos);
				setHouseInfo("a", houseInfos);
			}else{
				addrMgr.getA_list().clear();
				addrMgr.getG_list().clear();
				addrMgr.getB_list().clear();
				addrMgr.getH_list().clear();
				ShowMsg("暂无相关信息！");
			}
//			getHouseInfos("a", addrMgr.getC_list().get(addrMgr.getC_position()).getId());
		}else if(type.equalsIgnoreCase("a")){
			if(addr != null){
				addrMgr.setA_position(getHouseInfoPosition(addr[2], addrMgr.getA_list())) ;
				String addr = addrMgr.getP_list().get(addrMgr.getP_position()).getName() + 
						addrMgr.getC_list().get(addrMgr.getC_position()).getName() + 
						addrMgr.getA_list().get(addrMgr.getA_position()).getName();
				et_cs.setText(addr);
			}
			if(addrMgr.getA_list().size()>addrMgr.getA_position() &&!"null".equals(addrMgr.getA_list().get(addrMgr.getA_position()).getId())){		
				getHouseInfos("g", addrMgr.getA_list().get(addrMgr.getA_position()).getId());
			}
//			tv_area.setText(a_list.get(a_position).getName());
		}else if(type.equalsIgnoreCase("g")){
//			g_list.clear();
//			g_list.addAll(list);
//			g_adapter.notifyDataSetChanged();
//			g_position = 0;
			if(addr != null){
				addrMgr.setG_position(getHouseInfoPosition(addr[3], addrMgr.getG_list())) ;
				et_xq.setText(addrMgr.getG_list().get(addrMgr.getG_position()).getName());
			}
			if(addrMgr.getG_list().size()>addrMgr.getG_position()&&!"null".equals(addrMgr.getG_list().get(addrMgr.getG_position()).getId())){				
				getHouseInfos("b", addrMgr.getG_list().get(addrMgr.getG_position()).getId());
			}
//			tv_garden.setText(g_list.get(g_position).getName());	
		}else if(type.equalsIgnoreCase("b")){
//			b_list.clear();
//			b_list.addAll(list);
//			b_adapter.notifyDataSetChanged();
//			b_position = 0;
			if(addr != null){
				addrMgr.setB_position(getHouseInfoPosition(addr[4], addrMgr.getB_list())) ;
			}
			if(addrMgr.getB_list().size()>addrMgr.getB_position()&&!"null".equals(addrMgr.getB_list().get(addrMgr.getB_position()).getId())){				
				getHouseInfos("h", addrMgr.getB_list().get(addrMgr.getB_position()).getId());
			}
//			tv_building.setText(b_list.get(b_position).getName());
		}else if(type.equalsIgnoreCase("h")){
//			h_list.clear();
//			h_list.addAll(list);
//			h_adapter.notifyDataSetChanged();
//			h_position = 0;
			if(addr != null){
				addrMgr.setH_position(getHouseInfoPosition(addr[5], addrMgr.getH_list())) ;
				String addr = addrMgr.getB_list().get(addrMgr.getB_position()).getName() + 
						addrMgr.getH_list().get(addrMgr.getH_position()).getName();
				et_fh.setText(addr);
			}
//			tv_house.setText(h_list.get(h_position).getName());
		}
	}
	
	private int getHouseInfoPosition(String id, List<HouseInfo> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getId().equals(id)){
				return i;
			}
		}
		return 0;
	}
	
	private void CheckData() {
		// TODO Auto-generated method stub
		String deviceId = et_sbid.getText().toString();
		if(TextUtils.isEmpty(deviceId)){
			ShowMsg("设备ID不能为空，请输入设备ID！");
			return;
		}
		if(!RegExpValidator.IsNumber(deviceId)){
          ShowMsg("设备ID必须为数字!");
          return;
        }
		if(TextUtils.isEmpty(et_cs.getText().toString())){
			ShowMsg("请先选择所在省、市、区！");
			return;
		}
		if(TextUtils.isEmpty(et_xq.getText().toString())){
			ShowMsg("请先选择所在小区！");
			return;
		}
		if(TextUtils.isEmpty(et_fh.getText().toString())){
			ShowMsg("请先选择所在房号！");
			return;
		}
		String deviceName = et_mc.getText().toString();
		if(TextUtils.isEmpty(deviceName)){
			ShowMsg("设备名称不能为空，请输入设备ID！");
			return;
		}
		
		String password = et_mm.getText().toString();
		if(TextUtils.isEmpty(password)){
			ShowMsg("设备密码不能为空，请输入设备ID！");
			return;
		}
		
//		addDevice(deviceId, deviceName, password, h_list.get(h_position).getId());
		if(addrMgr.getH_list().size()>addrMgr.getH_position()){			
			addDevice(deviceId, deviceName, password, addrMgr.getH_list().get(addrMgr.getH_position()).getId());
		}
	}
	
	
	private void addDevice(final String deviceid, final String dname, final String password, final String hid) {
		// TODO Auto-generated method stub
		LogMgr.showLog("deviceid------------------------>" + deviceid);
		final String uid = SharepreferenceUtil.readString(this,	SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams params = new CellComAjaxParams();
//		params.put("uid", uid);
//		params.put("did", deviceid);
//		params.put("optype", optype);
//		params.put("hid", hid);
//		params.put("dname", dname);
		if (bm == null) {
			ifimg="N";
//			cellComAjaxParams.put("ifpic", "N");
		} else {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
			byte[] bytes = stream.toByteArray();
			File tmpDir = new File(Environment.getExternalStorageDirectory()
					+ "/com.jikexueyuan.avater");
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			final File tempf = getFileFromBytes(bytes, tmpDir.getAbsolutePath()
					+ "/avatertemp.png");
			ifimg="Y";
			try {
				params.put("file", tempf);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpHelper.getInstances(this).send(FlowConsts.YYW_SETDEVICE+"?uid="+uid+"&did="+deviceid.trim()+"&optype="+optype+"&hid="+hid+"&dname="+dname.trim()+"&ifimg="+ifimg, params, HttpWayMode.POST, new NetCallBack<CellComAjaxResult>() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				ShowProgressDialog(R.string.app_progress);
			}
			
			@Override
			public void onSuccess(CellComAjaxResult t) {
				// TODO Auto-generated method stub
				DismissProgressDialog();
				try {
					SysComm comm = t.read(SysComm.class, ParseType.GSON);
					if(!comm.getReturnCode().equals("1")){
						ShowMsg(comm.getReturnMessage());
						return;
					}
					if(optype.equals("1")){
						ShowMsg("设备添加成功！");
					}else{
						ShowMsg("设备编辑成功！");
					}
					Intent intent = new Intent();
					Device device = null;
					if(optype.equals("1")){
						device = new Device();
						device.setDeviceId(deviceid);
						device.setDeviceName(dname);
						device.setDevicePassword(password);
						intent.setAction(OsConstants.JSH.ADD_DEVICES_SUCCES);
					}else{
						device = FList.getInstance().isDevice(deviceid);
						device.setDeviceName(dname);
						device.setDevicePassword(password);
						if(addr[5].equalsIgnoreCase(hid) && ifimg.equalsIgnoreCase("N")){
							intent.setAction(OsConstants.JSH.REFRUSH_ADAPTER_NOTICE);
						}else{
							intent.setAction(OsConstants.JSH.REFRUSH_DEVICES_NOTICE);
						}
					}
					DBManager.saveDevice(JshWdjEditActivity.this, device);
					sendBroadcast(intent);
					finish();
				} catch (Exception e) {
					// TODO: handle exception
					if(optype.equals("1")){
						ShowMsg("设备添加失败！");
					}else{
						ShowMsg("设备编辑失败！");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				DismissProgressDialog();
			}
		});
	}
}
