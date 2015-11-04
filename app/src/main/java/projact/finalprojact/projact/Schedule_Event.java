package projact.finalprojact.projact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Schedule_Event extends Fragment {

    TextView day;
    TextView Set_Start_Time;
    TextView Set_End_Time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View event= inflater.inflate(R.layout.schedule_event, container, false);
        day=(TextView)event.findViewById(R.id.eventday);
        Set_Start_Time=(TextView)event.findViewById(R.id.event_start_txt);
        Set_End_Time=(TextView)event.findViewById(R.id.event__end_txt);
        Set_Start_Time.setText(Schedule_Activity.Start_time);
        Set_End_Time.setText(Schedule_Activity.End_time);
        day.setText(Schedule_Activity.Current_day);

        return event;
    }

}