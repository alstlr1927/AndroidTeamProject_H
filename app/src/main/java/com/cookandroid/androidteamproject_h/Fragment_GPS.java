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

import java.util.ArrayList;

public class Fragment_GPS extends Fragment {

    private Button btn1;

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private RecyclerView recyclerView_gps;
    private LinearLayoutManager layoutManager;
    private GPSAdapter adapter;

    BottomMenuActivity activity = new BottomMenuActivity();

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
        adapter = new GPSAdapter(R.layout.gps_item, checkList);
        recyclerView_gps.setAdapter(adapter);

        btn1 = view.findViewById(R.id.btn1);
        btn1.setOnClickListener((View v) ->{
            MainActivity.db.execSQL("DELETE FROM checker_" + LoginActivity.userID + ";");
            adapter.notifyDataSetChanged();
            getActivity().finish();
        });

        return view;
    }
}
