package ir.easybazi.view.fragment.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ir.easybazi.R
import ir.easybazi.view.custom_views.my_views.MyViews


class FragmentIntro1 : Fragment() {

    internal lateinit var view: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_intro1, container, false)

        var txt_description: TextView
        txt_description = view.findViewById(R.id.txt_description)
        txt_description.setTypeface(MyViews.getIranSansBoldFont(getContext()!!))

        return view
    }


}
