package com.mgosu.walpaperprojects.view.detail;


import android.databinding.DataBindingUtil;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



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
        binding.textView2.setText(item.getDownload()+"");
        binding.textView3.setText(item.getLoveCount()+"");

    }

}
