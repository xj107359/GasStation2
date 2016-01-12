package cn.sopho.destiny.gasstation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by optiplex9020 on 2016/1/12.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String titles[] ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return RecentFragment.newInstance();
//                return SampleFragment.newInstance(position);
            case 1:
                return DealFragment.newInstance();
//                return SampleFragment.newInstance(position);
            case 2:
                return DealFragment.newInstance();
//                return SampleFragment.newInstance(position);
            case 3:
                return PayFragment.newInstance();
//                return SampleFragment.newInstance(position);
//            case 4:
//                return SampleFragment.newInstance(position);
//            case 5:
//                return SampleFragment.newInstance(position);
//            case 6:
//                return SampleFragment.newInstance(position);
//            case 7:
//                return SampleFragment.newInstance(position);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
