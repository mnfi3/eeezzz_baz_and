package sabalan.paydar.mohtasham.ezibazi.view.activity

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.wang.avi.AVLoadingIndicatorView

import org.json.JSONException
import org.json.JSONObject

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.api_service.comment.CommentService
import sabalan.paydar.mohtasham.ezibazi.model.Comment
import sabalan.paydar.mohtasham.ezibazi.model.Paginate
import sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.ListCommentAdapter
import sabalan.paydar.mohtasham.ezibazi.system.application.G
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.ScaleInAnimationAdapter
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.recyclerview_animation.adapters.SlideInBottomAnimationAdapter

class ActivityComment : AppCompatActivity() {

    internal var img_back: ImageView
    internal var txt_page_name: TextView
    internal var rcv_comments: RecyclerView

    internal var service: CommentService
    internal var fab_add_comment: FloatingActionButton

    internal var dialog: Dialog
    internal var btn_add_comment: Button
    internal var txt_dialog_head: TextView
    internal var edt_comment: EditText

    internal var game_info_id: Int = 0


    internal var avl_center: AVLoadingIndicatorView
    internal var avl_bottom: AVLoadingIndicatorView
    internal var page_num = 1
    internal var paginate: Paginate? = null
    internal var adapter: ListCommentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        setupViews()
        setTypeFace()

        rcv_comments.layoutManager = GridLayoutManager(this@ActivityComment, 1, GridLayoutManager.VERTICAL, false)

        val extras = intent.extras
        prepareComments(extras, savedInstanceState)


        img_back.setOnClickListener { finish() }


        fab_add_comment.setOnClickListener {
            if (!G.isLoggedIn) {
                val intent = Intent(this@ActivityComment, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            } else {
                dialog.show()
            }
        }



        btn_add_comment.setOnClickListener {
            insertComment()
            dialog.dismiss()
        }


        rcv_comments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (paginate != null) {
                        if (page_num != paginate!!.last_page) {
                            page_num++
                            avl_bottom.visibility = View.VISIBLE
                            getComments()
                        }
                    }
                }
            }
        })


    }

    private fun setupViews() {
        img_back = findViewById(R.id.img_back)
        txt_page_name = findViewById(R.id.txt_page_name)
        rcv_comments = findViewById(R.id.rcv_comments)
        fab_add_comment = findViewById(R.id.fab_add_comment)


        //dialog
        dialog = Dialog(this@ActivityComment)
        dialog.setContentView(R.layout.custom_dialog_insert_comment)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        btn_add_comment = dialog.findViewById(R.id.btn_add_comment)
        txt_dialog_head = dialog.findViewById(R.id.txt_dialog_head)
        edt_comment = dialog.findViewById(R.id.edt_comment)

        avl_center = findViewById(R.id.avl_center)
        avl_bottom = findViewById(R.id.avl_bottom)

    }

    private fun setTypeFace() {
        txt_page_name.typeface = MyViews.getIranSansLightFont(this@ActivityComment)

        //dialog
        btn_add_comment.typeface = MyViews.getIranSansLightFont(this@ActivityComment)
        txt_dialog_head.typeface = MyViews.getIranSansLightFont(this@ActivityComment)
        edt_comment.typeface = MyViews.getIranSansLightFont(this@ActivityComment)

    }

    private fun prepareComments(extras: Bundle?, savedInstanceState: Bundle?) {
        val game_info_id: Int
        var game_name: String? = ""
        if (extras != null) {
            game_info_id = extras.getInt("GAME_INFO_ID")
            game_name = extras.getString("GAME_NAME")
        } else {
            game_info_id = savedInstanceState!!.getSerializable("GAME_INFO_ID") as Int
            game_name = savedInstanceState.getString("GAME_NAME")
        }
        this.game_info_id = game_info_id

        txt_page_name.text = " نظرات " + game_name!!

        getComments()


    }


    private fun getComments() {
        service = CommentService(this@ActivityComment)
        service.getComments(game_info_id, page_num, CommentService.onCommentsReceived { status, message, comments, paginate ->
            avl_center.visibility = View.INVISIBLE
            avl_bottom.visibility = View.INVISIBLE
            if (status != 1) {
                //          MyViews.makeText(ActivityComment.this, message, Toast.LENGTH_SHORT);
                return@onCommentsReceived
            }
            this@ActivityComment.paginate = paginate
            if (page_num == 1) {
                adapter = ListCommentAdapter(this@ActivityComment, comments)
                val alphaAdapter = SlideInBottomAnimationAdapter(adapter)
                rcv_comments.adapter = ScaleInAnimationAdapter(alphaAdapter)
            } else {
                adapter.notifyData(comments)
            }
        })
    }


    private fun insertComment() {
        if (!checkEntry()) {
            return
        }
        val comment = edt_comment.text.toString()
        val `object` = JSONObject()
        try {
            `object`.put("commentable_id", game_info_id)
            `object`.put("commentable_type", "GameInfo")
            `object`.put("text", comment)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        service = CommentService(this@ActivityComment)
        service.addComment(`object`) { status, message, comment ->
            edt_comment.setText("")
            if (status == 1) {
                MyViews.makeText(this@ActivityComment, "نظر شما با موفقیت ثبت شد", Toast.LENGTH_SHORT)
            } else {
                MyViews.makeText(this@ActivityComment, message, Toast.LENGTH_SHORT)
            }
        }


    }

    private fun checkEntry(): Boolean {
        val text = edt_comment.text.toString()
        if (text.length < 3) {
            MyViews.makeText(this@ActivityComment, "متن شما برای ثبت نظر بسیار کوتاه است", Toast.LENGTH_SHORT)
            return false
        }
        return true
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
