package sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter.user_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperDate;
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText;
import sabalan.paydar.mohtasham.ezibazi.model.RentRequest;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowRent;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ListRentRequestAdapter extends RecyclerView.Adapter<ListRentRequestAdapter.ListViewHolder>{


  private Context context;
  private List<RentRequest> requests;

  public ListRentRequestAdapter(Context context, List<RentRequest> requests){

    this.context = context;
    this.requests = requests;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_user_rent_request,parent,false);
    return new ListViewHolder(view);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final RentRequest request = requests.get(position);
    String image_url = request.getGame().getApp_cover_photo_url();
    Picasso.with(context).
      load(image_url)
//    .noFade()
//    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//    .skipMemoryCache()
      //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
      //.error(context.getResources().getDrawable(R.drawable.default_no_image))
      .into(holder.img_game);

    holder.txt_name.setText(request.getGame().getName());
//    holder.txt_release_date.setText("تاریخ ارائه : " + game.getProduction_date());
//    holder.txt_rating.setText("rate : " + "4.5");
    holder.txt_region.setText("Region : " + request.getGame().getRegion());
    holder.txt_rent_day.setText(" اجاره " + HelperText.toPersianNumber(Integer.toString(request.getRent_day_count())) +" روزه");
    holder.btn_price.setText(HelperText.split_price(request.getRent_price()) + " تومان ");

    if(request.getIs_finish() == 1) {
      holder.txt_status.setText("تمام شده");
    }
    else if(request.getIs_finish() == 0 && request.getIs_success() == 1){
      holder.txt_status.setText("دریافت شده");
    }
    else if(request.getIs_finish() == 0 && request.getIs_success() == 0 && request.getIs_sent() == 1){
      holder.txt_status.setText("ارسال شده");
    }
    else if(request.getIs_finish() == 0 && request.getIs_success() == 0 && request.getIs_sent() == 0){
      holder.txt_status.setText("در حال ارسال");
    }

    holder.txt_finished_at.setText(new HelperDate().timestampToPersian(request.getFinished_at()));

    if(request.getIs_finish() == 1){
      holder.txt_finished_at.setVisibility(View.INVISIBLE);
      holder.txt_show_finished_at.setVisibility(View.INVISIBLE);
    }




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ActivityShowRent.class);
        intent.putExtra("ID", request.getGame().getId());
        context.startActivity(intent);
      }
    });


  }

  @Override
  public int getItemCount() {
    return requests.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private RoundedImageView img_game;
    private TextView txt_name;
    private TextView txt_rating;
    private TextView txt_region;
    private TextView txt_release_date;
    private TextView txt_rent_day;
    private Button btn_price;
    private TextView txt_show_status, txt_status, txt_show_finished_at, txt_finished_at;

    public ListViewHolder(View itemView) {
      super(itemView);
      img_game = itemView.findViewById(R.id.img_game);
      txt_name = itemView.findViewById(R.id.txt_name);
      txt_rating = itemView.findViewById(R.id.txt_rating);
      txt_region = itemView.findViewById(R.id.txt_region);
      txt_release_date = itemView.findViewById(R.id.txt_release_date);
      txt_rent_day = itemView.findViewById(R.id.txt_rent_day);
      btn_price = itemView.findViewById(R.id.btn_price);
      txt_show_status = itemView.findViewById(R.id.txt_show_status);
      txt_status = itemView.findViewById(R.id.txt_status);
      txt_show_finished_at = itemView.findViewById(R.id.txt_show_finished_at);
      txt_finished_at = itemView.findViewById(R.id.txt_finished_at);


      setTypeFace();
    }

    private void setTypeFace(){
//      txt_name.setTypeface(MyViews.getIranSansLightFont(context));
//      txt_name.setTypeface(MyViews.getBlackthornsFont(context));
      txt_name.setTypeface(MyViews.getRobotoLightFont(context));
      txt_rating.setTypeface(MyViews.getRobotoLightFont(context));
      txt_region.setTypeface(MyViews.getRobotoLightFont(context));
      txt_release_date.setTypeface(MyViews.getIranSansMediumFont(context));
      txt_rent_day.setTypeface(MyViews.getIranSansLightFont(context));
      btn_price.setTypeface(MyViews.getIranSansLightFont(context));

      txt_show_status.setTypeface(MyViews.getIranSansLightFont(context));
      txt_status.setTypeface(MyViews.getIranSansLightFont(context));
      txt_show_finished_at.setTypeface(MyViews.getIranSansLightFont(context));
      txt_finished_at.setTypeface(MyViews.getIranSansLightFont(context));
    }


  }


  public void notifyData(List<RentRequest> requests1) {
    //Log.d("notifyData ", myList.size() + "");
    if (requests1.size() > 0) {
      for (int i = 0; i < requests1.size(); i++) {
        this.requests.add(requests1.get(i));
      }
      this.notifyItemInserted(requests.size() - 1);

      //notifyDataSetChanged();
    }
  }


  public void clear() {
    final int size = requests.size();
    requests.clear();
    notifyItemRangeRemoved(0, size);
  }







}