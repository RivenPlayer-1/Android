package com.example.mediastore.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.mediastore.R
import com.example.mediastore.databinding.ActivityMusicBinding
import com.example.mediastore.service.MusicService
import com.example.mediastore.view_model.MusicViewModel
private const val CHANNEL_ID = "3"
private const val notificationId = 5
private const val tag = "MusicActivity"
class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var viewModel: MusicViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MusicService.MusicBinding
                viewModel.observeMusic(binder.getMediaPlayer())
                binding.startPlay.setOnClickListener {
                    Log.d(tag, "onCreate: ")
                    binder.startMusic()
                }

                binding.pausePlay.setOnClickListener {
                    binder.pauseMusic()
                }
                binding.stopPlay.setOnClickListener {
                    binder.resetMusic()
                }
                binding.lastMusic.setOnClickListener {

                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }

        bindService(Intent(this, MusicService::class.java), serviceConnection, BIND_AUTO_CREATE)
    }
}