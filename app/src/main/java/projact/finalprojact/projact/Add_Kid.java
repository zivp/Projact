package projact.finalprojact.projact;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;


public class Add_Kid extends Fragment {

    EditText AgeKid;
    EditText UserNameKid;
    EditText FirstNameKid;
    EditText LastNameKid;
    EditText PhonNaberKid;

    String KidAge;
    String KidUserName;
    String KidFirstName;
    String KidLastName;
    String KidPhonNamber;
  public TextView ObjactDadId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Kid=inflater.inflate(R.layout.fragment_add__kid, container, false);

        AgeKid=(EditText)Kid.findViewById(R.id.KidAgeEditText);
        UserNameKid=(EditText)Kid.findViewById(R.id.UserNameKidEditText);
        FirstNameKid=(EditText)Kid.findViewById(R.id.first_nameKidEditText);
        LastNameKid=(EditText)Kid.findViewById(R.id.last_nameKidEditText);
        PhonNaberKid=(EditText)Kid.findViewById(R.id.phoneKidEditText);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.DadId);
        final String highScore = sharedPref.getString(getString(R.string.DadId), defaultValue);
       // Toast.makeText(getActivity(),highScore , Toast.LENGTH_SHORT).show();

        Kid.findViewById(R.id.save_Kid_btn).setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {

                KidFirstName=FirstNameKid.getText().toString();
                KidLastName=LastNameKid.getText().toString();
                KidPhonNamber=PhonNaberKid.getText().toString();
                KidAge=AgeKid.getText().toString();
                KidUserName=UserNameKid.getText().toString();

                if(!(KidAge.isEmpty()||KidUserName.isEmpty()||KidPhonNamber.isEmpty()||KidLastName.isEmpty()||
                KidFirstName.isEmpty()))
                {
                    ParseObject AddKidTable = new ParseObject("Kids");
                    AddKidTable.put("Age",KidAge);
                    AddKidTable.put("Name",KidFirstName);
                    AddKidTable.put("UserName",KidUserName);
                    AddKidTable.put("LastName",KidLastName);
                    AddKidTable.put("PhonNamber",KidPhonNamber);
                   AddKidTable.put("DadId",highScore);

                    AddKidTable.saveInBackground();
                    Toast.makeText(getActivity(), "Kid : "+KidFirstName+"Writh", Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(getActivity(), "Mising Diatals", Toast.LENGTH_SHORT).show();
                }



            }
        });




        return Kid;

    }
}
