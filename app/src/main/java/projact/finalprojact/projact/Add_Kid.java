package projact.finalprojact.projact;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class Add_Kid extends Fragment {

    private EditText AgeKid;
    private EditText UserNameKid;
    private EditText FirstNameKid;
    private EditText LastNameKid;
    private EditText PhonNaberKid;
    private EditText Pass;
    private EditText ConfirmPass;

    static protected String KidAge;
    static protected String KidUserName;
    static protected String KidFirstName;
    static protected String KidLastName;
    static protected String KidPhonNamber;
    static protected String KidPass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Kid=inflater.inflate(R.layout.add__kid, container, false);

        FirstNameKid=(EditText)Kid.findViewById(R.id.first_nameKidEditText);
        LastNameKid=(EditText)Kid.findViewById(R.id.last_nameKidEditText);
        PhonNaberKid=(EditText)Kid.findViewById(R.id.phoneKidEditText);
        AgeKid=(EditText)Kid.findViewById(R.id.KidAgeEditText);
        UserNameKid=(EditText)Kid.findViewById(R.id.UserNameKidEditText);
        Pass=(EditText)Kid.findViewById(R.id.kid_user_pass);
        ConfirmPass=(EditText)Kid.findViewById(R.id.kid_user_Confirm_pass);

        final SharedPreferences prefernces1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defaultValue = getResources().getString(R.string.DadId);
        final String DadIdFromPref = prefernces1.getString(getString(R.string.DadId), defaultValue);

        //Saving al the kid details on parse kids Table..................
        Kid.findViewById(R.id.save_Kid_btn).setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                //checking if the pass is matches and the user is not robot or Drunk:)
                if(Pass.getText().toString().matches(ConfirmPass.getText().toString())){
                    //put all the user kid details value in string....

                    KidFirstName=FirstNameKid.getText().toString();
                    KidLastName=LastNameKid.getText().toString();
                    KidPhonNamber=PhonNaberKid.getText().toString();
                    KidAge=AgeKid.getText().toString();
                    KidUserName=UserNameKid.getText().toString();
                    KidPass=Pass.getText().toString();
                    //checking if we forget something
                    if(!(KidAge.isEmpty()||KidUserName.isEmpty()||KidPhonNamber.isEmpty()||KidLastName.isEmpty()||KidFirstName.isEmpty()||KidPass.isEmpty()))
                    {
                        Bitmap bitmap = BitmapFactory.decodeFile(myMainScreen.imgDecodableString);//BitmapFactory.decodeResource(getResources(),R.drawable.shado);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        ParseFile file = new ParseFile("androidbegin.png", image);
                        file.saveInBackground();
                        //update the parse kids table....
                        ParseObject AddKidTable = new ParseObject("Kids");
                        AddKidTable.put("Age",KidAge);
                        AddKidTable.put("Name",KidFirstName);
                        AddKidTable.put("UserName",KidUserName);
                        AddKidTable.put("LastName",KidLastName);
                        AddKidTable.put("PhonNamber",KidPhonNamber);
                        AddKidTable.put("DadId",DadIdFromPref);
                        AddKidTable.put("Password",KidPass);
                        AddKidTable.put("Image",file);
                        AddKidTable.saveInBackground();

                        Toast.makeText(getActivity(), "Kid : " + KidFirstName + " Writh", Toast.LENGTH_SHORT).show();
                        //saving kid PARSE ID in preferences to reuse....

                        putallkidIDindadtable();

                        }
                    else{
                        Toast.makeText(getActivity(), "Mising Diatals", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "The passowrd are not matches", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Kid.findViewById(R.id.BackAddkidToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newfragment;
                newfragment = new main_menu();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_placeholder, newfragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        putallkidIDindadtable();
        return Kid;
    }
    //function that pop the kid Object ID from parse and save the ID in preferenc..
    //the function are saving also the user name in preferenc......................
    private void getkidID(){
        Toast.makeText(getActivity(),KidUserName+KidPass,Toast.LENGTH_SHORT).show();
        ParseQuery<ParseObject> Query=ParseQuery.getQuery("Kids");
        Query.whereEqualTo("UserName", KidUserName);
        Query.whereEqualTo("Password",KidPass);
        Query.findInBackground( new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e2) {
                if (e2==null) {
                    String id = parseObjects.get(0).getObjectId();//geting parse objectID
                    String USERNAME = parseObjects.get(0).getString("UserName");//get kid user name
                    Toast.makeText(getActivity(), USERNAME + "--" + id, Toast.LENGTH_SHORT).show();
                    //------------------------------------------------------------||
                    final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String old_contact_list = prefernces.getString("KIDS_ID_PREF", "");
                    String old_contactnames = prefernces.getString("CONTACT_NAMES", "");
                    SharedPreferences.Editor editor = prefernces.edit();
                    //open dad parse Table
                    editor.putString("KIDS_ID_PREF", old_contact_list + id + ",");
                    editor.putString("CONTACT_NAMES", old_contactnames + USERNAME+ "," );
                    editor.commit();
                    putallkidIDindadtable();
                }
            }
        });
    }
    //function are save get the kids id from the preferences and put it in parse dad table
    private void putallkidIDindadtable(){
        getkidID();
        SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String dad_id=prefernces.getString(getString(R.string.DadId),"");
        ParseQuery<ParseObject> myquery = ParseQuery.getQuery("Sighup");
        myquery.getInBackground(dad_id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    //get all.... the kid objectID from preferences
                    final String old_contact_list=prefernces.getString("KIDS_ID_PREF","");
                    Toast.makeText(getActivity(), old_contact_list + " all the kids id", Toast.LENGTH_SHORT).show();
                    parseObject.put("Kids_ID", old_contact_list);
                    parseObject.saveInBackground();
                }
            }
        });
    }
    public void SaveNewKid(View view){

    }
}
