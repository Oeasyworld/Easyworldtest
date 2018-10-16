package ng.com.easyworld.easyworld;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Oisrael on 7/22/2018.
 */

public class SaveToSharePref {


    private SharedPreferences userSession;
    private Activity activity;
    private String prefName;

    SaveToSharePref ( Activity activity, String prefName){

        this.activity = activity;
        this.prefName = prefName;

    }

    public void SaveContent(String label, String val) {

        //Save the user info in SHAREDPREFERENCE
        userSession = activity.getSharedPreferences(prefName,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();
        editor.putString(label, val);
        editor.apply();

    }



}
