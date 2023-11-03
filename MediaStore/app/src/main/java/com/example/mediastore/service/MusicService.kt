package com.example.mediastore.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log


private const val TAG = "MusicService"

class MusicService : Service(), MediaPlayer.OnPreparedListener {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder {

        return MusicBinding()
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        initMediaPlayer()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initMediaPlayer() {
        val fd = assets.openFd("mp3.mp3")
        mediaPlayer?.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer?.prepareAsync()
    }

    inner class MusicBinding : Binder() {
        fun startMusic() {
            mediaPlayer?.start()
        }

        fun pauseMusic() {
            mediaPlayer?.pause()
        }

        fun stopMusic() {
            mediaPlayer?.stop()
            initMediaPlayer()
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