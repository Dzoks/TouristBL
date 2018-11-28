package org.unibl.etf.mr.touristbl.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.model.Entry;

import java.io.Serializable;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {


    MapView mMapView;
    private GoogleMap googleMap;
    private Entry mapEntry;
    private Marker centerMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getArguments()!=null){
            Entry entry= (Entry) getArguments().getSerializable("mapEntry");
            mapEntry=entry;
            
        }
        mMapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        ((NavigationView)getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_map).setChecked(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        googleMap.setOnInfoWindowClickListener(this);
        initMarkers();
    }

    private void initMarkers() {
        new MapLoader().execute();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DetailFragment fragment=new DetailFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("entry", (Serializable) marker.getTag());
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
    }

    class MapLoader extends AsyncTask<Void,Void,List<Entry>>{

        @Override
        protected List<Entry> doInBackground(Void... voids) {
            return MainActivity.getEntryDB().entryDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();


            for (Entry entry:entries){
                LatLng latLng=new LatLng(entry.getLat(),entry.getLng());
                bounds.include(latLng);
                Marker marker=MapFragment.this.googleMap.addMarker(new MarkerOptions().position(latLng).title(entry.getTitle()).
                        icon(BitmapDescriptorFactory.defaultMarker(getColorBasedOnCategory(entry.getCategory()))));
                marker.setTag(entry);
                if (mapEntry!=null && entry.getId()==mapEntry.getId()){
                    centerMarker=marker;
                }
            }
            if (centerMarker!=null){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerMarker.getPosition(),18));
                centerMarker.showInfoWindow();
            }else{
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),50));
            }
        }
    }

    private float getColorBasedOnCategory(String category) {
        float retValue;
        switch (category){
            case "HOTEL":
                retValue=BitmapDescriptorFactory.HUE_CYAN;
                break;
            case "SIGHTSEE":
                retValue=BitmapDescriptorFactory.HUE_MAGENTA;
                break;
            case "EVENT":
                retValue=BitmapDescriptorFactory.HUE_BLUE;
                break;
            case "INSTITUTION":
                retValue=BitmapDescriptorFactory.HUE_YELLOW;
                break;
                default:
                    retValue=BitmapDescriptorFactory.HUE_RED;
        }
        return retValue;
    }


}
