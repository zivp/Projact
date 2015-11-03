package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Schedule_Activity extends ActionBarActivity {
    private int screennumber=1;
    static protected boolean startorend=false;
    static protected String Start_time="00:00";
    static protected String End_time="00:00";
    static protected String Current_day="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        Fragment newfragment;
        newfragment = new Schedule_List_AllEvent();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();



        HorizontalScrollView sv=(HorizontalScrollView)findViewById(R.id.scrollView_kidsImage);
        LinearLayout ll =new LinearLayout(this);//(LinearLayout)findViewById(R.id.linear);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        sv.addView(ll);
        for(int i = 0; i < 10; i++) {
            ImageView kidImage=new ImageView(this);
            kidImage.setImageResource(R.drawable.shado);
            ll.addView(kidImage);
        }
    }
    public void NewEventScreen(View view){
        screennumber=2;
        Fragment newfragment;
        newfragment = new Schedule_Event();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void ChooseDay(View view){
        String day="";
        if(view==findViewById(R.id.Sunday_btn)){
            day="Sunday";
        }else if(view==findViewById(R.id.Monday_btn)){
            day="Monday";
        }else if(view==findViewById(R.id.Tuesday_btn)){
            day="Tuesday";
        }else if(view==findViewById(R.id.Wednesday_btn)){
            day = "Wednesday";
        }else if(view==findViewById(R.id.Thursday_btn)){
            day = "Thursday";
        }else if(view==findViewById(R.id.Friday_btn)){
            day = "Friday";
        }
        else if(view==findViewById(R.id.Saturday_btn)){
            day = "Saturday";
        }
        Current_day=day;
        if(screennumber==1){
            ((TextView)findViewById(R.id.ListEvent_Today)).setText(Current_day);
        }else{
            ((TextView)findViewById(R.id.eventday)).setText(Current_day);
        }
    }
    public void SaveNewEvent(View view){
        /*String nameevent=((EditText)findViewById(R.id.event_name)).getText().toString();
        String addressevent=((EditText)findViewById(R.id.event_address)).getText().toString();
        String startevent=((EditText)findViewById(R.id.event_start_txt)).getText().toString();
        String endevent=((EditText)findViewById(R.id.event__end_txt)).getText().toString();
        ParseObject AddKidTable = new ParseObject("Event");
        AddKidTable.put("NameEvent",nameevent);
        AddKidTable.put("Address",addressevent);
        AddKidTable.put("StartEvent",startevent);
        AddKidTable.put("EndEvent",endevent);
        AddKidTable.saveInBackground();*/
        Fragment newfragment;
        newfragment = new Schedule_List_AllEvent();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
        screennumber=1;
    }
    public void chooseTime(View view){
        if(view==findViewById(R.id.start_time_btn)){
            startorend=true;
        }else
            startorend=false;
        Fragment newfragment;
        newfragment = new MyTimePicker();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void savetime(View view){

        String Hours=((TimePicker)findViewById(R.id.mytime)).getCurrentHour().toString();
        String Minute=((TimePicker)findViewById(R.id.mytime)).getCurrentMinute().toString();
        Toast.makeText(getApplication(), Hours + ":" + Minute, Toast.LENGTH_SHORT).show();
        Fragment newfragment;
        newfragment = new Schedule_Event();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
        if(startorend){
            Start_time=Hours+":"+Minute;
        }else
            End_time=Hours+":"+Minute;
    }
}
