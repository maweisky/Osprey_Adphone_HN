package osprey_adphone_hn.cellcom.com.cn.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import osprey_adphone_hn.cellcom.com.cn.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class ImageUtil {

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		// Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
		// Bitmap.Config.ARGB_8888);
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}
	
	//把图片转换成String
	public static String photoToString(Bitmap photo) {
		// TODO Auto-generated method stub
		try {  			
            ByteArrayOutputStream stream = new ByteArrayOutputStream();  
             photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);  
             byte[] b = stream.toByteArray();  
             // 将图片流以字符串形式存储下来  
             String tp = new String(Base64.encode(b));//tp 就是最终的参数  
             return tp;
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } 
		return "";
	}
	
	/**
	 * 拍照（从视屏流中截取)
	 * **/
	public static int savePhotoBySnapShot(Context ctx, Bitmap bitmap, int screenWidth,	int screenHeight, String fileUrl) {
		int result;
		if (bitmap == null) {
			LogMgr.showLog("拍照失败bitmap==null!");
			return -1;
		}

		try {
			ContextUtil.createFile(fileUrl, true);
			File file = new File(fileUrl);
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = -1;
			LogMgr.showLog("拍照失败!");
			e.printStackTrace();
		}
		result = 1;
		return result;
	}
	
	public static Drawable blur(Context context, View view) {
//        long startMs = System.currentTimeMillis();
//        float scaleFactor = 1;
//        float radius = 20;
		float scaleFactor = 1;
		float radius = 20f;		
		view.buildDrawingCache();	
        Bitmap bmp = view.getDrawingCache();
		Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
				(int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft(), -view.getTop());
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bmp, 0, 0, paint);
		overlay = FastBlur.doBlur(overlay, (int) radius, true);
//		view.setBackground(new BitmapDrawable(context.getResources(), overlay));
		return new BitmapDrawable(context.getResources(), overlay);
    }

}
