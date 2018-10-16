package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 9/19/2018.
 */

public class UserToCall {

    private static String UserName;
    UserToCall(String name){

        this.UserName = name;
    }
    UserToCall(){}

    public String getName(){
        return UserName;
    }
}
