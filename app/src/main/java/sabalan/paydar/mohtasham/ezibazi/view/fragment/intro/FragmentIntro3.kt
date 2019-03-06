package sabalan.paydar.mohtasham.ezibazi.view.fragment.intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView

import sabalan.paydar.mohtasham.ezibazi.R


class FragmentIntro3 : Fragment() {

    internal lateinit var view: View
    internal lateinit var web_view_intro3: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_intro3, container, false)


        setupViews()
        setHtml()

        return view
    }


    private fun setupViews() {
        web_view_intro3 = view.findViewById(R.id.web_view_intro3)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setHtml() {
        val webSettings = web_view_intro3.settings
        webSettings.javaScriptEnabled = true
        //    assert web_view_in]
        // tro3 != null;
        //    web_view_intro2.getSettings().setJavaScriptEnabled(true);
        //    web_view_intro2.getSettings().setAppCacheEnabled(true);
        web_view_intro3.loadUrl("file:///android_asset/intro/introthree.html")

    }


}
