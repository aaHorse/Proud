package com.horse.proud.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.horse.proud.R
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.rental.RentalFragment
import com.horse.proud.ui.task.TaskFragment
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_over_view_published.*

/**
 * 查看个人所有已发布的信息
 *
 * @author liliyuan
 * @since 2020年5月28日14:25:49
 * */
class OverViewPublishedActivity : BaseActivity() {

    /**
     * 发布信息类型列表
     * */
    lateinit var mGroupListView: QMUIGroupListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_over_view_published)
    }

    override fun setupViews() {
        setupToolbar()
        mGroupListView = published_type
        initGroupListView()
    }

    private fun initGroupListView(){
        val height = QMUIResHelper.getAttrDimen(this, com.qmuiteam.qmui.R.attr.qmui_list_item_height)
        val task = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "任务发布",
            "累计发布任务：8个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        val lost = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "失物招领",
            "累计发布失物招领：8个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        val rental = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "物品租赁",
            "累计发布物品租赁：8个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        QMUIGroupListView.newSection(this)
            .addItemView(task){v ->
                MainActivity.actionStart(this,1)
            }
            .addItemView(lost){v ->
                MainActivity.actionStart(this,2)
            }
            .addItemView(rental){v ->
                MainActivity.actionStart(this,3)
            }
            .addTo(mGroupListView)
    }

    companion object{

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                OverViewPublishedActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
