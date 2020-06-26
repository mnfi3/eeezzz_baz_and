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
import ir.easybazi.api_service.main.PostService
import ir.easybazi.model.Paginate
import ir.easybazi.model.Post
import ir.easybazi.recyclerview_adapter.ListPostAdapter
import ir.easybazi.system.application.G
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter

class ActivityListPost : AppCompatActivity() {


    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView

    internal lateinit var rcv_list_post: RecyclerView
    internal lateinit var apiService: PostService


    internal lateinit var avl_center: AVLoadingIndicatorView
    internal lateinit var avl_bottom: AVLoadingIndicatorView
    internal var page_num = 1
    internal var paginate: Paginate? = null
    internal lateinit var listAdapter: ListPostAdapter

    @SuppressLint("WrongConstant")
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
        val onPostsReceived = object : PostService.onPostsReceived{
            override fun onReceived(status: Int, message: String, posts: List<Post>, paginate: Paginate) {
                avl_center.visibility = View.INVISIBLE
                avl_bottom.visibility = View.INVISIBLE
                if (status == 0) {
                    //          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
                } else {
                    this@ActivityListPost.paginate = paginate
                    if (page_num == 1) {
                        listAdapter = ListPostAdapter(this@ActivityListPost, posts as MutableList<Post>)
                        val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
                        rcv_list_post.adapter = ScaleInAnimationAdapter(alphaAdapter)
                    } else {
                        listAdapter.notifyData(posts)
                    }
                }
            }
        }
        apiService.getMainPosts(page_num, onPostsReceived)
//        apiService.getMainPosts(page_num) { status, message, posts, paginate ->
//            avl_center.visibility = View.INVISIBLE
//            avl_bottom.visibility = View.INVISIBLE
//            if (status == 0) {
//                //          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
//            } else {
//                this@ActivityListPost.paginate = paginate
//                if (page_num == 1) {
//                    listAdapter = ListPostAdapter(this@ActivityListPost, posts)
//                    val alphaAdapter = SlideInBottomAnimationAdapter(listAdapter)
//                    rcv_list_post.adapter = ScaleInAnimationAdapter(alphaAdapter)
//                } else {
//                    listAdapter.notifyData(posts)
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
