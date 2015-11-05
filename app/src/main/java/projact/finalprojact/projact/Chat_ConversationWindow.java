package projact.finalprojact.projact;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Chat_ConversationWindow extends Activity {
    private ChatArrayAdapter abp;
    private ListView list;
    private EditText chattext;
    private Button send;
    private Button back;


    static protected String FriendFirstName="";
    static protected String FriendParseObjectID="";
    static protected Bitmap FriendImage;

    private String userMessage;
    private String MY_ID;
    private static Thread mythread;
    private static boolean threadRunning;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_conversation_win);
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
        MY_ID=prefernces.getString(getString(R.string.user_parse_id),"");
        //set friend image
        ((ImageView)findViewById(R.id.ConversationUserImage)).setImageBitmap(FriendImage);
        //set friend first name
        ((TextView)findViewById(R.id.user_name_chat_win)).setText(FriendFirstName);
        //open old message
        GetingHistoryChat(FriendParseObjectID, MY_ID);
        //checking every 2.5 seconds new message
        checkingmessage(FriendParseObjectID, MY_ID);


        send = (Button) findViewById(R.id.btnsend);
        list = (ListView) findViewById(R.id.listmessage);
        abp = new ChatArrayAdapter(getApplicationContext(), R.layout.chat_single_message);
        chattext = (EditText) findViewById(R.id.chat);
        //*send message --------------------------------------------------------------
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    if (!chattext.getText().toString().isEmpty()) {
                        //update message on parse
                        userMessage = chattext.getText().toString();
                        send();
                    }
                }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(abp);
        abp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged() {
                super.onChanged();
                list.setSelection(abp.getCount() - 1);
            }
        });
        back=(Button)findViewById(R.id.cht2list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Chat_ConversationWindow.this,Chat_Contact_List.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //*sanding function ...
    private boolean sendChatMessage() {
        Time now = new Time();
        now.setToNow();
        String apptime= Integer.toString(now.hour)+":"+ Integer.toString(now.second);
        abp.add(new ChatMessage(side, chattext.getText().toString()+"\n"+apptime));
        chattext.setText("");
        //*side = !side;
        return true;
    }
    private void send(){
        Time now = new Time();
        now.setToNow();
        String apptime= Integer.toString(now.hour)+":"+ Integer.toString(now.second);
        sendChatMessage();
        ParseObject newmessage = new ParseObject("Message");
        newmessage.put("Sender",MY_ID);
        newmessage.put("Receiver",FriendParseObjectID);
        newmessage.put("txt",userMessage);
        newmessage.put("Read",false);
        newmessage.put("Time",apptime);
        newmessage.saveInBackground();
    }
    private void checkingmessage(final String friend,final String me){
        threadRunning=true;
        mythread = new Thread() {
            public void run() {
                while (threadRunning) {
                    try {
                        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
                        GetMessageTable.whereMatches("Sender",friend);
                        GetMessageTable.whereMatches("Receiver", me);
                        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> object, ParseException e) {
                                if(e==null){
                                    for (int indx = 0; indx < object.size(); indx++) {
                                        if(!object.get(indx).getBoolean("Read")){
                                            if(object.get(indx).getString("Sender").matches(friend))
                                            object.get(indx).put("Read",true);
                                            object.get(indx).saveInBackground();
                                            abp.add(new ChatMessage(!side,object.get(indx).getString("txt") +"\n"
                                                    +object.get(indx).getString("Time")));
                                        }
                                    }
                                }
                            }
                        });
                        Thread.sleep(2500);
                    }catch (InterruptedException ex) {
                    }
                }
            }
        };mythread.start();
    }
    //open history chat from parse
    private void GetingHistoryChat(final String friend,final String me){


        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if(e==null){
                    for (int indx = 0; indx < object.size(); indx++) {
                        //if its my message
                        if(object.get(indx).getString("Receiver").matches(friend)&&
                                object.get(indx).getString("Sender").matches(me)){
                            abp.add(new ChatMessage(side, object.get(indx).getString("txt")+"\n"+object.get(indx).getString("Time")));
                        }
                        else
                            if(object.get(indx).getString("Receiver").matches(me)&&
                                    object.get(indx).getString("Sender").matches(friend)) {
                                abp.add(new ChatMessage(!side,object.get(indx).getString("txt")+"\n"+object.get(indx).getString("Time")));
                            }
                    }
                }
            }
        });

    }
}
