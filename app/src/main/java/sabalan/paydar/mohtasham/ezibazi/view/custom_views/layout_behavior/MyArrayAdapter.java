package sabalan.paydar.mohtasham.ezibazi.view.custom_views.layout_behavior;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<String> implements Filterable {



  private ArrayList<String> _items = new ArrayList<String>();
  private ArrayList<String> orig = new ArrayList<String>();
  private TextView txt_item;

  public MyArrayAdapter(Context context, int resource, ArrayList<String> items) {
    super(context, resource, items);

    for (int i = 0; i < items.size(); i++) {
      orig.add(items.get(i));
    }
  }

  @Override
  public int getCount() {
    if (_items != null)
      return _items.size();
    else
      return 0;
  }

  @Override
  public String getItem(int arg0) {
    if(_items.size() > arg0)
      return _items.get(arg0);

    return "";
  }


  @Override
  public Filter getFilter() {
    Filter filter = new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {

        if(constraint != null)
          Log.d("Constraints", constraint.toString());
        FilterResults oReturn = new FilterResults();

        int counters = 0;
        if (constraint != null){

          _items = new ArrayList<String>();
          if (orig != null && orig.size() > 0) {
            for(int i=0; i<orig.size(); i++)
            {
                _items.add(orig.get(i));
                counters++;
            }
          }
          if(counters == 0)
          {
            _items.clear();
            _items = orig;
          }
          oReturn.values = _items;
          oReturn.count = _items.size();
        }
        return oReturn;
      }


      @SuppressWarnings("unchecked")
      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        if(results != null && results.count > 0) {
          notifyDataSetChanged();
        }
        else {
          notifyDataSetInvalidated();
        }

      }

    };

    return filter;

  }



}
