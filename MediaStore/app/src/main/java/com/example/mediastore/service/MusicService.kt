package com.example.mediastore.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mediastore.R
import com.example.mediastore.ui.MusicActivity
import com.example.mediastore.view_model.MusicViewModel


private const val TAG = "MusicService"


private const val channelId = "0";

class MusicService() : Service(), MediaPlayer.OnPreparedListener {
    private var mediaPlayer: MediaPlayer? = null


    override fun onBind(intent: Intent): IBinder {
        return MusicBinding()
    }

    @SuppressLint("RemoteViewLayout")
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        initMediaPlayer()
        var notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "music channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val remoteViews = RemoteViews(packageName, R.layout.notification)
        val intent = Intent(this, MusicActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(remoteViews)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        initMediaPlayer()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initMediaPlayer() {
        val fd = assets.openFd("mp3.mp3")
        mediaPlayer?.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer?.prepareAsync()

    }

    inner class MusicBinding : Binder() {

        fun initMedia(){
//            mediaPlayer?.prepareAsync()
        }
        fun startMusic() {
            mediaPlayer?.start()
        }

        fun pauseMusic() {
            mediaPlayer?.pause()
        }
        
        fun resetMusic() {
            mediaPlayer?.reset()
            initMediaPlayer()
        }

        fun stopMusic() {
            mediaPlayer?.stop()
        }

        fun getMediaPlayer(): MediaPlayer? {
            return mediaPlayer
        }

    }

    override fun onPrepared(mp: MediaPlayer?) {
//        mediaPlayer?.start()
        Log.d(TAG, "onPrepared: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}