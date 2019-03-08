package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView


import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.makeramen.roundedimageview.RoundedImageView
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.squareup.picasso.Picasso

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.RentService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.model.RentType
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail.FragmentRelatedPost
import sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail.FragmentRelatedRents


class ActivityShowRent : AppCompatActivity() {


    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView
    internal lateinit var app_bar: AppBarLayout
    internal lateinit var collapsing_toolbar: CollapsingToolbarLayout
    internal lateinit var toolbar: Toolbar

    internal lateinit var vdo_game: VideoView
    internal lateinit var slider_game: SliderLayout
    internal lateinit var img_game_cover: RoundedImageView
    internal lateinit var txt_name: TextView
    internal lateinit var txt_console: TextView
    internal lateinit var txt_genres: TextView
    internal lateinit var txt_show_genres: TextView
    internal lateinit var txt_release_date: TextView
    internal lateinit var txt_rate: TextView
    internal lateinit var txt_detail_age_class: TextView
    internal lateinit var txt_region: TextView
    internal lateinit var txt_show_region: TextView
    internal lateinit var img_detail_console_icon: RoundedImageView
    internal lateinit var txt_detail_release_date: TextView
    internal lateinit var img_rate_star: ImageView
    internal lateinit var expand_text_view: ExpandableTextView

    internal var game: Game? = null

    internal lateinit var service: RentService

    internal lateinit var nested_scroll: NestedScrollView
    internal lateinit var txt_rent_period: TextView
    internal lateinit var btn_rent: Button
    internal lateinit var btn_rent_day_count_7: Button
    internal lateinit var btn_rent_day_count_10: Button
    internal lateinit var btn_rent_day_count_20: Button
    internal lateinit var btn_rent_day_count_30: Button
    internal lateinit var btn_comments: Button

    internal lateinit var ft: FragmentTransaction


    //for rent type initialize
    internal var rentTypes: List<RentType>? = G.rentTypes
    internal val rent_day_7 = 7
    internal val rent_day_10 = 10
    internal val rent_day_20 = 20
    internal val rent_day_30 = 30
    internal var current_rent_day: Int = 0
    internal var rent_price_percent_7: Int = 0
    internal var rent_price_percent_10: Int = 0
    internal var rent_price_percent_20: Int = 0
    internal var rent_price_percent_30: Int = 0
    internal var is_ready_rent_types = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_rent)


        setupViews()
        setTypeFace()


        val extras = intent.extras
        prepareGame(extras, savedInstanceState)





        img_back.setOnClickListener { this@ActivityShowRent.finish() }


        btn_comments.setOnClickListener(View.OnClickListener {
            if (game == null) {
                return@OnClickListener
            }
            val intent = Intent(this@ActivityShowRent, ActivityComment::class.java)
            intent.putExtra("GAME_INFO_ID", game!!.game_info_id)
            intent.putExtra("GAME_NAME", game!!.name)
            this@ActivityShowRent.startActivity(intent)
        })


        btn_rent.setOnClickListener { goToPurchase() }





        btn_rent_day_count_7.setOnClickListener {
            if (is_ready_rent_types) {
                setRentDay(rent_day_7)
            }
        }

        btn_rent_day_count_10.setOnClickListener {
            if (is_ready_rent_types) {
                setRentDay(rent_day_10)
            }
        }

        btn_rent_day_count_20.setOnClickListener {
            if (is_ready_rent_types) {
                setRentDay(rent_day_20)
            }
        }

        btn_rent_day_count_30.setOnClickListener {
            if (is_ready_rent_types) {
                setRentDay(rent_day_30)
            }
        }


    }

    private fun setupViews() {
        img_back = findViewById<View>(R.id.img_back) as ImageView
        txt_page_name = findViewById<View>(R.id.txt_page_name) as TextView

        app_bar = findViewById<View>(R.id.app_bar) as AppBarLayout
        collapsing_toolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        vdo_game = findViewById(R.id.vdo_game)
        slider_game = findViewById(R.id.slider_game)
        img_game_cover = findViewById<View>(R.id.img_game_cover) as RoundedImageView
        txt_name = findViewById<View>(R.id.txt_name) as TextView
        txt_console = findViewById<View>(R.id.txt_console) as TextView
        txt_genres = findViewById<View>(R.id.txt_genres) as TextView
        txt_show_genres = findViewById<View>(R.id.txt_show_genres) as TextView
        txt_release_date = findViewById<View>(R.id.txt_release_date) as TextView
        txt_rate = findViewById<View>(R.id.txt_rate) as TextView
        img_rate_star = findViewById<View>(R.id.img_rate_star) as ImageView
        txt_region = findViewById(R.id.txt_region)
        txt_show_region = findViewById(R.id.txt_show_region)

        //bottom of detail
        txt_detail_age_class = findViewById<View>(R.id.txt_detail_age_class) as TextView
        img_detail_console_icon = findViewById(R.id.img_detail_console_icon)
        txt_detail_release_date = findViewById(R.id.txt_detail_release_date)

        expand_text_view = findViewById<View>(R.id.expand_text_view) as ExpandableTextView

        nested_scroll = findViewById<View>(R.id.nested_scroll) as NestedScrollView
        txt_rent_period = findViewById<View>(R.id.txt_rent_period) as TextView
        btn_rent = findViewById(R.id.btn_rent)
        btn_rent_day_count_7 = findViewById(R.id.btn_rent_day_count_7)
        btn_rent_day_count_10 = findViewById(R.id.btn_rent_day_count_10)
        btn_rent_day_count_20 = findViewById(R.id.btn_rent_day_count_20)
        btn_rent_day_count_30 = findViewById(R.id.btn_rent_day_count_30)

        btn_comments = findViewById(R.id.btn_comments)

    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getRobotoLightFont(this@ActivityShowRent)
        txt_name.typeface = MyViews.getRobotoLightFont(this@ActivityShowRent)
        txt_rate.typeface = MyViews.getRobotoLightFont(this@ActivityShowRent)
        txt_region.typeface = MyViews.getRobotoLightFont(this@ActivityShowRent)
        txt_show_region.typeface = MyViews.getRobotoLightFont(this@ActivityShowRent)
        txt_show_genres.typeface = MyViews.getIranSansLightFont(this@ActivityShowRent)
        txt_genres.typeface = MyViews.getIranSansLightFont(this@ActivityShowRent)

        txt_rent_period.typeface = MyViews.getIranSansLightFont(this@ActivityShowRent)
        btn_rent.typeface = MyViews.getIranSansLightFont(this@ActivityShowRent)
        btn_rent_day_count_7.typeface = MyViews.getIranSansBoldFont(this@ActivityShowRent)
        btn_rent_day_count_10.typeface = MyViews.getIranSansBoldFont(this@ActivityShowRent)
        btn_rent_day_count_20.typeface = MyViews.getIranSansBoldFont(this@ActivityShowRent)
        btn_rent_day_count_30.typeface = MyViews.getIranSansBoldFont(this@ActivityShowRent)

        btn_comments.typeface = MyViews.getIranSansLightFont(this@ActivityShowRent)
    }

    private fun prepareGame(extras: Bundle?, savedInstanceState: Bundle?) {
        val id: Int
        if (extras != null) {
            id = extras.getInt("ID")
        } else {
            id = savedInstanceState!!.getSerializable("ID") as Int
        }

        service = RentService(this@ActivityShowRent)
        val onSpecialRentReceived = object : RentService.onSpecialRentReceived{
            override fun onReceived(status: Int, message: String, game: Game) {
                if (status != 1) {
//                    MyViews.makeText(this@ActivityShowRent, message, Toast.LENGTH_SHORT);
                } else {
                    this@ActivityShowRent.game = game
                    getRentTypes()
                    fillViews();
                    setRelatedFragments()
                }
            }
        }

        service.getSpecialRent(id, onSpecialRentReceived)

    }

    private fun getRentTypes() {
        val service = RentService(this@ActivityShowRent)
        val onRentTypesReceived = object : RentService.onRentTypesReceived{
            override fun onReceived(status: Int, message: String, rentTypes: List<RentType>) {
                if (status == 1) {
                    this@ActivityShowRent.rentTypes = rentTypes
                    G.rentTypes = rentTypes
                    initializeRentPricePercent()
                    fillViews()
                    setRentDay(rent_day_7)
                    is_ready_rent_types = true
                } else {
                    MyViews.makeText(this@ActivityShowRent, message, Toast.LENGTH_SHORT)
                }
            }
        }
        service.getRentTypes(onRentTypesReceived)

    }

    private fun fillViews() {
        txt_page_name.text = game!!.name
        txt_name.text = game!!.name
        txt_console.text = game!!.console_name

        var genres = ""
        for (i in 0 until game!!.genres!!.size) {
            genres += game!!.genres!![i]
        }
        txt_genres.text = genres
        txt_release_date.text = "تاریخ عرضه: " + game!!.production_date!!
        //    txt_rate.setText("rate : 4.6");
        txt_region.text = game!!.region

        val cover_image = game!!.app_cover_photo_url
        Picasso.with(this@ActivityShowRent).load(cover_image)
                //      .placeholder()
                .into(img_game_cover)

        val is_play_video = SettingPrefManager(this@ActivityShowRent).playVideos
        if (game!!.videos!!.size > 0 && is_play_video != 0) {
            slider_game.visibility = View.INVISIBLE
            playVideo()
        } else {
            vdo_game.visibility = View.INVISIBLE
            setSlider()
        }


        //game detail
        expand_text_view.text = game!!.description
        txt_detail_age_class.text = "+" + game!!.age_class
        if (game!!.console_name!!.contains("xbox")) {
            img_detail_console_icon.setImageResource(R.drawable.ic_xbox)
        } else if (game!!.console_name!!.contains("ps")) {
            img_detail_console_icon.setImageResource(R.drawable.ic_playstation)
        }

        txt_detail_release_date.text = game!!.production_date!!.substring(0, 4)


    }


    private fun goToPurchase() {
        if (game == null || rentTypes == null || game!!.count < 1) {
            return
        }

        if (!G.isLoggedIn) {
            val intent = Intent(this@ActivityShowRent, ActivityLogin::class.java)
            startActivity(intent)
            return
        }

        var rent_type_id = 0
        for (i in rentTypes!!.indices) {
            if (current_rent_day == rentTypes!![i].day_count) {
                rent_type_id = rentTypes!![i].id
                break
            }
        }

        val intent = Intent(this@ActivityShowRent, ActivityPurchase::class.java)
        intent.putExtra("TYPE", "RENT")
        intent.putExtra("ID", game!!.id)
        intent.putExtra("RENT_TYPE_ID", rent_type_id)
        startActivity(intent)


    }


    private fun setSlider() {
        for (i in 0 until game!!.app_main_photos_url!!.size) {
            val textSliderView = TextSliderView(this@ActivityShowRent)
            textSliderView
                    .image(game!!.app_main_photos_url!![i]).scaleType = BaseSliderView.ScaleType.Fit

            slider_game.addSlider(textSliderView)
        }

        slider_game.startAutoCycle()
        slider_game.setPresetTransformer(SliderLayout.Transformer.DepthPage)
        slider_game.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        slider_game.setCustomAnimation(DescriptionAnimation())
        slider_game.setDuration(5000)
    }


    private fun playVideo() {
        //    getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //    MediaController mediaController = new MediaController(ActivityShowRent.this);
        //    mediaController.setAnchorView(vdo_game);
        //    vdo_game.setMediaController(mediaController);

        vdo_game.setVideoURI(Uri.parse(game!!.videos!![0].url))
        vdo_game.requestFocus()
        vdo_game.setOnPreparedListener { mediaPlayer ->
//            mediaPlayer.setVolume(0f, 0f)
            //        vdo_game.setVisibility(View.VISIBLE);
            slider_game.visibility = View.INVISIBLE
            vdo_game.start()
        }
        vdo_game.setOnCompletionListener { mediaPlayer ->
//            mediaPlayer.setVolume(0f, 0f)
            vdo_game.start()
        }
    }

    private fun setRelatedFragments() {
        ft = supportFragmentManager.beginTransaction()
        //    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
        val relatedRents = FragmentRelatedRents()
        val args = Bundle()
        args.putInt("game_id", game!!.id)
        relatedRents.arguments = args


        val relatedPost = FragmentRelatedPost()
        val args2 = Bundle()
        args2.putInt("game_info_id", game!!.game_info_id)
        relatedPost.arguments = args2


        ft.replace(R.id.lyt_related_rents, relatedRents)
        ft.replace(R.id.lyt_related_posts, relatedPost)
        ft.commit()
    }


    private fun setRentDay(day: Int) {
        resetChoose()

        if (game!!.count == 0) {
            btn_rent.setBackgroundResource(R.drawable.back_list_game_price_finished)
            btn_rent.text = "ناموجود"
            return
        }

        current_rent_day = day
        when (day) {
            rent_day_7 -> {
                btn_rent_day_count_7.setBackgroundResource(R.drawable.back_rent_day_count_ch)
                btn_rent.text = " اجاره با قیمت " + HelperText.split_price(game!!.price * rent_price_percent_7 / 100) + " تومان "
            }

            rent_day_10 -> {
                btn_rent_day_count_10.setBackgroundResource(R.drawable.back_rent_day_count_ch)
                btn_rent.text = " اجاره با قیمت " + HelperText.split_price(game!!.price * rent_price_percent_10 / 100) + " تومان "
            }

            rent_day_20 -> {
                btn_rent_day_count_20.setBackgroundResource(R.drawable.back_rent_day_count_ch)
                btn_rent.text = " اجاره با قیمت " + HelperText.split_price(game!!.price * rent_price_percent_20 / 100) + " تومان "
            }

            rent_day_30 -> {
                btn_rent_day_count_30.setBackgroundResource(R.drawable.back_rent_day_count_ch)
                btn_rent.text = " اجاره با قیمت " + HelperText.split_price(game!!.price * rent_price_percent_30 / 100) + " تومان "
            }
        }
    }

    private fun resetChoose() {
        btn_rent_day_count_7.setBackgroundResource(R.drawable.back_rent_day_count)
        btn_rent_day_count_10.setBackgroundResource(R.drawable.back_rent_day_count)
        btn_rent_day_count_20.setBackgroundResource(R.drawable.back_rent_day_count)
        btn_rent_day_count_30.setBackgroundResource(R.drawable.back_rent_day_count)
    }


    private fun initializeRentPricePercent() {
        for (i in rentTypes!!.indices) {
            when (rentTypes!![i].day_count) {
                7 -> rent_price_percent_7 = rentTypes!![i].price_percent

                10 -> rent_price_percent_10 = rentTypes!![i].price_percent

                20 -> rent_price_percent_20 = rentTypes!![i].price_percent

                30 -> rent_price_percent_30 = rentTypes!![i].price_percent
            }
        }
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
