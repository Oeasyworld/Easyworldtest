package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 1/30/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class ViewDialog {


    public void showDialog(Home activity ){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_otp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.txt_file_path);
        TextView veryFreeVal = dialog.findViewById(R.id.VeryFreeVal);
        TextView  movingSlowlyVal = dialog.findViewById(R.id.MovingSlowlyVal);
        TextView  standstillVal = dialog.findViewById(R.id.StandstillVal);

        SharedPreferences userSession;

        try {
            userSession = activity.getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()){
                veryFreeVal.setText(userSession.getString("VeryFree", null));
                movingSlowlyVal.setText(userSession.getString("MovingSlowly", null));
                standstillVal.setText(userSession.getString("Standstill", null));
            }

        }
        catch(Exception e){
            String err = e.toString();

        }









        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.CloseBtn);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}
