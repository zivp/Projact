package projact.finalprojact.projact;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class Add_Kid extends Fragment {

    private static Thread mythread;
    private static boolean threadRunning;

    private EditText First_Name;
    private EditText Last_Name;
    private EditText User_Name;
    private EditText Password;
    private EditText Confirm_Password;
    private EditText Phone_NUmber;
    private EditText Address;
    private EditText Email;
    private EditText Birthday;

    static protected String First_Name_STR="";
    static protected String Last_Name_STR;
    static protected  String User_Name_STR;
    static protected  String Password_STR;
    static protected  String Confirm_Password_STR;
    static protected  String Phone_NUmber_STR;
    static protected  String Address_STR;
    static protected  String Email_STR;
    static protected  String Birthday_STR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View Kid=inflater.inflate(R.layout.add__kid, container, false);
        //Saving al the kid details on parse kids Table..................
        Kid.findViewById(R.id.save_Kid_btn).setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                First_Name=(EditText)Kid.findViewById(R.id.Add_Kid_F_Name);
                Last_Name=(EditText)Kid.findViewById(R.id.Add_Kid_L_Name);
                User_Name=(EditText)Kid.findViewById(R.id.Add_Kid_User_Name);
                Password=(EditText)Kid.findViewById(R.id.Add_Kid_Password);
                Confirm_Password=(EditText)Kid.findViewById(R.id.Add_Kid_Confirm_Password);
                Phone_NUmber=(EditText)Kid.findViewById(R.id.Add_Kid_Phone_Number);
                Address=(EditText)Kid.findViewById(R.id.Add_Kid_Address);
                Email=(EditText)Kid.findViewById(R.id.Add_Kid_Email);
                Birthday=(EditText)Kid.findViewById(R.id.Add_Kid_Birthday);
                //put all the user kid details value in string....
                First_Name_STR = First_Name.getText().toString();
                Last_Name_STR = Last_Name.getText().toString();
                User_Name_STR = User_Name.getText().toString();
                Password_STR = Password.getText().toString();
                Confirm_Password_STR = Confirm_Password.getText().toString();
                Phone_NUmber_STR = Phone_NUmber.getText().toString();
                Address_STR = Address.getText().toString();
                Email_STR = Email.getText().toString();
                Birthday_STR = Birthday.getText().toString();
                //checking password
                if (Password_STR.matches(Confirm_Password_STR)) {
                    //checking all the text box
                    if (!(First_Name_STR.isEmpty() || Last_Name_STR.isEmpty() || User_Name_STR.isEmpty()
                            || Password_STR.isEmpty() || Phone_NUmber_STR.isEmpty() || Address_STR.isEmpty()
                            ||Email_STR.isEmpty()||Birthday_STR.isEmpty())) {
                        Bitmap bitmap = BitmapFactory.decodeFile(Menu_Main_Activity.imgDecodableString);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        ParseFile file = new ParseFile("androidbegin.png", image);
                        file.saveInBackground();
                        //create new kid rew in parse user table
                        ParseObject addkid = new ParseObject("USER");
                        //set all details
                        addkid.put("F_name", First_Name_STR);
                        addkid.put("L_name", Last_Name_STR);
                        addkid.put("User_Name", User_Name_STR);
                        addkid.put("Password", Password_STR);
                        addkid.put("Address", Address_STR);
                        addkid.put("Phone_Number", Phone_NUmber_STR);
                        addkid.put("Image", file);
                        addkid.put("Email", Email_STR);
                        addkid.put("Birthday", Birthday_STR);
                        addkid.put("Parent_OR_kid", "k");
                        addkid.saveInBackground();
                        //
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("successful update");
                        alertDialog.setMessage("Kid user name is:" + User_Name_STR +
                                "\nKid password:" + Password_STR);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //geting this kid parse object id to create a relationship between parent and kid
                                checkingupdateukid();
                            }
                        });alertDialog.show();
                        //clear all the text box and image
                        First_Name.setText("");
                        Last_Name.setText("");
                        User_Name.setText("");
                        Password.setText("");
                        Confirm_Password.setText("");
                        Phone_NUmber.setText("");
                        Address.setText("");
                        Email.setText("");
                        Birthday.setText("");
                        ((ImageView)Kid.findViewById(R.id.kid_Image)).clearColorFilter();
                    } else {
                        Toast.makeText(getActivity(), "Mising Diatals", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "The passowrd are not matches", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Kid.findViewById(R.id.BackAddkidToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newfragment;
                newfragment = new Menu_Parent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_placeholder, newfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return Kid;
    }
    public void checkingupdateukid(){

        threadRunning=true;
        mythread = new Thread() {
            public void run() {
                while (threadRunning){
                    try {
                        ParseQuery<ParseObject> getthiskidid = ParseQuery.getQuery("USER");
                        getthiskidid.whereMatches("User_Name", User_Name_STR);
                        getthiskidid.whereMatches("Password", Password_STR);
                        getthiskidid.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> object, ParseException e) {
                                if (e == null) {
                                    for (int indx = 0; indx < object.size(); indx++) {
                                        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        final String ParentID = prefernces.getString(getString(R.string.user_parse_id), "");
                                        //create relationship between parent and kid
                                        ParseObject conaction = new ParseObject("Parent_Kid");
                                        conaction.put("Parent_ID", ParentID);
                                        conaction.put("Kid_ID", object.get(indx).getObjectId());
                                        conaction.saveInBackground();
                                        threadRunning=false ;
                                    }
                                }
                            }
                        });
                        Thread.sleep(2500);
                    }catch (InterruptedException ex) {
                    }
                }
            }
        };mythread.start();
    }
}
