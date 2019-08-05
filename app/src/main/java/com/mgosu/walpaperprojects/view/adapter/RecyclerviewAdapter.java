package com.mgosu.walpaperprojects.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.ultil.OnItemListener;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ListItem> listItems;
    private Context context;
    public OnItemListener onItemListener;

    public RecyclerviewAdapter(List<ListItem> listItems, Context context, OnItemListener onItemListener) {
        this.listItems = listItems;
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ryc_image_item, viewGroup,false);
            return new ItemViewhodel(view);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false);
            return  new ItemLoading(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof ItemViewhodel){
            ShowItemViewhodel((ItemViewhodel) viewHolder,i);
        }else {
            ShowLoadingView((ItemLoading) viewHolder, i);
        }
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewhodel extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView tvLoveDL,tvdownload;
        private ConstraintLayout contr;
        public Context context;
        public OnItemListener onItemListener;
        public ItemViewhodel(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view3d);
            tvLoveDL = itemView.findViewById(R.id.tvlovecount3d);
            tvdownload = itemView.findViewById(R.id.tvdowncount3d);
            contr = itemView.findViewById(R.id.ctr_image);
        }
    }
    private class ItemLoading extends RecyclerView.ViewHolder{
    ProgressBar progressBar;
        public ItemLoading(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    private void ShowLoadingView(ItemLoading itemLoading, int position){

    }
    private void ShowItemViewhodel(ItemViewhodel itemViewhodel,final int position){
        final ListItem listItem = listItems.get(position);

        Glide.with(context).load("http://192.168.200.216/dev/media/calltools/wallpaper/"+listItem.getThumbLarge())
                .placeholder(R.drawable.imgerror)
                .error(R.drawable.imgerror)
                .into(itemViewhodel.imageView);
        itemViewhodel.tvLoveDL.setText(listItem.getDownload()+"");
        itemViewhodel.tvdownload.setText(listItem.getLoveCount()+"");
        itemViewhodel.contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItemlistener(position);
            }
        });
        Log.d("fff",listItem.getFileUrl());
    }

    public void addImage(List<ListItem> list){
        for(ListItem item : list){
            listItems.add(item);
        }
        notifyDataSetChanged();
    }
}
