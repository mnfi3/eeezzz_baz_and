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
import sabalan.paydar.mohtasham.ezibazi.api_service.main.ShopService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ShopMainAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityListShop
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentShops : Fragment() {

    internal var lyt_shops: LinearLayout
    internal var rcv_shops: RecyclerView
    internal var apiService: ShopService
    internal var page_num = 1
    internal var txt_show_shops_list: TextView
    internal var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_shops, container, false)

        setupViews()
        setTypeFace()
        lyt_shops.visibility = View.INVISIBLE

        rcv_shops.layoutManager = LinearLayoutManager(G.context, LinearLayoutManager.HORIZONTAL, true)
        getShops()

        txt_show_shops_list.setOnClickListener {
            val intent = Intent(G.context, ActivityListShop::class.java)
            startActivity(intent)
        }

        return view
    }


    private fun setupViews() {
        rcv_shops = view.findViewById(R.id.rcv_shops)
        txt_show_shops_list = view.findViewById(R.id.txt_show_shops_list)
        lyt_shops = view.findViewById(R.id.lyt_shops)
    }

    private fun getShops() {
        apiService = ShopService(G.context)
        apiService.getMainShops(1) { status, message, games, paginate ->
            if (status == 0) {
                //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
            } else {
                val listAdapter = ShopMainAdapter(G.context, games)
                val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                rcv_shops.adapter = AlphaInAnimationAdapter(alphaAdapter)
                setAnimation()
                lyt_shops.visibility = View.VISIBLE
            }
        }
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_left)
        lyt_shops.animation = anim
        anim.start()
    }


    private fun setTypeFace() {
        txt_show_shops_list.typeface = MyViews.getIranSansMediumFont(context!!)
    }


}
