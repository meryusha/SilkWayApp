package silkway.merey.silkwayapp.agent.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.fragments.TourProgramTimetableCreateFragment;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;

/**
 * Created by Merey on 04.03.17.
 */

public class CreateTimetableViewPagerAdapter extends FragmentPagerAdapter {
    private final List<TourProgramTimetableCreateFragment> fragmentList = new ArrayList<>();
    private boolean doNotifyDataSetChangedOnce = false;

    public CreateTimetableViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int num) {
        return fragmentList.get(num);

    }

    @Override
    public int getCount() {
        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }
        return fragmentList.size();
    }

    public void addFragment(TabLayout tabLayout) {
        TourProgramTimetableCreateFragment fragment = TourProgramTimetableCreateFragment.newInstance(fragmentList.size() + 1);
        fragmentList.add(fragment);
        tabLayout.addTab(tabLayout.newTab().setText("День " + fragmentList.size()));
        notifyDataSetChanged();
    }

    public void removeFragment(TabLayout tabLayout) {
        doNotifyDataSetChangedOnce = true;
        Fragment fr = fragmentList.remove(fragmentList.size() - 1);
        if (fr != null) {
            Log.d("Merey", fragmentList.size() + "");
            tabLayout.removeTabAt(fragmentList.size() - 1);
            notifyDataSetChanged();
        }
    }
    //   notifyDataSetChanged();

    public void storeFragmentList(Tour tour) {
        for (int i = 0; i < fragmentList.size(); i++) {
            TourProgramTimetableCreateFragment fragment = fragmentList.get(i);
            List<View> slots = fragment.getSlots();
            for (int j = 0; j < slots.size(); j++) {
                View v = slots.get(j);
                TextView startTextView = (TextView) v.findViewById(R.id.startTimeTextView);
                TextView endTextView = (TextView) v.findViewById(R.id.endTimeTextView);
                String[] start = startTextView.getText().toString().split(":");
                String[] end = endTextView.getText().toString().split(":");
                // Log.d("timetable", Integer.parseInt(start[1]) + " " + Integer.parseInt(end[0]));
                EditText timeDescription = (EditText) v.findViewById(R.id.timeDescriptionEditText);
                TimeInstance instance = new TimeInstance(tour, Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(end[0]), Integer.parseInt(end[1]), timeDescription.getText().toString(), fragment.getDay());
                DataManager.getInstance().saveTime(instance);
            }

        }
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragmentList.indexOf(object);

        if (index == -1) {
            return POSITION_NONE;
        }

        return index;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "День " + (position + 1);
    }
}

