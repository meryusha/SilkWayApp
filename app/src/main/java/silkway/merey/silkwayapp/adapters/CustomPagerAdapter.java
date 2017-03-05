package silkway.merey.silkwayapp.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.TimetableDay;

/**
 * Created by Merey on 18.02.17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<TimetableDay> days;
    private ListView listView;

    public CustomPagerAdapter(Context context, ArrayList<TimetableDay> days) {
        this.days = days;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TimetableDay day = days.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_timetable_day, collection, false);
        listView = (ListView) layout.findViewById(R.id.timetableListView);
        listView.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mContext.getResources().getStringArray(R.array.timetable)));
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TimetableDay day = days.get(position);
        return "Day " + day.getNumber();
    }

}

