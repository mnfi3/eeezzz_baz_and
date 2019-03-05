package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.sub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.PostService
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.model.Post
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.PostMainAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityListPost
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentPosts : Fragment() {

    internal var lyt_posts: LinearLayout
    internal var rcv_posts: RecyclerView
    internal var apiService: PostService
    internal var page_num = 1
    internal var txt_show_posts: TextView
    internal var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_posts, container, false)

        setupViews()
        setTypeFace()
        lyt_posts.visibility = View.INVISIBLE

        rcv_posts.layoutManager = LinearLayoutManager(G.context, LinearLayoutManager.HORIZONTAL, true)
        getPosts()

        txt_show_posts.setOnClickListener {
            val intent = Intent(G.context, ActivityListPost::class.java)
            startActivity(intent)
        }

        return view
    }


    private fun setupViews() {
        rcv_posts = view.findViewById(R.id.rcv_posts)
        txt_show_posts = view.findViewById(R.id.txt_show_posts)
        lyt_posts = view.findViewById(R.id.lyt_posts)
    }

    private fun getPosts() {
        apiService = PostService(G.context)
        apiService.getMainPosts(page_num) { status, message, posts, paginate ->
            if (status == 0) {
                //          MyViews.makeText( ActivityMain, message, Toast.LENGTH_SHORT);
            } else {
                val listAdapter = PostMainAdapter(activity, posts)
                val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                rcv_posts.adapter = AlphaInAnimationAdapter(alphaAdapter)
                setAnimation()
                lyt_posts.visibility = View.VISIBLE
            }
        }
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_left)
        lyt_posts.animation = anim
        anim.start()
    }


    private fun setTypeFace() {
        txt_show_posts.typeface = MyViews.getIranSansMediumFont(context!!)
    }


}
