package com.horse.proud.util

import com.horse.core.proud.Const
import com.horse.core.proud.util.SharedUtil

/**
 * 获取当前用户信息的工具类
 *
 * @author liliyuan
 * @since 2020年4月10日06:22:35
 * */
object UserUtil {

    val nickname: String
        get() = SharedUtil.read(Const.User.NICKNAME, "")

    val avatar: String
        get() = SharedUtil.read(Const.User.AVATAR, "")

    val bgImage: String
        get() = SharedUtil.read(Const.User.BG_IMAGE, "")

    val description: String
        get() = SharedUtil.read(Const.User.DESCRIPTION, "")

    fun saveNickname(nickname: String?) {
        if (nickname != null && nickname.isNotBlank()) {
            SharedUtil.save(Const.User.NICKNAME, nickname)
        } else {
            SharedUtil.clear(Const.User.NICKNAME)
        }
    }

    fun saveAvatar(avatar: String?) {
        if (avatar != null && avatar.isNotBlank()) {
            SharedUtil.save(Const.User.AVATAR, avatar)
        } else {
            SharedUtil.clear(Const.User.AVATAR)
        }
    }

    fun saveBgImage(bgImage: String?) {
        if (bgImage != null && bgImage.isNotBlank()) {
            SharedUtil.save(Const.User.BG_IMAGE, bgImage)
        } else {
            SharedUtil.clear(Const.User.BG_IMAGE)
        }
    }

    fun saveDescription(description: String?) {
        if (description != null && description.isNotBlank()) {
            SharedUtil.save(Const.User.DESCRIPTION, description)
        } else {
            SharedUtil.clear(Const.User.DESCRIPTION)
        }
    }

}