package projact.finalprojact.projact;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Schedule_Activity extends ActionBarActivity {
    static protected int screennumber=1;
    static protected boolean NewEventOrOld=false;
    static protected boolean schoosekid=false;
    static protected boolean startorend=false;
    static protected boolean ShowEventDetails=false;
    static protected String Start_time="00:00";
    static protected String End_time="00:00";
    static protected String Current_day="";
    static protected String kidID="";
    static protected String EventID="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);


        final HorizontalScrollView sv=(HorizontalScrollView)findViewById(R.id.scrollView_kidsImage);
        final LinearLayout ll =new LinearLayout(this);//(LinearLayout)findViewById(R.id.linear);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        sv.addView(ll);
        //open user Image
        SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
        String UserNames = prefernces.getString("CONTACT_NAMES", "");
        String UsersId=prefernces.getString("KIDS_ID_PREF", "");
        if (UserNames.isEmpty()||UsersId.isEmpty()) {
            Toast.makeText(getApplication(), "add kid first", Toast.LENGTH_SHORT).show();
        } else
        {
            final String[] singleID=UsersId.split(",");
            //Set Default Kid
            kidID=singleID[0];
            final String[] SingleUserNam = UserNames.split(",");
            for (int indx = 0; indx < singleID.length; indx++) {

                ParseQuery<ParseObject> KidsTable = ParseQuery.getQuery("Kids");
                KidsTable.getInBackground(singleID[indx], new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e1) {
                        if (e1 == null) {
                            for(int indx2=0;indx2<SingleUserNam.length;indx2++) {
                                if (parseObject.getString("UserName").matches(SingleUserNam[indx2])) {
                                    ParseFile fileObject = (ParseFile) parseObject.get("Image");
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                ImageView kidImage=new ImageView(Schedule_Activity.this);
                                                kidImage.setImageBitmap(bmp);
                                                //set new height and width to user image
                                                int width = sv.getHeight(); // ((display.getWidth()*20)/100)
                                                int height = sv.getHeight();// ((display.getHeight()*30)/100)
                                                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
                                                kidImage.setLayoutParams(parms);

                                                kidImage.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        if (!NewEventOrOld) {
                                                            ((TextView) findViewById(R.id.kidNameListEvent)).setText(parseObject.getString("Name"));
                                                            kidID = parseObject.getObjectId();
                                                            schoosekid = true;
                                                            Schedule_List_AllEvent.CleenEventList();
                                                            setallevent();
                                                        }
                                                        return false;
                                                    }
                                                });
                                                ll.addView(kidImage);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
        ParseQuery<ParseObject> DadTable = ParseQuery.getQuery("Kids");
        DadTable.getInBackground(kidID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e2) {
                if (e2 == null) {
                    ((TextView) findViewById(R.id.kidNameListEvent)).setText(parseObject.getString("Name"));
                }
            }
        });
        Fragment newfragment;
        newfragment = new Schedule_List_AllEvent();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void NewEventScreen(View view){
        if(schoosekid) {
            if(!ShowEventDetails){
                NewEventOrOld = true;
            }
            screennumber = 2;
            Fragment newfragment;
            newfragment = new Schedule_Event();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_tochange, newfragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    public void ChooseDay(View view){
        if(schoosekid) {
            if(!NewEventOrOld) {
                Schedule_List_AllEvent.CleenEventList();

                String day = "";
                if (view == findViewById(R.id.Sunday_btn)) {
                    day = "Sunday";
                } else if (view == findViewById(R.id.Monday_btn)) {
                    day = "Monday";
                } else if (view == findViewById(R.id.Tuesday_btn)) {
                    day = "Tuesday";
                } else if (view == findViewById(R.id.Wednesday_btn)) {
                    day = "Wednesday";
                } else if (view == findViewById(R.id.Thursday_btn)) {
                    day = "Thursday";
                } else if (view == findViewById(R.id.Friday_btn)) {
                    day = "Friday";
                } else if (view == findViewById(R.id.Saturday_btn)) {
                    day = "Saturday";
                }
                Current_day = day;
                //Search events for selected day
                setallevent();
                if (screennumber == 1) {
                    ((TextView) findViewById(R.id.ListEvent_Today)).setText(Current_day);
                } else {
                    ((TextView) findViewById(R.id.eventday)).setText(Current_day);
                }
            }
        }
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
    public void SaveEvent(View view){
        if(ShowEventDetails){

        }
        NewEventOrOld=false;
        String nameevent=((TextView)findViewById(R.id.event_name)).getText().toString();
        String addressevent=((TextView)findViewById(R.id.event_address)).getText().toString();
        String startevent=((TextView)findViewById(R.id.event_start_txt)).getText().toString();
        String endevent=((TextView)findViewById(R.id.event__end_txt)).getText().toString();
        //Create new event
        ParseObject AddKidTable = new ParseObject("Event");
        AddKidTable.put("NameEvent",nameevent);
        AddKidTable.put("Address",addressevent);
        AddKidTable.put("StartEvent",startevent);
        AddKidTable.put("EndEvent",endevent);
        AddKidTable.put("Day",Current_day);
        AddKidTable.put("Kid_ID",kidID);
        AddKidTable.saveInBackground();

        Fragment newfragment;
        newfragment = new Schedule_List_AllEvent();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tochange, newfragment);
        transaction.addToBackStack(null);
        transaction.commit();
        Schedule_Activity.screennumber=1;
    }
    //This function displays events by choosing a boy or a different day
    public void setallevent(){
        final ParseQuery<ParseObject> EventTable = ParseQuery.getQuery("Event");
        EventTable.whereEqualTo("Kid_ID", kidID);
        EventTable.whereEqualTo("Day",Current_day);
        EventTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> results, ParseException e) {
                if (results.size() > 0) {
                    for (int indx = 0; indx < results.size(); indx++) {
                        final int loc=indx;
                        Button event = new Button(Schedule_Activity.this);
                        event.setText(results.get(indx).getString("NameEvent") +
                                "\nstart:" + results.get(indx).getString("StartEvent") +
                                "\nEnd:" + results.get(indx).getString("EndEvent"));
                        event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowEventDetails = true;
                                EventID = results.get(loc).getObjectId();
                                Toast.makeText(getApplication(), EventID, Toast.LENGTH_SHORT).show();
                                NewEventScreen(v);
                            }
                        });
                        Schedule_List_AllEvent.ll.addView(event);
                    }
                }
            }
        });
    }
}

