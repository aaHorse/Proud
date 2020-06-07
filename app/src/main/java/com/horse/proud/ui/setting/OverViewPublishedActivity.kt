package com.horse.proud.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.databinding.ActivityOverViewPublishedBinding
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.lost.LostFragment
import com.horse.proud.ui.rental.RentalFragment
import com.horse.proud.ui.task.TaskFragment
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_over_view_published.*
import kotlinx.android.synthetic.main.item_lost.*
import org.koin.android.ext.android.inject

/**
 * 查看个人所有已发布的信息
 *
 * @author liliyuan
 * @since 2020年5月28日14:25:49
 * */
class OverViewPublishedActivity : BaseActivity() {

    /**
     * 界面对应的userID
     * */
    private var userID = 0

    private val viewModelFactory by inject<OverViewPublishedViewModelFactory>()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(
        OverViewPublishedViewModel::class.java) }

    /**
     * 发布信息类型列表
     * */
    lateinit var mGroupListView: QMUIGroupListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = intent.getIntExtra(Const.ACTIVITY_CONTENT,0)
        val binding = DataBindingUtil.setContentView<ActivityOverViewPublishedBinding>(this,R.layout.activity_over_view_published)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

    override fun onStart() {
        super.onStart()
        viewModel.userMsg(userID)
        viewModel.getCount(userID)
    }

    override fun setupViews() {
        setupToolbar()
        mGroupListView = published_type
        observe()
    }

    private fun observe(){
        viewModel.done.observe(this, Observer {
            logWarn(TAG,"$it")
            if(it == 3){
                initGroupListView()
            }
        })
    }

    private fun initGroupListView(){
        val height = QMUIResHelper.getAttrDimen(this, com.qmuiteam.qmui.R.attr.qmui_list_item_height)
        val task = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "任务发布",
            "累计发布任务：${viewModel.taskCount}个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        val lost = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "失物招领",
            "累计发布失物招领：${viewModel.lostCount}个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        val rental = mGroupListView.createItemView(ContextCompat.getDrawable(this, R.drawable.sign),
            "物品租赁",
            "累计发布物品租赁：${viewModel.rentalCount}个",
            QMUICommonListItemView.VERTICAL,
            QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
            height)
        mGroupListView.removeAllViews()
        QMUIGroupListView.newSection(this)
            .addItemView(task){v ->
                MainActivity.actionStart(this,1,userID)
            }
            .addItemView(lost){v ->
                MainActivity.actionStart(this,2,userID)
            }
            .addItemView(rental){v ->
                MainActivity.actionStart(this,3,userID)
            }
            .addTo(mGroupListView)
    }

    companion object{

        private const val TAG = "OverViewPublishedActivity"

        fun actionStart(activity: Activity,userID:Int){
            val intent = Intent(activity, OverViewPublishedActivity::class.java)
            intent.putExtra(Const.ACTIVITY_CONTENT,userID)
            activity.startActivity(intent)
        }

    }

}
