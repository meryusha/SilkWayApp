package silkway.merey.silkwayapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Merey on 25.02.17.
 */

public class LocationAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> locationList;

    public LocationAdapter(Context context, int resource, List<String> locationList) {
        super(context, resource, locationList);
        this.context = context;
        this.locationList = locationList;
    }

    public int getCount() {
        return locationList.size();
    }

    public String getItem(int position) {
        return locationList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(locationList.get(position));

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(locationList.get(position));
        view.setHeight(100);
        return view;
    }
}
