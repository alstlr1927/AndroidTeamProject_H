package com.cookandroid.androidteamproject_h;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Fragment_GPS extends Fragment implements OnMapReadyCallback {

    private Button btn1;

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private RecyclerView recyclerView_gps;
    private LinearLayoutManager layoutManager;
    private GPSAdapter adapter;
    private MapView map = null;

    private double goalLng = 0.0, goalLat = 0.0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gps_fragment, container, false);

        checkList.removeAll(checkList);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

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

        btn1 = view.findViewById(R.id.btn1);
        btn1.setOnClickListener((View v) ->{
            MainActivity.db.execSQL("DELETE FROM checker_" + LoginActivity.userID + ";");
            adapter.notifyDataSetChanged();
            getActivity().finish();
        });

        map = (MapView) view.findViewById(R.id.map);
        map.getMapAsync(this);

        return view;
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
        LatLng SEOUL = new LatLng(37.56,126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

    }
}
