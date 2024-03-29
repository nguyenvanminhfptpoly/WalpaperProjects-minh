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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.ultil.OnItemListener;

import java.util.List;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ViewHodel> {

    private List<ListItem> listItems;
    private Context context;
    public OnItemListener onItemListener;

    public AdapterImage(List<ListItem> listItems, Context context, OnItemListener onItemListener) {
        this.listItems = listItems;
        this.context = context;
        this.onItemListener = onItemListener;


    }



    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {



        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v1 = inflater.inflate(R.layout.ryc_image_item, viewGroup, false);
        return new ViewHodel(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel viewHodel,final int i) {
        final  ListItem listItem = listItems.get(i);

        Glide.with(context).load("http://192.168.200.216/dev/media/calltools/wallpaper/"+listItem.getThumbLarge())
                .placeholder(R.drawable.imgerror)
                .error(R.drawable.imgerror)
                .into(viewHodel.imageView);
        viewHodel.tvLoveDL.setText(listItem.getDownload()+"");
        viewHodel.tvdownload.setText(listItem.getLoveCount()+"");
        viewHodel.contr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItemlistener(i);
            }
        });
        Log.d("fff",listItem.getFileUrl());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHodel extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView tvLoveDL,tvdownload;
    private ConstraintLayout contr;
    public Context context;
    public OnItemListener onItemListener;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view3d);
            tvLoveDL = itemView.findViewById(R.id.tvlovecount3d);
            tvdownload = itemView.findViewById(R.id.tvdowncount3d);
            contr = itemView.findViewById(R.id.ctr_image);

        }


    }
    public void addImage(List<ListItem> list){
        for(ListItem item : list){
            listItems.add(item);
        }
        notifyDataSetChanged();
    }
}
