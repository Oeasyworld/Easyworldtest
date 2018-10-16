package ng.com.easyworld.easyworld;

import android.accounts.NetworkErrorException;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by oisrael on 11-Apr-17.
 */

public class GetFeedFromUrl {


    public FeedObject GetFeedFromURL(String url){


        FeedObject FD = new FeedObject();
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

            clientt.disconnect();

            FD.SetMyXml(myXml);
        } catch (MalformedURLException e) {

            FD.SetErr(false);
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        } catch (IOException e) {
            FD.SetErr(false);
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }





        return FD;
    }



}
