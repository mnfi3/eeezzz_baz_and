package ir.easybazi.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.easybazi.R
import ir.easybazi.view.fragment.home.sub.FragmentPosts
import ir.easybazi.view.fragment.home.sub.FragmentRents
import ir.easybazi.view.fragment.home.sub.FragmentShops


class FragmentHome : Fragment() {

    //  DrawerLayout drawerLayout;

    internal lateinit var view: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)
        //    View view1 = this.getActivity().findViewById(R.id.drawerLayout);
        setupViews()

        val ft = activity!!.supportFragmentManager.beginTransaction()
        //    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
        ft.replace(R.id.lyt_main_posts, FragmentPosts())
        ft.replace(R.id.lyt_main_rents, FragmentRents())
        ft.replace(R.id.lyt_main_shops, FragmentShops())
        ft.commit()

        //    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        //    ft2.setCustomAnimations(R.anim.anim_enter_from_right, R.anim.anim_exit_to_left);
        //    ft2.commit();


        return view
    }

    private fun setupViews() {

    }

    companion object {

        private val TAG = "FragmentHome"
    }
}
