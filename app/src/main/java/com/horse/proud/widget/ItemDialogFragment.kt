package com.horse.proud.widget

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import kotlinx.android.synthetic.main.bottom_navigation.view.*
import java.lang.IllegalStateException

/**
 * 自定义列表对话框
 *
 * @author liliyuan
 * @since 2020年5月28日09:08:21
 * */
class ItemDialogFragment:DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            val arrays = arrayOf("1","2","3")
            builder.setTitle("设置")
                .setItems(arrays,DialogInterface.OnClickListener{
                    dialog, which ->
                    showToast("点击了${which}")
                })
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

}