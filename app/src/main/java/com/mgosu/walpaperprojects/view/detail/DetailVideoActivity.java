package com.mgosu.walpaperprojects.view.detail;



import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;


import com.mgosu.walpaperprojects.R;

import com.mgosu.walpaperprojects.data.model.home.Application;
import com.mgosu.walpaperprojects.data.model.wallpaper.Wallpaper;
import com.mgosu.walpaperprojects.databinding.ActivityDetailVideoBinding;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;

import com.mgosu.walpaperprojects.ultil.CheckConnection;
import com.mgosu.walpaperprojects.view.service.MyWallpaperService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;


public class DetailVideoActivity extends AppCompatActivity {
    ActivityDetailVideoBinding binding;
    private String videourl;
    private ProgressDialog progressBar;
    private String path;
    private ListItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_video);
        CheckConnect();
        initView();
    }

    private void CheckConnect() {
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            SetWall();
            GetInfomation();
        } else {
            CheckConnection.showToast_short(getApplicationContext(), "Connect Error");
        }

    }


    private void initView() {
        binding.toolbar.setTitle("Detail Video");
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
         item = (ListItem) getIntent().getSerializableExtra("videoinfo");
        videourl = "http://192.168.200.216/dev/media/calltools/wallpaper/" + item.getFileUrl();
        new DownloadFileFromURL(DetailVideoActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,videourl);

        binding.videoView3.setVideoURI(Uri.parse(videourl));
        binding.videoView3.start();
        binding.textView5.setText(item.getLoveCount() + "");
        binding.textView7.setText(item.getDownload() + "");
        binding.videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                binding.progressBar2.setVisibility(View.GONE);
            }
        });
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoView3);
        binding.videoView3.setMediaController(mediaController);

    }

    private void SetWall() {
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WallpaperManager instance = WallpaperManager.getInstance(DetailVideoActivity.this);
                try {
                    instance.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(DetailVideoActivity.this, MyWallpaperService.class));
                startActivity(intent);
            }
        });
    }

    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        public Context context;

        public DownloadFileFromURL(Context context) {
            this.context = context;
        }

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            progressBar = new ProgressDialog(DetailVideoActivity.this);
            progressBar.setMessage("Loading... Please wait...");
            progressBar.setIndeterminate(false);
            progressBar.setCancelable(false);
//            progressBar.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream to write file
                Application.path = root +"/video_" + (item.getItemId()) +".mp4";
                Log.d("fileN", Application.path);
                OutputStream output = new FileOutputStream(Application.path);

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int) (total * 100/lenghtOfFile));
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
         **/
        @Override
        protected void onPostExecute(String file_url) {

        }

    }

}
