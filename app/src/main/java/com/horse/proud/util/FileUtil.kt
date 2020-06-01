package com.horse.proud.util

import android.content.Context
import java.io.File

/**
 * 临时图片
 *
 * @author liliyuan
 * @since 2020年6月1日09:36:23
 * */
class FileUtil {

    companion object{

        fun getSaveFile(context: Context): File? {
            return File(context.filesDir, "pic.jpg")
        }

    }

}