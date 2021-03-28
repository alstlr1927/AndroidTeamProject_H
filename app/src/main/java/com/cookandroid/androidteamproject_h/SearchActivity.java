package com.cookandroid.androidteamproject_h;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ImageButton btnSearch2;
    private EditText edtSearch2;
    private RecyclerView grid_recyclerView2;

    private RequestQueue queue;
    private ArrayList<ThemeData> list = new ArrayList<>();

    private static SearchAdapter adapter;
    private LinearLayoutManager layoutManager;

    String keyword;

    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewByIdFunc();

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");

        btnSearch2.setOnClickListener((View v) -> {
            String word2 = edtSearch2.getText().toString().trim();

            if(word2.length() >1) {
                searchData(word2);
            } else {
                Toast.makeText(getApplicationContext(), "두 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        searchData(word);
    }

    private void searchData(String word) {
        try {
            keyword = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(getApplicationContext());

        String url = "http://api.visitkorea.or.kr/openapi/service/"
                + "rest/KorService/searchKeyword?ServiceKey=" + KEY
                + "&MobileApp="+ appName +"&MobileOS=AND&pageNo=1&numOfRows=20&listYN=Y&arrange=P&areaCode=1&keyword="
                + keyword + "&_type=json";

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject parse_response = (JSONObject) response.get("response");
                            JSONObject parse_body = (JSONObject) parse_response.get("body");
                            JSONObject parse_items = (JSONObject) parse_body.get("items");
                            JSONArray parse_itemlist = (JSONArray) parse_items.get("item");

                            list.removeAll(list);

                            for(int i =0;i <parse_itemlist.length();i++) {
                                JSONObject imsi = (JSONObject) parse_itemlist.get(i);

                                Log.d("@@@@", "imsi : " + imsi.toString());

                                ThemeData data = new ThemeData();
                                data.setFirstImage(imsi.getString("firstimage"));
                                data.setTitle(imsi.getString("title"));
                                data.setContentsID(Integer.valueOf(imsi.getString("contentid")));

                                list.add(data);
                            }
                            grid_recyclerView2.setAdapter(adapter);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                            AlertDialog.Builder dlg = new AlertDialog.Builder(SearchActivity.this);
                            dlg.setMessage("찾으시는 항목이 존재하지 않습니다.");
                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            dlg.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);

        grid_recyclerView2 = findViewById(R.id.grid_recyclerview2);

        adapter = new SearchAdapter(SearchActivity.this, list, R.layout.item_search_theme);

        layoutManager = new LinearLayoutManager(this);

        grid_recyclerView2.setLayoutManager(layoutManager);
    }

    private void findViewByIdFunc() {
        btnSearch2 = findViewById(R.id.btnSearch2);
        edtSearch2 = findViewById(R.id.edtSearch2);
    }
}