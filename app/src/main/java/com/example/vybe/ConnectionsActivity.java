package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ConnectionsActivity extends AppCompatActivity {

    private TabLayout connectionsTabLayout;
    private ViewPager connectionsViewPager;

    private ConnectionsPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        connectionsTabLayout = findViewById(R.id.connections_tab_layout);
        connectionsViewPager = findViewById(R.id.connections_view_pager);

        pageAdapter = new ConnectionsPageAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this);
        connectionsViewPager.setAdapter(pageAdapter);
        connectionsTabLayout.setupWithViewPager(connectionsViewPager);
    }
}
