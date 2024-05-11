package com.example.practika6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ActionBar");

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_bar);

        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            viewPager.setCurrentItem(0);
                            actionBar.setTitle("Домашняя страничка");
                            return true;
                        } else if (itemId == R.id.notification) {
                            viewPager.setCurrentItem(1);
                            actionBar.setTitle("Огурчики");
                            return true;
                        } else if (itemId == R.id.settings) {
                            viewPager.setCurrentItem(2);
                            actionBar.setTitle("Настройки");
                            return true;
                        }
                        return false;
                    }
                });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.notification);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.settings);
                        break;
                }
            }
        });
    }

    public void backtoMenu(View view){ finish();}

    private static class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        ViewPagerFragmentAdapter(SecondActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new FirstFragment();
                case 2:
                    return new SecondFragment();
            }
            return new HomeFragment();
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}