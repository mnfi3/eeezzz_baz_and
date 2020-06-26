package ir.easybazi.view.fragment.home.game_detail

import android.app.Activity
import android.app.PendingIntent.getActivity
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
import ir.easybazi.api_service.main.RentService
import ir.easybazi.model.Game
import ir.easybazi.recyclerview_adapter.RelatedRentAdapter
import ir.easybazi.view.custom_views.my_views.MyViews
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.security.AccessController.getContext


class FragmentRelatedRents : Fragment() {

    internal lateinit var context: Context
    internal lateinit var activity: Activity

    private var game_id: Int = 0

    internal lateinit var lyt_related_rents: LinearLayout
    internal lateinit var rcv_related_rents: RecyclerView
    internal lateinit var apiService: RentService
    internal var page_num = 1
    internal lateinit var txt_related_rents: TextView

    internal lateinit var view: View
    fun setId(game_id: Int) {
        this.game_id = game_id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_related_rents, container, false)

        context = getContext()!!
        activity = getActivity()!!

        setupViews()
        setTypeFace()
        lyt_related_rents.visibility = View.GONE

        rcv_related_rents.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true)

        game_id = arguments!!.getInt("game_id")
        getRelatedRents()




        return view
    }

    private fun setupViews() {
        rcv_related_rents = view.findViewById(R.id.rcv_related_rents)
        txt_related_rents = view.findViewById(R.id.txt_related_rents)
        lyt_related_rents = view.findViewById(R.id.lyt_related_rents)
    }

    private fun setTypeFace() {
        txt_related_rents.typeface = MyViews.getIranSansMediumFont(context!!)
    }


    private fun getRelatedRents() {
        apiService = RentService(context)
        val onRelatedRentsReceived = object : RentService.onRelatedRentsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>) {
                if (status == 0) {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                } else {
                    if (games.size > 0) {
                        val listAdapter = RelatedRentAdapter(activity, games as MutableList<Game>)
                        val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                        rcv_related_rents.adapter = AlphaInAnimationAdapter(alphaAdapter)
                        setAnimation()
                        lyt_related_rents.visibility = View.VISIBLE
                    }
                }
            }
        }

        apiService.getRelatedRents(game_id, onRelatedRentsReceived)
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_right)
        lyt_related_rents.animation = anim
        anim.start()
    }

    companion object {


        private val TAG = "FragmentRelatedRents"
    }

}
