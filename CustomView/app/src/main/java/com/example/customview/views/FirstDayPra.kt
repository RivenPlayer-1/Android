package com.example.customview.views

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.Shader
import android.view.View

class FirstDayPra(context: Context) : View(context) {

    private var paint = Paint()
    private var path = Path()
    private var process: Int = 10
        set(value) {
            field = value
            // 通知view刷新
            invalidate()
        }

    private val linerShader = LinearGradient(
        100f,
        100f,
        300f,
        300f,
        Color.parseColor("#E91E63"),
        Color.parseColor("#2196F3"),
        Shader.TileMode.CLAMP
    )
    private val bitmap = BitmapFactory.decodeStream(context.assets.open("1.jpg"))
    private val bitShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    private val bitmap2 = BitmapFactory.decodeStream(context.assets.open("2.jpg"))
    private val bitShader2 = BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    private val composeShader = ComposeShader(linerShader, bitShader2, PorterDuff.Mode.DST_IN)
    override fun onDraw(canvas: Canvas) {
//        path.addCircle(100f,100f,100f,Path.Direction.CW)
//        path.lineTo(100f, 100f)
//        path.arcTo(100f, 100f, 300f, 300f, -90f, 90f, false)
        paint.apply {
            color = Color.RED
            strokeWidth = 20f
//            style = Paint.Style.STROKE
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

        paint.shader = linerShader
        canvas.drawCircle(100f, 500f, 100f, paint)

        canvas.save()
        paint.shader = bitShader
//        canvas.clipRect(100f,700f,150f,750f)
        canvas.rotate(45f, 150f, 750f)
        canvas.drawRect(0f, 400f, 200f, 600f, paint)
        canvas.restore()
        paint.shader = bitShader2
        canvas.drawCircle(100f, 900f, 100f, paint)

        canvas.drawLine(500f, 500f, 800f, 800f, paint)

        canvas.drawArc(100f, 1000f, 300f, 1200f, 0f, 3.6f * process, true, paint)

    }
}