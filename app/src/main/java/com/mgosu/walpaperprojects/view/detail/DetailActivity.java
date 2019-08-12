package com.mgosu.walpaperprojects.view.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

    private Bitmap bitmap1, bitmap2;
    private DisplayMetrics displayMetrics;
    private int width, height;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        CheckConnect();
        initView();
        progressDialog = new ProgressDialog(DetailActivity.this);
    }
    private void CheckConnect(){
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetInfomation();

        }else{
            CheckConnection.showToast_short(getApplicationContext(), "Connect Error");
        }


    }
    private void initView() {
        binding.toolbar2.setTitle("Detail Image");
        setSupportActionBar(binding.toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar2.setSubtitleTextColor(Color.WHITE);
        binding.toolbar2.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


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
                if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    btnBoth.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            GetScreenWidthHeight();
                            final Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                            final WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            // On Android N and above use the new API to set both the general system wallpaper and
                                            // the lock-screen-specific wallpaper
                                            wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                                            progressDialog.dismiss();
                                        } else {
                                            wallManager.setBitmap(bitmapImg);
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }, 1500);

                        }
                    });
                    btnHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetScreenWidthHeight();
                            final Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                            final WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
                            progressDialog.setMessage("Please Wait ...");
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        wallManager.clear();
                                        wallManager.setBitmap(bitmapImg);
                                        progressDialog.dismiss();
                                        Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } catch (IOException ex) {
                                    }
                                }
                            }, 1500);

                        }
                    });
                    btnLock.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            GetScreenWidthHeight();
                            final Bitmap bitmapImg = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                            final WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
                            progressDialog.setMessage("please wait...");
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            // On Android N and above use the new API to set both the general system wallpaper and
                                            // the lock-screen-specific wallpaper
                                            wallManager.setBitmap(bitmapImg, null, true, WallpaperManager.FLAG_LOCK);
                                            progressDialog.dismiss();
                                        } else {
                                            wallManager.setBitmap(bitmapImg);
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } catch (IOException ex) {
                                    }
                                }
                            }, 1500);

                        }
                    });
                }else {
                    Toast.makeText(DetailActivity.this, "Connect Error", Toast.LENGTH_SHORT).show();
                }
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
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            displayMetrics = new DisplayMetrics();
             width = displayMetrics.widthPixels;
             height = displayMetrics.heightPixels;
        } else {
            displayMetrics = new DisplayMetrics();
            Point size = new Point();
            WindowManager windowManager = getWindowManager();
            windowManager.getDefaultDisplay().getSize(size);
             width = size.x;
             height = size.y;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
