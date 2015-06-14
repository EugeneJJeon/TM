package xyz.dcafe.touchingmessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class MY {
    public static String Number;
    public static String NickName;

    public static String getName(Context context) {
        String appName = context.getText(R.string.app_name).toString();
        SharedPreferences pref = context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        Number = pref.getString("Number", "");
        NickName = pref.getString("NickName", "");
        return NickName;
    }

    public static void setName(Context context, String nickName) {
        NickName = nickName;
        String appName = context.getText(R.string.app_name).toString();
        SharedPreferences pref = context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NickName", nickName);

        TelephonyManager systemService = (TelephonyManager)context.getSystemService    (Context.TELEPHONY_SERVICE);
        String phoneNumber = systemService.getLine1Number();
        phoneNumber = phoneNumber.substring(phoneNumber.length()-10, phoneNumber.length());
        phoneNumber="0" + phoneNumber;

        editor.putString("Number", phoneNumber);
        Number = phoneNumber;
        editor.commit();
    }
}
