package com.example.mediastore.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mediastore.R
import com.example.mediastore.databinding.ActivityServiceBinding
import com.example.mediastore.service.MyService

class ServiceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityServiceBinding
    private lateinit var downloadBinder : MyService.DownloadBinder

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadBinder = service as MyService.DownloadBinder
            downloadBinder.startDownload()
            downloadBinder.getProgress()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service)
        binding.startService.setOnClickListener(this)
        binding.stopService.setOnClickListener(this)
        binding.bindService.setOnClickListener(this)
        binding.unbindService.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.startService->{
                startService(Intent(this, MyService::class.java,))
            }
            R.id.stopService -> {
                stopService(Intent(this, MyService::class.java))
            }
            R.id.bindService -> {
                bindService(Intent(this,MyService::class.java ),connection, Context.BIND_AUTO_CREATE)
            }
            R.id.unbindService -> {
                unbindService(connection)
            }
        }
    }
}