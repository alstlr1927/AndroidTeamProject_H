package com.cookandroid.androidteamproject_h;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> {

    Context context;
    ArrayList<ThemeData> list;
    int layout;

    private View dialogView;

    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    private ThemeData detailData = new ThemeData();

    private RequestQueue queue;

    public SearchAdapter(Context context, ArrayList<ThemeData> list, int layout) {
        super();
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        SearchAdapter.CustomViewHolder holder = new SearchAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.CustomViewHolder holder, final int position) {
        holder.txtView2.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getFirstImage()).into(holder.imgView2);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                SearchAdapter.AsyncTaskClass asyncTaskClass = new SearchAdapter.AsyncTaskClass();
                asyncTaskClass.execute(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtView2;
        public ImageView imgView2;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            txtView2 = itemView.findViewById(R.id.txtView2);
            imgView2 = itemView.findViewById(R.id.imgView2);
        }
    }

    class AsyncTaskClass extends android.os.AsyncTask<Integer, ThemeData, ThemeData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ThemeData doInBackground(Integer... integers) {
            int position = integers[0];

            ThemeData myThemeData = list.get(position);
            ThemeData themeData = getData(myThemeData.getContentsID());

            return themeData;
        }

        @Override
        protected void onProgressUpdate(ThemeData... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ThemeData themeData) {
            super.onPostExecute(themeData);

            dialogView = View.inflate(context, R.layout.dialog_info, null);

            TextView txt_Detail_title = dialogView.findViewById(R.id.txt_Detail_title);
            TextView txt_Detail_addr = dialogView.findViewById(R.id.txt_Detail_addr);
            TextView txt_Detail_info = dialogView.findViewById(R.id.txt_Detail_info);
            ImageView img_Detail_info = dialogView.findViewById(R.id.img_Detail_info);

            Button btnExit = dialogView.findViewById(R.id.btnExit);
            Button btnBrowse = dialogView.findViewById(R.id.btnBrowse);

            Glide.with(context).load(themeData.getFirstImage()).override(500, 300).into(img_Detail_info);

            txt_Detail_title.setText(themeData.getTitle());
            txt_Detail_addr.setText(themeData.getAddr());
            txt_Detail_info.setText(themeData.getOverView());

            final Dialog dlg = new Dialog(context);

            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dlg.setContentView(dialogView);
            dlg.show();

            btnExit.setOnClickListener((View v) -> {
                dlg.dismiss();
            });

            btnBrowse.setOnClickListener((View v) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+themeData.getTitle()));
                context.startActivity(intent);
            });
        }

        private ThemeData getData(int contentID) {
            queue = Volley.newRequestQueue(context);

            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey="
                    + KEY + "&contentId=" + contentID
                    + "&firstImageYN=Y&mapinfoYN=Y&addrinfoYN=Y&defaultYN=Y&overviewYN=Y"
                    + "&pageNo=1&MobileOS=AND&MobileApp="
                    + appName + "&_type=json";

            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject parse_response = (JSONObject) response.get("response");
                                JSONObject parse_body = (JSONObject) parse_response.get("body");
                                JSONObject parse_items = (JSONObject) parse_body.get("items");
                                JSONObject parse_itemlist = (JSONObject) parse_items.get("item");

                                detailData.setFirstImage(parse_itemlist.getString("firstimage"));
                                detailData.setTitle(parse_itemlist.getString("title"));
                                detailData.setAddr(parse_itemlist.getString("addr1"));
                                detailData.setOverView(parse_itemlist.getString("overview"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(jsonObjectRequest);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return detailData;
        }
    }
}
