package com.example.ashutosh.mapping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] tr) {
        super(context, R.layout.custom_row ,tr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater list_Inflater = LayoutInflater.from(getContext());
        View customView = list_Inflater.inflate(R.layout.custom_row, parent, false);

        String singleTrItem = getItem(position);
        TextView list__Text = (TextView) customView.findViewById(R.id.Text);
        ImageView list_Image = (ImageView) customView.findViewById(R.id.Image);

        list__Text.setText(singleTrItem);
        list_Image.setImageResource(R.drawable.mapper4);
        return customView;
    }
}