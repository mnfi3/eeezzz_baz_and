package sabalan.paydar.mohtasham.ezibazi.view.fragment.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.wang.avi.AVLoadingIndicatorView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.user_actvity.UserRequestsService
import sabalan.paydar.mohtasham.ezibazi.model.RentRequest
import sabalan.paydar.mohtasham.ezibazi.model.ShopRequest
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.user_activity.ListRentRequestAdapter
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.user_activity.ListShopRequestAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.AlphaInAnimationAdapter


class FragmentActivity : Fragment() {

    internal lateinit var txt_rents: TextView
    internal lateinit var txt_buys: TextView
    internal lateinit var rcv_rents: RecyclerView
    internal lateinit var rcv_buys: RecyclerView
    internal lateinit var service: UserRequestsService
    internal lateinit var avl_rents: AVLoadingIndicatorView
    internal lateinit var avl_buys: AVLoadingIndicatorView
    internal lateinit var context: Context

    internal lateinit var view: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_activity, container, false)
        //    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
        context = getContext()!!

        setupViews()
        setTypeFace()

        rcv_rents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rcv_buys.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        getUserRentRequests()
        avl_rents.visibility = View.VISIBLE
        getUserShopRequests()
        avl_buys.visibility = View.VISIBLE




        return view
    }

    private fun setupViews() {
        txt_rents = view.findViewById(R.id.txt_rents)
        txt_buys = view.findViewById(R.id.txt_buys)
        rcv_rents = view.findViewById(R.id.rcv_rents)
        rcv_buys = view.findViewById(R.id.rcv_buys)
        avl_rents = view.findViewById(R.id.avl_rents)
        avl_buys = view.findViewById(R.id.avl_buys)
    }

    private fun setTypeFace() {
        txt_rents.typeface = MyViews.getIranSansMediumFont(context)
        txt_buys.typeface = MyViews.getIranSansMediumFont(context)
    }

    private fun getUserRentRequests() {
        service = UserRequestsService(context)
        val onRentRequestsReceived = object : UserRequestsService.onRentRequestsReceived{
            override fun onReceived(status: Int, message: String, requests: List<RentRequest>) {
                avl_rents.visibility = View.INVISIBLE
                if (status != 0) {
                    val adapter = ListRentRequestAdapter(context, requests as MutableList<RentRequest>)
                    val alphaAdapter = AlphaInAnimationAdapter(adapter)
                    rcv_rents.adapter = AlphaInAnimationAdapter(alphaAdapter)
                } else {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                }
            }
        }

        service.getRentRequests(onRentRequestsReceived)
    }

    private fun getUserShopRequests() {
        service = UserRequestsService(context)
        val onShopRequestsReceived = object : UserRequestsService.onShopRequestsReceived{
            override fun onReceived(status: Int, message: String, requests: List<ShopRequest>) {
                avl_buys.visibility = View.INVISIBLE
                if (status == 1) {
                    val adapter = ListShopRequestAdapter(context, requests as MutableList<ShopRequest>)
                    val alphaAdapter = AlphaInAnimationAdapter(adapter)
                    rcv_buys.adapter = AlphaInAnimationAdapter(alphaAdapter)
                } else {
                    //          MyViews.makeText((AppCompatActivity) getActivity(), message, Toast.LENGTH_SHORT);
                }
            }
        }

        service.getShopRequests(onShopRequestsReceived)
    }


    override fun onStart() {
        super.onStart()

        //    getUserRentRequests();
        //    getUserShopRequests();
    }

    override fun onResume() {
        super.onResume()

        //    getUserRentRequests();
        //    getUserShopRequests();
    }


}