package com.cookandroid.androidteamproject_h;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tutorial_frag2 extends Fragment {

    private View view;

    // 뷰페이저를 통해서 슬라이딩이나 탭키를 눌러서 뷰페이저 프래그먼트가 변경이 되려면 현재 프래그먼트 상태를 저장하는 변수 필요.
    public static Tutorial_frag2 newInstance(){

        Tutorial_frag2 fragment2 = new Tutorial_frag2();

        return fragment2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tutorial_frag2, container, false); // 메인 액티비티의 setContentView와 같음.
        return view;
    }
}
