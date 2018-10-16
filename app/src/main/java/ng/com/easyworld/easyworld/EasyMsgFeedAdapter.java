package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by Oisrael on 5/1/2018.
 */

public class EasyMsgFeedAdapter extends ArrayAdapter<EasyFeedMessage> {

    private ArrayList<EasyFeedMessage> dataSet;
    Context mContext;
    Activity activity;
    public ImageLoader imageLoader;


    // View lookup cache
    private static class ViewHolder2 {

        TextView PostId;
        TextView PostTitle;
        ImageButton Image;
        ImageButton ProfileImage;
        VideoView Video;
        TextView Content;
        TextView emailAddress;
        TextView Timee;
        TextView Datee;
        TextView Longi;
        TextView Lati;
        TextView Privacy;
        TextView Likes;

        TextView Comments;
        TextView Dislikes;
        TextView SenderName;

        //OTHER BUTTONS
        Button Like;
        Button Comment;
        ImageButton MapBtn;
        ImageButton Options;

    }

    public EasyMsgFeedAdapter(ArrayList<EasyFeedMessage> data, Activity activity) {
        super(activity, R.layout.feeditems, data);
        this.dataSet = data;
        this.activity=activity;
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }


    String uri = null;
    String uri2 = null;

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //
         View v = convertView;
        // Check if an existing view is being reused, otherwise inflate the view
        EasyMsgFeedAdapter.ViewHolder2 viewHolder; // view lookup cache stored in tag



        if (convertView == null) {

            viewHolder = new EasyMsgFeedAdapter.ViewHolder2();
            LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
            convertView = inflater.inflate(R.layout.feeditems, parent, false);


            viewHolder.PostId = (TextView) convertView.findViewById(R.id.PostId);
            viewHolder.PostTitle = (TextView) convertView.findViewById(R.id.PostTitle);
            viewHolder.Image = (ImageButton) convertView.findViewById(R.id.Image1);
            viewHolder.ProfileImage = (ImageButton) convertView.findViewById(R.id.PosterImage);
            viewHolder.Video = (VideoView) convertView.findViewById(R.id.Video1);
            viewHolder.Content = (TextView) convertView.findViewById(R.id.Content);
           // viewHolder.emailAddress = (TextView) convertView.findViewById(R.id.SenderName);
            viewHolder.Timee = (TextView) convertView.findViewById(R.id.DateAndTime);
            //viewHolder.Datee = (TextView) convertView.findViewById(R.id.Comment);
           // viewHolder.Longi = (TextView) convertView.findViewById(R.id.Comment);
           // viewHolder.Lati = (TextView) convertView.findViewById(R.id.Comment);
           // viewHolder.Privacy = (TextView) convertView.findViewById(R.id.Comment);
           // viewHolder.Likes = (TextView) convertView.findViewById(R.id.NoOfLike);
           // viewHolder.Comments = (TextView) convertView.findViewById(R.id.NoOfComments);
            viewHolder.Like =  convertView.findViewById(R.id.Like);
            viewHolder.Comment = convertView.findViewById(R.id.Comment);
            //viewHolder.Dislikes = (TextView) convertView.findViewById(R.id.Dis);
            viewHolder.SenderName = (TextView) convertView.findViewById(R.id.SenderName);
            viewHolder.MapBtn =  convertView.findViewById(R.id.MapBtn);
            viewHolder.Options =  convertView.findViewById(R.id.Options);


            convertView.setTag(viewHolder);
        } else {
            Bitmap bm= null;
            viewHolder = (EasyMsgFeedAdapter.ViewHolder2) convertView.getTag();

            final EasyFeedMessage easyFeedMsg = getItem(position);


            if(easyFeedMsg!=null){
                viewHolder.PostTitle.setText(easyFeedMsg.getTitle());
                //viewHolder.Image.setText(easyFeedMsg.getTitle());
                // viewHolder.Video.setText(easyFeedMsg.getTitle());
                viewHolder.Content.setText(easyFeedMsg.getContent());

                viewHolder.SenderName.setText(easyFeedMsg.getSenderName());
                viewHolder.Timee.setText(easyFeedMsg.getTime());
                //viewHolder.Datee.setText(easyFeedMsg.getDate());
                //    viewHolder.Longi.setText(easyFeedMsg.getLongi());

                //   viewHolder.Lati.setText(easyFeedMsg.getLati());
                //   viewHolder.Privacy.setText(easyFeedMsg.getPrivacy());
               // viewHolder.Likes.setText(easyFeedMsg.getLikes());

               // viewHolder.Comments.setText(easyFeedMsg.getComment());

                //  viewHolder.Dislikes.setText(easyFeedMsg.getDislikes());
                Drawable image=(Drawable)activity.getResources().getDrawable(R.drawable.mapicon);
                viewHolder.MapBtn.setBackground(image);


                Drawable OptionImage=(Drawable)activity.getResources().getDrawable(R.drawable.options);
                viewHolder.Options.setBackground(OptionImage);


                viewHolder.Image.setTag(easyFeedMsg.getImage());


                uri = "https://www.easyworld.com.ng/images/"  + easyFeedMsg.getImage();//easyUser.getBcprofilephoto()
                uri2 = "https://www.easyworld.com.ng/images/"  + easyFeedMsg.getPhoto();//easyUser.getBcprofilephoto()

                if(easyFeedMsg.getImage()!=null){

                    // EasyImageLoader myLoader = new EasyImageLoader(viewHolder.Image,uri);
                    //myLoader.execute();

                   // ImageManager Im = new ImageManager(activity);
                    //Im.DisplayImage(uri,activity,viewHolder.Image,easyFeedMsg.getImage().toString());
                    imageLoader.DisplayImage(uri, viewHolder.Image,uri2, viewHolder.ProfileImage);

                }else{
                    viewHolder.Image.setVisibility(View.INVISIBLE);
                }






            }

        }






        return convertView;
    }

    }
