package org.unibl.etf.mr.touristbl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        view.findViewById(R.id.home_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new NewsFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_container,new WeatherFragment()).addToBackStack(null).commit();
            }
        });
        view.findViewById(R.id.home_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_container,new MapFragment()).addToBackStack(null).commit();

            }
        });
        view.findViewById(R.id.home_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_container,new EventFragment()).addToBackStack(null).commit();

            }
        });
        return view;
    }
}
