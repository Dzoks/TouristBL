package org.unibl.etf.mr.touristbl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class DetailFragment extends Fragment {

    private ImageView detailsPhoto;
    private TextView detailsTitle;
    private WebView detailsFull;
    private ToggleButton btnFavorite;
    private Button btnMap;

    private Entry entry;
    private EntryDB database;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_details,container,false);

        detailsPhoto=view.findViewById(R.id.details_photo);
        detailsFull=view.findViewById(R.id.details_full);
        detailsTitle=view.findViewById(R.id.details_title);
        btnFavorite=view.findViewById(R.id.button_favorite);
        btnMap=view.findViewById(R.id.button_see_map);
       // database=(EntryDB)getArguments().getSerializable("database");
        entry=(Entry) getArguments().get("entry");
        detailsPhoto.setImageDrawable(getResources().getDrawable(R.drawable.demo));
        detailsTitle.setText(entry.getTitle());
        detailsFull.loadData(entry.getDescription(),"text/html","utf-8");

        return view;
    }
}
