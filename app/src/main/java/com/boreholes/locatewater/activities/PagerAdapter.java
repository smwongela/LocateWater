package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.boreholes.locatewater.activities.BoreholesFragment;
import com.boreholes.locatewater.activities.DamsFragment;
import com.boreholes.locatewater.activities.LakesFragment;
import com.boreholes.locatewater.activities.RiversFragment;
import com.boreholes.locatewater.activities.SpringsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter{
        private final int mNumberOfTabs;

        public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
            super(fm, numOfTabs);
            this.mNumberOfTabs = numOfTabs;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RiversFragment();
                case 1:
                    return new DamsFragment();
                case 2:
                    return new LakesFragment();
                case 3:
                    return new SpringsFragment();
                case 4:
                    return new BoreholesFragment();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return mNumberOfTabs;
        }
}

