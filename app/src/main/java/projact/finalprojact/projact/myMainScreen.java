package projact.finalprojact.projact;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class myMainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm= getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        login signupFragment=new login();
        transaction.add(R.id.fragment_placeholder,signupFragment);
        transaction.commit();

    }
    public void onClick(View v) {

        Fragment newfragment;//creating acopy to hold the layout fragment
          if(v ==findViewById(R.id.sig_up_btn))
          {
              newfragment=new signup();
          }
          else if(v==findViewById(R.id.go_btn))
          {
              newfragment=new main_menu();
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
