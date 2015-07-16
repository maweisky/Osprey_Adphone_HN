package osprey_adphone_hn.cellcom.com.cn.activity.jsh;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LoadingFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.fragment_loading, container, false);
		view.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ImageView imageView = (ImageView) view
						.findViewById(R.id.loadingImageView);
				AnimationDrawable animationDrawable = (AnimationDrawable) imageView
						.getBackground();
				animationDrawable.start();
			}
		});
		return view;
	}
}