package org.unibl.etf.mr.touristbl;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private List<Entry> entryList;
    private Activity activity;

    public DetailAdapter(List<Entry> entryArrayList, Activity activity) {
        entryList=entryArrayList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, final int position) {
        Entry entry=entryList.get(position);
        holder.title.setText(entry.getTitle());
        holder.entryType.setImageDrawable(setDrawableBasedOnCategory(entry.getCategory()));
        holder.image.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                DetailFragment fragment=new DetailFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("entry",entryList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });

       // holder.image.setImageBitmap(BitmapFactory.decodeByteArray(entry.getImage(), 0, entry.getImage().length));
        holder.image.setImageDrawable(activity.getResources().getDrawable(R.drawable.demo));
    }

    private Drawable setDrawableBasedOnCategory(String category) {
        int drawableCode=0;
        switch(category){
            case "EVENT":
                drawableCode=R.drawable.ic_wb_event;
                break;
            case "HOTEL":
                drawableCode=R.drawable.ic_wb_hotel;
                break;
            case "INSTITUION":
                drawableCode=R.drawable.ic_wb_institution;
                break;
            case "LANDMARK":
                drawableCode=R.drawable.ic_wb_sightsee;
                break;
        }
        return activity.getResources().getDrawable(drawableCode);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }


    class DetailViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView image;
        ImageView entryType;

        DetailViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            image =  itemView.findViewById(R.id.card_image);
            entryType= itemView.findViewById(R.id.card_category_photo);
        }


    }
}
