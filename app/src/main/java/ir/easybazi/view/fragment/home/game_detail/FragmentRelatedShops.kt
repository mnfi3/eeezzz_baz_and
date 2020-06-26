package ir.easybazi.view.fragment.home.game_detail

import android.app.Activity
import android.content.Context
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
import ir.easybazi.api_service.main.ShopService
import ir.easybazi.model.Game
import ir.easybazi.recyclerview_adapter.RelatedShopAdapter
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter


class FragmentRelatedShops : Fragment() {


    internal lateinit var context: Context
    internal lateinit var activity: Activity


    internal var game_id: Int = 0

    internal lateinit var lyt_related_shops: LinearLayout
    internal lateinit var rcv_related_shops: RecyclerView
    internal lateinit var apiService: ShopService
    internal var page_num = 1
    internal lateinit var txt_related_shops: TextView


    internal lateinit var view: View
    fun setId(game_id: Int) {
        this.game_id = game_id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_related_shops, container, false)

        context = getContext()!!
        activity = getActivity()!!

        setupViews()
        setTypeFace()
        lyt_related_shops.visibility = View.GONE

        rcv_related_shops.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        game_id = arguments!!.getInt("game_id")
        getRelatedRents()




        return view
    }

    private fun setupViews() {
        rcv_related_shops = view.findViewById(R.id.rcv_related_shops)
        txt_related_shops = view.findViewById(R.id.txt_related_shops)
        lyt_related_shops = view.findViewById(R.id.lyt_related_shops)
    }


    private fun setTypeFace() {
        txt_related_shops.typeface = MyViews.getIranSansMediumFont(context!!)
    }


    private fun getRelatedRents() {
        apiService = ShopService(context)
        val onRelatedShopsReceived = object : ShopService.onRelatedShopsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>) {
                if (status == 0) {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                } else {
                    if (games.size > 0) {
                        val listAdapter = RelatedShopAdapter(activity, games as MutableList<Game>)
                        val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                        rcv_related_shops.adapter = AlphaInAnimationAdapter(alphaAdapter)
                        setAnimation()
                        lyt_related_shops.visibility = View.VISIBLE
                    }
                }
            }
        }

        apiService.getRelatedShops(game_id, onRelatedShopsReceived)
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_right)
        lyt_related_shops.animation = anim
        anim.start()
    }

    companion object {


        private val TAG = "FragmentRelatedShops"
    }
}
