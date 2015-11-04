package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Schedule_List_AllEvent extends Fragment {

    static protected LinearLayout ll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View showing_events= inflater.inflate(R.layout.schedule_list_all_event, container, false);

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        Schedule_Activity.Current_day=weekDay;
        ((TextView) showing_events.findViewById(R.id.ListEvent_Today)).setText(weekDay);

        ScrollView sv=(ScrollView)showing_events.findViewById(R.id.scrolevent);
        ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

            final ParseQuery<ParseObject> EventTable = ParseQuery.getQuery("Event");
            EventTable.whereEqualTo("Kid_ID", Schedule_Activity.kidID);
            EventTable.whereEqualTo("Day",Schedule_Activity.Current_day);
            EventTable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> results, ParseException e) {
                    if (results.size() > 0) {
                        for (int index = 0; index < results.size(); index++) {
                            final int loc=index;
                            Button event = new Button(getActivity());
                            event.setText(results.get(index).getString("NameEvent") +
                                    "\nstart:" + results.get(index).getString("StartEvent") +
                                    "\nEnd:" + results.get(index).getString("EndEvent"));
                            /*if user touch on the event then the event screen are open with all the
                            event details*/
                            event.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //saving event parse id
                                    Schedule_Activity.EventID = results.get(loc).getObjectId();
                                    //open event details
                                    if (Schedule_Activity.schoosekid) {
                                        if (!Schedule_Activity.ShowEventDetails) {
                                            Schedule_Activity.NewEventOrOld = true;
                                        }
                                        Schedule_Activity.screennumber = 2;
                                        Fragment newfragment;
                                        newfragment = new Schedule_Event();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_tochange, newfragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                }
                            });
                            ll.addView(event);
                        }
                    }
                }
            });

        return showing_events;
    }
    static protected void CleenEventList(){
        ll.removeAllViews();
    }
}