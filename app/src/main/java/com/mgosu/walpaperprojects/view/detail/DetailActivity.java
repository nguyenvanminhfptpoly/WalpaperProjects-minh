package com.mgosu.walpaperprojects.view.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.databinding.ActivityDetailBinding;
import com.mgosu.walpaperprojects.ultil.CheckConnection;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private WallpaperManager wallpaperManager;
    private Bitmap bitmap1, bitmap2;
    private DisplayMetrics displayMetrics;
    private int width, height;
    private BitmapDrawable bitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        CheckConnect();
        initView();
    }
    private void CheckConnect(){
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetInfomation();

        }else{
            CheckConnection.showToast_short(getApplicationContext(), "Connect Error");
        }


    }
    private void initView() {
        setSupportActionBar(binding.toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar2.setSubtitleTextColor(Color.WHITE);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                View v1 = getLayoutInflater().inflate(R.layout.dialog_setwall, null);
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
                        Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                // On Android N and above use the new API to set both the general system wallpaper and
                                // the lock-screen-specific wallpaper
                                wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                            } else {
                                wallManager.setBitmap(bitmapImg);
                            }
                            Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException ex) {
                        }
                    }
                });
                btnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetScreenWidthHeight();
                        Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                        try {
                            wallManager.clear();
                            wallManager.setBitmap(bitmapImg);
                            Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException ex) {
                        }
                    }
                });
                btnLock.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        GetScreenWidthHeight();
                        Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                // On Android N and above use the new API to set both the general system wallpaper and
                                // the lock-screen-specific wallpaper
                                wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_LOCK);
                            } else {
                                wallManager.setBitmap(bitmapImg);
                            }
                            Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException ex) {
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

    private void GetInfomation() {
        ListItem listItem = (ListItem) getIntent().getSerializableExtra("imageinfo");
        Glide.with(getApplicationContext())
                .asBitmap()
                .load("http://192.168.200.216/dev/media/calltools/wallpaper/" + listItem.getFileUrl())
                .error(R.drawable.imgerror)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.imageView3.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        binding.textView8.setText(listItem.getDownload() + "");
        binding.textView9.setText(listItem.getLoveCount() + "");
    }


    public void GetScreenWidthHeight() {

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = getWindowManager().getDefaultDisplay().getWidth();

        height = getWindowManager().getDefaultDisplay()
                .getHeight();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
