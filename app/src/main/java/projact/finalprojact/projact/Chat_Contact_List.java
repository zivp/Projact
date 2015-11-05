package projact.finalprojact.projact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class Chat_Contact_List extends Activity {

    private boolean ParentORkid=true;//parent=true/kid=false
    private Singel_User_Array_Adapter singeluserAdapArry;
    private ListView list;
    private Button backTomenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat__contact__list);
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_Contact_List.this);
        if(prefernces.getString(getString(R.string.Parent_OR_kid),"").matches("p")){
            ParentORkid=true;
        }else{
            ParentORkid=false;
        }
        //connect list and adapter....
        list=(ListView)findViewById(R.id.chat_contect_list);
        singeluserAdapArry=new Singel_User_Array_Adapter(getApplication(), R.layout.singel_person);
        list.setAdapter(singeluserAdapArry);
        //open contact list withe the function-fullList.....
        fulllist();
    }
    //open contact list.....................
    private void fulllist() {
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_Contact_List.this);
        String userid="";
        if(ParentORkid) {
            // in case user is parent
            userid = prefernces.getString(getString(R.string.user_parse_id), "");
        }else{
            //in case user is kid
            userid = prefernces.getString(getString(R.string.parent_id), "");
        }
        ParseQuery<ParseObject> getthiskidid = ParseQuery.getQuery("Parent_Kid");
        getthiskidid.whereMatches("Parent_ID", userid);
        getthiskidid.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> object, ParseException e1) {
                if (e1 == null) {
                    for (int indx = 0; indx < object.size(); indx++) {
                        if (!object.get(indx).getString("Kid_ID").matches(prefernces.getString(getString(R.string.user_parse_id), ""))) {
                            final int count = indx;
                            ParseQuery<ParseObject> getcontactuser = ParseQuery.getQuery("USER");
                            getcontactuser.getInBackground(object.get(indx).getString("Kid_ID"), new GetCallback<ParseObject>() {
                                @Override
                                public void done(final ParseObject parseObject, ParseException e2) {
                                    if (e2 == null) {
                                        ParseFile fileObject = (ParseFile) parseObject.get("Image");
                                        fileObject.getDataInBackground(new GetDataCallback() {
                                            public void done(byte[] data, ParseException e2) {
                                                if (e2 == null) {
                                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    singeluserAdapArry.add(new Singel_User(true,
                                                            bmp,
                                                            parseObject.getString("F_name"),
                                                            parseObject.getObjectId()));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }else{
                            ParseQuery<ParseObject> getcontactuser = ParseQuery.getQuery("USER");
                            getcontactuser.getInBackground(object.get(indx).getString("Parent_ID"), new GetCallback<ParseObject>() {
                                @Override
                                public void done(final ParseObject parseObject, ParseException e2) {
                                    if (e2 == null) {
                                        ParseFile fileObject = (ParseFile) parseObject.get("Image");
                                        fileObject.getDataInBackground(new GetDataCallback() {
                                            public void done(byte[] data, ParseException e2) {
                                                if (e2 == null) {
                                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    singeluserAdapArry.add(new Singel_User(true,
                                                            bmp,
                                                            parseObject.getString("F_name"),
                                                            parseObject.getObjectId()));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //start adapter class............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public class Singel_User_Array_Adapter extends ArrayAdapter<Singel_User> {
        private TextView User_Name;
        private ImageView UserImage;
        private List<Singel_User> Contact_List = new ArrayList<Singel_User>();
        private LinearLayout layout;

        public Singel_User_Array_Adapter(Context context, int chat) {
            super(context, chat);
        }
        public void add(Singel_User object) {
            Contact_List.add(object);
            super.add(object);
        }
        public int getCount() {
            return this.Contact_List.size();
        }
        public Singel_User getItem(int index) {
            return this.Contact_List.get(index);
        }
        public View getView(int position, View ConvertView, ViewGroup perent) {
            View v = ConvertView;
            if (v == null) {
                LayoutInflater infleter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = infleter.inflate(R.layout.singel_person, perent, false);
            }
            layout = (LinearLayout) v.findViewById(R.id.user);
            final Singel_User userobj = getItem(position);
            User_Name = (TextView) v.findViewById(R.id.singel_user_name);
            UserImage=(ImageView)v.findViewById(R.id.SingleUserImage);
            UserImage.setImageBitmap(userobj.UserImage);
            User_Name.setText(userobj.UserName);
            layout.setGravity(Gravity.LEFT);//object side
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Chat_Contact_List.this,Chat_ConversationWindow.class);
                    Chat_ConversationWindow.FriendParseObjectID=userobj.UserParseID;
                    Chat_ConversationWindow.FriendImage=userobj.UserImage;
                    Chat_ConversationWindow.FriendFirstName=userobj.UserName;
                    startActivity(intent);
                    finish();
                }
            });
            return v;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //End adapter class..............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
}
