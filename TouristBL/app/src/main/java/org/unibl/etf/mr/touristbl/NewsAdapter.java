package org.unibl.etf.mr.touristbl;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.squareup.picasso.Picasso;

import java.util.List;

import org.unibl.etf.mr.touristbl.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Activity activity;

    public NewsAdapter(List<News> newsArrayList,Activity activity) {
        newsList=newsArrayList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        holder.primaryText.setText(newsList.get(position).getTitle());
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IDEMOO"+position);


            }
        });
        holder.mediaImage.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                NewsDetailsFragment fragment=new NewsDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("NEWS",newsList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });
        Picasso.get().load(newsList.get(position).getImageUrl()).into(holder.mediaImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView primaryText;
        ImageView mediaImage;
        ToggleButton favoriteButton;

        NewsViewHolder(View itemView) {
            super(itemView);
            primaryText = (TextView) itemView.findViewById(R.id.primary_text);
            mediaImage = (ImageView) itemView.findViewById(R.id.media_image);
            favoriteButton=(ToggleButton) itemView.findViewById(R.id.button_favorite);


        }


    }
}
