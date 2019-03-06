package sabalan.paydar.mohtasham.ezibazi.view.fragment.search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView


import com.wang.avi.AVLoadingIndicatorView

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.PostService
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.api_service.main.ShopService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.model.Post
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListPostAdapter
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListRentAdapter
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListShopAdapter
import sabalan.paydar.mohtasham.ezibazi.system.hardware.Hardware
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.CityPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter


class FragmentSearch : Fragment() {

    internal lateinit var context: Context

    internal lateinit var edt_search: EditText

    internal lateinit var txt_search_in: TextView
    internal lateinit var rdg_search: RadioGroup
    internal lateinit var rdo_rent: RadioButton
    internal lateinit var rdo_shop: RadioButton
    internal lateinit var rdo_post: RadioButton
    internal lateinit var avl_search: AVLoadingIndicatorView
    internal lateinit var txt_no_result: TextView
    internal lateinit var rcv_search: RecyclerView
    internal lateinit var img_search: ImageView

    internal lateinit var view: View

    internal var rdo_rent_num = 1
    internal var rdo_shop_num = 2
    internal var rdo_post_num = 3

    internal var current_rdo = rdo_rent_num

    internal lateinit var rentService: RentService
    internal lateinit var shopService: ShopService
    internal lateinit var postService: PostService
    internal lateinit var prefManager: CityPrefManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_search, container, false)

        //    View view1 = getActivity().findViewById();
        context = getContext()!!

        setupViews()
        setTypeFace()

        rcv_search.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)

        img_search.setOnClickListener { search() }

        rdg_search.setOnCheckedChangeListener { radioGroup, i ->
            val rdo_rent_check = rdo_rent.isChecked
            val rdo_shop_check = rdo_shop.isChecked
            val rdo_post_check = rdo_post.isChecked
            if (rdo_rent_check) {
                current_rdo = rdo_rent_num
                //         Toast.makeText(getContext(), "rent",Toast.LENGTH_SHORT).show();
            } else if (rdo_shop_check) {
                current_rdo = rdo_shop_num
                //         Toast.makeText(getContext(), "shop",Toast.LENGTH_SHORT).show();
            } else if (rdo_post_check) {
                current_rdo = rdo_post_num
                //         Toast.makeText(getContext(), "post",Toast.LENGTH_SHORT).show();
            }
            if (edt_search.text.toString().length >= 3) {
                search()
            }
        }

        edt_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false
        }

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //        search();
            }
        })


        return view
    }


    private fun setupViews() {
        edt_search = view.findViewById(R.id.edt_search)
        txt_search_in = view.findViewById(R.id.txt_search_in)
        rdg_search = view.findViewById(R.id.rdg_search)
        rdo_rent = view.findViewById(R.id.rdo_rent)
        rdo_shop = view.findViewById(R.id.rdo_shop)
        rdo_post = view.findViewById(R.id.rdo_post)
        avl_search = view.findViewById(R.id.avl_search)
        txt_no_result = view.findViewById(R.id.txt_no_result)
        rcv_search = view.findViewById(R.id.rcv_search)
        img_search = view.findViewById(R.id.img_search)

    }

    private fun setTypeFace() {
        edt_search.typeface = MyViews.getIranSansLightFont(context!!)
        txt_search_in.typeface = MyViews.getIranSansLightFont(context!!)
        rdo_rent.typeface = MyViews.getIranSansLightFont(context!!)
        rdo_shop.typeface = MyViews.getIranSansLightFont(context!!)
        rdo_post.typeface = MyViews.getIranSansLightFont(context!!)
    }


    private fun search() {
        if (checkEntry()) {
            Hardware.hideKeyboard(activity!!)
            prefManager = CityPrefManager(context!!)
            val city_id = prefManager.cityId
            val text = edt_search.text.toString()
            val `object` = JSONObject()
            try {
                `object`.put("text", text)
                `object`.put("city_id", city_id)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            txt_no_result.visibility = View.GONE
            avl_search.visibility = View.VISIBLE

            emptyRecyclerView()

            when (current_rdo) {
                1 -> searchRents(`object`)

                2 -> searchShops(`object`)

                3 -> searchPosts(`object`)
            }
        }
    }

    private fun searchRents(`object`: JSONObject) {
        rentService = RentService(context)
        val onSearchedRentsReceived = object : RentService.onSearchedRentsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>) {
                avl_search.visibility = View.GONE
                if (games.size > 0) {
                    val adapter = ListRentAdapter(context, games as MutableList<Game>)
                    val alphaAdapter = SlideInBottomAnimationAdapter(adapter)
                    rcv_search.adapter = ScaleInAnimationAdapter(alphaAdapter)
                } else {
                    txt_no_result.visibility = View.VISIBLE
                }
            }
        }

        rentService.getSearchedRents(`object`, onSearchedRentsReceived)

    }

    private fun searchShops(`object`: JSONObject) {
        shopService = ShopService(context)
        val onSearchedShopsReceived = object : ShopService.onSearchedShopsReceived{
            override fun onReceived(status: Int, message: String, games: List<Game>) {
                avl_search.visibility = View.GONE
                if (games.size > 0) {
                    val adapter = ListShopAdapter(context, games as MutableList<Game>)
                    val alphaAdapter = SlideInBottomAnimationAdapter(adapter)
                    rcv_search.adapter = ScaleInAnimationAdapter(alphaAdapter)
                } else {
                    txt_no_result.visibility = View.VISIBLE
                }
            }
        }

        shopService.getSearchedShops(`object`, onSearchedShopsReceived)
    }

    private fun searchPosts(`object`: JSONObject) {
        postService = PostService(context)
        val onSearchedPostsReceived = object : PostService.onSearchedPostsReceived{
            override fun onReceived(status: Int, message: String, posts: List<Post>) {
                avl_search.visibility = View.GONE
                if (posts.size > 0) {
                    val adapter = ListPostAdapter(context, posts as MutableList<Post>)
                    val alphaAdapter = SlideInBottomAnimationAdapter(adapter)
                    rcv_search.adapter = ScaleInAnimationAdapter(alphaAdapter)
                } else {
                    txt_no_result.visibility = View.VISIBLE
                }
            }
        }
        postService.getSearchedPosts(`object`, onSearchedPostsReceived)
    }


    private fun checkEntry(): Boolean {
        val text = edt_search.text.toString()
        return if (text.length < 3) {
            //      MyViews.makeText((AppCompatActivity) getActivity(), "متن شما برای جستجو بسیار کوتاه است", Toast.LENGTH_SHORT);
            false
        } else true
    }

    private fun emptyRecyclerView() {
        val adapter = ListShopAdapter(context, ArrayList())
        rcv_search.adapter = adapter
    }


}
