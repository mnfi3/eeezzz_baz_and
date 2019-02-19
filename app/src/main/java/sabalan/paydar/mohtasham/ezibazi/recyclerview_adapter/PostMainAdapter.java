package sabalan.paydar.mohtasham.ezibazi.recyclerview_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import sabalan.paydar.mohtasham.ezibazi.R;
import sabalan.paydar.mohtasham.ezibazi.model.Post;
import sabalan.paydar.mohtasham.ezibazi.view.activity.ActivityWebView;
import sabalan.paydar.mohtasham.ezibazi.view.custom_views.my_views.MyViews;

public class PostMainAdapter extends RecyclerView.Adapter<PostMainAdapter.ListViewHolder>{


  private Context context;
  private List<Post> posts;

  public PostMainAdapter(Context context, List<Post> posts){

    this.context = context;
    this.posts = posts;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_fragment_main_posts,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final Post post = posts.get(position);
    Picasso.with(context).
      load(post.getImage_url())
//      .noFade()
//      .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//      .skipMemoryCache()
      //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
      //.error(context.getResources().getDrawable(R.drawable.default_no_image))
      .into(holder.img_post);

    holder.txt_post_title.setText(post.getTitle());
    holder.txt_post_date.setText(post.getCreated_at());




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ActivityWebView.class);
        intent.putExtra("URL", "https://www.google.com");
        context.startActivity(intent);

      }
    });


  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private RoundedImageView img_post;
    private TextView txt_post_title;
    private TextView txt_post_date;

    public ListViewHolder(View itemView) {
      super(itemView);
      img_post = itemView.findViewById(R.id.img_post);
      txt_post_title = itemView.findViewById(R.id.txt_post_title);
      txt_post_date = itemView.findViewById(R.id.txt_post_date);

      setTypeFace();

    }


    private void setTypeFace(){
      txt_post_title.setTypeface(MyViews.getIranSansMediumFont(context));
      txt_post_date.setTypeface(MyViews.getIranSansMediumFont(context));
    }


  }


  public void notifyData(List<Post> newPosts) {
    //Log.d("notifyData ", myList.size() + "");
    if (newPosts.size() > 0) {
      for (int i = 0; i < newPosts.size(); i++) {
        this.posts.add(newPosts.get(i));
      }
      this.notifyItemInserted(posts.size() - 1);

      //notifyDataSetChanged();
    }
  }


  public void clear() {
    final int size = posts.size();
    posts.clear();
    notifyItemRangeRemoved(0, size);
  }





}