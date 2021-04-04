package com.cookandroid.androidteamproject_h;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
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
    private DrawerLayout drawerLayout;
    private ConstraintLayout drawer;
    private Integer[] checkID = new Integer[] {R.id.check1, R.id.check2, R.id.check3, R.id.check4, R.id.check5,
            R.id.check6, R.id.check7, R.id.check8, R.id.check9};
    private ImageView[] check = new ImageView[checkID.length];

    private boolean isFbOpen = false;
    private Animation fab_open, fab_close;
    private Animation ai;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collect, container, false);

        findViewByIdFunc();

        layoutManager = new LinearLayoutManager(view.getContext());

        collectList.removeAll(collectList);

        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM checker_"+LoginActivity.userID+" WHERE complete=1;", null);
        while(cursor.moveToNext()) {
            collectList.add(new ThemeData(cursor.getString(1), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9)));
        }
        cursor.moveToFirst();

        checkInvisible();

        for(int i =1;i <=cursor.getCount();i++) {
            switch(i) {
                case 1 :
                    checkVisible(i);
                    break;
                case 2 :
                    checkVisible(i);
                    break;
                case 3 :
                    checkVisible(i);
                    break;
                case 4 :
                    checkVisible(i);
                    break;
                case 5 :
                    checkVisible(i);
                    break;
                case 6 :
                    checkVisible(i);
                    break;
                case 7 :
                    checkVisible(i);
                    break;
                case 8 :
                    checkVisible(i);
                    break;
                case 9 :
                    checkVisible(i);
                    break;
            }
        }

        adapter = new CollecAdapter(R.layout.item_collec, collectList);

        recyclerViewCollec.setLayoutManager(layoutManager);

        recyclerViewCollec.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private void checkVisible(int i) {
        check[i-1].setVisibility(View.VISIBLE);
        ai = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        check[i-1].startAnimation(ai);
    }

    private void checkInvisible() {
        for(int i =0;i <checkID.length;i++) {
            check[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb :
                animation();
                break;
            case R.id.fb1 :
                drawerLayout.openDrawer(drawer);
                animation();
                break;
            case R.id.fb2 :
                animation();
                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("Caution!!!");
                dlg.setMessage("정말 찜목록을 다 지우시겠습니까.(사진, 메모도 다 사라집니다.)");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collectList.removeAll(collectList);

                        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
                        MainActivity.db.execSQL("DROP TABLE IF EXISTS checker_" + LoginActivity.userID + ";");
                        MainActivity.db.execSQL("CREATE TABLE IF NOT EXISTS checker_" + LoginActivity.userID + "("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "title TEXT,"
                                + "addr TEXT,"
                                + "mapX REAL,"
                                + "mapY REAL,"
                                + "firstImage TEXT,"
                                + "picture TEXT,"
                                + "contents TEXT,"
                                + "overView TEXT,"
                                + "complete INTEGER);");
                        Intent intent = new Intent(view.getContext(), ThemeActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                break;
        }
    }

    private void findViewByIdFunc() {
        recyclerViewCollec = view.findViewById(R.id.recyclerViewCollec);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        drawer = view.findViewById(R.id.drawer);


        fb = view.findViewById(R.id.fb);
        fb1 = view.findViewById(R.id.fb1);
        fb2 = view.findViewById(R.id.fb2);

        for(int i =0;i <checkID.length;i++) {
            check[i] = view.findViewById(checkID[i]);
        }

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);

        fb.setOnClickListener(this);
        fb1.setOnClickListener(this);
        fb2.setOnClickListener(this);
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
