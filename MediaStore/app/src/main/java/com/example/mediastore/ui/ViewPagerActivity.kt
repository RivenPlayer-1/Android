package com.example.mediastore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.example.mediastore.R
import com.example.mediastore.databinding.ActivityViewPagerBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.zip.Inflater

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding
    var index = 0
    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text1 = LayoutInflater.from(this).inflate(R.layout.text_row_item, null)
        val text2 = LayoutInflater.from(this).inflate(R.layout.text_row_item, null)
        val text3 = LayoutInflater.from(this).inflate(R.layout.text_row_item, null)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_view_pager
        )

        var view: View
        binding.vf.stopFlipping()
        binding.vf.removeAllViews()
        for (i in 0..2) {
            Log.d("TAG", "onCreate: $index")
            Log.d("TAG", "onCreate: ${binding.vf.size}")
            index++
            view = LayoutInflater.from(this).inflate(R.layout.text_row_item, null)
            view.findViewById<TextView>(R.id.textView).text = "text$index"
            binding.vf.addView(view)
        }
        binding.btAddView.setOnClickListener {


            val file = File(applicationContext.filesDir, "3.txt")
            Log.d("TAG", "onCreate: ${file.absolutePath}")
            if(file.exists()){
                val fileReadable = FileReader(file)
                val readBufferedReader = BufferedReader(fileReadable)
                binding.tvWarningContent1.append(readBufferedReader.readText())
                binding.tvWarningContent1.movementMethod = ScrollingMovementMethod.getInstance()
            }

        }
        val viewList = listOf<View>(text1, text2, text3)

    }
}