package com.cookandroid.androidteamproject_h;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_collec extends Fragment implements View.OnClickListener {

    private View view;

    private RecyclerView recyclerViewCollec;
    private ArrayList<ThemeData> collectList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private CollecAdapter adapter;

    private FloatingActionButton fb, fb1, fb2;

    private boolean isFbOpen = false;
    private Animation fab_open, fab_close;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collect, container, false);

        findViewByIdFunc();

        recyclerViewSetting();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb :
                animation();
                break;
            case R.id.fb1 :
                animation();
                break;
            case R.id.fb2 :
                animation();
                break;
        }
    }

    private void findViewByIdFunc() {
        recyclerViewCollec = view.findViewById(R.id.recyclerViewCollec);
        fb = view.findViewById(R.id.fb);
        fb1 = view.findViewById(R.id.fb1);
        fb2 = view.findViewById(R.id.fb2);

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);

        fb.setOnClickListener(this);
        fb1.setOnClickListener(this);
        fb2.setOnClickListener(this);
    }

    private void recyclerViewSetting() {
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
    }

    public void animation() {

        if (isFbOpen) {

            fb1.startAnimation(fab_close);
            fb2.startAnimation(fab_close);
            fb1.setClickable(false);
            fb2.setClickable(false);
            isFbOpen = false;

        } else {

            fb1.startAnimation(fab_open);
            fb2.startAnimation(fab_open);
            fb1.setClickable(true);
            fb2.setClickable(true);
            isFbOpen = true;

        }
    }
}
