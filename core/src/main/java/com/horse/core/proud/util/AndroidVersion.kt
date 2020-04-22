package com.horse.core.proud.util

import android.os.Build

/**
 * Android版本判断
 *
 * @author liliyuan
 * @since 2020年4月7日14:42:17
 * */
object AndroidVersion {

    /**
     * @return 16以上true，否则false
     * */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * @return 17以上为true，否则false
     * */
    fun hasJellyBeanMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    /**
     * @return 18以上为true，否则为false
     * */
    fun hasJellyBeanMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
    }

    /**
     * @return 19以上为true，否则为false
     * */
    fun hasKitkat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * @return 21以上为true，否则为false
     * */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * @return 22以上为true，否则为false
     * */
    fun hasLollipopMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * @return 23以上为true，否则为false
     * */
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * @return 24以上为true，否则为false
     * */
    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * @return 29以上为true，否则为false
     * */
    fun hasQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

}