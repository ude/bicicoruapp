package com.udepardo.bicicoru.extensions

import android.content.Context
import android.content.res.Resources
import android.location.Location
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

fun View.runOnGlobalLayout(runnable: Runnable) {
    val vto = this.viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@runOnGlobalLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            runnable.run()
        }
    })
}

fun Location.isEmpty(): Boolean = this.provider == ""


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibleOrGone(isVisible: Boolean) {
    if (isVisible) {
        this.visible()
    } else {
        this.gone()
    }
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.toggleVisibility() {
    if (visibility == View.GONE) {
        visible()
    } else {
        gone()
    }
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Resources.color(@ColorRes id: Int) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.getColor(id, null)
        } else {
            this.getColor(id)
        }


fun Resources.drawable(@DrawableRes id: Int) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.getDrawable(id, null)
        } else {
            this.getDrawable(id)
        }


