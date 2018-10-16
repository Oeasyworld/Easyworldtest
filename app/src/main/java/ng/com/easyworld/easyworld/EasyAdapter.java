package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



/**
 * Created by oisrael on 07-Apr-17.
 */

public class EasyAdapter extends ArrayAdapter<EasyUser> {

    private ArrayList<EasyUser> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView businesname;
        TextView person;
        TextView pos;
        TextView field1;
        TextView field2;
        TextView field3;
        TextView officeaddress;
        TextView phone1;
        TextView phone2;
        TextView emailaddress;
        TextView websit;
       // TextView descriptn;
        ImageView logo;
        //TextView bclongitude;
        //TextView bclatitude;
        TextView category;
        ImageView profilephoto;
        ImageView banner;
        ImageButton callindi;
        ImageButton msgindi;
        ImageButton videoindi;
        ProgressBar progressBar;
        SharedPreferences userSession;
    }

    public EasyAdapter(ArrayList<EasyUser> data, Context context) {
        super(context, R.layout.connectlistviewrowss, data);
        this.dataSet = data;
        this.mContext=context;

    }


    URL uri = null;

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
      final  EasyUser easyUser = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.connectlistviewrowss, parent, false);
            viewHolder.businesname = (TextView) convertView.findViewById(R.id.BusName);
            viewHolder.person = (TextView) convertView.findViewById(R.id.PersonInCh);
            viewHolder.pos = (TextView) convertView.findViewById(R.id.Positn);
            viewHolder.field1 = (TextView) convertView.findViewById(R.id.Field1);
            viewHolder.banner = (ImageView) convertView.findViewById(R.id.Banner);
            //viewHolder.field2 = (TextView) convertView.findViewById(R.id.Field2);
            //viewHolder.field3 = (TextView) convertView.findViewById(R.id.Field3);
            //viewHolder.officeaddress = (TextView) convertView.findViewById(R.id.OffiAddress);
           // viewHolder.phone1 = (TextView) convertView.findViewById(R.id.Phone1);
           // viewHolder.phone2 = (TextView) convertView.findViewById(R.id.Phone2);
            //viewHolder.emailaddress = (TextView) convertView.findViewById(R.id.Email);
            //viewHolder.websit = (TextView) convertView.findViewById(R.id.Websit);
            //viewHolder.logo = (ImageView) convertView.findViewById(R.id.bclogo);
           // viewHolder.category = (TextView) convertView.findViewById(R.id.Category);
            viewHolder.profilephoto = (ImageView) convertView.findViewById(R.id.ProfileImage);




            // viewHolder.callindi = (ImageButton) convertView.findViewById(R.id.CallindiBtn);
            //viewHolder.msgindi = (ImageButton) convertView.findViewById(R.id.MsgIndiBtn);
            //viewHolder.videoindi = (ImageButton) convertView.findViewById(R.id.VideoIndiBtn);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUserEmail = easyUser.getBcEmailaddress();
                //String selectedUserPhone = easyUser.getBcphone1().toString();

                SharedPreferences userSession;
                userSession =getContext().getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("selUserEmail", selectedUserEmail);
                //editor.putString("selUserPhone", selectedUserPhone);

                editor.apply();

                Intent chatPage = new Intent(getContext(),ChatControlActivity.class);

                getContext().startActivity(chatPage);

            }
        });



        viewHolder.profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUserEmail = easyUser.getBcEmailaddress();
                //String selectedUserPhone = easyUser.getBcphone1().toString();

                SharedPreferences userSession;
                userSession =getContext().getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("selUserEmail", selectedUserEmail);
                //editor.putString("selUserPhone", selectedUserPhone);

                editor.apply();

                Intent chatPage = new Intent(getContext(),ChatControlActivity.class);

                getContext().startActivity(chatPage);

            }
        });






        viewHolder.businesname.setText(easyUser.getBcbusinesname());
        viewHolder.person.setText(easyUser.getbcperson());
        viewHolder.pos.setText(easyUser.getBcpos());
        viewHolder.field1.setText(easyUser.getBcfield1());
        //viewHolder.field2.setText(easyUser.getBcfield2());
        //viewHolder.field3.setText(easyUser.getBcfield3());
       // viewHolder.officeaddress.setText(easyUser.getBcoffiaddress());
        //viewHolder.phone1.setText(easyUser.getBcphone1());
       // viewHolder.phone2.setText(easyUser.getBcphone2());
        //viewHolder.emailaddress.setText(easyUser.getBcEmailaddress());
        //viewHolder.websit.setText(easyUser.getBcWebsite());
       // viewHolder.logo.setImageResource(R.drawable.msgindi);
        //viewHolder.category.setText(easyUser.getBcCategory());

        // Return the completed view to render on screen
        //viewHolder.callindi.setImageResource(R.drawable.callimage);
        //viewHolder.msgindi.setImageResource(R.drawable.chatimage);
        //viewHolder.videoindi.setImageResource(R.drawable.videoimage);



        try {
            uri = new URL("https://www.easyworld.com.ng/images/" + "xSGyIfSrbNRhXqdGv0xM8MUlC8eVfy9ozYR.jpg");//easyUser.getBcprofilephoto()
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        EasyImageLoader myLoader = new EasyImageLoader(viewHolder.profilephoto,uri);
        myLoader.execute();

        return convertView;
    }
}
