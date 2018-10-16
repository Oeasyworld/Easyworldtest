package ng.com.easyworld.easyworld;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Oisrael on 6/11/2018.
 */

public class BitmapAndImageView {

    private Bitmap bitmap;
    private ImageView imageView;
    BitmapAndImageView(Bitmap b,ImageView i){
        bitmap = b;
        imageView = i;
    }

    public Bitmap getBitmap(){return bitmap;}
    public ImageView getImageView(){return imageView;}

}
