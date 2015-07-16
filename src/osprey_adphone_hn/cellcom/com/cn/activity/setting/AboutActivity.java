package osprey_adphone_hn.cellcom.com.cn.activity.setting;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.RegisterXieYiActivity;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends ActivityFrame {
	private TextView tv_sytk, tv_yyrx;
	private LinearLayout ll_yyrx;
	private LinearLayout ll_kfdh;
	private TextView tv_kfdh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_about_activity);
		AppendTitleBody1();
		SetTopBarTitle("关于");
		findViewById(R.id.llAppSet).setVisibility(View.INVISIBLE);
		isShowSlidingMenu(false);
		initView();
		initListener();
	}

	private void initView() {
		tv_sytk = (TextView) findViewById(R.id.tv_sytk);
		tv_yyrx = (TextView) findViewById(R.id.tv_yyrx);
		ll_yyrx = (LinearLayout) findViewById(R.id.ll_yyrx);
		ll_kfdh=(LinearLayout)findViewById(R.id.ll_kfdh);
		tv_kfdh=(TextView)findViewById(R.id.tv_kfdh);
	}

	private void initListener() {
		// 使用条款
		tv_sytk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenActivity(RegisterXieYiActivity.class);
			}
		});
		tv_yyrx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:400-0731-611"));
				startActivity(intent);
			}
		});
		//客服电话
		tv_kfdh.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + "0731-89875328"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
          }
        });
		//客服电话
		ll_kfdh.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + "0731-89875328"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
          }
        });
		// 鱼鹰热线
		ll_yyrx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ "400-0731-611"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (CommonUtils.getCurrentChildMenuActivity().equals("gyyy")) {
			CommonUtils.setCurrentChildMenuActivity("");
		}
	}
}
