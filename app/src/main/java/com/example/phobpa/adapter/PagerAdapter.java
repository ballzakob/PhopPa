package com.example.phobpa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.phobpa.fragment.Sub1Fragment;
import com.example.phobpa.fragment.Sub2Fragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Sub1Fragment();
            case 1:
                return new Sub2Fragment();
            default:
                return null;
        }
    }
     

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}