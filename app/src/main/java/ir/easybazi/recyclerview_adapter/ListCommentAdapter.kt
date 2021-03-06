package ir.easybazi.recyclerview_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ms.square.android.expandabletextview.ExpandableTextView
import ir.easybazi.R
import ir.easybazi.model.Comment
import ir.easybazi.system.helper.HelperDate
import ir.easybazi.system.helper.HelperText
import ir.easybazi.view.custom_views.my_views.MyViews

class ListCommentAdapter(private val context: Context, private val comments: MutableList<Comment>) : RecyclerView.Adapter<ListCommentAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val comment = comments[position]


        holder.txt_full_name.text = comment.user!!.full_name
        holder.txt_date.text = HelperText.toPersianNumber(HelperDate().timestampToPersian(comment.created_at!!))
        holder.expand_text_view.text = comment.text




        holder.itemView.setOnClickListener {
            //        Intent intent=new Intent(context, ActivityShowPost.class);
            //        intent.putExtra("ID", post.getId());
            //        intent.putExtra("IMAGE", post.getImageUrl());
            //        intent.putExtra("MAIN_IMAGE", post.getMainImageUrl());
            //        intent.putExtra("DESCRIPTION", post.getDescription());
            //        intent.putExtra("TAGS", post.getTags());
            //        intent.putExtra("SAVE_COUNT", post.getSaveCount());
            //        intent.putExtra("SHARE_COUNT", post.getShareCount());
            //        intent.putExtra("USERNAME", post.getUsername());
            //        intent.putExtra("DESCRIPTION", post.getDescription());
            //        context.startActivity(intent);
        }


    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_full_name: TextView
        val txt_date: TextView
        val expand_text_view: ExpandableTextView
        private val expandable_text: TextView

        init {
            txt_full_name = itemView.findViewById(R.id.txt_full_name)
            txt_date = itemView.findViewById(R.id.txt_date)
            expand_text_view = itemView.findViewById(R.id.expand_text_view)
            expandable_text = itemView.findViewById(R.id.expandable_text)



            setTypeFace()
        }

        private fun setTypeFace() {
            txt_full_name.typeface = MyViews.getIranSansBoldFont(context)
            txt_date.typeface = MyViews.getIranSansUltraLightFont(context)
            expandable_text.typeface = MyViews.getIranSansLightFont(context)
        }


    }


    fun notifyData(newComments: List<Comment>) {
        //Log.d("notifyData ", myList.size() + "");
        if (newComments.size > 0) {
            for (i in newComments.indices) {
                this.comments.add(newComments[i])
            }
            this.notifyItemInserted(comments.size - 1)

            //notifyDataSetChanged();
        }
    }


    fun clear() {
        val size = comments.size
        comments.clear()
        notifyItemRangeRemoved(0, size)
    }


}