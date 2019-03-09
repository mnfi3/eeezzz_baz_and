package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.wang.avi.AVLoadingIndicatorView

import org.json.JSONException
import org.json.JSONObject

import java.util.regex.Pattern

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.Urls
import sabalan.paydar.mohtasham.ezibazi.api_service.account.AccountService
import sabalan.paydar.mohtasham.ezibazi.api_service.firebase.FireBaseApiService
import sabalan.paydar.mohtasham.ezibazi.model.Device
import sabalan.paydar.mohtasham.ezibazi.model.Fcm
import sabalan.paydar.mohtasham.ezibazi.model.User
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.config.AppConfig
import sabalan.paydar.mohtasham.ezibazi.system.hardware.ConnectivityListener
import sabalan.paydar.mohtasham.ezibazi.system.hardware.Hardware
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.FcmPrefManager
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.UserPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ActivityLogin : AppCompatActivity() {


    internal var connectivityListener: ConnectivityListener? = null
    internal lateinit var lyt_root: LinearLayout
    internal lateinit var txt_register: TextView
    internal lateinit var txt_page_name: TextView
    internal lateinit var txt_show_pass: TextView
    internal lateinit var txt_forget_password: TextView
    internal lateinit var img_back: ImageView
    internal lateinit var edt_email: EditText
    internal lateinit var edt_password: EditText
    internal lateinit var btn_login: Button
    internal lateinit var service: AccountService
    internal lateinit var chk_show_pass: CheckBox
    internal lateinit var avl_login: AVLoadingIndicatorView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViews()
        setTypeFace()

        txt_page_name.text = "ورود به حساب کاربری"


        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras != null) {
                val email = extras.getString("EMAIL")
                val password = extras.getString("PASSWORD")
                edt_email.setText(email)
                edt_password.setText(password)
            }
        } else {
            val email = savedInstanceState.getSerializable("EMAIL") as String
            val password = savedInstanceState.getSerializable("PASSWORD") as String
            edt_email.setText(email)
            edt_password.setText(password)
        }





        img_back.setOnClickListener { this@ActivityLogin.finish() }


        chk_show_pass.setOnClickListener {
            if (chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_CLASS_TEXT
            } else if (!chk_show_pass.isChecked) {
                edt_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }


        txt_forget_password.setOnClickListener {
            var url = AppConfig.MAIN_URL
            url = url.substring(0, url.length - 4)
            url += Urls.PASSWORD_RESET
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }



        txt_register.setOnClickListener {
            val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
            startActivity(intent)
        }


        btn_login.setOnClickListener(View.OnClickListener {
            if (!checkEntry()) return@OnClickListener
            avl_login.visibility = View.VISIBLE
            MyViews.freezeEnable(this@ActivityLogin)
            Hardware.hideKeyboard(this@ActivityLogin)
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            val `object` = JSONObject()
            try {
                `object`.put("email", email)
                `object`.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            service = AccountService(this@ActivityLogin)
            val onLoginComplete = object : AccountService.onLoginComplete{
                override fun onComplete(status: Int, message: String, token: String, user: User) {
                    avl_login.visibility = View.INVISIBLE
                    MyViews.freezeDisable(this@ActivityLogin)
//                    MyViews.makeText(this@ActivityLogin, message, Toast.LENGTH_SHORT)
                    if (status == 1) {
                        val prefManager = UserPrefManager(this@ActivityLogin)
                        prefManager.saveUser(user)
                        G.isLoggedIn = true
                        G.initializeLogin()
                        //update user fcm token in server
                        sendFcmInfoToServer()

                        //restart ActivityMenu
                        ActivityMenu.instance!!.finish()
                        val intent = Intent(this@ActivityLogin, ActivityMenu::class.java)
                        startActivity(intent)
                        this@ActivityLogin.finish()
                    }
                }
            }

            service.login(`object`, onLoginComplete)
//            service.login(`object`) { status, message, token, user ->
//                avl_login.visibility = View.INVISIBLE
//                MyViews.makeText(this@ActivityLogin, message, Toast.LENGTH_SHORT)
//                if (status == 1) {
//                    val prefManager = UserPrefManager(this@ActivityLogin)
//                    prefManager.saveUser(user)
//                    G.isLoggedIn = true
//                    G.initializeLogin()
//                    //update user fcm token in server
//                    sendFcmInfoToServer()
//
//                    //restart ActivityMenu
//                    ActivityMenu.instance!!.finish()
//                    val intent = Intent(this@ActivityLogin, ActivityMenu::class.java)
//                    startActivity(intent)
//                    this@ActivityLogin.finish()
//                }
//            }
        })

    }


    //  private void restartFragments(){
    //    Fragment frg, frg2 = null;
    //    frg = getSupportFragmentManager().findFragmentByTag("2");
    //    frg2 = getSupportFragmentManager().findFragmentByTag("4");
    //    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    //    ft.detach(frg);
    //    ft.detach(frg2);
    //    ft.attach(frg);
    //    ft.attach(frg2);
    //    ft.commit();
    //  }

    private fun setupViews() {
        lyt_root = findViewById(R.id.lyt_root)
        txt_register = findViewById(R.id.txt_register)
        img_back = findViewById(R.id.img_back)
        edt_email = findViewById(R.id.edt_email)
        edt_password = findViewById(R.id.edt_password)
        btn_login = findViewById(R.id.btn_login)
        chk_show_pass = findViewById(R.id.chk_show_pass)
        avl_login = findViewById(R.id.avl_login)
        txt_page_name = findViewById(R.id.txt_page_name)
        txt_show_pass = findViewById(R.id.txt_show_pass)
        txt_forget_password = findViewById(R.id.txt_forget_password)
    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityLogin)
        edt_email.typeface = MyViews.getIranSansLightFont(this@ActivityLogin)
        edt_password.typeface = MyViews.getIranSansLightFont(this@ActivityLogin)
        btn_login.typeface = MyViews.getIranSansLightFont(this@ActivityLogin)
        txt_show_pass.typeface = MyViews.getIranSansLightFont(this@ActivityLogin)
        txt_forget_password.typeface = MyViews.getIranSansMediumFont(this@ActivityLogin)
        txt_register.typeface = MyViews.getIranSansMediumFont(this@ActivityLogin)
    }


    private fun sendFcmInfoToServer() {
        val fcm = FcmPrefManager(this@ActivityLogin).fcm
        val `object` = JSONObject()
        try {
            `object`.put("device_type", "ANDROID")
            `object`.put("client_key", fcm.client_key)
            `object`.put("fcm_token", fcm.token)
        } catch (e: JSONException) {
        }

        val service = FireBaseApiService(this@ActivityLogin)
        val onRefreshTokenReceived = object : FireBaseApiService.onRefreshTokenReceived{
            override fun onReceived(status: Int, message: String, device: Device) {

            }
        }
        service.refreshFcmToken(`object`, onRefreshTokenReceived)
    }


    private fun checkEntry(): Boolean {
        if (edt_email.text.toString().length < 1 || edt_password.text.toString().length < 1) {
            MyViews.makeText(this@ActivityLogin, "لطفا اطلاعات خود را کامل وارد کنید", Toast.LENGTH_SHORT)
            return false
        } else if (edt_password.text.toString().length < 6) {
            MyViews.makeText(this@ActivityLogin, "کلمه عبور خود را بادقت وارد کنید", Toast.LENGTH_SHORT)
            return false
        } else if (!validEmail(edt_email.text.toString())) {
            MyViews.makeText(this@ActivityLogin, "ایمیل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT)
            return false
        }
        return true
    }


    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    override fun onStart() {
        super.onStart()

        //    connectivityListener = new ConnectivityListener(lyt_root);
        registerReceiver(G.connectivityListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(G.connectivityListener)
    }
}
