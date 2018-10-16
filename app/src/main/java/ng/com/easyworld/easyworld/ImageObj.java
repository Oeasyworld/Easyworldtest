package ng.com.easyworld.easyworld;

import android.graphics.Bitmap;

/**
 * Created by oisrael on 29-Dec-17.
 */

public class ImageObj {
    private Bitmap bitmap;
    private String imagePath;
    private String imageName;

    ImageObj (Bitmap bitmap, String imagePath,String imageName){
        this.bitmap = bitmap;
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public String getImagePath(){
        return imagePath;
    }
    public String getImageName(){
        return imageName;
    }
}
