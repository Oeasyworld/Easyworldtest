package ng.com.easyworld.easyworld;

import android.graphics.Bitmap;

/**
 * Created by Oisrael on 6/10/2018.
 */

public class BitmapDecodeRes {
    private Bitmap bitmap;
    private boolean result;

    BitmapDecodeRes(boolean result, Bitmap bitmap){

        this.result = result;
        this.bitmap = bitmap;

    }

    public boolean getResult(){return result;}
    public Bitmap getBitmap(){return bitmap;}

}
