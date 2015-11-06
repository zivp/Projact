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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class login extends Fragment {

    Button Go;
    EditText Password;
    EditText UserName;
    String UserName_STR="";
    String UserPass_STR="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView= inflater.inflate(R.layout.fragment_login, container, false);

        UserName=(EditText)LoginView.findViewById(R.id.Login_User_Name);
        Password=(EditText)LoginView.findViewById(R.id.Login_User_Password);

        LoginView.findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName_STR=UserName.getText().toString();
                UserPass_STR=Password.getText().toString();
                Toast.makeText(getActivity(),UserName_STR+"--"+UserPass_STR,Toast.LENGTH_SHORT).show();
                if(!(UserName_STR.isEmpty()||UserPass_STR.isEmpty())) {
                    ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("USER");
                    GetMessageTable.whereMatches("User_Name", UserName_STR);
                    GetMessageTable.whereMatches("Password", UserPass_STR);
                    GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> object, ParseException e) {
                            if (e == null) {
                                for (int indx = 0; indx < object.size(); indx++) {
                                    Toast.makeText(getActivity(),"wellcom",Toast.LENGTH_SHORT).show();
                                    SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    final SharedPreferences.Editor editor = prefernces.edit();
                                    editor.putString(getString(R.string.user_name),
                                            object.get(indx).getString("User_Name")).apply();
                                    editor.putString(getString(R.string.user_password),
                                            object.get(indx).getString("Password")).apply();
                                    editor.putString(getString(R.string.user_parse_id),
                                            object.get(indx).getObjectId()).apply();
                                    editor.putString(getString(R.string.Parent_OR_kid),
                                            object.get(indx).getString("Parent_OR_kid")).apply();
                                    //check user status--------------------------------------------
                                    if (object.get(indx).getString("Parent_OR_kid").matches("p")) {
                                        //in case the user is parent
                                        Fragment newfragment;
                                        newfragment = new Menu_Parent();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_placeholder, newfragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    } else {

                                        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Parent_Kid");
                                        GetMessageTable.whereMatches("Kid_ID", prefernces.
                                                getString(getString(R.string.user_parse_id), ""));
                                        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> object, ParseException e) {
                                                if (e == null) {
                                                    for (int indx = 0; indx < object.size(); indx++) {
                                                        //save parent parse object id
                                                        editor.putString(getString(R.string.parent_id),
                                                                object.get(indx).getString("Parent_ID")).apply();
                                                    }
                                                }
                                            }
                                        });
                                        //in case user is kid
                                        Fragment newfragment;
                                        newfragment = new Menu_Kid();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_placeholder, newfragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                }
                            }else{
                                Toast.makeText(getActivity(),"user not exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return LoginView;
    }
}