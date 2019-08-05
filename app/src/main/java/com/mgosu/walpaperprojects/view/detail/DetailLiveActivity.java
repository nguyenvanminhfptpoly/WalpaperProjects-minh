package com.mgosu.walpaperprojects.view.detail;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.databinding.ActivityDetailLiveBinding;
import com.mgosu.walpaperprojects.ultil.CheckConnection;


import java.io.IOException;

public class DetailLiveActivity extends AppCompatActivity  {
    ActivityDetailLiveBinding binding;
    public DisplayMetrics displayMetrics;
    public int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_live);
       CheckConnect();
        initView();
    }
    private void CheckConnect(){
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetInformation();

            setWall();
        }else {
            CheckConnection.showToast_short(getApplicationContext(), "Connect Error");
        }
    }
    private void initView(){
        binding.toolbar4.setTitle("Detail Live");
        setSupportActionBar(binding.toolbar4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar4.setSubtitleTextColor(Color.WHITE);
    }

    private void GetInformation() {
        ListItem listItem = (ListItem) getIntent().getSerializableExtra("imagelive");
        Glide.with(getApplicationContext())
                .asBitmap()
                .load("http://192.168.200.216/dev/media/calltools/wallpaper/" + listItem.getThumbLarge())
                .error(R.drawable.imgerror)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.imageView2.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        binding.textView2.setText(listItem.getDownload() + "");
        binding.textView3.setText(listItem.getLoveCount() + "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


   private void setWall(){
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailLiveActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = inflater.inflate(R.layout.dialog_setwall, null);
        Button btnHome = v1.findViewById(R.id.btnHome);
        Button btnLock = v1.findViewById(R.id.btnLock);
        Button btnBoth = v1.findViewById(R.id.btnBoth);
        TextView tvCancel = v1.findViewById(R.id.textView4);
        builder.setView(v1);
        final Dialog dialog = builder.create();
        dialog.show();

        btnBoth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                GetScreenWidthHeight();
                Bitmap bitmapImg = ((BitmapDrawable) binding.imageView2.getDrawable()).getBitmap();
                WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // On Android N and above use the new API to set both the general system wallpaper and
                        // the lock-screen-specific wallpaper
                        wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                    } else {
                        wallManager.setBitmap(bitmapImg);
                    }
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (IOException ex) {
                }
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetScreenWidthHeight();
                Bitmap bitmapImg = ((BitmapDrawable) binding.imageView2.getDrawable()).getBitmap();
                WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    wallManager.clear();
                    wallManager.setBitmap(bitmapImg);
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnLock.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                GetScreenWidthHeight();
                Bitmap bitmapImg = ((BitmapDrawable) binding.imageView2.getDrawable()).getBitmap();
                WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // On Android N and above use the new API to set both the general system wallpaper and
                        // the lock-screen-specific wallpaper
                        wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_LOCK);
                    } else {
                        wallManager.setBitmap(bitmapImg);
                    }
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

            }
        });
    }
    public void GetScreenWidthHeight() {

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels;

    }
}
