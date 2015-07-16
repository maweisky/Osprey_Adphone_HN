package osprey_adphone_hn.cellcom.com.cn.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class ContextUtil {

  public static String getQhbTime(String time) {
    if (!TextUtils.isEmpty(time)) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date;
      try {
        date = simpleDateFormat.parse(time);
        Calendar dealcal = Calendar.getInstance();
        dealcal.setTime(date);
        Calendar syscal = Calendar.getInstance();
        int year = syscal.get(Calendar.YEAR);
        int month = syscal.get(Calendar.MONTH);
        int day = syscal.get(Calendar.DAY_OF_MONTH);
        int hour = syscal.get(Calendar.HOUR_OF_DAY);
        String hourStr;
        if(hour<10){
          hourStr="0"+hour;
        }else{
          hourStr=hour+"";
        }
        int minute = syscal.get(Calendar.MINUTE);
       String minuteStr;
       if(minute<10){
         minuteStr="0"+minute;
       }else{
         minuteStr=""+minute;
       }
        return year+"年"+month+"月"+day+"日"+" "+hourStr+":"+minuteStr;
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return "";
  }

  public static String getDealTime(String text) {
    if (!TextUtils.isEmpty(text)) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        Date date = simpleDateFormat.parse(text);
        Calendar dealcal = Calendar.getInstance();
        dealcal.setTime(date);
        Calendar syscal = Calendar.getInstance();
        int year = syscal.get(Calendar.YEAR);
        int month = syscal.get(Calendar.MONTH);
        int day = syscal.get(Calendar.DAY_OF_MONTH);
        if (dealcal.get(Calendar.YEAR) == year) {
          if (dealcal.get(Calendar.MONTH) == month) {
            if (dealcal.get(Calendar.DAY_OF_MONTH) == day) {
              return (dealcal.get(Calendar.HOUR_OF_DAY) > 9
                  ? dealcal.get(Calendar.HOUR_OF_DAY)
                  : "0" + dealcal.get(Calendar.HOUR_OF_DAY))
                  + ":"
                  + (dealcal.get(Calendar.MINUTE) > 9
                      ? dealcal.get(Calendar.MINUTE)
                      : ("0" + dealcal.get(Calendar.MINUTE)));
            } else {
              return (dealcal.get(Calendar.MONTH) + 1)
                  + "-"
                  + dealcal.get(Calendar.DAY_OF_MONTH)
                  + " "
                  + (dealcal.get(Calendar.HOUR_OF_DAY) > 9
                      ? dealcal.get(Calendar.HOUR_OF_DAY)
                      : "0" + dealcal.get(Calendar.HOUR_OF_DAY))
                  + ":"
                  + (dealcal.get(Calendar.MINUTE) > 9
                      ? dealcal.get(Calendar.MINUTE)
                      : ("0" + dealcal.get(Calendar.MINUTE)));
            }
          } else {
            return (dealcal.get(Calendar.MONTH) + 1)
                + "-"
                + dealcal.get(Calendar.DAY_OF_MONTH)
                + " "
                + (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal.get(Calendar.HOUR_OF_DAY) : "0"
                    + dealcal.get(Calendar.HOUR_OF_DAY))
                + ":"
                + (dealcal.get(Calendar.MINUTE) > 9 ? dealcal.get(Calendar.MINUTE) : ("0" + dealcal
                    .get(Calendar.MINUTE)));
          }
        } else {
          return dealcal.get(Calendar.YEAR)
              + "-"
              + (dealcal.get(Calendar.MONTH) + 1)
              + "-"
              + dealcal.get(Calendar.DAY_OF_MONTH)
              + " "
              + (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal.get(Calendar.HOUR_OF_DAY) : "0"
                  + dealcal.get(Calendar.HOUR_OF_DAY))
              + ":"
              + (dealcal.get(Calendar.MINUTE) > 9 ? dealcal.get(Calendar.MINUTE) : ("0" + dealcal
                  .get(Calendar.MINUTE)));
        }
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return text;
  }

  /*
   * 旋转图片
   * 
   * @param angle
   * 
   * @param bitmap
   * 
   * @return Bitmap
   */
  public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
    // 旋转图片 动作
    Matrix matrix = new Matrix();;
    matrix.postRotate(angle);
    System.out.println("angle2=" + angle);
    // 创建新的图片
    Bitmap resizedBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return resizedBitmap;
  }

  /**
   * 读取图片属性：旋转的角度
   * 
   * @param path 图片绝对路径
   * @return degree旋转的角度
   */
  public static int readPictureDegree(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation =
          exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
              ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  /**
   * 获取程序外部的缓存目录
   * 
   * @param context
   * @return
   */
  public static File getExternalCacheDir(Context context) {
    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
    return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
  }

  // 初始化SD卡文件目录
  public static String initSDCardDir(Context ctx) {
    final String cachePath =
        Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            ? getExternalCacheDir(ctx).getPath()
            : ctx.getCacheDir().getPath();
    File PHOTO_DIR = new File(cachePath + File.separator + "Adv");
    PHOTO_DIR.mkdirs();
    String savePath = PHOTO_DIR.getAbsolutePath();
    File file = new File(savePath);
    if (!file.exists()) {
      file.mkdir();
    }
    return savePath;
  }

  // 初始化SD卡视频设备封面文件目录
  public static String initSDCardDirByPreview(Context ctx) {
    final String cachePath =
        Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            ? getExternalCacheDir(ctx).getPath()
            : ctx.getCacheDir().getPath();
    String account = SharepreferenceUtil.readString(ctx, SharepreferenceUtil.fileName, "account");
    File PHOTO_DIR = new File(cachePath + File.separator + account + File.separator + "page");
    PHOTO_DIR.mkdirs();
    String savePath = PHOTO_DIR.getAbsolutePath();
    File file = new File(savePath);
    if (!file.exists()) {
      file.mkdir();
    }
    return savePath;
  }

  /**
   * 获取当前日期是星期几<br>
   * 
   * @param dt
   * @return 当前日期是星期几
   */
  public static String getWeekOfCalendar(String tag) {
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = getDate(tag);
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) w = 0;

    return weekDays[w];
  }

  private static Calendar getDate(String tag) {
    Calendar cal = Calendar.getInstance();
    if ("-1".equals(tag)) {
      cal.add(Calendar.DATE, +1);
    } else if ("0".equals(tag)) {
      cal.add(Calendar.DATE, -0);
    } else if ("1".equals(tag)) {
      cal.add(Calendar.DATE, -1);
    } else if ("2".equals(tag)) {
      cal.add(Calendar.DATE, -2);
    } else if ("3".equals(tag)) {
      cal.add(Calendar.DATE, -3);
    } else if ("4".equals(tag)) {
      cal.add(Calendar.DATE, -4);
    } else if ("5".equals(tag)) {
      cal.add(Calendar.DATE, -5);
    } else if ("6".equals(tag)) {
      cal.add(Calendar.DATE, -6);
    }
    return cal;
  }

  public static String getPreDate(String tag) {
    Calendar cal = getDate(tag);
    return cal.get(Calendar.YEAR)
        + "-"
        + (cal.get(Calendar.MONTH) + 1)
        + "-"
        + cal.get(Calendar.DAY_OF_MONTH)
        + " "
        + (cal.get(Calendar.HOUR_OF_DAY) > 9 ? cal.get(Calendar.HOUR_OF_DAY) : "0"
            + cal.get(Calendar.HOUR_OF_DAY))
        + ":"
        + (cal.get(Calendar.MINUTE) > 9 ? cal.get(Calendar.MINUTE) : ("0" + cal
            .get(Calendar.MINUTE)));
  }

  public static String[] getAppVersionName(Context context) {
    String[] codename = null;
    String versionCode = ""; // ?��??
    String versionName = ""; // ?��???
    try {
      // ---get the package info---
      PackageManager pm = context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
      versionCode = pi.versionCode + "";
      versionName = pi.versionName;
      codename = new String[] {versionName, versionCode};
      // codename=new String[]{versionCode,versionName};
    } catch (Exception e) {
      codename = new String[] {"0", ""};
    }
    return codename;
  }

  public static int getWidth(Context cx) {
    int width = 0;
    DisplayMetrics dm = null;
    dm = new DisplayMetrics();
    dm = cx.getApplicationContext().getResources().getDisplayMetrics();
    width = dm.widthPixels;
    return width;
  }

  public static int getHeith(Context cx) {
    int height = 0;
    DisplayMetrics dm = null;
    dm = new DisplayMetrics();
    dm = cx.getApplicationContext().getResources().getDisplayMetrics();
    height = dm.heightPixels;
    return height;
  }

  /*
   * dip转换成px
   */
  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  /*
   * px转换成dip
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static String getSubStringEnd(String text, String mark) {
    if (!TextUtils.isEmpty(text)) {
      if (text.contains(mark)) {
        return text.substring(text.lastIndexOf(mark), text.length());
      }
    }
    return text;
  }

  public static String getWeekOfDate() {
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    Date dt = new Date();
    cal.setTime(dt);
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) w = 0;
    return weekDays[w];
  }

  public static String getWeekTranDate(String data) {
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = dateFormat.parse(data);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (date != null) {
      cal.setTime(date);
      int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
      if (w < 0) w = 0;
      return weekDays[w];
    } else {
      return weekDays[0];
    }
  }

  /**
   * MD5加密
   * 
   * @param str
   * @return
   */
  public static String encodeMD5(String str) {
    if (str == null) return str;
    return MD5.compile(str);
    // return encrypt(str.getBytes()).toString();
  }

  // 获得sd卡的路径
  public static String getSdCardPath() {
    return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  /**
   * 创建文件或覆盖文件 【不管文件目录是否存在】
   * 
   * @param filePath 文件路径
   * @param bool 是否覆盖 true 覆盖 false 不覆盖
   * @return File 文件对象
   */
  public static File createFile(String filePath, boolean bool) {
    try {
      File file = new File(filePath);

      if (!file.exists()) {// 不存在
        File parent = file.getAbsoluteFile().getParentFile();// 获得父级目录
        if (parent.exists() || parent.mkdirs()) // 目录不存在则创建目录
          if (file.createNewFile()) // 创建文件
            return file;

        return null;
      } else if (bool) {// 存在 是否覆盖
        file.delete();
        file.createNewFile();
      }

      return file;
    } catch (IOException e) {
      return null;
    }
  }

  public static String formateTime(String time) {
    String timestr = time;
    if (timestr.contains(".")) {
      timestr = timestr.substring(0, timestr.indexOf("."));
    }
    return timestr;
  }

  public static String checkSSid(String ssid) {
    // TODO Auto-generated method stub
    char[] str = ssid.toCharArray();
    String newSsid = "";
    for (int i = 0; i < str.length; i++) {
      LogMgr.showLog("str[" + i + "]----------->" + str[i]);
      if (str[i] != '"') {
        newSsid = newSsid + str[i];
      }
    }
    return newSsid;
  }

  public static String getCurrentTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    String str = formatter.format(curDate);
    return str;
  }
}
