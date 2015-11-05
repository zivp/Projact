package projact.finalprojact.projact;

import android.graphics.Bitmap;

/**
 * Created by Admin on 06/10/2015.
 */
public class Singel_User {
    String UserName;
    Bitmap UserImage;
    String UserParseID;


    public Singel_User(boolean left,String userName) {
        UserName = userName;
        this.left = left;
    }
    public String getUserParseID() {
        return UserParseID;
    }
    public void setUserParseID(String userParseID) {
        UserParseID = userParseID;
    }
    public Bitmap getUserImage() {
        return UserImage;
    }
    public void setUserImage(Bitmap userImage) {
        UserImage = userImage;
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
    public Singel_User(Boolean left,Bitmap bitmap, String UserName,String UserParseID){
        super();
        this.left=left;
        this.UserName=UserName;
        this.UserImage=bitmap;
        this.UserParseID=UserParseID;
    }
}
