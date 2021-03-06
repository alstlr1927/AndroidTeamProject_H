package com.cookandroid.androidteamproject_h;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class    Fragment_event extends Fragment {

    static final String TAG = "MAP";

    static ArrayList<ThemeData> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MapAdapter adapter;

    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;

    private LinearLayout drawer;
    private DrawerLayout drawerLayout;
    private ImageView imgGpsPicture;
    private Button alert, information;
    private TextView tvTitle;

    private double startLat, startLng, goalLat, goalLng, distance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        findViewByIdFunc(view);

        recyclerSetting();

        eventHandlerFunc();

        return view;
    }

    private void recyclerSetting() {
        list.removeAll(list);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM checker_" + LoginActivity.userID + ";", null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                list.add(new ThemeData(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5)));
            }
        }

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MapAdapter(R.layout.item_event, list);
        recyclerView.setAdapter(adapter);
    }

    private void findViewByIdFunc(View view) {
        drawerLayout = view.findViewById(R.id.drawerLayout);
        recyclerView = view.findViewById(R.id.recyclerView_map);
        imgGpsPicture = view.findViewById(R.id.imgGpsPicture);
        alert = view.findViewById(R.id.alert);
        information = view.findViewById(R.id.information);
        drawer = view.findViewById(R.id.drawer);
        tvTitle = view.findViewById(R.id.tvTitle);
    }

    private void eventHandlerFunc() {
        adapter.setOnItemClickListener(new MapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                tvTitle.setText(list.get(pos).getTitle());

                drawerLayout.closeDrawer(drawer);
                Glide.with(getActivity()).load(list.get(pos).getFirstImage()).override(500, 300).into(imgGpsPicture);

                locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

                Location userLocation = getMyLocation();

                if(userLocation != null) {
                    startLat = userLocation.getLatitude();
                    startLng = userLocation.getLongitude();

                    Log.d(TAG, startLat + "  " + startLng);

                }

                goalLat = list.get(pos).getMapY();
                goalLng = list.get(pos).getMapX();

                distance = DistanceByDegree(startLat, startLng, startLat, startLng);

                alert.setOnClickListener((View view2) -> {
                    if(distance > 100.0) {
                        if(distance >= 1000.0) {
                            Log.d(TAG, Math.round(distance / 10.0) /100.0 +"km");
                            Toast.makeText(getActivity(), "?????? ????????? ????????????. (" + Math.round(distance / 10.0) /100.0 + "km)", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, Math.round(distance) + "m");
                            Toast.makeText(getActivity(), "?????? ????????? ????????????. (" + Math.round(distance) + "m)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(view2.getContext(), CameraActivity.class);
                        intent.putExtra("title", list.get(pos).getTitle());
                        view2.getContext().startActivity(intent);
                    }
                });

            }
        });

        information.setOnClickListener((View v) -> {
            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
            dlg.setTitle("??? ???");
            dlg.setMessage("???????????? ????????? ??? ???????????? 500m ???????????? ????????? ?????? ????????? ????????? ?????? ????????? ???????????????!!");
            dlg.setPositiveButton("??????", null);
            dlg.show();
        });
    }

    private Location getMyLocation() {
        Location currentLocation = null;

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation();
        } else {
            // ???????????? ?????? ?????????
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
}
