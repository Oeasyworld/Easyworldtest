package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 5/1/2018.
 */

public class EasyFeedMessage {

    String postId;
    String postTitle;
    String image;
    String video;
    String content;
    String emailaddress;
    String timee;
    String datee;
    String longi;
    String lati;
    String privacy;
    String likes;
    String comments;
    String dislikes;
    String sendername;
    String photo;


    public EasyFeedMessage(String postId, String postTitle, String image,
                           String video, String content, String emailaddress, String timee, String datee,
                           String longi, String lati, String privacy, String likes, String comments, String dislikes, String sendername, String photo) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.image = image;
        this.video = video;
        this.content = content;
        this.emailaddress = emailaddress;
        this.timee = timee;
        this.datee = datee;
        this.longi = longi;
        this.lati = lati;
        this.privacy = privacy;
        this.likes = likes;
        this.comments = comments;
        this.dislikes = dislikes;
        this.sendername = sendername;
        this.photo = photo;
    }

    //Setters
    public void setPostId(String val) {
        postId = val;
    }

    public void setPostTitle(String val) {
        postTitle = val;
    }

    public void setImage(String val) {
        image = val;
    }

    public void setVideo(String val) {
        video = val;
    }

    public void setContent(String val) {
        content = val;
    }

    public void setSenderName(String val) {
        emailaddress = val;
    }

    public void setTime(String val) {
        timee = val;
    }

    public void setDate(String val) {
        datee = val;
    }

    public void setLongi(String val) {
        longi = val;
    }

    public void setLati(String val) {
        lati = val;
    }

    public void setPrivacy(String val) {
        privacy = val;
    }

    public void setLikes(String val) {
        likes = val;
    }

    public void setComments(String val) {
        comments = val;
    }

    public void setDislikes(String val) {
        dislikes = val;
    }

    public void setSendername(String val) {
        sendername = val;
    }

    public void setPhoto(String val) {
        photo = val;
    }


    //Getters
    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return postTitle;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getContent() {
        return content;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getTime() {
        return timee;
    }

    public String getDate() {
        return datee;
    }

    public String getLongi() {
        return longi;
    }

    public String getLati() {
        return lati;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getLikes() {
        return likes;
    }

    public String getComment() {
        return comments;
    }

    public String getDislikes() {
        return dislikes;
    }

    public String getSenderName() {
        return sendername;
    }

    public String getPhoto() {
        return photo;
    }
}