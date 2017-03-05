package silkway.merey.silkwayapp.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.fragments.TourProgramTimetableFragment;

/**
 * Created by Merey on 04.03.17.
 */

public class CreateTimetableViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public CreateTimetableViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int num) {
        return fragmentList.get(num);

    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(TabLayout tabLayout) {
        TourProgramTimetableFragment fragment = new TourProgramTimetableFragment();
        fragmentList.add(fragment);
        tabLayout.addTab(tabLayout.newTab().setText("День " + fragmentList.size()));
        notifyDataSetChanged();
        // fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "День " + (position + 1);
    }
}

