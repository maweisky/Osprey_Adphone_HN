package osprey_adphone_hn.cellcom.com.cn.util;

import java.io.InputStream;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

public class BitMapUtil {
	public static void getImg(Activity activity,FinalBitmap finalBitmap,View view, int resid) {
		String drawable="drawable"+resid;
		if(finalBitmap.getBitmapFromMemoryCache(drawable)==null){
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
//			Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), resid);
			InputStream is = activity.getResources().openRawResource(resid);
			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
			finalBitmap.addMemoryCache(drawable, bm);
		}
		Bitmap bm = finalBitmap.getBitmapFromMemoryCache(drawable);
		BitmapDrawable bd = new BitmapDrawable(activity.getResources(), bm);
		if(view instanceof ImageView){
			((ImageView)view).setImageDrawable(bd);
		}else{
//			view.setBackground(bd);
			view.setBackgroundDrawable(bd);
		}
//		view.setBackgroundDrawable(bd);
	}
	
	public static void getImgOpt(Activity activity,FinalBitmap finalBitmap,View view, int resid) {
		String drawable="drawable"+resid;
		if(finalBitmap.getBitmapFromMemoryCache(drawable)==null){
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			InputStream is = activity.getResources().openRawResource(resid);
			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
			finalBitmap.addMemoryCache(drawable, bm);
		}
		Bitmap bm = finalBitmap.getBitmapFromMemoryCache(drawable);
		BitmapDrawable bd = new BitmapDrawable(activity.getResources(), bm);
		if(view instanceof ImageView){
			((ImageView)view).setImageDrawable(bd);
		}else{
			view.setBackgroundDrawable(bd);
		}
	}
}
