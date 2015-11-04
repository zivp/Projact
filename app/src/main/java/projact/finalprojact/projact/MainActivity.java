package projact.finalprojact.projact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {

    //this value is for know if the user is dad or kid ...if its kid the value-
    ProgressBar progressBar;
    int progressStatus = 0;
    Handler handler = new Handler();
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach);
        textView2 = (TextView) findViewById(R.id.textLowding);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100)
                {
                    progressStatus += 4;
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setProgress(progressStatus);
                          textView2.setText(progressStatus + "%");
                        }
                    });
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (progressStatus==100)
                {



                    Intent i = new Intent(MainActivity.this, myMainScreen.class);
                    startActivity(i);
                }
            }
        }).start();
    }}