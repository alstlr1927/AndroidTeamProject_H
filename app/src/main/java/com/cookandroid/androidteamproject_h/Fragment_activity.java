package com.cookandroid.androidteamproject_h;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class Fragment_activity extends Fragment {

    private LottieAnimationView lottieAnimationView;

    // 생성자 생성할것
    public Fragment_activity() {

    }

    public static Fragment_activity newInstance() {
        Fragment_activity fragment_activity = new Fragment_activity();
        return fragment_activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        return view;
    }
}
