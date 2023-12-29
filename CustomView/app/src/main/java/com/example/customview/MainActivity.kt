package com.example.customview

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.views.FirstDayPra

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"
    private lateinit var view: View
    private lateinit var textView: TextView
    private lateinit var windowManager: WindowManager
    private lateinit var linearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = LayoutInflater.from(this).inflate(R.layout.custom_scroll_view, null)
        textView = view.findViewById<View>(R.id.tv_main) as TextView
        windowManager = this.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        val button = view.findViewById<View>(R.id.bt_main) as Button
        val button2 = view.findViewById<View>(R.id.bt_second) as Button
        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        setContentView(view)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ addView() }, 1000)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_main -> {
                val handlerThread = HandlerThread("aaaa")
                handlerThread.start()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
//                    textView.text = "B"
                    addView()

                }, 0)
            }

            R.id.bt_second -> {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ addView2() }, 0)
            }

        }
    }

    /**
     * 动态添加view流程：
     * 1、获取WindowManger
     * 2、新增View/ViewGroup
     * 3、新增GroupParams
     * 4、向WindowManger添加ViewGroup和GroupParams
     */
    private fun addView() {
        linearLayout = LinearLayout(this)
        val circleView = FirstDayPra(this)
//        val objectAnimator1 = ObjectAnimator.ofFloat(circleView, "x", 0f, 100f).apply { duration = 3000 }
//        val objectAnimator = ObjectAnimator.ofFloat(circleView,"rotation",90f)
//        val objectAnimator2 = ObjectAnimator.ofFloat(circleView, "y", 0f, 1000f).apply { duration = 3000 }
//        val objectAnimator3 = ObjectAnimator.ofFloat(circleView, "x", 100f, 0f).apply { duration = 3000 }
//        val objectAnimator4 = ObjectAnimator.ofFloat(circleView, "y", 1000f, 0f).apply { duration = 3000
//        start()
//        }
        val objectAnimator = ObjectAnimator.ofInt(circleView, "process", 0, 88).apply {
            duration = 3000
        }
        val button = Button(this).apply {
            text = "animator"
            setOnClickListener {
                objectAnimator.start()
            }
        }

//        animationSet.play(objectAnimator)
//        animationSet.start()
        val textView = TextView(this).apply {
            text = "bb"
            setTextColor(Color.RED)
        }
        val rootParams =
            WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL)
                .apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    format = PixelFormat.RGBA_8888
                }
        linearLayout.setBackgroundColor(Color.WHITE)

        linearLayout.addView(textView)
        linearLayout.addView(button)
        linearLayout.addView(circleView)
        windowManager.addView(linearLayout, rootParams)

    }

    private fun addView2() {
        val textView2 = TextView(this)
        textView2.text = "AA"
        textView2.setTextColor(Color.RED)
        Toast.makeText(this, "AA", Toast.LENGTH_SHORT)
        Log.d(TAG, "addView2: aaaa")
        linearLayout.addView(textView2)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}