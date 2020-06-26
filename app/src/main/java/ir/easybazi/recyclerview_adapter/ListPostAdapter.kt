package ir.easybazi.recyclerview_adapter

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
import ir.easybazi.model.Post
import ir.easybazi.system.helper.HelperDate
import ir.easybazi.view.activity.ActivityWebView
import ir.easybazi.view.custom_views.my_views.MyViews

class ListPostAdapter(private val context: Context, private val posts: MutableList<Post>) : RecyclerView.Adapter<ListPostAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_posts, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = posts[position]
        Picasso.with(context).load(post.image_url)
                //      .noFade()
                //      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //      .skipMemoryCache()
                //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
                //.error(context.getResources().getDrawable(R.drawable.default_no_image))
                .into(holder.img_post)

        holder.txt_post_title.text = post.title
        holder.txt_post_date.text = HelperDate().timestampToPersianFull(post.created_at!!)




        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActivityWebView::class.java)
            intent.putExtra("URL", "https://easybazi.ir/blog/" +  posts[position].id + "/" +  posts[position].title)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val img_post: RoundedImageView
        internal val txt_post_title: TextView
        internal val txt_post_date: TextView

        init {
            img_post = itemView.findViewById(R.id.img_post)
            txt_post_title = itemView.findViewById(R.id.txt_post_title)
            txt_post_date = itemView.findViewById(R.id.txt_post_date)


            setTypeFace()
        }

        private fun setTypeFace() {
            txt_post_title.typeface = MyViews.getIranSansMediumFont(context)
            txt_post_date.typeface = MyViews.getIranSansMediumFont(context)
        }


    }


    fun notifyData(newPosts: List<Post>) {
        //Log.d("notifyData ", myList.size() + "");
        if (newPosts.size > 0) {
            for (i in newPosts.indices) {
                this.posts.add(newPosts[i])
            }
            this.notifyItemInserted(posts.size - 1)

            //notifyDataSetChanged();
        }
    }


    fun clear() {
        val size = posts.size
        posts.clear()
        notifyItemRangeRemoved(0, size)
    }


}