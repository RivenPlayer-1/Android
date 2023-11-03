package com.example.mediastore.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.mediastore.R
import com.example.mediastore.ui.MusicActivity


/*
    1：Service实例只有一个
    2：如果组件使用startService()启动服务，服务会一直运行，除非使用stopSelf()或stopService()，注：可通过其他组件调用这两种方法，
    3：如果组件使用bindService()创建服务，且未调用onStartCommand(),即不使用startService，则服务只在与该组件绑定时运行。当不再有组件与服务绑定时，系统会销毁服务
    4：如果服务同时处理多个对 onStartCommand() 的请求，则不应在处理完一个启动请求之后停止服务，因为Service已收到新的启动请求（在第一个请求结束时停止服务会终止第二个请求）。
        为避免此问题，可以使用 stopSelf(int) 确保服务停止请求始终基于最近的启动请求。换言之，在调用 stopSelf(int) 时，需传递与停止请求 ID 相对应的启动请求 ID（传递给 onStartCommand() 的 startId）。
        此外，如果服务在能够调用 stopSelf(int) 之前收到新启动请求，则 ID 不匹配，服务也不会停止。
*/
private const val TAG: String = "MyService"
private const val myService = "my_service"

class MyService : Service() {

    private var mBinder = DownloadBinder()
    private var remoteViews: RemoteViews? = null


    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }


    override fun onCreate() {
        super.onCreate() // 调用父类的onCreate方法
        Log.d(TAG, "onCreate: ") // 打印日志信息
       remoteViews = RemoteViews(this.packageName, R.layout.notification)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // 获取通知管理器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 如果设备版本大于等于Android 8.0
            val channel = NotificationChannel(
                myService, // 通知通道名称
                "前台Service通知", // 通知通道描述
                NotificationManager.IMPORTANCE_DEFAULT // 通知重要性
            )
            manager.createNotificationChannel(channel) // 创建通知通道
        }
        val intent = Intent(this, MusicActivity::class.java) // 创建启动MainActivity的意图
        val pi = PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE) // 创建 PendingIntent
        val notification = NotificationCompat.Builder(
            this, myService // 通知的上下文和服务名称
        ).setContentText("This is contextLL") // 设置通知的内容文本
            .setContentText("This is content text") // 设置通知的内容文本
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 设置通知的小图标
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground)) // 设置通知的大图标
            .setContentIntent(pi) // 设置通知的点击意图
            .setCustomContentView(remoteViews)
            .build() // 构建通知
        startForeground(1,notification) // 在前台启动通知
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
//        val player = ExoPlayer.Builder(intent).build()
//        val mediaSession = MediaSession.Builder(context, player).build()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        Log.d(TAG, "onDestroy: ")
    }

    class DownloadBinder : Binder() {
        fun startDownload() {
            Log.d(TAG, "startDownload: ")
        }

        fun getProgress(): Int {
            Log.d(TAG, "getProgress: ")
            return 0
        }

    }
}