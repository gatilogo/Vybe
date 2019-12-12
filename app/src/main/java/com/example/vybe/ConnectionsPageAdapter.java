package com.example.vybe;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vybe.Models.User;

public class ConnectionsPageAdapter extends FragmentPagerAdapter {

    private Resources resources;
    private User user;

    public ConnectionsPageAdapter(@NonNull FragmentManager fm, int behavior, Context context, User user) {
        super(fm, behavior);
        this.resources = context.getResources();
        this.user = user;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FollowersFragment(user);
        } else {
            return new FollowingFragment(user);
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
