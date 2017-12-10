package jcubed.wakemewhen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * AlarmAdapter for populating MainActivity ListView
 */

public class AlarmAdapter extends BaseAdapter {
    private Context c;
    private LayoutInflater inflater;
    private ArrayList<Alarm> dataSource;

    public AlarmAdapter(Context context, ArrayList<Alarm> items) {
        c = context;
        dataSource = items;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = inflater.inflate(R.layout.list_item_alarm, parent, false);

        return rowView;
    }
}
