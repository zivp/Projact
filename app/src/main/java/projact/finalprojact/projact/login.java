package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class login extends Fragment {

    Button Go;
    EditText ChackPassword;
    EditText ChackUserName;
    String UserStringPassword;
    String UserStringUserName;
    int FlagChack=0;

    String idDad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView= inflater.inflate(R.layout.fragment_login, container, false);
           ChackPassword=(EditText)LoginView.findViewById(R.id.ChackPassword);
           ChackUserName=(EditText)LoginView.findViewById(R.id.ChackUserName);
           LoginView.findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {
                   UserStringPassword=ChackPassword.getText().toString();
                   UserStringUserName=ChackUserName.getText().toString();
                   if(UserStringUserName.isEmpty()&&UserStringPassword.isEmpty())
                   {
                       Toast.makeText(getActivity(), "Please enter details", Toast.LENGTH_SHORT).show();
                   return;}
                   //check if the user is dad...................
                   final ParseQuery<ParseObject> Query=ParseQuery.getQuery("Sighup");
                   Query.whereEqualTo("UserName", UserStringUserName);
                   Query.whereEqualTo("Password", UserStringPassword);
                   Query.findInBackground( new FindCallback<ParseObject>() {
                       @Override
                       public void done(List<ParseObject> results, ParseException e) {
                           if (results.size()>0) {
                               //flag
                               FlagChack = 1;
                               for (int index = 0; index < results.size(); index++) {
                                   idDad = results.get(index).getObjectId();
                                   final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                   SharedPreferences.Editor editor = prefernces.edit();
                                   //saving user name
                                   editor.putString("USER_NAME", UserStringUserName).apply();
                                   //saving user password
                                   editor.putString("USER_PASS", UserStringPassword).apply();
                                   //saving dad kids ID
                                   editor.putString("KIDS_ID_PREF", results.get(index).getString("Kids_ID")).apply();
                                   String kidsid = prefernces.getString("KIDS_ID_PREF", "");
                                   //saving all the kids user name for chat contact lise
                                   if (!kidsid.isEmpty()) {
                                       String[] Singel_id = kidsid.split(",");
                                       for (int count = 0; count < Singel_id.length; count++) {
                                           ParseQuery<ParseObject> myquery = ParseQuery.getQuery("Kids");
                                           myquery.getInBackground(Singel_id[count], new GetCallback<ParseObject>() {
                                               @Override
                                               public void done(ParseObject parseObject, ParseException e2) {
                                                   if (e2 == null) {
                                                       //if the kid is exist on parse table we add am to the contact list preferences
                                                       final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                                       final SharedPreferences.Editor editor = prefernces.edit();
                                                       String old_contact_list = prefernces.getString("CONTACT_NAMES", "");
                                                       editor.putString("CONTACT_NAMES", old_contact_list + parseObject.getString("UserName") + ",").apply();
                                                   }
                                               }
                                           });
                                       }
                                   }
                                   Fragment newfragment;
                                   newfragment = new main_menu();
                                   FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                   transaction.replace(R.id.fragment_placeholder, newfragment);
                                   transaction.addToBackStack(null);
                                   transaction.commit();
                               }
                               final SharedPreferences prefernces1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                               SharedPreferences.Editor editor = prefernces1.edit();
                               editor.putString(getString(R.string.DadId), idDad);
                               editor.commit();
                           }
                           //Open Main_Menu Page
                           if (FlagChack == 1) {
                               Fragment newfragment;
                               newfragment = new main_menu();
                               FragmentTransaction transaction = getFragmentManager().beginTransaction();
                               transaction.replace(R.id.fragment_placeholder, newfragment);
                               transaction.addToBackStack(null);
                               transaction.commit();
                           }
                       }
                   });
                   //check if the user is kid.....
                   ParseQuery<ParseObject> KidTable=ParseQuery.getQuery("Kids");
                   KidTable.whereEqualTo("UserName", UserStringUserName);
                   KidTable.whereEqualTo("Password",UserStringPassword);
                   KidTable.findInBackground( new FindCallback<ParseObject>() {
                       @Override
                       public void done(List<ParseObject> parseObjects, ParseException e) {
                           if (parseObjects.size()>0) {
                               final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                               SharedPreferences.Editor editor = prefernces.edit();
                               //saving parse user details
                               editor.putString("USER_NAME",UserStringUserName).apply();
                               editor.putString("USER_PASS",UserStringPassword).apply();
                               editor.putString("MY_USER_ID",parseObjects.get(0).getObjectId()).apply();
                               //save dad id
                               String mydad_ID=parseObjects.get(0).getString("DadId");
                               editor.putString(getString(R.string.DadId),mydad_ID).apply();
                               //OPEN DAD TABLE TO GET ALL THE KIDS ID
                               ParseQuery<ParseObject> DadTable = ParseQuery.getQuery("Sighup");
                               DadTable.getInBackground(mydad_ID, new GetCallback<ParseObject>() {
                                   @Override
                                   public void done(ParseObject parseObject, ParseException e) {
                                       if (e == null) {
                                           final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                           //GETING ALL THE KIDS ID FROM THE DAD TABLE
                                           SharedPreferences.Editor editor = prefernces.edit();
                                           editor.putString("KIDS_ID_PREF",parseObject.getObjectId()+","+parseObject.getString("Kids_ID")).apply();
                                           editor.putString("CONTACT_NAMES",parseObject.getString("UserName")+",").apply();
                                           SharedPreferences prefernces2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                           String kidsid=prefernces2.getString("KIDS_ID_PREF","");
                                           SharedPreferences.Editor editor2 = prefernces.edit();
                                           editor2.putString("KIDS_ID_PREF","").apply();
                                           //saving all the kids user name for chat contact lise
                                           if(!kidsid.isEmpty()){
                                               String[] Singel_id = kidsid.split(",");
                                               for (int count = 0; count < Singel_id.length; count++) {
                                                   ParseQuery<ParseObject> myquery2 = ParseQuery.getQuery("Kids");
                                                   myquery2.getInBackground(Singel_id[count], new GetCallback<ParseObject>() {
                                                       @Override
                                                       public void done(ParseObject parseObject, ParseException e) {
                                                           if (e == null) {
                                                               //if the kid is exist on parse table we add am to the contact list preferences
                                                               SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                                               String myusername=prefernces.getString("USER_NAME","");
                                                               if(!parseObject.getString("UserName").matches(myusername)){
                                                                   //checking if its login user ID...
                                                                   SharedPreferences.Editor editor = prefernces.edit();
                                                                   String old_kidsid=prefernces.getString("KIDS_ID_PREF","");
                                                                   String old_contact_list=prefernces.getString("CONTACT_NAMES","");
                                                                   editor.putString("CONTACT_NAMES",old_contact_list+parseObject.getString("UserName")+",").apply();
                                                                   editor.putString("KIDS_ID_PREF",old_kidsid+parseObject.getObjectId()+",").apply();
                                                               }
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                           String old_kidsid=prefernces.getString("KIDS_ID_PREF","");
                                           String dad_id=prefernces.getString(getString(R.string.DadId),"");
                                           editor.putString("KIDS_ID_PREF",old_kidsid+dad_id+",").apply();
                                           Fragment newfragment;
                                           newfragment = new main_menu();
                                           FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                           transaction.replace(R.id.fragment_placeholder, newfragment);
                                           transaction.addToBackStack(null);
                                           transaction.commit();

                                       }
                                   }

                               });
                           }
                           else {
                               Toast.makeText(getActivity(), "One Or More Of your Incorrect Information", Toast.LENGTH_SHORT).show();
                               return;
                           }
                       }
                   });
               }


           });


        return LoginView;
    }





    //function to check if user is kid
    private void CaseUserIsKid(String login_username,String login_userpass){
        UserStringUserName=login_username;
        UserStringPassword=login_userpass;

    }

}