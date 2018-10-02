package org.unibl.etf.mr.touristbl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventFragment extends Fragment {
    private RecyclerView recyclerView;
    private DetailAdapter adapter;
    private List<Entry> eventList;



    public EventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Entry entry=new Entry();
        entry.setTitle("Nektar demofest 2018");
        entry.setCategory("EVENT");
        entry.setDescription("\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec rhoncus fringilla ex ac vulputate. Donec laoreet porta convallis. Vestibulum at libero tempus, maximus ipsum sed, scelerisque urna. Nunc dapibus enim vel finibus posuere. Aenean mi quam, luctus vel scelerisque volutpat, varius in lectus. Aliquam eleifend est vel tortor eleifend placerat. Suspendisse quis enim accumsan massa feugiat pretium nec vel elit.\n" +
                "\n" +
                "Etiam ornare, purus et euismod dapibus, erat dui rutrum mauris, ac mattis elit orci interdum elit. Maecenas id facilisis felis. Suspendisse elit ipsum, porttitor at dui at, placerat euismod mi. Donec aliquet ut nulla a porttitor. Vivamus et vestibulum nibh. Suspendisse consequat justo id elementum maximus. Maecenas vehicula velit eget tristique volutpat.\n" +
                "\n" +
                "Aenean sed suscipit nisi. Curabitur in lacinia ligula. Etiam vel lorem in ante finibus sodales. Aliquam libero ligula, rhoncus a diam consectetur, auctor porta diam. Praesent posuere sapien at venenatis placerat. Donec blandit rhoncus dolor, sit amet tincidunt magna congue vitae. Integer aliquet at est sed pharetra.\n" +
                "\n" +
                "Maecenas leo tortor, lobortis quis scelerisque a, malesuada sit amet nisl. Maecenas accumsan dictum est at blandit. Donec fermentum non eros sit amet venenatis. Quisque et tristique arcu. In nec blandit massa. Duis quis neque rhoncus, gravida risus egestas, mollis elit. Sed mi felis, finibus id iaculis vel, dignissim vel libero.\n" +
                "\n" +
                "Maecenas sodales lectus nec vulputate tristique. Fusce ut nulla ac risus consectetur facilisis molestie at lorem. Mauris volutpat mattis tincidunt. Cras at aliquam enim. Praesent fringilla tellus vel placerat imperdiet. Curabitur tristique ac erat a viverra. Donec quis molestie diam. Donec et velit odio. Phasellus ut elit non neque commodo volutpat in at nunc. ");
        eventList=new ArrayList<>();
        eventList.add(entry);
        adapter = new DetailAdapter(eventList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    /* recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                NewsDetailsFragment fragment=new NewsDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("NEWS",newsArrayList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/

        return view;
    }


}
