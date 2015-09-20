package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class main_menu  extends Fragment {
    Button AddKid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Main= inflater.inflate(R.layout.fragment_main_menu, container, false);


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


        return Main;
    }
}