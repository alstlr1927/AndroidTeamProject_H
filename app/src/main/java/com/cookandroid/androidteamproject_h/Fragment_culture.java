package com.cookandroid.androidteamproject_h;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Fragment_culture extends Fragment {
    private View view;
    private ProgressDialog pDialog;
    private RelativeLayout relativeLayout;
    private LinearLayoutManager layoutManager;
    private Context context;
    private AlertDialog.Builder dialog;
    final static String TAG = "ThemeActivity";
    static final String KEY = "CbOMUNZgaHbm9JGpeKVIG3z4RxvE1IxfjVbKdWkmD4Wqbzb9WfDEjBJpMGJ%2FNMgm97UCS8vUeAVL66X3QS5Zew%3D%3D";


    private ArrayList<Integer> contenIdList = new ArrayList<>();


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
