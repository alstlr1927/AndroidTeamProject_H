package com.cookandroid.androidteamproject_h;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CollecAdapter extends RecyclerView.Adapter<CollecAdapter.CustomViewHolder> {

    private int layout;
    private ArrayList<ThemeData> list;

    public CollecAdapter(int layout, ArrayList<ThemeData> list) {
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
        if(list.get(position).getPicture() !=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(list.get(position).getPicture());
            holder.imgCollect.setImageBitmap(bitmap);
        }

        holder.txtTitle.setText(list.get(position).getTitle());

        holder.btnContent.setOnClickListener((View v) -> {
            String content = list.get(position).getContents();
            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
            dlg.setTitle("Content");
            dlg.setMessage(content);
            dlg.show();
        });
    }

    @Override
    public int getItemCount() {
        return list !=null ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgCollect;
        public Button btnContent;
        public TextView txtTitle;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCollect = itemView.findViewById(R.id.imgCollect);
            btnContent = itemView.findViewById(R.id.btnContent);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}
