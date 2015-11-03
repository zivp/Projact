package projact.finalprojact.projact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class Chat_Contact_List extends ActionBarActivity {

    private Singel_User_Array_Adapter singeluserAdapArry;
    private ListView list;
    private Button backTomenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat__contact__list);
        //connect list and adapter....
        list=(ListView)findViewById(R.id.chat_contect_list);
        singeluserAdapArry=new Singel_User_Array_Adapter(getApplication(), R.layout.singel_person);
        list.setAdapter(singeluserAdapArry);
        //open contact list withe the function-fullList.....
        fulllist();

    }
    //open contact list.....................
    private void fulllist() {
        SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
        String UserNames = prefernces.getString("CONTACT_NAMES", "");
        String UsersId=prefernces.getString("KIDS_ID_PREF", "");
        if (UserNames.isEmpty()||UsersId.isEmpty()) {
            Toast.makeText(getApplication(), "add kid first", Toast.LENGTH_SHORT).show();
        } else
        {
            String[] singleID=UsersId.split(",");
            final String[] SingleUserNam = UserNames.split(",");
            for (int indx = 0; indx < singleID.length; indx++) {

                ParseQuery<ParseObject> KidsTable = ParseQuery.getQuery("Kids");
                KidsTable.getInBackground(singleID[indx], new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e1) {
                        if (e1 == null) {
                            for(int indx2=0;indx2<SingleUserNam.length;indx2++) {
                                if (parseObject.getString("UserName").matches(SingleUserNam[indx2])) {
                                    ParseFile fileObject = (ParseFile) parseObject.get("Image");
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                // Close progress dialog
                                                singeluserAdapArry.add(new Singel_User(true, bmp, parseObject.getString("Name")));
                                            }
                                            else
                                                singeluserAdapArry.add(new Singel_User(true, parseObject.getString("Name")));
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
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
            Singel_User userobj = getItem(position);
            User_Name = (TextView) v.findViewById(R.id.singel_user_name);
            UserImage=(ImageView)v.findViewById(R.id.SingleUserImage);
            UserImage.setImageBitmap(userobj.UserImage);
            User_Name.setText(userobj.UserName);
            layout.setGravity(Gravity.LEFT);//object side
            onclick(v,position,User_Name.getText().toString());
            return v;
        }
        public void onclick(View view, final int position,final String s){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Chat_Contact_List.this,Chat_ConversationWindow.class);
                    final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_Contact_List.this);
                    SharedPreferences.Editor editor = prefernces.edit();
                    editor.putString("CHATINGWITHE", s).apply();
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //End adapter class..............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
}
