package mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.model.Game;
import mohtasham.paydar.sabalan.ezbazi.model.common.Photo;
import mohtasham.paydar.sabalan.ezbazi.view.activity.ActivityShowShop;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ShopMainAdapter extends RecyclerView.Adapter<ShopMainAdapter.ListViewHolder>{


  private Context context;
  private List<Game> games;

  public ShopMainAdapter(Context context, List<Game> games){

    this.context = context;
    this.games = games;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_fragment_main_games,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final Game game = games.get(position);
    final int id = game.getId();
    ArrayList<Photo> photos = game.getPhotos();
      Picasso.with(context).
        load(photos.get(0).getUrl())
//      .noFade()
//      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//      .skipMemoryCache()
        //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
        //.error(context.getResources().getDrawable(R.drawable.default_no_image))
        .into(holder.img_game);

    holder.txt_game_name.setText(game.getName());
    holder.txt_release_date.setText(game.getProduction_date());




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ActivityShowShop.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ID", id);
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
    private TextView txt_game_name;
    private TextView txt_release_date;

    public ListViewHolder(View itemView) {
      super(itemView);
      img_game = itemView.findViewById(R.id.img_game);
      txt_game_name = itemView.findViewById(R.id.txt_game_name);
      txt_release_date = itemView.findViewById(R.id.txt_release_date);


      setTypeFace();
    }


    private void setTypeFace(){
      txt_game_name.setTypeface(MyViews.getIranSansMediumFont(context));
      txt_release_date.setTypeface(MyViews.getIranSansMediumFont(context));
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