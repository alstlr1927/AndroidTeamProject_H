package com.cookandroid.androidteamproject_h;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tutorial_frag6 extends Fragment {

    private View view;

    // 뷰페이저를 통해서 슬라이딩이나 탭키를 눌러서 뷰페이저 프래그먼트가 변경이 되려면 현재 프래그먼트 상태를 저장하는 변수 필요.
    public static Tutorial_frag6 newInstance(){

        Tutorial_frag6 fragment6 = new Tutorial_frag6();

        return fragment6;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tutorial_frag6, container, false); // 메인 액티비티의 setContentView와 같음.
        return view;
    }
}
