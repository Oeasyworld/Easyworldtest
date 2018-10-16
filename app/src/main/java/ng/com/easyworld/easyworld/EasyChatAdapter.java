package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Oisrael on 1/22/2018.
 */

public class EasyChatAdapter extends ArrayAdapter<EasyMessage> {

    private ArrayList<EasyMessage> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder2 {

        //MESSAGES TEXT VIEWS
        TextView myMessage;
        TextView othersMessage;

        //IMAGE BUTTONS
        ImageButton myImages;
        ImageButton othersImages;

        //VIDEO BUTTONS
        VideoView myVideos;
        VideoView othersVideos;

        //DATE TEXT VIEW
        TextView dateTimeTxt;


    }

    public EasyChatAdapter(ArrayList<EasyMessage> data, Context context) {
        super(context, R.layout.chat_list, data);
        this.dataSet = data;
        this.mContext=context;

    }









    URL uri = null;

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final  EasyMessage easyUser = getItem(position);



        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder2 viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder2();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.chat_list, parent, false);
            viewHolder.myMessage = (TextView) convertView.findViewById(R.id.MyChatMsg);
            viewHolder.othersMessage = (TextView) convertView.findViewById(R.id.OtherChatMsg);

            viewHolder.myImages = (ImageButton) convertView.findViewById(R.id.MyImgs);
            viewHolder.othersImages = (ImageButton) convertView.findViewById(R.id.OthersImgs);

            viewHolder.myVideos = (VideoView) convertView.findViewById(R.id.MyVideo);
            viewHolder.othersVideos = (VideoView) convertView.findViewById(R.id.othersVideo);

           viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.DataTimeTxt);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;


        //SET THE TEXTVIEWS

        if(easyUser.getMyMessage() != "" && easyUser.getMyMessage() != null ){

            viewHolder.myMessage.setText(easyUser.getMyMessage());
        }else{
            viewHolder.myMessage.setVisibility(View.GONE);
        }



        if(easyUser.getOthersMessage() != "" && easyUser.getOthersMessage() != null ) {
            viewHolder.othersMessage.setText(easyUser.getOthersMessage());
        }
        else
        {
            viewHolder.othersMessage.setVisibility(View.GONE);
        }


        viewHolder.dateTimeTxt.setText(easyUser.getDateTimeTxt());
        viewHolder.othersVideos.setVisibility(View.GONE);

        viewHolder.myVideos.setVisibility(View.GONE);
        viewHolder.myImages.setVisibility(View.GONE);

        viewHolder.othersImages.setVisibility(View.GONE);


        try {
            uri = new URL("https://www.easyworld.com.ng/images/" + "xSGyIfSrbNRhXqdGv0xM8MUlC8eVfy9ozYR.jpg");//easyUser.getBcprofilephoto()
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //EasyImageLoader myLoader = new EasyImageLoader(viewHolder.myImages,uri);
       // myLoader.execute();

       // EasyImageLoader myLoader2 = new EasyImageLoader(viewHolder.othersImages,uri);
       // myLoader2.execute();


        //SET THE VIDEOS
        // EasyImageLoader myLoader = new EasyImageLoader(viewHolder.myImages,uri);
        //myLoader.execute();

        //EasyImageLoader myLoader2 = new EasyImageLoader(viewHolder.othersImages,uri);
        //myLoader2.execute();


        viewHolder.myImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String selectedUserEmail = easyUser.getMyImages();

            }
        });

        viewHolder.othersImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String selectedUserEmail = easyUser.getOthersImages();

            }
        });


        return convertView;
    }
}
