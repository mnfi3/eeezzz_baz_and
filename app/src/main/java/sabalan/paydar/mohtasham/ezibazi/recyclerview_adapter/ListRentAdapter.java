package sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter;

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
import sabalan.paydar.mohtasham.ezibazi.system.application.G;
import sabalan.paydar.mohtasham.ezibazi.system.helper.HelperText;
import sabalan.paydar.mohtasham.ezibazi.model.Game;
import sabalan.paydar.mohtasham.ezibazi.model.RentType;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityShowRent;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class ListRentAdapter extends RecyclerView.Adapter<ListRentAdapter.ListViewHolder>{


  private Context context;
  private List<Game> games;
  private List<RentType> rentTypes;

  public ListRentAdapter(Context context, List<Game> games){
    this.context = context;
    this.games = games;
    this.rentTypes = G.rentTypes;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_list_game_rent,parent,false);
    return new ListViewHolder(view);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final Game game = games.get(position);
    Picasso.with(context).
      load(game.getApp_cover_photo_url())
//     .noFade()
//     .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//     .skipMemoryCache()
      //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
      //.error(context.getResources().getDrawable(R.drawable.default_no_image))
      .into(holder.img_game);

    holder.txt_name.setText(game.getName());
//     holder.txt_release_date.setText("تاریخ ارائه : " + game.getProduction_date());
//     holder.txt_rating.setText("rate : " + "4.5");
    holder.txt_region.setText(" Region : " + game.getRegion());

    //calculate rent cost
    int sevenDayPricePercent = 1;
    for (int i=0;i<rentTypes.size();i++){
      if(rentTypes.get(i).getDay_count() == 7){
        sevenDayPricePercent = rentTypes.get(i).getPrice_percent();
      }
    }
    int cost = ((game.getPrice() * (sevenDayPricePercent)) / 100);


    if(game.getCount() == 0) {
      holder.btn_price.setBackgroundResource(R.drawable.back_list_game_price_finished);
      holder.btn_price.setText("ناموجود");
    }else{
      holder.btn_price.setBackgroundResource(R.drawable.back_list_game_price);
      holder.btn_price.setText(HelperText.split_price(cost) + " تومان ");
    }




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ActivityShowRent.class);
        intent.putExtra("ID", game.getId());
        context.startActivity(intent);
      }
    });


  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private RoundedImageView img_game;
    private TextView txt_name;
    private TextView txt_rating;
    private TextView txt_region;
    private TextView txt_release_date;
    private TextView txt_rent_day;
    private Button btn_price;

    public ListViewHolder(View itemView) {
      super(itemView);
      img_game = itemView.findViewById(R.id.img_game);
      txt_name = itemView.findViewById(R.id.txt_name);
      txt_rating = itemView.findViewById(R.id.txt_rating);
      txt_region = itemView.findViewById(R.id.txt_region);
      txt_release_date = itemView.findViewById(R.id.txt_release_date);
      txt_rent_day = itemView.findViewById(R.id.txt_rent_day);
      btn_price = itemView.findViewById(R.id.btn_price);


      setTypeFace();
    }

    private void setTypeFace(){
      txt_name.setTypeface(MyViews.getRobotoLightFont(context));
      txt_rating.setTypeface(MyViews.getRobotoLightFont(context));
      txt_region.setTypeface(MyViews.getRobotoLightFont(context));
      txt_release_date.setTypeface(MyViews.getIranSansMediumFont(context));
      txt_rent_day.setTypeface(MyViews.getIranSansLightFont(context));
      btn_price.setTypeface(MyViews.getIranSansMediumFont(context));
    }


  }


  public void notifyData(List<Game> games1) {
    //Log.d("notifyData ", myList.size() + "");
    if (games1.size() > 0) {
      for (int i = 0; i < games1.size(); i++) {
        this.games.add(games1.get(i));
      }
      this.notifyItemInserted(games.size() - 1);

      //notifyDataSetChanged();
    }
  }


  public void clear() {
    final int size = games.size();
    games.clear();
    notifyItemRangeRemoved(0, size);
  }







}