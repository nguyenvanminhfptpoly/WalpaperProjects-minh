package com.mgosu.walpaperprojects.view.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.mgosu.walpaperprojects.R;
import com.mgosu.walpaperprojects.data.model.home.Application;

public class MyWallpaperService extends WallpaperService {
    private MediaPlayer mediaPlayer;
    private String pathVideo;
    private Intent intent;


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

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initMedia(holder);
        }

        private void initMedia(SurfaceHolder surfaceHolder){
            if(surfaceHolder != null){
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(pathVideo));
                mediaPlayer.setSurface(surfaceHolder.getSurface());
                mediaPlayer.setLooping(true);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                mediaPlayer.start();
            }else {
                mediaPlayer.pause();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopSelf();
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
