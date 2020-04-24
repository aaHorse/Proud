package com.horse.proud.callback

interface PermissionListener {

    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)

}