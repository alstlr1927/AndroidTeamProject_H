package com.cookandroid.androidteamproject_h;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.CustomViewHolder> {

    private Context context;
    private int layout;
    private ArrayList<ThemeData> list;

    public MapAdapter(int layout, ArrayList<ThemeData> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.mapTitle.setText(list.get(position).getTitle());
        holder.mapContent.setText(list.get(position).getAddr());
        holder.itemView.setTag(position);
        holder.imgSearch.setOnClickListener((View v) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + list.get(position).getTitle()));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView mapTitle;
        TextView mapContent;
        ImageView imgSearch;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            mapTitle = itemView.findViewById(R.id.mapTitle);
            mapContent = itemView.findViewById(R.id.mapContent);
            imgSearch = itemView.findViewById(R.id.imgSearch);
        }
    }
}
