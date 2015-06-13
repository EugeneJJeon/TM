package xyz.dcafe.touchingmessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import xyz.dcafe.touchingmessage.widgets.FloatingEditText;
import xyz.dcafe.touchingmessage.widgets.PaperButton;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class MainActivity extends Activity {
    private FloatingEditText nameEditText;
    private PaperButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!USER.getName(this).equals("")) {
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
                    USER.setName(this, nickName);
                    //TODO: 서버에 가입되는 코드 작성해야 한다.
                    nextActivity();
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
