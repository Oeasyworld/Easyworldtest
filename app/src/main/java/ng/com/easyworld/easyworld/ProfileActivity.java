package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    Bitmap bitmap;
    ProgressDialog prgDialog;
    SharedPreferences userSession;
    String destinationUrl = String.format("https://www.easyworld.com.ng/ewc.asmx/upldImage?");
    String databasUrl = String.format("https://www.easyworld.com.ng/ewc.asmx/UpdatePhoto?");
    // RequestParams params = new RequestParams();
    String imgPath, fileName,emailPref,pwPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void BtnPhotoCameraClicked(View v){

        ChooseImage();
    }

    public void ChooseImage() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data)
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


                    //Get the password and email from the prefference
                    userSession = getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
                    Map<String, ?> allPreferences = userSession.getAll();

                    if (!allPreferences.isEmpty()){
                        emailPref = userSession.getString("Email", null);
                        pwPref = userSession.getString("Pw", null);
                    }


                    SaveImageToInternalDirectory SaveImg = new SaveImageToInternalDirectory(getBaseContext());


                    encodeImageToStringThenPost ImageUploadTask = new encodeImageToStringThenPost(getBaseContext(), destinationUrl,databasUrl,imgPath,emailPref,pwPref,true);
                    ImageUploadTask.execute();


                } else {
                    bitmap = null;
                }

            }
            else
            {

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





}
