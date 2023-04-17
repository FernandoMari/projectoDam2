package org.proven.decisions2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends ArrayAdapter<String> implements Filterable {

    private final Activity context;
    private final List<String> values;

    private List<String> filteredValues;

    private CustomFilter filter;
    private final int layoutResourceId;


    public CustomListAdapter(Activity context, List<String> values, int layoutResourceId) {
        super(context, layoutResourceId, values);
        this.context = context;
        this.values = values;
        this.layoutResourceId = layoutResourceId;
        this.filteredValues = values;
        this.filter = new CustomFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;
        if (convertView == null) {
            // Inflar el layout correspondiente según el valor de layoutResourceId
            if (layoutResourceId == R.layout.list_item_add) {
                rowView = inflater.inflate(R.layout.list_item_add, null, true);
            } else if (layoutResourceId == R.layout.list_item_remove) {
                rowView = inflater.inflate(R.layout.list_item_remove, null, true);
            } else {
                rowView = inflater.inflate(R.layout.list_item_request, null, true);
            }
        } else {
            rowView = convertView;
        }

        // Asignar los valores correspondientes a las vistas en el layout
        TextView tUsername = (TextView) rowView.findViewById(R.id.tUsername);
        tUsername.setText(filteredValues.get(position));

        return rowView;
    }

    @Override
    public int getCount() {
        return filteredValues.size();
    }

    @Override
    public String getItem(int position) {
        return filteredValues.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                String filterString = constraint.toString().toUpperCase();
                List<String> filteredList = new ArrayList<>();
                for (String value : values) {
                    if (value.toUpperCase().contains(filterString)) {
                        filteredList.add(value);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = values.size();
                results.values = values;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredValues = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}