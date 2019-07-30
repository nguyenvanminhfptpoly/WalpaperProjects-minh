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

import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.view.adapter.AdapterImage;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.data.model.wallpaper.Wallpaper;
import com.mgosu.walpaperprojects.ultil.APIUltil;
import com.mgosu.walpaperprojects.ultil.OnItemListener;
import com.mgosu.walpaperprojects.view.detail.DetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private AdapterImage adapterImage;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int mPageNumber = 1;
    private int mItemCount = 10;

    private boolean isLoading  = true;
    private int visibleitem,visibleItemCount,totalItem,pre_item = 0;
    private int viewThe = 10;
    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2 = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = v2.findViewById(R.id.ryc_fagimg);
        recyclerView.setHasFixedSize(true);
        progressBar = v2.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        APIUltil.getData().getWallpaper("list_item", "image", "1", "20").enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                final List<ListItem> listItems = response.body().getData().getListItems();

                adapterImage = new AdapterImage(listItems, getActivity(), new OnItemListener() {
                    @Override
                    public void OnItemlistener(int position) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("imageinfo", listItems.get(position));
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapterImage);
                Log.d("abc",response.body().getData().getListItems().toString());
                // khoi tao adapter roi bo vao hien thi thoi e
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {
                Log.e("FF", t.getMessage());

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                visibleitem = ((GridLayoutManager) ImageFragment.this.recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if(dy>0){
                    if(isLoading){
                        if(totalItem > pre_item){
                            isLoading = false;
                            pre_item = totalItem;
                        }
                    }
                    if(!isLoading && (totalItem - visibleItemCount) <= (visibleitem + viewThe)){
                        mPageNumber++;
                        loadmore();
                        isLoading = true;
                    }
                }
            }
        });
        return v2;
    }

    private void loadmore(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                APIUltil.getData().getWallpaper("list_item", "image", "1", "20").enqueue(new Callback<Wallpaper>() {
                    @Override
                    public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                        final List<ListItem> listItems = response.body().getData().getListItems();
                        adapterImage.addImage(listItems);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Wallpaper> call, Throwable t) {
                        Log.e("FF", t.getMessage());

                    }
                });
            }
        }, 1500);

    }
//    private void GetListItem(){
//        dataClient = apiUltil.getData();
//
//        Call<List<ListItem>> listCall = dataClient.getList();
//
//        listCall.enqueue(new Callback<List<ListItem>>() {
//            @Override
//            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
//                if(!response.isSuccessful()){
//                    textView.setText("COde" + response.code());
//                    return;
//                }
//                List<ListItem> posts = response.body();
//                for (ListItem post: posts){
//                    String content="";
//
//                    content += "Itemid: "+ post.getItemId() + "\n\n";
//
//                    textView.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ListItem>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
