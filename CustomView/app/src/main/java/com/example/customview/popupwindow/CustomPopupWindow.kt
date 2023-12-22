package com.example.customview.popupwindow

import android.view.View
import android.widget.PopupWindow


class CustomPopupWindow(view: View, width: Int, height: Int) : PopupWindow(view, width, height) {
    private var onBackPressListener: OnBackPressListener? = null

    override fun dismiss() {
//        val stackTrace = Exception().stackTrace
//        //        for (int i = 0; i < stackTrace.length; i++) {
////            Logger.i("key = " + stackTrace[i]);
////        }
//        if (stackTrace.size >= 2 && "dispatchKeyEvent" == stackTrace[1].methodName) {
//            //按了返回键
//            if (onBackPressListener != null) {
//                onBackPressListener!!.onBack()
//            } else {
//                newDismiss()
//            }
//        } else {
//            //点击外部或者点击关闭调用了dismiss,直接让弹窗消失
//            newDismiss()
//        }
    }

    //让弹窗消失
    private fun newDismiss() {
        super.dismiss()
    }

    fun setOnBackPressListener(onBackPressListener: OnBackPressListener) {
        this.onBackPressListener = onBackPressListener
    }

    interface OnBackPressListener {
        fun onBack()
    }
}

