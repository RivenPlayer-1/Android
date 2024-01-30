package com.example.handlerapplication

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.text.Layout.Alignment
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.handlerapplication.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {
//    private val handlerThread by lazy {
//        HandlerThread("handlerThread2222")
//    }

    private var handler: Handler? = null
    private var windowManager: WindowManager? = null
    private var binding: ActivityMainBinding? = null

    private var linearLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
//        handlerThread.start()
//        handler = Handler(handlerThread.looper)
//        windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        windowManager = getWindowManager()
        binding?.removeThreadView?.setOnClickListener {
//            thread{
//                it.setBackgroundColor(Color.GREEN)
//                Log.d("aaaa", "onCreate: ${Thread.currentThread().name}")
//            }
            binding?.myTv?.text ="231231"
            it.setBackgroundColor(Color.GRAY)
        }

    }

    private fun addThreadView() {
        linearLayout = LinearLayout(applicationContext).apply {
            setBackgroundColor(Color.BLUE)
        }
        val windowParams =
            WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY).apply {
                flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

                width = 500
                height = 500
                gravity = Gravity.RIGHT
                format = PixelFormat.RGBA_8888
            }

        windowManager?.addView(linearLayout, windowParams)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        windowManager?.removeView(linearLayout)
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show()
    }
}