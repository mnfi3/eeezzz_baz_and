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
import sabalan.paydar.mohtasham.ezibazi.api_service.main.PostService
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.Post
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListPostAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter

class ActivityListPost : AppCompatActivity() {


    internal var img_back: ImageView
    internal var txt_page_name: TextView

    internal var rcv_list_post: RecyclerView
    internal var apiService: PostService


    internal var avl_center: AVLoadingIndicatorView
    internal var avl_bottom: AVLoadingIndicatorView
    internal var page_num = 1
    internal var paginate: Paginate? = null
    internal var listAdapter: ListPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_post)

        setupViews()
        setTypeFace()

        txt_page_name.text = "جدیدترین اخبار"

        img_back.setOnClickListener { this@ActivityListPost.finish() }


        rcv_list_post.layoutManager = GridLayoutManager(this@ActivityListPost, 1, GridLayoutManager.VERTICAL, false)
        getPosts()


        rcv_list_post.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (paginate != null) {
                        if (page_num != paginate!!.last_page) {
                            page_num++
                            avl_bottom.visibility = View.VISIBLE
                            getPosts()
                        }
                    }
                }
            }
        })


    }

    private fun setupViews() {
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)
        rcv_list_post = findViewById(R.id.rcv_list_post)
        avl_center = findViewById(R.id.avl_center)
        avl_bottom = findViewById(R.id.avl_bottom)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityListPost)
    }


    private fun getPosts() {
        apiService = PostService(this@ActivityListPost)
        apiService.getMainPosts(page_num) { status, message, posts, paginate ->
            avl_center.visibility = View.INVISIBLE
            avl_bottom.visibility = View.INVISIBLE
            if (status == 0) {
                //          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
            } else {
                this@ActivityListPost.paginate = paginate
                if (page_num == 1) {
                    listAdapter = ListPostAdapter(this@ActivityListPost, posts)
                    val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
                    rcv_list_post.adapter = ScaleInAnimationAdapter(alphaAdapter)
                } else {
                    listAdapter.notifyData(posts)
                }
            }
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
