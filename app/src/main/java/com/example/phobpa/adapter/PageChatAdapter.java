package com.example.phobpa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.phobpa.fragment.ChatsFragment;
import com.example.phobpa.fragment.Sub1Fragment;
import com.example.phobpa.fragment.Sub2Fragment;
import com.example.phobpa.fragment.UsersFragment;

public class PageChatAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PageChatAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ChatsFragment();
            case 1:
                return new UsersFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}