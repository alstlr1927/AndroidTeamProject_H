package com.cookandroid.androidteamproject_h;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Fragment_GPS extends Fragment implements OnMapReadyCallback{

    private Button btn1,btn2;

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private RecyclerView recyclerView_gps;
    private LinearLayoutManager layoutManager;
    private GPSAdapter adapter;
    private MapView map = null;
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;
    Location myLocation = null;

    private double goalLng = 0.0, goalLat = 0.0;
    static final String TAG = "GPSActivity";
    GoogleMap mMap =null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gps_fragment, container, false);

        checkList.removeAll(checkList);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
        findViewByIdFunc(view);

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



        btn1.setOnClickListener((View v) ->{
            MainActivity.db.execSQL("DELETE FROM checker_" + LoginActivity.userID + ";");
            adapter.notifyDataSetChanged();
            getActivity().finish();
        });

        map = (MapView) view.findViewById(R.id.map);
        map.getMapAsync(this);


        btn2.setOnClickListener(view1 -> {

            locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            myLocation = getMyLocation();
            if(myLocation != null){
                double lat = myLocation.getLatitude();
                double org = myLocation.getLongitude();

                LatLng SEOUL = new LatLng(lat,org);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(SEOUL);
                markerOptions.title("내위치");
                markerOptions.snippet("응응");
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            }else{
                Toast.makeText(getActivity(), "gps가 안잡혀요", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


    private void findViewByIdFunc(View view) {
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
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
        LatLng SEOUL = new LatLng(37.56,126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

    };

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
}
