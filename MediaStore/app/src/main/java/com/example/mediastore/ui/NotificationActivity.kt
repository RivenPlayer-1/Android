package com.example.mediastore.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mediastore.R

private const val CHANNEL_ID = "5"
private const val notificationId = 50
private const val tag = "NotificationActivity"

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        createNotificationChannel()

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_launcher_foreground)
            priority = NotificationCompat.PRIORITY_LOW
        }
        val PROGRESS_MAX = 10
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(applicationContext).apply {
            // Issue the initial notification with zero progress.
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(notificationId, builder.build())

            Thread{
                for (i in 0..PROGRESS_MAX) {
                    Thread.sleep(100)
                    Log.d(tag, "onCreate: $i")
                    builder.setProgress(PROGRESS_MAX, i, false)
                    notify(notificationId, builder.build())
                }
                Thread.sleep(10)
                builder.setContentText("Download complete")
                    .setProgress(0, 0, false)
                notify(notificationId, builder.build())
            }.start()


            // Do the job that tracks the progress here.
            // Usually, this is in a worker thread.
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            // notificationManager.notify(notificationId, builder.build());

            // When done, update the notification once more to remove the progress bar.
//            builder.setContentText("Download complete")
//                .setProgress(0, 0, false)
//            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "notifycation", importance).apply {
                description = "222222"
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}