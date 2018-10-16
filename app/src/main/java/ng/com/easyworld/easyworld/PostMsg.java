package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.BitmapFactory.decodeFile;

public class PostMsg extends AppCompatActivity {
    Bitmap bitmap;
    ProgressDialog prgDialog;
    String destinationUrl = String.format("https://www.easyworld.com.ng/ewc.asmx/upldImage?");
    String databasUrl = String.format("https://www.easyworld.com.ng/ewc.asmx/UpdatePhoto?");
    String  databasUrl2 = String.format("https://www.easyworld.com.ng/ewc.asmx/SendPost?");
    // RequestParams params = new RequestParams();
    String imgPath, fileName,emailPref,pwPref;
    private static int RESULT_LOAD_IMG1 = 1;
    private static int RESULT_LOAD_IMG2 = 2;
    SharedPreferences userSession;

    StringBuilder ResponseXml = new StringBuilder("");
    String URLPost;
    private String RegResponse;
    URL postUrl;

    private ImageButton sendPost;
    private ImageButton addVideo;


    EditText postTitleBox;
    EditText contentBox;

    String email = null; //pref
    String pw = null; //pref
    String postId = null;
    String postTitle = "";
    String imageName = ""; //pref
    String videoName = ""; //pref
    String content = null ;


    String tim = null ;
    Date dat = null ;
    String formattedDateString;
    double logi = 0.0; //pref
    double lati = 0.0; //pref
    String privacy = "public"; //pref
    int likes  ;
    int comments ;
    int dislikes ;
    private final int IMAGENUMBER1 = 1;
    private final int IMAGENUMBER2 = 1;
    private ImageView image1;
    private ImageView image2;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_msg);


        image1 = findViewById(R.id.imageButton);
        image2 = findViewById(R.id.imageButton2);
    }



    public void SendPost(View v){

        Animation scaleBtn = AnimationUtils.loadAnimation(  getBaseContext(), R.anim.hidefabbtn);
        sendPost=(ImageButton) findViewById(R.id.SendPost);
        sendPost.startAnimation(scaleBtn);

        //START THE SERVICE
        postTitleBox = findViewById(R.id.PostTitle);
        contentBox = findViewById(R.id.PostContent);
        //SET ALL REQUIRED INFORMATION

        postTitle = postTitleBox.getText().toString();
        content = contentBox.getText().toString();
        likes = 0;
        comments =0;
        dislikes = 0;


        //GET EMAIL AND PW FROM SHAREPREFFERENCE
        userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

        Map<String, ?> allPreferences = userSession.getAll();

        if (!allPreferences.isEmpty()){
            email = userSession.getString("Email", "");
            pw = userSession.getString("Pw", "");
            imageName = userSession.getString("ImageName", "");
            videoName = userSession.getString("VideoName", "");
            logi = userSession.getFloat("Longi", 0);
            lati = userSession.getFloat("Lati", 0);
            privacy = userSession.getString("Privacy", "public");

        }

        //GENERATE POSTID
        SerialGeneratn gen = new SerialGeneratn();
        postId = gen.getNewSerialName();


        //SET THE TIME AND DATE
        Calendar cal = Calendar.getInstance();
        //int millisecond = cal.get(Calendar.MILLISECOND);
        // int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        //12 hour format
        int hour = cal.get(Calendar.HOUR);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);

        tim = hour + ":" + minute ;
        //dat = Calendar.getInstance().getTime().toString();
        dat = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDateString = formatter.format(dat);


        //connect to service and post the message

        SendWallMsgDoInBackground sendPost = new SendWallMsgDoInBackground();
        Thread postTask = new Thread(sendPost);
        postTask.start();

    }


    @Override
    protected void onStop() {
        super.onStop();


    }







    private class SendWallMsgDoInBackground implements Runnable {
        @Override
        public void run() {
          imgPath =  GetFromSharedPref("WALLIMAGE1PATH");
            StringBuilder xml = new StringBuilder("");
            String s2=null;
            URL  URLPost = null;
            try {
                URLPost = new URL(String.format("https://www.easyworld.com.ng/ewc.asmx/SendPost?"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try{

                HashMap<String,String> dataToBeSent = new HashMap<>();

                dataToBeSent.put("email",email);
                dataToBeSent.put("pw",pw);
                dataToBeSent.put("postId",postId);
                dataToBeSent.put("postTitle",postTitle);
                dataToBeSent.put("imageName",imageName);
                dataToBeSent.put("videoName",videoName);
                dataToBeSent.put("contentt",content);
                dataToBeSent.put("tim",tim);
                dataToBeSent.put("dat",formattedDateString);
                dataToBeSent.put("logi",new Double(logi).toString());
                dataToBeSent.put("lati",new Double(lati).toString());
                dataToBeSent.put("privacy",privacy);
                dataToBeSent.put("likes",Integer.toString(likes) );
                dataToBeSent.put("comments",Integer.toString(comments));
                dataToBeSent.put("dislikes",Integer.toString(dislikes));

                encodeImageToStringThenPost postMsg = new encodeImageToStringThenPost(getBaseContext(),destinationUrl,databasUrl2,imgPath,true,dataToBeSent);
                postMsg.execute();
                      }
            catch(Exception e){

                String v = e.toString();
            }



        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ( activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        if (isConnected) {
            return true;    }    return false;
    }


    public void ChooseImage1(View v) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG1);

    }

    public void ChooseImage2(View v) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG2);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == RESULT_LOAD_IMG1 && resultCode == RESULT_OK && null != data)
            {
                Uri selectedImageUrl = data.getData();
                String fileManagerString = selectedImageUrl.getPath();
                String SelectedImagePath = getPath(selectedImageUrl);
                if (SelectedImagePath != null) {
                    imgPath = SelectedImagePath;
                } else if (fileManagerString != null) {
                    imgPath = fileManagerString;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (imgPath != null) {

                    SaveToSharedPref("WALLIMAGE1PATH",imgPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    bitmap = decodeFile(imgPath, options);
                    image1.setImageBitmap(bitmap);

                } else {
                    bitmap = null;
                }

            }
            else if(requestCode == RESULT_LOAD_IMG2 && resultCode == RESULT_OK && null != data)
            {

                Uri selectedImageUrl = data.getData();
                String fileManagerString = selectedImageUrl.getPath();
                String SelectedImagePath = getPath(selectedImageUrl);
                if (SelectedImagePath != null) {
                    imgPath = SelectedImagePath;
                } else if (fileManagerString != null) {
                    imgPath = fileManagerString;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (imgPath != null) {

                    SaveToSharedPref("WALLIMAGE1PATH",imgPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    bitmap = decodeFile(imgPath, options);
                    image2.setImageBitmap(bitmap);


                } else {
                    bitmap = null;
                }



            }
            else{

                Toast.makeText(this, "You haven't picked Image",

                        Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e){

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection , null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }




      public void  SaveToSharedPref(String key,String value){

            SharedPreferences usession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = usession.edit();
            editor.putString(key, value);
            editor.apply();
        }


    public String  GetFromSharedPref(String key){

          String val = null;
        SharedPreferences sessn = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
        Map<String, ?> allPreferences = sessn.getAll();


        if (!allPreferences.isEmpty()){

            val = sessn.getString(key, null);

        }
        return val;
    }




}
