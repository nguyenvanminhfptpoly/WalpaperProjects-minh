package com.mgosu.walpaperprojects.view.detail;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.view.home.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private ImageView mImgDetail;
    private Button mBtnSetWall;
    private ImageView mImageView3;
    private TextView mTvLove;
    private ImageView mImageView4;
    private TextView mTvDown;
    private WallpaperManager wallpaperManager;
    private Bitmap bitmap1, bitmap2 ;
    private DisplayMetrics displayMetrics ;
    private int width, height;
    private BitmapDrawable bitmapDrawable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Khaibao();
        GetInfomation();
        SetWallpaper();
    }
    private void Khaibao(){
        mImgDetail = findViewById(R.id.img_detail);
        mBtnSetWall = findViewById(R.id.btnSetWall);
        mImageView3 = findViewById(R.id.imageView3);
        mTvLove = findViewById(R.id.tvLove);
        mImageView4 = findViewById(R.id.imageView4);
        mTvDown = findViewById(R.id.tvDown);
    }
    private void GetInfomation(){
        ListItem listItem =(ListItem) getIntent().getSerializableExtra("imageinfo");
        Glide.with(getApplicationContext())
                .asBitmap()
                .load("http://19    2.168.200.216/dev/media/calltools/wallpaper/"+listItem.getFileUrl())
                .error(R.drawable.imgerror)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mImgDetail.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        mTvDown.setText(listItem.getDownload()+"");
        mTvLove.setText(listItem.getLoveCount()+"");
    }

    private void SetWallpaper(){
        mBtnSetWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetScreenWidthHeight();
                Bitmap bitmapImg = ((BitmapDrawable) mImgDetail.getDrawable()).getBitmap();
                WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    wallManager.clear();
                    wallManager.setBitmap(bitmapImg);
                    Toast.makeText(DetailActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                }
            }
        });

    }
    public void GetScreenWidthHeight(){

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels;

    }


}
