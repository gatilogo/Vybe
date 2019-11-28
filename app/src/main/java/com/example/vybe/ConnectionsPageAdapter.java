package com.example.vybe;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ConnectionsPageAdapter extends FragmentPagerAdapter {

    private Resources resources;

    public ConnectionsPageAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.resources = context.getResources();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FollowersFragment();
        } else {
            return new FollowingFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return resources.getString(R.string.followers);
        } else {
            return resources.getString(R.string.following);
        }
    }
}
