package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.wang.avi.AVLoadingIndicatorView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.RentType
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListRentAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter

class ActivityListRent : AppCompatActivity() {

    internal lateinit var rcv_list_rents: RecyclerView
    internal lateinit var apiService: RentService
    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView

    internal lateinit var avl_center: AVLoadingIndicatorView
    internal lateinit var avl_bottom: AVLoadingIndicatorView
    internal var paginate: Paginate? = null
    internal lateinit var listAdapter: ListRentAdapter
    internal var page_num = 1

    internal var rentTypes = G.rentTypes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_game)

        setupViews()
        setTypeFace()

        rcv_list_rents.layoutManager = GridLayoutManager(this@ActivityListRent, 1, GridLayoutManager.VERTICAL, false)
        getRentTypes()
        //    getRents();

        txt_page_name.text = "لیست بازی های اجاره ای"
        img_back.setOnClickListener { this@ActivityListRent.finish() }

        rcv_list_rents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (paginate != null) {
                        if (page_num != paginate!!.last_page) {
                            page_num++
                            avl_bottom.visibility = View.VISIBLE
                            getRents()
                        }
                    }
                }
            }
        })
    }

    private fun setupViews() {
        rcv_list_rents = findViewById<View>(R.id.rcv_list_game) as RecyclerView
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)

        avl_center = findViewById(R.id.avl_center)
        avl_bottom = findViewById(R.id.avl_bottom)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityListRent)
    }


    private fun getRentTypes() {
        apiService = RentService(this@ActivityListRent)
        val onRentTypesReceived = object : RentService.onRentTypesReceived{
            override fun onReceived(status: Int, message: String, rentTypes: List<RentType>) {
                if (status == 1) {
                    this@ActivityListRent.rentTypes = rentTypes
                    getRents()
                } else {
                    this@ActivityListRent.rentTypes = G.rentTypes
                    //          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
                }
            }
        }

        apiService.getRentTypes(onRentTypesReceived)
//        apiService.getRentTypes { status, message, rentTypes ->
//            if (status == 1) {
//                this@ActivityListRent.rentTypes = rentTypes
//                getRents()
//            } else {
//                this@ActivityListRent.rentTypes = G.rentTypes
//                //          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
//            }
//        }
    }


    private fun getRents() {
        //    apiService = new RentService(G.context);
        val onRentsReceived = object : RentService.onRentsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate) {
                avl_center.visibility = View.INVISIBLE
                avl_bottom.visibility = View.INVISIBLE
                if (status == 0) {
                    //          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
                } else {
                    this@ActivityListRent.paginate = paginate
                    if (page_num == 1) {
                        listAdapter = ListRentAdapter(this@ActivityListRent, games as MutableList<Game>)
                        val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
                        rcv_list_rents.adapter = ScaleInAnimationAdapter(alphaAdapter)
                    } else {
                        listAdapter.notifyData(games)
                    }
                }
            }
        }

        apiService.getRents(page_num, onRentsReceived)
//        apiService.getRents(page_num) { status, message, games, paginate ->
//            avl_center.visibility = View.INVISIBLE
//            avl_bottom.visibility = View.INVISIBLE
//            if (status == 0) {
//                //          MyViews.makeText(ActivityListRent.this, message, Toast.LENGTH_SHORT);
//            } else {
//                this@ActivityListRent.paginate = paginate
//                if (page_num == 1) {
//                    listAdapter = ListRentAdapter(this@ActivityListRent, games)
//                    val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
//                    rcv_list_rents.adapter = ScaleInAnimationAdapter(alphaAdapter)
//                } else {
//                    listAdapter.notifyData(games)
//                }
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
