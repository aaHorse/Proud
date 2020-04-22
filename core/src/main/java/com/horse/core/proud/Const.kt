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

            const val USER_ID = "u_d"

            const val TOKEN = "t_k"

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

    interface Feed {
        companion object {

            const val MAIN_PAGER_POSITION = "mpp"

            const val MAIN_LAST_USE_TIME = "mlut"

            const val MAIN_LAST_IGNORE_TIME = "mlit"
        }

    }

}