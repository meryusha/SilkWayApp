package silkway.merey.silkwayapp.agent.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import silkway.merey.silkwayapp.agent.fragments.TourProgramTimetableFragment;
import silkway.merey.silkwayapp.classes.TimeInstance;


/**
 * Created by Merey on 12.03.17.
 */

public class TimetableTabsAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private List<TimeInstance> slots;


    public TimetableTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int num) {
        return fragmentList.get(num);

    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(TabLayout tabLayout, List<TimeInstance> slots) {
        TourProgramTimetableFragment fragment = TourProgramTimetableFragment.newInstance(slots);
        fragmentList.add(fragment);
        tabLayout.addTab(tabLayout.newTab().setText("День " + fragmentList.size()));
        notifyDataSetChanged();
    }

    public Map<Integer, List<TimeInstance>> allocate(List<TimeInstance> slots) {
        Map<Integer, List<TimeInstance>> slotsMap = new HashMap<>();
        for (int i = 0; i < slots.size(); i++) {
            TimeInstance timeInstance = slots.get(i);

            if (slotsMap.containsKey(timeInstance.getDay())) {
                slotsMap.get(timeInstance.getDay()).add(timeInstance);
            } else {
                List<TimeInstance> newSlots = new ArrayList<TimeInstance>();
                newSlots.add(timeInstance);
                slotsMap.put(timeInstance.getDay(), newSlots);
            }
        }
        return slotsMap;
    }

    public void buildTimetable(List<TimeInstance> slots, TabLayout tabLayout) {
        Map<Integer, List<TimeInstance>> slotsMap = allocate(slots);
        Log.d("MAP is here", slotsMap.size() + "");
        for (int i =  1; i <= slotsMap.size(); i++) {
            if (!slotsMap.containsKey(i)) {
                break;
            }

            List<TimeInstance> daySlots = slotsMap.get(i);
            addFragment(tabLayout, daySlots);
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


