package com.cookandroid.androidteamproject_h;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ThemeData> themeList;
    private ThemeData themeData = new ThemeData();

    private static final String key = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";

    private static final String appName = "tourApp";

    private int layout = 0;

    private RequestQueue request;

    private View view;
    private View viewDialog;

    public ThemeAdapter(Context context, ArrayList<ThemeData> themeList, int layout) {
        this.context = context;
        this.themeList = themeList;
        this.layout = layout;
    }

    public void setList(ArrayList<ThemeData> arrayList) {
        this.themeList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtView.setText(themeList.get(position).getTitle());

        Glide.with(context).load(themeList.get(position).getFirstImage()).into(holder.imgView);

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                ThemeAdapter.AsyncYTaskClassSub asyncYTaskClassSub = new ThemeAdapter.AsyncYTaskClassSub();
                asyncYTaskClassSub.execute(position);
            }
        });

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

        Cursor cursor = MainActivity.db.rawQuery("SELECT title FROM favorite_" + LoginActivity.userID + ";", null);
        while(cursor.moveToNext()) {
            if(cursor.getString(0).equals(themeList.get(position).getTitle())) {
                themeList.get(position).setHeart(true);
                break;
            }
        }

        if(cursor != null) {
            cursor.close();
        }

        if(themeList.get(position).isHeart()) {
            holder.like_heart.setSelected(true);
        } else {
            holder.like_heart.setSelected(false);
        }

        holder.like_heart.setOnClickListener((View v) -> {
            if (themeList.get(position).isHeart()) {
                holder.like_heart.setSelected(false);
                themeList.get(position).setHeart(false);

                String favorDelete =
                        "DELETE FROM favorite_" + LoginActivity.userID + " WHERE title='" + themeList.get(position).getTitle() +"';";
                MainActivity.db.execSQL(favorDelete);
            } else {
                holder.like_heart.setSelected(true);
                themeList.get(position).setHeart(true);

                String favorInsert = "INSERT INTO favorite_" + LoginActivity.userID + " VALUES('"
                        + themeList.get(position).getTitle() + "','"
                        + themeList.get(position).getAddr() + "','"
                        + themeList.get(position).getMapX() + "','"
                        + themeList.get(position).getMapY() + "','"
                        + themeList.get(position).getFirstImage() + "')";
                MainActivity.db.execSQL(favorInsert);
                holder.like_heart.likeAnimation(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (themeList != null) {
            Log.d("@@@@@@@", "themeList:" + themeList.size());
        }
        return themeList == null ? 0 : themeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;
        public ImageView imgView;

        public SmallBangView like_heart;
        public ImageView imageHeart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtView = itemView.findViewById(R.id.txtView);
            imgView = itemView.findViewById(R.id.imgView);
            like_heart = itemView.findViewById(R.id.like_heart);
            imageHeart = itemView.findViewById(R.id.imageHeart);
        }
    }

    class AsyncYTaskClassSub extends android.os.AsyncTask<Integer, ThemeData, ThemeData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ThemeData doInBackground(Integer... integers) {

            int position = integers[0];

            ThemeData myThemeData1 = themeList.get(position);
            ThemeData themeData = getData(myThemeData1.getContentsID());

            return themeData;
        }

        @Override
        protected void onPostExecute(ThemeData themeData) {
            super.onPostExecute(themeData);

            viewDialog = View.inflate(context, R.layout.dialog_info, null);

            TextView txt_Detail_title = viewDialog.findViewById(R.id.txt_Detail_title);
            TextView txt_Detail_addr = viewDialog.findViewById(R.id.txt_Detail_addr);
            TextView txt_Detail_info = viewDialog.findViewById(R.id.txt_Detail_info);

            Button btnExit = viewDialog.findViewById(R.id.btnExit);
            Button btnBrowse = viewDialog.findViewById(R.id.btnBrowse);

            ImageView img_Detail_info = viewDialog.findViewById(R.id.img_Detail_info);

            txt_Detail_title.setText(themeData.getTitle());
            txt_Detail_addr.setText(themeData.getAddr());
            txt_Detail_info.setText(themeData.getOverView());

            Glide.with(context).load(themeData.getFirstImage()).override(500, 300).into(img_Detail_info);

            final Dialog dialog = new Dialog(viewDialog.getContext());

            dialog.setContentView(viewDialog);
            dialog.show();

            btnExit.setOnClickListener((View v) -> {
                dialog.dismiss();
            });
            btnBrowse.setOnClickListener((View v) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+themeData.getTitle()));
                context.startActivity(intent);
            });
        }
    }

    private ThemeData getData(int contentID) {
        request = Volley.newRequestQueue(context);

        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey="
                + key + "&contentId=" + contentID
                + "&firstImageYN=Y&mapinfoYN=Y&addrinfoYN=Y&defaultYN=Y&overviewYN=Y"
                + "&pageNo=1&MobileOS=AND&MobileApp="
                + appName + "&_type=json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONObject parse_itemlist = (JSONObject) parse_items.get("item");

                            themeData.setFirstImage(parse_itemlist.getString("firstimage"));
                            themeData.setTitle(parse_itemlist.getString("title"));
                            themeData.setAddr(parse_itemlist.getString("addr1"));
                            themeData.setOverView(parse_itemlist.getString("overview"));
                            themeData.setTel(parse_itemlist.getString("tel"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, (error) -> {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                });
        request.add(jsonObjectRequest);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return themeData;
    }
}