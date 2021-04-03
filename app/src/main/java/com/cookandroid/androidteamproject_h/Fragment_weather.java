package com.cookandroid.androidteamproject_h;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Fragment_weather extends Fragment {
    private int tabPosition;

    private View rootView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ImageView weathericon;

    private TextView current_temp;          //현재기온
    private TextView current_desc;          //설명
    private TextView current_temp_max;
    private TextView current_temp_min;
    private String temperature;

    private String dailyLow;
    private String dailyHigh;
    private String temp_f;

    private String temp_extra;
    private ArrayList<HourlyItem> hourlyItemList = new ArrayList<>();
    private ArrayList<DailyItem> dailyItemList = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        initView(inflater, container, savedInstanceState);

        displayWeather(rootView.getContext());

        return rootView;
    }


    public void displayWeather(Context context) {
        openProgressDialog();

        float lat = PreferenceManager.getFloat(context,"LATITUDE");
        float lon = PreferenceManager.getFloat(context,"LONGITUDE");

        find_weather(lat,lon);
        find_future_weather(lat,lon);

    }
    
    public void find_future_weather(float lat, float lon) {
        String url="http://api.openweathermap.org/data/2.5/onecall?appid=fdf791ec127c3eec1f20991329a20e2f&units=metric&id=1835848&lang=kr";
        url += "&lat="+String.valueOf(lat)+"&lon="+String.valueOf(lon);
        Log.e("SEULGI WEATHER API URL", url);

        if(!hourlyItemList.isEmpty()) hourlyItemList.clear();
        hourlyItemList = new ArrayList<>();

        if(!dailyItemList.isEmpty()) dailyItemList.clear();
        dailyItemList = new ArrayList<>();

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray hourly_object = response.getJSONArray("hourly");
                    JSONArray daily_object = response.getJSONArray("daily");

                    for(int i=0;i<hourly_object.length() && i<36; i+=2){ //2시간 간격 | 18번만 나오게
                        JSONObject rec= hourly_object.getJSONObject(i);

                        //시간
                        String dt = rec.getString("dt");
                        long timestamp = Long.parseLong(dt);
                        Date date = new java.util.Date(timestamp*1000L);
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("a h" + "시");
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
                        dt = sdf.format(date);

                        //온도
                        temp_f = rec.getString("temp");
                        temp_f = String.valueOf(Math.round(Double.valueOf(temp_f)));

                        JSONArray weather_object = rec.getJSONArray("weather");
                        JSONObject weather = weather_object.getJSONObject(0);
                        String icon = weather.getString("icon");
                        int resID = getResId("icon_"+icon, R.drawable.class);

                        if(i==0){
                            hourlyItemList.add(new HourlyItem("지금",resID,temp_f+getString(R.string.temperature_unit)));
                        }
                        else {
                            hourlyItemList.add(new HourlyItem(dt,resID,temp_f+getString(R.string.temperature_unit)));
                        }
                    }

                    for(int i=1; i<daily_object.length(); i++){
                        JSONObject rec = daily_object.getJSONObject(i);
                        JSONObject get_temp = rec.getJSONObject("temp");

                        //요일
                        String dt = rec.getString("dt");
                        long timestamp = Long.parseLong(dt);
                        Date date = new java.util.Date(timestamp*1000L);
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE", Locale.KOREAN);
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
                        dt = sdf.format(date);

                        //최저기온
                        dailyLow = get_temp.getString("min");
                        dailyLow = String.valueOf(Math.round(Double.valueOf(dailyLow)));

                        //최고기온
                        dailyHigh = get_temp.getString("max");
                        dailyHigh = String.valueOf(Math.round(Double.valueOf(dailyHigh)));

                        //아이콘
                        JSONArray weather_object = rec.getJSONArray("weather");
                        JSONObject weather = weather_object.getJSONObject(0);
                        String icon = weather.getString("icon");
                        int resID = getResId("icon_"+icon, R.drawable.class);

                        dailyItemList.add(new DailyItem(dt,dailyLow+getString(R.string.temperature_unit),dailyHigh+getString(R.string.temperature_unit),resID));

                    }

                    /* HOURLY RECYCLERVIEW */
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.hourly_recycler);
                    LinearLayoutManager layoutManager_h= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                    recyclerView.setLayoutManager(layoutManager_h);

                    HourlyItemAdapter adapter_h;
                    adapter_h = new HourlyItemAdapter(getActivity(),hourlyItemList);
                    recyclerView.setAdapter(adapter_h);
                    adapter_h.notifyDataSetChanged();

                    /*DAILY RECYCLERVIEW */
                    recyclerView2 = (RecyclerView) rootView.findViewById(R.id.daily_recycler);
                    LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
                    recyclerView2.setLayoutManager(layoutManager);

                    DailyItemAdapter adapter;
                    adapter = new DailyItemAdapter(getActivity(),dailyItemList);
                    recyclerView2.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    closeProgressDialog();

                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SEULGI API RESPONSE",error.toString());
            }
        }
        );

        RequestQueue queue =  Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jor);
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void closeProgressDialog() {
        progressDialog.dismiss();
    }

    public void find_weather(float lat, float lon) {
        String url="http://api.openweathermap.org/data/2.5/weather?appid=fdf791ec127c3eec1f20991329a20e2f&units=metric&id=1835848&lang=kr";
        url += "&lat="+String.valueOf(lat)+"&lon="+String.valueOf(lon);
        Log.e("SEULGI1 WEATHER API URL", url);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray weather_object = response.getJSONArray("weather");

                    //기온
                    temperature = main_object.getString("temp");
                    temperature = String.valueOf(Math.round(Double.valueOf(temperature)));
                    temp_extra = temperature;
                    current_temp.setText(temperature+getString(R.string.temperature_unit));

                    //최고온도
                    String temp_max = main_object.getString("temp_max");
                    temp_max = String.valueOf(Math.round(Double.valueOf(temp_max)));
                    current_temp_max.setText(getString(R.string.temperature_max)+" "+temp_max+getString(R.string.temperature_unit));

                    //최저온도
                    String temp_min = main_object.getString("temp_min");
                    temp_min = String.valueOf(Math.round(Double.valueOf(temp_min)));
                    current_temp_min.setText(getString(R.string.temperature_min)+" "+temp_min+getString(R.string.temperature_unit));

                    JSONObject weather= weather_object.getJSONObject(0);
                    String description = weather.getString("description");
                    current_desc.setText(description);

                    //날씨 아이콘
                    String icon = weather.getString("icon");
                    int resID = getResId("icon_"+icon, R.drawable.class);
                    weathericon.setImageResource(resID);

                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SEULGI API RESPONSE",error.toString());
            }
        }
        );

        RequestQueue queue =  Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jor);
    }

    private void openProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();
    }

    private void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        current_temp = (TextView)rootView.findViewById(R.id.temp_now);
        current_desc=(TextView)rootView.findViewById(R.id.weather_description);
        current_temp_max =(TextView)rootView.findViewById(R.id.temp_max);
        current_temp_min =(TextView)rootView.findViewById(R.id.temp_min);
        weathericon = (ImageView)rootView.findViewById(R.id.image_weather);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);

        /* pull to refresh */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* 새로고침 시 수행될 코드 */
                displayWeather(rootView.getContext());

                /* 새로고침 완료 */
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setTabPosition(int position) { tabPosition=position; }
}
