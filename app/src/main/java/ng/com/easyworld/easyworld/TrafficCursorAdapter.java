package ng.com.easyworld.easyworld;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Oisrael on 8/4/2018.
 */

public class TrafficCursorAdapter  extends CursorAdapter {



    public TrafficCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.trafficreply, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView locationNamee = (TextView) view.findViewById(R.id.Txt_Location_Name);
        TextView veryFastt = (TextView) view.findViewById(R.id.VeryFreeVal);
        TextView movingSlowlyy = (TextView) view.findViewById(R.id.MovingSlowlyVal);
        TextView standStilll = (TextView) view.findViewById(R.id.StandstillVal);

        cursor.moveToPosition(-1);
        while ( cursor.moveToNext() ){

            String locationName = cursor.getString( cursor.getColumnIndex("location") );
            String veryFast = cursor.getString( cursor.getColumnIndex("veryfree") );
            String movingSlowly = cursor.getString( cursor.getColumnIndex("movingslowly") );
            String standStill = cursor.getString( cursor.getColumnIndex("standstill") );

            locationNamee.setText(locationName);
            veryFastt.setText(veryFast);
            movingSlowlyy.setText(movingSlowly);
            standStilll.setText(standStill);
        }





    }
}
