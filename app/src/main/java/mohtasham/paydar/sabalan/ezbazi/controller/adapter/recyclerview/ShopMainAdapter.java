package mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.model.GameForShop;
import mohtasham.paydar.sabalan.ezbazi.model.Post;
import mohtasham.paydar.sabalan.ezbazi.model.common.Photo;

public class ShopMainAdapter extends RecyclerView.Adapter<ShopMainAdapter.ListViewHolder>{


  private Context context;
  private List<GameForShop> games;

  public ShopMainAdapter(Context context, List<GameForShop> games){

    this.context = context;
    this.games = games;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_fragment_main_shops,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final GameForShop game = games.get(position);
    ArrayList<Photo> photos = game.getPhotos();
    if(photos != null) {
      Picasso.with(context).
        load(photos.get(0).getUrl())
//      .noFade()
//      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//      .skipMemoryCache()
        //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
        //.error(context.getResources().getDrawable(R.drawable.default_no_image))
        .into(holder.img_game);
    }

      holder.txt_game_name.setText(game.getName());




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
    });


  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private ImageView img_game;
    private TextView txt_game_name;

    public ListViewHolder(View itemView) {
      super(itemView);
      img_game = itemView.findViewById(R.id.img_game);
      txt_game_name= itemView.findViewById(R.id.txt_post_title);

    }


  }


  public void notifyData(List<GameForShop> games1) {
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