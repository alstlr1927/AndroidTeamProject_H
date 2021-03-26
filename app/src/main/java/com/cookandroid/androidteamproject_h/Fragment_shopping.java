package com.cookandroid.androidteamproject_h;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment_shopping extends Fragment {
    private View view;
    ProgressDialog pDialog;

    RecyclerView recyclerView;
    ThemeAdapter adapter;
    LinearLayoutManager layoutManager;

    RequestQueue queue;

    AlertDialog.Builder dialog;
    ArrayList<ThemeData> list = new ArrayList<>();

    ThemeData detailThemeData = new ThemeData();

    final static String TAG = "ThemeActivity";
    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    // 생성자 생성할것
    public Fragment_shopping() {

    }

    public static Fragment_shopping newInstance() {
        Fragment_shopping fragment_shopping = new Fragment_shopping();
        return fragment_shopping;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);

        recyclerView = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        Fragment_shopping.AsyncTaskClassMain async = new Fragment_shopping.AsyncTaskClassMain();
        async.execute();

        adapter = new ThemeAdapter(getActivity(), list);

        return view;
    }

    class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {

        //일반 쓰레드 돌리기 전 메인 쓰레드에서 보여줄 화면 처리
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoader();
        }

        //일반쓰레드에서 돌릴 네트워크 작업
        @Override
        protected String doInBackground(Integer... integers) {
            getAreaBasedList();
            return "작업 종료";
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            //일반 쓰레드가 도는 도중에 메인 쓰레드에서 처리할 UI작업
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    //contentid를 위한 함수(contentId는 detailCommon에서 쓰기 위해 구한다)
    private void getAreaBasedList() {
        queue = Volley.newRequestQueue(getActivity());

        String url = "http://api.visitkorea.or.kr/openapi/service/"
                + "rest/KorService/areaBasedList?ServiceKey=" + KEY
                + "&areaCode=1&contentTypeId=38&listYN=Y&arrange=P"
                + "&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp="
                + appName + "&_type=json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                            list.removeAll(list);
                            Log.e("Fragment_shopping",String.valueOf(parse_itemlist.length()));
                            for (int i = 0; i < parse_itemlist.length(); i++) {
                                JSONObject imsi = (JSONObject) parse_itemlist.get(i);

                                ThemeData themeData = new ThemeData();
                                themeData.setFirstImage(imsi.getString("firstimage"));
                                themeData.setTitle(imsi.getString("title"));
                                themeData.setAddr(imsi.getString("addr1"));
                                themeData.setMapX(imsi.getDouble("mapx"));
                                themeData.setMapY(imsi.getDouble("mapy"));http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D&areaCode=1&contentTypeId=38&listYN=Y&arrange=P&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=APPNAME&_type=json
//                                themeData.setTel(imsi.getString("tel"));
                                themeData.setContentsID(Integer.valueOf(imsi.getString("contentid")));

                                list.add(themeData);

                            }

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG,error.getMessage() + "에러");
                    }
                });
        queue.add(jsObjRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("잠시만 기다려 주세요");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
