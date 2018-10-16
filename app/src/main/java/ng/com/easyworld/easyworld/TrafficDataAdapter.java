package ng.com.easyworld.easyworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Oisrael on 8/14/2018.
 */

public class TrafficDataAdapter extends ArrayAdapter<TrafficQueryData> {
    private ArrayList<TrafficQueryData> dataSet;
    Context mContext;

    private static class ViewHolder2 {
        TextView locationNamee;
        TextView veryFastt;
        TextView movingSlowlyy;
        TextView standStilll;


    }


    public TrafficDataAdapter(ArrayList<TrafficQueryData> data, Context context) {
        super(context, R.layout.trafficreply, data);
        this.dataSet = data;
        this.mContext=context;
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final  TrafficQueryData data = getItem(position);



        // Check if an existing view is being reused, otherwise inflate the view
        final TrafficDataAdapter.ViewHolder2 viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new TrafficDataAdapter.ViewHolder2();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trafficreply, parent, false);
            viewHolder.locationNamee = (TextView) convertView.findViewById(R.id.Txt_Location_Name);
            viewHolder.veryFastt = (TextView) convertView.findViewById(R.id.VeryFreeVal);

            viewHolder.movingSlowlyy = (TextView) convertView.findViewById(R.id.MovingSlowlyVal);
            viewHolder.standStilll = (TextView) convertView.findViewById(R.id.StandstillVal);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TrafficDataAdapter.ViewHolder2) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.locationNamee.setText(data.getLocation()+"   "+data.getMsgId());
        viewHolder.veryFastt.setText(data.getVeryFree());
        viewHolder.movingSlowlyy.setText(data.getMovingSlowly());
        viewHolder.standStilll.setText(data.getStandStill());



        return convertView;
    }


}
