package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail

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
import sabalan.paydar.mohtasham.ezibazi.model.Post
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.PostMainAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentRelatedPost : Fragment() {

    internal var game_info_id: Int = 0

    internal var lyt_related_posts: LinearLayout
    internal var rcv_related_posts: RecyclerView
    internal var apiService: PostService
    internal var txt_related_posts: TextView

    internal var view: View
    fun setId(game_info_id: Int) {
        this.game_info_id = game_info_id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_related_post, container, false)
        setupViews()
        setTypeFace()
        lyt_related_posts.visibility = View.GONE

        rcv_related_posts.layoutManager = LinearLayoutManager(G.context, LinearLayoutManager.HORIZONTAL, true)

        game_info_id = arguments!!.getInt("game_info_id")
        getRelatedPosts()

        return view
    }

    private fun setupViews() {
        rcv_related_posts = view.findViewById(R.id.rcv_related_posts)
        txt_related_posts = view.findViewById(R.id.txt_related_posts)
        lyt_related_posts = view.findViewById(R.id.lyt_related_posts)
    }

    private fun setTypeFace() {
        txt_related_posts.typeface = MyViews.getIranSansMediumFont(context!!)
    }

    private fun getRelatedPosts() {
        apiService = PostService(context)
        apiService.getRelatedPosts(game_info_id) { status, message, posts ->
            if (status == 0) {
                //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
            } else {
                if (posts.size > 0) {
                    val listAdapter = PostMainAdapter(activity, posts)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_related_posts.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_related_posts.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right)
        lyt_related_posts.animation = anim
        anim.start()
    }

    companion object {


        private val TAG = "FragmentRelatedPost"
    }

}
