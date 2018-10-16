package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.SharedPreferences;
import android.view.View;

import static android.R.attr.bitmap;
import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by oisrael on 20-Dec-17.
 */

public class encodeImageToStringAndPost implements Runnable {


    private String url;
    private String imgPath;
    private static int RESULT_LOAD_IMG = 1;
    Bitmap bitmap;
    ProgressDialog prgDialog;
    String emailPref = null;
    String pwPref = null;
    String encodedString;
   static String imageUploadResponse;
   static String uploadedImage;
    Boolean uploadSuccessResponse = false;
    private static StringBuilder myXml = new StringBuilder("");
    SharedPreferences userSession;
     encodeImageToStringAndPost(String destinationUrl, String imageSourcePath,String email,String password){
        this.url = destinationUrl;
        this.imgPath = imageSourcePath;
         this.emailPref = email;
         this.pwPref = password;
    }


    //Return the name of the image that was uploaded
    public String getUploadedImage(){

        uploadedImage = imageUploadResponse;
        return uploadedImage;
    }

    //Return the flag indicating successful image upload
    public Boolean getUploadSuccessResponse(){

        return uploadSuccessResponse;
    }

    @Override
    public void run() {
        StringBuilder xml = new StringBuilder("");

        String s2 = null;
        StringBuilder ResponseXml = new StringBuilder();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        bitmap = decodeFile(imgPath, options);


        //CHECK IF THE IMAGE IS LOCAL OR IT NEED TO BE DOWNLOADED

        if (bitmap==null){

            //Get the real Image Path from the Url of the other source Image
            String realImgPath = imgPath.substring(imgPath.indexOf("http"),imgPath.length());

            //Download the image
            try {

                URL imageFromOtherSource = new URL(realImgPath);
                HttpURLConnection clientt = (HttpURLConnection) imageFromOtherSource.openConnection();

                InputStream in = clientt.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //Get the file name extention
        final String FPATH = imgPath;
        Filename fileToCheck = new Filename(FPATH, '/', '.');
        String imgExtention = fileToCheck.extension();

        //Compress the Image to reduce image size to make upload easy
        switch (imgExtention) {
            case "jpg":
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                break;
            case "png":
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                break;

            default: bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                break;
        }
        byte[] byte_arr = stream.toByteArray();

        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);


        URL ul = null;
        try {
            ul = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Connect to the WebService and post the Image
        try {
            FormPoster poster = new FormPoster(ul);

            //Add the query strings
            poster.add("encodedImage", encodedString);
            poster.add("pw", pwPref); //Get this from the preference
            poster.add("email", emailPref);

            //Post the Data and get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(poster.post()));
            String current;

            while ((current=(in.readLine())) != null) {

                xml.append( current) ;
            }

            String s = Pattern.compile("&lt;").matcher(xml).replaceAll("<");
            s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");

            myXml.append(s2);

            try{
                GetRespDataList ResData = new GetRespDataList();
                ArrayList<EasyUserReg> theUser =  ResData.GetObjectDataLit(myXml);
                imageUploadResponse = theUser.get(0).respons.toString();
                if (imageUploadResponse != "" && imageUploadResponse != null){

                    //Return a flag to indicate successful image upload
                    uploadSuccessResponse=true;
                }
            }
            catch(Exception e){

                //NOTIFY THE USER OF REGISTRATION ERROR
                imageUploadResponse = "Please try again later";

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
