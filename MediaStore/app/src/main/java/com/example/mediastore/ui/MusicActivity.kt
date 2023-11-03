package com.example.mediastore.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.mediastore.R
import com.example.mediastore.databinding.ActivityMusicBinding

private const val tag = "MusicActivity"
class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer()
        initMediaPlayer()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)
        binding.startPlay.setOnClickListener {
            Log.d(tag, "onCreate: ")
            mediaPlayer.start()
        }
        binding.pausePlay.setOnClickListener {
            mediaPlayer.pause()
        }
        binding.stopPlay.setOnClickListener {
            mediaPlayer.reset()
            initMediaPlayer()
        }
        binding.lastMusic.setOnClickListener {
        }
    }

    private fun initMediaPlayer() {
        Log.d(tag, "initMediaPlayer: ")
        val assertsManager = assets
        val fd = assertsManager.openFd("mp3.mp3")
        mediaPlayer.setDataSource(fd)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener(onMusicPreparedListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    var onMusicPreparedListener = MediaPlayer.OnPreparedListener {
        mediaPlayer.start()

    }
}