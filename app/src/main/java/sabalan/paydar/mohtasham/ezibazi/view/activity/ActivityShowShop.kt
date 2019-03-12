package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView

import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.makeramen.roundedimageview.RoundedImageView
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.squareup.picasso.Picasso

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.main.ShopService
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText
import sabalan.paydar.mohtasham.ezibazi.system.pref_manager.SettingPrefManager
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail.FragmentRelatedPost
import sabalan.paydar.mohtasham.ezibazi.view.fragment.home.game_detail.FragmentRelatedShops


class ActivityShowShop : AppCompatActivity() {

    internal lateinit var img_back: ImageView
    internal lateinit var txt_page_name: TextView
    internal lateinit var app_bar: AppBarLayout
    internal lateinit var collapsing_toolbar: CollapsingToolbarLayout
    internal lateinit var toolbar: Toolbar
    internal lateinit var vdo_game: VideoView
    internal lateinit var img_game: ImageView
    internal lateinit var slider_game: SliderLayout
    internal lateinit var img_game_cover: RoundedImageView
    internal lateinit var txt_name: TextView
    internal lateinit var txt_console: TextView
    internal lateinit var txt_genres: TextView
    internal lateinit var txt_show_genres: TextView
    internal lateinit var txt_release_date: TextView
    internal lateinit var txt_rate: TextView
    internal lateinit var txt_region: TextView
    internal lateinit var txt_show_region: TextView
    internal lateinit var img_rate_star: ImageView
    internal lateinit var expand_text_view: ExpandableTextView

    internal var game: Game? = null

    internal lateinit var service: ShopService

    internal lateinit var nested_scroll: NestedScrollView
    //  TextView txt_rent_period;
    internal lateinit var btn_shop: Button
    //  Button btn_rent_day_count_10, btn_rent_day_count_20, btn_rent_day_count_30;
    internal lateinit var btn_comments: Button

    internal lateinit var ft: FragmentTransaction


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_shop)

        setupViews()
        setTypeFace()

        //disable activity
        MyViews.freezeEnable(this@ActivityShowShop)

        val extras = intent.extras
        prepareGame(extras, savedInstanceState)

        img_back.setOnClickListener { this@ActivityShowShop.finish() }

        btn_shop.setOnClickListener { goToPurchase() }




        btn_comments.setOnClickListener(View.OnClickListener {
            if (game == null) {
                return@OnClickListener
            }
            val intent = Intent(this@ActivityShowShop, ActivityComment::class.java)
            intent.putExtra("GAME_INFO_ID", game!!.game_info_id)
            intent.putExtra("GAME_NAME", game!!.name)
            this@ActivityShowShop.startActivity(intent)
        })


    }

    private fun setupViews() {
        img_back = findViewById<View>(R.id.img_back) as ImageView
        txt_page_name = findViewById<View>(R.id.txt_page_name) as TextView

        app_bar = findViewById<View>(R.id.app_bar) as AppBarLayout
        collapsing_toolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        vdo_game = findViewById<View>(R.id.vdo_game) as VideoView
        img_game = findViewById<View>(R.id.img_game) as ImageView
        slider_game = findViewById(R.id.slider_game)
        img_game_cover = findViewById<View>(R.id.img_game_cover) as RoundedImageView
        txt_name = findViewById<View>(R.id.txt_name) as TextView
        txt_console = findViewById<View>(R.id.txt_console) as TextView
        txt_show_genres = findViewById<View>(R.id.txt_show_genres) as TextView
        txt_genres = findViewById<View>(R.id.txt_genres) as TextView
        txt_release_date = findViewById<View>(R.id.txt_release_date) as TextView
        txt_rate = findViewById<View>(R.id.txt_rate) as TextView
        img_rate_star = findViewById<View>(R.id.img_rate_star) as ImageView
        txt_show_region = findViewById(R.id.txt_show_region)
        txt_region = findViewById(R.id.txt_region)

        expand_text_view = findViewById<View>(R.id.expand_text_view) as ExpandableTextView

        nested_scroll = findViewById<View>(R.id.nested_scroll) as NestedScrollView
        //    txt_rent_period = (TextView) findViewById(R.id.txt_rent_period);
        btn_shop = findViewById(R.id.btn_shop)

        btn_comments = findViewById(R.id.btn_comments)

    }

    private fun prepareGame(extras: Bundle?, savedInstanceState: Bundle?) {
        val id: Int
        if (extras != null) {
            id = extras.getInt("ID")
        } else {
            id = savedInstanceState!!.getSerializable("ID") as Int
        }

        service = ShopService(this@ActivityShowShop)
        val onSpecialShopReceived = object : ShopService.onSpecialShopReceived{
            override fun onReceived(status: Int, message: String, game: Game) {
                if (status != 1) {
                    //          MyViews.makeText(ActivityShowShop.this, message, Toast.LENGTH_SHORT);
                } else {
                    this@ActivityShowShop.game = game
                    fillViews()
                    setRelatedFragments()
                }
            }
        }
        service.getSpecialShop(id, onSpecialShopReceived)

    }


    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getRobotoRegularFont(this@ActivityShowShop)
        txt_name.typeface = MyViews.getRobotoRegularFont(this@ActivityShowShop)
        txt_rate.typeface = MyViews.getRobotoLightFont(this@ActivityShowShop)
        txt_show_region.typeface = MyViews.getRobotoLightFont(this@ActivityShowShop)
        txt_region.typeface = MyViews.getRobotoLightFont(this@ActivityShowShop)
        txt_show_genres.typeface = MyViews.getIranSansLightFont(this@ActivityShowShop)
        txt_genres.typeface = MyViews.getIranSansLightFont(this@ActivityShowShop)

        //    txt_rent_period.setTypeface(MyViews.getIranSansLightFont(ActivityShowShop.this));
        btn_shop.typeface = MyViews.getIranSansLightFont(this@ActivityShowShop)
        //    btn_rent_day_count_10.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));
        //    btn_rent_day_count_20.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));
        //    btn_rent_day_count_30.setTypeface(MyViews.getIranSansBoldFont(ActivityShowShop.this));

        btn_comments.typeface = MyViews.getIranSansLightFont(this@ActivityShowShop)


    }


    @SuppressLint("SetTextI18n")
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
        var cover_image: String? = ""
        for (i in 0 until game!!.photos!!.size) {
            //      if (game.getPhotos().get(i).getHeight() == R.dimen.photo_cover_height){
            cover_image = game!!.photos!![0].url
            break
            //      }
        }
        Picasso.with(this@ActivityShowShop).load(cover_image)
                //      .placeholder()
                .into(img_game_cover)


        val is_play_video = SettingPrefManager(this@ActivityShowShop).playVideos
        if (game!!.videos!!.size > 0 && is_play_video != 0) {
            slider_game.visibility = View.INVISIBLE
            playVideo()
        } else {
            vdo_game.visibility = View.INVISIBLE
            setSlider()
        }



        if (game!!.count > 0) {
            btn_shop.text = " خرید با قیمت " + HelperText.split_price(game!!.price) + " تومان "
        } else {
            btn_shop.setBackgroundResource(R.drawable.back_list_game_price_finished)
            btn_shop.text = "ناموجود"
        }


//        expand_text_view.text = game!!.description
        expand_text_view.text = Html.fromHtml(game!!.description)


        //enable activity
        MyViews.freezeDisable(this@ActivityShowShop)
    }

    private fun setRelatedFragments() {
        ft = supportFragmentManager.beginTransaction()
        //    ft.setCustomAnimations(R.anim.anim_enter_from_left, R.anim.anim_exit_to_right);
        val relatedShops = FragmentRelatedShops()
        val args = Bundle()
        args.putInt("game_id", game!!.id)
        relatedShops.arguments = args
        //    relatedRents.setId(game.getId());

        val relatedPost = FragmentRelatedPost()
        val args2 = Bundle()
        args2.putInt("game_info_id", game!!.game_info_id)
        relatedPost.arguments = args2


        ft.replace(R.id.lyt_related_shops, relatedShops)
        ft.replace(R.id.lyt_related_posts, relatedPost)
        ft.commit()
    }


    private fun goToPurchase() {
        if (game == null || game!!.count < 1) {
            return
        }

        if (!G.isLoggedIn) {
            val intent = Intent(this@ActivityShowShop, ActivityLogin::class.java)
            startActivity(intent)
            return
        }


        val intent = Intent(this@ActivityShowShop, ActivityPurchase::class.java)
        intent.putExtra("TYPE", "SHOP")
        intent.putExtra("ID", game!!.id)
        startActivity(intent)


    }


    private fun setSlider() {
        for (i in 0 until game!!.app_main_photos_url!!.size) {
            val textSliderView = TextSliderView(this@ActivityShowShop)
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
            vdo_game.start()
        }
        vdo_game.setOnCompletionListener { mediaPlayer ->
//            mediaPlayer.setVolume(0f, 0f)
            vdo_game.start()
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

    companion object {
        private val TAG = "ActivityShowRent"
    }
}
