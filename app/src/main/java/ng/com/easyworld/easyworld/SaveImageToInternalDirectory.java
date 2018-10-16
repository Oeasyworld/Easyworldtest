package ng.com.easyworld.easyworld;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Oisrael on 5/28/2018.
 */

public class SaveImageToInternalDirectory {

    private Context ctx;
    SaveImageToInternalDirectory(Context ctx){
        this.ctx = ctx;
    }

    public void SaveImg( Bitmap bitmap, String name){

        File directory = new File("/sdcard/Easyworldd/");

        if(!directory.exists()){

            directory.mkdirs();
        }


        //Generate new image name
        SerialGeneratn genNewName = new SerialGeneratn();
        String imagename = genNewName.getNewSerialName() ;
        String imgnameToSq = imagename + "." + "jpg" ;
        //String imgnameToSq = "";


        // String filename = "M/Myfile.jpg";
        FileOutputStream fout ;


        try {
            // path to /data/data/yourapp/app_data/imageDir
            // File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            // Create imageDir
            File mypath=new File(directory,imgnameToSq);

            fout = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fout);

            fout.flush();

            fout.close();

            //Update the Profile Image Name in the Shared Preference
        //  SharedPreferences  userSession =ctx.getSharedPreferences("MYPREFS", Activity.MODE_PRIVATE);
         //   SharedPreferences.Editor editor = userSession.edit();

         //   editor.putString("Photo", name);


          //  editor.apply();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
