package org.unibl.etf.mr.touristbl;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private EntryDB database;

    public EntryDB getDatabase() {
        return database;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=findViewById(R.id.toolbar);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        mNavigationView=findViewById(R.id.nav_view);
        database=Room.databaseBuilder(getApplicationContext(),
                EntryDB.class, "database").build();
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragment()).commit();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (item.getItemId()){
                    case R.id.nav_news:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, new NewsFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_weather:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, new WeatherFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_map:
                        fragmentManager.beginTransaction().replace(R.id.frame_container,new MapFragment()).addToBackStack(null).commit();
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }
}
