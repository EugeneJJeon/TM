package xyz.dcafe.touchingmessage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class USER {
    public static String NickName;

    public static String getName(Context context) {
        String appName = context.getText(R.string.app_name).toString();
        SharedPreferences pref = context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        NickName = pref.getString("NickName", "");
        return NickName;
    }

    public static void setName(Context context, String nickName) {
        NickName = nickName;
        String appName = context.getText(R.string.app_name).toString();
        SharedPreferences pref = context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NickName", nickName);
        editor.commit();
    }
}
