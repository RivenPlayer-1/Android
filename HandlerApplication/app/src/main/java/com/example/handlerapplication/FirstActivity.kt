package com.example.handlerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast

class FirstActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_first,null)
        val jumpButton = view.findViewById(R.id.jumpToMain) as Button
        jumpButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        val destroyButton = view.findViewById(R.id.destroyMain) as Button
        destroyButton.setOnClickListener {
            Toast.makeText(this,"${Thread.activeCount()}",Toast.LENGTH_SHORT).show()
        }

        setContentView(view)
    }
}