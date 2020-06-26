package ir.easybazi.recyclerview_adapter.user_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import ir.easybazi.R
import ir.easybazi.model.ShopRequest
import ir.easybazi.system.helper.HelperDate
import ir.easybazi.system.helper.HelperText
import ir.easybazi.view.activity.ActivityShowShop
import ir.easybazi.view.custom_views.my_views.MyViews

class ListShopRequestAdapter(private val context: Context, private val requests: MutableList<ShopRequest>) : RecyclerView.Adapter<ListShopRequestAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_shop_request, parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val request = requests[position]
        if(request.game == null) return;
        if(request.game!!.app_cover_photo_url!!.length > 0) {
            val image_url = request.game!!.app_cover_photo_url
            Picasso.with(context).load(image_url)
                    //      .noFade()
                    //      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    //      .skipMemoryCache()
                    //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
                    //.error(context.getResources().getDrawable(R.drawable.default_no_image))
                    .into(holder.img_game)
        }

        holder.txt_name.text = request.game!!.name
        //      holder.txt_release_date.setText("تاریخ ارائه : " + game.getProduction_date());
        //      holder.txt_rating.setText("rate : " + "4.5");
        holder.txt_region.text = "Region : " + request.game!!.region!!

        holder.btn_price.text = HelperText.split_price(request.game_price) + " تومان "

        if (request.is_finish === 1) {
            holder.txt_status.text = "تمام شده"
        } else if (request.is_finish === 0 && request.is_delivered === 1) {
            holder.txt_status.text = "دریافت شده"
        } else if (request.is_finish === 0 && request.is_delivered === 0 && request.is_sent === 1) {
            holder.txt_status.text = "ارسال شده"
        } else if (request.is_finish === 0 && request.is_delivered === 0 && request.is_sent === 0) {
            holder.txt_status.text = "در حال ارسال"
        }

        holder.txt_date.text = HelperDate().timestampToPersian(request.created_at!!)






        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityShowShop::class.java)
            intent.putExtra("ID", request.game!!.id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return requests.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val img_game: RoundedImageView
        internal val txt_name: TextView
        internal val txt_rating: TextView
        internal val txt_region: TextView
        internal val txt_release_date: TextView
        internal val btn_price: Button
        internal val txt_show_status: TextView
        internal val txt_status: TextView
        internal val txt_date: TextView
        internal val txt_show_date: TextView

        init {
            img_game = itemView.findViewById(R.id.img_game)
            txt_name = itemView.findViewById(R.id.txt_name)
            txt_rating = itemView.findViewById(R.id.txt_rating)
            txt_region = itemView.findViewById(R.id.txt_region)
            txt_release_date = itemView.findViewById(R.id.txt_release_date)
            btn_price = itemView.findViewById(R.id.btn_price)
            txt_show_status = itemView.findViewById(R.id.txt_show_status)
            txt_status = itemView.findViewById(R.id.txt_status)
            txt_date = itemView.findViewById(R.id.txt_date)
            txt_show_date = itemView.findViewById(R.id.txt_show_date)


            setTypeFace()
        }

        private fun setTypeFace() {
            txt_name.typeface = MyViews.getRobotoLightFont(context)
            txt_rating.typeface = MyViews.getRobotoLightFont(context)
            txt_region.typeface = MyViews.getRobotoLightFont(context)
            txt_release_date.typeface = MyViews.getIranSansMediumFont(context)
            btn_price.typeface = MyViews.getIranSansLightFont(context)

            txt_show_status.typeface = MyViews.getIranSansLightFont(context)
            txt_status.typeface = MyViews.getIranSansLightFont(context)
            txt_date.typeface = MyViews.getIranSansLightFont(context)
            txt_show_date.typeface = MyViews.getIranSansLightFont(context)
        }


    }


    fun notifyData(requests1: List<ShopRequest>) {
        //Log.d("notifyData ", myList.size() + "");
        if (requests1.size > 0) {
            for (i in requests1.indices) {
                this.requests.add(requests1[i])
            }
            this.notifyItemInserted(requests.size - 1)

            //notifyDataSetChanged();
        }
    }


    fun clear() {
        val size = requests.size
        requests.clear()
        notifyItemRangeRemoved(0, size)
    }


}