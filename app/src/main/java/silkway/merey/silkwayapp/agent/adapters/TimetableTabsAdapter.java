package silkway.merey.silkwayapp.agent.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.agent.fragments.TourProgramTimetableFragment;


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


