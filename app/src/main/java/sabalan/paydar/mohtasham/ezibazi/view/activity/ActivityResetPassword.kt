package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_resset_password.*
import kotlinx.android.synthetic.main.part_show_game_media.*
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.account.AccountService
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityResetPassword : AppCompatActivity() {

    lateinit var mobile:String
    lateinit var token:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resset_password)


        val extras = intent.extras
        if (extras != null) {
            mobile = extras.getString("MOBILE")
            token = extras.getString("TOKEN")
        }

        txt_page_name.text = "ایجاد حساب کاربری"

        img_back.setOnClickListener { this@ActivityResetPassword.finish() }


        chk_show_pass.setOnClickListener {
            if (chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT
                edt_re_password.inputType = InputType.TYPE_CLASS_TEXT
            } else if (!chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                edt_re_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }

        btn_ok.setOnClickListener {
            avl_password.visibility = View.VISIBLE
            if(checkEntry()){
                changePassword()
            }
        }
    }




    private fun checkEntry(): Boolean{
        if (edt_password.text.toString().length < 6) {
            MyViews.makeText(this@ActivityResetPassword, "کلمه عبور باید حداقل 6 کاراکتر داشته باشد", Toast.LENGTH_SHORT)
            return false
        }else if (edt_password.text.toString() != edt_re_password.text.toString()) {
            MyViews.makeText(this@ActivityResetPassword, "کلمه های عبور وارد شده مطابقت ندارند", Toast.LENGTH_SHORT)
            return false
        }
        return true
    }


    private fun changePassword(){
        var service = AccountService(this@ActivityResetPassword)
        var json = JSONObject()
        json.put("mobile", mobile)
        json.put("token", token)
        json.put("password", edt_password.text.toString())
        var onResetPasswordComplete = object : AccountService.onResetPasswordComplete{
            override fun onComplete(status: Int, message: String) {
                avl_password.visibility = View.GONE
                MyViews.makeText(this@ActivityResetPassword, message, Toast.LENGTH_SHORT)
                if (status == 1){
                    var intent = Intent(this@ActivityResetPassword, ActivityLogin::class.java)
                    intent.putExtra("MOBILE", mobile)
                    intent.putExtra("PASSWORD", edt_password.text.toString())
                    startActivity(intent)
                }
            }
        }
        service.resetPassword(json, onResetPasswordComplete);
    }
}
