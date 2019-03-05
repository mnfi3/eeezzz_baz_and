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
import sabalan.paydar.mohtasham.ezibazi.api_service.main.ShopService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.RelatedShopAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentRelatedShops : Fragment() {

    internal var game_id: Int = 0

    internal var lyt_related_shops: LinearLayout
    internal var rcv_related_shops: RecyclerView
    internal var apiService: ShopService
    internal var page_num = 1
    internal var txt_related_shops: TextView


    internal var view: View
    fun setId(game_id: Int) {
        this.game_id = game_id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_related_shops, container, false)
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
        apiService.getRelatedShops(game_id) { status, message, games ->
            if (status == 0) {
                //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
            } else {
                if (games.size > 0) {
                    val listAdapter = RelatedShopAdapter(activity, games)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_related_shops.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_related_shops.visibility = View.VISIBLE
                }
            }
        }
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
