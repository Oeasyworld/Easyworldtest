package ng.com.easyworld.easyworld;

/**
 * Created by oisrael on 25-Dec-17.
 */

public class UploadResponsObj {

    Boolean uploadSuccess;
    String uploadedImageName;

    UploadResponsObj(){
    }

    public void setUploadSuccess(Boolean val){

        uploadSuccess = val;
    }

    public void setUploadedImageName(String val){

        uploadedImageName = val;
    }


    public Boolean getUploadSuccess(){

        return uploadSuccess;
    }
    public String getUploadedImageName(){

        return uploadedImageName;
    }
}
