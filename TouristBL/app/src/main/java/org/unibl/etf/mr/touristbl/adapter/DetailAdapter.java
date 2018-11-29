package org.unibl.etf.mr.touristbl.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.fragment.DetailFragment;
import org.unibl.etf.mr.touristbl.fragment.FavoriteFragment;
import org.unibl.etf.mr.touristbl.fragment.MapFragment;
import org.unibl.etf.mr.touristbl.model.Entry;
import org.unibl.etf.mr.touristbl.util.EntryDAO;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> implements Filterable {

    private List<Entry> entryList;
    private List<Entry> filteredList;
    private Activity activity;
    private Fragment fragment;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    public DetailAdapter(List<Entry> entryArrayList, Fragment fragment) {
        entryList=entryArrayList;
        filteredList=entryArrayList;
        this.fragment=fragment;
        this.activity=fragment.getActivity();
    }



    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailViewHolder holder, final int position) {
        final Entry entry=filteredList.get(position);
        holder.title.setText(entry.getTitle());
        holder.favoriteButton.setChecked(entry.isFavorite());
        holder.entryType.setImageDrawable(setDrawableBasedOnCategory(entry.getCategory()));
        holder.image.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                DetailFragment fragment=new DetailFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("entry",filteredList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry.setFavorite(holder.favoriteButton.isChecked());
                view.startAnimation(buttonClick);
                AsyncTask<Void,Void,Void> task=new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        MainActivity.getEntryDB().entryDAO().updateAll(entry);
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (!entry.isFavorite()&&fragment instanceof FavoriteFragment){
                            removeItem(position);

                        }
                    }
                };
                task.execute();
            }
        });
        holder.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                MapFragment fragment=new MapFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("mapEntry",filteredList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });
        holder.image.setImageDrawable(activity.getResources().getDrawable(entry.getImage()));
    }

    public void removeItem(int position) {
        Entry entry=filteredList.remove(position);
        entryList.remove(entry);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,filteredList.size());

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
            case "INSTITUTION":
                drawableCode=R.drawable.ic_wb_institution;
                break;
            case "SIGHTSEE":
                drawableCode=R.drawable.ic_wb_sightsee;
                break;
        }
        return activity.getResources().getDrawable(drawableCode);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = entryList;
                } else {
                    List<Entry> newList = new ArrayList<>();
                    for (Entry row : entryList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            newList.add(row);
                        }
                    }
                    filteredList = newList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Entry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class DetailViewHolder extends RecyclerView.ViewHolder{

        Button mapButton;
        TextView title;
        ImageView image;
        ImageView entryType;
        ToggleButton favoriteButton;


        DetailViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);
            image =  itemView.findViewById(R.id.card_image);
            entryType= itemView.findViewById(R.id.card_category_photo);
            mapButton=itemView.findViewById(R.id.button_card_map);
            favoriteButton=itemView.findViewById(R.id.button_card_favorite);

        }


    }


}
