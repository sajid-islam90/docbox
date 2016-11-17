package adapters;

/**
 * Created by romichandra on 16/11/16.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.ConferenceScheduleFragment1;
import fragments.ConferenceScheduleFragment2;
import fragments.ConferenceScheduleFragment3;

public class AssociationConferenceScheduleAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public AssociationConferenceScheduleAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConferenceScheduleFragment1 tab1 = new ConferenceScheduleFragment1();
                return tab1;
            case 1:
                ConferenceScheduleFragment2 tab2 = new ConferenceScheduleFragment2();
                return tab2;
            case 2:
                ConferenceScheduleFragment3 tab3 = new ConferenceScheduleFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
