package ng.com.easyworld.easyworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NotificationReceiver extends AppCompatActivity {

    private Button veryfree;
    private Button movingSlowly;
    private Button standstill;
    private Button reporIncidents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traficdialog);

        Intent i = getIntent();
        final String msgId = i.getStringExtra("MSGID");


        veryfree = findViewById(R.id.VeryFree);
        movingSlowly = findViewById(R.id.MovingSlowly);
        standstill = findViewById(R.id.Standstill);
        reporIncidents = findViewById(R.id.ReportBtn);


        veryfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = new Bundle();
                Intent ServiceTask = new Intent( getBaseContext() ,EasyService.class);
                ServiceTask.setAction("SEND_TRAFFIC_ANSWER");
                extras.putString("ANSWER","Very Free");
                extras.putString("MSGID",msgId);
                ServiceTask.putExtras(extras);
                getBaseContext().startService(ServiceTask);
                finish();
            }
        });

        movingSlowly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = new Bundle();
                Intent ServiceTask = new Intent( getBaseContext() ,EasyService.class);
                ServiceTask.setAction("SEND_TRAFFIC_ANSWER");
                extras.putString("ANSWER","Moving Slowly");
                extras.putString("MSGID",msgId);
                ServiceTask.putExtras(extras);
                getBaseContext().startService(ServiceTask);
                finish();

            }
        });

        standstill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = new Bundle();
                Intent ServiceTask = new Intent( getBaseContext() ,EasyService.class);
                ServiceTask.setAction("SEND_TRAFFIC_ANSWER");
                extras.putString("ANSWER","Standstill");
                extras.putString("MSGID",msgId);
                ServiceTask.putExtras(extras);
                getBaseContext().startService(ServiceTask);
                finish();

            }
        });

        reporIncidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = new Bundle();
                Intent ServiceTask = new Intent( getBaseContext() ,EasyService.class);
                ServiceTask.setAction("SEND_TRAFFIC_ANSWER");
                extras.putString("REPORTINCIDENT","Report Incidents");
                ServiceTask.putExtras(extras);
                getBaseContext().startService(ServiceTask);
                finish();

            }
        });




    }

}
