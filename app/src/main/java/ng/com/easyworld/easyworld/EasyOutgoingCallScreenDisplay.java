package ng.com.easyworld.easyworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EasyOutgoingCallScreenDisplay extends AppCompatActivity {


    private TextView PhoneNum ;
    TelephonyManager tm;

    private String Name;
    private String Longitude;
    private String Latitude;
    private String Phonenumber;

    String phoneNumber ;
    ViewDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        setContentView(R.layout.activity_easy_outgoing_call_screen_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        PhoneNum = (TextView) findViewById(R.id.PhoneNum);





        Intent intent = getIntent();

        phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        Name = intent.getStringExtra("Name");
        Longitude = intent.getStringExtra("Longitude");
        Latitude = intent.getStringExtra("Latitude");
        Phonenumber = intent.getStringExtra("PhoneNumber");

         alert = new ViewDialog();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }





    private PhoneStateListener mPhoneListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        PhoneNum.setText(Name+"   "+ Phonenumber);


                       // alert.showDialog(EasyOutgoingCallScreenDisplay.this, "OTP has been sent to your Mail ");

                        //Toast.makeText(EasyOutgoingCallScreenDisplay.this, "CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        PhoneNum.setText(Name+"   "+ Phonenumber);

                       // Toast.makeText(EasyOutgoingCallScreenDisplay.this, "CALL_STATE_OFFHOOK", Toast.LENGTH_SHORT).show();
                       // alert.showDialog(EasyOutgoingCallScreenDisplay.this, "OTP has been sent to your Mail ");
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        PhoneNum.setText(Name+"   "+ Phonenumber);
                       // Toast.makeText(EasyOutgoingCallScreenDisplay.this, "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
                       // alert.showDialog(EasyOutgoingCallScreenDisplay.this, "OTP has been sent to your Mail ");
                        break;
                    default:
                        PhoneNum.setText(Name+"   "+ Phonenumber);
                       // Toast.makeText(EasyOutgoingCallScreenDisplay.this, "default", Toast.LENGTH_SHORT).show();
                        Log.i("Default", "Unknown phone state=" + state);
                       // alert.showDialog(EasyOutgoingCallScreenDisplay.this, "OTP has been sent to your Mail ");
                }
            } catch (Exception e) {
                Log.i("Exception", "PhoneStateListener() e = " + e);
            }
        }
    };
}





