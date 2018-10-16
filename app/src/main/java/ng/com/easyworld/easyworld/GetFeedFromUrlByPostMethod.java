package ng.com.easyworld.easyworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Oisrael on 8/26/2018.
 */

public class GetFeedFromUrlByPostMethod {


    public FeedObject GetFeedFromUrlByPostMethod(String url,String param1,String param2){

        FeedObject FD = new FeedObject();
        StringBuilder myXml2 = new StringBuilder();
        StringBuilder xml2 = new StringBuilder();
        URL urll = null;
        try {
             urll = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FormPoster poster2 = new FormPoster(urll);

        poster2.add("email",param1);
        poster2.add("dat",param2);

        //Post the Data and get the response
        BufferedReader in2 = null;
        try {
            in2 = new BufferedReader(new InputStreamReader(poster2.post()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String current2;

        try {
            while ((current2 = (in2.readLine())) != null) {

                xml2.append(current2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ss2 = Pattern.compile("&lt;").matcher(xml2).replaceAll("<");
        String  sss2 = Pattern.compile("&gt;").matcher(ss2).replaceAll(">");

        myXml2.append(sss2);

        FD.SetMyXml(myXml2);

        return FD;
    }





}
