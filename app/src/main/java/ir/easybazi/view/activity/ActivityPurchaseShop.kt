package ir.easybazi.view.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ir.easybazi.api_service.account.UserDetailService
import ir.easybazi.api_service.address.AddressService
import ir.easybazi.api_service.main.ShopService
import ir.easybazi.api_service.payment.ShopPayService
import ir.easybazi.model.*
import ir.easybazi.system.helper.HelperText
import ir.easybazi.view.custom_views.my_views.MyViews
import kotlinx.android.synthetic.main.activity_purchase_shop.*
import kotlinx.android.synthetic.main.toolbar_public.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ActivityPurchaseShop : AppCompatActivity() {


    internal var type: String? = null
    internal var id: Int = 0
    internal var rent_type_id = 0
    internal var rent_price = 0

    internal lateinit var game: Game
    private var user_balance = 0

    private var last_address: Address? = null

    private var is_use_last_address = false

    private var states = ArrayList<State>()
    private var cities = ArrayList<City>()
    private var selected_state = ""
    private var selected_city = ""

    private var state_id = 0
    private var city_id = 0
    private var new_address_id = 0
    private var sum_price = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ir.easybazi.R.layout.activity_purchase_shop)

        setTypeFace()

        avl_wait.visibility = View.VISIBLE
        MyViews.freezeEnable(this@ActivityPurchaseShop)


        getUserLastAddress()
        getUserBalance()
        getStates()


        val extras = intent.extras
        if (extras != null) {
            type = extras.getString("TYPE")
            id = extras.getInt("ID")
            if (type == TYPE_RENT) {
                rent_type_id = extras.getInt("RENT_TYPE_ID")
            }
        } else {
            type = savedInstanceState!!.getSerializable("TYPE") as String
            id = savedInstanceState.getSerializable("ID") as Int
            if (type == TYPE_RENT) {
                rent_type_id = savedInstanceState.getSerializable("RENT_TYPE_ID") as Int
            }
        }

        when (type) {
//            TYPE_RENT -> setRent()
            TYPE_SHOP -> setShop()
            else -> {
                Log.i(TAG, "onCreate: " + "data error")
                this@ActivityPurchaseShop.finish()
            }
        }

        img_back.setOnClickListener {
            this@ActivityPurchaseShop.finish();
        }


        swch_use_last_address.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                txt_address_text.visibility = View.VISIBLE
                lyt_new_address.visibility = View.GONE
                is_use_last_address = true
            } else {
                txt_address_text.visibility = View.GONE
                lyt_new_address.visibility = View.VISIBLE
                is_use_last_address = false
            }
        }

        btn_open_map.setOnClickListener {
            val intent = Intent(this@ActivityPurchaseShop, ActivityMap::class.java)
            intent.putExtra("STATE", selected_state)
            intent.putExtra("CITY", selected_city)
            startActivityForResult(intent, 1)
        }


        spinner_state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                state_id = states[i].id
                selected_state = states[i].name!!
                avl_wait.visibility = View.VISIBLE
                MyViews.freezeEnable(this@ActivityPurchaseShop)
                getStateCities(state_id)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }


        spinner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                city_id = cities[i].id
                selected_city = cities[i].name!!
//                btn_open_map.visibility = View.VISIBLE
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                btn_open_map.visibility = View.GONE
            }
        }


        btn_discount.setOnClickListener {
            avl_wait.visibility = View.VISIBLE
            MyViews.freezeEnable(this@ActivityPurchaseShop)
            validateDiscount()
        }

        btn_bank_pay.setOnClickListener {
            if (type == TYPE_RENT) {
//                rentGameWithBank()
            } else {

                val alertDialog2 = AlertDialog.Builder( this@ActivityPurchaseShop)
                alertDialog2.setTitle("تایید خرید")
                alertDialog2.setMessage("آیا مطمئن هستید؟")
                alertDialog2.setIcon(ir.easybazi.R.drawable.abc_dialog_material_background)
                alertDialog2.setPositiveButton("بله",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                            avl_wait.visibility = View.VISIBLE
                            MyViews.freezeEnable(this@ActivityPurchaseShop)
                           shopGameWithBank()
                        })
                alertDialog2.setNegativeButton("خیر",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        })

                alertDialog2.show()
            }
        }



        btn_wallet_pay.setOnClickListener {
            if (type == TYPE_RENT) {
//                rentGameWithWallet()
            } else {
                val alertDialog2 = AlertDialog.Builder( this@ActivityPurchaseShop)
                alertDialog2.setTitle("تایید خرید")
                alertDialog2.setMessage("آیا مطمئن هستید؟")
                alertDialog2.setIcon(ir.easybazi.R.drawable.abc_dialog_material_background)
                alertDialog2.setPositiveButton("بله",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                            avl_wait.visibility = View.VISIBLE
                            MyViews.freezeEnable(this@ActivityPurchaseShop)
                            shopGameWithWallet()
                        })
                alertDialog2.setNegativeButton("خیر",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        })

                alertDialog2.show()

            }
        }
    }




    private fun setShop() {
        val shopService = ShopService(this@ActivityPurchaseShop)
        val onSpecialShopReceived = object : ShopService.onSpecialShopReceived{
            override fun onReceived(status: Int, message: String, game: Game) {
                if (status == 1) {
                    this@ActivityPurchaseShop.game = game
                    //fill shop views
                    fillViews()
                }
            }
        }
        shopService.getSpecialShop(id, onSpecialShopReceived)

    }


    @SuppressLint("SetTextI18n")
    private fun fillViews() {
        if (type == TYPE_RENT) {
            txt_page_name.text = game.name + "  اجاره  "
        }else{
            txt_page_name.text = game.name + "  خرید  "
        }
        try {
            Picasso.with(this@ActivityPurchaseShop).load(game.app_cover_photo_url)
                    //      .placeholder()
                    .into(img_game)
        }catch (e: Exception){}
        txt_name.text = game.name
        txt_region.text = "Region : " + game.region!!

        if (type == TYPE_RENT) {
            txt_rent_day.text = "اجاره " + RentType.findById(rent_type_id).day_count + " روزه "
            txt_game_status.text = "اجاره ای"
        } else if (type == TYPE_SHOP) {
            txt_rent_day.text = ""
            txt_game_status.text = "فروشی"
        }


        fillPrices()

    }


    private fun getUserLastAddress() {
        val service = UserDetailService(this@ActivityPurchaseShop)
        val onLastAddressReceived = object : UserDetailService.onLastAddressReceived{
            override fun onComplete(status: Int, message: String, address: Address?) {
                avl_wait.visibility = View.INVISIBLE
                MyViews.freezeDisable(this@ActivityPurchaseShop)
                this@ActivityPurchaseShop.last_address = address
                if (address != null) {
                    fillWithAddress()
                } else {
                    fillWithoutAddress()
                }
            }
        }
        service.getUserLastAddress(onLastAddressReceived)

    }





    private val newAddressObject: JSONObject?
        get() {
            if (!checkEntry()) return null
            val content = edt_address_content.text.toString()
            val phone = edt_phone_number.text.toString()
            var post_code = edt_postcode.text.toString()
            if (post_code.length < 1) post_code = ""

            val `object` = JSONObject()
            try {
                `object`.put("state_id", state_id)
                `object`.put("city_id", city_id)
                `object`.put("post_code", post_code)
                `object`.put("home_phone_number", phone)
                `object`.put("content", content)
                `object`.put("latitude", 0)
                `object`.put("longitude", 0)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return `object`
        }



    private fun fillWithAddress() {
        txt_address_text.text = last_address!!.addressText
        txt_use_last_address.visibility = View.VISIBLE
        swch_use_last_address.isChecked = true
        is_use_last_address = true
        swch_use_last_address.visibility = View.VISIBLE
        lyt_new_address.visibility = View.GONE
    }

    private fun fillWithoutAddress() {
        txt_address_text.visibility = View.INVISIBLE
        swch_use_last_address.isChecked = false
        is_use_last_address = false
        swch_use_last_address.visibility = View.INVISIBLE
        txt_use_last_address.visibility = View.INVISIBLE
        lyt_last_address.visibility = View.GONE

    }


    private fun getStates() {
        val service = AddressService(this@ActivityPurchaseShop)
        val onStatesReceived = object : AddressService.onStatesReceived{
            override fun onComplete(status: Int, message: String, states: ArrayList<State>) {
                this@ActivityPurchaseShop.states = states
                val states_array = ArrayList<String>()
                for (i in states.indices) {
                    states_array.add(states[i].name!!)
                }
                val adapter = ArrayAdapter(this@ActivityPurchaseShop, ir.easybazi.R.layout.item_style_spinner, states_array)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_state.adapter = adapter
            }
        }
        service.getStates(onStatesReceived)

    }


    private fun getStateCities(state_id: Int) {
        val service = AddressService(this@ActivityPurchaseShop)
        val onStateCitiesReceived = object : AddressService.onStateCitiesReceived{
            override fun onComplete(status: Int, message: String, cities: ArrayList<City>) {
                avl_wait.visibility = View.INVISIBLE
                MyViews.freezeDisable(this@ActivityPurchaseShop)
                this@ActivityPurchaseShop.cities = cities
                val city_array = ArrayList<String>()
                for (i in cities.indices) {
                    city_array.add(cities[i].name!!)
                }

                val adapter = ArrayAdapter(this@ActivityPurchaseShop, ir.easybazi.R.layout.item_style_spinner, city_array)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_city.adapter = adapter
            }
        }
        service.getStateCities(state_id, onStateCitiesReceived)

    }


    private fun getUserBalance() {
        val service = UserDetailService(this@ActivityPurchaseShop)
        val onFinanceReceivedComplete = object : UserDetailService.onFinanceReceivedComplete{
            override fun onComplete(status: Int, message: String, finance: Finance) {
                if (status == 1) {
                    user_balance = finance.user_balance
                    //calculate pay with wallet
                }
            }
        }
        service.getUserFinance(onFinanceReceivedComplete)

    }


    @SuppressLint("SetTextI18n")
    private fun fillPrices() {
        sum_price = game.price + rent_price
        txt_game_price.text = HelperText.split_price(game.price) + "\t تومان \t"
        if (type == TYPE_RENT) {
            txt_rent_price.text = HelperText.split_price(rent_price) + "\t تومان \t"
            txt_sum_price.text = HelperText.split_price(game.price + rent_price) + "\t تومان \t"
        } else {
            lyt_rent_price.visibility = View.GONE
            txt_game_price.text = HelperText.split_price(game.price) + "\t تومان \t"
            txt_sum_price.text = HelperText.split_price(game.price) + "\t تومان \t"
        }
    }


    fun validateDiscount(){
        val service = ShopPayService(this@ActivityPurchaseShop)

        val `object` = JSONObject()
        try {
            `object`.put("code", edt_discount.text)
            `object`.put("type", "shop")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val onDiscountValidateComplete = object : ShopPayService.onDiscountValidateComplete{
            override fun onComplete(status: Int, message: String, percent: Int) {
                avl_wait.visibility = View.INVISIBLE
                MyViews.freezeDisable(this@ActivityPurchaseShop)

                if(status == 0)
                    MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                else
                    MyViews.makeText(this@ActivityPurchaseShop, "کد تخفیف اعمال شد", Toast.LENGTH_SHORT)
                txt_sum_price.text = HelperText.split_price(game.price - ((game.price * percent) / 100)) + "\t تومان \t"
            }
        }

        service.validateDiscount(`object`, onDiscountValidateComplete)
    }


    fun shopGameWithWallet() {
        if (sum_price > user_balance) {
            MyViews.makeText(this@ActivityPurchaseShop, "موجودی کیف پول شما کمتر میباشد. لطفا اقدام به شارژ کیف پول خود نمایید", Toast.LENGTH_LONG)
            return
        }


        val service = ShopPayService(this@ActivityPurchaseShop)
        if (is_use_last_address) {
            val `object` = JSONObject()
            try {
                `object`.put("game_id", game.id)
                `object`.put("address_id", last_address!!.id)
                `object`.put("discount_code", edt_discount.text)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val onShopWithWalletComplete = object : ShopPayService.onShopWithWalletComplete{
                override fun onComplete(status: Int, message: String) {
                    avl_wait.visibility = View.INVISIBLE
                    MyViews.freezeDisable(this@ActivityPurchaseShop)

                    if (status == 0) {
                        MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                    } else {
//                        MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                        restartApp()
                    }
                }
            }

            service.shopWithWallet(`object`, onShopWithWalletComplete)


        } else {
            if (newAddressObject == null) return
            val addressService = AddressService(this@ActivityPurchaseShop)
            val onAddressSaved = object : AddressService.onAddressSaved{
                override fun onComplete(status: Int, message: String, address: Address) {
                    avl_wait.visibility = View.INVISIBLE
                    MyViews.freezeDisable(this@ActivityPurchaseShop)

                    if (status == 0) {
                        MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                    } else {
                        new_address_id = address.id
                        val `object` = JSONObject()
                        try {
                            `object`.put("game_id", game.id)
                            `object`.put("address_id", new_address_id)
                            `object`.put("discount_code", edt_discount.text)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        val onShopWithWalletComplete = object : ShopPayService.onShopWithWalletComplete{
                            override fun onComplete(status: Int, message: String) {
                                if (status == 0) {
                                    MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                                } else {
//                                    MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                                    restartApp()
                                }
                            }
                        }
                        service.shopWithWallet(`object`, onShopWithWalletComplete)

                    }
                }
            }

            addressService.saveAddress(newAddressObject!!, onAddressSaved)

        }
    }



    fun shopGameWithBank() {
        val service = ShopPayService(this@ActivityPurchaseShop)
        if (is_use_last_address) {
            val `object` = JSONObject()
            try {
                `object`.put("game_id", game.id)
                `object`.put("address_id", last_address!!.id)
                `object`.put("discount_code", edt_discount.text)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val onShopWithBankComplete = object : ShopPayService.onShopWithBankComplete{
                override fun onComplete(status: Int, message: String, pay_url: String) {
                    avl_wait.visibility = View.INVISIBLE
                    MyViews.freezeDisable(this@ActivityPurchaseShop)
                    if (status == 0) {
                        MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                    } else {
                        openBrowser(pay_url)
                    }
                }
            }

            service.shopWithBank(`object`, onShopWithBankComplete)


        } else {
            if (newAddressObject == null) return
            val addressService = AddressService(this@ActivityPurchaseShop)
            val onAddressSaved = object : AddressService.onAddressSaved{
                override fun onComplete(status: Int, message: String, address: Address) {
                    avl_wait.visibility = View.INVISIBLE
                    MyViews.freezeDisable(this@ActivityPurchaseShop)
                    if (status == 0) {
                        MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                    } else {
                        new_address_id = address.id
                        val `object` = JSONObject()
                        try {
                            `object`.put("game_id", game.id)
                            `object`.put("address_id", new_address_id)
                            `object`.put("discount_code", edt_discount.text)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        val onShopWithBankComplete = object : ShopPayService.onShopWithBankComplete{
                            override fun onComplete(status: Int, message: String, pay_url: String) {
                                avl_wait.visibility = View.INVISIBLE
                                MyViews.freezeDisable(this@ActivityPurchaseShop)
                                if (status == 0) {
                                    MyViews.makeText(this@ActivityPurchaseShop, message, Toast.LENGTH_SHORT)
                                } else {
                                    openBrowser(pay_url)
                                }
                            }
                        }
                        service.shopWithBank(`object`, onShopWithBankComplete)


                    }
                }
            }
            addressService.saveAddress(newAddressObject!!, onAddressSaved)

        }
    }


    private fun checkEntry(): Boolean {
        val address = edt_address_content.text.toString()
        val phone = edt_phone_number.text.toString()
        val post_code = edt_postcode.text.toString()
        if (address.length < 2) {
            MyViews.makeText(this@ActivityPurchaseShop, "آدرس وارد شده بسیار کوتاه میباشد", Toast.LENGTH_SHORT)
            return false
        } else if (address.length > 1990) {
            MyViews.makeText(this@ActivityPurchaseShop, "آدرس وارد شده بسیار طولانی میباشد", Toast.LENGTH_SHORT)
            return false
        } else if (phone.length < 9 || phone.length > 14) {
            MyViews.makeText(this@ActivityPurchaseShop, "شماره تلفن وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT)
            return false
        }

        return true
    }




    private fun setTypeFace() {
        txt_page_name.typeface =  MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)
        txt_show_product.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_region.typeface = MyViews.getRobotoLightFont(this@ActivityPurchaseShop)
        txt_name.typeface = MyViews.getRobotoLightFont(this@ActivityPurchaseShop)
        txt_rent_day.typeface = MyViews.getRobotoLightFont(this@ActivityPurchaseShop)
        txt_game_status.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_address.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_last_address_show.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_address_text.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_use_last_address.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_show_state.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_city.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        edt_address_content.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        edt_phone_number.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        edt_postcode.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        btn_open_map.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_payment.typeface = MyViews.getIranSansMediumFont(this@ActivityPurchaseShop)//medium
        txt_game_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_game_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_rent_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_rent_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_sum_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        txt_show_sum_price.typeface = MyViews.getIranSansLightFont(this@ActivityPurchaseShop)
        btn_wallet_pay.typeface = MyViews.getIranSansUltraLightFont(this@ActivityPurchaseShop)
        btn_bank_pay.typeface = MyViews.getIranSansUltraLightFont(this@ActivityPurchaseShop)
    }


    private fun restartApp() {
        if (ActivityMenu.instance != null) {
            ActivityMenu.instance!!.finish()
            val intent = Intent(this@ActivityPurchaseShop, ActivityMenu::class.java)
            startActivity(intent)
            this@ActivityPurchaseShop.finish()
        }
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    companion object {

        private val TAG = "ActivityPurchaseShop"

        private val TYPE_RENT = "RENT"
        private val TYPE_SHOP = "SHOP"
    }
}
