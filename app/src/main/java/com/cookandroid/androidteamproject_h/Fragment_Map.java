package com.cookandroid.androidteamproject_h;

import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private ArrayList<String> check = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView_map;
    private LinearLayoutManager layoutManager;
    private MapAdapter adapter;

    private LocationManager locationManager;

    private GoogleMap googleMap1;
    private GoogleMap googleMap2;

    private MapView mapView;

    private View view1;

    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private double lat = 0.0, lng = 0.0;
    private boolean win = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM checker_" + LoginActivity.userID + ";", null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                checkList.add(new ThemeData(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5)));
            }
        }

        recyclerView_map = view.findViewById(R.id.recyclerView_map);
        drawerLayout = view.findViewById(R.id.drawerLayout_map);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_map.setLayoutManager(layoutManager);
        adapter = new MapAdapter(R.layout.item_map, checkList);
        recyclerView_map.setAdapter(adapter);

        mapView = view1.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        recyclerView_map.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView_map, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                boolean tag = true;

                if(check.size() == 0) {
                    check.add(checkList.get(position).getTitle());
                } else {
                    for(int i =0;i < check.size();i++) {
                        if(check.get(i).equals(checkList.get(position).getTitle())) {
                            check.remove(i);
                            tag =false;
                            break;
                        }
                    }
                    if(tag) {
                        check.add(checkList.get(position).getTitle());
                    }
                }
                mapView = view1.findViewById(R.id.mapView);

                mapView.getMapAsync(Fragment_Map.this);

                drawerLayout.closeDrawer(Gravity.LEFT);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap1 = googleMap;
        googleMap2 = googleMap;

        googleMap1.clear();

        if(check.size() >=1) {

            for(String x:check) {

                for(ThemeData y:checkList) {

                    if(x.equals(y.getTitle())) {

                        LatLng latLng = new LatLng(y.getMapY(), y.getMapX());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(y.getTitle());
                        markerOptions.snippet(y.getAddr());
                        markerOptions.position(latLng);

                        googleMap1.addMarker(markerOptions);
                        googleMap1.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    }
                }
            }
        }

        if(win) {
            LatLng latLng = new LatLng(lat, lng);

            Log.d("MAP", lat + "  " + lng);

            MarkerOptions markerOptions = new MarkerOptions();

        }
    }
}
