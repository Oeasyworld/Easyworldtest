package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.xwalk.core.XWalkView;

import java.util.Map;

public class EasyVideoCallView extends AppCompatActivity {

    private XWalkView xWalkWebView;

    private WebView browser;
    private ProgressDialog progressDialog;

    private String url;

    private String device = "Android";
    private String UsernameToCal;
    private String name;
    private String ilekun;

    //Share Preference
    private SharedPreferences userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_video_call_view);

        LoadUserInfo();
       // String uri =  String.format("https://www.easyworld.com.ng/Easy Mobile.aspx?device=%s&UsernameToCal=%s&name=%s&i=%s&o=%s",device,UsernameToCal,name,ilekun,"open");
       // url = String.format("https://www.easyworld.com.ng/Easy Mobile.aspx?device=%s&UsernameToCal=%s&name=%s&ilekun=%s",device,UsernameToCal,name,ilekun);


        //find the WebView by name in the main.xml of step 2
        //browser=(WebView)findViewById(R.id.WebViewDisplay);

         //Load the webpage
       // browser.loadUrl(uri);


       Uri uri = Uri.parse( String.format("https://www.easyworld.com.ng/Easy Mobile.aspx?device=%s&UsernameToCal=%s&name=%s&i=%s&o=%s",device,UsernameToCal,name,ilekun,"open"));
       Intent intent = new Intent(Intent.ACTION_VIEW, uri);

       startActivity(intent);



    }
    public void LoadUserInfo() {


        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                name = userSession.getString("Email", null);
                ilekun = userSession.getString("Pw", null);

            }

        } catch (Exception e) {
            String err = e.toString();

        }

        try {
            userSession = getSharedPreferences("MYCURRENTCHAT", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {

                UsernameToCal = userSession.getString("selUserEmail", null);
                //Callee.setText(userSession.getString("selUserEmail", null));

            }

        } catch (Exception e) {
            String err = e.toString();

        }

    }
}
