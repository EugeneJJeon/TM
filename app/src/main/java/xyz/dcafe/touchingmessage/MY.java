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
    public static String GCMID;

    public static String getName(Context context) {
        SharedPreferences pref = getPreferences(context);
        Number = pref.getString(SharedPreferencesProperty.Number, "");
        NickName = pref.getString(SharedPreferencesProperty.NickName, "");
        GCMID = pref.getString(SharedPreferencesProperty.GCMID, "");
        return NickName;
    }

    public static void setName(Context context, String nickName) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        //TODO: debug mode! so, change release mode!
//        TelephonyManager systemService = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        String phoneNumber = systemService.getLine1Number();
//        phoneNumber = phoneNumber.substring(phoneNumber.length()-10, phoneNumber.length());
//        phoneNumber="0" + phoneNumber;
        String phoneNumber = "00000000000";

        editor.putString(SharedPreferencesProperty.Number, phoneNumber);
        Number = phoneNumber;

        editor.putString(SharedPreferencesProperty.NickName, nickName);
        NickName = nickName;

        editor.commit();
    }

    public static String getGCMID(Context context) {
        SharedPreferences pref = getPreferences(context);
        GCMID = pref.getString(SharedPreferencesProperty.GCMID, "");
        return GCMID;
    }

    public static void setGCMID(Context context, String gcmID) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        GCMID = gcmID;

        editor.putString(SharedPreferencesProperty.GCMID, GCMID);
        editor.commit();
    }

    public static void deleteName(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(SharedPreferencesProperty.Number);
        editor.remove(SharedPreferencesProperty.NickName);
        editor.remove(SharedPreferencesProperty.GCMID);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context) {
        String appName = context.getText(R.string.app_name).toString();
        return context.getSharedPreferences(appName, Context.MODE_PRIVATE);
    }
}
