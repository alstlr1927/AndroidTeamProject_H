package com.cookandroid.androidteamproject_h;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TutorialViewPagerAdapter extends FragmentPagerAdapter {

    public TutorialViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // 뷰페이저에서 프래그먼트 교체를 보여주는 역할.
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return Tutorial_frag1.newInstance();

            case 1:

                return Tutorial_frag2.newInstance();

            case 2:

                return Tutorial_frag3.newInstance();

            case 3:

                return Tutorial_frag4.newInstance();

            case 4:

                return Tutorial_frag5.newInstance();

            case 5:

                return Tutorial_frag6.newInstance();

            case 6:

                return Tutorial_frag7.newInstance();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}
