package projact.finalprojact.projact;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;

public class myMainScreen extends Activity {

    EditText username;
    EditText password;
    public static final String APPLICATION_ID = "iqR5A9NLhvxuHH4t2Yk7yg4jEfLyA0KDHsT6dsUq";
    public static final String CLIENT_KEY = "zL75CMfUkUkb4PvHc7ROojcoANmxT6uPwcFQcI06";
    public static String DadId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //parse parse parse parse parse parse parse parse

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "iqR5A9NLhvxuHH4t2Yk7yg4jEfLyA0KDHsT6dsUq", "zL75CMfUkUkb4PvHc7ROojcoANmxT6uPwcFQcI06");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Automat login
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(myMainScreen.this);
        final String username=prefernces.getString("USER_NAME", "");
        final String userpass=prefernces.getString("USER_PASS", "");
        if(username.isEmpty()||userpass.isEmpty()){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            login signupFragment = new login();
            transaction.add(R.id.fragment_placeholder, signupFragment);
            transaction.commit();
        }
        else{
            ParseQuery<ParseObject> dadtable=ParseQuery.getQuery("Sighup");
            dadtable.whereEqualTo("UserName", username);
            dadtable.whereEqualTo("Password", userpass);
            dadtable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e==null){
                        if(parseObjects.size()>0) {
                            Fragment newfragment;
                            newfragment = new main_menu();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_placeholder, newfragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                    else{
                        Toast.makeText(getApplication(), "Connection problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
            ParseQuery<ParseObject> kidTable=ParseQuery.getQuery("Kids");
            kidTable.whereEqualTo("UserName", username);
            kidTable.whereEqualTo("Password", userpass);
            kidTable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e==null){
                        if(parseObjects.size()>0) {
                            Fragment newfragment;
                            newfragment = new main_menu();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_placeholder, newfragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                    else{
                        Toast.makeText(getApplication(), "Connection problem", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        //parse parse parse parse parse parse parse parse

    }
    public void onClick(View v) {

        Fragment newfragment;//creating acopy to hold the fragment
        if(v ==findViewById(R.id.sig_up_btn))
        {
            newfragment=new signup();
        }
        else
        {
            newfragment=new login();
        }
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //*function are lanche you to location screen from menu*//
    public void tolocation(View view)
    {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
    public void toSchedule(View view){
        Intent intent=new Intent(this,Schedule_Activity.class);
        startActivity(intent);
    }

}












