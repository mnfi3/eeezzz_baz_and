package ir.easybazi.view.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.wang.avi.AVLoadingIndicatorView

import ir.easybazi.R
import ir.easybazi.api_service.main.ShopService
import ir.easybazi.model.Game
import ir.easybazi.model.Paginate
import ir.easybazi.recyclerview_adapter.ListShopAdapter
import ir.easybazi.system.application.G
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter

class ActivityListShop : AppCompatActivity() {

    internal lateinit var rcv_list_shops: RecyclerView
    internal lateinit var apiService: ShopService

    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView

    internal lateinit var avl_center: AVLoadingIndicatorView
    internal lateinit var avl_bottom: AVLoadingIndicatorView
    internal var page_num = 1
    internal var paginate: Paginate? = null
    internal lateinit var listAdapter: ListShopAdapter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_game)

        setupViews()
        setTypeFace()

        rcv_list_shops.layoutManager = GridLayoutManager(this@ActivityListShop, 1, GridLayoutManager.VERTICAL, false)
        getShops()

        txt_page_name.text = "لیست بازی های فروشی"
        img_back.setOnClickListener { this@ActivityListShop.finish() }



        rcv_list_shops.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (paginate != null) {
                        if (page_num != paginate!!.last_page) {
                            page_num++
                            avl_bottom.visibility = View.VISIBLE
                            getShops()
                        }
                    }
                }
            }
        })

    }

    private fun setupViews() {
        rcv_list_shops = findViewById<View>(R.id.rcv_list_game) as RecyclerView
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)

        avl_center = findViewById(R.id.avl_center)
        avl_bottom = findViewById(R.id.avl_bottom)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityListShop)
    }


    private fun getShops() {
        apiService = ShopService(this@ActivityListShop)
        val onShopsReceived = object : ShopService.onShopsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate) {
                avl_center.visibility = View.INVISIBLE
                avl_bottom.visibility = View.INVISIBLE
                if (status == 0) {
                    //          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
                } else {
                    this@ActivityListShop.paginate = paginate
                    if (page_num == 1) {
                        listAdapter = ListShopAdapter(this@ActivityListShop, games as MutableList<Game>)
                        //          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
                        val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
                        //          rcv_list_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
                        rcv_list_shops.adapter = ScaleInAnimationAdapter(alphaAdapter)
                    } else {
                        listAdapter.notifyData(games)
                    }

                }
            }

        }

        apiService.getMainShops(page_num, onShopsReceived)
//        apiService.getMainShops(page_num) { status, message, games, paginate ->
//            avl_center.visibility = View.INVISIBLE
//            avl_bottom.visibility = View.INVISIBLE
//            if (status == 0) {
//                //          Toast.makeText(G.context, message, Toast.LENGTH_SHORT).show();
//            } else {
//                this@ActivityListShop.paginate = paginate
//                if (page_num == 1) {
//                    listAdapter = ListShopAdapter(this@ActivityListShop, games)
//                    //          AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(listAdapter);
//                    val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
//                    //          rcv_list_shops.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
//                    rcv_list_shops.adapter = ScaleInAnimationAdapter(alphaAdapter)
//                } else {
//                    listAdapter.notifyData(games)
//                }
//
//            }
//        }
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
