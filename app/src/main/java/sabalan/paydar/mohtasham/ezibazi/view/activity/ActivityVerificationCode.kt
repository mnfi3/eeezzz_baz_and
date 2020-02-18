package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mobile.btn_ok
import kotlinx.android.synthetic.main.activity_verification_code.*
import kotlinx.android.synthetic.main.question.*
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.account.VerificationCodeService
import sabalan.paydar.mohtasham.ezibazi.model.VerificationCode
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityVerificationCode : AppCompatActivity() {

    lateinit var target:String
    lateinit var mobile:String
    lateinit var token:String
    lateinit var code:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)


        val extras = intent.extras
        if (extras != null) {
            target = extras.getString("TARGET")
            mobile = extras.getString("MOBILE")
        }

        txt_page_name.text = " کد تایید به شماره " + mobile + " ارسال شد "
        img_back.setOnClickListener {
            this@ActivityVerificationCode.finish();
        }


        btn_ok.setOnClickListener{
            code = edt_code.text.toString()
            avl_code.visibility = View.VISIBLE
            if(target == "register"){
                validateRegisterCode()
            }
        }



    }

    private fun validateCode(){
        if(edt_code.text.toString().length < 5){
            MyViews.makeText(this@ActivityVerificationCode, "کد وارد شده اشتباه است", Toast.LENGTH_SHORT)
        }
    }

    private fun validateRegisterCode(){
        var service = VerificationCodeService(this@ActivityVerificationCode)
        var json = JSONObject()
        json.put("mobile", mobile)
        json.put("code", code)
        val onVerifyCodeComplete = object : VerificationCodeService.onVerifyCodeComplete{
            override fun onComplete(status: Int, message: String, vc: VerificationCode) {
                avl_code.visibility = View.GONE
                if(status == 0) MyViews.makeText(this@ActivityVerificationCode, message, Toast.LENGTH_SHORT)
                else {
                    var intent = Intent(this@ActivityVerificationCode, ActivityRegister::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TOKEN", vc.token)
                    startActivity(intent)
                }
            }


        }

        service.verifyCode(json, onVerifyCodeComplete);
    }

    private fun validatePasswordRressetCode(){

    }
}
