package com.cookandroid.androidteamproject_h;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GPSAdapter extends RecyclerView.Adapter<GPSAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<ThemeData> list;

    private GPSAdapter.OnItemClickListener mListener;

    public GPSAdapter(int layout, ArrayList<ThemeData> list) {
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
        holder.checkerTitle.setText(list.get(position).getTitle());
        holder.checkerContent.setText(list.get(position).getAddr());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView checkerTitle;
        TextView checkerContent;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            checkerTitle = itemView.findViewById(R.id.checkerTitle);
            checkerContent = itemView.findViewById(R.id.checkerContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(GPSAdapter.OnItemClickListener Listener) {
        this.mListener = Listener;
    }
}
