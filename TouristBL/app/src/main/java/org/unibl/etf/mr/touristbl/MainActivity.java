package org.unibl.etf.mr.touristbl;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;


import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.unibl.etf.mr.touristbl.fragment.EventFragment;

import org.unibl.etf.mr.touristbl.fragment.FavoriteFragment;
import org.unibl.etf.mr.touristbl.fragment.HomeFragment;
import org.unibl.etf.mr.touristbl.fragment.HotelFragment;
import org.unibl.etf.mr.touristbl.fragment.InstitutionFragment;
import org.unibl.etf.mr.touristbl.fragment.MapFragment;
import org.unibl.etf.mr.touristbl.fragment.NewsFragment;
import org.unibl.etf.mr.touristbl.fragment.SightseeFragment;
import org.unibl.etf.mr.touristbl.fragment.WeatherFragment;
import org.unibl.etf.mr.touristbl.model.Entry;
import org.unibl.etf.mr.touristbl.model.NewsDetails;
import org.unibl.etf.mr.touristbl.util.EntryDAO;
import org.unibl.etf.mr.touristbl.util.EntryDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private static EntryDB entryDB;
    private ImageButton settingButton;
    private static CircularFifoQueue<NewsDetails> newsCache;
    private static Menu mainMenu;

    public static void setNewsCache(CircularFifoQueue<NewsDetails> newsCache) {
        MainActivity.newsCache = newsCache;
    }

    public static CircularFifoQueue<NewsDetails> getNewsCache() {
        return newsCache;
    }

    public static EntryDB getEntryDB() {
        return entryDB;
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

    private void selectLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        entryDB=Room.databaseBuilder(getApplicationContext(), EntryDB.class, "tourist-db").build();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        selectLanguage(PreferenceManager.getDefaultSharedPreferences(this).getString("language_list",""));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                fillData();
                return null;
            }
        }.execute();
        setContentView(R.layout.activity_main);
        mToolbar=findViewById(R.id.toolbar);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        mNavigationView=findViewById(R.id.nav_view);
        settingButton=findViewById(R.id.setting_button);
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });




        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragment()).commit();
        findViewById(R.id.search_entry).setVisibility(View.GONE);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof HomeFragment))
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new HomeFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_news:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof NewsFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new NewsFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_institutions:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof InstitutionFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new InstitutionFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_sightsee:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof SightseeFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new SightseeFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_hotels:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof HotelFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new HotelFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_events:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof EventFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container,new EventFragment() ).addToBackStack(null).commit();
                        break;
                    case R.id.nav_weather:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof WeatherFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.frame_container, new WeatherFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_map:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof MapFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new MapFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_fav:
                        if (!(fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1) instanceof FavoriteFragment))

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new FavoriteFragment()).addToBackStack(null).commit();
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void fillData() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstrun", true)) {
            fillHotels();
            fillInstitutions();
            fillLandmarks();
            fillEvents();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstrun", false).apply();
        }
    }

    private void fillHotels() {
        String magic="HOTEL";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Hotel Bosna");
        entry1.setDescription("Elitni hotel");
        entry1.setLat(44.7731236);
        entry1.setLng(17.1921137);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Motel Nana");
        entry2.setDescription("Najbolji motel u gradu");
        entry2.setLat(44.8054147);
        entry2.setLng(17.1988231);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Hotel Jelena");
        entry3.setDescription("Hotel jelena bla bla.");
        entry3.setLat(44.7764062);
        entry3.setLng(17.1865617);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillInstitutions(){
        String magic="INSTITUTION";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Narodna Skupština Republike Srpske");
        entry1.setDescription("Bivši dom JNA.");
        entry1.setLat(44.7746068);
        entry1.setLng(17.1932777);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Klinički centar RS - Paprikovac");
        entry2.setDescription("Bolnica");
        entry2.setLat(44.7782103);
        entry2.setLng(17.1838894);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Muzej Republike Srpske");
        entry3.setDescription("Muzej.");
        entry3.setLat(44.7691033);
        entry3.setLng(17.1914287);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillLandmarks(){
        String magic="SIGHTSEE";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Tvrđava Kastel");
        entry1.setDescription("Tvrđava na obali Vrbasa.");
        entry1.setLat(44.7666);
        entry1.setLng(17.1907);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Banski dvor");
        entry2.setDescription("Banski dvor napravljen kada je Banja Luka bila centar Vrbaske banovine.");
        entry2.setLat(44.7731);
        entry2.setLng(17.1924);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Banj brdo");
        entry3.setDescription("Spomenik palim borcima u Drugom svjetskom ratu.");
        entry3.setLat(44.7436879);
        entry3.setLng(17.1633191);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }
    private void fillEvents(){
        String magic="EVENT";
        Entry entry1=new Entry();
        entry1.setCategory(magic);
        entry1.setTitle("Svečano otvaranje Paviljona 1 - 10.03.2021.");
        entry1.setDescription("Ministar prosvjete otvorio paviljon 1. Novi direktor doma se zahvalio državi na dobro odrađenom poslu.");
        entry1.setLat(44.765755);
        entry1.setLng(17.2003373);
        Entry entry2=new Entry();
        entry2.setCategory(magic);
        entry2.setTitle("Koncert Željka Joksimovića - 01.01.2019.");
        entry2.setDescription("Novogodišnji koncert.");
        entry2.setLat(44.7697388);
        entry2.setLng(17.1898314);
        Entry entry3=new Entry();
        entry3.setCategory(magic);
        entry3.setTitle("Velika žurka ETF-a Club Opium - 27.12.2018.");
        entry3.setDescription("Najveća žurka dosad.");
        entry3.setLat(44.7729542);
        entry3.setLng(17.1909932);
        entryDB.entryDAO().insertAll(entry1,entry2,entry3);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cache_switch",false)) {
            try {
                FileInputStream fin = openFileInput("cache");
                ObjectInputStream oin = new ObjectInputStream(fin);
                newsCache = (CircularFifoQueue<NewsDetails>) oin.readObject();

                oin.close();
            } catch (IOException | ClassNotFoundException e) {
                newsCache = new CircularFifoQueue<>(20);
            }
        }


    }
    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    protected void onStop() {

        super.onStop();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cache_switch",false)){
            try {
                FileOutputStream fin= openFileOutput("cache",Context.MODE_PRIVATE);
                ObjectOutputStream os=new ObjectOutputStream(fin);
                os.writeObject(newsCache);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
