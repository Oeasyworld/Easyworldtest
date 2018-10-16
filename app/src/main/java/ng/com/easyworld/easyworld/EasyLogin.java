package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by oisrael on 29-Apr-17.
 */

public class EasyLogin extends AppCompatActivity {


    private AuthLogin authlogin;

    private Button mLogin;
    private EditText mEmail;
    private EditText mPassword;
    static String URL;
    private static StringBuilder myXml = new StringBuilder("");


    private  String  Useremail = "";
    private String Userpass = "";

    //Data from the server
    private String ServPhoto;
    private String ServUsername;
    private String ServPhone;
    private String Servpass;
    private String ServFirstname;
    private String ServLastname;
    private String ServOldname;
    private String ServEmail;


    private boolean authLoginFlag = false;
    private boolean objDataFlag = false;
    ArrayList<EasyUserProfile> easyUsers;
    SharedPreferences userSession;
   private TextView LoginError;


    StringBuilder xmll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.easylogin);





        //FIND ALL THE CONTROLS---------------------------


        mEmail = (EditText) findViewById((R.id.EmailText));
        mPassword = (EditText) findViewById(R.id.PasswordTxt);


        try {
            userSession =  getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);

            String emailPref ;
            String pwPref ;



            Map<String, ?> allPreferences = userSession.getAll();


            if (!allPreferences.isEmpty()){
                emailPref = userSession.getString("Email", null);
                pwPref = userSession.getString("Pw", null);

                if(emailPref != null && pwPref != null){

                    Intent loginPage = new Intent(EasyLogin.this, Home.class);

                    startActivity(loginPage);
                }

            }

        }
        catch(Exception e){
            String err = e.toString();

        }


        //Show Error message to the user
        LoginError =(TextView) findViewById(R.id.LoginErrorTxt);

        final Button Signup = (Button) findViewById(R.id.CreateAccount);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent regPage = new Intent(EasyLogin.this,EasySignup.class);

                startActivity(regPage);

            }
        });


        Button Loginn = (Button) findViewById(R.id.loginn);
        Loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Useremail = mEmail.getText().toString();
                Userpass = mPassword.getText().toString();


                doBackgroundThreadProcessing backTask = new doBackgroundThreadProcessing();
                Thread thread = new Thread(backTask);
                thread.start();

            }
        });


    }

    public class GetObjectDataListAsyc extends AsyncTask<StringBuilder,Integer,Boolean>{

        Boolean flag = false;
        @Override
        protected Boolean doInBackground(StringBuilder... params) {

            GetProfileObjectDataLst objDataList = new GetProfileObjectDataLst();
            try {
              easyUsers =  objDataList.GetObjectDataList(myXml);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!easyUsers.isEmpty()){

                ServEmail = easyUsers.get(0).emailaddress.toString();
                Servpass = easyUsers.get(0).kokoro.toString();
                ServUsername = easyUsers.get(0).uname.toString();
                ServPhone = easyUsers.get(0).phone.toString();
                ServPhoto = easyUsers.get(0).photo.toString();
                ServFirstname = easyUsers.get(0).fname.toString();
                ServLastname = easyUsers.get(0).lname.toString();
                ServOldname = easyUsers.get(0).oldname.toString();


                if (Useremail.equals(ServEmail) && (Servpass != null && Servpass != "" ) ){

                    //Save the user info in SHAREDPREFERENCE
                    userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userSession.edit();
                    editor.putString("Email", ServEmail);
                    editor.putString("Pw", Servpass);//Userpass
                    editor.putString("Username", ServUsername);
                    editor.putString("Phone", ServPhone);
                    editor.putString("Photo", ServPhoto);
                    editor.putString("Firstname", ServFirstname);
                    editor.putString("Lastname", ServLastname);
                    editor.putString("Oldname", ServOldname);

                    editor.apply();



                    flag = true;
                }

            }

            return flag;
        }


        @Override
        protected void onProgressUpdate(Integer... params){



        }

        @Override
        protected void onPostExecute(Boolean val){




            if(val){
                Intent loginPage = new Intent(EasyLogin.this,Home.class);

                startActivity(loginPage);
            }else{

                LoginError.setVisibility(View.VISIBLE);

            }


        }

    }

    private class doBackgroundThreadProcessing implements Runnable {
        @Override
        public void run() {
            StringBuilder xml = new StringBuilder("");

            String s2=null;
            URL = String.format("https://www.easyworld.com.ng/ewc.asmx/getUserInfo?uname=%s&pwd=%s",Useremail,Userpass) ;


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


                myXml.append(s2);


                    GetObjectDataListAsyc objDataList = new GetObjectDataListAsyc();
                    objDataList.execute(myXml);




                in.close();
                clientt.disconnect();

            } catch (MalformedURLException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            } catch (IOException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            }




        }
    }







}
