package ir.easybazi.view.activity

import android.content.IntentFilter
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ir.easybazi.R
import ir.easybazi.system.application.G
import ir.easybazi.system.hardware.ConnectivityListener
import ir.easybazi.view.custom_views.my_views.MyViews

class ActivityNetworkError : AppCompatActivity() {


    internal lateinit var txt_network_error: TextView

    internal var connectivityListener: ConnectivityListener? = null
    internal lateinit var lyt_root: CoordinatorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_error)

        setupViews()
        setTypeFace()

        val rotation = AnimationUtils.loadAnimation(this@ActivityNetworkError, R.anim.rotate_network_error)
        rotation.fillAfter = true
        txt_network_error.startAnimation(rotation)


    }


    private fun setupViews() {
        txt_network_error = findViewById(R.id.txt_network_error)
        lyt_root = findViewById(R.id.lyt_root)
    }

    private fun setTypeFace() {
        txt_network_error.typeface = MyViews.getIranSansLightFont(this@ActivityNetworkError)

    }


    override fun onStart() {
        super.onStart()

        //    connectivityListener = new ConnectivityListener(lyt_root);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(G.connectivityListener)
    }

}
