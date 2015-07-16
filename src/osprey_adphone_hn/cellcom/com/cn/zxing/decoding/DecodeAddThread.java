/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package osprey_adphone_hn.cellcom.com.cn.zxing.decoding;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import osprey_adphone_hn.cellcom.com.cn.zxing.activity.CaptureAddActivity;
import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;

/**
 * This thread does all the heavy lifting of decoding the images.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class DecodeAddThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";

	/**
	 * ����activity
	 */
	private final CaptureAddActivity activity;
	/**
	 * ����һ�������Ž����ʽ���ַ����ص���
	 */
	private final Hashtable<DecodeHintType, Object> hints;
	/**
	 * �������handler
	 */
	private Handler handler;
	/**
	 * �̰߳�ȫ������װ��
	 */
	private final CountDownLatch handlerInitLatch;

	DecodeAddThread(CaptureAddActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ResultPointCallback resultPointCallback) {

		this.activity = activity;
		/**
		 * ���ü���Ϊ1
		 */
		handlerInitLatch = new CountDownLatch(1);

		/**
		 * ��������
		 */
		hints = new Hashtable<DecodeHintType, Object>(3);

		/**
		 * ���ý����ʽ��Ȼ��ŵ�������
		 */
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		/**
		 * ����ַ�Ϊ�գ��ŵ�������
		 */
		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		/**
		 * �����ص���ŵ�������,�����hints�ᱻ���õ��������У������������ͼƬ��ʱ��Ὣ�����Ƕ
		 * �ά��ĵ㷵�ص����ص����� ����foundPossibleResultPoint(ResultPoint
		 * point)��������������ֻҪ�������е���View����
		 * �ķ���������Щ���ܵ���Ƴ�4����������ɨ�迴�����̵�
		 */
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}

	Handler getHandler() {
		try {
			/**
			 * �ȴ����Ϊ0��ʱ���������ִ��
			 */
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		/**
		 * �������handler
		 */
		handler = new DecodeAddHandler(activity, hints);
		/**
		 * �����1
		 */
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
