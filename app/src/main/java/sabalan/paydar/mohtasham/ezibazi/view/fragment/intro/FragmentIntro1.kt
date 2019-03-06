package sabalan.paydar.mohtasham.ezibazi.view.fragment.intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView

import sabalan.paydar.mohtasham.ezibazi.R


class FragmentIntro1 : Fragment() {

    internal lateinit var view: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_intro1, container, false)

        return view
    }


}
