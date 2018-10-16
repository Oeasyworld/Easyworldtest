package ng.com.easyworld.easyworld;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrafficResponseReceiver extends AppCompatActivity {

    private TextView veryFree;
    private TextView movingSlowly;
    private TextView standstill;
    private ImageView closeBtn;
    private DbaseHelper db;

    private ImageView CloseBtn;
   private  ListView list;
   private TextView timer;

    private SharedPreferences userSession;

    private CountDownTimer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_reply_list);

        UpdateTrafficVal up = new UpdateTrafficVal();
        up.execute();

        db = new DbaseHelper(getBaseContext());

        CloseBtn=findViewById(R.id.CloseBtn);



        timer = findViewById(R.id.timer);
        
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });

    }

    public class UpdateTrafficVal extends AsyncTask<Integer,Void,TrafficDataAdapter>{


        @Override
        protected TrafficDataAdapter doInBackground(Integer... integers) {


             TrafficDataAdapter adapter = null ;

            try {


                ArrayList<TrafficQueryData> data = db.getAllData();

                adapter = new TrafficDataAdapter(data,TrafficResponseReceiver.this);



            } catch (Exception e) {
                e.printStackTrace();

            }



           return adapter;
        }

        @Override
        protected void onPostExecute(TrafficDataAdapter adapter) {
            super.onPostExecute(adapter);

            list=findViewById(R.id.TrafficReplyList);

            list.setAdapter(adapter);
        }
    }
}
