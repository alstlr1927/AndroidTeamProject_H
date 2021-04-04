package com.cookandroid.androidteamproject_h;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tutorial_frag7 extends Fragment {

    private View view;
    private Button btnStart;

    // 뷰페이저를 통해서 슬라이딩이나 탭키를 눌러서 뷰페이저 프래그먼트가 변경이 되려면 현재 프래그먼트 상태를 저장하는 변수 필요.
    public static Tutorial_frag7 newInstance(){

        Tutorial_frag7 fragment7 = new Tutorial_frag7();

        return fragment7;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tutorial_frag7, container, false); // 메인 액티비티의 setContentView와 같음.

        btnStart = view.findViewById(R.id.btnStart);

        btnStart.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}
