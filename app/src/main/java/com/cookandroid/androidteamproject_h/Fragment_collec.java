package com.cookandroid.androidteamproject_h;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment_collec extends Fragment {

    private RecyclerView recyclerViewCollec;
    private ArrayList<ThemeData> collectList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private CollecAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);

        recyclerViewCollec = view.findViewById(R.id.recyclerViewCollec);

        layoutManager = new LinearLayoutManager(view.getContext());

        collectList.removeAll(collectList);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM checker_"+LoginActivity.userID+" WHERE complete=1;", null);
        while(cursor.moveToNext()) {
            collectList.add(new ThemeData(cursor.getString(1), cursor.getString(6),
                    cursor.getString(9), cursor.getString(10), cursor.getInt(11)));
        }
        cursor.moveToFirst();

        adapter = new CollecAdapter(R.layout.item_collec, collectList);

        recyclerViewCollec.setLayoutManager(layoutManager);

        recyclerViewCollec.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
}
