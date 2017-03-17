package silkway.merey.silkwayapp.agent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import silkway.merey.silkwayapp.classes.Location;

/**
 * Created by Merey on 25.02.17.
 */

public class LocationAdapter extends ArrayAdapter<Location> {
    private Context context;
    private List<Location> locations;

    public LocationAdapter(Context context, int resource, List<Location> locations) {
        super(context, resource, locations);
        this.context = context;
        this.locations = locations;
    }

    public int getCount() {
        return locations.size();
    }

    public Location getItem(int position) {
        return locations.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(locations.get(position).getName());

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(locations.get(position).getName());
        view.setHeight(100);
        return view;
    }
}
