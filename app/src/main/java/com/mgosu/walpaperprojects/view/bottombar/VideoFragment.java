package com.mgosu.walpaperprojects.view.bottombar;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.ultil.CheckConnection;
import com.mgosu.walpaperprojects.view.adapter.AdapterImage;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.data.model.wallpaper.Wallpaper;
import com.mgosu.walpaperprojects.ultil.APIUltil;
import com.mgosu.walpaperprojects.ultil.OnItemListener;
import com.mgosu.walpaperprojects.view.adapter.RecyclerviewAdapter;
import com.mgosu.walpaperprojects.view.detail.DetailVideoActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
//    private AdapterImage adapterImage;
    private RecyclerView recyclerView;
    public VideoFragment() {
        // Required empty public constructor

    }
    private ProgressBar progressBar;
    List<ListItem> listItems;
    private RecyclerviewAdapter adapter;
    Handler handler;

    boolean isLoading = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2 = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = v2.findViewById(R.id.ryc_fagvideo);


        progressBar = v2.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        CheckConnect();
        return v2;
    }
    private void CheckConnect(){
        if(CheckConnection.haveNetworkConnection(getActivity())){
            AddOnScrollRecyclerview();
            GetDataFormAPI();
        }else {
            CheckConnection.showToast_short(getActivity(),"Connect Error");
        }
    }
    public void AddOnScrollRecyclerview(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    layoutManager =  recyclerView.getLayoutManager();
                    if(!isLoading){
                        if(layoutManager != null && ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == listItems.size() - 1){
                            Loadmore();
                            isLoading = true;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }
    private void GetDataFormAPI(){
        APIUltil.getData().getWallpaper("list_item", "video", "1", "20").enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                 listItems = response.body().getData().getListItems();

                adapter = new RecyclerviewAdapter(listItems, getActivity(), new OnItemListener() {
                    @Override
                    public void OnItemlistener(int position) {
                        if(CheckConnection.haveNetworkConnection(getActivity())) {
                            Intent intent = new Intent(getActivity(), DetailVideoActivity.class);
                            intent.putExtra("videoinfo", listItems.get(position));
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(), "Connect Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
                Log.d("abc",response.body().getData().getListItems().toString());
                // khoi tao adapter roi bo vao hien thi thoi e
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {
                Log.e("FF", t.getMessage());

            }
        });
    }
    private void Loadmore(){
        listItems.add(null);
        adapter.notifyItemInserted(listItems.size() - 1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listItems.remove(listItems.size() - 1);
                int scrollposition = listItems.size();
                adapter.notifyItemRemoved(scrollposition);

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1500);


    }
}
