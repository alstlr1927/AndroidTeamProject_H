package com.cookandroid.androidteamproject_h;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment_Map extends Fragment {

    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private RecyclerView recyclerView_map;
    private LinearLayoutManager layoutManager;
    private MapAdapter adapter;

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
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView_map.setLayoutManager(layoutManager);
        adapter = new MapAdapter(R.layout.item_map, checkList);
        recyclerView_map.setAdapter(adapter);





        return view;
    }

}
