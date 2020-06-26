package ir.easybazi.view.fragment.home.sub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.easybazi.R
import ir.easybazi.api_service.main.RentService
import ir.easybazi.model.Game
import ir.easybazi.model.Paginate
import ir.easybazi.recyclerview_adapter.RentMainAdapter
import ir.easybazi.view.activity.ActivityListRent
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter


class FragmentRents : Fragment() {
    internal var activity: AppCompatActivity? = null


    internal lateinit var lyt_rents: LinearLayout
    internal lateinit var rcv_rents: RecyclerView
    internal lateinit var apiService: RentService
    internal var page_num = 1
    internal lateinit var txt_show_rent_list: TextView
    internal lateinit var txt_show_list: TextView
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_rents, container, false)
        setupViews()
        setTypeFace()
        lyt_rents.visibility = View.INVISIBLE

        rcv_rents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        getRents()



        txt_show_list.setOnClickListener {
            val intent = Intent(context, ActivityListRent::class.java)
            startActivity(intent)
        }





        return view
    }

    private fun setupViews() {
        rcv_rents = view.findViewById(R.id.rcv_rents)
        txt_show_rent_list = view.findViewById(R.id.txt_show_rent_list)
        txt_show_list = view.findViewById(R.id.txt_show_list)
        lyt_rents = view.findViewById(R.id.lyt_rents)
    }


    private fun getRents() {
        apiService = RentService(context!!)
        val onRentsReceived = object : RentService.onRentsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>, paginate: Paginate) {
                if (status == 0) {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                } else {
                    val listAdapter = RentMainAdapter(getActivity()!!, games as MutableList<Game>)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_rents.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_rents.visibility = View.VISIBLE
                }
            }
        }
        apiService.getRents(1, onRentsReceived)
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_right)
        lyt_rents.animation = anim
        anim.start()
    }


    private fun setTypeFace() {
        txt_show_rent_list.typeface = MyViews.getIranSansMediumFont(context!!)
        txt_show_list.typeface = MyViews.getIranSansLightFont(context!!)
    }


}
