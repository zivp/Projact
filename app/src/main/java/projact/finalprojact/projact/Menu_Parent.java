package projact.finalprojact.projact;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class Menu_Parent extends Fragment {
    //btns......
    private Button AddKid;
    private Button chat_btn;
    private Button dteails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Main= inflater.inflate(R.layout.menu_parent, container, false);


        Main.findViewById(R.id.btnTOaddkid).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment newfragment;
                newfragment = new Add_Kid();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_placeholder, newfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Main.findViewById(R.id.btnTOchat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chat_Contact_List.class);
                startActivity(intent);
            }
        });
        //function and button to testing and qa ...........
        Main.findViewById(R.id.details_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());//*open referens in this app
                String mycontactlist = prefernces.getString("CONTACT_NAMES", "");
                String myusername=prefernces.getString("USER_NAME", "");
                String myuserpass=prefernces.getString("USER_PASS", "");
                String myuserID=prefernces.getString("MY_USER_ID", "");
                String kidsid=prefernces.getString("KIDS_ID_PREF","");
                Toast.makeText(getActivity(), mycontactlist + "\nuser name:" + myusername + "\nuser pass:" + myuserpass + "\nuser ID:" + myuserID + "\nkids id:" + kidsid, Toast.LENGTH_LONG).show();
            }
        });
        Main.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Welcome to AndroidHive.info");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getActivity(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
                SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor=prefernces.edit();
                String[] contact_history=prefernces.getString("KIDS_ID_PREF","").split(",");
                for(int indx=0;indx<contact_history.length;indx++)
                    editor.putString(contact_history[indx],"").apply();
                editor.putString("CONTACT_NAMES", "").apply();
                editor.putString("USER_NAME", "").apply();
                editor.putString("USER_PASS", "").apply();
                editor.putString("KIDS_ID_PREF", "").apply();
                editor.putString("MY_USER_ID", "").apply();
                editor.putString("CHATINGWITHE", "").apply();


                editor.putString("FRIANDE_CHAT", "").apply();
                editor.putString(getString(R.string.DadId), "").apply();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                login signupFragment = new login();
                transaction.add(R.id.fragment_placeholder, signupFragment);
                transaction.commit();
            }
        });

            return Main;
    }
}