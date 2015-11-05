package projact.finalprojact.projact;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 05/11/2015.
 */
public class Menu_Kid extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View menu_Kid= inflater.inflate(R.layout.menu_kid, container, false);
        menu_Kid.findViewById(R.id.Menu_Kid_chat_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chat_Contact_List.class);
                startActivity(intent);
            }
        });
        return menu_Kid;
    }
}
