package com.cookandroid.androidteamproject_h;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ThemeViewPagerAdapter extends FragmentStatePagerAdapter {

    public ThemeViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return Fragment_culture.newInstance();
            case 1: return Fragment_attraction.newInstance();
            case 2: return Fragment_shopping.newInstance();
            case 3: return Fragment_food.newInstance();
            case 4: return Fragment_activity.newInstance();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    //상단의 탭 레이아웃 인디케이터에 텍스트를 선언해주는것
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0: return "Culture";
            case 1: return "Attraction";
            case 2: return "Shopping";
            case 3: return "Food";
            case 4: return "Activity";
            default: return null;
        }
    }
}
