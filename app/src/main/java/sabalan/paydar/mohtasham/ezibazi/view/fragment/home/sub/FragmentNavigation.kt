package sabalan.paydar.mohtasham.ezibazi.view.fragment.home.sub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.model.User
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.auth.Auth
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityLogin
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityTicket


class FragmentNavigation : Fragment() {

    internal var view: View
    internal var lyt_login: LinearLayout
    internal var lyt_ticket: LinearLayout
    internal var drawerLayout: DrawerLayout
    internal var txt_users_name: TextView
    internal var txt_register_login: TextView

    private var isLoggedIn = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_main_navigation, container, false)
        val view1 = this.activity!!.findViewById<View>(R.id.drawerLayout)
        drawerLayout = view1 as DrawerLayout

        setupViews()


        Auth.loginCheck { user, isLoggedIn ->
            this@FragmentNavigation.isLoggedIn = isLoggedIn
            if (isLoggedIn) {
                txt_users_name.text = user.full_name
                txt_register_login.text = "داشبورد"
            }
        }


        lyt_login.setOnClickListener {
            if (!isLoggedIn) {
                val intent = Intent(G.context, ActivityLogin::class.java)
                startActivity(intent)
                drawerLayout.closeDrawers()
            }
        }

        lyt_ticket.setOnClickListener {
            if (isLoggedIn) {
                val intent = Intent(G.context, ActivityTicket::class.java)
                startActivity(intent)
                drawerLayout.closeDrawers()
            }
        }





        return view
    }

    private fun setupViews() {
        lyt_login = view.findViewById(R.id.lyt_login)
        lyt_ticket = view.findViewById(R.id.lyt_ticket)
        txt_users_name = view.findViewById(R.id.txt_users_name)
        txt_register_login = view.findViewById(R.id.txt_register_login)
    }


    override fun onResume() {
        super.onResume()
        Auth.loginCheck { user, isLoggedIn ->
            this@FragmentNavigation.isLoggedIn = isLoggedIn
            if (isLoggedIn) {
                txt_users_name.text = user.full_name
                txt_register_login.text = "داشبورد"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Auth.loginCheck { user, isLoggedIn ->
            this@FragmentNavigation.isLoggedIn = isLoggedIn
            if (isLoggedIn) {
                txt_users_name.text = user.full_name
                txt_register_login.text = "داشبورد"
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Auth.loginCheck { user, isLoggedIn ->
            this@FragmentNavigation.isLoggedIn = isLoggedIn
            if (isLoggedIn) {
                txt_users_name.text = user.full_name
                txt_register_login.text = "داشبورد"
            }
        }
    }
}
