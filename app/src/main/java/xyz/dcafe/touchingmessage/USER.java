package xyz.dcafe.touchingmessage;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class User {
    public String nickName;
    public String number;

    public User(String nickName, String number) {
        this.nickName = nickName;
        this.number = number;
    }

    // Adapter에 오브젝트를 넣을 때, 중요한 키-포인트가 된다!
    @Override
    public String toString() {
        return this.nickName;
    }
}
