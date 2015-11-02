package projact.finalprojact.projact;

import android.widget.ImageView;

/**
 * Created by Admin on 06/10/2015.
 */
public class Singel_User {
    String UserName;
    ImageView UserImeg;

    public Singel_User(String userName, ImageView userImeg, boolean left) {
        UserName = userName;
        UserImeg = userImeg;
        this.left = left;
    }

    public ImageView getUserImeg() {
        return UserImeg;
    }

    public void setUserImeg(ImageView userImeg) {
        UserImeg = userImeg;
    }

    protected boolean left;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public Singel_User(Object p0) {
    }
    public Singel_User(Boolean left, String UserName){
        super();
        this.left=left;
        this.UserName=UserName;
    }
}
