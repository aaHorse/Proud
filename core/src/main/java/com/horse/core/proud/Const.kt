package com.horse.core.proud

/**
 * 所有全局通用常量管理类
 *
 * @author liliyuan
 * @since 2020年4月7日17:00:11
 * */
interface Const {

    interface Auth {
        companion object {

            /**
             * 登录的学号
             * */
            const val USER_ID = "u_d"

            //
            const val TOKEN = "t_k"

            /**
             * 登录类型：认证登录、游客登录
             * 认证登录：1
             * 游客登录：2
             * */
            const val LOGIN_TYPE = "l_t"
        }

    }

    interface User {
        companion object {

            const val NICKNAME = "nk"

            const val AVATAR = "ar"

            const val BG_IMAGE = "bi"

            const val DESCRIPTION = "de"
        }

    }

    /**
     * 发布任务、失物招领、物品租赁列表
     * */
    interface Item {
        companion object {

            const val POSITION_LATITUDE = "latitude"

            const val POSITION_LONGITUTE = "longitude"

        }

    }

}