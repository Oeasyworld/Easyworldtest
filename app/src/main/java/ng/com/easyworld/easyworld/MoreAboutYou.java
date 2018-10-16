package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class MoreAboutYou extends AppCompatActivity {






    private  String  Firstname ;
    private String Email;
    private String pass1;
    private String pass = null;
    private String phone = null;
    private String dateofbirth = null;
    private String occupation = null;
    private String gender = null;


    private EditText mFirstname;
    private EditText mEmail;
    private EditText mPassword1;
    private EditText mPassword;
    private TextView mNotMatched;
    private String RegResponse;
    SharedPreferences userSession;

    private String ServPhone = null;
    private String Servpass = null;
    private String ServEmail = null;
    private  String  Useremail = null;
    private String Userpass = null;
    private String ServPhoto = null;
    private String ServFirstname = null;
    private TextView ErrLabel;


    Boolean SignupFlag = false;

    StringBuilder ResponseXml = new StringBuilder("");
    String URL;

    private EditText MobilePhone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about_you);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        MobilePhone = findViewById(R.id.MobilePhoneTxt);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    public void SignupBtnClicked(View v){


        LoadUserInfo();



                doBackgroundThreadProcessing SignupTask = new doBackgroundThreadProcessing();
                Thread thread = new Thread(SignupTask);
                thread.start();


    }




    private class doBackgroundThreadProcessing implements Runnable {
        @Override
        public void run() {

            StringBuilder xml = new StringBuilder("");


            String dob = "2017-01-01";
            String s2=null;
            URL = String.format("https://www.easyworld.com.ng/ewc.asmx/basicRegistration?fname=%s&email=%s&pwd=%s&pnone=%s&dateofbirth=%s&occupation=%s&gender=%s",Firstname,Email,pass,phone,dob,occupation,gender) ;


            try {
                java.net.URL ul = new URL(URL);

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
                s2 = Pattern.compile("&gt;").matcher(s).replaceAll(">");


                ResponseXml.append(s2);

                //REGISTRATION OBJECT
                try{
                    GetRespDataList ResData = new GetRespDataList();
                    ArrayList<EasyUserReg> newUser =  ResData.GetObjectDataLit(ResponseXml);
                    RegResponse = newUser.get(0).respons.toString();
                }
                catch(Exception e){

                    //NOTIFY THE USER OF REGISTRATION ERROR
                    RegResponse = "Please try again later";
                    ErrLabel.setVisibility(View.VISIBLE);
                    ErrLabel.setText(RegResponse);

                }




                if(RegResponse != "" && RegResponse != null){ //The response contains the userid return from basicregistration procedure


                    //Save the user Phone in SHAREDPREFERENCE
                    userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userSession.edit();
                    editor.putString("Phone", ServPhone);
                   // editor.putString("Photo", ServPhoto);


                    editor.apply();


                    Intent regPage = new Intent(MoreAboutYou.this,Home.class);

                    startActivity(regPage);
                    clientt.disconnect();
                }
                else
                {
                    ErrLabel.setVisibility(View.VISIBLE);
                    ErrLabel.setText(RegResponse);
                    clientt.disconnect();

                }


            } catch (MalformedURLException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            } catch (IOException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            }


        }
    }
    public void LoadUserInfo() {


        try {
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            Map<String, ?> allPreferences = userSession.getAll();

            if (!allPreferences.isEmpty()) {



                Firstname = userSession.getString("Firstname", null);
                Email = userSession.getString("Email", null);
                pass = userSession.getString("Pw", null);
                phone = MobilePhone.getText().toString();

            }

        } catch (Exception e) {
            String err = e.toString();

        }



    }

}
