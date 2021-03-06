package com.boreholes.locatewater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boreholes.locatewater.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SourceListActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(SourceListActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setToolbarTitle("");
        //create an instance of the tab layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_4));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_5));
        //LET THE TAN FILL THE ENTIRE SCREEN LAYOUT
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //use a page adapter to manage the views the screens
        final ViewPager myViewPager = findViewById(R.id.view_pager);
        final PagerAdapter myPageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        myViewPager.setAdapter(myPageAdapter);
        //SET ON CLICK LISTENERS
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            //  mAuth.signOut();
            mAuth.signOut();
            Intent logouIntent = new Intent(SourceListActivity.this, LoginActivity.class);
            logouIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logouIntent);
            finish();
        }

        if (id == R.id.home_page) {
            //  mAuth.signOut();

            Intent mainIntent = new Intent(SourceListActivity.this, MainActivity.class);

            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }
}
