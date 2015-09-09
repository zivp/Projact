package projact.finalprojact.projact;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;

public class myMainScreen extends Activity {

    EditText username;
    EditText password;
    public static final String APPLICATION_ID = "iqR5A9NLhvxuHH4t2Yk7yg4jEfLyA0KDHsT6dsUq";
    public static final String CLIENT_KEY = "zL75CMfUkUkb4PvHc7ROojcoANmxT6uPwcFQcI06";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //parse parse parse parse parse parse parse parse

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "iqR5A9NLhvxuHH4t2Yk7yg4jEfLyA0KDHsT6dsUq", "zL75CMfUkUkb4PvHc7ROojcoANmxT6uPwcFQcI06");



        //parse parse parse parse parse parse parse parse




        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        login signupFragment = new login();
        transaction.add(R.id.fragment_placeholder, signupFragment);
        transaction.commit();

    }






    public void onClick(View v) {

        Fragment newfragment;//creating acopy to hold the layout fragment
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

}












