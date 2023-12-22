package com.example.customview

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.popupwindow.CustomPopupWindow

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
        val button3 = view.findViewById<View>(R.id.bt_popup) as Button
        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        setContentView(view)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_main -> {
                val handlerThread = HandlerThread("aaaa")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                handler.postDelayed({
//                    textView.text = "B"
                    addView()

                }, 2000)
            }

            R.id.bt_second -> {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ addView2() }, 2000)
            }

            R.id.bt_popup -> {
                addPopupWindow()
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
//        (view as LinearLayout).addView(textView)
        linearLayout = LinearLayout(this)
        val textView2 = TextView(this).apply {
            text = "AA"
            setTextColor(Color.RED)
        }
        val rootParams =
            WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION)
        rootParams.width = 300
        rootParams.height = 300
        windowManager.addView(textView2, rootParams)

    }

    private fun addView2() {
        val textView2 = TextView(this)
        textView2.text = "AA"
        textView2.setTextColor(Color.RED)
        linearLayout.addView(textView2)
    }

    private fun addPopupWindow() {
        val view = LayoutInflater.from(this).inflate(R.layout.popwindo, null)

        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                Log.d(TAG, "onKey: aaaaaaa")
                return true
            }
        })

        val popupWindow = CustomPopupWindow(view, 1000, 300).apply {
            isFocusable = true
            isOutsideTouchable = true
            setOnDismissListener {
                PopupWindow.OnDismissListener {
                    Log.d(
                        TAG,
                        "dismiss: "
                    )
                }
            }
        }
//        popupWindow.setOnBackPressListener(object : CustomPopupWindow.OnBackPressListener {
//            override fun onBack() {
//                Log.d(TAG, "onBack: aaa")
//            }
//        })

//        val myDecoderView = PopupWindow::class.java.getDeclaredMethod("PopupDecorView")
//        myDecoderView.isAccessible = true
//        Toast.makeText(this, myDecoderView.getAnnotation(), Toast.LENGTH_SHORT)

        popupWindow.showAsDropDown(view, 0, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}