package com.example.diagnaltest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diagnaltest.R;
import com.example.diagnaltest.model.Item;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Item> {
    private Context context;
    ArrayList<Item> mItemList = new ArrayList<Item>();

    public ListAdapter(Context context, ArrayList<Item> mItemList) {
        super(context, R.layout.grid_view_items, mItemList);
        this.context = context;
        this.mItemList = mItemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        TextView textView = (TextView) v.findViewById(R.id.movieName);
        ImageView imageView = (ImageView) v.findViewById(R.id.movieImage);
        textView.setText(mItemList.get(position).getmMovieName());
        imageView.setImageResource(mItemList.get(position).getmMovieImage());
        return v;
    }
}
