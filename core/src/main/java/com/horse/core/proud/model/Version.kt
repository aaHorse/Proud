package com.horse.core.proud.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 版本更新信息
 *
 * @author liliyuan
 * @since 2020年4月21日06:43:03
 * */
@Parcelize
class Version(@SerializedName("change_log") val changeLog: String,
              @SerializedName("is_force") val isForce: Boolean,
              val url: String,
              @SerializedName("version_name") val versionName: String,
              @SerializedName("version_code") val versionCode: Int,
              val channel: String) : Parcelable