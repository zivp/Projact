package projact.finalprojact.projact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by Admin on 12/08/2015.
 */
public class signup extends Fragment  {



    Button Save;

    EditText Password;
    EditText ConfirmPasword;
    EditText PhonNamber;
    EditText UserName;
    EditText Email;
    EditText Address;
    EditText FirstName;
    EditText LastName;

    String userFirstName;
    String UserLastName;
    String UserAddress;
    String  UserEmail;
    String UserPhonNaber;
    String UserNAME;
    String UserPassword;
    String UserConfirmPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_signup, container, false);

      //casting EditText
        LastName=(EditText)myView.findViewById(R.id.last_name_signup);
        FirstName=(EditText)myView.findViewById(R.id.first_name_signup);
        Address=(EditText)myView.findViewById(R.id.address_signup);
        Email=(EditText)myView.findViewById(R.id.email_signup);
        PhonNamber=(EditText)myView.findViewById(R.id.phone_signup);
        UserName=(EditText)myView.findViewById(R.id.username_signup);
        Password=(EditText)myView.findViewById(R.id.password_signup);
        ConfirmPasword=(EditText)myView.findViewById(R.id.conf_password_signup);

       // OnClick on Save Button
        myView.findViewById(R.id.save_signup_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //&ver = EditText.ToString
                UserLastName = LastName.getText().toString();
                userFirstName = FirstName.getText().toString();
                UserAddress = Address.getText().toString();
                UserEmail = Email.getText().toString();
                UserPhonNaber = PhonNamber.getText().toString();
                UserNAME = UserName.getText().toString();
                UserPassword = Password.getText().toString();
                UserConfirmPassword = ConfirmPasword.getText().toString();

                  // tast if EditText Is Empty
                if (!(userFirstName.isEmpty() || UserLastName.isEmpty() || UserAddress.isEmpty() || UserEmail.isEmpty() || UserPhonNaber.isEmpty()
                        || UserNAME.isEmpty() || UserPassword.isEmpty() || UserConfirmPassword.isEmpty())) {

                     // input Parse Table --> "Sighup"
                    final ParseObject sighup = new ParseObject("Sighup");
                    sighup.put("FirstName", userFirstName);
                    sighup.put("LastName", UserLastName);
                    sighup.put("Address", UserAddress);
                    sighup.put("Email", UserEmail);
                    sighup.put("PhonNamber", UserPhonNaber);
                    sighup.put("UserName", UserNAME);
                    sighup.put("Password", UserPassword);
                    sighup.put("ConfirmPassword", UserConfirmPassword);

                    if (UserPassword.equals(UserConfirmPassword)) {
                        sighup.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if(e==null){
                                                            String DadId=sighup.getObjectId();
                                                          String  Id=DadId;
                                                        }
                                                        else
                                                            return;
                                                    }
                                                });
                                Toast.makeText(getActivity(), "Welcome " + userFirstName + " To KID-SAFE Family" + '\n' + "Your User: " + UserNAME + " Enjoy!!", Toast.LENGTH_SHORT).show();

                          // case that all input as in the table go page back
                        if (getFragmentManager().getBackStackEntryCount() > 0) {

                            getFragmentManager().popBackStack();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Your Password Is Not Math,Try Again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Complete All The Details", Toast.LENGTH_SHORT).show();
                }
            }


        });


        return myView;

    }


}





