package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Schedule_List_AllEvent extends Fragment {


    private Singel_Event SingleEventAdepter;
    private ListView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View showing_events= inflater.inflate(R.layout.schedule_list_all_event, container, false);

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        Schedule_Activity.Current_day=weekDay;
        ((TextView) showing_events.findViewById(R.id.ListEvent_Today)).setText(weekDay);
        list=(ListView)showing_events.findViewById(R.id.Events_ListView);
        SingleEventAdepter=new Singel_Event(getActivity(), R.layout.singel_person);
        list.setAdapter(SingleEventAdepter);
        for (int count = 0; count < 4; count++) {
                SingleEventAdepter.add(new Singel_User(true, "event"));
        }
        return showing_events;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //start adapter class............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public class Singel_Event extends ArrayAdapter<Singel_User> {
        private TextView User_Name;
        private List<Singel_User> Contact_List = new ArrayList<Singel_User>();
        private LinearLayout layout;

        public Singel_Event(Context context, int chat) {
            super(context, chat);
        }
        public void add(Singel_User object) {
            Contact_List.add(object);
            super.add(object);
        }


        public int getCount() {
            return this.Contact_List.size();
        }

        public Singel_User getItem(int index) {
            return this.Contact_List.get(index);
        }

        public View getView(int position, View ConvertView, ViewGroup perent) {
            View v = ConvertView;
            if (v == null) {
                LayoutInflater infleter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = infleter.inflate(R.layout.singel_person, perent, false);
            }
            layout = (LinearLayout) v.findViewById(R.id.user);
            Singel_User userobj = getItem(position);
            User_Name = (TextView) v.findViewById(R.id.singel_user_name);

            User_Name.setText(userobj.UserName);

            layout.setGravity(Gravity.LEFT);//object side
            onclick(v,position,User_Name.getText().toString());
            return v;
        }
        public void onclick(View view, final int position,final String s){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = prefernces.edit();
                    editor.putString("EVENT_NAME", s).apply();
                    Fragment newfragment;
                    newfragment = new Schedule_Event();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_tochange, newfragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //End adapter class..............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

}