package ng.com.easyworld.easyworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by oisrael on 29-May-17.
 */

public class EasyImageLoader extends AsyncTask<Void,Void,Bitmap> {

    //IMAGEVIEW TO USE FOR THE DISPLAY
    private ImageView EasyImage;

    //THE URL OF THE IMAGE
    private URL url;

    private Boolean AdapterFlag ;

    EasyImageLoader( ImageView theimage, URL ul){

        url = ul;
        EasyImage=theimage;
        this.AdapterFlag = true;
    }
    @Override
    protected Bitmap doInBackground(Void... strings) {
        Bitmap mIcon = null;
        try {

            HttpURLConnection clientt = (HttpURLConnection) url.openConnection();

            InputStream in = clientt.getInputStream();
            mIcon = BitmapFactory.decodeStream(in);


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap img){


            EasyImage.setImageBitmap(img);

    }
}

