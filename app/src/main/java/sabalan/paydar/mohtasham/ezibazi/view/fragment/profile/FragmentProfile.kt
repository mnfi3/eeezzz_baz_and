package sabalan.paydar.mohtasham.ezibazi.view.fragment.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wang.avi.AVLoadingIndicatorView
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONException
import org.json.JSONObject
import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.account.AccountService
import sabalan.paydar.mohtasham.ezibazi.api_service.account.LoginCheckerService
import sabalan.paydar.mohtasham.ezibazi.api_service.account.UserDetailService
import sabalan.paydar.mohtasham.ezibazi.api_service.payment.FinanceService
import sabalan.paydar.mohtasham.ezibazi.api_service.ticket.TicketService
import sabalan.paydar.mohtasham.ezibazi.model.Finance
import sabalan.paydar.mohtasham.ezibazi.model.User
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.config.AppConfig
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.UserPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityLogin
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityMenu
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityMobile
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityTicket
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews


class FragmentProfile : Fragment() {

    internal lateinit var context: Context

    internal lateinit var btn_login: Button
    internal lateinit var lyt_user_detail: LinearLayout
    internal lateinit var lyt_logout: LinearLayout
    internal lateinit var lyt_ticket: LinearLayout
    internal lateinit var txt_full_name: TextView
    internal lateinit var txt_user_balance: TextView
    internal lateinit var txt_show_user_balance: TextView
    internal lateinit var btn_charge_account: Button
    internal lateinit var btn_settlement: Button
    internal lateinit var txt_ticket: TextView
    internal lateinit var txt_logout: TextView
    internal lateinit var txt_show_admin_accounts: TextView
    internal lateinit var txt_rules: TextView
    internal lateinit var txt_new_ticket_count: TextView
    internal lateinit var txt_show_setting: TextView
    internal lateinit var txt_show_video: TextView
    internal lateinit var swch_show_video: SwitchCompat


    internal lateinit var view: View

    internal lateinit var prefManager: UserPrefManager
    internal lateinit var settingPrefManager: SettingPrefManager

    internal lateinit var dialog: Dialog
    internal lateinit var btn_go_to_bank: Button
    internal lateinit var txt_dialog_head: TextView
    internal lateinit var edt_amount: EditText
    internal lateinit var avl_pay: AVLoadingIndicatorView
    internal var amount = 0
    internal var is_to_change = true

    internal var is_pause = false

    private var new_tickets_runnable: Runnable? = null
    private var new_tickets_handler: Handler? = null
    private var finance_runnable: Runnable? = null
    private var finance_handler: Handler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_profile, container, false)

        context = getContext()!!



        setupViews()
        setTypeFace()

        getNewTicketsCount()
        getUserFinance()

        prefManager = UserPrefManager(context)
        settingPrefManager = SettingPrefManager(context)

        //get new tickets count (async)
        //    receiveNewTicketsCount();
        initializeSetting()


        initDetailViews()

        if(G.isLoggedIn == false){
            initializeLogin()
        }




        lyt_ticket.setOnClickListener {
            //reset tickets notification
            txt_new_ticket_count.text = "0"
            txt_new_ticket_count.visibility = View.INVISIBLE

            val intent = Intent(context, ActivityTicket::class.java)
            startActivity(intent)
        }

        lyt_logout.setOnClickListener {
            val service = AccountService(context)
            service.logout(onLogoutComplete = object : AccountService.onLogoutComplete{
                override fun onComplete(status: Int, message: String) {
                    if (status == 1) {
                        logoutUser()
                    } else {
                        MyViews.makeText((activity as AppCompatActivity?)!!, message, Toast.LENGTH_SHORT)
                    }
                }
            });
        }



        btn_charge_account.setOnClickListener {
            if (!G.isLoggedIn) {
                val intent = Intent(activity, ActivityLogin::class.java)
                startActivity(intent)
            } else {
                dialog.show()
            }
        }

        btn_settlement.setOnClickListener {
//            val intent = Intent(context, ActivitySettlement::class.java)
//            startActivity(intent)

            val intent = Intent(context, ActivityMobile::class.java)
            intent.putExtra("TARGET", "settlement");
            startActivity(intent)
        }



        btn_login.setOnClickListener {
            val intent = Intent(context, ActivityLogin::class.java)
            context!!.startActivity(intent)
        }



        edt_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                //        String text = edt_amount.getText().toString();
                //        text = HelperText.splitedPersianToLatin(text);
                //        amount = Integer.valueOf(text);
                //        String splitedText = HelperText.split_price(text);
                //        edt_amount.setText(splitedText);
                //        text = HelperText.splitedPersianToLatin(edt_amount.getText().toString());
                //        int price = Integer.valueOf(text);
                //        Toast.makeText(getContext(), "amount = " + amount, Toast.LENGTH_SHORT).show();
                //        Toast.makeText(getContext(), "amount = " + price, Toast.LENGTH_SHORT).show();
            }
        })


        btn_go_to_bank.setOnClickListener(View.OnClickListener {
            if (!checkEntry()) {
                return@OnClickListener
            }
            increaseCredit()
        })

        swch_show_video.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                settingPrefManager.savePlayVideo(1)
            } else {
                settingPrefManager.savePlayVideo(0)
            }
        }

        return view
    }


    private fun setupViews() {
        btn_login = view.findViewById(R.id.btn_login)
        lyt_user_detail = view.findViewById(R.id.lyt_user_detail)
        lyt_logout = view.findViewById(R.id.lyt_logout)
        lyt_ticket = view.findViewById(R.id.lyt_ticket)
        txt_full_name = view.findViewById(R.id.txt_full_name)
        txt_user_balance = view.findViewById(R.id.txt_user_balance)
        txt_show_user_balance = view.findViewById(R.id.txt_show_user_balance)
        btn_charge_account = view.findViewById(R.id.btn_charge_account)
        btn_settlement = view.findViewById(R.id.btn_settlement)
        txt_ticket = view.findViewById(R.id.txt_ticket)
        txt_logout = view.findViewById(R.id.txt_logout)
        txt_show_admin_accounts = view.findViewById(R.id.txt_show_admin_accounts)
        txt_rules = view.findViewById(R.id.txt_rules)
        txt_new_ticket_count = view.findViewById(R.id.txt_new_ticket_count)
        txt_show_setting = view.findViewById(R.id.txt_show_setting)
        txt_show_video = view.findViewById(R.id.txt_show_video)
        swch_show_video = view.findViewById(R.id.swch_show_video)

        dialog = Dialog(context!!)
        dialog.setContentView(R.layout.custom_dialog_increase_credit)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        btn_go_to_bank = dialog.findViewById(R.id.btn_go_to_bank)
        txt_dialog_head = dialog.findViewById(R.id.txt_dialog_head)
        edt_amount = dialog.findViewById(R.id.edt_amount)
        avl_pay = dialog.findViewById(R.id.avl_pay)
    }

    private fun setTypeFace() {
        btn_login.typeface = MyViews.getIranSansLightFont(context!!)
        txt_full_name.typeface = MyViews.getIranSansMediumFont(context!!)
        txt_user_balance.typeface = MyViews.getIranSansLightFont(context!!)
        txt_show_user_balance.typeface = MyViews.getIranSansLightFont(context!!)
        btn_charge_account.typeface = MyViews.getIranSansLightFont(context!!)
        btn_settlement.typeface = MyViews.getIranSansLightFont(context!!)
        txt_ticket.typeface = MyViews.getIranSansLightFont(context!!)
        txt_logout.typeface = MyViews.getIranSansLightFont(context!!)
        txt_show_admin_accounts.typeface = MyViews.getIranSansLightFont(context!!)
        txt_rules.typeface = MyViews.getIranSansLightFont(context!!)
        txt_new_ticket_count.typeface = MyViews.getIranSansUltraLightFont(context!!)
        txt_show_setting.typeface = MyViews.getIranSansLightFont(context!!)
        txt_show_video.typeface = MyViews.getIranSansLightFont(context!!)

        btn_go_to_bank.typeface = MyViews.getIranSansUltraLightFont(context!!)
        txt_dialog_head.typeface = MyViews.getIranSansUltraLightFont(context!!)
        edt_amount.typeface = MyViews.getIranSansUltraLightFont(context!!)

    }


    private fun initializeLogin(){
        val loginCheckerService = LoginCheckerService(context)
        loginCheckerService.check(User(), object : LoginCheckerService.onLoginCheckComplete{
            override fun onComplete(isLoggedIn: Boolean, full_name: String) {
                G.isLoggedIn = isLoggedIn
                initDetailViews()
            }
        })
    }

    private fun initDetailViews(){
        if(G.isLoggedIn == false){
            lyt_user_detail.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }else {
            lyt_user_detail.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
//          getUserDetail();
            txt_full_name.text =  UserPrefManager(getContext()!!).user.full_name;
            getUserFinance();
        }
    }

    private fun initializeSetting() {
        val is_play_video = settingPrefManager.playVideos
        if (is_play_video == 2) {
            settingPrefManager.savePlayVideo(1)
            swch_show_video.isChecked = true
        } else if (is_play_video == 0) {
            swch_show_video.isChecked = false
        } else if (is_play_video == 1) {
            swch_show_video.isChecked = true
        }
    }


    private fun logoutUser() {
        prefManager = UserPrefManager(context!!)
        val user = User()
        user.token = ""
        user.full_name = ""
        prefManager.saveUser(user)
        G.isLoggedIn = false
        G.initializeLogin()

        lyt_user_detail.visibility = View.GONE
        btn_login.visibility = View.VISIBLE

        if (ActivityMenu.instance != null) {
            ActivityMenu.instance!!.finish()
            val intent = Intent(context, ActivityMenu::class.java)
            startActivity(intent)
        }
    }


    private fun getUserFinance() {
        val service = UserDetailService(context)
        service.getUserFinance(onFinanceReceivedComplete = object : UserDetailService.onFinanceReceivedComplete{
            override fun onComplete(status: Int, message: String, finance: Finance) {
                if (status == 1) {
                    txt_user_balance.text = HelperText.split_price(finance.user_balance) + "  تومان  "
                }
            }
        })
    }


    private fun getNewTicketsCount() {
        val service = TicketService(context)
        service.getNewTicketsCount(onNewTicketsCount = object : TicketService.onNewTicketsCount{
            override fun onReceived(status: Int, message: String, count: Int) {
                if (status == 1) {
                    if (count > 0) {
                        txt_new_ticket_count.text = count.toString()
                        txt_new_ticket_count.visibility = View.VISIBLE
                        //add notification count to app icon
                        ShortcutBadger.applyCount(context, count)
                    }
                }
            }
        })
    }


    private fun checkEntry(): Boolean {
        val text = edt_amount.text.toString()
        if (text.length < 1) {
            MyViews.makeText((activity as AppCompatActivity?)!!, "هیچ مبلغی وارد نشده است", Toast.LENGTH_SHORT)
            return false
        } else if (Integer.parseInt(text) < 100) {
            MyViews.makeText((activity as AppCompatActivity?)!!, "مبلغ وارد شده حداقل باید 100 تومان باشد", Toast.LENGTH_SHORT)
            return false
        }
        return true
    }

    private fun receiveNewTicketsCount() {
        new_tickets_handler = Handler()
        new_tickets_runnable = object : Runnable {
            override fun run() {
                getNewTicketsCount()
                if (G.isLoggedIn) {
                    new_tickets_handler!!.postDelayed(this, AppConfig.NEW_TICKETS_CHECK_TIME_MS.toLong())
                } else {
                    return
                }
            }
        }

        new_tickets_handler!!.post(new_tickets_runnable)

    }


    private fun receiveUserFinance() {
        finance_handler = Handler()
        finance_runnable = object : Runnable {
            override fun run() {
                getUserFinance()
                if (G.isLoggedIn) {
                    finance_handler!!.postDelayed(this, AppConfig.TIME_GET_USER_FINANCE_REFRESH_MS.toLong())
                } else {
                    return
                }
            }
        }

        finance_handler!!.post(finance_runnable)

    }


    private fun increaseCredit() {
        avl_pay.visibility = View.VISIBLE
        val service = FinanceService(context)
        val `object` = JSONObject()
        amount = Integer.parseInt(edt_amount.text.toString())
        try {
            `object`.put("amount", amount)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        service.getPayUrl(`object`,  object : FinanceService.onCreditComplete{
            override fun onComplete(status: Int, message: String, url: String) {
                avl_pay.visibility = View.INVISIBLE
                if (status == 1) {
                    edt_amount.setText("")
                    dialog.dismiss()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                } else {
                    MyViews.makeText((activity as AppCompatActivity?)!!, message, Toast.LENGTH_SHORT)
                }
            }
        })

    }


    override fun onPause() {
        super.onPause()
        is_pause = true
        //    getUserFinance();
        //    getNewTicketsCount();
        //    new_tickets_handler.removeCallbacks(new_tickets_runnable);
        //    finance_handler.removeCallbacks(finance_runnable);
    }

    override fun onResume() {
        super.onResume()
        getUserFinance()
        getNewTicketsCount()
        //    receiveUserFinance();
        //    receiveNewTicketsCount();
    }

    override fun onStop() {
        super.onStop()
        //    new_tickets_handler.removeCallbacks(new_tickets_runnable);
        //    finance_handler.removeCallbacks(finance_runnable);
    }

    override fun onStart() {
        super.onStart()
        getNewTicketsCount()
        getUserFinance()
    }


}
