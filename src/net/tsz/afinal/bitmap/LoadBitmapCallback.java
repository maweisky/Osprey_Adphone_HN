package net.tsz.afinal.bitmap;

import android.graphics.Bitmap;

public interface LoadBitmapCallback {
	/**
     * 对Bitmap做某事
     * 
     * @param bitmap
     */
    void doSomething(Bitmap bitmap);
}