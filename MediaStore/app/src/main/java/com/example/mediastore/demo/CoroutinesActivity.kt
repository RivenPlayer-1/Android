package com.example.mediastore.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.mediastore.R
import com.example.mediastore.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "el"

class CoroutinesActivity : AppCompatActivity() {
    private val scope = MainScope()

    private lateinit var binding: ActivityCoroutinesBinding
    private lateinit var textView1: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_coroutines)


        textView1 = binding.text1
        button1 = binding.button1
        button2 = binding.button2


        button1.setOnClickListener {
            textView1.text = "${System.currentTimeMillis()}"
        }


        button2.setOnClickListener {
            scope.launch {
                 task(2000)
                task(5000)
                task(1000)
                task(500)

                changeUi(0)

                task(2000)
                task(5000)
                task(1000)
                task(500)

                changeUi(1)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changeUi(flag: Int) {
        val startTime = System.currentTimeMillis()
        scope.launch {
            Log.i(TAG, "changeUi($flag): launch time = ${System.currentTimeMillis() - startTime}")
            timeConsuming(100)
            textView1.text = "${System.currentTimeMillis()}-From changeUi $flag"
        }
    }

    private fun task(delay: Int) {
        val startTime = System.currentTimeMillis()
        scope.launch {
            Log.i(TAG, "task: launch($delay) time = ${System.currentTimeMillis() - startTime}")
            timeConsuming(delay)
            textView1.text = "${System.currentTimeMillis()}-From task($delay)"
        }
    }


    private fun timeConsuming(times: Int) {
        val file = File(cacheDir, "test.txt")
        if (!file.exists()) {
            file.createNewFile()
        }

        repeat(times * 100) {
            file.appendText("${System.currentTimeMillis()} - balabala - ${it} \n")
        }
    }
}
