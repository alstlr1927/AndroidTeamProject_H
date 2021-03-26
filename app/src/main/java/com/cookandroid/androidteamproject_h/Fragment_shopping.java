package com.cookandroid.androidteamproject_h;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_shopping extends Fragment {
    private View view;
    ProgressDialog pDialog;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;


    static final String TAG = "ThemeActivity";
    static final String KEY = "2sODhH1TupFo8WC14q9q9smsSqNhEbiqYsJwrsBQP0svyz%2FWJvpZ1080fEkDZQC6mw%2BOBFRxQ%2BbuFfmKu8BOSg%3D%3D";
    static final String appName = "tourApp";

    // 생성자 생성할것
    public Fragment_shopping() {

    }

    public static Fragment_shopping newInstance() {
        Fragment_shopping fragment_shopping = new Fragment_shopping();
        return fragment_shopping;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);

        recyclerView = view.findViewById(R.id.grid_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

//        Fragment_shopping.AsyncTaskClassMain async = new Fragment_shopping



        return view;
    }

//    public class AsyncTaskClassMain extends android.os.AsyncTask<Integer, Long, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            displayLoader();
//        }
//    }
//
//    public void displayLoader() {
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("잠시만 기다려 주세요");
//        pDialog.setIndeterminate
//    }
}
