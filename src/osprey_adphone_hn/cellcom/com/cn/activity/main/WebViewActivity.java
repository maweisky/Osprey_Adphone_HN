package osprey_adphone_hn.cellcom.com.cn.activity.main;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
/**
 * 内嵌网页
 * @author Administrator
 *
 */
public class WebViewActivity extends ActivityFrame {
	private WebView webView;
	private String nowurl;
	private ImageView iv_drawable;
	private AnimationDrawable animationDrawable;
	String content;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		AppendMainBody(R.layout.app_webview);
		AppendTitleBody1();
		isShowSlidingMenu(false);
		HideSet();
		SetTopBarTitle("加载中...");
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_drawable = (ImageView) findViewById(R.id.iv_drawable);
		webView = (WebView) findViewById(R.id.webView);
	}

	private void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("url");
			webViewShow(url);
			
			animationDrawable = (AnimationDrawable) iv_drawable.getBackground();
			iv_drawable.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					animationDrawable.start();
				}
			});
	}

	private void webViewShow(String url) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new getJavaScriptLocalObj(), "html_obj");
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebPageClient());
		webView.getSettings().setDefaultTextEncodingName("GBK");
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.getWindow().setFeatureInt(
						Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
				if (newProgress >= 99) {
					webView.setVisibility(View.VISIBLE);
					iv_drawable.setVisibility(View.GONE);
				} else {
					webView.setVisibility(View.GONE);
					iv_drawable.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (getIntent().getExtras() != null
						&& getIntent().getExtras().getString("title") != null) {
					SetTopBarTitle(getIntent().getExtras().getString("title"));
				} else {
					SetTopBarTitle(title);
				}
				// WebViewActivity.this.setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		webView.loadUrl(url);
		nowurl = url;
	}

	private class WebPageClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 把当前页面的地址赋值到全局变量，方便监听
			nowurl = url;
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			getHtmlTitle(view);
			super.onPageFinished(view, url);
		}
	}

	private void getHtmlTitle(WebView view) {
		view.loadUrl("javascript:window.html_obj.showSource('<head>'+"
				+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String httptitle = "";
				String cardtitle = "";
				int ElemetLen = 0;
				System.out.println("httptitle--------->" + content);
				if (content != null) {
					if (content.indexOf("<title>") != -1) {
						ElemetLen = ("<title>").length();
						httptitle = content.substring(
								content.indexOf("<title>") + ElemetLen,
								content.indexOf("</title>"));
					} else if (content.contains("card title")) {
						ElemetLen = ("<card title=").length() + 1;
						cardtitle = content.substring(content
								.indexOf("<card title=") + ElemetLen);
						httptitle = cardtitle.substring(0,
								cardtitle.indexOf("\""));
					}
				}
			}
		}, 1000);
	}

	final class getJavaScriptLocalObj {
		public void showSource(String html) {
			content = html;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 如果当前页面为主页就不允许后退，直接退出
		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0 && notIndex(nowurl)) {
			webView.goBack();
			nowurl = webView.getOriginalUrl();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 判断这个页面是不是主页面
	public boolean notIndex(String url) {
		if (url.endsWith("index.html")) {
			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(animationDrawable!=null){
			animationDrawable.stop();
		}
	}
}