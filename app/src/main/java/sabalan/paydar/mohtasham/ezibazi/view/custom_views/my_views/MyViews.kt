package sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import sabalan.paydar.mohtasham.ezibazi.R


class MyViews : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {


        fun makeText(appCompatActivity: AppCompatActivity, text: CharSequence, duration: Int) {

            // Activity activity=appCompatActivity;
            //Context context=appCompatActivity;
            //AppCompatActivity appCompatActivity=context;
            val contextWrapper = ContextWrapper(appCompatActivity)
            val inflater = appCompatActivity.layoutInflater
            val layout = inflater.inflate(R.layout.custom_view_toast,
                    appCompatActivity.findViewById<View>(R.id.lyt_toast) as ViewGroup)
            val txt_toast_text = layout.findViewById<View>(R.id.txt_toast_text) as TextView
            txt_toast_text.text = text
            //add new
            txt_toast_text.typeface = MyViews.getIranSansLightFont(appCompatActivity)

            val toast = Toast(contextWrapper.applicationContext)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }


        fun getIranSansFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/IRAN_Sans.ttf")
        }

        fun getIranSansBoldFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/IRAN_Sans_Bold.ttf")
        }

        fun getIranSansLightFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/IRAN_Sans_Light.ttf")
        }

        fun getIranSansMediumFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/IRAN_Sans_Medium.ttf")
        }

        fun getIranSansUltraLightFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/IRAN_Sans_UltraLight.ttf")
        }


        fun getBlackthornsFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/blackthorns_regular.ttf")
        }

        fun getPlayFairDisplayFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/PlayfairDisplay_Regular.otf")
        }

        fun getPlayFairDisplaySCFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/PlayfairDisplaySC_Regular.otf")
        }


        fun getRobotoLightFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/Roboto_Light.ttf")
        }

        fun getRobotoRegularFont(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "fonts/Roboto_Regular.ttf")
        }
    }
    //blackthorns_regular
}
