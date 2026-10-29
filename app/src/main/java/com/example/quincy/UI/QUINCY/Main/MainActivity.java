package com.example.quincy.UI.QUINCY.Main; // Sesuaikan dengan nama package project kamu

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quincy.R;
import com.example.quincy.UI.QUINCY.Favorite.FragmentFavorite;
import com.example.quincy.UI.QUINCY.History.FragmentHistory;
import com.example.quincy.UI.QUINCY.Home.FragmentHome;
import com.example.quincy.UI.QUINCY.Search.FragmentSearch;
import com.example.quincy.UI.QUINCY.Setting.FragmentSetting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // 1. Tampilkan HomeFragment secara default saat pertama kali dibuka
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentHome())
                    .commit();
        }

        // 2. Set listener saat menu bottom navigation diklik
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new FragmentHome();
                } else if (itemId == R.id.nav_search) {
                    selectedFragment = new FragmentSearch();
                } else if (itemId == R.id.nav_favorite) {
                    selectedFragment = new FragmentFavorite();
                } else if (itemId == R.id.nav_history) {
                    selectedFragment = new FragmentHistory();
                } else if (itemId == R.id.nav_settings) {
                    selectedFragment = new FragmentSetting();
                }

                // Ganti isi fragment_container dengan fragment yang dipilih
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}