package com.cookandroid.androidteamproject_h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomMenuActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);

        findViewByIdFunc();

        setChangeFragment(0);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.gps_frag :
                        setChangeFragment(0);
                        break;
                    case R.id.frag2 :
                        setChangeFragment(1);
                        break;
                    case R.id.map_frag :
                        setChangeFragment(2);
                        break;
                    case R.id.frag4 :
                        setChangeFragment(3);
                        break;
                    case R.id.collect_frag :
                        setChangeFragment(4);
                        break;
//                    case R.id.appInfo_frag :
//                        setChangeFragment(5);
//                        break;
                }
                return true;
            }
        });
    }

    private void setChangeFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        switch(position) {
            case 0 :
                Fragment_GPS fragment_gps = new Fragment_GPS();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment_gps, "gps")
                        .commit();
                break;
            case 1 :
                ft.replace(R.id.frameLayout, new Fragment2());
                break;
            case 2 :
                ft.replace(R.id.frameLayout, new Fragment_Map());
                break;
            case 3 :
                ft.replace(R.id.frameLayout, new Fragment4());
                break;
            case 4 :
                ft.replace(R.id.frameLayout, new Fragment_collec());
                break;
//            case 5 :
//                ft.replace(R.id.frameLayout, new Fragment_appInfo());
//                break;
        }
        ft.commit();
    }

    private void findViewByIdFunc() {
        navigationView = findViewById(R.id.navigationView);
        frameLayout = findViewById(R.id.frameLayout);
    }
}