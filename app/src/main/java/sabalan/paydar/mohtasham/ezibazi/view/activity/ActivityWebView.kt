package sabalan.paydar.mohtasham.ezibazi.view.activity

//import android.annotation.SuppressLint;
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.graphics.Bitmap
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.wang.avi.AVLoadingIndicatorView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityWebView : AppCompatActivity() {


    internal lateinit var web_view: WebView
    internal lateinit var swipe: SwipeRefreshLayout
    internal lateinit var txt_page_name: TextView
    internal lateinit var img_back: ImageView
    internal lateinit var img_finish: ImageView
    internal var url: String? = null
    internal lateinit var avl_webview: AVLoadingIndicatorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setupViews()

        val extras = intent.extras
        if (extras != null) {
            url = extras.getString("URL")
        } else {
            url = savedInstanceState!!.getSerializable("URL") as String
        }

        //    url = "https://www.google.com";


        img_back.setOnClickListener {
            if (web_view.canGoBack()) {
                web_view.goBack()
            } else {
                finish()
            }
        }

        img_finish.setOnClickListener { finish() }

        swipe.setColorSchemeResources(R.color.colorAccent)
        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            avl_webview.visibility = View.INVISIBLE
            WebAction()
        }

//        web_view.loadUrl(url)
        WebAction()

    }

    private fun setupViews() {
        txt_page_name = findViewById(R.id.txt_page_name)
        img_back = findViewById(R.id.img_back)
        img_finish = findViewById(R.id.img_finish)
        web_view = findViewById(R.id.web_view)
        swipe = findViewById(R.id.swipe)
        avl_webview = findViewById(R.id.avl_webview)
    }




    @SuppressLint("SetJavaScriptEnabled")
    fun WebAction() {

        web_view.settings.javaScriptEnabled = true
        web_view.settings.setAppCacheEnabled(true)

        web_view.loadUrl(url)

        if (!swipe.isRefreshing) {
            avl_webview.visibility = View.VISIBLE
        }

        web_view.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                //        web_view.loadUrl("file:///android_assets/error.html");
            }

            override fun onPageFinished(view: WebView, url: String) {
                swipe.isRefreshing = false
                avl_webview.visibility = View.INVISIBLE
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                this@ActivityWebView.url = url
                txt_page_name.text = this@ActivityWebView.url
            }
        }

//        web_view.webChromeClient = object : WebChromeClient(){
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                super.onProgressChanged(view, newProgress)
//                avl_webview.visibility = View.INVISIBLE
//            }
//
//        }

    }





    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            finish()
        }
    }


    override fun onStart() {
        super.onStart()

        //    connectivityListener = new ConnectivityListener(lyt_action);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(G.connectivityListener)
    }

}
