package com.cookandroid.androidteamproject_h;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_festival extends Fragment {

    private RecyclerView grid_recyclerview;
    private static ThemeAdapter adapter;
    private LinearLayoutManager layoutManager;

    private RequestQueue queue;

    private ArrayList<ThemeData> dataList = new ArrayList<>();

    static final String TAG = "Festival_Fragment";
    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    // 생성자 생성할것
    public Fragment_festival() {

    }

    public static Fragment_festival newInstance() {
        Fragment_festival fragment_festival = new Fragment_festival();
        return fragment_festival;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        grid_recyclerview = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());

        grid_recyclerview.setLayoutManager(layoutManager);

        adapter = new ThemeAdapter(getActivity(), dataList);

        Fragment_festival.AsyncTaskClassMain asyncTaskClassMain = new Fragment_festival.AsyncTaskClassMain();
        asyncTaskClassMain.execute();

        return view;
    }

    public class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            getAreaBasedList();
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void getAreaBasedList() {
        queue = Volley.newRequestQueue(getActivity());

        String url ="http://api.visitkorea.or.kr/openapi/service/" +
                "rest/KorService/areaBasedList?ServiceKey=" +
                KEY +"&areaCode=1&contentTypeId=15&listYN=Y&arrange=P&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=" +
                appName + "&_type=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                            dataList.removeAll(dataList);

                            for (int i = 0; i < parse_itemlist.length(); i++) {
                                JSONObject imsi = (JSONObject) parse_itemlist.get(i);
                                Log.d("@@@@", "imsi:" +imsi.toString());
                                ThemeData data = new ThemeData();
                                data.setFirstImage(imsi.getString("firstimage"));
                                data.setTitle(imsi.getString("title"));
                                data.setAddr(imsi.getString("addr1"));
                                data.setMapX(imsi.getDouble("mapx"));
                                data.setMapY(imsi.getDouble("mapy"));
                                data.setContentsID(Integer.valueOf(imsi.getString("contentid")));

                                dataList.add(data);
                            }
                            grid_recyclerview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, error.getMessage() + "Error");
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
