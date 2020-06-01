package com.horse.proud.data.model.login

import com.google.gson.annotations.SerializedName

/**
 * 图片文字识别
 *
 * @author liliyuan
 * @since 2020年6月1日06:07:09
 * */
class WordsResult {

    @SerializedName("log_id")
    var logId:Long = 0

    @SerializedName("words_result_num")
    var resultNum = 0

    @SerializedName("words_result")
    lateinit var resultList:ArrayList<Result>


    class Result{

        @SerializedName("location")
        lateinit var location:Location

        @SerializedName("words")
        lateinit var words:String

    }

    class Location{

        var left:Int = -1

        var top:Int = -1

        var width:Int = -1

        var height:Int = -1

    }

}