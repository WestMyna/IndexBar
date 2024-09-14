package io.github.library

import android.content.Context

object Utils {

    /**
     * sp转像素
     *
     * @param c       the c
     * @param spValue the sp value
     * @return the int
     */
    fun sp2px(c: Context, spValue: Float): Int {
        val fontScale = c.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * dp转像素
     *
     * @param c       the c
     * @param dpValue the dp value
     * @return the int
     */
    fun dip2px(c: Context, dpValue: Float): Int {
        val scale = c.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}