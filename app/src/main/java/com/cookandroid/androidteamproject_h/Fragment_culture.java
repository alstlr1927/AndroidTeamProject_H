package com.cookandroid.androidteamproject_h;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//문화 테마
public class Fragment_culture extends Fragment {

    private View view;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Context context;
    private AlertDialog.Builder dialog;
    private RequestQueue queue;
    private ThemeAdapter adapter;
    private ArrayList<ThemeData> list = new ArrayList<>();

    final static String TAG = "ThemeActivity";
    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    private ArrayList<Integer> contenIdList = new ArrayList<>();


    // 생성자 생성할것
    public Fragment_culture() {

    }

    public static Fragment_culture newInstance() {
        Fragment_culture fragment_culture = new Fragment_culture();
        return fragment_culture;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);

        recyclerView = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new ThemeAdapter(getActivity(), list, R.layout.item_theme);

        Fragment_culture.AsyncTaskClassMain asyns = new Fragment_culture.AsyncTaskClassMain();
        asyns.execute();

        return view;
    }

    public class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoader();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            getAreaBasedList();
            return "작업종료";
        }
    }

    public class AsyncTaskClassSub extends android.os.AsyncTask<Integer, ThemeData, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            int position = integers[0];
            //adapter.TourData(position);
            Log.d(TAG, "포지션 값: " + position);

            return "작업종료";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(ThemeData... values) {
            super.onProgressUpdate(values);
        }
    }


    public void getAreaBasedList() {
        queue = Volley.newRequestQueue(getActivity());
        String url ="http://api.visitkorea.or.kr/openapi/service/" +
                "rest/KorService/areaBasedList?ServiceKey=" +
                KEY +"&areaCode="+ LoginActivity.areaCode +"&contentTypeId=14&listYN=Y&arrange=P&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=" +
                appName + "&_type=json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                            for (int i = 0; i < parse_itemlist.length(); i++) {

                                JSONObject imsi = (JSONObject) parse_itemlist.get(i);

                                Log.d("@@@@","imsi:"+ imsi.toString());
                                ThemeData themeData = new ThemeData();
                                themeData.setFirstImage(imsi.getString("firstimage"));
                                themeData.setTitle(imsi.getString("title"));
                                themeData.setAddr(imsi.getString("addr1"));
                                themeData.setMapX(imsi.getDouble("mapx"));
                                themeData.setMapY(imsi.getDouble("mapy"));
                                themeData.setContentsID(Integer.valueOf(imsi.getString("contentid")));

                                list.add(themeData);

                            }
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, (error) -> {
                    pDialog.dismiss();
                    Log.d(TAG, error.getMessage() + "에러");
                });
        queue.add(jsObjRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("잠시만...!");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


}