package com.horse.proud.util

import android.content.Context
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.OcrRequestParams
import com.baidu.ocr.sdk.model.OcrResponseResult
import java.io.File

/**
 * 调用百度识图的类
 *
 * @author liliyuan
 * @since 2020年6月1日10:41:06
 * */
class RecognizeService {

    interface ServiceListener{

        fun onResult(result:String)

    }

    companion object{

        fun recHandwriting(ctx: Context?, filePath: String?, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeHandwriting(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    error.message?.let { listener.onResult(it) }
                }
            })
        }

        fun recReceipt(ctx: Context?, filePath: String?, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            param.putParam("detect_direction", "true")
            OCR.getInstance(ctx).recognizeReceipt(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    error.message?.let { listener.onResult(it) }
                }
            })
        }

    }

}