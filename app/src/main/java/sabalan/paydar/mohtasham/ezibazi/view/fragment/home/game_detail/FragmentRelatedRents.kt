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
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.RelatedRentAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentRelatedRents : Fragment() {

    private var game_id: Int = 0

    internal var lyt_related_rents: LinearLayout
    internal var rcv_related_rents: RecyclerView
    internal var apiService: RentService
    internal var page_num = 1
    internal var txt_related_rents: TextView

    internal var view: View
    fun setId(game_id: Int) {
        this.game_id = game_id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_related_rents, container, false)
        setupViews()
        setTypeFace()
        lyt_related_rents.visibility = View.GONE

        rcv_related_rents.layoutManager = LinearLayoutManager(G.context, LinearLayoutManager.HORIZONTAL, true)

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
        apiService.getRelatedRents(game_id) { status, message, games ->
            if (status == 0) {
                //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
            } else {
                if (games.size > 0) {
                    val listAdapter = RelatedRentAdapter(activity, games)
                    val alphaAdapter = AlphaInAnimationAdapter(listAdapter)
                    rcv_related_rents.adapter = AlphaInAnimationAdapter(alphaAdapter)
                    setAnimation()
                    lyt_related_rents.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setAnimation() {
        val anim = AnimationUtils.loadAnimation(G.context, R.anim.anim_enter_from_right)
        lyt_related_rents.animation = anim
        anim.start()
    }

    companion object {


        private val TAG = "FragmentRelatedRents"
    }

}
