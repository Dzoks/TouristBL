package org.unibl.etf.mr.touristbl.fragment;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.adapter.DetailAdapter;
import org.unibl.etf.mr.touristbl.model.Entry;
import org.unibl.etf.mr.touristbl.util.EntryDAO;
import org.unibl.etf.mr.touristbl.util.EntryDB;

import java.util.List;


public class SightseeFragment extends Fragment {
    private RecyclerView recyclerView;
    private DetailAdapter adapter;
    private List<Entry> eventList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        new SightseeLoader().execute(MainActivity.getEntryDB().entryDAO());
        SearchView search=getActivity().findViewById(R.id.search_entry);
        search.setVisibility(View.VISIBLE);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    class SightseeLoader extends AsyncTask<EntryDAO,Void,List<Entry>>{

        @Override
        protected List<Entry> doInBackground(EntryDAO... voids) {
            EntryDAO dao=voids[0];
            return  dao.getByCategory("SIGHTSEE");
        }

        @Override
        protected void onPostExecute(List<Entry> eventList) {
            super.onPostExecute(eventList);
            SightseeFragment.this.eventList=eventList;
            adapter = new DetailAdapter(eventList, SightseeFragment.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NavigationView)getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_sightsee).setChecked(true);
    }

}
