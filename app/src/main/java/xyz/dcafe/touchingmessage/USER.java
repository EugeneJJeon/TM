package xyz.dcafe.touchingmessage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class User implements Parcelable{
    public static final String TAG = "USER";
    public String number;
    public String nickName;
    public String gcmID;

    public User(String number, String nickName, String gcmID) {
        this.number = number;
        this.nickName = nickName;
        this.gcmID = gcmID;
    }

    // Adapter에 오브젝트를 넣을 때, 중요한 키-포인트가 된다!
    @Override
    public String toString() {
        return this.nickName;
    }

    /* setting Parcelable */
    public User (Parcel src) {
        this.number = src.readString();
        this.nickName = src.readString();
        this.gcmID = src.readString();
    }
    public static final Creator CREATOR = new Creator() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
    public int describeContents() {
        return 0;
    }
    //분해
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(nickName);
        dest.writeString(gcmID);
    }
}
