package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EasySignup extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easysignup);

        mFirstname=(EditText) findViewById(R.id.FirstnameText);
        mEmail=(EditText) findViewById(R.id.EmailText);
        mPassword1=(EditText) findViewById(R.id.PasswordTxt1);
        mPassword=(EditText) findViewById(R.id.PasswordTxt);
        mNotMatched = (TextView) findViewById(R.id.NotMatched);
        ErrLabel = (TextView) findViewById(R.id.ErrLabel);
    }




    public void SignupBtnClicked(View v){


       Firstname  = mFirstname.getText().toString();
        Email = mEmail.getText().toString();
        pass1 = mPassword1.getText().toString();
        pass = mPassword.getText().toString();


        if (!Firstname.equals("") && !Email.equals("")){

            if (!pass1.equals("") && !pass1.equals(pass) ){
                mNotMatched.setVisibility(View.VISIBLE);

            }
            else{

                //Save the Data and move to next page--------
                doBackgroundThreadProcessing SaveDateTask = new doBackgroundThreadProcessing();
                Thread thread = new Thread(SaveDateTask);
                thread.start();



            }


        }

    }




    private class doBackgroundThreadProcessing implements Runnable {
        @Override
        public void run() {

            mFirstname=(EditText) findViewById(R.id.FirstnameText);
            mEmail=(EditText) findViewById(R.id.EmailText);
            mPassword1=(EditText) findViewById(R.id.PasswordTxt1);
            mPassword=(EditText) findViewById(R.id.PasswordTxt);

            ServFirstname  = mFirstname.getText().toString();
            ServEmail = mEmail.getText().toString();
            pass1 = mPassword1.getText().toString();
            Servpass = mPassword.getText().toString();


            //Save the user info in SHAREDPREFERENCE
            userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = userSession.edit();
            editor.putString("Email", ServEmail);
            editor.putString("Pw", Servpass);//Save the (RegResponse) userid as the pw
            editor.putString("Firstname", ServFirstname);
            editor.putString("Phone", ServPhone);
            editor.putString("Photo", ServPhoto);


            editor.apply();


            Intent regPage = new Intent(EasySignup.this,MoreAboutYou.class);

            startActivity(regPage);

        }
    }


}
