package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by oisrael on 25-Dec-17.
 */

public class encodeImageToStringThenPost extends AsyncTask<Void,Void,ImageObj> {

    private String ur;
    private String imgPath;

    Bitmap bitmap;
    ProgressDialog prgDialog;
    String email = null;
    String pw = null;
    String encodedString;
    private String imageUploadResponse;
    private String uploadedImage;
    private  Boolean uploadSuccessResponse = false;
    private static StringBuilder myXml = new StringBuilder("");
    private static StringBuilder myXml2 = new StringBuilder("");
    private SaveImageToInternalDirectory SaveImg;
    private  String imgnameToSq;
    private String databasUrl;
    private int imageNumber;
    private  boolean isFromWallPost = false;//Flag to know if the action is from wallpost
    private  boolean isFromProfile = false;//Flag to know if the action is from wallpost
    private Context ctx;
    HashMap dataToBeSent;

    UploadResponsObj Resobj = null;
    encodeImageToStringThenPost(Context ctx, String imageSourcePath, int imageNumber){

        this.imgPath = imageSourcePath;
        this.SaveImg = SaveImg;
        this.imageNumber = imageNumber;
        this.ctx = ctx;
    }

    encodeImageToStringThenPost(Context ctx,String directoryDestinationUrl,String databasUrl,  String imageSourcePath,String email,String password,boolean isFromProfile){
        this.ur = directoryDestinationUrl ;
        this.imgPath = imageSourcePath;
        this.email = email;
        this.pw = password;
        this.databasUrl = databasUrl;

        this.isFromProfile = isFromProfile;
        this.ctx = ctx;
    }

    encodeImageToStringThenPost(Context ctx,String directoryDestinationUrl,String databasUrl,  String imageSourcePath,boolean isFromWallPost,HashMap dataToBeSent){
        this.ur = directoryDestinationUrl ;
        this.imgPath = imageSourcePath;
        this.databasUrl = databasUrl;
        this.isFromWallPost = isFromWallPost;
        this.isFromProfile = isFromProfile;
        this.ctx = ctx;
        this.dataToBeSent = dataToBeSent;
    }


    @Override
    protected ImageObj doInBackground(Void... strings) {

        String BeginLetterExtention = "" ;
        String imagename = null;

        //END CHECK IF THE WALLIMAGEE HAS BEEN SET-----------

        //222222222222222222222222222222222222222222222
        if (imgPath!=null ){//
            Resobj = new UploadResponsObj();


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            SaveImg = new SaveImageToInternalDirectory(ctx);

            //Get the file name extention
            final String FPATH = imgPath;
            Filename fileToCheck = new Filename(FPATH, '/', '.');
            String imgExtention = fileToCheck.extension();
            String finalExtention = "" ;



                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = decodeFile(imgPath, options);

                //CHECK IF THE IMAGE IS LOCAL OR IT NEED TO BE DOWNLOADED

                if (bitmap == null) {

                    //Get the real Image Path from the Url of the other source Image
                    String realImgPath = imgPath.substring(imgPath.indexOf("http"), imgPath.length());

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



                //Compress the Image to reduce image size to make upload easy
                switch (imgExtention) {
                    case "jpg":
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        finalExtention = imgExtention;
                        BeginLetterExtention = "j";
                        break;
                    case "png":
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        finalExtention = imgExtention;
                        BeginLetterExtention = "p";
                        break;

                    default:
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        finalExtention = "png";
                        BeginLetterExtention = "p";
                        break;
                }
                byte[] byte_arr = stream.toByteArray();

                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);

                //Generate new image name
                SerialGeneratn genNewName = new SerialGeneratn();
                imagename = genNewName.getNewSerialName() ;
                imgnameToSq = imagename + "." + finalExtention ;
                //String imgnameToSq = "";


        }


        //Connect to the WebService and post the Image
        try {
            //POSTING AREA-----------------------BEGIN------------------------------------------------
            //imgnameToSq = Resobj.getUploadedImageName();

            //iiiiiiiiiiiiiiiiiii-------------
            URL ul2 = new URL(databasUrl) ;
            StringBuilder xml2 = new StringBuilder("");
            String s2 = null;
            URL ul = new URL(ur);
            StringBuilder xml = new StringBuilder("");

            if (isFromWallPost){//------------

                try {

                    ul = new URL(ur);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                FormPoster poster2 = new FormPoster(ul2);
                poster2.add("email",dataToBeSent.get("email").toString());
                poster2.add("pw",dataToBeSent.get("pw").toString());
                poster2.add("postId",dataToBeSent.get("postId").toString());
                poster2.add("postTitle",dataToBeSent.get("postTitle").toString());
                poster2.add("imageName",imgnameToSq);//Get ImaageName fro Preference
                poster2.add("videoName",dataToBeSent.get("videoName").toString());
                poster2.add("contentt",dataToBeSent.get("contentt").toString());
                poster2.add("tim",dataToBeSent.get("tim").toString());
                poster2.add("dat",dataToBeSent.get("dat").toString());
                poster2.add("logi",dataToBeSent.get("logi").toString());
                poster2.add("lati",dataToBeSent.get("lati").toString());
                poster2.add("privacy",dataToBeSent.get("privacy").toString());
                poster2.add("likes",dataToBeSent.get("likes").toString() );
                poster2.add("comments",dataToBeSent.get("comments").toString());
                poster2.add("dislikes",dataToBeSent.get("dislikes").toString());

                //Post the Data and get the response
                BufferedReader in2 = new BufferedReader(new InputStreamReader(poster2.post()));
                String current2;

                while ((current2 = (in2.readLine())) != null) {

                    xml2.append(current2);
                }

                in2.close();
                String ss2 = Pattern.compile("&lt;").matcher(xml2).replaceAll("<");
                String  sss2 = Pattern.compile("&gt;").matcher(ss2).replaceAll(">");

                myXml2.append(sss2);

                FormPoster poster = new FormPoster(ul);

                //Generate and Add the query strings
                poster.add("exten", BeginLetterExtention);
                poster.add("imgname", imagename);
                poster.add("encodedImage", encodedString);
                // poster.add("email", emailPref);


                //Post the Data and get the response
                BufferedReader in = new BufferedReader(new InputStreamReader(poster.post()));
                String current;

                while ((current = (in.readLine())) != null) {

                    xml.append(current);
                }

                in.close();
                String s = Pattern.compile("&lt;").matcher(xml).replaceAll("<");
                s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");

                myXml.append(s2);

                try {
                    GetRespDataList ResData = new GetRespDataList();
                    ArrayList<EasyUserReg> theUser = ResData.GetObjectDataLit(myXml);
                    imageUploadResponse = theUser.get(0).respons.toString();
                    if (imageUploadResponse != "" && imageUploadResponse != null) {
                        Resobj = new UploadResponsObj();

                        //Return a flag to indicate successful image upload
                        Resobj.setUploadedImageName(imageUploadResponse);
                        Resobj.setUploadSuccess(true);


                    }
                } catch (Exception e) {

                    //NOTIFY THE USER OF REGISTRATION ERROR
                    Resobj.setUploadedImageName("Please try again later");
                    Resobj.setUploadSuccess(false);
                }


                SharedPreferences usession = ctx.getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = usession.edit();

                    editor.putString("WALLIMAGE1PATH", null);

            }
            else if(isFromProfile){//-------------------------
                ul2 = new URL(databasUrl);
                FormPoster poster2 = new FormPoster(ul2);
                poster2.add("pw", pw); //Get this from the preference
                poster2.add("email", email);
                poster2.add("valu", imgnameToSq);

                //Post the Data and get the response
                BufferedReader in2 = new BufferedReader(new InputStreamReader(poster2.post()));
                String current2;

                while ((current2 = (in2.readLine())) != null) {

                    xml2.append(current2);
                }

                in2.close();
                String ss2 = Pattern.compile("&lt;").matcher(xml2).replaceAll("<");
                String  sss2 = Pattern.compile("&gt;").matcher(ss2).replaceAll(">");

                myXml2.append(sss2);

                FormPoster poster = new FormPoster(ul);

                //Generate and Add the query strings
                poster.add("exten", BeginLetterExtention);
                poster.add("imgname", imagename);
                poster.add("encodedImage", encodedString);
                // poster.add("email", emailPref);


                //Post the Data and get the response
                BufferedReader in = new BufferedReader(new InputStreamReader(poster.post()));
                String current;

                while ((current = (in.readLine())) != null) {

                    xml.append(current);
                }

                in.close();
                String s = Pattern.compile("&lt;").matcher(xml).replaceAll("<");
                s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");

                myXml.append(s2);

                try {
                    GetRespDataList ResData = new GetRespDataList();
                    ArrayList<EasyUserReg> theUser = ResData.GetObjectDataLit(myXml);
                    imageUploadResponse = theUser.get(0).respons.toString();
                    if (imageUploadResponse != "" && imageUploadResponse != null) {
                        Resobj = new UploadResponsObj();

                        //Return a flag to indicate successful image upload
                        Resobj.setUploadedImageName(imageUploadResponse);
                        Resobj.setUploadSuccess(true);


                    }
                } catch (Exception e) {

                    //NOTIFY THE USER OF REGISTRATION ERROR
                    Resobj.setUploadedImageName("Please try again later");
                    Resobj.setUploadSuccess(false);
                }
            }

            else{//-----------------------

            }
            //iiiiiiiiiiiiiiiiii--------------



        } catch (Exception e) {
            e.printStackTrace();
        }
//POSTING AREA-----------------------END------------------------------------------------



        return new ImageObj(bitmap,imgPath,imgnameToSq);
    }

    @Override
    protected void onPostExecute(ImageObj imageObj){

       // PostMsg postMsg = new PostMsg();
        //SaveImg = new SaveImageToInternalDirectory(postMsg.getBaseContext());
  // SaveImg.SaveImg(imageObj.getBitmap(),imageObj.getName());

    }




}
