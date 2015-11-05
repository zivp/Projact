package projact.finalprojact.projact;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Chat_ConversationWindow extends Activity {
    private ChatArrayAdapter abp;
    private ListView list;
    private EditText chattext;
    private EditText usernametitle;
    private Button send;
    private Button back;


    private String userMessage;
    private String friend_id;
    private String MY_ID;
    private static Thread mythread;
    private static boolean threadRunning;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_conversation_win);
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=prefernces.edit();
        final String friendusername = prefernces.getString("CHATINGWITHE","");
        if(!prefernces.getString("MY_USER_ID","").isEmpty())
            MY_ID=prefernces.getString("MY_USER_ID","");
        else
            MY_ID=prefernces.getString(getString(R.string.DadId),"");
        //GET CHAT FRIEND ID FROM PARSE....
        String ConntactUserID=prefernces.getString("KIDS_ID_PREF","");
        String[] Singel_ID=ConntactUserID.split(",");
        for(int indx=0;indx<Singel_ID.length;indx++) {
            //IN CASE THAT FRIEND IS KID
            ParseQuery<ParseObject> KidsTable = ParseQuery.getQuery("Kids");
            KidsTable.getInBackground(Singel_ID[indx], new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e2) {
                    if (e2 == null) {
                        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_ConversationWindow.this);
                        final SharedPreferences.Editor editor = prefernces.edit();
                        if(parseObject.getString("Name").matches(friendusername)) {
                            editor.putString("CHAT_FRIEND_ID", parseObject.getObjectId()).apply();
                            GetingHistoryChat(parseObject.getObjectId(), MY_ID);
                            checkingmessage(parseObject.getObjectId(), MY_ID);
                            ((TextView)findViewById(R.id.user_name_chat_win)).setText(parseObject.getString("Name"));
                            final ParseFile fileObject = (ParseFile) parseObject.get("Image");
                            fileObject.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e2) {
                                    if (e2 == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        // Close progress dialog
                                        ((ImageView)findViewById(R.id.ConversationUserImage)).setImageBitmap(bmp);
                                    }
                                }
                            });
                        }
                    }
                }
            });
            //IN CASE ITS DAD TABLE
            ParseQuery<ParseObject> DadTable = ParseQuery.getQuery("Sighup");
            DadTable.getInBackground(Singel_ID[indx], new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e2) {
                    if (e2 == null) {
                        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_ConversationWindow.this);
                        final SharedPreferences.Editor editor = prefernces.edit();
                        if (parseObject.getString("FirstName").matches(friendusername)) {
                            editor.putString("CHAT_FRIEND_ID", parseObject.getObjectId()).apply();
                            GetingHistoryChat(parseObject.getObjectId(), MY_ID);
                            checkingmessage(parseObject.getObjectId(), MY_ID);
                            ((TextView)findViewById(R.id.user_name_chat_win)).setText(parseObject.getString("FirstName"));
                            final ParseFile fileObject = (ParseFile) parseObject.get("Image");
                            fileObject.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e2) {
                                    if (e2 == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        // Close progress dialog
                                        ((ImageView)findViewById(R.id.ConversationUserImage)).setImageBitmap(bmp);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        final String Friend_ID=prefernces.getString("CHAT_FRIEND_ID","");
        //OPEN HISTORY CHAT WITHE USER X

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
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(Chat_ConversationWindow.this);
        final String Friend_ID=prefernces.getString("CHAT_FRIEND_ID","");
        Time now = new Time();
        now.setToNow();
        String apptime= Integer.toString(now.hour)+":"+ Integer.toString(now.second);
        sendChatMessage();
        ParseObject newmessage = new ParseObject("Message");
        newmessage.put("Sender",MY_ID);
        newmessage.put("Receiver",Friend_ID);
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
