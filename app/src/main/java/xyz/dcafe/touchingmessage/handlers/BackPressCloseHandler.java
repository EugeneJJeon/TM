package xyz.dcafe.touchingmessage.handlers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Context mContext;

    public BackPressCloseHandler(Context context) {
        mContext = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ((Activity)mContext).finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(mContext,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
