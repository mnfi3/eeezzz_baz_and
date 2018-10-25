package sabalan.paydar.mohtasham.ezibazi.view.fragment.intro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import sabalan.paydar.mohtasham.ezibazi.R;


public class FragmentIntro3 extends Fragment {

  View view;
  WebView web_view_intro3;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_intro3, container, false);


    setupViews();
    setHtml();

    return view;
  }


  private void setupViews(){
    web_view_intro3 = view.findViewById(R.id.web_view_intro3);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setHtml(){
    WebSettings webSettings = web_view_intro3.getSettings();
    webSettings.setJavaScriptEnabled(true);
//    assert web_view_in]
// tro3 != null;
//    web_view_intro2.getSettings().setJavaScriptEnabled(true);
//    web_view_intro2.getSettings().setAppCacheEnabled(true);
    web_view_intro3.loadUrl("file:///android_asset/intro/introthree.html");

  }










}
