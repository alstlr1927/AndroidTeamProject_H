package com.cookandroid.androidteamproject_h;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class Fragment_culture extends Fragment {

    // 생성자 생성할것
    public Fragment_culture() {

    }

    public static Fragment_culture newInstance() {
        Fragment_culture fragment_culture = new Fragment_culture();
        return fragment_culture;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        return view;
    }
}
