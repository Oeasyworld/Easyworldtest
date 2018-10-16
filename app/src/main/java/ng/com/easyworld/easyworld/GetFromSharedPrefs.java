package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Oisrael on 7/22/2018.
 */

public class GetFromSharedPrefs {


    private SharedPreferences userSession;
    private Activity activity;
    private String prefName;
    private String label;
    private String val;

    GetFromSharedPrefs ( Activity activity, String prefName){

        this.activity = activity;
        this.prefName = prefName;


    }
    public String GetVal(String label) {
        //Get the value from the SHAREDPREFERENCE

        userSession = activity.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Map<String, ?> allPreferences = userSession.getAll();

        if (!allPreferences.isEmpty()) {
            String  val = userSession.getString(label, null);
            return val;
        }
        return  null;
    }



}
