package mohtasham.paydar.sabalan.ezbazi.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import mohtasham.paydar.sabalan.ezbazi.R;

public class ActivityWebView extends AppCompatActivity {


  WebView web_view;
  SwipeRefreshLayout swipe;
  TextView txt_page_name;
  ImageView img_back, img_finish;
  String url;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web_view);

    setupViews();

    url = "https://www.google.com/";



    img_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (web_view.canGoBack()){
          web_view.goBack();
        }else {
          finish();
        }
      }
    });

    img_finish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        WebAction();
      }
    });

    WebAction();

  }

  private void setupViews(){
    txt_page_name = (TextView) findViewById(R.id.txt_page_name);
    img_back = (ImageView) findViewById(R.id.img_back);
    img_finish = (ImageView) findViewById(R.id.img_finish);
    web_view = (WebView) findViewById(R.id.web_view);
    swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
  }


  @SuppressLint("SetJavaScriptEnabled")
  public void WebAction(){

    web_view = (WebView) findViewById(R.id.web_view);
    web_view.getSettings().setJavaScriptEnabled(true);
    web_view.getSettings().setAppCacheEnabled(true);

    web_view.loadUrl(url);

//    url = web_view.getUrl();

    swipe.setRefreshing(true);

    web_view.setWebViewClient(new WebViewClient(){
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        web_view.loadUrl("file:///android_assets/error.html");
      }
      public void onPageFinished(WebView view, String url) {
        swipe.setRefreshing(false);

      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ActivityWebView.this.url = url;
        txt_page_name.setText(ActivityWebView.this.url);
      }
    });

  }


  @Override
  public void onBackPressed(){

    if (web_view.canGoBack()){
      web_view.goBack();
    }else {
      finish();
    }
  }

}
