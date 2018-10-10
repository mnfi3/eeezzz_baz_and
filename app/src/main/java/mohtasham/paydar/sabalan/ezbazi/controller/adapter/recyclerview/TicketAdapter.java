package mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.helper.HelperDate;
import mohtasham.paydar.sabalan.ezbazi.controller.system.helper.HelperText;
import mohtasham.paydar.sabalan.ezbazi.model.Ticket;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ListViewHolder>{


  private Context context;
  private List<Ticket> tickets;

  public TicketAdapter(Context context, List<Ticket> tickets){

    this.context = context;
    this.tickets = tickets;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_ticket,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final Ticket ticket = tickets.get(position);


    holder.txt_created_at.setText(HelperText.toPersianNumber(new HelperDate().timestampToPersianFull(ticket.getCreated_at())));

    if (ticket.getIs_user_sent() == 0) {

      holder.txt_message.setText("پشتیبانی : " + "\n" + ticket.getText());
      holder.txt_message.setBackgroundResource(R.drawable.back_ticket_item2);
      holder.lyt_ticket_item.setGravity(Gravity.LEFT);
    }else {
      holder.txt_message.setText(ticket.getText());
      holder.txt_message.setBackgroundResource(R.drawable.back_ticket_item1);
      holder.lyt_ticket_item.setGravity(Gravity.RIGHT);
    }



    if (ticket.getIs_user_sent() == 1 && ticket.getIs_seen() == 1){
      holder.img_is_seen.setVisibility(View.VISIBLE);
    }else {
      holder.img_is_seen.setVisibility(View.INVISIBLE);
    }






//    holder.itemView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//      }
//    });


  }

  @Override
  public int getItemCount() {
    return tickets.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private TextView txt_message;
    private ImageView img_is_seen;
    private TextView txt_created_at;
    private RelativeLayout lyt_ticket_item;

    public ListViewHolder(View itemView) {
      super(itemView);
      txt_message = itemView.findViewById(R.id.txt_message);
      img_is_seen = itemView.findViewById(R.id.img_is_seen);
      txt_created_at = itemView.findViewById(R.id.txt_created_at);
      lyt_ticket_item = itemView.findViewById(R.id.lyt_ticket_item);



      setTypeFace();
    }

    private void setTypeFace(){
      txt_message.setTypeface(MyViews.getIranSansUltraLightFont(context));
      txt_created_at.setTypeface(MyViews.getIranSansMediumFont(context));
    }



  }


  public void notifyData(List<Ticket> newTickets) {
    //Log.d("notifyData ", myList.size() + "");
    if (newTickets.size() > 0) {
      for (int i = 0; i < newTickets.size(); i++) {
        this.tickets.add(newTickets.get(i));
      }
      this.notifyItemInserted(tickets.size() - 1);

      //notifyDataSetChanged();
    }
  }


  public void clear() {
    final int size = tickets.size();
    tickets.clear();
    notifyItemRangeRemoved(0, size);
  }





}