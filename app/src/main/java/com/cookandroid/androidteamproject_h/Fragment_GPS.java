package com.cookandroid.androidteamproject_h;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_GPS extends Fragment implements OnMapReadyCallback, View.OnClickListener{

    private FloatingActionButton btn1,btn2,btn3;

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private RecyclerView recyclerView_gps;
    private LinearLayoutManager layoutManager;
    private GPSAdapter adapter;
    private MapView map = null;
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;
    Location myLocation = null;
    private DrawerLayout drawerLayout;
    private LinearLayout gps_layout;
    LatLng Gaul;
    private Animation btn_open, btn_close;
    private boolean fbOpen = false;

    private double goalLng = 0.0, goalLat = 0.0,distance=0.0;
    static final String TAG = "GPSActivity";
    GoogleMap mMap =null;
    private boolean flag = false;
    LatLng SEOUL1;
    CircleOptions circle1KM;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        checkList.removeAll(checkList);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
        findViewByIdFunc(view);

        findLikeList(view);

        eventHandlerFunc(view);


        return view;
    }

    public void eventHandlerFunc(View view) {

        map = (MapView) view.findViewById(R.id.map);
        map.getMapAsync(this);

        adapter.setOnItemClickListener(new GPSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                drawerLayout.closeDrawer(gps_layout);
                double mapX = checkList.get(pos).getMapY();
                double mapY = checkList.get(pos).getMapX();
                String title = checkList.get(pos).getTitle();
                String addr = checkList.get(pos).getAddr();

                Gaul = new LatLng(mapX,mapY);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(Gaul);
                markerOptions.title(title);
                markerOptions.snippet(addr);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Gaul));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

        });

    }


    public void findLikeList(View view) {
        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM checker_" + LoginActivity.userID + ";", null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                checkList.add(new ThemeData(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5)));
            }
        }

        recyclerView_gps = view.findViewById(R.id.recyclerView_gps);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_gps.setLayoutManager(layoutManager);
        adapter = new GPSAdapter(R.layout.item_gps, checkList);
        recyclerView_gps.setAdapter(adapter);
    }


    private void findViewByIdFunc(View view) {
        gps_layout = view.findViewById(R.id.gps_drawer);
        drawerLayout = view.findViewById(R.id.gps_activity);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        btn_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(map != null){
            map.onCreate(savedInstanceState);
        }
    }

   @Override
   public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       LatLng SEOUL = new LatLng(37.56, 126.97);

       MarkerOptions markerOptions = new MarkerOptions();

       markerOptions.position(SEOUL);

       markerOptions.title("서울");

       markerOptions.snippet("수도");

       googleMap.addMarker(markerOptions);

       googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));

       googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
   }


    private Location getMyLocation() {
        Location currentLocation = null;

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation();
        } else {
            Log.d(TAG,"널포인터");
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lat = currentLocation.getLatitude();
                double lng = currentLocation.getLongitude();
            }
        }
        return currentLocation;
    }
    public double DistanceByDegree(double lat1, double lng1, double lat2, double lng2) {
        Location startLoc = new Location("start");
        Location goalLoc = new Location("goal");

        startLoc.setLatitude(lat1);
        startLoc.setLongitude(lng1);

        goalLoc.setLatitude(lat2);
        goalLoc.setLongitude(lng2);

        double distance = startLoc.distanceTo(goalLoc);

        return distance;
    }
    private void fbAnimation() {
        if (fbOpen) {
            btn2.startAnimation(btn_close);
            btn3.startAnimation(btn_close);
            btn2.setClickable(false);
            btn3.setClickable(false);
            fbOpen = false;
        } else {
            btn2.startAnimation(btn_open);
            btn3.startAnimation(btn_open);
            btn2.setClickable(true);
            btn3.setClickable(true);
            fbOpen = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1 :
                fbAnimation();
                break;
            case R.id.btn2 :
                fbAnimation();
                locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
                myLocation = getMyLocation();
                if(myLocation != null){
                    double lat = myLocation.getLatitude();
                    double org = myLocation.getLongitude();

                    SEOUL1 = new LatLng(lat,org);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEOUL1);
                    markerOptions.title("내위치");
                    markerOptions.snippet("응응");
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL1));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }else{
                    Toast.makeText(getActivity(), "gps가 안잡혀요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn3 :
                if(btn3.isActivated()){
                    btn3.setActivated(false);
                    mMap.clear();
                    flag = false;
                }else{

                    fbAnimation();
                    btn3.setActivated(true);
                    flag = true;
                    locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
                    myLocation = getMyLocation();
                    double lat = myLocation.getLatitude();
                    double org = myLocation.getLongitude();

                    SEOUL1 = new LatLng(lat,org);
                    circle1KM = new CircleOptions().center(SEOUL1) //원점
                            .radius(100)      //반지름 단위 : m
                            .strokeWidth(0f)  //선너비 0f : 선없음
                            .fillColor(Color.parseColor("#880000ff")); //배경색

                    mMap.addCircle(circle1KM);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEOUL1);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL1));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
                    myLocation = getMyLocation();
                    double lat1 = myLocation.getLatitude();
                    double org1 = myLocation.getLongitude();
                    fbAnimation();
                    distance = DistanceByDegree(lat1, org1, goalLng, goalLat);
                    if(distance <= 500.0){
                        MarkerOptions markerOptions1 = new MarkerOptions();
                        markerOptions1.position(Gaul);
                        mMap.addMarker(markerOptions1);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Gaul));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }
                }

                break;
        }
    }
}
