package com.horse.core.proud

/**
 * 所有全局通用常量管理类
 *
 * @author liliyuan
 * @since 2020年4月7日17:00:11
 * */
interface Const {

    companion object{

        /**
         * 活动间跳转，标志位
         * */
        const val ACTIVITY_FLAG = "a_f"

        /**
         * 活动间跳转，内容标志
         * */
        const val ACTIVITY_CONTENT = "a_c"

        /**
         * 任务发布的类型
         * */
        val TASK_TYPE = arrayOf("取快递", "找联系方式", "兼职发布", "其他")

        /**
         * 失物招领发布的类型
         * */
        val LOST_AND_FOUND_TYPE = arrayOf("图书", "数码电子", "钥匙", "衣物", "学生证", "其他")

        /**
         * 物品租赁发布的类型
         * */
        val RENTAL_TYPE = arrayOf("车车", "数码电子", "图书", "衣物", "家电", "其他")

        /**
         * 发布任务的时间选择
         * */
        val TIME = arrayOf("1天内过期", "2天内过期", "3天内过期","5天内过期","不过期")

    }

    interface Auth {
        companion object {

            /**
             * 用户id
             * */
            const val USER_ID = "user_id"

            /**
             * 学号
             * */
            const val NUMBER = "number"

            /**
             * 姓名
             * */
            const val NAME = "name"


            /**
             * 手机号码
             * */
            const val PHONE = "phone"

            /**
             * 头像地址
             * */
            const val HEAD = "head"

            /**
             * 个性签名
             * */
            const val INFO = "info"

            /**
             * token
             * */
            const val TOKEN = "token"

            /**
             * 登录类型：认证登录、游客登录
             * 认证登录：1
             * 游客登录：0
             * -1:用户未选择身份
             * */
            const val LOGIN_TYPE = "l_t"

            /**
             * 认证登录
             * */
            const val COMFIR = 1

            /**
             * 游客登录
             * */
            const val VISITOR = 0

            /**
             * 未选择
             * */
            const val UNCHECK = -1


        }

    }

    interface User {
        companion object {

            const val NICKNAME = "nk"

            const val AVATAR = "ar"

            const val BG_IMAGE = "bi"

            const val NUMBER = "num"

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

    /**
     * 点赞事件
     * */
    interface Like{
        companion object{

            const val TASK = 0

            const val LOST = 1

            const val FOUND = 2

            const val RENTAL = 3

        }
    }

}