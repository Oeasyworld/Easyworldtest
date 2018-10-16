package ng.com.easyworld.easyworld;

import android.os.StrictMode;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by oisrael on 30-Apr-17.
 */

public class AuthLogin  {


    GetObjectDataLst getlogData;
    ArrayList<EasyUser> easyUserLogedIn = new ArrayList<>();


    String url;
    String email;
    String password;

    public  AuthLogin(String url, String email,String password){

        this.email = email;
        this.password = password;
        this.url = url;

    }

    public boolean AuthUserlogin(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


       boolean flag = false;


        StringBuilder myXml = new StringBuilder();
        StringBuilder xml = new StringBuilder();
        try {
            URL ul = new URL(url);

            //Connect to the WebService
            HttpURLConnection clientt = (HttpURLConnection) ul.openConnection();


            //Read the data from the client

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientt.getInputStream()));


            String current;

            while ((current=(in.readLine())) != null) {

                xml.append( current) ;
            }




            String s = Pattern.compile("&lt;").matcher(xml).replaceAll("<");
            String s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");

            myXml.append(s2);
            easyUserLogedIn =   getlogData.GetObjectDataList(myXml);
            clientt.disconnect();

        } catch (MalformedURLException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        String emailfroServer =  easyUserLogedIn.get(0).bcemailaddress;

        if (email == emailfroServer ) {
            //SAVE THIS EMAIL IN A SESSION VARIABLE FOR FUTURE USE-------------

            //RETURN TRUE
            flag = true;
        }


        return flag;
    }





}
