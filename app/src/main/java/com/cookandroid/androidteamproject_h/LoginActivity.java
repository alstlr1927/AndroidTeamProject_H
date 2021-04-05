package com.cookandroid.androidteamproject_h;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    private LoginButton btnKakao;
    private Spinner spinner;

    public static Long userID = null;
    public static int areaCode = 1;

    private ArrayList<String> items = new ArrayList<>(Arrays.asList("서울", "인천", "대전", "대구", "광주", "부산",
            "울산", "세종", "경기도", "강원도", "제주도"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = findViewById(R.id.spinner);

//        getAppKeyHash();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        areaCode = 1;
                        break;
                    case 1 :
                        areaCode = 2;
                        break;
                    case 2 :
                        areaCode = 3;
                        break;
                    case 3 :
                        areaCode = 4;
                        break;
                    case 4 :
                        areaCode = 5;
                        break;
                    case 5 :
                        areaCode = 6;
                        break;
                    case 6 :
                        areaCode = 7;
                        break;
                    case 7 :
                        areaCode = 8;
                        break;
                    case 8 :
                        areaCode = 31;
                        break;
                    case 9 :
                        areaCode = 32;
                        break;
                    case 10 :
                        areaCode = 39;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
    }

//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something);
//            }
//        } catch (Exception e) {
//            Log.e("name not found", e.toString());
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다." +errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " +errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {

                    try {
                        MainActivity.dbHelper = new DBHelper(getApplicationContext());

                        userID = result.getId();

                        MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

                        String insertUserInfo = "INSERT OR REPLACE INTO userTBL VALUES('"
                                + result.getId() + "','"
                                + result.getNickname() + "','"
                                + result.getProfileImagePath() + "');";
                        MainActivity.db.execSQL(insertUserInfo);

                        String createFavoriteTBL = "CREATE TABLE IF NOT EXISTS favorite_" + result.getId() + "("
                                + "title TEXT PRIMARY KEY,"
                                + "addr TEXT,"
                                + "mapX REAL,"
                                + "mapY REAL,"
                                + "firstImage TEXT);";
                        MainActivity.db.execSQL(createFavoriteTBL);

                        String createCheckerTBL = "CREATE TABLE IF NOT EXISTS checker_" + result.getId() + "("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "title TEXT,"
                                + "addr TEXT,"
                                + "mapX REAL,"
                                + "mapY REAL,"
                                + "firstImage TEXT,"
                                + "picture TEXT,"
                                + "contents TEXT,"
                                + "complete INTEGER);";
                        MainActivity.db.execSQL(createCheckerTBL);
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplicationContext(), ThemeActivity.class);
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요." + exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}