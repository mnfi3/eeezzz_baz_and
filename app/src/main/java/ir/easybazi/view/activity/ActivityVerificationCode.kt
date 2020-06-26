package ir.easybazi.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.easybazi.api_service.account.VerificationCodeService
import ir.easybazi.model.VerificationCode
import ir.easybazi.view.custom_views.my_views.MyViews
import kotlinx.android.synthetic.main.activity_mobile.btn_ok
import kotlinx.android.synthetic.main.activity_verification_code.*
import kotlinx.android.synthetic.main.question.*
import org.json.JSONObject


class ActivityVerificationCode : AppCompatActivity() {

    lateinit var target:String
    lateinit var mobile:String
    lateinit var token:String
    lateinit var code:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ir.easybazi.R.layout.activity_verification_code)


        val extras = intent.extras
        if (extras != null) {
            target = extras.getString("TARGET")
            mobile = extras.getString("MOBILE")
        }

        txt_page_name.text = " کد تایید به شماره " + mobile + " ارسال شد "
        img_back.setOnClickListener {
            var intent = Intent(this@ActivityVerificationCode, ActivityMobile::class.java)
            intent.putExtra("TARGET", target)
            intent.putExtra("MOBILE", mobile)
            startActivity(intent)
            this@ActivityVerificationCode.finish();
        }


        btn_ok.setOnClickListener{
            code = edt_code.text.toString()
            if (validateCode()) {
                avl_code.visibility = View.VISIBLE
                if (target == "register") {
                    validateRegisterCode()
                } else if (target == "reset-password") {
                    validatePasswordResetCode()
                }else if (target == "settlement") {
                    validateSettlementCode()
                }
            }
        }



    }

    override fun onBackPressed() {
        var intent = Intent(this@ActivityVerificationCode, ActivityMobile::class.java)
        intent.putExtra("TARGET", target)
        intent.putExtra("MOBILE", mobile)
        startActivity(intent)
        this@ActivityVerificationCode.finish();
    }

    private fun validateCode(): Boolean{
        if(edt_code.text.toString().length < 5){
            MyViews.makeText(this@ActivityVerificationCode, "کد وارد شده اشتباه است", Toast.LENGTH_SHORT)
            return false
        }
        return true
    }

    private fun validateRegisterCode(){
        var service = VerificationCodeService(this@ActivityVerificationCode)
        var json = JSONObject()
        json.put("mobile", mobile)
        json.put("code", code)
        val onVerifyCodeComplete = object : VerificationCodeService.onVerifyCodeComplete{
            override fun onComplete(status: Int, message: String, vc: VerificationCode) {
                avl_code.visibility = View.GONE
                MyViews.makeText(this@ActivityVerificationCode, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                    var intent = Intent(this@ActivityVerificationCode, ActivityRegister::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TOKEN", vc.token)
                    startActivity(intent)
                    this@ActivityVerificationCode.finish()
                }
            }
        }
        service.verifyCode(json, onVerifyCodeComplete);
    }

    private fun validatePasswordResetCode(){
        var service = VerificationCodeService(this@ActivityVerificationCode)
        var json = JSONObject()
        json.put("mobile", mobile)
        json.put("code", code)
        val onVerifyCodeComplete = object : VerificationCodeService.onVerifyCodeComplete{
            override fun onComplete(status: Int, message: String, vc: VerificationCode) {
                avl_code.visibility = View.GONE
                MyViews.makeText(this@ActivityVerificationCode, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                    var intent = Intent(this@ActivityVerificationCode, ActivityResetPassword::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TOKEN", vc.token)
                    startActivity(intent)
                    this@ActivityVerificationCode.finish()
                }
            }
        }
        service.verifyCode(json, onVerifyCodeComplete);
    }

    private fun validateSettlementCode(){
        var service = VerificationCodeService(this@ActivityVerificationCode)
        var json = JSONObject()
        json.put("mobile", mobile)
        json.put("code", code)
        val onVerifyCodeComplete = object : VerificationCodeService.onVerifyCodeComplete{
            override fun onComplete(status: Int, message: String, vc: VerificationCode) {
                avl_code.visibility = View.GONE
                MyViews.makeText(this@ActivityVerificationCode, message, Toast.LENGTH_SHORT)
                if(status == 1) {
                    var intent = Intent(this@ActivityVerificationCode, ActivitySettlement::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("TOKEN", vc.token)
                    startActivity(intent)
                    this@ActivityVerificationCode.finish()
                }
            }
        }
        service.verifyCode(json, onVerifyCodeComplete);
    }
}
