package sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

import sabalan.paydar.mohtasham.ezibazi.R
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText
import sabalan.paydar.mohtasham.ezibazi.model.Game
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowShop
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews

class ListShopAdapter(private val context: Context, private val games: MutableList<Game>) : RecyclerView.Adapter<ListShopAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_game_shop, parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val game = games[position]
        Picasso.with(context).load(game.app_cover_photo_url)
                //      .noFade()
                //      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //      .skipMemoryCache()
                //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
                //.error(context.getResources().getDrawable(R.drawable.default_no_image))
                .into(holder.img_game)

        holder.txt_name.text = game.name
        holder.txt_release_date.text = "تاریخ ارائه : " + game.production_date!!
        //      holder.txt_rating.setText("rate : 4.8");
        holder.txt_region.text = " Region : " + game.region!!


        if (game.count == 0) {
            holder.btn_price.setBackgroundResource(R.drawable.back_list_game_price_finished)
            holder.btn_price.text = "ناموجود"
        } else {
            holder.btn_price.setBackgroundResource(R.drawable.back_list_game_price)
            holder.btn_price.text = HelperText.split_price(game.price) + " تومان "
        }




        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityShowShop::class.java)
            intent.putExtra("ID", game.id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return games.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val img_game: RoundedImageView
        internal val txt_name: TextView
        internal val txt_rating: TextView
        internal val txt_region: TextView
        internal val txt_release_date: TextView
        internal val btn_price: Button

        init {
            img_game = itemView.findViewById(R.id.img_game)
            txt_name = itemView.findViewById(R.id.txt_name)
            txt_rating = itemView.findViewById(R.id.txt_rating)
            txt_region = itemView.findViewById(R.id.txt_region)
            txt_release_date = itemView.findViewById(R.id.txt_release_date)
            btn_price = itemView.findViewById(R.id.btn_price)


            setTypeFace()
        }


        private fun setTypeFace() {
            txt_name.typeface = MyViews.getRobotoLightFont(context)
            txt_rating.typeface = MyViews.getRobotoLightFont(context)
            txt_region.typeface = MyViews.getRobotoLightFont(context)
            txt_release_date.typeface = MyViews.getIranSansMediumFont(context)
            btn_price.typeface = MyViews.getIranSansMediumFont(context)
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