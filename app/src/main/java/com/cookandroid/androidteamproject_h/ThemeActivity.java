package com.cookandroid.androidteamproject_h;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private String strName, strProfile;

    private EditText edtSearch;
    private ImageButton btnSearch;

    private Bitmap bitmap;

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

        strName = getIntent().getStringExtra("name");
        strProfile = getIntent().getStringExtra("profile");

        CharSequence txt = "txt";
        int time = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), txt, time);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_toastview, (ViewGroup)findViewById(R.id.containers));

        TextView txtName = view.findViewById(R.id.txtName);
        CircleImageView circleImageView = view.findViewById(R.id.txtProfileImage);

        txtName.setText(strName);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strProfile);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            circleImageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toast.setView(view);
        toast.show();
    }

    private void findViewByIdFunc() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fb1 = findViewById(R.id.fb1);
        fb2 = findViewById(R.id.fb2);
        fb3 = findViewById(R.id.fb3);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fb1.setOnClickListener(this);
        fb2.setOnClickListener(this);
        fb3.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        edtSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb1:
                fbAnimation();
                break;
            case R.id.fb2:
                fbAnimation();
                Intent intent = new Intent(ThemeActivity.this, BottomMenuActivity.class);
                startActivity(intent);
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

                Button btnSave = dialogView.findViewById(R.id.btnSave);
                Button btnExit = dialogView.findViewById(R.id.btnExit);

                final Dialog dialog = new Dialog(dialogView.getContext());

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SparseBooleanArray booleanArray = listView.getCheckedItemPositions();

                        if(booleanArray.get(position)) {
                            checkList.add(list.get(position));
                            MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

                            Cursor cursor1 = MainActivity.db.rawQuery("SELECT title FROM checker_" + LoginActivity.userID + ";", null);

                            if(checkList != null) {
                                while(cursor1.moveToNext()) {
                                    if(cursor1.getString(0).equals(list.get(position).getTitle())) {
                                        Toast.makeText(getApplicationContext(), list.get(position).getTitle() +"은(는) 이미 리스트에 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                        checkList.remove(list.get(position));
                                    }
                                }
                                cursor1.moveToFirst();
                            }
                        } else {
                            checkList.remove(list.get(position));
                        }
                        cursor.close();
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ThemeActivity.this);
                        dlg.setMessage("정말로 찜목록에서 \""+ titleList.get(position) +"\" 을(를) 제거 하시겠습니까.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String favorDelete = "DELETE FROM favorite_" + LoginActivity.userID + " WHERE title ='" + list.get(position).getTitle() + "';";
                                MainActivity.db.execSQL(favorDelete);

                                adapter.remove(titleList.get(position));
                                adapter.notifyDataSetChanged();

                                list.remove(list.get(position));
                            }
                        });
                        dlg.setNegativeButton("취소", null);
                        dlg.show();
                        return true;
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(dialogView);
                dialog.show();

                btnSave.setOnClickListener((View view) -> {
                    MainActivity.db = MainActivity.dbHelper.getWritableDatabase();
                    Cursor cursor1 = MainActivity.db.rawQuery("SELECT * FROM checker_" + LoginActivity.userID + ";", null);
                    cursor1.moveToFirst();

                    if(checkList.size() + cursor1.getCount() > 9) {
                        Toast.makeText(getApplicationContext(),
                                "9개를 초과해서 체크리스트에 담을 수는 없습니다.(현재 체크리스트 : "+ cursor1.getCount() +"곳)", Toast.LENGTH_SHORT).show();
                    } else {
                        for(ThemeData data:checkList) {

                            String insertCheckList =
                                    "INSERT INTO checker_" + LoginActivity.userID + "(title, addr, mapX, mapy, firstImage) VALUES('"
                                    + data.getTitle() + "', '"
                                    + data.getAddr() + "', '"
                                    + data.getMapX() + "', '"
                                    + data.getMapY() + "', '"
                                    + data.getFirstImage() + "');";

                            MainActivity.db.execSQL(insertCheckList);
                        }
                        Toast.makeText(getApplicationContext(), "체크리스트 잘 담았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    cursor1.close();
                    dialog.dismiss();
                });

                btnExit.setOnClickListener((View view) -> {
                    dialog.dismiss();
                });

                cursor.close();

                break;

            case R.id.btnSearch :
                String word = edtSearch.getText().toString().trim();
                Intent intent2 = new Intent(ThemeActivity.this, SearchActivity.class);
                if(word.length() >= 2) {
                    intent2.putExtra("word", word);
                    startActivity(intent2);
                } else {
                    Toast.makeText(getApplicationContext(), "두 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            default :
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