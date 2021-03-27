package com.cookandroid.androidteamproject_h;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class Fragment_activity extends Fragment {

    private View view;
    private ProgressDialog pDialog;

    private RecyclerView grid_recyclerview;
    private RequestQueue queue;
    private static ThemeAdapter adapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<ThemeData> dataList = new ArrayList<>();

    static final String TAG = "Activity_Fragment";
    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    // 생성자 생성할것
    public Fragment_activity() {

    }

    public static Fragment_activity newInstance() {
        Fragment_activity fragment_activity = new Fragment_activity();
        return fragment_activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);

        grid_recyclerview = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());

        grid_recyclerview.setLayoutManager(layoutManager);

        Fragment_activity.AsyncTaskClassMain async = new Fragment_activity.AsyncTaskClassMain();
        async.execute();

        adapter = new ThemeAdapter(getActivity(), dataList, R.layout.item_theme);

        return view;
    }

    public class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoader();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            getAreaBasedList();

            return "작업 종료";
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
                KEY +"&areaCode=1&contentTypeId=28&listYN=Y&arrange=P&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=" +
                appName + "&_type=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    JSONObject parse_response = (JSONObject) response.get("response");
                    JSONObject parse_body = (JSONObject) parse_response.get("body");
                    JSONObject parse_items = (JSONObject) parse_body.get("items");
                    JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                    dataList.removeAll(dataList);

                    for(int i =0;i <parse_itemlist.length();i++) {
                        JSONObject imsi = (JSONObject) parse_itemlist.get(i);

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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, error.getMessage() + "에러");
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("잠시만...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
