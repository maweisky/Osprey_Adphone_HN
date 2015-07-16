package osprey_adphone_hn.cellcom.com.cn.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3DAnimation extends Animation {
	// 旋转轴
	private String mRotate = "y";
	// ��ʼ�Ƕ�
	private final float mFromDegrees;
	// ����Ƕ�
	private final float mToDegrees;
	// ���ĵ�
	private final float mCenterX;
	private final float mCenterY;
	private final float mDepthZ;
	// �Ƿ���ҪŤ��
	private final boolean mReverse;
	// ����ͷ
	private Camera mCamera;

	public Rotate3DAnimation(String rotate, float fromDegrees, float toDegrees,
			float centerX, float centerY, float depthZ, boolean reverse) {
		mRotate = rotate;
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
	}

	public Rotate3DAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthZ, boolean reverse) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	// ���Transformation
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		// ����м�Ƕ�
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		if (mRotate.equals("y")) {
			camera.rotateY(degrees);
		} else if (mRotate.equals("x")) {
			camera.rotateX(degrees);
		} else if (mRotate.equals("z")) {
			camera.rotateZ(degrees);
		}
		// ȡ�ñ任��ľ���
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
