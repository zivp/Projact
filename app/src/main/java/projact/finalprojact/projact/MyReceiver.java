package projact.finalprojact.projact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Admin on 19/10/2015.
 */
public class MyReceiver extends BroadcastReceiver {
    private final String TAG = "Parse Notification";
    private String msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Push received!!!!.", Toast.LENGTH_LONG).show();
    }
}
