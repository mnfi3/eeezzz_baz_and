package ir.easybazi.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.easybazi.R
import ir.easybazi.api_service.account.SettlementService
import ir.easybazi.model.Settlement
import ir.easybazi.view.custom_views.my_views.MyViews
import kotlinx.android.synthetic.main.activity_settlement.*
import kotlinx.android.synthetic.main.toolbar_public.*
import org.json.JSONObject

class ActivitySettlement : AppCompatActivity() {


    lateinit var mobile:String
    lateinit var token:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement)


        val extras = intent.extras
        if (extras != null) {
            mobile = extras.getString("MOBILE")
            token = extras.getString("TOKEN")
        }


        img_back.setOnClickListener {
            this@ActivitySettlement.finish()
        }
        txt_page_name.text = "درخواست تسویه حساب";
        avl_loading.visibility = View.VISIBLE
        getLastSettlement()


        btn_settlement.setOnClickListener {
            avl_loading.visibility = View.VISIBLE
            requestSettlement()
        }

        setTypeFace()
    }



    private fun getLastSettlement(){
        val service = SettlementService(this@ActivitySettlement)
        service.getLastSettlement(object : SettlementService.onLastSettlementReceived{
            override fun onComplete(status: Int, message: String, settlement: Settlement) {
                avl_loading.visibility = View.GONE
                if (status == 1){
                    if(settlement.bank_account_owner_name != "null") edt_owner_name.setText(settlement.bank_account_owner_name)
                    if(settlement.bank_card_number != "null") edt_card_number.setText(settlement.bank_card_number)
                    if(settlement.bank_account_number != "null") edt_account_number.setText(settlement.bank_account_number)
                    if(settlement.bank_shba_number != "null") edt_shba_number.setText(settlement.bank_shba_number)
                }
            }
        })
    }



    private fun requestSettlement(){
        val service = SettlementService(this@ActivitySettlement)
        var json = JSONObject()
        json.put("bank_account_owner_name", edt_owner_name.text.toString())
        json.put("bank_card_number", edt_card_number.text.toString())
        json.put("bank_account_number", edt_account_number.text.toString())
        json.put("bank_shba_number", edt_shba_number.text.toString())
        json.put("mobile", mobile)
        json.put("token", token)

        service.requestSettlement(json, object :SettlementService.onSettlementRequestComplete{
            override fun onComplete(status: Int, message: String, settlement: Settlement) {
                MyViews.makeText(this@ActivitySettlement, message, Toast.LENGTH_LONG)
                avl_loading.visibility = View.GONE
                if(status == 1){
                    this@ActivitySettlement.finish()
                }
            }
        })
    }


    private fun setTypeFace(){
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        txt_field_message.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        edt_owner_name.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        edt_card_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        edt_account_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        edt_shba_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        txt_owner_name.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        txt_card_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        txt_account_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        txt_shba_number.typeface = MyViews.getIranSansLightFont(this@ActivitySettlement)
        btn_settlement.typeface = MyViews.getIranSansMediumFont(this@ActivitySettlement)
    }


}
