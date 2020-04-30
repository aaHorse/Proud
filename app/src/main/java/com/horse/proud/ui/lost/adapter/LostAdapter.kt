package com.horse.proud.ui.lost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.proud.R
import com.horse.proud.data.model.lost.LostItem
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.widget.SeeMoreView

/**
 * 任务列表的 RecyclerView 适配器。
 *
 * @author liliyuan
 * @since 2020年4月25日15:36:50
 * */
class LostAdapter(private val lostFragment: LostFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<LostItem>(recyclerView, R.layout.item_task) {

    override fun fillData(helper: BGAViewHolderHelper, position: Int, lostItem: LostItem) {
        helper.setText(R.id.text, lostItem.name)
        lostItem.content?.let { helper.getView<SeeMoreView>(R.id.seemore).setText(it) }
        val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
        ninePhotoLayout.setDelegate(lostFragment)
        ninePhotoLayout.data = lostItem.photos
    }

}