package com.cookandroid.androidteamproject_h;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

public class App extends Application {

    private static volatile App instance = null;

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         *  Session Config에 대해서는 default 값들이 존재한다.
         *  필요한 상황에서만 override 해서 사용하면 됨.
         * @return Session 설정값.
         */
        // 카카오 로그인 세션을 불러올 때의 설정값을 설정하는 부분.
        public ISessionConfig getSessionConfig() {

            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                    /*로그인을 하는 방식을 지정하는 부분. AuthType 으로는 다음 네가지 방식이 있다.
                    KAKAO_TALK: 카카오톡으로 로그인 , KAKAO_STORY: 카카오스토리로 로그인, KAKAO_ACCOUNT: 웹뷰를 통한 로그인,
                    KAKAO_TALK_EXCLUDE_NATIVE_LOGIN: 카카오톡으로만 로그인+계정 없으면 계정 생성 버튼 제공
                    KAKAO_LOGIN_ALL: 모든 로그인 방식 사용가능. 정확히는, 카카오톡이나 카카오스토리가 있으면 그쪽으로 로그인 기능을 제공하고, 둘다 없으면 웹뷰로 로그인 제공.
                     */
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                    // SDK 로그인시 사용되는 WebView 에서 pause 와 resume 시에 Timer 를 설정하여 CPU 소모를 절약한다.
                    // true 를 리턴할경우 webView 로그인을 사용하는 화면에서 모든 webView 에 onPause 와 onResume 시에 Timer 설정해주어야 한다.
                    // 지정하지 않을 시 false 로 설정된다.
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                    // 로그인시 access token 과 refresh token 을 저장할 때의 암호와 여부를 결정한다.
                }

                @Nullable
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                    // 일반 사용자가 아닌 Kakao 와 제휴된 앱에서만 사용되는 값으로, 값을 채워주지 않을 경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                    // Kakao SDK 에서 사용되는 WebView 에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                    // true 일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return App.getGlobalApplicationContext();
                }
            };
        }
    }

    public static App getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(new KakaoSDKAdapter());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
