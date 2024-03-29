package com.mgosu.walpaperprojects.view.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.mgosu.walpaperprojects.data.model.home.Application;
import com.mgosu.walpaperprojects.data.model.wallpaper.ListItem;


import static android.support.constraint.Constraints.TAG;

public class MyWallpaperService extends WallpaperService {
    private String pathVideo;
    protected static int playheadTime = 0;
    ListItem item;

    @Override
    public void onCreate() {
        pathVideo = Application.path;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    class VideoEngine extends Engine {
        private MediaPlayer mediaPlayer;

        public VideoEngine() {
            super();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(pathVideo));

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mediaPlayer.setSurface(holder.getSurface());
            mediaPlayer.start();
            Log.e(TAG, "onSurfaceCreated: " );
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.w(TAG, "onSurfaceChanged");
            mediaPlayer.setLooping(true);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d(TAG, "onSurfaceDestroyed: ");
            playheadTime = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}
