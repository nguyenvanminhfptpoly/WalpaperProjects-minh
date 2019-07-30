package com.mgosu.walpaperprojects.view.detail;



import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;


import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.databinding.ActivityDetailVideoBinding;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;
import com.mgosu.walpaperprojects.view.home.MainActivity;
import com.mgosu.walpaperprojects.view.service.MyWallpaperService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DetailVideoActivity extends AppCompatActivity {
    ActivityDetailVideoBinding binding;
    private String videourl;
    private ProgressDialog progressBar;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_video);
        SetWall();
        GetInfomation();
        initView();


    }


    private void initView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setSubtitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetInfomation() {
        ListItem item = (ListItem) getIntent().getSerializableExtra("videoinfo");
        videourl = "http://192.168.200.216/dev/media/calltools/wallpaper/" + item.getFileUrl();
        new DownloadFileFromURL().execute(videourl);
        binding.videoView2.setVideoURI(Uri.parse(videourl));
        binding.videoView2.start();
        binding.textView2.setText(item.getLoveCount() + "");
        binding.textView3.setText(item.getDownload() + "");
        binding.videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                binding.place.setVisibility(View.GONE);
            }
        });


    }

    private void SetWall() {
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(DetailVideoActivity.this, MyWallpaperService.class));
                startActivity(intent);
            }
        });
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            progressBar = new ProgressDialog(DetailVideoActivity.this);
            progressBar.setMessage("Loading... Please wait...");
            progressBar.setIndeterminate(false);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(root+"/downloadedfile.jpg");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");

            progressBar.dismiss();
        }

    }

}
