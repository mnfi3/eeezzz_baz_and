package ir.easybazi.recyclerview_adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

import ir.easybazi.R
import ir.easybazi.model.Game
import ir.easybazi.system.helper.HelperText
import ir.easybazi.view.activity.ActivityShowShop
import ir.easybazi.view.custom_views.my_views.MyViews

class RelatedShopAdapter(private val context: Context, private val games: MutableList<Game>) : RecyclerView.Adapter<RelatedShopAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_fragment_main_games, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val game = games[position]
        val id = game.id
        Picasso.with(context).load(game.app_cover_photo_url)
                //      .noFade()
                //      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //      .skipMemoryCache()
                //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
                //.error(context.getResources().getDrawable(R.drawable.default_no_image))
                .into(holder.img_game)

        holder.txt_game_name.text = game.name
        holder.txt_release_date.text = game.production_date

        if (game.count == 0) {
            holder.txt_game_price.setBackgroundResource(R.drawable.back_menu_game_price_finished)
            holder.txt_game_price.text = "ناموجود"
        } else {
            holder.txt_game_price.setBackgroundResource(R.drawable.back_menu_game_price)
            holder.txt_game_price.text = HelperText.split_price(game.price) + " تومان "
        }





        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityShowShop::class.java)
            //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("ID", id)
            (context as Activity).finish()
            context.startActivity(intent)
            //        Toast.makeText(context, "shop id = "+ game.getId() + " gameInfoId = " + game.getGame_info_id(),Toast.LENGTH_LONG).show();
        }


    }

    override fun getItemCount(): Int {
        return games.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val img_game: RoundedImageView
        internal val txt_game_name: TextView
        internal val txt_release_date: TextView
        internal val txt_game_price: TextView

        init {
            img_game = itemView.findViewById(R.id.img_game)
            txt_game_name = itemView.findViewById(R.id.txt_game_name)
            txt_release_date = itemView.findViewById(R.id.txt_release_date)
            txt_game_price = itemView.findViewById(R.id.txt_game_price)


            setTypeFace()
        }


        private fun setTypeFace() {
            txt_game_name.typeface = MyViews.getRobotoLightFont(context)
            txt_release_date.typeface = MyViews.getIranSansMediumFont(context)
            txt_game_price.typeface = MyViews.getIranSansLightFont(context)
        }


    }


    fun notifyData(games1: List<Game>) {
        //Log.d("notifyData ", myList.size() + "");
        if (games1.size > 0) {
            for (i in games1.indices) {
                this.games.add(games1[i])
            }
            this.notifyItemInserted(games.size - 1)

            //notifyDataSetChanged();
        }
    }


    fun clear() {
        val size = games.size
        games.clear()
        notifyItemRangeRemoved(0, size)
    }


}