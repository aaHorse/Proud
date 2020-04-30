package com.horse.proud.ui.rental.adapter

import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.proud.R
import com.horse.proud.data.model.rental.RentalItem
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.rental.RentalFragment
import com.horse.proud.widget.SeeMoreView

/**
 * 物品租赁的 RecyclerView 适配器。
 *
 * @author liliyuan
 * @since 2020年4月26日15:24:38
 * */
class RentalAdapter(private val rentalFragment: RentalFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<RentalItem>(recyclerView, R.layout.item_task) {

    override fun fillData(helper: BGAViewHolderHelper, position: Int, rentalItem: RentalItem) {
        helper.setText(R.id.text, rentalItem.name)
        rentalItem.content?.let { helper.getView<SeeMoreView>(R.id.seemore).setText(it) }
        val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
        ninePhotoLayout.setDelegate(rentalFragment)
        ninePhotoLayout.data = rentalItem.photos
    }

}