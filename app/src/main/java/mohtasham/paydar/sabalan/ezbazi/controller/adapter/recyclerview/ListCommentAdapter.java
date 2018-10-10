package mohtasham.paydar.sabalan.ezbazi.controller.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import mohtasham.paydar.sabalan.ezbazi.R;
import mohtasham.paydar.sabalan.ezbazi.controller.system.helper.HelperDate;
import mohtasham.paydar.sabalan.ezbazi.controller.system.helper.HelperText;
import mohtasham.paydar.sabalan.ezbazi.model.Comment;
import mohtasham.paydar.sabalan.ezbazi.view.custom_views.my_views.MyViews;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.ListViewHolder>{


  private Context context;
  private List<Comment> comments;

  public ListCommentAdapter(Context context, List<Comment> comments){

    this.context = context;
    this.comments = comments;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    final Comment comment = comments.get(position);


    holder.txt_full_name.setText(comment.getUser().getFull_name());
    holder.txt_date.setText(HelperText.toPersianNumber(new HelperDate().timestampToPersian(comment.getCreated_at())));
    holder.expand_text_view.setText(comment.getText());




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
    return comments.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private TextView txt_full_name;
    private TextView txt_date;
    private ExpandableTextView expand_text_view;
    private TextView expandable_text;

    public ListViewHolder(View itemView) {
      super(itemView);
      txt_full_name = itemView.findViewById(R.id.txt_full_name);
      txt_date = itemView.findViewById(R.id.txt_date);
      expand_text_view = itemView.findViewById(R.id.expand_text_view);
      expandable_text = itemView.findViewById(R.id.expandable_text);



      setTypeFace();
    }

    private void setTypeFace(){
      txt_full_name.setTypeface(MyViews.getIranSansBoldFont(context));
      txt_date.setTypeface(MyViews.getIranSansUltraLightFont(context));
      expandable_text.setTypeface(MyViews.getIranSansLightFont(context));
    }


  }


  public void notifyData(List<Comment> newComments) {
    //Log.d("notifyData ", myList.size() + "");
    if (newComments.size() > 0) {
      for (int i = 0; i < newComments.size(); i++) {
        this.comments.add(newComments.get(i));
      }
      this.notifyItemInserted(comments.size() - 1);

      //notifyDataSetChanged();
    }
  }


  public void clear() {
    final int size = comments.size();
    comments.clear();
    notifyItemRangeRemoved(0, size);
  }





}