package ir.easybazi.view.fragment.home.sub

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
import ir.easybazi.api_service.main.ShopService
import ir.easybazi.model.Game
import ir.easybazi.model.Paginate
import ir.easybazi.recyclerview_adapter.ShopMainAdapter
import ir.easybazi.view.activity.ActivityListShop
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter


class FragmentShops : Fragment() {

    internal lateinit var lyt_shops: LinearLayout
    internal lateinit var rcv_shops: RecyclerView
    internal lateinit var apiService: ShopService
    internal var page_num = 1
    internal lateinit var txt_show_shops_list: TextView
    internal lateinit var txt_show_list: TextView
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_shops, container, false)

        setupViews()
        setTypeFace()
        lyt_shops.visibility = View.INVISIBLE

        rcv_shops.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        getShops()

        txt_show_list.setOnClickListener {
            val intent = Intent(context, ActivityListShop::class.java)
            startActivity(intent)
        }

        return view
    }


    private fun setupViews() {
        rcv_shops = view.findViewById(R.id.rcv_shops)
        txt_show_shops_list = view.findViewById(R.id.txt_show_shops_list)
        txt_show_list = view.findViewById(R.id.txt_show_list)
        lyt_shops = view.findViewById(R.id.lyt_shops)
    }

    private fun getShops() {
        apiService = ShopService(context!!)
        val onShopsReceived = object : ShopService.onShopsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate) {
                if (status == 0) {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                } else {
                    val listAdapter = ShopMainAdapter(context!!, games as MutableList<Game>)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_shops.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_shops.visibility = View.VISIBLE
                }
            }
        }
        apiService.getMainShops(1, onShopsReceived)
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_left)
        lyt_shops.animation = anim
        anim.start()
    }


    private fun setTypeFace() {
        txt_show_shops_list.typeface = MyViews.getIranSansMediumFont(context!!)
        txt_show_list.typeface = MyViews.getIranSansLightFont(context!!)
    }


}
