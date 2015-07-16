package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import net.tsz.afinal.bitmap.display.AvatarDrawableFactory;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.JfscSpTypeAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.JfscSpType;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfo;
import osprey_adphone_hn.cellcom.com.cn.bean.UserInfoComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.RegExpValidator;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionSheet;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import osprey_adphone_hn.cellcom.com.cn.widget.CitySheet;
import osprey_adphone_hn.cellcom.com.cn.widget.CitySheet.SheetCallBack;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.AddAdapter;
import osprey_adphone_hn.cellcom.com.cn.widget.popupwindow.ListViewPopupWindow.ItemClick;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

/**
 * 车辆信息
 * 
 * @author wma
 * 
 */
public class ClxxActivity extends ActivityFrame implements
		OnActionSheetSelected, OnCancelListener {
	private ImageView iv_user_img;
	private LinearLayout updatell;
	private ImageView ivedit;
	private TextView tvsave;
	private boolean isEdit = true;

	private static final int CHOOSE_PHOTO_REQUESTCODE = 3;// 选取本地照片
	private static final int TAKE_PHOTO_REQUESTCODE = 4;// 获取拍照照片
	private static final int PHOTO_PICKED_WITH_DATA = 5;// 图片裁剪
	private Uri imageUri;
	String picpath = "";
	Bitmap bitmap = null;
	SoftReference<Bitmap> ref;
	public static final String IMAGE_FILE_LOCATION = ContextUtil
			.getSdCardPath() + "/osprey_adphone_hn/cgimg/uploadimg.jpg";

	private EditText ncet;
	private EditText zhet;
	private TextView xbet;
	private EditText shret;
	private EditText lxdhet;
	private TextView sfdqet;
	private EditText xxdzet;
	private EditText yzbmet;

	private FinalBitmap finalBitmap;
	private Bitmap loadingBitmap;

	private ListViewPopupWindow xb_popup;
	private JfscSpTypeAdapter sptype_adapter;
	private List<JfscSpType> jfscSpTypeList = new ArrayList<JfscSpType>();
	private CitySheet citySheet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendTitleBody2();
		AppendMainBody(R.layout.os_csh_clxx);
		isShowSlidingMenu(false);
		SetTopBarTitle("车辆信息");
		initView();
		initData();
		initListener();
	}

	private void initData() {
		// TODO Auto-generated method stub
		citySheet = new CitySheet(ClxxActivity.this);
		finalBitmap = FinalBitmap.create(ClxxActivity.this);
		iv_user_img.setEnabled(false);
		loadingBitmap = BitmapFactory.decodeResource(
				ClxxActivity.this.getResources(), R.drawable.os_dhb_itempic);
		xb_popup = new ListViewPopupWindow(this);
		xb_popup.setAddAdapter(new AddAdapter() {

			@Override
			public void addAdapter(ListView listview) {
				// TODO Auto-generated method stub
				JfscSpType jfscSpType1 = new JfscSpType();
				jfscSpType1.setName("男");
				jfscSpType1.setVid("01");
				JfscSpType jfscSpType2 = new JfscSpType();
				jfscSpType2.setName("女");
				jfscSpType2.setVid("02");
				ArrayList<JfscSpType> jfscSpTypes = new ArrayList<JfscSpType>();
				jfscSpTypes.add(jfscSpType1);
				jfscSpTypes.add(jfscSpType2);
				jfscSpTypeList.addAll(jfscSpTypes);
				sptype_adapter = new JfscSpTypeAdapter(ClxxActivity.this,
						jfscSpTypeList);
				listview.setAdapter(sptype_adapter);
			}
		});
		getGrzl();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
		updatell = (LinearLayout) findViewById(R.id.updatell);
		ivedit = (ImageView) findViewById(R.id.ivedit);
		tvsave = (TextView) findViewById(R.id.tvsave);

		ncet = (EditText) findViewById(R.id.ncet);
		zhet = (EditText) findViewById(R.id.zhet);
		xbet = (TextView) findViewById(R.id.xbet);
		shret = (EditText) findViewById(R.id.shret);
		lxdhet = (EditText) findViewById(R.id.lxdhet);
		sfdqet = (TextView) findViewById(R.id.sfdqet);
		xxdzet = (EditText) findViewById(R.id.xxdzet);
		yzbmet = (EditText) findViewById(R.id.yzbmet);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// 用户头像
		iv_user_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActionSheet.showSheet(ClxxActivity.this, ClxxActivity.this,
						ClxxActivity.this, "1");
			}
		});
		// 性别
		xbet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isEdit){					
					xb_popup.showAsDropDown(xbet, 0, 1, xbet.getWidth(),
							LayoutParams.WRAP_CONTENT);
				}
			}
		});
		// 收发地
		sfdqet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isEdit){						
					citySheet.showSheet(ClxxActivity.this, sfdqet);
					citySheet.setSheetCallBack(new SheetCallBack() {
						
						@Override
						public void callback(String addr) {
							// TODO Auto-generated method stub
							sfdqet.setText(addr);
						}
					});
				}
			}
		});
		xb_popup.setItemClick(new ItemClick() {

			@Override
			public void setOnItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				xbet.setText(jfscSpTypeList.get(position).getName());
				xb_popup.dimissPopupwindow();
			}
		});
		// 切换编辑模式
		updatell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isEdit) {
					isEdit = false;
					iv_user_img.setEnabled(true);
					ivedit.setVisibility(View.GONE);
					tvsave.setVisibility(View.VISIBLE);

					ncet.setFocusable(true);
					ncet.setFocusableInTouchMode(true);
					ncet.requestFocus();
					ncet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));

//					zhet.setFocusable(true);
//					zhet.setFocusableInTouchMode(true);
//					zhet.setBackgroundColor(getResources().getColor(
//							R.color.grzlinput));
					xbet.setClickable(true);
					xbet.setFocusable(true);
					xbet.setFocusableInTouchMode(true);
					xbet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
					shret.setFocusable(true);
					shret.setFocusableInTouchMode(true);
					shret.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
					lxdhet.setFocusable(true);
					lxdhet.setFocusableInTouchMode(true);
					lxdhet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
					sfdqet.setClickable(true);
					sfdqet.setFocusable(true);
					sfdqet.setFocusableInTouchMode(true);
					sfdqet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
					xxdzet.setFocusable(true);
					xxdzet.setFocusableInTouchMode(true);
					xxdzet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
					yzbmet.setFocusable(true);
					yzbmet.setFocusableInTouchMode(true);
					yzbmet.setBackgroundColor(getResources().getColor(
							R.color.grzlinput));
				} else {
					// editMode();
				}
			}
		});
		// 保存
		tvsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				xgGrzlTj();
			}
		});
	}
	private void xgGrzlTj() {
		String ncettxt = ncet.getText().toString();
		String xbettxt = xbet.getText().toString();
		String shrettxt = shret.getText().toString();
		String lxdhettxt = lxdhet.getText().toString();
		String sfdqettxt = sfdqet.getText().toString();
		String xxdzettxt = xxdzet.getText().toString();
		String yzbmettxt = yzbmet.getText().toString();
		if (TextUtils.isEmpty(ncettxt)) {
			ShowMsg("请输入昵称");
			return;
		}
//		if (TextUtils.isEmpty(xbettxt)) {
//			ShowMsg("请输入昵称");
//			return;
//		}
		if (TextUtils.isEmpty(shrettxt)) {
//			ShowMsg("请输入收货人");
//			return;
			shrettxt="";
		}
		if (TextUtils.isEmpty(lxdhettxt)) {
//			ShowMsg("请输入联系电话");
//			return;
			lxdhettxt="";
		}
		if (!TextUtils.isEmpty(lxdhettxt)&&
				!RegExpValidator.IsHandset(lxdhettxt)&&!RegExpValidator.IsTelephone(lxdhettxt)) {
			ShowMsg("联系号码格式错误");
			return;
		}
		if (TextUtils.isEmpty(sfdqettxt)) {
//			ShowMsg("请输入省份地区");
//			return;
			sfdqettxt="";
		}
		if (TextUtils.isEmpty(xxdzettxt)) {
//			ShowMsg("请输入详细地址");
//			return;
			xxdzettxt="";
		}
		if (TextUtils.isEmpty(yzbmettxt)) {
//			ShowMsg("请输入邮政编码");
//			return;
			yzbmettxt="";
		}
		xgGrzl(ncettxt.trim(), xbettxt.trim(), shrettxt.trim(),
				lxdhettxt.trim(), sfdqettxt.trim(), xxdzettxt.trim(),
				yzbmettxt.trim());
	}

	private void editMode() {
		isEdit = true;
		iv_user_img.setEnabled(false);
		ivedit.setVisibility(View.VISIBLE);
		tvsave.setVisibility(View.GONE);

		ncet.setFocusable(false);
		ncet.setFocusableInTouchMode(false);
		ncet.setBackgroundColor(getResources().getColor(R.color.translucent));
		xbet.setClickable(false);
		xbet.setFocusable(false);
		xbet.setFocusableInTouchMode(false);
		xbet.setBackgroundColor(getResources().getColor(R.color.translucent));
		shret.setFocusable(false);
		shret.setFocusableInTouchMode(false);
		shret.setBackgroundColor(getResources().getColor(R.color.translucent));
		lxdhet.setFocusable(false);
		lxdhet.setFocusableInTouchMode(false);
		lxdhet.setBackgroundColor(getResources().getColor(R.color.translucent));
		sfdqet.setClickable(false);
		sfdqet.setFocusable(false);
		sfdqet.setFocusableInTouchMode(false);
		sfdqet.setBackgroundColor(getResources().getColor(R.color.translucent));
		xxdzet.setFocusable(false);
		xxdzet.setFocusableInTouchMode(false);
		xxdzet.setBackgroundColor(getResources().getColor(R.color.translucent));
		yzbmet.setFocusable(false);
		yzbmet.setFocusableInTouchMode(false);
		yzbmet.setBackgroundColor(getResources().getColor(R.color.translucent));
	}

	// 修改个人资料
	private void xgGrzl(final String username, String sex, String receiver,
			String contact, String address, String fulladdress, String areacode) {
		String uid = SharepreferenceUtil.readString(ClxxActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		cellComAjaxParams.put("username", username);
		cellComAjaxParams.put("sex", sex);
		cellComAjaxParams.put("receiver", receiver);
		cellComAjaxParams.put("contact", contact);
		cellComAjaxParams.put("address", address);
		cellComAjaxParams.put("fulladdress", fulladdress);
		cellComAjaxParams.put("areacode", areacode);
		final File file = new File(picpath);
		if (!file.exists()) {
			cellComAjaxParams.put("ifpic", "N");
		} else {
			cellComAjaxParams.put("ifpic", "Y");
			try {
				cellComAjaxParams.put("file", file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpHelper.getInstances(ClxxActivity.this).send(
				FlowConsts.YYW_USERINFO_UPDATE, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.hsc_progress);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						ShowMsg("修改成功");
						if (file.exists()) {							
							file.delete();
						}
						SharepreferenceUtil.write(ClxxActivity.this, SharepreferenceUtil.fileName, "username", username);
						setResult(RESULT_OK, getIntent());
						ClxxActivity.this.finish();
					}
				});
	}

	// 获取个人资料
	private void getGrzl() {
		String uid = SharepreferenceUtil.readString(ClxxActivity.this,
				SharepreferenceUtil.fileName, "uid");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", uid);
		HttpHelper.getInstances(ClxxActivity.this).send(
				FlowConsts.YYW_USERINFO, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.hsc_progress);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						UserInfoComm userInfoComm = arg0.read(
								UserInfoComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = userInfoComm.getReturnCode();
						String msg = userInfoComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						initData(userInfoComm.getBody());
					}
				});
	}

	private void initData(UserInfo userInfo) {
		String account = SharepreferenceUtil.readString(ClxxActivity.this,
				SharepreferenceUtil.fileName, "account");

		finalBitmap.display(iv_user_img, userInfo.getHeadpicurl());
		ncet.setText(userInfo.getUsername());
		zhet.setText(account);
		xbet.setText(userInfo.getSex());
		shret.setText(userInfo.getReceiver());
		lxdhet.setText(userInfo.getContact());
		sfdqet.setText(userInfo.getAddress());
		xxdzet.setText(userInfo.getFulladdress());
		yzbmet.setText(userInfo.getAreacode());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (CommonUtils.getCurrentChildMenuActivity().equals("grzl")) {
			CommonUtils.setCurrentChildMenuActivity("");
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(int whichButton) {
		// TODO Auto-generated method stub
		if (whichButton == 1) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				ContextUtil.createFile(IMAGE_FILE_LOCATION, true);
				imageUri = Uri.parse("file://" + IMAGE_FILE_LOCATION);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO_REQUESTCODE);
			} else {
				Toast.makeText(ClxxActivity.this, "未安装SD卡！", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (whichButton == 2) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				ContextUtil.createFile(IMAGE_FILE_LOCATION, true);
				imageUri = Uri.parse("file://" + IMAGE_FILE_LOCATION);
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra("return-data", false);
				intent.putExtra("noFaceDetection", true); // no face detection
				startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			} else {
				Toast.makeText(ClxxActivity.this, "未安装SD卡！", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (whichButton == 5) {
			// if(notepadbean.getImgurl()!=null&&!notepadbean.getImgurl().equals("")){
			// //删除图片流程
			// deleteNotePadPic();
			// }else{
			// Toast.makeText(GrzlActivity.this, "没有可删除的图片",
			// Toast.LENGTH_SHORT).show();
			// }
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Ignore failed requests
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case CHOOSE_PHOTO_REQUESTCODE:
			Uri uri = data.getData();
			if (uri != null) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra("return-data", false);
				intent.putExtra("noFaceDetection", true); // no face detection
				startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			} else {
				Toast.makeText(ClxxActivity.this, "获取图片无效，请重新选择！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case TAKE_PHOTO_REQUESTCODE:
			if (imageUri != null) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra("return-data", false);
				intent.putExtra("noFaceDetection", true); // no face detection
				startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			} else {
				Toast.makeText(ClxxActivity.this, "获取图片无效，请重新选择！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			if (imageUri != null) {
				// force reload of image even it is the same uri
				if (decodeUriAsBitmap(imageUri) != null) {
					AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(
							ClxxActivity.this.getResources());
					BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
					bitmapDisplayConfig.setBitmapWidth(ContextUtil.dip2px(this,
							55));
					bitmapDisplayConfig.setBitmapHeight(ContextUtil.dip2px(
							this, 55));
					Drawable borderedRoundedAvatarDrawable = avatarDrawableFactory
							.getBorderedRoundedAvatarDrawable(
									bitmapDisplayConfig, bitmap);
					iv_user_img.setImageDrawable(borderedRoundedAvatarDrawable);
					// iv_user_img.setImageBitmap(decodeUriAsBitmap(imageUri));
				} else {
					Toast.makeText(ClxxActivity.this,
							"获取图片失败！此图片可能已损坏，请选择其他图片！", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		default:

		}
	}

	/**
	 * 
	 * 将uri资源路径转为bitmap对象
	 * 
	 * @param uri
	 *            资源路径
	 * @return 位图bitmap对象
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		try {

			int targetW = ContextUtil.getWidth(ClxxActivity.this);
			int targetH = ContextUtil.getHeith(ClxxActivity.this);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, opts);
			int photoW = opts.outWidth;
			int photoH = opts.outHeight;
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0)) {
				scaleFactor = Math.min(photoW / targetW, photoH / targetH);
			}
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = scaleFactor;// 设置位图缩放比例
			opts.inPurgeable = true;// 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
			opts.inTempStorage = new byte[100 * 1024];// 为位图设置100K的缓存
			if (scaleFactor < 1) {
				System.out.println("scaleFactor----1---->" + scaleFactor);
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));
				if (bitmap != null) {
					picpath = IMAGE_FILE_LOCATION;
					int degree = ContextUtil
							.readPictureDegree(IMAGE_FILE_LOCATION);
					/**
					 * 把图片旋转为正的方向
					 */
					Bitmap newbitmap = ContextUtil.rotaingImageView(degree,
							bitmap);
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(IMAGE_FILE_LOCATION);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					// 将bitmap存储为jpg格式的图片
					newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					try {
						// 刷新文件流
						fOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						// 关闭文件流
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ref = new SoftReference<Bitmap>(newbitmap);
				}
			} else {
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri), null, opts);
				if (bitmap != null) {
					picpath = IMAGE_FILE_LOCATION;
					int degree = ContextUtil
							.readPictureDegree(IMAGE_FILE_LOCATION);
					/**
					 * 把图片旋转为正的方向
					 */
					Bitmap newbitmap = ContextUtil.rotaingImageView(degree,
							bitmap);

					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(IMAGE_FILE_LOCATION);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					// 将bitmap存储为jpg格式的图片
					newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					try {
						// 刷新文件流
						fOut.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						// 关闭文件流
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ref = new SoftReference<Bitmap>(newbitmap);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return ref.get();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&&!isEdit) {
			ShowAlertDialog(
					this.getResources().getString(R.string.os_dhb_grzx_wxts),
					this.getResources().getString(R.string.os_dhb_grzx_sfbcxg),
					new MyDialogInterface() {

						@Override
						public void onClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							xgGrzlTj();
						}
					},new MyDialogInterface() {
						
						@Override
						public void onClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							ClxxActivity.this.finish();
						}
					});
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}