package projact.finalprojact.projact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Schedule_List_AllEvent extends Fragment {



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

        ScrollView sv=(ScrollView)showing_events.findViewById(R.id.scrolevent);
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        for(int i = 0; i < 10; i++) {
            TextView event=new TextView(getActivity());
            event.setText("event");
            event.setTextSize(30);
            ll.addView(event);
        }
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return showing_events;
    }

}