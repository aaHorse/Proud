package com.horse.proud.data.network.callback

interface PermissionListener {

    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)

}