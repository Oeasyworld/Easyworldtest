package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Oisrael on 6/10/2018.
 */

public class ImageManager {
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

    private File cacheDir;
    public ImageManager(Context context) {
        // Make background thread low priority, to avoid affecting UI performance
        //PhotosLoader.setPriority(Thread.NORM_PRIORITY-1);
        // Find the dir to save cached images
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            cacheDir = new File(sdDir,"data/codehenge");
        }
        else
            cacheDir = context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    PhotosLoaderr photoLoaderThread=new PhotosLoaderr();
    final int stub_id=R.drawable.advert;

    public void DisplayImage(String url, Activity activity, ImageView imageView,String PicName)
    {
        if(cache.containsKey(url))
            imageView.setImageBitmap(cache.get(url));
        else
        {
            queuePhoto(url, imageView,PicName);
            imageView.setImageResource(R.drawable.pen);
        }
    }

    private void queuePhoto(String url,  ImageView imageView,String PicName)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView,PicName);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        //String status = photoLoaderThread.getStatus();
        if(photoLoaderThread.getState() == Thread.State.NEW)
        {
            photoLoaderThread.start();
        }
    }



    private Bitmap getBitmap(String url,String PicName)
    {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        //String filename=String.valueOf(url.hashCode());
        //File f=new File(cacheDir, filename);
        String filename = PicName;
        File f=new File(cacheDir,filename);

        //from SD cache

        BitmapDecodeRes response = decodeFile(f);

        if(response.getResult()) {
            return response.getBitmap();
        }
        else{
            //from web
            try {
                Bitmap bitmap=null;
                // InputStream is=new URL(url).openStream();
                // OutputStream os = new FileOutputStream(f);

                URL ul = new URL(url);
                HttpURLConnection clientt = (HttpURLConnection) ul.openConnection();
                InputStream is = clientt.getInputStream();

                OutputStream os = new FileOutputStream(f);

                //Utils.CopyStream(is, os);
                copy(is,os);

                os.close();
                 response = decodeFile(f);


            } catch (Exception ex){
                ex.printStackTrace();

            }
        }

        return response.getBitmap();
    }

    //decodes image and scales it to reduce memory consumption
    private BitmapDecodeRes decodeFile(File f){
        Bitmap bitmap = null;
        boolean flag;
        try {

            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;

            bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            flag = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            flag=false;

        }

        return new BitmapDecodeRes(flag,bitmap);
    }


    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public String PicName;
        public PhotoToLoad(String u, ImageView i,String PicName){
            this.url   = u;
            this.imageView  = i;
            this.PicName = PicName;
        }





    }

    PhotosQueue photosQueue=new PhotosQueue();

    public void stopThread()
    {
        try {
            photoLoaderThread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }



    class PhotosLoaderr extends Thread {
        public void run() {
            BitmapAndImageView bitmapAndImageView = null;
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url,photoToLoad.PicName);
                        if (bmp!=null){
                            cache.put(photoToLoad.url, bmp);
                            Object tag=photoToLoad.imageView.getTag();
                            if(tag!=null && ((String)tag).equals(photoToLoad.url)){

                               // bitmapAndImageView = new BitmapAndImageView(bmp,photoToLoad.imageView);
                                FFeed f = new FFeed();
                                f.BitmapDisplayer(bmp,photoToLoad.imageView);

                            }
                        }



                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }

/*
   public class PhotosLoaderr extends AsyncTask<Void,Void,BitmapAndImageView>{

       @Override
       protected BitmapAndImageView doInBackground(Void... voids) {
           BitmapAndImageView bitmapAndImageView = null;
           try {

               while(true)
               {
                   //thread waits until there are any images to load in the queue
                   if(photosQueue.photosToLoad.size()==0)
                       synchronized(photosQueue.photosToLoad){
                           photosQueue.photosToLoad.wait();
                       }
                   if(photosQueue.photosToLoad.size()!=0)
                   {
                       PhotoToLoad photoToLoad;
                       synchronized(photosQueue.photosToLoad){
                           photoToLoad=photosQueue.photosToLoad.pop();
                       }
                       Bitmap bmp=getBitmap(photoToLoad.url,photoToLoad.PicName);
                       if (bmp!=null){
                           cache.put(photoToLoad.url, bmp);
                           Object tag=photoToLoad.imageView.getTag();
                          // if(tag!=null && ((String)tag).equals(photoToLoad.url)){


                              // FFeed.BitmapDisplayer bd=new FFeed.BitmapDisplayer(bmp, photoToLoad.imageView);

                               //SAVE THE BITMAP AND THE IMAGEVIEW AND RETURN IT
                               bitmapAndImageView = new BitmapAndImageView(bmp,photoToLoad.imageView);

                          // }
                       }



                   }
                   if(Thread.interrupted())
                       break;
               }
           } catch (InterruptedException e) {
               //allow thread to exit
           }
           return bitmapAndImageView;
       }

       @Override
       protected void onPostExecute(BitmapAndImageView bitmapAndImageView){

           FFeed f = new FFeed();

          f.BitmapDisplayer(bitmapAndImageView.getBitmap(),bitmapAndImageView.getImageView());
       }
   }
*/






    public void clearCache() {
        //clear memory cache
        cache.clear();

        //clear SD cache
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }


    public static void copy(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1)
                break;
            out.write(buffer, 0, bytesRead);
        }
    }



















}
