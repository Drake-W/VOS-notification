package com.example.drawo.afinal;

import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    //private TextView lastUpdate;

    //broadcast receiving updates the status every hour at :00
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    Button click = (Button) findViewById(R.id.startTask);
                    Calendar c =Calendar.getInstance();
                    int minutes = c.get(Calendar.MINUTE);
                    if (minutes == 0) {
                        click.performClick();
                    }
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public void onSendNotificationsButtonClick(View view) {
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }

    public void stopSendNotificationsButtonClick(View view) {
        NotificationEventReceiver.cancelAlarm(getApplicationContext());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView lastUpdate;
        lastUpdate = (TextView) findViewById(R.id.lastUpdate);
        Button click = (Button) findViewById(R.id.startTask);
        click.performClick(); // updates the UI on first open
    }


    //This function is called when update button is clicked.
    public void startTask(View view) {
        myAsyncTask mTask = new myAsyncTask();
        mTask.execute("abc", "10", "Hello world");
    }

    //This class accesses twitter data and updates the UI
    public class myAsyncTask extends AsyncTask<String, Integer, Void> {
        String mTAG = "myAsyncTask";
        String tweet = "";
        String clan1 = "error";
        String clan2 = "error";

        @Override
        protected void onPreExecute() {
            Log.d(mTAG, "Hello from onPreExecute");
        }

        @Override
        protected Void doInBackground(String... arg) {
            //Implements the twitter4j library to access twitter
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("aYmPlB4SGcGU82zvU7P2LcQRa")
                    .setOAuthConsumerSecret("YG1fCvfsvxJanuut6KoeEqxSeq8FUSCFOtvU80q56I8jKuniS6")
                    .setOAuthAccessToken("145005836-Pu7jhyuYKLTtUoSVgkii4nl7hjvDHJRAYfK4Ul5j")
                    .setOAuthAccessTokenSecret("qStJkrFYJqB6B9VBmFOVB6xYYHyvC8iAw7tkNyynPa2Xh");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            String search = "voice"; //search keyword

            boolean first = true;
            String[] clans = new String[8]; //secondary keywords
            clans[0] = "Amlodd";
            clans[1] = "Cadarn";
            clans[2] = "Crwys";
            clans[3] = "Hefin";
            clans[4] = "Iorwerth";
            clans[5] = "Ithell";
            clans[6] = "Meilyr";
            clans[7] = "Trahaearn";

            try {
                List<twitter4j.Status> statuses;

                String user = "JagexClock";
                statuses = twitter.getUserTimeline(user); //creates a list of recent updates from
                for (twitter4j.Status status : statuses) { //the specified user
                    tweet = status.getText();
                    //finds the first tweet which contains the keyword
                    if (tweet.toLowerCase().contains(search.toLowerCase())) {
                        break;
                    }
                }
                //searches for the 2 secondary keywords and determines what they are
                for (int i = 0; i < 8; i++) {
                    if (tweet.toLowerCase().contains(clans[i].toLowerCase())) {
                        if (first) {
                            clan1 = clans[i];
                            first = false;
                        } else {
                            clan2 = clans[i];
                        }
                    }
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                System.exit(-1);
            }


            //New thread is created because this function can't update UI Thread.
            runOnUiThread(new Thread() {
                public void run() {
                    TextView lastUpdate = (TextView) findViewById(R.id.lastUpdate);
                    ImageView clanPic1 = (ImageView) findViewById(R.id.imageView);
                    ImageView clanPic2 = (ImageView) findViewById(R.id.imageView2);
                    //changes each of the clan icons to the newly updated clans
                    switch (clan1) {
                        case "Amlodd":
                            clanPic1.setImageResource(R.drawable.amlodd);
                            break;
                        case "Cadarn":
                            clanPic1.setImageResource(R.drawable.cadarn);
                            break;
                        case "Crwys":
                            clanPic1.setImageResource(R.drawable.crwys);
                            break;
                        case "Hefin":
                            clanPic1.setImageResource(R.drawable.hefin);
                            break;
                        case "Iorwerth":
                            clanPic1.setImageResource(R.drawable.iorwerth_dark);
                            break;
                        case "Ithell":
                            clanPic1.setImageResource(R.drawable.ithell_dark);
                            break;
                        case "Meilyr":
                            clanPic1.setImageResource(R.drawable.meilyr);
                            break;
                        case "Trahaearn":
                            clanPic1.setImageResource(R.drawable.trahaearn);
                            break;
                    }

                    switch (clan2) {
                        case "Amlodd":
                            clanPic2.setImageResource(R.drawable.amlodd);
                            break;
                        case "Cadarn":
                            clanPic2.setImageResource(R.drawable.cadarn);
                            break;
                        case "Crwys":
                            clanPic2.setImageResource(R.drawable.crwys);
                            break;
                        case "Hefin":
                            clanPic2.setImageResource(R.drawable.hefin);
                            break;
                        case "Iorwerth":
                            clanPic2.setImageResource(R.drawable.iorwerth_dark);
                            break;
                        case "Ithell":
                            clanPic2.setImageResource(R.drawable.ithell_dark);
                            break;
                        case "Meilyr":
                            clanPic2.setImageResource(R.drawable.meilyr);
                            break;
                        case "Trahaearn":
                            clanPic2.setImageResource(R.drawable.trahaearn);
                            break;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                    sdf.setTimeZone(TimeZone.getDefault());
                    String currentTime = sdf.format(new Date());
                    lastUpdate.setText("Last updated: " + currentTime);

                }
            });
            return null;
        }

        //@Override
        protected void onProgressUpdate(Integer... a) {
            Log.d(mTAG, "Progressing");
        }

        @Override
        protected void onPostExecute(Void a) {
            Log.d(mTAG, "Inside onPostExecute");
        }
    }

}