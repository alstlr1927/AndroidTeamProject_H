package com.cookandroid.androidteamproject_h;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener, ViewPager.OnPageChangeListener {

    private FragmentStatePagerAdapter fragmentStatePagerAdapter;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListView listView;

    private ArrayList<ThemeData> list = new ArrayList<>();
    private ArrayList<ThemeData> checkList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();

    private boolean dragged;
    private long backButtonTime = 0L;

    private FloatingActionButton fb1, fb2, fb3;
    private Animation fab_open, fab_close;
    private boolean fbOpen = false;

    private EditText edtSearch;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        findViewByIdFunc();

        fragmentStatePagerAdapter = new ThemeViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentStatePagerAdapter);

        tabLayout.post(() -> {
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabsFromPagerAdapter(fragmentStatePagerAdapter);
            tabLayout.addOnTabSelectedListener(ThemeActivity.this);
        });

        viewPager.addOnPageChangeListener(this);
    }

    private void findViewByIdFunc() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fb1 = findViewById(R.id.fb1);
        fb2 = findViewById(R.id.fb2);
        fb3 = findViewById(R.id.fb3);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fb1.setOnClickListener(this);
        fb2.setOnClickListener(this);
        fb3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb1:
                fbAnimation();
                break;
            case R.id.fb2:
                break;
            case R.id.fb3:

                list.removeAll(list);
                checkList.removeAll(checkList);
                titleList.removeAll(titleList);

                fbAnimation();

                final View dialogView = View.inflate(v.getContext(), R.layout.dialog_favorite, null);

                listView = dialogView.findViewById(R.id.listView);

                MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

                final Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM favorite_" + LoginActivity.userID + ";", null);

                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        list.add(new ThemeData(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4)));
                        titleList.add(cursor.getString(0));
                    }
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_check_box_view, titleList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



                final Dialog dialog = new Dialog(dialogView.getContext());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(dialogView);
                dialog.show();

                break;
        }
    }

    private void fbAnimation() {
        if (fbOpen) {
            fb2.startAnimation(fab_close);
            fb3.startAnimation(fab_close);
            fb2.setClickable(false);
            fb3.setClickable(false);
            fbOpen = false;
        } else {
            fb2.startAnimation(fab_open);
            fb3.startAnimation(fab_open);
            fb2.setClickable(true);
            fb3.setClickable(true);
            fbOpen = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (!dragged) {
            viewPager.setCurrentItem(tab.getPosition());
        }
        dragged = false;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i == ViewPager.SCROLL_STATE_DRAGGING)
            dragged = true;
    }

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        long gapTime = currentTime - backButtonTime;

        if (gapTime >= 0 && gapTime <= 2000) {
            super.onBackPressed();
        } else {
            backButtonTime = currentTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}