package com.example.customview

import android.content.res.Resources

object DensityUtil {

    private var mScale: Float = -1f
    public fun dp2Px(dp: Int): Float {
        if (mScale == -1f) {
            val widthPixels = Resources.getSystem().displayMetrics.widthPixels
            mScale = widthPixels / 1920f
        }
        return dp * mScale * 1.5f
    }

    public fun px2Px(dp: Int): Float {
        if (mScale == -1f) {
            val widthPixels = Resources.getSystem().displayMetrics.widthPixels
            mScale = widthPixels / 1920f
        }
        return dp * mScale
    }

    public fun sp2Px(sp: Int): Float {
        if (mScale == -1f) {
            val widthPixels = Resources.getSystem().displayMetrics.widthPixels
            mScale = widthPixels / 1920f
        }
        return sp * mScale
    }


}