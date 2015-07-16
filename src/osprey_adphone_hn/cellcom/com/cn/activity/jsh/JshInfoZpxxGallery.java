package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.bean.SnapInfo;
import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionShareSheet;
import osprey_adphone_hn.cellcom.com.cn.widget.ActionShareSheet.OnActionSheetSelected;
import osprey_adphone_hn.cellcom.com.cn.widget.MarqueeText;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 照片浏览
 * 
 * @author Administrator
 * 
 */
public class JshInfoZpxxGallery extends ActivityFrame
    implements
      OnActionSheetSelected,
      OnCancelListener {
  LinearLayout ll_back;
  LinearLayout ll_set;
  private LinearLayout llAppShare;
  MarqueeText tv_title;
  private ImageSwitcher switcher;
  private int selectedItem;
  private int position;
  private List<SnapInfo> lists = new ArrayList<SnapInfo>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.os_jsh_info_snapshot_dialog_gallery);
    isShowSlidingMenu(false);
    initView();
    initData();
    initListener();
  }

  private void initView() {
    // TODO Auto-generated method stub
    llAppShare = (LinearLayout) findViewById(R.id.llAppShare);
    ll_back = (LinearLayout) findViewById(R.id.llAppBack);
    ll_set = (LinearLayout) findViewById(R.id.llAppSet);
    tv_title = (MarqueeText) findViewById(R.id.tvTopTitle);
  }

  private void initListener() {
    // TODO Auto-generated method stub
    llAppShare.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        // 其次把文件插入到系统图库
        ActionShareSheet.showSheet(JshInfoZpxxGallery.this, JshInfoZpxxGallery.this,
            JshInfoZpxxGallery.this, "1");

      }
    });
    ll_back.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        JshInfoZpxxGallery.this.finish();
      }
    });
    final GestureDetector gd =
        new GestureDetector(JshInfoZpxxGallery.this, new OnGestureListener() {

          @Override
          public boolean onDown(MotionEvent arg0) {
            // TODO Auto-generated method stub
            return false;
          }

          @Override
          public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
            // TODO Auto-generated method stub
            float x1 = arg0.getRawX();
            float x2 = arg1.getRawX();
            float distance = x1 - x2;
            if ((distance > 0) && (Math.abs(distance) > 30)) {
              if (++selectedItem < lists.size()) {
                switcher.setInAnimation(AnimationUtils.loadAnimation(JshInfoZpxxGallery.this,
                    R.anim.slide_in_right_100));
                switcher.setOutAnimation(AnimationUtils.loadAnimation(JshInfoZpxxGallery.this,
                    R.anim.slide_out_left_100));
                switcher.setImageDrawable(new BitmapDrawable(getResources(), lists
                    .get(selectedItem).getPath()));
              } else {
                selectedItem = lists.size() - 1;
              }

              LogMgr.showLog(Runtime.getRuntime().totalMemory() + "---------1");
            } else if ((distance < 0) && (Math.abs(distance) > 30)) {
              if (--selectedItem >= 0) {
                switcher.setInAnimation(AnimationUtils.loadAnimation(JshInfoZpxxGallery.this,
                    R.anim.slide_in_left_100));
                switcher.setOutAnimation(AnimationUtils.loadAnimation(JshInfoZpxxGallery.this,
                    R.anim.slide_out_right_100));
                switcher.setImageDrawable(new BitmapDrawable(getResources(), lists
                    .get(selectedItem).getPath()));
              } else {
                selectedItem = 0;
              }
            }
            return true;
          }

          @Override
          public void onLongPress(MotionEvent arg0) {
            // TODO Auto-generated method stub

          }

          @Override
          public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
            // TODO Auto-generated method stub
            return false;
          }

          @Override
          public void onShowPress(MotionEvent arg0) {
            // TODO Auto-generated method stub

          }

          @Override
          public boolean onSingleTapUp(MotionEvent arg0) {
            // TODO Auto-generated method stub
            return false;
          }

        });
    switcher.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        gd.onTouchEvent(arg1);
        return true;
      }
    });
  }

  private void initData() {
    // TODO Auto-generated method stub
    position = getIntent().getIntExtra("position", 0);
    lists = (ArrayList<SnapInfo>) getIntent().getSerializableExtra("value");
    tv_title.setText(R.string.os_jsh_zpj);
    switcher = (ImageSwitcher) findViewById(R.id.img_container);
    switcher.setFactory(new ViewFactory() {

      @Override
      public View makeView() {
        // TODO Auto-generated method stub
        ImageView view = new ImageView(JshInfoZpxxGallery.this);
        view.setScaleType(ScaleType.FIT_CENTER);
        view.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT));
        LogMgr.showLog(Runtime.getRuntime().totalMemory() + "-------------0");
        return view;
      }
    });
    selectedItem = position;
    BitmapDrawable bitmapDrawable =
        new BitmapDrawable(getResources(), lists.get(selectedItem).getPath());
    switcher.setImageDrawable(bitmapDrawable);
  }

  /**
   * 分享功能
   * 
   * @param context 上下文
   * @param activityTitle Activity的名字
   * @param msgTitle 消息标题
   * @param msgText 消息内容
   * @param imgPath 图片路径，不分享图片则传null
   */
  public void shareMsg(String activityTitle, String imgPath) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    if (imgPath == null || imgPath.equals("")) {
      intent.setType("text/plain"); // 纯文本
    } else {
      File f = new File(imgPath);
      if (f != null && f.exists() && f.isFile()) {
        intent.setType("image/jpg");
        Uri u = Uri.fromFile(f);
        intent.putExtra(Intent.EXTRA_STREAM, u);
      }
    }
    // intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
    // intent.putExtra(Intent.EXTRA_TEXT, msgText);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(Intent.createChooser(intent, activityTitle));
  }

  @Override
  public void onCancel(DialogInterface dialog) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onClick(int whichButton) {
    // TODO Auto-generated method stub
    if (whichButton == 1) {
      String path = lists.get(selectedItem).getPath();
      try {
        String absolutepath = path.substring(0, path.lastIndexOf("/"));
        String name = path.substring(path.lastIndexOf("/") + 1, path.length());
        MediaStore.Images.Media.insertImage(JshInfoZpxxGallery.this.getContentResolver(),
            absolutepath, name, null);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      // 最后通知图库更新
      JshInfoZpxxGallery.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
          .parse("file://" + path)));
      Toast.makeText(JshInfoZpxxGallery.this, "保存成功", Toast.LENGTH_SHORT).show();
    } else if (whichButton == 2) {
      shareMsg("JshInfoZpxxGallery", lists.get(selectedItem).getPath());

    }
  }
}
