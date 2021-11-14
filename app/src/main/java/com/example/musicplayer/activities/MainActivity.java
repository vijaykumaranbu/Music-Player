package com.example.musicplayer.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewStateAdapter;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.utilities.Constants;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewStateAdapter viewStateAdapter = new ViewStateAdapter(getSupportFragmentManager(),getLifecycle());
        binding.viewPager.setAdapter(viewStateAdapter);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.Tracks));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.albums));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.Artists));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.Folders));
        // Connecting tabLayout to adapter
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // Change tab when swapping
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }
}