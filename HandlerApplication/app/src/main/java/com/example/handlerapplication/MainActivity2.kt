package com.example.handlerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_main2,null)
        val textView = view.findViewById<TextView>(R.id.tv) as TextView
        val button: Button = view.findViewById(R.id.bt) as Button
        button.setOnClickListener {
            thread {
                textView.text = "3333"
                textView.alpha = 0.5f
            }
        }
        setContentView(view)
    }
}