package com.mgosu.walpaperprojects.view.detail;



import android.databinding.DataBindingUtil;


import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;




import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.databinding.ActivityDetailVideoBinding;
import com.mgosu.walpaperprojects.model.wallpaper.ListItem;




public class DetailVideoActivity extends AppCompatActivity {
    ActivityDetailVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_detail_video);

        GetInfomation();

    }
    private void GetInfomation(){
        ListItem item = (ListItem) getIntent().getSerializableExtra("videoinfo");
        String videourl = "http://192.168.200.216/dev/media/calltools/wallpaper/"+item.getFileUrl();
        binding.videoView2.setVideoURI(Uri.parse(videourl));
        binding.videoView2.start();
        binding.textView2.setText(item.getLoveCount()+"");
        binding.textView3.setText(item.getDownload()+"");
        binding.videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                binding.place.setVisibility(View.GONE);
            }
        });

    }

   private void SetWall(){
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
   }


}
