package com.example.customview.scroll

import android.content.Context
import android.text.Layout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View

class ScrollView1(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var lastX : Float? = null
    var lastY : Float? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

        when (event?.action){
            MotionEvent.ACTION_DOWN ->{
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE ->{
                var moveX = (event.x - lastX!!).toInt()
                var moveY = (event.y - lastY!!).toInt()
                layout(left + moveX, top + moveY, right + moveX, bottom + moveY)
            }
        }
        return  true
    }
}