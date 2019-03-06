package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.wang.avi.AVLoadingIndicatorView

import org.json.JSONException
import org.json.JSONObject

import java.util.regex.Pattern

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.account.AccountService
import sabalan.paydar.mohtasham.ezibazi.model.User
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityRegister : AppCompatActivity() {

    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView
    internal lateinit var edt_full_name: EditText
    internal lateinit var edt_email: EditText
    internal lateinit var edt_password: EditText
    internal lateinit var edt_re_password: EditText
    internal lateinit var txt_show_pass: TextView
    internal lateinit var btn_register: Button
    internal lateinit var service: AccountService
    internal lateinit var chk_show_pass: CheckBox
    internal lateinit var avl_register: AVLoadingIndicatorView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupViews()
        setTypeFace()
        txt_page_name.text = "یجاد حساب کاربری"

        img_back.setOnClickListener { this@ActivityRegister.finish() }


        chk_show_pass.setOnClickListener {
            if (chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT
                edt_re_password.inputType = InputType.TYPE_CLASS_TEXT
            } else if (!chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                edt_re_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }



        btn_register.setOnClickListener(View.OnClickListener {
            if (!checkEntry()) return@OnClickListener
            avl_register.visibility = View.VISIBLE
            val full_name = edt_full_name.text.toString()
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            val `object` = JSONObject()
            try {
                `object`.put("full_name", full_name)
                `object`.put("email", email)
                `object`.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            service = AccountService(this@ActivityRegister)
            val onRegisterComplete = object : AccountService.onRegisterComplete{
                override fun onComplete(status: Int, message: String, token: String, user: User) {
                    avl_register.visibility = View.INVISIBLE
                    MyViews.makeText(this@ActivityRegister, message, Toast.LENGTH_SHORT)
                    if (status == 1) {
                        MyViews.makeText(this@ActivityRegister, "لطفا وارد حساب کاربری خود شوید", Toast.LENGTH_SHORT)
                        val intent = Intent(this@ActivityRegister, ActivityLogin::class.java)
                        intent.putExtra("EMAIL", edt_email.text.toString())
                        intent.putExtra("PASSWORD", edt_password.text.toString())
                        startActivity(intent)
                        this@ActivityRegister.finish()
                    }
                }
            }

            service.register(`object`, onRegisterComplete)
        })


    }

    private fun setupViews() {
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)
        edt_full_name = findViewById(R.id.edt_full_name)
        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        edt_re_password = findViewById(R.id.edt_re_password)
        txt_show_pass = findViewById(R.id.txt_show_pass)
        btn_register = findViewById(R.id.btn_register)
        chk_show_pass = findViewById(R.id.chk_show_pass)
        avl_register = findViewById(R.id.avl_register)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        edt_full_name.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        edt_email.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        edt_password.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        edt_re_password.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        txt_show_pass.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
        btn_register.typeface = MyViews.getIranSansLightFont(this@ActivityRegister)
    }


    private fun checkEntry(): Boolean {
        if (edt_full_name.text.toString().length < 1 ||
                edt_email.text.toString().length < 1 ||
                edt_password.text.toString().length < 1 ||
                edt_re_password.text.toString().length < 1) {
            MyViews.makeText(this@ActivityRegister, "لطفا اطلاعات خود را کامل وارد کنید", Toast.LENGTH_SHORT)
            return false
        } else if (edt_full_name.text.toString().length < 6) {
            MyViews.makeText(this@ActivityRegister, "لطفا نام و نام خانوادگی خود را با دقت وارد کنید", Toast.LENGTH_SHORT)
            return false
        } else if (edt_password.text.toString().length < 6) {
            MyViews.makeText(this@ActivityRegister, "کلمه عبور باید حداقل 6 کاراکتر داشته باشد", Toast.LENGTH_SHORT)
            return false
        } else if (!validEmail(edt_email.text.toString())) {
            MyViews.makeText(this@ActivityRegister, "ایمیل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT)
            return false
        } else if (edt_password.text.toString() != edt_re_password.text.toString()) {
            MyViews.makeText(this@ActivityRegister, "کلمه های عبور وارد شده مطابقت ندارند", Toast.LENGTH_SHORT)
            return false
        }


        return true
    }


    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun validMobile(mobile: String): Boolean {
        if (mobile.length != 11 || mobile[0] != '0') return false

        for (i in 0 until mobile.length) {
            if (mobile[i] > '9' || mobile[i] < '0') {
                return false
            }
        }
        return true
    }


    override fun onStart() {
        super.onStart()

        //    connectivityListener = new ConnectivityListener(lyt_action);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(G.connectivityListener)
    }


}
