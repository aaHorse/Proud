package com.horse.network.util

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.util.GlobalUtil
import com.horse.core.proud.util.SharedUtil
import java.lang.Exception
import java.util.*

/**
 * 获取设备的信息
 *
 * @author liliyuan
 * @since 2020年4月8日08:30:47
 * */
object Utility {

    private val TAG = "Utility"

    private var deviceSerial: String? = null

    /**
     * 获取设备的品牌和型号。
     *
     * @return "品牌 型号"，获取失败时"unknown"
     * */
    val deviceName: String
        get() {
            var deviceName = Build.BRAND + " " + Build.MODEL
            if(TextUtils.isEmpty(deviceName)){
                logWarn(TAG,"获取设备的品牌和型号失败")
                deviceName = "unknown"
            }
            return deviceName
        }

    /**
     * 获取APP当前版本号。
     *
     * @return APP当前版本号
     * */
    val appVersion: String
        get() {
            var version = ""
            try{
                val packageManager = Proud.getContext().packageManager
                val packInfo = packageManager.getPackageInfo(Proud.getPackageName(), 0)
                version = packInfo.versionName
            }catch (e: Exception){
                logWarn(TAG, "获取应用版本号失败",e)
            }

            if(TextUtils.isEmpty(version)){
                version = "unknown"
            }
            return version
        }

    /**
     * 获取App网络请求验证参数，用于辨识是不是官方渠道的App。
     */
    val appSign: String
        get() {
            return MD5.encrypt(SignUtil.getAppSignature() + appVersion)
        }

    /**
     * 获取设备的序列号。如果无法获取到设备的序列号，则会生成一个随机的UUID来作为设备的序列号，UUID生成之后会存入缓存，
     * 下次获取设备序列号的时候会优先从缓存中读取。
     * @return 设备的序列号。
     */
    @SuppressLint("HardwareIds")
    fun getDeviceSerial(): String {
        if (deviceSerial == null) {
            var deviceId: String? = null
            val appChannel =  GlobalUtil.getApplicationMetaData("APP_CHANNEL")
            if ("google" != appChannel || "samsung" != appChannel) {
                try {
                    deviceId = Settings.Secure.getString(Proud.getContext().contentResolver, Settings.Secure.ANDROID_ID)
                } catch (e: Exception) {
                    logWarn(TAG, "get android_id with error", e)
                }
                if (!TextUtils.isEmpty(deviceId) && deviceId!!.length < 255) {
                    deviceSerial = deviceId
                    return deviceSerial.toString()
                }
            }
            var uuid = SharedUtil.read(NetworkConst.UUID, "")
            if (!TextUtils.isEmpty(uuid)) {
                deviceSerial = uuid
                return deviceSerial.toString()
            }
            uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase()
            SharedUtil.save(NetworkConst.UUID, uuid)
            deviceSerial = uuid
            return deviceSerial.toString()
        } else {
            return deviceSerial.toString()
        }
    }
}