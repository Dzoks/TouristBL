package org.unibl.etf.mr.touristbl.fragment;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.util.EntryDB;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.model.Entry;

public class DetailFragment extends Fragment {

    private ImageView detailsPhoto;
    private TextView detailsTitle;
    private WebView detailsFull;
    private ToggleButton btnFavorite;
    private Button btnMap;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    private Entry entry;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_details,container,false);
        detailsPhoto=view.findViewById(R.id.details_photo);
        detailsFull=view.findViewById(R.id.details_full);
        detailsTitle=view.findViewById(R.id.details_title);
        btnFavorite=view.findViewById(R.id.button_favorite);
        btnMap=view.findViewById(R.id.button_see_map);
        entry=(Entry) getArguments().get("entry");
        detailsPhoto.setImageDrawable(getResources().getDrawable(entry.getImage()));
        btnFavorite.setChecked(entry.isFavorite());
        detailsTitle.setText(entry.getTitle());
        detailsFull.loadData(entry.getDescription(),"text/html","utf-8");
        getActivity().findViewById(R.id.search_entry).setVisibility(View.GONE);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                MapFragment fragment=new MapFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("mapEntry",entry);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                entry.setFavorite(btnFavorite.isChecked());
                AsyncTask<Void,Void,Void> task=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        MainActivity.getEntryDB().entryDAO().updateAll(entry);
                        return null;
                    }
                };
                task.execute();
            }
        });
        return view;
    }
}
