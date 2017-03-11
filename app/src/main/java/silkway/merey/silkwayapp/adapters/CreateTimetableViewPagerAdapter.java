package silkway.merey.silkwayapp.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.fragments.TourProgramTimetableFragment;

/**
 * Created by Merey on 04.03.17.
 */

public class CreateTimetableViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
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
        TourProgramTimetableFragment fragment = new TourProgramTimetableFragment();
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

