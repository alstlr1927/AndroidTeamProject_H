package com.cookandroid.androidteamproject_h;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ThemeData> themeList;
    ThemeData themeData = new ThemeData();

    static final String key = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";

    static final String appName = "tourApp";

    int layout = 0;

    RequestQueue request;

    View view ;

    public ThemeAdapter(Context context, ArrayList<ThemeData> themeList,int layout) {
        super();
        this.context = context;
        this.themeList = themeList;
        this.layout = layout;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtView.setText(themeList.get(position).getTitle());

        Glide.with(context).load(themeList.get(position).getFirstImage()).into(holder.imgView);

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int)view.getTag();
                ThemeAdapter.AsyncYTaskClassSub asyncYTaskClassSub = new ThemeAdapter.AsyncYTaskClassSub();
                asyncYTaskClassSub.execute(position);
            }
        });
    }

    @Override
    public int getItemCount() {
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

    public ThemeData TourData(int position){

        return themeList != null ? themeList.get(position) : null;
    }

    class AsyncYTaskClassSub extends android.os.AsyncTask<Integer,ThemeData,ThemeData>{

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
    }

    private ThemeData getData(Integer contentsID) {
        request = Volley.newRequestQueue(context);

        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey="+
                key+"&areaCode=1&contentTypeId="+contentsID
                +"&listYN=Y&arrange=P&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp="
                +appName +"&_type=json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_item = (JSONObject) parse_body.get("items");
                            JSONObject parse_itemlist = (JSONObject) parse_item.get("item");

                            themeData.setFirstImage(parse_itemlist.getString("firstimage"));
                            themeData.setTitle(parse_itemlist.getString("title"));
                            themeData.setAddr(parse_itemlist.getString("addr1"));
                            themeData.setOverView(parse_itemlist.getString("overview"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
