package ng.com.easyworld.easyworld;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.hubs.HubProxy;

/**
 * Created by oisrael on 29-Dec-17.
 */

public class EasyProfileAdapter extends ArrayAdapter<EasyUserProfile> {

    private String host;
    private HubProxy hub;
    private TrafficPrivateMessage trafficPm;
    private PrivateMessage loginData;
    private static String Caller;
    private static String pw;
    private SharedPreferences userSession;

    private boolean isDone = false;
    private ArrayList<EasyUserProfile> dataSet;
    Context mContext;
    Activity activity;
    public ImageLoader imageLoader;
    private LinearLayout container_locn;



    // View lookup cache
    private static class ViewHolder {
        TextView username;
        TextView firstname;
        TextView lastname;
        TextView oldname;
        TextView oldNickName;
        TextView emailAddress;
        TextView phone1;
        TextView phone2;
        TextView phone3;
        TextView phone4;
        TextView dateOfBirth;
        TextView addres;
        TextView city;
        TextView statee;
        TextView country;
        TextView occupation;
        TextView gender;
        ImageView profilePhoto;
        ImageView SendLocationMsg;
        ImageView CircleCall;
        ImageView trafficQuestion;
        ImageButton VideoCall;
        ProgressBar progressBar;
        SharedPreferences userSession;
        LinearLayout ActionBtnContainer;
        ListView myView;
        ListEventObject clicked;
    }


    public EasyProfileAdapter(ArrayList<EasyUserProfile> data, Activity activity) {
        super(activity, R.layout.connectlistviewrowss, data);
        this.dataSet = data;
        this.activity = activity;
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    String uri = null;
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final EasyUserProfile easyUser = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final EasyProfileAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;


        container_locn = activity.findViewById(R.id.CotainerLocatnName);

        if (convertView == null) {

            viewHolder = new EasyProfileAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.connectlistviewrowss, parent, false);
            viewHolder.username = (TextView) convertView.findViewById(R.id.BusName);
            viewHolder.firstname = (TextView) convertView.findViewById(R.id.PersonInCh);
            viewHolder.lastname = (TextView) convertView.findViewById(R.id.Positn);
            // viewHolder.oldname = (TextView) convertView.findViewById(R.id.Field1);
            // viewHolder.oldNickName = (TextView) convertView.findViewById(R.id.Field2);
            viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.Email);
            //viewHolder.phone1 = (TextView) convertView.findViewById(R.id.Pho);
            // viewHolder.phone2 = (TextView) convertView.findViewById(R.id.Phone2);
            //viewHolder.phone3 = (TextView) convertView.findViewById(R.id.Phone1);
            // viewHolder.phone4 = (TextView) convertView.findViewById(R.id.Phone2);
            // viewHolder.dateOfBirth = (TextView) convertView.findViewById(R.id.Email);
            // viewHolder.addres = (TextView) convertView.findViewById(R.id.Websit);
            // viewHolder.city = (ImageView) convertView.findViewById(R.id.bclogo);
            // viewHolder.country = (TextView) convertView.findViewById(R.id.Category);
            viewHolder.occupation = (TextView) convertView.findViewById(R.id.Field1);
            // viewHolder.gender = (TextView) convertView.findViewById(R.id.Gender);
            viewHolder.profilePhoto = (ImageView) convertView.findViewById(R.id.ProfileImage);
            viewHolder.SendLocationMsg = (ImageView) convertView.findViewById(R.id.SendLocationMsg);
            viewHolder.CircleCall = (ImageView) convertView.findViewById(R.id.CircleCall);
            viewHolder.VideoCall = (ImageButton) convertView.findViewById(R.id.LocationVideoCal);
            viewHolder.trafficQuestion = (ImageButton) convertView.findViewById(R.id.TrafficReport);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder.ActionBtnContainer = (LinearLayout) convertView.findViewById(R.id.ActionBtnContainer);
            viewHolder.myView = convertView.findViewById(R.id.ConnectList);



            convertView.setTag(viewHolder);
        } else {



            Bitmap bm = null;

            viewHolder = (EasyProfileAdapter.ViewHolder) convertView.getTag();



            viewHolder.ActionBtnContainer.setVisibility(View.INVISIBLE);
            viewHolder.profilePhoto.setImageBitmap(bm);



            uri = "https://www.easyworld.com.ng/images/"  + easyUser.getPhoto();//easyUser.getBcprofilephoto()

            if(easyUser.getPhoto()!=null){

                imageLoader.DisplayImage(uri, viewHolder.profilePhoto,"",null);

            }else{
                viewHolder.profilePhoto.setVisibility(View.INVISIBLE);
            }

        }




        viewHolder.SendLocationMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedUserEmail = easyUser.getEmailaddress();
                //String selectedUserPhone = easyUser.getBcphone1().toString();

                SharedPreferences userSession;
                userSession = getContext().getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("selUserEmail", selectedUserEmail);
                //editor.putString("selUserPhone", selectedUserPhone);

                editor.apply();

                Intent chatPage = new Intent(getContext(), ChatControlActivity.class);

                activity.startActivity(chatPage);

            }
        });


        viewHolder.CircleCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phone = easyUser.getPhone();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));

                intent.putExtra("PhoneNumber",easyUser.getPhone());
                intent.putExtra("Name",easyUser.getFname());
                intent.putExtra("Longitude",easyUser.getLongitude());
                intent.putExtra("Latitude",easyUser.getLatitude());

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                activity.startActivity(intent);


                //Intent i = new Intent(getContext(),EasyOutgoingCallScreenDisplay.class);
                //mContext.startActivity(i);



            }
        });

        viewHolder.VideoCall.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     String selectedUserEmail = easyUser.getEmailaddress();
                     //String selectedUserPhone = easyUser.getBcphone1().toString();

                     //Save the User to call in the Static Data
                    UserToCall userToCall = new UserToCall(selectedUserEmail);

                     Intent VideoChatPage = new Intent(getContext(), EasyVideoCall.class);//Will later update back to native View

                     activity.startActivity(VideoChatPage);
                 }
        });

        viewHolder.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Animation buttons = AnimationUtils.loadAnimation(getContext(), R.anim.showlistview);

                viewHolder.ActionBtnContainer.setVisibility(View.VISIBLE);
                viewHolder.ActionBtnContainer.startAnimation(buttons);

            }
        });

        viewHolder.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Animation buttons = AnimationUtils.loadAnimation(getContext(), R.anim.showlistview);

                viewHolder.ActionBtnContainer.setVisibility(View.VISIBLE);
                viewHolder.ActionBtnContainer.startAnimation(buttons);

            }
        });

        viewHolder.trafficQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    container_locn.setVisibility(View.VISIBLE);

                }
                catch(Exception e){
                    Log.d("Error","Message Sending Error");
                }




            }
        });



        viewHolder.username.setText(easyUser.getUname());
        viewHolder.firstname.setText(easyUser.getFname());
        viewHolder.lastname.setText(easyUser.getLname());
       // viewHolder.phone1.setText(easyUser.getPhone());


        return convertView;
    }







}










