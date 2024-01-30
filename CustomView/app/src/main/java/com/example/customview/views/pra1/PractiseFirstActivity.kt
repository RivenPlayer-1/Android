package com.example.customview.views.pra1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.customview.R

class PractiseFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practise_first)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val vp = findViewById<ViewPager2>(R.id.my_vp)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_menu, menu)
        val shareItem = menu?.findItem(R.id.action_share);
        var myShareActionProvider = MenuItemCompat.getActionProvider(shareItem)
        myShareActionProvider.setVisibilityListener {
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show()

        }
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {

            }
            R.id.action_share -> {
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show()
                Log.d("aaaaa", "onOptionsItemSelected: aaa")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}