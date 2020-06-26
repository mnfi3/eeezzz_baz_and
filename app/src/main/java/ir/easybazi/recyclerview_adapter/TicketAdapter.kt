package ir.easybazi.recyclerview_adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.easybazi.R
import ir.easybazi.model.Ticket
import ir.easybazi.system.helper.HelperDate
import ir.easybazi.system.helper.HelperText
import ir.easybazi.view.custom_views.my_views.MyViews

class TicketAdapter(private val context: Context, private val tickets: MutableList<Ticket>) : RecyclerView.Adapter<TicketAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val ticket = tickets[position] as Ticket


        holder.txt_created_at.text = HelperText.toPersianNumber(HelperDate().timestampToPersianFull(ticket.created_at!!))

        if (ticket.is_user_sent === 0) {

            holder.txt_message.text = "پشتیبانی : " + "\n" + ticket.text
            holder.txt_message.setBackgroundResource(R.drawable.back_ticket_item2)
            holder.lyt_ticket_item.gravity = Gravity.LEFT
        } else {
            holder.txt_message.text = ticket.text
            holder.txt_message.setBackgroundResource(R.drawable.back_ticket_item1)
            holder.lyt_ticket_item.gravity = Gravity.RIGHT
        }



        if (ticket.is_user_sent === 1 && ticket.is_seen === 1) {
            holder.img_is_seen.visibility = View.VISIBLE
        } else {
            holder.img_is_seen.visibility = View.INVISIBLE
        }


        //    holder.itemView.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View v) {
        //
        //      }
        //    });


    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val txt_message: TextView
        internal val img_is_seen: ImageView
        internal val txt_created_at: TextView
        internal val lyt_ticket_item: RelativeLayout

        init {
            txt_message = itemView.findViewById(R.id.txt_message)
            img_is_seen = itemView.findViewById(R.id.img_is_seen)
            txt_created_at = itemView.findViewById(R.id.txt_created_at)
            lyt_ticket_item = itemView.findViewById(R.id.lyt_ticket_item)



            setTypeFace()
        }

        private fun setTypeFace() {
            txt_message.typeface = MyViews.getIranSansUltraLightFont(context)
            txt_created_at.typeface = MyViews.getIranSansMediumFont(context)
        }


    }


    fun notifyData(newTickets: List<Ticket>) {
        //Log.d("notifyData ", myList.size() + "");
        if (newTickets.size > 0) {
            for (i in newTickets.indices) {
                this.tickets.add(newTickets[i])
            }
            this.notifyItemInserted(tickets.size - 1)

            //notifyDataSetChanged();
        }
    }


    fun clear() {
        val size = tickets.size
        tickets.clear()
        notifyItemRangeRemoved(0, size)
    }


}