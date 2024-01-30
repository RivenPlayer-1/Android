package com.example.customview.views.pra1

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil

object MyDiff : DiffUtil.ItemCallback<Fragment>() {
    override fun areItemsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Fragment, newItem: Fragment): Boolean {
        return oldItem.id == newItem.id
    }

}