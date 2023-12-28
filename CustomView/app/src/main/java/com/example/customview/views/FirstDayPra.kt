package com.example.customview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.view.View

class FirstDayPra(context: Context) : View(context) {

    private var paint = Paint()
    private var path = Path()
    override fun onDraw(canvas: Canvas) {
//        path.addCircle(100f,100f,100f,Path.Direction.CW)
        path.lineTo(100f,100f)
        path.arcTo(100f,100f,300f,300f,-90f,90f,false)
        paint.apply {
            color = Color.RED
            strokeWidth = 10f
        }
        canvas.drawPath(path, paint)
        val text = "Hello world!"

        paint.textSize = 18f;
        canvas.drawText(text, 100f, 145f, paint)
        paint.textSize = 36f;
        canvas.drawText(text, 100f, 70f, paint)
        paint.textSize = 60f;
        canvas.drawText(text, 100f, 145f, paint)
        paint.textSize = 84f;
        canvas.drawText(text, 100f, 240f, paint)

        val shader = LinearGradient(100f,100f,300f,300f,Color.parseColor("#E91E63"),Color.parseColor("#2196F3"),Shader.TileMode.CLAMP)
        paint.setShader(shader)
        canvas.drawCircle(100f,500f,100f,paint)

//        canvas.drawCircle(100f,100f,100f,paint)
//        canvas.drawArc(100f, 0f, 800f,500f, 100f, 170f, true, paint)
//        canvas.drawPath(path, paint)

//        canvas.drawRect(0f,0f,100f,100f,paint)
//        canvas.drawCircle(100f,100f,100f,paint)
    }
}