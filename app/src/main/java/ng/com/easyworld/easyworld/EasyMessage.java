package ng.com.easyworld.easyworld;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by Oisrael on 1/22/2018.
 */

public class EasyMessage {

    String msgId;
    String myMessage;
    String othersMessage;
    String myImages;
    String othersImages;
    String myVideos;
    String othersVideos;
    String dateTimeTxt;

    public EasyMessage() {}

    //Setters
    public void setMsgId(String val) {
        msgId = val;
    }
    public void setMyMessage(String val) {
        myMessage = val;
    }
    public void setOthersMessage(String val) {
        othersMessage = val;
    }
    public void setMyImages(String val) {
        myImages = val;
    }
    public void setOthersImages(String val) {
        othersImages = val;
    }
    public void setMyVideos(String val) {
        myVideos = val;
    }
    public void setOthersVideos(String val) {
        othersVideos = val;
    }
    public void setDateTimeTxt(String val) {
        dateTimeTxt = val;
    }


    //Getters
    public String getMsgId() {
        return msgId;
    }

    public String getMyMessage() {
        return myMessage;
    }

    public String getOthersMessage() {
        return othersMessage;
    }

    public String getMyImages() {
        return myImages;
    }

    public String getOthersImages() {
        return othersImages;
    }

    public String getMyVideos() {
        return myVideos;
    }

    public String getOthersVideos() {
        return othersVideos;
    }

    public String getDateTimeTxt() {
        return dateTimeTxt;
    }

}
