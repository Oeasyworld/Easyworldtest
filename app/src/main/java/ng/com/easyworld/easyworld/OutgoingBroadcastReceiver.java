package ng.com.easyworld.easyworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class OutgoingBroadcastReceiver extends BroadcastReceiver {


    private String Name;
    private String Longitude;
    private String Latitude;
    private String Phonenumber;
    private String savedNumber;

    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber;
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // If it is to call (outgoing)
            Intent i = new Intent(context, EasyOutgoingCallScreenDisplay.class);


            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            Uri data = intent.getData();

            Name = intent.getStringExtra("Name");
            Longitude = intent.getStringExtra("Longitude");
            Latitude = intent.getStringExtra("Latitude");
            Phonenumber = intent.getStringExtra("PhoneNumber");


            i.putExtra("PhoneNumber",savedNumber);
            i.putExtra("Name",Name);
            i.putExtra("Longitude",Longitude);
            i.putExtra("Latitude",Latitude);

            i.putExtras(intent);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else
        {
            //handle incoming call
        }
    }
}
