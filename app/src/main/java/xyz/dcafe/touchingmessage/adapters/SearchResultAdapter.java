package xyz.dcafe.touchingmessage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.dcafe.touchingmessage.R;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class SearchResultAdapter extends ArrayAdapter<HashMap<String, String>> {
    public SearchResultAdapter(Context context, ArrayList<HashMap<String, String>> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HashMap<String, String> results= getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.title);
        textView.setText(results.get("NICKNAME"));
        textView.setTextColor(Color.BLACK);

        return convertView;
    }
}
