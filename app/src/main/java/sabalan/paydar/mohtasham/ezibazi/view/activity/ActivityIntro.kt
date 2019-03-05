package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.AppUsagePrefManager
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.layout_behavior.OnSwipeTouchListener
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro1
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro2
import sabalan.paydar.mohtasham.ezibazi.view.fragment.intro.FragmentIntro3


//create intro view for first time
class ActivityIntro : AppCompatActivity() {

    internal var btn_per: Button
    internal var btn_next: Button

    internal var current_fragment = 1
    internal var ft: FragmentTransaction? = null
    internal var prefManager: AppUsagePrefManager
    internal var intro1: FragmentIntro1
    internal var intro2: FragmentIntro2
    internal var intro3: FragmentIntro3

    internal var lyt_root: RelativeLayout
    internal var lyt_intro: FrameLayout

    internal val fm = supportFragmentManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        intro1 = FragmentIntro1()
        intro2 = FragmentIntro2()
        intro3 = FragmentIntro3()

        prefManager = AppUsagePrefManager(this@ActivityIntro)
        if (prefManager.firstUse == 0) {
            val intent = Intent(this@ActivityIntro, ActivityMenu::class.java)
            startActivity(intent)
            finish()
        }

        val prefManager = SettingPrefManager(this@ActivityIntro)
        prefManager.saveFcmClientKey()

        setupViews()
        setTypeFace()
        initializeFragments()

        btn_per.visibility = View.INVISIBLE

        btn_per.setOnClickListener { prev() }

        btn_next.setOnClickListener { next() }

        lyt_root.setOnTouchListener(object : OnSwipeTouchListener(this@ActivityIntro) {
            override fun onSwipeTop() {
                //        Toast.makeText(ActivityIntro.this, "top", Toast.LENGTH_SHORT).show();
            }

            override fun onSwipeRight() {
                prev()
                //        Toast.makeText(ActivityIntro.this, "right", Toast.LENGTH_SHORT).show();
            }

            override fun onSwipeLeft() {
                next()
                //        Toast.makeText(ActivityIntro.this, "left", Toast.LENGTH_SHORT).show();
            }

            override fun onSwipeBottom() {
                //        Toast.makeText(ActivityIntro.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        })


    }


    private fun setupViews() {
        btn_per = findViewById(R.id.btn_per)
        btn_next = findViewById(R.id.btn_next)
        lyt_root = findViewById(R.id.lyt_root)
        lyt_intro = findViewById(R.id.lyt_intro)
    }

    private fun setTypeFace() {
        btn_per.typeface = MyViews.getIranSansFont(this@ActivityIntro)
        btn_next.typeface = MyViews.getIranSansLightFont(this@ActivityIntro)
    }


    private fun initializeFragments() {
        fm.beginTransaction().add(R.id.lyt_intro, intro1, "1").commit()
        fm.beginTransaction().add(R.id.lyt_intro, intro2, "2").hide(intro2).commit()
        fm.beginTransaction().add(R.id.lyt_intro, intro3, "3").hide(intro3).commit()
    }


    private fun setButtonsAnim(is_next: Boolean) {
        if (!is_next) {
            val rotation = AnimationUtils.loadAnimation(this@ActivityIntro, R.anim.rotate_intro_button2)
            btn_per.startAnimation(rotation)
        } else {
            val rotation2 = AnimationUtils.loadAnimation(this@ActivityIntro, R.anim.rotate_intro_button)
            btn_next.startAnimation(rotation2)
        }
    }

    private fun prev() {
        setButtonsAnim(false)
        if (current_fragment == 2) {
            fm.beginTransaction().hide(intro2).show(intro1).commit()
            btn_per.visibility = View.INVISIBLE
            current_fragment--
        } else if (current_fragment == 3) {
            btn_next.text = "بعدی"
            fm.beginTransaction().hide(intro3).show(intro2).commit()
            btn_per.visibility = View.VISIBLE
            current_fragment--
        }
    }

    private operator fun next() {
        setButtonsAnim(true)
        if (current_fragment == 1) {
            fm.beginTransaction().hide(intro1).show(intro2).commit()
            btn_per.visibility = View.VISIBLE
            current_fragment++
        } else if (current_fragment == 2) {
            fm.beginTransaction().hide(intro2).show(intro3).commit()
            btn_next.text = "تمام"
            current_fragment++
        } else if (current_fragment == 3) {
            prefManager = AppUsagePrefManager(this@ActivityIntro)
            prefManager.saveFirstUse()
            val intent = Intent(this@ActivityIntro, ActivityMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
