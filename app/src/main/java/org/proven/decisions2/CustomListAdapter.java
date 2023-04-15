package org.proven.decisions2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.proven.decisions2.R;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> values;
    private final int layoutResourceId;

    public CustomListAdapter(Activity context, List<String> values, int layoutResourceId) {
        super(context, layoutResourceId, values);
        this.context = context;
        this.values = values;
        this.layoutResourceId = layoutResourceId;
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
        tUsername.setText(values.get(position));

        return rowView;
    }
}