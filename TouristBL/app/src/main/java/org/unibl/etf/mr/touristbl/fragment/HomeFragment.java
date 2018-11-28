package org.unibl.etf.mr.touristbl.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import org.unibl.etf.mr.touristbl.R;

public class HomeFragment extends Fragment {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        view.findViewById(R.id.home_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container, new NewsFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new WeatherFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new MapFragment()).addToBackStack(null).commit();

            }
        });
        view.findViewById(R.id.home_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new EventFragment()).addToBackStack(null).commit();

            }
        });
        view.findViewById(R.id.home_institution).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new InstitutionFragment()).addToBackStack(null).commit();

            }
        });
        view.findViewById(R.id.home_hotel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new HotelFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_sightsee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new SightseeFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frame_container,new FavoriteFragment()).addToBackStack(null).commit();
            }
        });
        getActivity().findViewById(R.id.search_entry).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NavigationView)getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_home).setChecked(true);
    }
}
