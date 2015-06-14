package xyz.dcafe.touchingmessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import xyz.dcafe.touchingmessage.services.Server;
import xyz.dcafe.touchingmessage.services.Sign;
import xyz.dcafe.touchingmessage.widgets.FloatingEditText;
import xyz.dcafe.touchingmessage.widgets.PaperButton;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class MainActivity extends Activity {
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "MainActivity";

    private String SENDER_ID = "703077999256";
    private GoogleCloudMessaging gcm;

    private FloatingEditText nameEditText;
    private PaperButton loginButton;

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 구글 서비스 체크
        //if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            MY.GCMID = getRegistrationId(this);

            if (MY.GCMID.isEmpty()) {
                registerInBackground();
            }
        //} else {
       //     Log.i(TAG, "No valid Google Play Services APK found.");
        //}

        if (!MY.getName(this).equals("")) {
            nextActivity();
        }

        nameEditText = (FloatingEditText) findViewById(R.id.input_name);
        loginButton = (PaperButton) findViewById(R.id.button_login);

        // EditText의 Enter 줄내림 방지
        nameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    // SharedPreference 를 확인하여 등록된 아이디를 찾는 기능
    // 등록된 아이디가 없다면, 빈 문자열 반환
    private String getRegistrationId(Context context) {
        if (MY.getGCMID(this).isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // 앱 버전 확인 후, 버전이 변경되었다면 기존 등록 아이디 제거
        int registeredVersion = MY.getPreferences(this).getInt(SharedPreferencesProperty.AppVersion, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return MY.GCMID;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // 아이디 서버에 요청, 그리고 앱에 등록
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    MY.GCMID = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + MY.GCMID;
                    Log.i(TAG, msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.i(TAG, msg);
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg + "\n");
            }
        }.execute(null, null, null);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                String nickName = nameEditText.getText().toString();
                if (nickName.isEmpty()) {
                    nameEditText.setValidateResult(false, "Error, Input your nickname!");
                }
                else if (checkNickName(nickName)) {
                    nameEditText.setValidateResult(false, "Error, not use special letters!");
                }
                else {
                    MY.setName(this, nickName);
                    //TODO: 서버에 가입되는 코드 작성해야 한다.
                    try {
                        String value = new Sign(this).execute().get();
                        if (value.equals(Server.SUCCESS)) {
                            nextActivity();
                        }
                        else {
                            MY.deleteName(this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private boolean checkNickName(String nickName) {
        // 한글, 영문, 숫자 제외 return false
        return !nickName.matches("^[\\u0030-\\u0039\\u0041-\\u005a\\u0061-\\u007a\\u3130-\\u318f\\uac00-\\ud7af]*$");
    }

    private void nextActivity() {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
        finish();
    }
}
