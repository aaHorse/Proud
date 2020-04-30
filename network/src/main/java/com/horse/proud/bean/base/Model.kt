package com.horse.proud.bean.base

import org.litepal.crud.LitePalSupport

/**
 * 所有网络通讯数据模型实体类的基类。
 *
 * @author liliyuan
 * @since 2020年4月23日14:04:03
 * */
abstract class Model : LitePalSupport(){

    /**
     * 获取当前实体类数据的id。比如，User 类就获取 userId，Comment 类就获取 commentId。
     * */
    abstract val modelId: Long

}