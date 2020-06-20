package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mobile.*
import kotlinx.android.synthetic.main.question.*
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.account.VerificationCodeService
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityMobile : AppCompatActivity() {

    lateinit var target:String
    lateinit var mobile:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile)


        val extras = intent.extras
        if (extras != null) {
            target = extras.getString("TARGET")
            if (extras.getString("MOBILE") != null){
                mobile = extras.getString("MOBILE");
                edt_mobile.setText(mobile)
                checkMobile()
            }
        }


        btn_ok.visibility = View.GONE
        txt_page_name.text = "شماره موبایل خود را وارد کنید"
        img_back.setOnClickListener {
            this@ActivityMobile.finish();
        }

        btn_ok.setOnClickListener{
            mobile = edt_mobile.text.toString()
            avl_mobile.visibility = View.VISIBLE
            if (target == "register"){
                getRegisterCode()
            }else if(target == "reset-password"){
                getPasswordResetCode()
            }else if(target == "settlement"){
                getSettlementCode()
            }
        }






        edt_mobile.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) { checkMobile() }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }


    private fun checkMobile(): Boolean{
        btn_ok.visibility = View.GONE
        var text:String  = edt_mobile.text.toString()
        if (text.length == 0) return false

        if (text.length == 1 && edt_mobile.text.get(0) != '0') {
            MyViews.makeText(this@ActivityMobile, "شماره موبایل باید با 0 شروع شود", Toast.LENGTH_SHORT)
            edt_mobile.text.clear()
            return false
        }

        if(text.length < 11) {
            return false
        }

        btn_ok.visibility = View.VISIBLE
        return true
    }



    private fun getRegisterCode(){
        var service = VerificationCodeService(this@ActivityMobile)
        var json = JSONObject()
        json.put("mobile", mobile)
        val onSendCodeComplete = object : VerificationCodeService.onSendCodeComplete{
            override fun onComplete(status: Int, message: String) {
                avl_mobile.visibility = View.GONE
                MyViews.makeText(this@ActivityMobile, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                   var intent = Intent(this@ActivityMobile, ActivityVerificationCode::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TARGET", target)
                    startActivity(intent)
                    this@ActivityMobile.finish()
                }
            }
        }

        service.requestRegisterCode(json, onSendCodeComplete);
    }



    private fun getPasswordResetCode(){
        var service = VerificationCodeService(this@ActivityMobile)
        var json = JSONObject()
        json.put("mobile", mobile)
        val onSendCodeComplete = object : VerificationCodeService.onSendCodeComplete{
            override fun onComplete(status: Int, message: String) {
                avl_mobile.visibility = View.GONE
                MyViews.makeText(this@ActivityMobile, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                    var intent = Intent(this@ActivityMobile, ActivityVerificationCode::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TARGET", target)
                    startActivity(intent)
                    this@ActivityMobile.finish()
                }
            }
        }

        service.requestPasswordRessetCode(json, onSendCodeComplete);
    }


    private fun getSettlementCode(){
        var service = VerificationCodeService(this@ActivityMobile)
        var json = JSONObject()
        val onSendCodeComplete = object : VerificationCodeService.onSendCodeComplete{
            override fun onComplete(status: Int, message: String) {
                avl_mobile.visibility = View.GONE
                MyViews.makeText(this@ActivityMobile, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                    var intent = Intent(this@ActivityMobile, ActivityVerificationCode::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TARGET", target)
                    startActivity(intent)
                    this@ActivityMobile.finish()
                }
            }
        }

        service.requestSettlementCode(json, onSendCodeComplete);
    }



}
