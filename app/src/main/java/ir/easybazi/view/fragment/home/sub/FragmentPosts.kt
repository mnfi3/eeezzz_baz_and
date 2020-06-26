package ir.easybazi.view.fragment.home.sub

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ir.easybazi.R
import ir.easybazi.api_service.main.PostService
import ir.easybazi.model.Paginate
import ir.easybazi.model.Post
import ir.easybazi.recyclerview_adapter.PostMainAdapter
import ir.easybazi.view.activity.ActivityListPost
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter


class FragmentPosts : Fragment() {

    internal lateinit var activity: Activity

    internal lateinit var lyt_posts: LinearLayout
    internal lateinit var rcv_posts: RecyclerView
    internal lateinit var apiService: PostService
    internal var page_num = 1
    internal lateinit var txt_show_posts: TextView
    internal lateinit var txt_show_list: TextView
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_posts, container, false)

        activity = getActivity()!!

        setupViews()
        setTypeFace()
        lyt_posts.visibility = View.INVISIBLE

        rcv_posts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        getPosts()

        txt_show_list.setOnClickListener {
            val intent = Intent(context, ActivityListPost::class.java)
            startActivity(intent)
        }

        return view
    }


    private fun setupViews() {
        rcv_posts = view.findViewById(R.id.rcv_posts)
        txt_show_posts = view.findViewById(R.id.txt_show_posts)
        txt_show_list = view.findViewById(R.id.txt_show_list)
        lyt_posts = view.findViewById(R.id.lyt_posts)
    }

    private fun getPosts() {
        apiService = PostService(context!!)
        val onPostsReceived = object : PostService.onPostsReceived{
            override fun onReceived(status: Int, message: String, posts: List<Post>, paginate: Paginate) {
                if (status == 0) {
                    //          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
                } else {
                    val listAdapter = PostMainAdapter(activity, posts as MutableList<Post>)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_posts.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_posts.visibility = View.VISIBLE
                }
            }
        }

        apiService.getMainPosts(page_num, onPostsReceived)
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_left)
        lyt_posts.animation = anim
        anim.start()
    }


    private fun setTypeFace() {
        txt_show_posts.typeface = MyViews.getIranSansMediumFont(context!!)
        txt_show_list.typeface = MyViews.getIranSansLightFont(context!!)
    }


}
