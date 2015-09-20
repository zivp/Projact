package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
                   {Toast.makeText(getActivity(), "Please Fill In The Details", Toast.LENGTH_SHORT).show();
                   return;}

                  //pop from Table
                   final ParseQuery<ParseObject> Query=ParseQuery.getQuery("Sighup");

                   Query.whereEqualTo("UserName", UserStringUserName);
                   Query.whereEqualTo("Password", UserStringPassword);


                   Query.findInBackground( new FindCallback<ParseObject>() {
                       public void done(List<ParseObject> results, ParseException e) {
                           if (results.size()>0) {
                               //flag
                               FlagChack = 1;

                               for(int index=0;index <results.size();index++)
                               {
                             idDad= results.get(index).getObjectId();

                               }
                               SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPref.edit();
                               editor.putString(getString(R.string.DadId),idDad );
                               editor.commit();

                              // Toast.makeText(getActivity(), idDad, Toast.LENGTH_SHORT).show();

                           } else {
                               Toast.makeText(getActivity(), "Eror !!!"+'\n'+ "One Or More Of your Incorrect Information", Toast.LENGTH_SHORT).show();
                               return;

                           }

                           //Open Main_Menu Page
                           if (FlagChack == 1) {
                               Fragment newfragment;
                               newfragment = new main_menu();
                               FragmentTransaction transaction = getFragmentManager().beginTransaction();
                               transaction.replace(R.id.fragment_placeholder, newfragment);
                               transaction.addToBackStack(null);
                               transaction.commit();
                           } else {
                               Toast.makeText(getActivity(), "One Or More Of your Incorrect Information", Toast.LENGTH_SHORT).show();
                               return;
                           }


                       }


                   });


               }


           });


        return LoginView;
    }

public String SetString(String idDad)
{
    String x =idDad;
    return x;

}
}